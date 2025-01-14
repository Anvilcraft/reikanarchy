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

public class SpawnerControllerSpawnEvent extends TileEntityEvent {
    public final Class entityClass;

    public SpawnerControllerSpawnEvent(TileEntity te, Class entity) {
        super(te);

        entityClass = entity;
    }
}
