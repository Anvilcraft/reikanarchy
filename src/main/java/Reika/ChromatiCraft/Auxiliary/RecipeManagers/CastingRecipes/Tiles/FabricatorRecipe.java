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

import Reika.ChromatiCraft.Auxiliary.ChromaStacks;
import Reika.ChromatiCraft.Auxiliary.RecipeManagers.CastingRecipe.PylonCastingRecipe;
import Reika.ChromatiCraft.Registry.CrystalElement;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class FabricatorRecipe extends PylonCastingRecipe {
    public FabricatorRecipe(ItemStack out, ItemStack main) {
        super(out, main);

        this.addAuxItem(ChromaStacks.focusDust, -2, 0);
        this.addAuxItem(ChromaStacks.focusDust, 2, 0);
        this.addAuxItem(ChromaStacks.focusDust, 0, -2);
        this.addAuxItem(ChromaStacks.energyCore, 0, 2);

        this.addAuxItem(ChromaStacks.bindingCrystal, -4, 0);
        this.addAuxItem(ChromaStacks.bindingCrystal, 4, 0);
        this.addAuxItem(ChromaStacks.bindingCrystal, 0, -4);
        this.addAuxItem(ChromaStacks.bindingCrystal, 0, 4);

        this.addAuxItem(ChromaStacks.chromaDust, -4, 2);
        this.addAuxItem(ChromaStacks.chromaDust, -4, -2);
        this.addAuxItem(ChromaStacks.chromaDust, 4, 2);
        this.addAuxItem(ChromaStacks.chromaDust, 4, -2);
        this.addAuxItem(ChromaStacks.chromaDust, 2, -4);
        this.addAuxItem(ChromaStacks.chromaDust, -2, -4);
        this.addAuxItem(ChromaStacks.chromaDust, 2, 4);
        this.addAuxItem(ChromaStacks.chromaDust, -2, 4);

        this.addAuxItem(ChromaStacks.auraDust, -4, -4);
        this.addAuxItem(ChromaStacks.auraDust, 4, -4);
        this.addAuxItem(ChromaStacks.auraDust, 4, 4);
        this.addAuxItem(ChromaStacks.auraDust, -4, 4);

        this.addAuxItem(Items.iron_ingot, -2, -2);
        this.addAuxItem(Items.iron_ingot, 2, -2);
        this.addAuxItem(Items.iron_ingot, 2, 2);
        this.addAuxItem(Items.iron_ingot, -2, 2);

        this.addAuraRequirement(CrystalElement.GRAY, 10000);
        this.addAuraRequirement(CrystalElement.BLUE, 2000);
        this.addAuraRequirement(CrystalElement.BROWN, 2000);
    }
}
