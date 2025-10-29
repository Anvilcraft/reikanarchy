package Reika.ChromatiCraft.API.Event;

import java.util.UUID;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.world.World;

public class LumenWireToggleEvent extends Event {
    public final World world;
    public final int x;
    public final int y;
    public final int z;

    public final UUID connectionID;
    public final boolean isEnabled;

    public LumenWireToggleEvent(World world, int x, int y, int z, UUID uid, boolean on) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        connectionID = uid;
        isEnabled = on;
    }
}
