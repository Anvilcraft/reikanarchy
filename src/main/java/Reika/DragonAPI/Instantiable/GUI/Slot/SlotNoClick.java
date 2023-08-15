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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/** Identical to Slot but disallows item insertion. */
public class SlotNoClick extends Slot {
    public final boolean allowInsertion;
    public final boolean allowExtraction;

    public SlotNoClick(IInventory ii, int id, int x, int y, boolean add, boolean take) {
        super(ii, id, x, y);
        allowInsertion = add;
        allowExtraction = take;
    }

    @Override
    public boolean isItemValid(ItemStack is) {
        return allowInsertion && super.isItemValid(is);
    }

    @Override
    public boolean canTakeStack(EntityPlayer ep) {
        return allowExtraction && super.canTakeStack(ep);
    }
}
