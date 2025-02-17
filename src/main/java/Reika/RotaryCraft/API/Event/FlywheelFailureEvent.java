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
import net.minecraft.tileentity.TileEntity;

public class FlywheelFailureEvent extends TileEntityEvent {
    public final float explosivePower;

    public FlywheelFailureEvent(TileEntity te, float power) {
        super(te);

        explosivePower = power;
    }
}
