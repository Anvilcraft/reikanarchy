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

public class PileDriverImpactEvent extends TileEntityEvent {
    public final int centerX;
    public final int centerY;
    public final int centerZ;

    public PileDriverImpactEvent(TileEntity te, int x, int y, int z) {
        super(te);

        centerX = x;
        centerY = y;
        centerZ = z;
    }
}
