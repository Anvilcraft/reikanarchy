/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.Instantiable.Rendering;

import Reika.DragonAPI.Auxiliary.ReikaSpriteSheets;
import Reika.DragonAPI.Base.DragonAPIMod;
import Reika.DragonAPI.Exception.RegistrationException;
import Reika.DragonAPI.Interfaces.Item.IndexedItemSprites;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

@SideOnly(Side.CLIENT)
public class ItemSpriteSheetRenderer implements IItemRenderer {
    protected final String spritesheet;
    private final DragonAPIMod mod;
    protected final Class modClass;

    public ItemSpriteSheetRenderer(ItemSpriteSheetRenderer ref) {
        this(ref.mod, ref.modClass, ref.spritesheet);
    }

    public ItemSpriteSheetRenderer(DragonAPIMod mod, Class root, String file) {
        if (file == null && this.getClass() == ItemSpriteSheetRenderer.class)
            throw new RegistrationException(
                mod, "You cannot have an item sheet renderer with no texture sheet!"
            );
        //this.spritesheet = ReikaSpriteSheets.setupTextures(root, file);
        //if (ReikaTextureHelper.isUsingDefaultTexturePack()) {
        this.mod = mod;
        modClass = root;
        spritesheet = file;
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(
        ItemRenderType type, ItemStack item, ItemRendererHelper helper
    ) {
        return helper != helper.ENTITY_ROTATION
            ? true
            : Minecraft.getMinecraft().gameSettings.fancyGraphics;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if (item == null)
            return;
        Item cls = item.getItem();
        if (cls instanceof IndexedItemSprites) {
            IndexedItemSprites iis = (IndexedItemSprites) cls;
            int index = iis.getItemSpriteIndex(item);
            ReikaSpriteSheets.renderItem(modClass, spritesheet, index, type, item, data);
        }
    }
}
