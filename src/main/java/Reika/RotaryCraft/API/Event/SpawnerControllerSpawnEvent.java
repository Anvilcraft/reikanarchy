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
