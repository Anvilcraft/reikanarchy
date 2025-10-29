package Reika.DragonAPI.Instantiable.Event;

import codechicken.nei.guihook.GuiContainerManager;
import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

@Cancelable
public class NEIRecipeCheckEvent extends Event {
    private final ItemStack item;
    public final GuiContainer gui;

    public NEIRecipeCheckEvent(GuiContainer gui) {
        this(gui, GuiContainerManager.getStackMouseOver(gui));
    }

    public NEIRecipeCheckEvent(GuiContainer gui, ItemStack item) {
        this.gui = gui;
        this.item = item;
    }

    public ItemStack getItem() {
        return item != null ? item.copy() : null;
    }
}
