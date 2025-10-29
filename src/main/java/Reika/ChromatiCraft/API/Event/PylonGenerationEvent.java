package Reika.ChromatiCraft.API.Event;

import java.util.Random;

import Reika.ChromatiCraft.API.CrystalElementAccessor.CrystalElementProxy;
import Reika.DragonAPI.Instantiable.Event.WorldGenEvent;
import net.minecraft.world.World;

/** Fired when a pylon is successfully generated. */
public class PylonGenerationEvent extends WorldGenEvent {
    /**
     * Whether or not the structure is damaged and thus the pylon is inactive and must be
     * repaired.
     */
    public final boolean isBroken;
    /** Pylon color */
    public final CrystalElementProxy color;

    public PylonGenerationEvent(
        World world, int x, int y, int z, Random r, boolean br, CrystalElementProxy e
    ) {
        super(world, x, y, z, r);
        isBroken = br;
        color = e;
    }
}
