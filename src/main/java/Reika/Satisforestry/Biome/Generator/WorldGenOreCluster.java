package Reika.Satisforestry.Biome.Generator;

import java.util.Random;

import Reika.DragonAPI.Instantiable.Data.Immutable.BlockKey;
import Reika.DragonAPI.Libraries.Java.ReikaRandomHelper;
import Reika.Satisforestry.Biome.BiomePinkForest;
import Reika.Satisforestry.Biome.DecoratorPinkForest;
import Reika.Satisforestry.Biome.DecoratorPinkForest.OreSpawnLocation;
import Reika.Satisforestry.Registry.SFOptions;
import Reika.Satisforestry.Satisforestry;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenOreCluster extends WorldGenerator {
    public WorldGenOreCluster() {}

    @Override
    public boolean generate(World world, Random rand, int x, int y, int z) {
        int r = 8;
        int r2 = 16;
        boolean flag = false;
        int n = (int) (5 * SFOptions.BORDERORE.getFloat());
        for (int i = 0; i < n; i++) {
            int dx = ReikaRandomHelper.getRandomPlusMinus(x, r, rand);
            int dz = ReikaRandomHelper.getRandomPlusMinus(z, r, rand);

            if (!Satisforestry.isPinkForest(world, dx, dz))
                continue;

            int edge = BiomePinkForest.getNearestBiomeEdge(world, dx, dz, r2);
            if (edge < 0)
                continue;
            float f = 1F - edge / (float) r2;

            if (rand.nextFloat() > f)
                continue;

            int dy = DecoratorPinkForest.getTrueTopAt(world, dx, dz) + 1;

            if (isReplaceable(world, dx, dy, dz)
                && !Satisforestry.pinkforest.isRoad(world, dx, dz)) {
                int size = (int) (1.25F + f * rand.nextFloat() * 1.5F);
                OreSpawnLocation.setRNG(rand);
                BlockKey ore = DecoratorPinkForest.generateOreClumpAt(
                    world,
                    dx,
                    dy,
                    dz,
                    rand,
                    OreSpawnLocation.BORDER,
                    size,
                    (w, c)
                        -> isReplaceable(w, c.xCoord, c.yCoord, c.zCoord)
                        && DecoratorPinkForest.getTrueTopAt(w, c.xCoord, c.zCoord)
                            >= dy - 4
                );
                if (ore != null) {
                    if (isReplaceable(world, dx, dy + 1, dz))
                        world.setBlock(dx, dy + 1, dz, ore.blockID, ore.metadata, 2);
                    flag = true;
                }
            }
        }

        return flag;
    }

    private static boolean isReplaceable(World world, int x, int y, int z) {
        return world.getBlock(x, y, z).isAir(world, x, y, z);
    }
}