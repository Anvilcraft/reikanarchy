/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.MeteorCraft;

import java.util.Random;

import Reika.DragonAPI.Instantiable.Data.BlockStruct.BlockArray;
import Reika.DragonAPI.Libraries.World.ReikaWorldHelper;
import Reika.DragonAPI.ModInteract.ReikaTwilightHelper;
import Reika.MeteorCraft.MeteorGenerator.MeteorType;
import Reika.MeteorCraft.Registry.MeteorOptions;
import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;

public class OldMeteorGenerator implements IWorldGenerator {
    @Override
    public void generate(
        Random random,
        int chunkX,
        int chunkZ,
        World world,
        IChunkProvider chunkGenerator,
        IChunkProvider chunkProvider
    ) {
        if (this.canGen(world)) {
            if (random.nextInt(100) == 0) {
                this.generate(random, world, chunkX * 16, chunkZ * 16);
            }
        }
    }

    private boolean canGen(World world) {
        if (!MeteorOptions.OLDGEN.getState())
            return false;
        int id = world.provider.dimensionId;
        if (id == -1 || id == 1 || world.provider.hasNoSky)
            return false;
        if (world.getWorldInfo().getTerrainType() == WorldType.FLAT)
            return false;
        return true;
    }

    private void generate(Random rand, World world, int chunkX, int chunkZ) {
        int x = chunkX + rand.nextInt(16);
        int z = chunkZ + rand.nextInt(16);

        if (ReikaWorldHelper.isVoidWorld(world, x, z))
            return;

        int y = this.getGenY(world, x, z, rand);
        if (y == -1)
            return;

        //ReikaJavaLibrary.pConsole(x+", "+y+", "+z);
        BlockArray gen = MeteorGenerator.instance.getMeteorBlockArray(world, x, y, z);
        double tries = 0;
        int h = gen.getSizeY() + 1;
        boolean solid = gen.isAtLeastXPercentSolid(world, 50);
        while (tries < h && !solid) {
            gen.offset(0, -1, 0);
            tries++;
            solid = gen.isAtLeastXPercentSolid(world, 50);
        }
        if (solid) {
            MeteorGenerator.instance.generate(world, x, y, z);
            //ReikaJavaLibrary.pConsole(x+", "+y+", "+z);
        }
    }

    private MeteorType getRandomType(Random rand) {
        if (rand.nextInt(10) == 0)
            return MeteorType.END;
        else if (rand.nextInt(5) == 0)
            return MeteorType.NETHERRACK;
        else
            return MeteorType.STONE;
    }

    private int getGenY(World world, int x, int z, Random rand) {
        if (world.provider.dimensionId == ReikaTwilightHelper.getDimensionID())
            return 5 + rand.nextInt(30);
        BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
        int max = world.provider.getAverageGroundLevel();
        if (max <= 0)
            max = 1;
        int y = 5 + rand.nextInt(max);
        int tries = 0;
        while (world.canBlockSeeTheSky(x, y + 1, z) && tries < 40) {
            y = 5 + rand.nextInt(max);
            tries++;
        }
        return world.canBlockSeeTheSky(x, y + 1, z) ? -1 : y;
    }
}
