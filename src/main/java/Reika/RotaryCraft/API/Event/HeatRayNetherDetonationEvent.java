package Reika.RotaryCraft.API.Event;

import Reika.DragonAPI.Instantiable.Event.Base.WorldPositionEvent;
import net.minecraft.world.World;

public class HeatRayNetherDetonationEvent extends WorldPositionEvent {
    public HeatRayNetherDetonationEvent(World world, int x, int y, int z) {
        super(world, x, y, z);
    }
}
