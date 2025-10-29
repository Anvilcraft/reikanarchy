package Reika.DragonAPI.Instantiable.Event;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.MinecraftForge;

public class DimensionSeedEvent extends Event {
    public final World world;
    public final long originalSeed;

    public long seed;

    public DimensionSeedEvent(World world) {
        this.world = world;
        originalSeed = world.getWorldInfo().getSeed();

        seed = originalSeed;
    }

    public static long fire(WorldProvider p) {
        DimensionSeedEvent evt = new DimensionSeedEvent(p.worldObj);
        if (!p.worldObj.isRemote) {
            MinecraftForge.EVENT_BUS.post(evt);
        }
        return evt.seed;
    }
}
