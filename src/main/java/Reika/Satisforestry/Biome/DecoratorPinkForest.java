/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.Satisforestry.Biome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import Reika.DragonAPI.ASM.DependentMethodStripper.ModDependent;
import Reika.DragonAPI.Instantiable.Data.Immutable.BlockKey;
import Reika.DragonAPI.Instantiable.Data.Immutable.Coordinate;
import Reika.DragonAPI.Instantiable.Data.WeightedRandom;
import Reika.DragonAPI.Instantiable.IO.ModLogger;
import Reika.DragonAPI.Instantiable.Worldgen.StackableBiomeDecorator;
import Reika.DragonAPI.Libraries.Java.ReikaJavaLibrary;
import Reika.DragonAPI.Libraries.Java.ReikaRandomHelper;
import Reika.DragonAPI.Libraries.World.ReikaBlockHelper;
import Reika.DragonAPI.Libraries.World.ReikaWorldHelper;
import Reika.DragonAPI.ModInteract.ItemHandlers.ChiselBlockHandler;
import Reika.DragonAPI.ModInteract.ItemHandlers.ThaumItemHelper;
import Reika.DragonAPI.ModList;
import Reika.Satisforestry.Biome.Biomewide.BiomewideFeatureGenerator;
import Reika.Satisforestry.Biome.Generator.WorldGenCaveFlora;
import Reika.Satisforestry.Biome.Generator.WorldGenCrashSite;
import Reika.Satisforestry.Biome.Generator.WorldGenFrackingNode;
import Reika.Satisforestry.Biome.Generator.WorldGenOreCluster;
import Reika.Satisforestry.Biome.Generator.WorldGenPoisonRocks;
import Reika.Satisforestry.Biome.Generator.WorldGenPonds;
import Reika.Satisforestry.Biome.Generator.WorldGenPowerSlugs;
import Reika.Satisforestry.Biome.Generator.WorldGenRedBamboo;
import Reika.Satisforestry.Blocks.BlockPowerSlug;
import Reika.Satisforestry.Blocks.BlockPowerSlug.TilePowerSlug;
import Reika.Satisforestry.Blocks.BlockTerrain.TerrainType;
import Reika.Satisforestry.Config.BiomeConfig;
import Reika.Satisforestry.Registry.SFBlocks;
import Reika.Satisforestry.SFThaumHandler;
import Reika.Satisforestry.Satisforestry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.INode;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;

public class DecoratorPinkForest extends StackableBiomeDecorator {
    private final WorldGenRedBamboo redBambooGenerator = new WorldGenRedBamboo();
    private final WorldGenCaveFlora caveFloraGenerator = new WorldGenCaveFlora();
    //private final WorldGenPinkRiver riverGenerator = new WorldGenPinkRiver();
    private final WorldGenPoisonRocks rockGenerator = new WorldGenPoisonRocks(false);
    private final WorldGenPonds pondGenerator = new WorldGenPonds(false);
    private final WorldGenOreCluster oreGenerator = new WorldGenOreCluster();
    private final WorldGenPowerSlugs slugGenerator = new WorldGenPowerSlugs();
    private final WorldGenFrackingNode frackingGenerator
        = new WorldGenFrackingNode(false);
    private final WorldGenCrashSite crashGenerator = new WorldGenCrashSite(false);

    //private int riverHeight;
    //private int glassHeight;

    private static final double RIVER_DEPTH = 5.5;
    private static final double RIVER_WATER_MAX_DEPTH = 3;

    private final HashMap<Coordinate, BiomeFootprint> biomeColumns = new HashMap();

    @ModDependent(ModList.THAUMCRAFT)
    private static WeightedRandom<Aspect> aspectTable;

    public DecoratorPinkForest() {
        super();
    }

    @ModDependent(ModList.THAUMCRAFT)
    public static void loadAspects() {
        aspectTable = new WeightedRandom();
        aspectTable.addEntry(Aspect.BEAST, 30);
        aspectTable.addEntry(Aspect.GREED, 20);
        aspectTable.addEntry(Aspect.LIFE, 10);
        aspectTable.addEntry(Aspect.MINE, 20);
        aspectTable.addEntry(Aspect.TREE, 50);
        aspectTable.addEntry(Aspect.POISON, 20);
        aspectTable.addEntry(SFThaumHandler.FICSIT, 50);
    }

    @Override
    protected void genDecorations(BiomeGenBase biome) {
        /*
        for (int i = 0; i < 16; i++) {
            for (int k = 0; k < 16; k++) {
                int dx = chunk_X+i;
                int dz = chunk_Z+k;
                int top = this.getTrueTopAt(currentWorld, dx, dz);
                if (currentWorld.getBiomeGenForCoords(dx, dz) == biome) {
                    //this.cleanColumn(currentWorld, dx, top, dz);
                    double river = PinkForestRiverShaper.instance.getIntensity(dx, dz);
                    if (river > 0) {
                        double avg = this.getAverageHeight(currentWorld, dx, dz, 12);
                        double yb = avg-RIVER_DEPTH;
                        double yRes = Math.min(top, river*yb+(1-river)*Math.min(top,
        avg)); if (yRes < avg && yRes < top) { int yf = (int)yRes; for (int y = yf+1; y <=
        top; y++) { Block b = Blocks.air; if (y-yf <= RIVER_WATER_MAX_DEPTH && y < avg) b
        = Blocks.water; currentWorld.setBlock(dx, y, dz, b);
                            }
                            currentWorld.setBlock(dx, yf, dz, Blocks.sand);
                            currentWorld.setBlock(dx, yf-1, dz, Blocks.dirt);
                        }
                    }
                }
            }
        }
         */

        /*
        if (ReikaWorldHelper.getNaturalGennedBiomeAt(currentWorld, chunk_X, chunk_Z) ==
        biome) { new WorldGenPinkRiver().generate(currentWorld, randomGenerator, chunk_X,
        0, chunk_Z); new WorldGenUraniumCave().generate(currentWorld, randomGenerator,
        chunk_X, 0, chunk_Z);
        }
         */

        Coordinate c = new Coordinate(chunk_X, 0, chunk_Z);
        BiomeFootprint at = biomeColumns.get(c);
        if (at == null) {
            if (ReikaWorldHelper.getNaturalGennedBiomeAt(currentWorld, chunk_X, chunk_Z)
                == biome) {
                BiomeFootprint bf = new BiomeFootprint();
                if (bf.calculate(currentWorld, chunk_X, chunk_Z)) {
                    for (Coordinate c2 : bf.getCoords()) {
                        biomeColumns.put(c2, bf);
                    }
                } else {
                    bf = null;
                }
                at = bf;
            }
        }
        if (at != null && at.sizeX() >= 96 && at.sizeZ() >= 96) {
            Vec3 center = at.getCenter();
            int x = MathHelper.floor_double(center.xCoord);
            int z = MathHelper.floor_double(center.zCoord);
            if (x >= chunk_X && z >= chunk_Z && x - chunk_X < 16 && z - chunk_Z < 16) {
                BiomewideFeatureGenerator.instance.generateUniqueCenterFeatures(
                    currentWorld, x, z, randomGenerator, at
                );
            }
        }

        int x = chunk_X + randomGenerator.nextInt(16) + 8;
        int z = chunk_Z + randomGenerator.nextInt(16) + 8;

        int top = currentWorld.getTopSolidOrLiquidBlock(x, z);

        if (!pondGenerator.generate(currentWorld, randomGenerator, x, top, z)) {
            if (!crashGenerator.generate(
                    currentWorld, randomGenerator, x, top, z
                )) //was randomGenerator.nextInt(16) > 0 ||
                if (!frackingGenerator.generate(currentWorld, randomGenerator, x, top, z))
                    oreGenerator.generate(currentWorld, randomGenerator, x, top, z);
        }

        super.genDecorations(biome);

        x = chunk_X + randomGenerator.nextInt(16) + 8;
        z = chunk_Z + randomGenerator.nextInt(16) + 8;

        rockGenerator.setFrequency(
            Satisforestry.pinkforest.getSubBiome(currentWorld, x, z)
        );

        top = getTrueTopAt(currentWorld, x, z);

        int d1 = randomGenerator.nextInt(3);
        if (chunk_X % (4 + d1) == chunk_Z % (3 - d1)) {
            if (rockGenerator.generate(currentWorld, randomGenerator, x, top, z)) {
                if (randomGenerator.nextInt(9) <= 1) {
                    int tier = 0;
                    if (randomGenerator.nextInt(3) > 0) {
                        tier = 1;
                        if (randomGenerator.nextInt(3) == 0)
                            tier = 2;
                    }
                    TilePowerSlug te = null;
                    int n = 0;
                    while (te == null && n <= 15) {
                        int dx = ReikaRandomHelper.getRandomPlusMinus(x, 2);
                        int dz = ReikaRandomHelper.getRandomPlusMinus(z, 2);
                        int dy = getTrueTopAt(currentWorld, dx, dz) + 1;
                        te = BlockPowerSlug.generatePowerSlugAt(
                            currentWorld, dx, dy, dz, randomGenerator, tier, true, 0, true
                        );
                        n++;
                    }
                }
            }
        }

        redBambooGenerator.generate(currentWorld, randomGenerator, chunk_X, chunk_Z);
        slugGenerator.generate(currentWorld, randomGenerator, chunk_X, chunk_Z);
        caveFloraGenerator.generate(currentWorld, randomGenerator, chunk_X, chunk_Z);

        if (ModList.THAUMCRAFT.isLoaded()) {
            if (randomGenerator.nextInt(12) == 0) {
                int dx = chunk_X + randomGenerator.nextInt(16) + 8;
                int dz = chunk_Z + randomGenerator.nextInt(16) + 8;
                int y = DecoratorPinkForest.getTrueTopAt(currentWorld, dx, dz) + 1
                    + randomGenerator.nextInt(3);
                if (currentWorld.isAirBlock(dx, y, dz)) {
                    currentWorld.setBlock(
                        dx, y, dz, ThaumItemHelper.BlockEntry.NODE.getBlock(), 0, 0
                    );
                    TileEntity te = currentWorld.getTileEntity(dx, y, dz);
                    if (te != null && te instanceof INode) {
                        NodeType nt = randomGenerator.nextInt(6) == 0 ? NodeType.UNSTABLE
                                                                      : NodeType.NORMAL;
                        NodeModifier nm = NodeModifier.PALE;
                        switch (randomGenerator.nextInt(15)) {
                            case 0:
                            case 1:
                            case 2:
                                nm = NodeModifier.FADING;
                                break;
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                                nm = NodeModifier.BRIGHT;
                                break;
                        }
                        AspectList al = new AspectList();
                        ArrayList<Aspect> li = new ArrayList(Aspect.getPrimalAspects());
                        int n = randomGenerator.nextInt(5) == 0
                            ? 1
                            : ReikaRandomHelper.getRandomBetween(2, 4, randomGenerator);
                        for (int i = 0; i < n; i++) {
                            Aspect a = li.remove(randomGenerator.nextInt(li.size()));
                            al.add(
                                a,
                                ReikaRandomHelper.getRandomBetween(
                                    15, 60, randomGenerator
                                )
                            );
                        }
                        n = randomGenerator.nextInt(4) == 0
                            ? 0
                            : ReikaRandomHelper.getRandomBetween(1, 2, randomGenerator);
                        for (int i = 0; i < n; i++) {
                            al.add(
                                aspectTable.getRandomEntry(),
                                ReikaRandomHelper.getRandomBetween(
                                    10, 30, randomGenerator
                                )
                            );
                        }
                        ((INode) te).setNodeType(nt);
                        ((INode) te).setNodeModifier(nm);
                        ((INode) te).setAspects(al);
                    }
                    currentWorld.markBlockForUpdate(x, y, z);
                    currentWorld.func_147451_t(dx, y, dz);
                }
            }
        }
        /*
        if (randomGenerator.nextInt(12) == 0) { //G/Y/P = 75%/20%/5%
            int tier = 0;
            if (randomGenerator.nextInt(4) == 0) {
                tier = 1;
                if (randomGenerator.nextInt(5) == 0)
                    tier = 2;
            }
            int dx = chunk_X + randomGenerator.nextInt(16) + 8;
            int dz = chunk_Z + randomGenerator.nextInt(16) + 8;
            int dy = getTrueTopAt(currentWorld, dx, dz)+1;
            TilePowerSlug te = BlockPowerSlug.generatePowerSlugAt(currentWorld, dx, dy,
        dz, tier); if (te != null) { switch(tier) { case 0:
                        te.setMobType(randomGenerator.nextInt(4) == 0 ?
        EntityCaveSpider.class : EntitySpider.class); break; case 1:
                        te.setSingleStrongEnemy(randomGenerator.nextInt(3) == 0 ?
        EntityEliteStinger.class : EntitySpider.class, 3); break; case 2:
                        te.setEnemyBoost(3);
                        te.setMobType(EntityEliteStinger.class);
                        break;
                }
            }
        }*/
    }

    /** Returns the coord of the ground */
    public static int getTrueTopAt(World currentWorld, int dx, int dz) {
        int top = currentWorld.getTopSolidOrLiquidBlock(dx, dz);
        Block at = currentWorld.getBlock(dx, top, dz);
        while (top > 0
               && (at == Blocks.air || !at.getMaterial().blocksMovement()
                   || at == Satisforestry.log || at == Satisforestry.leaves
                   || at == SFBlocks.BAMBOO.getBlockInstance()
                   || at.isWood(currentWorld, dx, top, dz)
                   || at.isLeaves(currentWorld, dx, top, dz)
                   || ReikaWorldHelper.softBlocks(currentWorld, dx, top, dz))) {
            top--;
            at = currentWorld.getBlock(dx, top, dz);
        }
        at = currentWorld.getBlock(dx, top + 1, dz);
        while (top < 255 && at == Blocks.glass) {
            top++;
            at = currentWorld.getBlock(dx, top + 1, dz);
        }
        return top;
    }

    private void cleanColumn(World world, int x, int top, int z) {
        for (int i = top; i >= top - 6; i--) {
            Block b = i == 0 ? Blocks.grass : Blocks.stone;
            Block at = world.getBlock(x, top - i, z);
            if (i > 0 && (at == Blocks.dirt || at == Blocks.grass))
                b = Blocks.dirt;
            if (at != b)
                world.setBlock(x, top - i, z, b);
        }
        /*
        if (river > 0) {
            /*
            riverHeight = -1;
            double avg = this.getAverageHeight(world, x, z, 15); //was 6 then 9
            int watermax = (int)(Math.min(avg-1.5, riverHeight));
            if (watermax > top) {
                for (int i = top+1; i <= watermax; i++) {
                    world.setBlock(x, i, z, Blocks.water);
                }
            }
            else {
                world.setBlock(x, top, z, Blocks.sand);
            }
         */

        /*
            if (!this.tryPlaceWaterAt(world, x, top+1, z)) {
                world.setBlock(x, top, z, Blocks.sand);
            }
         *//*
		}

		/*
		for (int h = 0; h < 10; h++) {
			if (world.getBlock(x, top+h, z) == Blocks.glass)
				world.setBlock(x, top+h, z, Blocks.air);
		}
		  */
    }

    /*
    private boolean tryPlaceWaterAt(World world, int x, int y, int z) {
        int r = 15;//12;
        ArrayList<ForgeDirection> open = new ArrayList();
        for (int i = 0; i < 4; i++) {
            ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i+2];
            for (int d = 1; d <= r; d++) {
                int dx = x+d*dir.offsetX;
                int dz = z+d*dir.offsetZ;
                Block at = world.getBlock(dx, y, dz);
                boolean soft = at != Blocks.sand && at != Blocks.stone && at !=
    Blocks.dirt && at != Blocks.clay;//ReikaWorldHelper.softBlocks(world, dx, y, dz); if
    (!soft) { break;
                }
                if (soft && d == r)
                    open.add(dir);
            }
        }
        boolean can = open.size() <= 1 || (open.size() == 2 &&
    !ReikaDirectionHelper.arePerpendicular(open.get(0), open.get(1))); if (can) {
            world.setBlock(x, y, z, Blocks.water);
            for (int d = 1; d <= r; d++) {
                for (ForgeDirection dir : open) {
                    int dy = y;
                    int dx = x+d*dir.offsetX;
                    int dz = z+d*dir.offsetZ;
                    int floor = dy-1;
                    while (ReikaWorldHelper.softBlocks(world, x, floor, z)) {
                        floor--;
                    }
                    if (world.getBlock(dx, floor, dz) == Blocks.clay) {
                        int max = floor+3;
                        for (int dy2 = floor+1; dy2 <= max; dy2++) {
                            world.setBlock(dx, dy2, dz, Blocks.water);
                        }
                    }
                }
            }
        }
        return can;
    }
     */

    @FunctionalInterface
    public static interface HeightValidityFunction {
        public boolean apply(World world, int x, int y, int z);
    }

    @FunctionalInterface
    public static interface OreValidityFunction {
        public boolean apply(World world, Coordinate c);
    }

    public static double getAverageHeight(World world, int x, int z, int r) {
        return getAverageHeight(world, x, z, r, null);
    }

    public static double
    getAverageHeight(World world, int x, int z, int r, HeightValidityFunction func) {
        double avg = 0;
        int n = 0;
        for (int i = -r; i <= r; i++) {
            for (int k = -r; k <= r; k++) {
                int dx = x + i;
                int dz = z + k;
                int top = getTrueTopAt(world, dx, dz);
                if (func != null && !func.apply(world, dx, top, dz))
                    continue;
                avg += top;
                n++;
            }
        }
        if (n > 0)
            avg /= n;
        return avg;
    }

    @Override
    protected ModLogger getLogger() {
        return Satisforestry.logger;
    }

    public static boolean isTerrain(World world, int x, int y, int z) {
        Block b = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        if (b == SFBlocks.TERRAIN.getBlockInstance())
            return TerrainType.list[meta].isTerrain();
        if (ModList.CHISEL.isLoaded() && ChiselBlockHandler.isWorldgenBlock(b, meta))
            return true;
        return b.isReplaceableOreGen(world, x, y, z, Blocks.stone)
            || b.isReplaceableOreGen(world, x, y, z, Blocks.sandstone)
            || b.getMaterial() == Material.ground || b.getMaterial() == Material.clay
            || b.getMaterial() == Material.sand
            || b.isReplaceableOreGen(world, x, y, z, Blocks.grass)
            || ReikaBlockHelper.isOre(b, meta);
    }

    public static BlockKey generateOreClumpAt(
        World world, int x, int y, int z, Random rand, OreSpawnLocation sec, int maxSize
    ) {
        return generateOreClumpAt(
            world, x, y, z, rand, sec, maxSize, (OreValidityFunction) null
        );
    }

    public static BlockKey generateOreClumpAt(
        World world,
        int x,
        int y,
        int z,
        Random rand,
        OreSpawnLocation sec,
        int maxSize,
        Set<Coordinate> set
    ) {
        return generateOreClumpAt(
            world, x, y, z, rand, sec, maxSize, (w, c) -> !set.contains(c.to2D())
        );
    }

    public static BlockKey generateOreClumpAt(
        World world,
        int x,
        int y,
        int z,
        Random rand,
        OreSpawnLocation sec,
        int maxSize,
        OreValidityFunction func
    ) {
        OreClusterType type = sec.getRandomOreSpawn();
        if (type == null)
            return null;
        int depth = rand.nextInt(2) + rand.nextInt(2) + rand.nextInt(2);
        depth *= type.sizeScale;
        depth = Math.min(depth, Math.min(maxSize, type.maxDepth));
        HashSet<Coordinate> place = new HashSet();
        HashSet<Coordinate> set = new HashSet();
        set.add(new Coordinate(x, y, z));
        for (int i = 0; i <= depth; i++) {
            HashSet<Coordinate> next = new HashSet();
            for (Coordinate c : set) {
                if (c.softBlock(world) && (func == null || func.apply(world, c))) {
                    place.add(c);
                    Coordinate c2 = c.offset(0, -1, 0);
                    while (c2.yCoord >= 0 && c2.softBlock(world)) {
                        place.add(c2);
                        c2 = c2.offset(0, -1, 0);
                    }
                    if (i < depth)
                        next.addAll(c.getAdjacentCoordinates());
                }
            }
            set = next;
        }

        BlockKey ore = type.getRandomBlock(rand);
        for (Coordinate c : place) {
            c.setBlock(world, ore.blockID, ore.metadata);
        }

        return ore;
    }

    public static class OreClusterType {
        public final String id;
        //public final BlockKey oreBlock;
        public final int spawnWeight;
        public final OreSpawnLocation spawnArea;

        private final ArrayList<BlockKey> blocks = new ArrayList();

        public float sizeScale = 1;
        public int maxDepth = 3;

        public OreClusterType(
            String s, OreSpawnLocation a, int w, ArrayList<BlockKey> li
        ) {
            id = s;
            spawnWeight = w;
            spawnArea = a;
            blocks.addAll(li);
        }

        public BlockKey getRandomBlock(Random rand) {
            return ReikaJavaLibrary.getRandomListEntry(rand, blocks);
        }
    }

    public static enum OreSpawnLocation {
        CAVE_ENTRY_TUNNEL,
        CAVE_MAIN_RING,
        CAVE_NODE_TUNNEL,
        CAVE_RESOURCE_ROOM,
        CAVE_RESOURCE_NODE,
        PONDS,
        BORDER,
        ;

        private final WeightedRandom<OreClusterType> oreSpawns = new WeightedRandom();

        private static final OreSpawnLocation[] list = values();

        public static void init() {
            for (OreSpawnLocation loc : list) {
                loc.oreSpawns.clear();
            }

            for (OreClusterType ore : BiomeConfig.instance.getOreTypes()) {
                ore.spawnArea.oreSpawns.addEntry(ore, ore.spawnWeight);
            }
        }

        public static void setRNG(Random rand) {
            for (OreSpawnLocation s : list) {
                s.oreSpawns.setRNG(rand);
            }
        }

        public OreClusterType getRandomOreSpawn() {
            return oreSpawns.getRandomEntry();
        }
    }
}
