package Reika.ChromatiCraft.Auxiliary.RecipeManagers.CastingRecipes.Blocks;

import Reika.ChromatiCraft.Auxiliary.RecipeManagers.CastingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class DecoCastingRecipe extends CastingRecipe {
    public DecoCastingRecipe(ItemStack out, IRecipe recipe) {
        super(out, recipe);
    }

    @Override
    public final boolean canGiveDoubleOutput() {
        return true;
    }

    @Override
    public final int getTypicalCraftedAmount() {
        return this.getTypicalTotalAmount() / this.getNumberProduced();
    }

    protected int getTypicalTotalAmount() {
        return this.getNumberProduced();
    }
}
