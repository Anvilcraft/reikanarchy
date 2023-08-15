/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.Libraries.IO;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;

import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.DragonOptions;
import Reika.DragonAPI.Exception.MisuseException;
import Reika.DragonAPI.IO.ReikaImageLoader;
import Reika.DragonAPI.IO.ReikaImageLoader.ImageEditor;
import Reika.DragonAPI.IO.ReikaTextureBinder;
import Reika.DragonAPI.Instantiable.Data.Maps.NestedMap;
import Reika.DragonAPI.Instantiable.Event.Client.TextureReloadEvent;
import Reika.DragonAPI.Instantiable.IO.RemoteSourcedAsset;
import Reika.DragonAPI.Libraries.Java.ReikaJavaLibrary;
import Reika.DragonAPI.Libraries.Java.ReikaObfuscationHelper;
import Reika.DragonAPI.Libraries.Java.ReikaObfuscationHelper.ReflectiveAccessExceptionHandler;
import Reika.DragonAPI.Libraries.Registry.ReikaDyeHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITickableTextureObject;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.ResourcePackFileNotFoundException;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ReikaTextureHelper {
    private static final HashMap<TextureIdentifier, Integer> textures = new HashMap();
    private static final NestedMap<Class, String, TextureIdentifier> textureKeys
        = new NestedMap();

    private static final HashMap<ReikaDyeHelper, Integer> colorOverrides = new HashMap();
    private static boolean noColorPacks = false;

    public static final TextureMap dummyTextureMap = new TextureMap(-1, "");
    private static TextureMap itemTextureMap;

    public static final ReikaTextureBinder binder = new ReikaTextureBinder();

    public static final ResourceLocation font
        = new ResourceLocation("textures/font/ascii.png");
    private static final ResourceLocation particle
        = new ResourceLocation("textures/particle/particles.png");
    private static final ResourceLocation gui
        = new ResourceLocation("textures/gui/widgets.png");
    private static final ResourceLocation hud
        = new ResourceLocation("textures/gui/icons.png");
    private static final ResourceLocation ench
        = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    static {
        ReikaObfuscationHelper.registerExceptionHandler(new ResourcePackErrorSilencer());
    }

    private static class ResourcePackErrorSilencer
        implements ReflectiveAccessExceptionHandler {
        @Override
        public boolean handleException(Exception e) {
            return !(e.getCause() instanceof ResourcePackFileNotFoundException);
        }
    }

    public static boolean reload() {
        return Keyboard.isKeyDown(Keyboard.KEY_F3) && Keyboard.isKeyDown(Keyboard.KEY_T);
    }

    public static void resetTexture(String tex) {
        textures.remove(createTextureID(tex));
    }

    public static void resetTexture(Class c, String tex) {
        textures.remove(createTextureID(c, tex));
    }

    public static int bindTexture(Class root, String tex) {
        return bindTexture(root, tex, null);
    }

    public static int bindTexture(RemoteSourcedAsset rem) {
        if (reload()) {
            textures.clear();
            colorOverrides.clear();
            MinecraftForge.EVENT_BUS.post(new TextureReloadEvent());
            return 0;
        } else {
            TextureIdentifier id = createTextureID(rem);
            Integer gl = textures.get(id);
            if (gl == null) {
                DragonAPICore.log("Loading dynamically sourced asset " + rem.path);
                try (InputStream data = rem.getData()) {
                    gl = bindRawDataTexture(data);
                } catch (IOException e) {
                    DragonAPICore.logError("Could not load asset!");
                    e.printStackTrace();
                    gl = binder.allocateAndSetupTexture(ReikaImageLoader.getMissingTex());
                }
                textures.put(id, gl);
            }
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, gl.intValue());
            return gl.intValue();
        }
    }

    public static int bindTexture(Class root, String tex, ImageEditor img) {
        if (reload()) {
            textures.clear();
            colorOverrides.clear();
            MinecraftForge.EVENT_BUS.post(new TextureReloadEvent());
            return 0;
        } else {
            if (root == null) {
                throw new MisuseException(
                    "You cannot fetch a render texture with reference to a null class!"
                );
            }
            String oldtex = tex;
            //String parent = root.getPackage().getName().replaceAll("\\.", "/")+"/";
            String s = root.getCanonicalName();
            String parent = s.substring(0, s.length() - root.getSimpleName().length() - 1)
                                .replaceAll("\\.", "/")
                + "/";
            if (tex.startsWith("/"))
                tex = tex.substring(1);
            String respath = tex.startsWith(parent) ? tex : parent + tex;

            TextureIdentifier id = createTextureID(root, tex);
            Integer gl = textures.get(id);
            if (gl == null) {
                ArrayList<IResourcePack> li = getCurrentResourcePacks();
                Collections.reverse(li); //because Mojang
                for (IResourcePack res : li) {
                    gl = bindPackTexture(root, respath, res, img);
                    if (gl != null) {
                        textures.put(id, gl);
                        DragonAPICore.log(
                            "Texture Pack " + res.getPackName()
                            + " contains an image for " + tex + "."
                        );
                        break;
                    }
                }
            }
            if (gl == null) {
                DragonAPICore.log(
                    "No texture packs contain an image for " + tex + ". Loading default."
                );
                gl = bindClassReferencedTexture(root, oldtex, img);
                textures.put(id, gl);
            }
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, gl.intValue());
            return gl.intValue();
        }
    }

    /** To disallow resource packs to change it */
    public static int bindFinalTexture(Class root, String tex) {
        TextureIdentifier id = createTextureID(root, tex);
        Integer gl = textures.get(id);
        if (gl == null) {
            gl = bindClassReferencedTexture(root, tex, null);
            textures.put(id, gl);
        }
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, gl.intValue());
        return gl.intValue();
    }

    private static Integer bindRawDataTexture(InputStream in) throws IOException {
        if (in == null) {
            DragonAPICore.logError("No image found!");
            return new Integer(
                binder.allocateAndSetupTexture(ReikaImageLoader.getMissingTex())
            );
        }
        BufferedImage img = ImageIO.read(in);
        if (img == null) {
            DragonAPICore.logError("No image found!");
            return new Integer(
                binder.allocateAndSetupTexture(ReikaImageLoader.getMissingTex())
            );
        } else {
            return new Integer(binder.allocateAndSetupTexture(img));
        }
    }

    private static Integer
    bindClassReferencedTexture(Class root, String tex, ImageEditor editor) {
        BufferedImage img = ReikaImageLoader.readImage(root, tex, editor);
        if (img == null) {
            DragonAPICore.logError("No image found for " + tex + "!");
            return new Integer(
                binder.allocateAndSetupTexture(ReikaImageLoader.getMissingTex())
            );
        } else {
            return new Integer(binder.allocateAndSetupTexture(img));
        }
    }

    public static int bindRawTexture(String tex) {
        TextureIdentifier id = createTextureID(tex);
        Integer gl = textures.get(id);
        if (gl == null) {
            BufferedImage img = ReikaImageLoader.readHardPathImage(tex);
            if (img == null) {
                DragonAPICore.logError("No image found for " + tex + "!");
                gl = new Integer(
                    binder.allocateAndSetupTexture(ReikaImageLoader.getMissingTex())
                );
                textures.put(id, gl);
            } else {
                gl = new Integer(binder.allocateAndSetupTexture(img));
                textures.put(id, gl);
            }
        }
        if (gl != null)
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, gl.intValue());
        return gl != null ? gl.intValue() : 0;
    }

    public static int bindRawTexture(BufferedImage tex, String ids) {
        TextureIdentifier id = createTextureID(ids);
        Integer gl = textures.get(id);
        if (gl == null) {
            gl = new Integer(binder.allocateAndSetupTexture(tex));
            textures.put(id, gl);
        }
        if (gl != null)
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, gl.intValue());
        return gl != null ? gl.intValue() : 0;
    }

    private static Integer
    bindPackTexture(Class root, String tex, IResourcePack res, ImageEditor editor) {
        BufferedImage img = ReikaImageLoader.getImageFromResourcePack(tex, res, editor);
        return img != null ? new Integer(binder.allocateAndSetupTexture(img)) : null;
    }

    private static TextureIdentifier createTextureID(Class c, String tex) {
        TextureIdentifier ref = textureKeys.get(c, tex);
        if (ref == null) {
            ref = new ClassRefTexture(c, tex);
            textureKeys.put(c, tex, ref);
        }
        return ref;
    }

    private static TextureIdentifier createTextureID(String tex) {
        TextureIdentifier ref = textureKeys.get(null, tex);
        if (ref == null) {
            ref = new PathTexture(tex);
            textureKeys.put(null, tex, ref);
        }
        return ref;
    }

    private static TextureIdentifier createTextureID(RemoteSourcedAsset rem) {
        return createTextureID(rem.reference, rem.path);
    }

    public static void bindTerrainTexture() {
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture
        );
    }

    public static void bindFontTexture() {
        Minecraft.getMinecraft().renderEngine.bindTexture(font);
    }

    public static void bindItemTexture() {
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture
        );
    }

    public static void bindGuiTexture() {
        Minecraft.getMinecraft().renderEngine.bindTexture(gui);
    }

    public static void bindEnchantmentTexture() {
        Minecraft.getMinecraft().renderEngine.bindTexture(ench);
    }

    public static void bindHUDTexture() {
        Minecraft.getMinecraft().renderEngine.bindTexture(hud);
    }

    public static TextureMap getItemTextureMap() {
        if (itemTextureMap == null) {
            try {
                TextureManager mgr = Minecraft.getMinecraft().renderEngine;
                Field f = mgr.getClass().getDeclaredField("listTickables");
                f.setAccessible(true);
                List<ITickableTextureObject> li
                    = (List<ITickableTextureObject>) f.get(mgr);
                for (ITickableTextureObject obj : li) {
                    if (obj instanceof TextureMap
                        && ((TextureMap) obj).getTextureType() == 1) {
                        itemTextureMap = (TextureMap) obj;
                        break;
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return itemTextureMap;
    }

    public static int getIconHeight() {
        return 16;
    }

    public static IIcon getMissingIcon() {
        TextureMap tex
            = (TextureMap) Minecraft.getMinecraft().getTextureManager().getTexture(
                TextureMap.locationBlocksTexture
            );
        return tex.getAtlasSprite("missingno");
    }

    /**
     * Overrides the standard ResourceLocation system. Unfortunately not yet functional.
     *//*
	public static void forceArmorTexturePath(String tex) {
		DragonAPICore.log("DRAGONAPI: Disabling ResourceLocation on armor texture "+tex);
		ForcedResource f = new ForcedResource(tex);
		Map map = getArmorTextureMappings();
		map.put(tex, f);
	}

	private static Map getArmorTextureMappings() {
		try {
			Class c = RenderBiped.class;
			Field f = ReikaObfuscationHelper.getField("field_110859_k");
			f.setAccessible(true);
			return (Map)f.get(null);
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not load the Armor Textures!");
		}
	}*/

    /**
     * Returns a list of the selected resource packs, in the order they appear in the
     * selection screen.
     */
    public static ArrayList<IResourcePack> getCurrentResourcePacks() {
        ArrayList<IResourcePack> packs = new ArrayList();
        List<ResourcePackRepository.Entry> li
            = Minecraft.getMinecraft().getResourcePackRepository().getRepositoryEntries();
        for (int i = 0; i < li.size(); i++) {
            ResourcePackRepository.Entry e = li.get(i);
            packs.add(e.getResourcePack());
        }
        return packs;
    }

    public static boolean isDefaultResourcePack() {
        return Minecraft.getMinecraft()
            .getResourcePackRepository()
            .getRepositoryEntries()
            .isEmpty();
    }

    public static IResourcePack getDefaultResourcePack() {
        return Minecraft.getMinecraft()
            .getResourcePackRepository()
            .rprDefaultResourcePack;
    }

    public static InputStream
    getStreamFromTexturePack(String path, AbstractResourcePack pack) {
        return (InputStream
        ) ReikaObfuscationHelper.invoke("getInputStreamByName", pack, path);
    }

    public static int getColorOverride(ReikaDyeHelper dye) {
        if (reload()) {
            colorOverrides.clear();
            noColorPacks = false;
        }
        Integer color = colorOverrides.get(dye);
        if (color == null && !noColorPacks) {
            initializeColorOverrides();
            color = colorOverrides.get(dye);
        }
        return color != null ? color.intValue() : dye.getDefaultColor();
    }

    private static void initializeColorOverrides() {
        ArrayList<IResourcePack> li = getCurrentResourcePacks();
        boolean loaded = false;
        for (int k = 0; k < li.size(); k++) {
            AbstractResourcePack pack = (AbstractResourcePack) li.get(k);
            String path = "Reika/DragonAPI/dyecolor.txt";
            try (InputStream in = getStreamFromTexturePack(path, pack)) {
                if (in != null) {
                    try (
                        BufferedReader p = new BufferedReader(new InputStreamReader(in))
                    ) {
                        for (int i = 0; i < 16; i++) {
                            String line = p.readLine();
                            String[] s = line.split(":");
                            int c = Color.decode(s[1]).getRGB();
                            Integer color = new Integer(c);
                            ReikaDyeHelper dye = ReikaDyeHelper.dyes[i];
                            colorOverrides.put(dye, color);
                        }
                    }
                    DragonAPICore.log(
                        "Found color override text file for texture pack "
                        + pack.getPackName() + "."
                    );
                    loaded = true;
                } else {
                    if (DragonOptions.LOGLOADING.getState())
                        DragonAPICore.log(
                            "No color override found for texture pack "
                            + pack.getPackName() + "."
                        );
                }
            } catch (Exception e) {
                DragonAPICore.logError(
                    "Error reading color override text file for texture pack "
                    + pack.getPackName() + "."
                );
                e.printStackTrace();
            }
        }
        if (!loaded) {
            DragonAPICore.log(
                "Could not find color override text file in any resource packs. Using defaults."
            );
            for (int i = 0; i < 16; i++) {
                ReikaDyeHelper dye = ReikaDyeHelper.dyes[i];
                int c = dye.getDefaultColor();
                Integer color = new Integer(c);
                colorOverrides.put(dye, color);
            }
            noColorPacks = true;
            return;
        }
    }

    public static void bindParticleTexture() {
        Minecraft.getMinecraft().renderEngine.bindTexture(particle);
    }

    public static int[][] getTextureData(IIcon ico, TextureMap map) {
        return map.getAtlasSprite(ico.getIconName()).getFrameTextureData(0);
    }

    public static void
    generateSpriteTextureData(String name, TextureMap map, int[][]... data) {
        TextureAtlasSprite tex = new GenSprite(name);
        tex.setFramesTextureData(ReikaJavaLibrary.makeListFrom(data));
        map.mapRegisteredSprites.put(name, tex);
    }

    private static class GenSprite extends TextureAtlasSprite {
        protected GenSprite(String s) {
            super(s);
        }
    }

    private static class PathTexture extends TextureIdentifier {
        private final String path;

        private PathTexture(String p) {
            path = p;
        }

        @Override
        public int hashCode() {
            return path.hashCode();
        }

        @Override
        public boolean equals(TextureIdentifier o) {
            return o instanceof PathTexture && ((PathTexture) o).path.equals(path);
        }
    }

    private static class ClassRefTexture extends TextureIdentifier {
        private final Class ref;
        private final String path;

        private ClassRefTexture(Class c, String p) {
            ref = c;
            path = p;
        }

        @Override
        public int hashCode() {
            return ref.hashCode() ^ path.hashCode();
        }

        @Override
        public boolean equals(TextureIdentifier o) {
            return o instanceof ClassRefTexture && ((ClassRefTexture) o).ref == ref
                && ((ClassRefTexture) o).path.equals(path);
        }
    }

    private static abstract class TextureIdentifier {
        @Override
        public abstract int hashCode();
        public abstract boolean equals(TextureIdentifier o);

        @Override
        public final boolean equals(Object o) {
            return o instanceof TextureIdentifier && this.equals(o);
        }
    }
}
