/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Auxiliary.RecipeManagers.CastingRecipes.Tools;

import Reika.ChromatiCraft.Auxiliary.ChromaStacks;
import Reika.ChromatiCraft.Auxiliary.RecipeManagers.CastingRecipe.MultiBlockCastingRecipe;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class TeleportWandRecipe extends MultiBlockCastingRecipe {
    public TeleportWandRecipe(ItemStack out, ItemStack main) {
        super(out, main);

        this.addAuxItem(Items.ender_pearl, -4, -2);
        this.addAuxItem(Items.ender_pearl, 4, -2);
        this.addAuxItem(Items.ender_pearl, 0, 4);

        this.addAuxItem(Items.diamond, -4, 2);
        this.addAuxItem(Items.diamond, 4, 2);
        this.addAuxItem(Items.diamond, 0, -4);

        this.addAuxItem("stickWood", -4, -4);
        this.addAuxItem("stickWood", 4, -4);
        this.addAuxItem("stickWood", -4, 4);
        this.addAuxItem("stickWood", 4, 4);

        this.addAuxItem(ChromaStacks.chromaDust, -2, 0);
        this.addAuxItem(ChromaStacks.chromaDust, 2, 0);
        this.addAuxItem(ChromaStacks.chromaDust, 0, 2);
        this.addAuxItem(ChromaStacks.chromaDust, 0, -2);

        this.addAuxItem(ChromaStacks.chromaIngot, -2, -2);
        this.addAuxItem(ChromaStacks.chromaIngot, 2, -2);
        this.addAuxItem(ChromaStacks.chromaIngot, -2, 2);
        this.addAuxItem(ChromaStacks.chromaIngot, 2, 2);

        this.addAuxItem(ChromaStacks.auraDust, -2, -4);
        this.addAuxItem(ChromaStacks.auraDust, 2, -4);
        this.addAuxItem(ChromaStacks.auraDust, -2, 4);
        this.addAuxItem(ChromaStacks.auraDust, 2, 4);
    }
}
