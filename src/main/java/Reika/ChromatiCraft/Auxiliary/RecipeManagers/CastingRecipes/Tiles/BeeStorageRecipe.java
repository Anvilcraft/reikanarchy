package Reika.ChromatiCraft.Auxiliary.RecipeManagers.CastingRecipes.Tiles;

import Reika.ChromatiCraft.Auxiliary.RecipeManagers.CastingRecipe.TempleCastingRecipe;
import Reika.ChromatiCraft.Registry.CrystalElement;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class BeeStorageRecipe extends TempleCastingRecipe {
    public BeeStorageRecipe(ItemStack out, IRecipe recipe) {
        super(out, recipe);

        this.addRune(CrystalElement.GREEN, -3, 0, -1);
        this.addRune(CrystalElement.LIGHTGRAY, 4, 0, 2);
    }
}
