package Reika.DragonAPI.Interfaces.TileEntity;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public interface AdjacentUpdateWatcher {
    public void onAdjacentUpdate(World world, int x, int y, int z, Block b);
}
