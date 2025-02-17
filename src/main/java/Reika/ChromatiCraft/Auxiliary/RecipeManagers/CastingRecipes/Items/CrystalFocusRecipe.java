/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Auxiliary.RecipeManagers.CastingRecipes.Items;

import Reika.ChromatiCraft.Auxiliary.RecipeManagers.CastingRecipe.MultiBlockCastingRecipe;
import Reika.ChromatiCraft.Registry.CrystalElement;
import net.minecraft.item.ItemStack;

public class CrystalFocusRecipe extends MultiBlockCastingRecipe {
    public CrystalFocusRecipe(ItemStack out, ItemStack main) {
        super(out, main);

        this.addAuxItem(this.getChargedShard(CrystalElement.BLUE), -2, 0);
        this.addAuxItem(this.getChargedShard(CrystalElement.BLUE), 2, 0);
        this.addAuxItem(this.getChargedShard(CrystalElement.YELLOW), 0, -2);
        this.addAuxItem(this.getChargedShard(CrystalElement.YELLOW), 0, 2);

        this.addAuxItem(this.getChargedShard(CrystalElement.PURPLE), -2, -2);
        this.addAuxItem(this.getChargedShard(CrystalElement.PURPLE), -2, 2);
        this.addAuxItem(this.getChargedShard(CrystalElement.PURPLE), 2, -2);
        this.addAuxItem(this.getChargedShard(CrystalElement.PURPLE), 2, 2);
    }

    @Override
    public boolean canBeSimpleAutomated() {
        return true;
    }

    @Override
    public boolean canGiveDoubleOutput() {
        return true;
    }

    @Override
    public int getTypicalCraftedAmount() {
        return 32;
    }
}
