package Reika.DragonAPI.Instantiable.Data.Collections;

import java.util.ArrayList;

import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.Instantiable.Event.AddRecipeEvent;
import Reika.DragonAPI.Libraries.ReikaRecipeHelper;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;

public class EventRecipeList extends ArrayList {
    private final ArrayList<IRecipe> vanillaRecipes = new ArrayList();

    @Override
    public boolean add(Object o) {
        AddRecipeEvent evt = new AddRecipeEvent((IRecipe) o);
        if (!MinecraftForge.EVENT_BUS.post(evt)) {
            if (AddRecipeEvent.isVanillaPass)
                vanillaRecipes.add(evt.recipe);
            super.add(o);
            return true;
        }
        return false;
    }

    public void filterVanillaRecipes() {
        AddRecipeEvent.isVanillaPass = true;
        DragonAPICore.log(
            "Refiltering " + this.size() + " vanilla recipes through event"
        );
        for (IRecipe r : vanillaRecipes) {
            AddRecipeEvent evt = new AddRecipeEvent(r);
            if (MinecraftForge.EVENT_BUS.post(evt)) {
                DragonAPICore.log("Removing " + ReikaRecipeHelper.toString(r));
                this.remove(r);
            }
        }
        DragonAPICore.log("Done filtering");
        AddRecipeEvent.isVanillaPass = false;
    }
}
