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

public class ShaftFailureEvent extends TileEntityEvent {
    public final boolean isSpeedFailure;
    public final int materialType;

    public ShaftFailureEvent(TileEntity te, boolean wasSpeed, int type) {
        super(te);

        isSpeedFailure = wasSpeed;
        materialType = type;
    }
}
