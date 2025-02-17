/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.GeoStrata.World;

import java.util.Random;

import Reika.DragonAPI.Instantiable.Math.LobulatedCurve;
import Reika.DragonAPI.Instantiable.Math.Noise.SimplexNoiseGenerator;
import Reika.DragonAPI.Interfaces.RetroactiveGenerator;
import Reika.DragonAPI.Libraries.Java.ReikaRandomHelper;
import Reika.DragonAPI.Libraries.MathSci.ReikaMathLibrary;
import Reika.DragonAPI.Libraries.World.ReikaBiomeHelper;
import Reika.DragonAPI.Libraries.World.ReikaWorldHelper;
import Reika.GeoStrata.Blocks.BlockCreepvine;
import Reika.GeoStrata.Blocks.BlockCreepvine.Pieces;
import Reika.GeoStrata.GeoStrata;
import Reika.GeoStrata.Registry.GeoBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public class CreepvineGenerator implements RetroactiveGenerator {
    public static final CreepvineGenerator instance = new CreepvineGenerator();

    private SimplexNoiseGenerator mainNoise;
    private long seed;

    private CreepvineGenerator() {}

    @Override
    public void generate(
        Random random,
        int chunkX,
        int chunkZ,
        World world,
        IChunkProvider chunkGenerator,
        IChunkProvider chunkProvider
    ) {
        chunkX *= 16;
        chunkZ *= 16;

        if (this.generateIn(world)) {
            this.setSeed(world);
            for (int i = 0; i < 64; i++) {
                int x = ReikaRandomHelper.getRandomBetween(chunkX, chunkX + 15, random);
                int z = ReikaRandomHelper.getRandomBetween(chunkZ, chunkZ + 15, random);
                int y = world.getTopSolidOrLiquidBlock(x, z);
                if (this.isValidLocation(world, x, y, z)) {
                    if (this.generate(world, x, y, z, random, 6, 12, 0.6F, true)) {
                        if (GeoStrata.kelpForest != null) {
                            int maxR = 7;
                            LobulatedCurve lb
                                = LobulatedCurve.fromMinMaxRadii(3, maxR, 5, true);
                            lb.generate(random);
                            for (int a = -maxR; a <= maxR; a++) {
                                for (int b = -maxR; b <= maxR; b++) {
                                    int dx = x + a;
                                    int dz = z + b;
                                    if (this.isValidLocation(world, dx, y, dz)) {
                                        double ang = Math.toDegrees(Math.atan2(b, a));
                                        if (ReikaMathLibrary.py3d(a, 0, b)
                                            <= lb.getRadius(ang))
                                            ReikaWorldHelper.setBiomeForXZ(
                                                world, dx, dz, GeoStrata.kelpForest, false
                                            );
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isValidLocation(World world, int x, int y, int z) {
        return ReikaBiomeHelper.isOcean(world.getBiomeGenForCoords(x, z))
            && instance.mainNoise.getValue(x, z) > 0.55;
    }

    public boolean generate(
        World world,
        int x,
        int y,
        int z,
        Random rand,
        int minHeight,
        int minHeightFertile,
        float fertileChance,
        boolean growFertile
    ) {
        int y1 = y;
        while (y1 < 256 && world.getBlock(x, y1, z) != Blocks.water) {
            y1++;
        }
        int y2 = y1;
        while (world.getBlock(x, y2 + 1, z) == Blocks.water) {
            y2++;
        }
        if (y1 < 60 && y2 < 64 && BlockCreepvine.canGrowOn(world, x, y1 - 1, z)) {
            int diff = y2 - y1;
            if (diff >= 8 && diff < 20) {
                int h = ReikaRandomHelper.getRandomBetween(
                    minHeight, Math.min(diff - 1, 16), rand
                );
                y2 = y1 + h;
                boolean fertile
                    = h >= minHeightFertile && rand.nextFloat() < fertileChance;
                world.setBlock(
                    x,
                    y1,
                    z,
                    GeoBlocks.CREEPVINE.getBlockInstance(),
                    Pieces.ROOT.ordinal(),
                    2
                );
                //ReikaJavaLibrary.pConsole(h+">"+fertile);
                if (fertile) {
                    int d1 = Math.min(5, h / 3);
                    int d2 = Math.min(4, h / 3);
                    int core = ReikaRandomHelper.getRandomBetween(y1 + d1, y2 - d2, rand);
                    for (int dy = y1 + 1; dy < core; dy++)
                        world.setBlock(
                            x,
                            dy,
                            z,
                            GeoBlocks.CREEPVINE.getBlockInstance(),
                            Pieces.STEM_EMPTY.ordinal(),
                            2
                        );
                    world.setBlock(
                        x,
                        core,
                        z,
                        GeoBlocks.CREEPVINE.getBlockInstance(),
                        (growFertile ? Pieces.CORE_5 : Pieces.CORE_EMPTY).ordinal(),
                        2
                    );
                    for (int dy = core + 1; dy <= y2; dy++)
                        world.setBlock(
                            x,
                            dy,
                            z,
                            GeoBlocks.CREEPVINE.getBlockInstance(),
                            Pieces.TOP.ordinal(),
                            2
                        );
                    world.func_147451_t(x, core, z);
                } else {
                    for (int dy = y1 + 1; dy < y2; dy++)
                        world.setBlock(
                            x,
                            dy,
                            z,
                            GeoBlocks.CREEPVINE.getBlockInstance(),
                            Pieces.STEM.ordinal(),
                            2
                        );
                    world.setBlock(
                        x,
                        y2,
                        z,
                        GeoBlocks.CREEPVINE.getBlockInstance(),
                        Pieces.TOP_YOUNG.ordinal(),
                        2
                    );
                }
                return true;
            }
        }
        return false;
    }

    private void setSeed(World world) {
        long s = world.getSeed();
        if (seed != s || mainNoise == null) {
            seed = s;
            Random rand = new Random(seed);
            rand.nextBoolean();
            mainNoise
                = (SimplexNoiseGenerator) new SimplexNoiseGenerator(seed).setFrequency(
                    0.016
                );
        }
    }

    private boolean generateIn(World world) {
        /*
        if (Math.abs(world.provider.dimensionId) <= 1)
            return true;
        if (world.provider.dimensionId == ExtraChromaIDs.DIMID.getValue())
            return true;
        if (ModList.MYSTCRAFT.isLoaded() && ReikaMystcraftHelper.isMystAge(world)) {
            if (!MystPages.Pages.PLANTS.existsInWorld(world)) {
                return false;
            }
        }*/
        return true;
    }

    @Override
    public boolean canGenerateAt(World world, int chunkX, int chunkZ) {
        return true;
    }

    @Override
    public String getIDString() {
        return "Creepvine";
    }
}
