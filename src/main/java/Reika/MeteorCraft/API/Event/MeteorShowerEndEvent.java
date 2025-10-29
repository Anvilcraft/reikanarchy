package Reika.MeteorCraft.API.Event;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.world.World;

public class MeteorShowerEndEvent extends Event {
    public final World world;

    public final long duration;

    public MeteorShowerEndEvent(World world, long dura) {
        this.world = world;
        duration = dura;
    }
}
