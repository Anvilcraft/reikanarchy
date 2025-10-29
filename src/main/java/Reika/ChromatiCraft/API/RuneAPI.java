package Reika.ChromatiCraft.API;

import Reika.ChromatiCraft.API.CrystalElementAccessor.CrystalElementProxy;
import net.minecraft.world.World;

public class RuneAPI {
    public static final RuneAPI instance = new RuneAPI();

    private RuneAPI() {}

    public boolean isRune(World world, int x, int y, int z, CrystalElementProxy e) {
        return world.getBlock(x, y, z).getClass().getSimpleName().equals(
                   "BlockCrystalRune"
               )
            && world.getBlockMetadata(x, y, z) == e.ordinal();
    }
}
