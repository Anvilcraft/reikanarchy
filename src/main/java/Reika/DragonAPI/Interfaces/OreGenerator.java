package Reika.DragonAPI.Interfaces;

import java.util.Random;

import Reika.DragonAPI.Interfaces.Registry.OreEnum;
import net.minecraft.world.World;

public interface OreGenerator {
    public void
    generateOre(OreEnum ore, Random random, World world, int chunkX, int chunkZ);
}
