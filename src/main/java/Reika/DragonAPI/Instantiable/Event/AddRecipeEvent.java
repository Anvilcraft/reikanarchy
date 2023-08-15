/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.Instantiable.Event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.item.crafting.IRecipe;

@Cancelable
public class AddRecipeEvent extends Event {
    public final IRecipe recipe;
    public static boolean isVanillaPass;

    public AddRecipeEvent(IRecipe r) {
        this(r, isVanillaPass);
    }

    public AddRecipeEvent(IRecipe r, boolean v) {
        recipe = r;
        isVanillaPass = v;
    }
}
