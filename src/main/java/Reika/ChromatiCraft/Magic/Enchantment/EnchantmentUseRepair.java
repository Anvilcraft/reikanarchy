/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Magic.Enchantment;

import Reika.ChromatiCraft.Base.ChromaticEnchantment;
import Reika.ChromatiCraft.Magic.Progression.ProgressStage;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class EnchantmentUseRepair extends ChromaticEnchantment {
    public EnchantmentUseRepair(int id) {
        super(id, EnumEnchantmentType.bow);
    }

    @Override
    public boolean canApply(ItemStack is) {
        return EnumEnchantmentType.weapon.canEnchantItem(is.getItem())
            || EnumEnchantmentType.bow.canEnchantItem(is.getItem());
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean isVisibleToPlayer(EntityPlayer ep, int level) {
        return level < 3 || ProgressStage.MAKECHROMA.isPlayerAtStage(ep);
    }

    public static int
    getRepairedDurability(ItemStack item, int level, float damage /*, UseType type*/) {
        float frac = (float) item.getItemDamage() / item.getMaxDamage();
        return (int
        ) Math.min(item.getItemDamage(), 1 + level * level * frac * damage / 2D);
    }

    /*
    public static enum UseType {
        MINE(),
        MELEE(),
        RANGED();
    }
     */
}
