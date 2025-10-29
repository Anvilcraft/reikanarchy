package Reika.RotaryCraft.API.Event;

import Reika.DragonAPI.Libraries.MathSci.ReikaMathLibrary;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.world.World;

public class RailgunImpactEvent extends Event {
    public final int blockX;
    public final int blockY;
    public final int blockZ;

    public final World world;

    public final int projectileMass;

    public RailgunImpactEvent(World world, int x, int y, int z, int tier) {
        this.world = world;
        blockX = x;
        blockY = y;
        blockZ = z;

        projectileMass = ReikaMathLibrary.intpow2(2, tier);
    }
}
