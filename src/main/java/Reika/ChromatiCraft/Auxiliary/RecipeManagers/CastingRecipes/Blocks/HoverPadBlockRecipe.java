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
import Reika.ChromatiCraft.Auxiliary.RecipeManagers.CastingRecipes.Tiles.HoverPadRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class HoverPadBlockRecipe extends TempleCastingRecipe {
    public HoverPadBlockRecipe(ItemStack out, IRecipe ir, HoverPadRecipe r) {
        super(out, ir);

        this.addRunes(r.getRunes());
    }

    @Override
    public int getNumberProduced() {
        return 3;
    }

    @Override
    public int getTypicalCraftedAmount() {
        return 25;
    }
}
