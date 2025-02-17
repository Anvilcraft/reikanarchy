/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.Items.Tools.Steel;

import java.util.List;

import Reika.DragonAPI.Libraries.Registry.ReikaItemHelper;
import Reika.RotaryCraft.Auxiliary.ItemStacks;
import Reika.RotaryCraft.Base.ItemRotaryArmor;
import Reika.RotaryCraft.RotaryCraft;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class ItemSteelArmor extends ItemRotaryArmor {
    public ItemSteelArmor(int tex, int render, int type) {
        super(RotaryCraft.HSLA, render, type, tex);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer ep, ItemStack is) {}

    /*
    @Override
    public String getArmorTexture(ItemStack is, Entity e, int slot, String nulll) {
        ItemRegistry item = ItemRegistry.getEntry(is);
        switch(item) {
        case STEELHELMET:
            return "/Reika/RotaryCraft/Textures/Misc/steel_1.png";
        case STEELLEGS:
            return "/Reika/RotaryCraft/Textures/Misc/steel_2.png";
        case STEELCHEST:
            return "/Reika/RotaryCraft/Textures/Misc/steel_1.png";
        case STEELBOOTS:
            return "/Reika/RotaryCraft/Textures/Misc/steel_1.png";
        default:
            return "";
        }
    }*/

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(
        Item id, CreativeTabs cr, List li
    ) //Adds the metadata blocks to the creative inventory
    {
        ItemStack is = new ItemStack(id, 1, 0);
        li.add(is);
    }

    @Override
    public boolean providesProtection() {
        return true;
    }

    @Override
    public boolean canBeDamaged() {
        return true;
    }

    @Override
    public double getDamageMultiplier(DamageSource src) {
        return 0.25;
    }

    @Override
    public boolean getIsRepairable(ItemStack tool, ItemStack item) {
        return tool.getItem() == this
            && ReikaItemHelper.matchStacks(item, ItemStacks.steelingot);
    }
}
