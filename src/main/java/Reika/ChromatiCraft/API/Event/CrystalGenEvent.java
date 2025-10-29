package Reika.ChromatiCraft.API.Event;

import java.util.Random;

import Reika.ChromatiCraft.API.CrystalElementAccessor;
import Reika.ChromatiCraft.API.CrystalElementAccessor.CrystalElementProxy;
import Reika.DragonAPI.Instantiable.Event.WorldGenEvent;
import net.minecraft.world.World;

/** Fired when a crystal is generated. */
public class CrystalGenEvent extends WorldGenEvent {
    /** Crystal color */
    public final CrystalElementProxy color;

    public CrystalGenEvent(World world, int x, int y, int z, Random random, int meta) {
        super(world, x, y, z, random);
        color = CrystalElementAccessor.getByIndex(meta % 16);
    }
}
