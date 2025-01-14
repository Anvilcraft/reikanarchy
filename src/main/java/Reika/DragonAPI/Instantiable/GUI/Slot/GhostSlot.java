/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.Instantiable.GUI.Slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * A slot that needs no inventory. Use it for things like ghost items (like diamond
 * pipes).
 */
public final class GhostSlot extends Slot {
    public GhostSlot(IInventory ii, int id, int x, int y) {
        super(ii, id, x, y);
    }

    public GhostSlot(int idx, int x, int y) {
        this(null, idx, x, y);
    }

    @Override
    public ItemStack getStack() {
        return null;
    }

    @Override
    public void putStack(ItemStack par1ItemStack) {}

    @Override
    public void onSlotChanged() {}

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public ItemStack decrStackSize(int par1) {
        return null;
    }
}
