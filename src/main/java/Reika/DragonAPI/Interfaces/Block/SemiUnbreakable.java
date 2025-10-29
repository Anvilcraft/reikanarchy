package Reika.DragonAPI.Interfaces.Block;

import net.minecraft.world.World;

public interface SemiUnbreakable {
    public boolean isUnbreakable(World world, int x, int y, int z, int meta);
}
