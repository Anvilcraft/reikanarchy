package Reika.ChromatiCraft.API.Event;

import java.util.Random;

import Reika.DragonAPI.Instantiable.Event.WorldGenEvent;
import net.minecraft.world.World;

/** Fired when a rainbow tree is generated. */
public class RainbowTreeEvent extends WorldGenEvent {
    public RainbowTreeEvent(World world, int x, int y, int z, Random r) {
        super(world, x, y, z, r);
    }
}
