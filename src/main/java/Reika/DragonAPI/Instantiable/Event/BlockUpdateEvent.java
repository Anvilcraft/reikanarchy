package Reika.DragonAPI.Instantiable.Event;

import Reika.DragonAPI.Instantiable.Event.Base.WorldPositionEvent;
import net.minecraft.world.World;

public class BlockUpdateEvent extends WorldPositionEvent {
    public final boolean renderOnly;

    public BlockUpdateEvent(World world, int x, int y, int z, boolean render) {
        super(world, x, y, z);
        renderOnly = render;
    }
}
