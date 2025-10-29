package Reika.ChromatiCraft.API.Interfaces;

import java.util.Collection;

import net.minecraft.item.ItemStack;

public interface CustomAdjacencyHandler {
    /** A basic description of the effect. */
    public String getDescription();

    /**
     * A list of the item/held versions of the blocks on which this effect applies. Used
     * for lexicon "show relevant items" display.
     */
    public Collection<ItemStack> getItems();
}
