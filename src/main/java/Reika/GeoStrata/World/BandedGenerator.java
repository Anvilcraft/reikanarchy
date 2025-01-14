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

import Reika.DragonAPI.Instantiable.Math.Noise.NoiseGeneratorBase;
import Reika.DragonAPI.Instantiable.Math.Noise.SimplexNoiseGenerator;
import Reika.GeoStrata.API.RockGenerationPatterns.RockGenerationPattern;
import Reika.GeoStrata.GeoStrata;
import Reika.GeoStrata.Registry.GeoOptions;
import Reika.GeoStrata.Registry.RockTypes;
import net.minecraft.world.World;

public class BandedGenerator implements RockGenerationPattern {
    private NoiseGeneratorBase bandOffsets;
    private static final int OFFSET_MARGIN = 16;

    private final WorldGenGeoRock[] generators
        = new WorldGenGeoRock[RockTypes.rockList.length];

    public BandedGenerator() {
        for (int i = 0; i < generators.length; i++) {
            generators[i] = new WorldGenGeoRock(
                this, RockTypes.rockList[i], RockGenerator.VEIN_SIZE
            );
            RockGenerator.instance.registerProfilingSubgenerator(
                RockTypes.rockList[i], this, generators[i]
            );
        }
    }

    @Override
    public void
    generateRockType(RockTypes geo, World world, Random random, int chunkX, int chunkZ) {
        double max
            = RockGenerator.BASE_GEN * geo.rarity * GeoOptions.getRockDensity() * 2;
        if (bandOffsets == null || bandOffsets.seed != world.getSeed()) {
            bandOffsets
                = new SimplexNoiseGenerator(world.getSeed()).setFrequency(1 / 64D);
        }
        //ReikaJavaLibrary.pConsole("Genning "+geo+" "+max+" times.");
        for (int i = 0; i < max; i++) {
            int posX = chunkX + random.nextInt(16);
            int posZ = chunkZ + random.nextInt(16);
            int posY = GeoStrata.config.getRockBand(geo)
                + (int) (OFFSET_MARGIN * bandOffsets.getValue(posX, posZ));
            //GeoStrata.logger.debug(geo.name()+":"+geo.canGenerateAt(world, posX, posY,
            //posZ, random));
            if (geo.canGenerateAt(world, posX, posY, posZ, random)) {
                //(new WorldGenMinable(geo.getID(RockShapes.SMOOTH), VEIN_SIZE,
                //Blocks.stone)).generate(world, random, posX, posY, posZ);
                generators[geo.ordinal()].generate(world, random, posX, posY, posZ);
                //GeoStrata.logger.log("Generating "+geo+" at "+posX+", "+posY+", "+posZ);
            }
        }
    }

    @Override
    public int getOrderingIndex() {
        return 0;
    }
}
