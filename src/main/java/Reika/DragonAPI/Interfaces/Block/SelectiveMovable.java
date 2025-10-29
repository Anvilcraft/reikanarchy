package Reika.DragonAPI.Interfaces.Block;

import net.minecraft.world.World;

/** Use this to declare a TileEntity-holding block as "not always safely movable". */
public interface SelectiveMovable {
    public boolean canMove(World world, int x, int y, int z);
}
