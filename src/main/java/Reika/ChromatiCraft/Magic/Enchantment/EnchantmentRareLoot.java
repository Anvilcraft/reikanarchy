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

public class EnchantmentRareLoot extends ChromaticEnchantment {
    public EnchantmentRareLoot(int id) {
        super(id, EnumEnchantmentType.weapon);
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public boolean canApply(ItemStack is) {
        return EnumEnchantmentType.weapon.canEnchantItem(is.getItem())
            || EnumEnchantmentType.bow.canEnchantItem(is.getItem());
    }

    @Override
    public boolean isVisibleToPlayer(EntityPlayer ep, int level) {
        switch (level) {
            case 1:
            case 2:
            default:
                return ProgressStage.KILLMOB.isPlayerAtStage(ep);
            case 3:
                return ProgressStage.CHROMA.isPlayerAtStage(ep);
            case 4:
                return ProgressStage.ALLOY.isPlayerAtStage(ep);
        }
    }
}
