package Reika.GeoStrata.API;

import java.util.Random;
import java.util.Set;

import Reika.DragonAPI.Instantiable.Data.Immutable.Coordinate;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.world.World;

public class ArcticSpireGenerationEvent extends Event {
    public final World world;
    public final int centerX;
    public final int baseY;
    public final int centerZ;
    public final Random chunkRand;

    public final Set<Coordinate> coreColumn;
    public final Set<Coordinate> underhangSnow;

    public ArcticSpireGenerationEvent(
        World w, int x, int y, int z, Random r, Set<Coordinate> cc, Set<Coordinate> s
    ) {
        world = w;
        centerX = x;
        baseY = y;
        centerZ = z;
        underhangSnow = s;
        coreColumn = cc;
        chunkRand = r;
    }
}
