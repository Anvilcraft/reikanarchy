/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Auxiliary;

import Reika.ChromatiCraft.Magic.Progression.ProgressStage;
import Reika.ChromatiCraft.Registry.ChromaItems;
import Reika.DragonAPI.Interfaces.PlayerSpecificTrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;

public class FragmentTrade extends MerchantRecipe implements PlayerSpecificTrade {
    public FragmentTrade() {
        super(ChromaItems.FRAGMENT.getStackOf(), new ItemStack(Items.emerald, 2, 0));
    }

    @Override
    public void incrementToolUses() {
        //No-op to prevent expiry
    }

    @Override
    public boolean isValid(EntityPlayer ep) {
        return ProgressStage.ANYSTRUCT.isPlayerAtStage(ep);
    }

    @Override
    public boolean hasSameIDsAs(MerchantRecipe mr) {
        return mr instanceof FragmentTrade;
    }

    @Override
    public boolean hasSameItemsAs(MerchantRecipe mr) {
        return mr instanceof FragmentTrade;
    }
}
