/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Auxiliary.RecipeManagers.CastingRecipes.Tiles;

import Reika.ChromatiCraft.Auxiliary.RecipeManagers.CastingRecipe.TempleCastingRecipe;
import Reika.ChromatiCraft.Registry.CrystalElement;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class ItemInserterRecipe extends TempleCastingRecipe {
    public ItemInserterRecipe(ItemStack out, IRecipe recipe) {
        super(out, recipe);

        this.addRune(CrystalElement.LIGHTGRAY, -3, 0, 4);
        this.addRune(CrystalElement.LIME, 1, 0, -3);
    }
}
