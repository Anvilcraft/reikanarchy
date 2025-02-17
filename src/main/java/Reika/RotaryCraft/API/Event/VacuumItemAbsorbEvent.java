/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.API.Event;

import Reika.DragonAPI.Instantiable.Event.TileEntityEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class VacuumItemAbsorbEvent extends TileEntityEvent {
    private final ItemStack item;

    public VacuumItemAbsorbEvent(TileEntity te, ItemStack item) {
        super(te);
        this.item = item;
    }

    public final ItemStack getItem() {
        return item != null ? item.copy() : null;
    }
}
