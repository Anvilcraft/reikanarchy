/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Auxiliary.RecipeManagers.CastingRecipes.Blocks;

import Reika.ChromatiCraft.Auxiliary.RecipeManagers.CastingRecipe.TempleCastingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class CastingInjectorFocusRecipe extends TempleCastingRecipe {
    public CastingInjectorFocusRecipe(
        ItemStack out, IRecipe recipe, TempleCastingRecipe... groups
    ) {
        super(out, recipe);

        for (TempleCastingRecipe cr : groups) {
            this.addRunes(cr.getRunes());
        }
    }

    @Override
    public int getNumberProduced() {
        return 1;
    }

    @Override
    public int getTypicalCraftedAmount() {
        return 8;
    }
}
