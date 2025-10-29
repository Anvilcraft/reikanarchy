package Reika.DragonAPI.Instantiable.Event;

import Reika.DragonAPI.Instantiable.Event.Base.WorldPositionEvent;
import cpw.mods.fml.common.eventhandler.Event.HasResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

@HasResult
public class IceFreezeEvent extends WorldPositionEvent {
    public final boolean needsEdge;

    public IceFreezeEvent(World world, int x, int y, int z, boolean edge) {
        super(world, x, y, z);
        needsEdge = edge;
    }

    public final boolean wouldFreezeNaturally() {
        return world.provider.canBlockFreeze(xCoord, yCoord, zCoord, needsEdge);
    }

    public static boolean fire(World world, int x, int y, int z, boolean edge) {
        IceFreezeEvent evt = new IceFreezeEvent(world, x, y, z, edge);
        MinecraftForge.EVENT_BUS.post(evt);
        switch (evt.getResult()) {
            case ALLOW:
                return true;
            case DEFAULT:
            default:
                return evt.wouldFreezeNaturally();
            case DENY:
                return false;
        }
    }

    public static boolean fire_IgnoreVanilla(World world, int x, int y, int z) {
        IceFreezeEvent evt = new IceFreezeEvent(world, x, y, z, false);
        MinecraftForge.EVENT_BUS.post(evt);
        return evt.getResult() != Result.DENY;
    }
}
