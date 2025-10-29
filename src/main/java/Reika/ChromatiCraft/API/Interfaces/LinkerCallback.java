package Reika.ChromatiCraft.API.Interfaces;

import net.minecraft.world.World;

public interface LinkerCallback {
    void linkTo(World world, int x, int y, int z);
}
