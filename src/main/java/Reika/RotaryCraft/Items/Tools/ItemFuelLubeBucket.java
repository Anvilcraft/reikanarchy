/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.Items.Tools;

import java.util.List;

import Reika.RotaryCraft.Base.ItemRotaryTool;
import Reika.RotaryCraft.Registry.ItemRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemFuelLubeBucket extends ItemRotaryTool {
    public ItemFuelLubeBucket(int tex) {
        super(tex);
        hasSubtypes = true;
        this.setContainerItem(Items.bucket);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(
        Item par1, CreativeTabs par2CreativeTabs, List par3List
    ) //Adds the metadata blocks to the creative inventory
    {
        for (int i = 0; i < ItemRegistry.BUCKET.getNumberMetadatas(); i++)
            par3List.add(new ItemStack(par1, 1, i));
    }

    @Override
    public String getUnlocalizedName(ItemStack is) {
        int d = is.getItemDamage();
        return super.getUnlocalizedName() + "." + String.valueOf(d);
    }

    @Override
    public int getItemSpriteIndex(ItemStack item) {
        return 104 + item.getItemDamage();
    }
}
