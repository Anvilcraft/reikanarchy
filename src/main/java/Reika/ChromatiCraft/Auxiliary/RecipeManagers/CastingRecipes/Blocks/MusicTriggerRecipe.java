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

import Reika.ChromatiCraft.Auxiliary.RecipeManagers.CastingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class MusicTriggerRecipe extends CastingRecipe {
    public MusicTriggerRecipe(ItemStack out, IRecipe recipe) {
        super(out, recipe);
    }

    @Override
    public int getTypicalCraftedAmount() {
        return 16;
    }
}
