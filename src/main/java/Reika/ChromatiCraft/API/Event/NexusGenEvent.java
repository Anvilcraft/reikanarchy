package Reika.ChromatiCraft.API.Event;

import java.util.Random;

import Reika.DragonAPI.Instantiable.Event.WorldGenEvent;
import net.minecraft.world.World;

/** Fired when a Warp Nexus is generated. */
public class NexusGenEvent extends WorldGenEvent {
    public NexusGenEvent(World world, int x, int y, int z, Random random) {
        super(world, x, y, z, random);
    }
}
