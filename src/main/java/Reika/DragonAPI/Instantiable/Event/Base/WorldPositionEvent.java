package Reika.DragonAPI.Instantiable.Event.Base;

import Reika.DragonAPI.Instantiable.Data.Immutable.WorldLocation;
import Reika.DragonAPI.Libraries.World.ReikaWorldHelper;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public abstract class WorldPositionEvent extends PositionEventBase {
    public final World world;

    public WorldPositionEvent(World world, int x, int y, int z) {
        super(world, x, y, z);
        this.world = world;
    }

    public final BiomeGenBase getBiome() {
        return isFakeWorld ? BiomeGenBase.ocean
                           : world.getBiomeGenForCoords(xCoord, zCoord);
    }

    public final void setBiome(BiomeGenBase b, boolean applyEnv) {
        if (isFakeWorld)
            return;
        ReikaWorldHelper.setBiomeForXZ(world, xCoord, zCoord, b, applyEnv);
    }

    public final boolean setBlock(Block b) {
        return world.setBlock(xCoord, yCoord, zCoord, b);
    }

    public final boolean setBlock(Block b, int meta, int flags) {
        if (isFakeWorld)
            return false;
        return world.setBlock(xCoord, yCoord, zCoord, b, meta, flags);
    }

    public final int dimensionID() {
        return world.provider != null ? world.provider.dimensionId : 0;
    }

    public final WorldLocation getLocation() {
        return new WorldLocation(world, xCoord, yCoord, zCoord);
    }
}
