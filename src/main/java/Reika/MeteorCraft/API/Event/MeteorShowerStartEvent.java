package Reika.MeteorCraft.API.Event;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.world.World;

public class MeteorShowerStartEvent extends Event {
    public final World world;

    /** World time at start */
    public final long startTime;

    public MeteorShowerStartEvent(World world) {
        this.world = world;
        startTime = world.getWorldTime();
    }
}
