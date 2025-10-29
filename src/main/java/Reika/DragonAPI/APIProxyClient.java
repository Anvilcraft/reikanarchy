package Reika.DragonAPI;

import Reika.DragonAPI.Auxiliary.DebugOverlay;
import Reika.DragonAPI.Auxiliary.Trackers.PlayerSpecificRenderer;
import Reika.DragonAPI.Extras.ReikaShader;
import Reika.DragonAPI.Extras.ThrottleableEffectRenderer;
import Reika.DragonAPI.IO.DelegateFontRenderer;
import Reika.DragonAPI.IO.DirectResourceManager;
import Reika.DragonAPI.IO.VanillaOnlyResourceManager;
import Reika.DragonAPI.Instantiable.EntityTumblingBlock;
import Reika.DragonAPI.Instantiable.Event.Client.ResourceReloadEvent;
import Reika.DragonAPI.Instantiable.Rendering.ParticleEngine;
import Reika.DragonAPI.Instantiable.Rendering.RenderTumblingBlock;
import Reika.DragonAPI.Libraries.IO.ReikaSoundHelper;
import codechicken.lib.gui.GuiDraw;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class APIProxyClient extends APIProxy {
    public static KeyBinding key_nbt;

    private static final SimpleReloadableResourceManager rm
        = (SimpleReloadableResourceManager) Minecraft.getMinecraft().getResourceManager();

    @Override
    public void registerSounds() {
        //MinecraftForge.EVENT_BUS.register(new SoundLoader(ReactorCraft.instance,
        //SoundRegistry.soundList));
    }

    @Override
    public void registerSidedHandlers() {
        //Minecraft mc = Minecraft.getMinecraft();
        //mc.mcResourceManager = new
        //CustomResourceManager((SimpleReloadableResourceManager)mc.mcResourceManager);

        MinecraftForge.EVENT_BUS.register(DebugOverlay.instance);
        ResourceReloadEvent.registerPre();

        //MinecraftForge.EVENT_BUS.register(PlayerModelRenderer.instance);
        //MinecraftForge.EVENT_BUS.register(CustomSoundHandler.instance);

        //key_nbt = new KeyBinding("TileEntity NBT Overlay", Keyboard.KEY_TAB,
        //"DragonAPI"); ClientRegistry.registerKeyBinding(key_nbt);

        RenderingRegistry.registerEntityRenderingHandler(
            EntityTumblingBlock.class, new RenderTumblingBlock()
        );
        ReikaShader.instance.register();
    }

    @Override
    public void registerSidedHandlersMain() {
        rm.registerReloadListener(DirectResourceManager.getInstance());
        rm.registerReloadListener(VanillaOnlyResourceManager.getInstance());
        ReikaSoundHelper.injectPaulscodeAccesses();
        Minecraft.getMinecraft().fontRenderer
            = new DelegateFontRenderer(Minecraft.getMinecraft().fontRenderer);
        if (ModList.NEI.isLoaded())
            GuiDraw.fontRenderer = Minecraft.getMinecraft().fontRenderer;
        Minecraft.getMinecraft().effectRenderer
            = new ThrottleableEffectRenderer(Minecraft.getMinecraft().effectRenderer);
        ParticleEngine.defaultCustomEngine.register();
    }

    @Override
    public void registerSidedHandlersGameLoaded() {
        PlayerSpecificRenderer.instance.registerIntercept();
        ResourceReloadEvent.registerPost();
    }

    @Override
    public World getClientWorld() {
        return FMLClientHandler.instance().getClient().theWorld;
    }

    @Override
    public void postLoad() {
        PlayerSpecificRenderer.instance.loadGlowFiles();
    }
}
