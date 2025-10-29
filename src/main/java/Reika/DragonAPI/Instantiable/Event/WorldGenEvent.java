package Reika.DragonAPI.Instantiable.Event;

import java.util.Random;

import Reika.DragonAPI.Instantiable.Event.Base.WorldPositionEvent;
import net.minecraft.world.World;

public abstract class WorldGenEvent extends WorldPositionEvent {
    private final Random rand;

    public WorldGenEvent(World world, int x, int y, int z, Random r) {
        super(world, x, y, z);
        rand = r;
    }

    public final int getRandomInt(int n) {
        return rand.nextInt(n);
    }

    public final float getRandomFloat() {
        return rand.nextFloat();
    }

    public final double getRandomDouble() {
        return rand.nextDouble();
    }
}
