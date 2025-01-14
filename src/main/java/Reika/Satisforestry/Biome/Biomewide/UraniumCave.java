package Reika.Satisforestry.Biome.Biomewide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import Reika.DragonAPI.Exception.InstallationException;
import Reika.DragonAPI.Exception.WTFException;
import Reika.DragonAPI.Instantiable.Data.BlockStruct.FilledBlockArray;
import Reika.DragonAPI.Instantiable.Data.Immutable.BlockKey;
import Reika.DragonAPI.Instantiable.Data.Immutable.Coordinate;
import Reika.DragonAPI.Instantiable.Data.Immutable.DecimalPosition;
import Reika.DragonAPI.Instantiable.Data.WeightedRandom;
import Reika.DragonAPI.Instantiable.Effects.LightningBolt;
import Reika.DragonAPI.Instantiable.Math.Noise.SimplexNoiseGenerator;
import Reika.DragonAPI.Instantiable.Math.Spline;
import Reika.DragonAPI.Instantiable.Math.Spline.BasicSplinePoint;
import Reika.DragonAPI.Instantiable.Math.Spline.SplineType;
import Reika.DragonAPI.Libraries.Java.ReikaJavaLibrary;
import Reika.DragonAPI.Libraries.Java.ReikaRandomHelper;
import Reika.DragonAPI.Libraries.MathSci.ReikaMathLibrary;
import Reika.DragonAPI.Libraries.MathSci.ReikaVectorHelper;
import Reika.DragonAPI.Libraries.ReikaNBTHelper.NBTTypes;
import Reika.DragonAPI.Libraries.World.ReikaWorldHelper;
import Reika.Satisforestry.Biome.DecoratorPinkForest;
import Reika.Satisforestry.Biome.DecoratorPinkForest.OreClusterType;
import Reika.Satisforestry.Biome.DecoratorPinkForest.OreSpawnLocation;
import Reika.Satisforestry.Blocks.BlockCaveSpawner.TileCaveSpawner;
import Reika.Satisforestry.Blocks.BlockDecoration.DecorationType;
import Reika.Satisforestry.Blocks.BlockGasEmitter.TileGasVent;
import Reika.Satisforestry.Blocks.BlockPinkGrass.GrassTypes;
import Reika.Satisforestry.Blocks.BlockResourceNode.TileResourceNode;
import Reika.Satisforestry.Blocks.BlockTerrain.TerrainType;
import Reika.Satisforestry.Entity.EntityEliteStinger;
import Reika.Satisforestry.Miner.MinerStructure;
import Reika.Satisforestry.Registry.SFBlocks;
import Reika.Satisforestry.Satisforestry;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraftforge.common.util.ForgeDirection;

public class UraniumCave {
    public static final UraniumCave instance = new UraniumCave();

    private final WeightedRandom<SpawnListEntry> caveSpawns = new WeightedRandom();

    private UraniumCave() {
        List<SpawnListEntry> li
            = Satisforestry.pinkforest.getSpawnableList(EnumCreatureType.monster);
        for (SpawnListEntry e : li) {
            if (e == null) // because some mods do stupid shit
                continue;
            if (EntitySpider.class.isAssignableFrom(e.entityClass)
                && e.entityClass != EntityEliteStinger.class) {
                caveSpawns.addEntry(e, e.itemWeight);
            }
        }
        if (caveSpawns.isEmpty()) {
            if (EntityList.classToStringMapping.containsKey(EntitySpider.class))
                throw new WTFException(
                    "Some mod removed the entries from the Pink Forest spawn list?!", true
                );
            else
                throw new InstallationException(
                    Satisforestry.instance,
                    "You cannot use Satisforestry without a spider type existing ingame!"
                );
        }
    }

    public CentralCave generate(
        World world,
        Random rand,
        int x,
        int z,
        Collection<Coordinate> rivers,
        CachedCave cache
    ) {
        caveSpawns.setRNG(rand);
        OreSpawnLocation.setRNG(rand);

        CentralCave cc = cache != null
            ? cache.reconstruct()
            : new CentralCave(x, this.getRandomY(world, x, z, rand), z);

        Satisforestry.logger.log(
            "Generating biome cave @ " + cc.center + ", from rivers " + rivers
            + " and cache " + cache
        );

        cc.calculate(world, rand);

        Collection<Tunnel> tunnels = new ArrayList();
        Vec3 avg = Vec3.createVectorHelper(0, 0, 0);
        double d
            = cc.outerCircleRadius * ReikaRandomHelper.getRandomBetween(1.35, 1.75, rand);
        CachedTunnel nodeTunnelCache = null;
        if (cache != null) {
            for (CachedTunnel ct : cache.tunnels.values()) {
                if (ct.isEdge) {
                    Tunnel add = new Tunnel(cc, ct.endpoint, ct.run, ct.angle);
                    add.setBolt(ct.bolt);
                    tunnels.add(add);
                    avg.xCoord += add.startingDX;
                    avg.zCoord += add.startingDZ;
                } else {
                    nodeTunnelCache = ct;
                }
            }
        } else {
            for (Coordinate c : rivers) {
                if (tunnels.size() >= 5)
                    continue;
                double dx = c.xCoord - cc.center.xCoord;
                double dz = c.zCoord - cc.center.zCoord;
                double dd = ReikaMathLibrary.py3d(dx, 0, dz);
                double dr = 9;
                Coordinate endpoint
                    = c.offset((int) (dx / dd * dr), 0, (int) (dz / dd * dr));
                double rootAngle = (Math.toDegrees(Math.atan2(
                                        endpoint.yCoord - cc.center.yCoord,
                                        endpoint.xCoord - cc.center.xCoord
                                    )) % 360
                                    + 360)
                    % 360;
                double nearestLow = -99999;
                double nearestHigh = 99999;
                boolean flag = false;
                for (Tunnel t : tunnels) {
                    double da = (Math.abs(t.startingAngle - rootAngle) % 360 + 360) % 360;
                    if (rootAngle - da > nearestLow)
                        nearestLow = t.startingAngle;
                    if (rootAngle + da < nearestHigh)
                        nearestHigh = t.startingAngle;
                    flag |= da < 30;
                }
                if (flag) {
                    if (nearestHigh - nearestLow < 45)
                        continue;
                    rootAngle = (nearestHigh + nearestLow) / 2D;
                }
                Tunnel add = new Tunnel(cc, endpoint, d, rootAngle);
                tunnels.add(add);
                avg.xCoord += add.startingDX;
                avg.zCoord += add.startingDZ;
            }
        }
        //avg.xCoord /= tunnels.size();
        //avg.zCoord /= tunnels.size();
        avg = avg.normalize();

        HashSet<Coordinate> carveSet = new HashSet();
        carveSet.addAll(cc.carve.keySet());

        for (Tunnel t : tunnels) {
            t.calculate(world, rand);
            carveSet.addAll(t.carve.keySet());
        }

        double dr = cc.outerCircleRadius * 1.5 + 12;
        ResourceNodeRoom rm = cache != null
            ? cache.reconstructNode(cc, cache.nodeTile)
            : new ResourceNodeRoom(
                cc, cc.center.offset(-avg.xCoord * dr, 0, -avg.zCoord * dr)
            );
        rm.calculate(world, rand);

        carveSet.addAll(rm.carve.keySet());

        Tunnel path;
        if (nodeTunnelCache != null) {
            path = new Tunnel(
                cc, nodeTunnelCache.endpoint, nodeTunnelCache.run, nodeTunnelCache.angle
            );
            path.setBolt(nodeTunnelCache.bolt);
        } else {
            path = new Tunnel(
                cc,
                new Coordinate(rm.center),
                cc.outerCircleRadius,
                Math.toDegrees(Math.atan2(-avg.xCoord, -avg.zCoord)),
                2
            );
        }
        path.targetY = path.endpoint.yCoord;
        path.isToBiomeEdge = false;
        path.calculate(world, rand);
        cc.nodeRoom = rm;

        carveSet.addAll(path.carve.keySet());
        tunnels.add(path);

        cc.generate(world, rand);

        for (Tunnel t : tunnels) {
            t.generate(world, rand);
        }

        rm.generate(world, rand);

        HashSet<Coordinate> flatFloor = new HashSet(cc.footprint.keySet());
        HashSet<Coordinate> pits = new HashSet();
        HashMap<Coordinate, Integer> remainingFloor = new HashMap();
        SimplexNoiseGenerator floorLevel
            = (SimplexNoiseGenerator) new SimplexNoiseGenerator(rand.nextLong())
                  .setFrequency(1 / 15D);
        SimplexNoiseGenerator holes
            = (SimplexNoiseGenerator) new SimplexNoiseGenerator(rand.nextLong())
                  .setFrequency(1 / 6D);

        for (Entry<Coordinate, Integer> e : cc.footprint.entrySet()) {
            Coordinate c = e.getKey();
            int yat = e.getValue() - 1;
            int h = (int) Math.round(ReikaMathLibrary.normalizeToBounds(
                floorLevel.getValue(c.xCoord, c.zCoord), 4, 7
            )); //was 3,6
            int floor = cc.lowestFloor - 1 - h;
            boolean hole = c.setY(yat + 1).isEmpty(world)
                && Math.abs(holes.getValue(c.xCoord, c.zCoord))
                    >= 0.4; //rand.nextInt(5) == 0;
            for (int i = hole ? 0 : 1; i < h; i++) {
                Coordinate c2 = c.setY(yat - i);
                if (i == 0)
                    c2.setBlock(
                        world,
                        SFBlocks.DECORATION.getBlockInstance(),
                        DecorationType.TENDRILS.ordinal()
                    );
                else
                    c2.setBlock(world, Blocks.air);
                carveSet.add(c2);
            }
            if (hole) {
                pits.add(c);
                flatFloor.remove(c);
                flatFloor.remove(c.offset(1, 0, 0));
                flatFloor.remove(c.offset(-1, 0, 0));
                flatFloor.remove(c.offset(0, 0, 1));
                flatFloor.remove(c.offset(0, 0, -1));
            } else {
                remainingFloor.put(c, e.getValue());
            }
            if (rand.nextInt(36) == 0) {
                world.setBlock(
                    c.xCoord,
                    floor,
                    c.zCoord,
                    SFBlocks.GASEMITTER.getBlockInstance(),
                    1,
                    2
                );
                TileGasVent te
                    = (TileGasVent) world.getTileEntity(c.xCoord, floor, c.zCoord);
                te.activeRadius = 5.5;
                te.activeHeight = 3.5;
                te.yOffset = 0;
            }
        }

        this.generateCasing(world, rand, carveSet);

        rm.generateResourceNode(world, rand);

        this.generateDecorations(world, rand, carveSet, remainingFloor, pits, cc);

        int ores = 9;
        int gen = 0;
        int tries = 0;
        while (gen < ores && tries < ores * 3) {
            tries++;
            Coordinate c = ReikaJavaLibrary.getRandomCollectionEntry(rand, flatFloor);
            c = c.setY(remainingFloor.get(c));
            while (c.offset(0, -1, 0).softBlock(world))
                c = c.offset(0, -1, 0);
            if (c.yCoord + 1 < cc.footprint.get(c.to2D()))
                continue;
            if (!c.isEmpty(world))
                continue;
            BlockKey prev = BlockKey.getAt(world, c.xCoord, c.yCoord, c.zCoord);
            BlockKey ore = DecoratorPinkForest.generateOreClumpAt(
                world,
                c.xCoord,
                c.yCoord,
                c.zCoord,
                rand,
                OreSpawnLocation.CAVE_MAIN_RING,
                4,
                pits
            );
            if (ore != null) {
                gen++;
                //ReikaJavaLibrary.pConsole("Generating ore clump "+ore.id+" @ "+c+" over
                //"+prev);

                TileCaveSpawner lgc
                    = this.generateSpawnerAt(world, c.xCoord, c.yCoord - 1, c.zCoord);
                this.setSpawnParameters(lgc, rand, 2, 10, 8); //ar was 8
                lgc.setInertTimeout(6000); //was 12 then 20, then 300
            }
        }

        for (Entry<DecimalPosition, Integer> e : rm.disks.entrySet()) {
            DecimalPosition p = e.getKey();
            int px = MathHelper.floor_double(p.xCoord);
            int py = MathHelper.floor_double(p.yCoord);
            int pz = MathHelper.floor_double(p.zCoord);
            DecoratorPinkForest.generateOreClumpAt(
                world,
                px,
                py - 1,
                pz,
                rand,
                OreSpawnLocation.CAVE_RESOURCE_ROOM,
                3,
                rm.nodeClearArea
            );
        }
        OreClusterType spike = OreSpawnLocation.CAVE_RESOURCE_NODE.getRandomOreSpawn();
        if (spike != null) {
            rm.generateOreSpike(world, rand, spike.getRandomBlock(rand));
        }

        rm.crackBlocks(world);

        for (int i = 0; i < 6; i++) {
            Coordinate c
                = ReikaJavaLibrary.getRandomCollectionEntry(rand, rm.adjacentFloor);
            while (rm.nodeClearArea.contains(c.to2D()))
                c = ReikaJavaLibrary.getRandomCollectionEntry(rand, rm.adjacentFloor);
            while (c.offset(0, -1, 0).softBlock(world)
                   || c.offset(0, -1, 0).getBlock(world)
                       == SFBlocks.CAVESHIELD.getBlockInstance())
                c = c.offset(0, -1, 0);
            TileCaveSpawner lgc
                = this.generateSpawnerAt(world, c.xCoord, c.yCoord, c.zCoord);
            this.setSpawnParameters(lgc, rand, 1, 6, 5);
            lgc.setInertTimeout(6000); //was 5 then 12, then 300

            /*
            for (Coordinate c2 : c.getAdjacentCoordinates()) {
                if (!carveSet.contains(c2)) {
                    c2.setBlock(world, SFBlocks.CAVESHIELD.getBlockInstance());
                }
            }*/
        }

        return cc;
    }

    private int getRandomY(World world, int x, int z, Random rand) {
        int top = DecoratorPinkForest.getTrueTopAt(world, x, z);
        int min = 40;
        return ReikaRandomHelper.getRandomBetween(
            min, Math.max(Math.min(min + 20, top), Math.min(72, top - 50)), rand
        );
    }

    private void generateDecorations(
        World world,
        Random rand,
        HashSet<Coordinate> carveSet,
        HashMap<Coordinate, Integer> floor,
        HashSet<Coordinate> pits,
        CentralCave cc
    ) {
        for (int i = 0; i < 40; i++) {
            Coordinate c
                = ReikaJavaLibrary.getRandomCollectionEntry(rand, floor.keySet());
            if (pits.contains(c))
                continue;
            int y = floor.get(c);
            if (!c.setY(y).isEmpty(world))
                continue;
            boolean flag = true;
            for (int i2 = 0; i2 <= 2; i2++) {
                if (!carveSet.contains(c.offset(0, y + i2, 0))) {
                    flag = false;
                    break;
                }
                if (!carveSet.contains(c.offset(1, y + i2, 0))
                    || !carveSet.contains(c.offset(-1, y + i2, 0))) {
                    flag = false;
                    break;
                }
                if (!carveSet.contains(c.offset(0, y + i2, 1))
                    || !carveSet.contains(c.offset(0, y + i2, -1))) {
                    flag = false;
                    break;
                }
            }
            if (!flag)
                continue;
            world.setBlock(
                c.xCoord,
                y,
                c.zCoord,
                SFBlocks.DECORATION.getBlockInstance(),
                DecorationType.STALAGMITE.ordinal(),
                2
            );
        }

        for (int i = 0; i < 20; i++) {
            Coordinate c
                = ReikaJavaLibrary.getRandomCollectionEntry(rand, floor.keySet());
            if (pits.contains(c))
                continue;
            int y = floor.get(c);
            if (!c.setY(y).isEmpty(world))
                continue;
            int h = ReikaRandomHelper.getRandomBetween(1, 3, rand);
            this.generateMushroom(world, c.xCoord, y, c.zCoord, h, carveSet);

            for (int i2 = 0; i2 < 3; i2++) {
                Coordinate c2 = c.offset(
                    ReikaRandomHelper.getRandomPlusMinus(0, 2, rand),
                    0,
                    ReikaRandomHelper.getRandomPlusMinus(0, 2, rand)
                );
                if (!floor.containsKey(c2))
                    continue;
                y = floor.get(c2);
                if (!c2.setY(y).isEmpty(world))
                    continue;
                h = ReikaRandomHelper.getRandomBetween(1, 3, rand);
                this.generateMushroom(world, c2.xCoord, y, c2.zCoord, h, carveSet);
            }
        }

        for (int i = 0; i < 120; i++) {
            Coordinate c
                = ReikaJavaLibrary.getRandomCollectionEntry(rand, cc.carve.keySet());
            while (carveSet.contains(c.offset(0, 1, 0))) {
                c = c.offset(0, 1, 0);
            }
            if (!c.isEmpty(world))
                continue;
            if (c.offset(0, 1, 0).softBlock(world))
                continue;
            if (rand.nextInt(3) > 0) {
                c.setBlock(
                    world,
                    SFBlocks.DECORATION.getBlockInstance(),
                    DecorationType.STALACTITE.ordinal()
                );
            } else {
                int l = Math.min(5, c.yCoord - cc.footprint.get(c.to2D()) / 2);
                this.generateVine(world, c.xCoord, c.yCoord, c.zCoord, l, carveSet);
            }
        }

        for (int i = 0; i < 20; i++) {
            Coordinate root
                = ReikaJavaLibrary.getRandomCollectionEntry(rand, floor.keySet());
            HashSet<Coordinate> place = new HashSet();
            place.add(root);
            int n = ReikaRandomHelper.getRandomBetween(0, 3, rand);
            for (int i2 = 0; i2 < n; i2++) {
                HashSet<Coordinate> toAdd = new HashSet();
                for (Coordinate c : place) {
                    for (Coordinate c2 : c.getAdjacentCoordinates()) {
                        if (c2.yCoord == c.yCoord && floor.containsKey(c2)) {
                            toAdd.add(c2);
                        }
                    }
                }
                place.addAll(toAdd);
            }
            for (Coordinate c : place) {
                int y = floor.get(c);
                if (!c.setY(y).isEmpty(world))
                    continue;
                this.generateStalks(world, c.xCoord, y, c.zCoord, rand);
            }
        }

        for (Tunnel t : cc.tunnels) {
            for (int i = 0; i < 16 * cc.getBiomeScaleFactor(); i++) {
                Coordinate c
                    = ReikaJavaLibrary.getRandomCollectionEntry(rand, t.carve.keySet());
                while (carveSet.contains(c.offset(0, -1, 0))) {
                    c = c.offset(0, -1, 0);
                }
                if (!c.isEmpty(world))
                    continue;
                int h = ReikaRandomHelper.getRandomBetween(0, 2, rand);
                this.generateMushroom(world, c.xCoord, c.yCoord, c.zCoord, h, carveSet);

                for (int i2 = 0; i2 <= 5; i2++) {
                    Coordinate c2 = c.offset(
                        ReikaRandomHelper.getRandomPlusMinus(0, 2, rand),
                        0,
                        ReikaRandomHelper.getRandomPlusMinus(0, 2, rand)
                    );
                    while (carveSet.contains(c2.offset(0, -1, 0))) {
                        c2 = c2.offset(0, -1, 0);
                    }
                    if (!carveSet.contains(c2))
                        continue;
                    if (!c2.isEmpty(world))
                        continue;
                    if (DecoratorPinkForest.getTrueTopAt(world, c2.xCoord, c2.zCoord)
                        <= c2.yCoord + 10)
                        continue;
                    int n = rand.nextInt(10);
                    switch (n) {
                        case 0:
                        case 1:
                            c2.setBlock(
                                world,
                                SFBlocks.DECORATION.getBlockInstance(),
                                DecorationType.STALAGMITE.ordinal()
                            );
                            if (c2.offset(0, 1, 0).getBlock(world)
                                == SFBlocks.GRASS.getBlockInstance())
                                ReikaJavaLibrary.pConsole("Placed stagmite into mushroom!"
                                );
                            break;
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                            h = ReikaRandomHelper.getRandomBetween(0, 2, rand);
                            if (c2.getBlock(world)
                                == SFBlocks.DECORATION.getBlockInstance())
                                ReikaJavaLibrary.pConsole("Placed mushroom into stagmite!"
                                );
                            this.generateMushroom(
                                world, c2.xCoord, c2.yCoord, c2.zCoord, h, carveSet
                            );
                            break;
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                            this.generateStalks(
                                world, c2.xCoord, c2.yCoord, c2.zCoord, rand
                            );
                            break;
                    }
                }
            }

            for (int i = 0; i < 80; i++) {
                Coordinate c
                    = ReikaJavaLibrary.getRandomCollectionEntry(rand, t.carve.keySet());
                while (carveSet.contains(c.offset(0, 1, 0))) {
                    c = c.offset(0, 1, 0);
                }
                if (!c.isEmpty(world))
                    continue;
                if (c.offset(0, 1, 0).softBlock(world))
                    continue;
                if (rand.nextInt(5) >= 1) {
                    c.setBlock(
                        world,
                        SFBlocks.DECORATION.getBlockInstance(),
                        DecorationType.STALACTITE.ordinal()
                    );
                } else {
                    this.generateVine(
                        world,
                        c.xCoord,
                        c.yCoord,
                        c.zCoord,
                        rand.nextBoolean() ? 3 : 2,
                        carveSet
                    );
                }
            }
        }

        for (int i = 0; i < 10; i++) {
            Coordinate c = ReikaJavaLibrary.getRandomCollectionEntry(
                rand, cc.nodeRoom.carve.keySet()
            );
            while (carveSet.contains(c.offset(0, -1, 0))) {
                c = c.offset(0, -1, 0);
            }
            if (!c.isEmpty(world))
                continue;
            if (c.getTaxicabDistanceTo(cc.nodeRoom.resourceNode) <= 2)
                continue;
            int h = ReikaRandomHelper.getRandomBetween(0, 2, rand);
            this.generateMushroom(world, c.xCoord, c.yCoord, c.zCoord, h, carveSet);

            for (int i2 = 0; i2 <= 5; i2++) {
                Coordinate c2 = c.offset(
                    ReikaRandomHelper.getRandomPlusMinus(0, 2, rand),
                    0,
                    ReikaRandomHelper.getRandomPlusMinus(0, 2, rand)
                );
                while (carveSet.contains(c2.offset(0, -1, 0))) {
                    c2 = c2.offset(0, -1, 0);
                }
                if (!carveSet.contains(c2))
                    continue;
                if (!c2.isEmpty(world))
                    continue;
                if (c2.getTaxicabDistanceTo(cc.nodeRoom.resourceNode) <= 2)
                    continue;
                if (DecoratorPinkForest.getTrueTopAt(world, c2.xCoord, c2.zCoord)
                    <= c2.yCoord + 10)
                    continue;
                int n = rand.nextInt(5);
                switch (n) {
                    case 0:
                    case 1:
                        this.generateStalks(world, c.xCoord, c.yCoord, c.zCoord, rand);
                        break;
                    case 2:
                        c2.setBlock(
                            world,
                            SFBlocks.DECORATION.getBlockInstance(),
                            DecorationType.STALAGMITE.ordinal()
                        );
                        if (c2.offset(0, 1, 0).getBlock(world)
                            == SFBlocks.GRASS.getBlockInstance())
                            ReikaJavaLibrary.pConsole("Placed stagmite into mushroom!");
                        break;
                    case 3:
                    case 4:
                        if (c2.getBlock(world) == SFBlocks.DECORATION.getBlockInstance())
                            ReikaJavaLibrary.pConsole("Placed mushroom into stagmite!");
                        h = ReikaRandomHelper.getRandomBetween(0, 2, rand);
                        this.generateMushroom(
                            world, c2.xCoord, c2.yCoord, c2.zCoord, h, carveSet
                        );
                        break;
                }
            }
        }

        for (int i = 0; i < 20; i++) {
            Coordinate c = ReikaJavaLibrary.getRandomCollectionEntry(
                rand, cc.nodeRoom.carve.keySet()
            );
            while (carveSet.contains(c.offset(0, 1, 0))) {
                c = c.offset(0, 1, 0);
            }
            if (!c.isEmpty(world))
                continue;
            if (c.getTaxicabDistanceTo(cc.nodeRoom.resourceNode) <= 4)
                continue;
            if (c.offset(0, 1, 0).softBlock(world))
                continue;
            if (rand.nextInt(5) >= 1) {
                c.setBlock(
                    world,
                    SFBlocks.DECORATION.getBlockInstance(),
                    DecorationType.STALACTITE.ordinal()
                );
            } else {
                this.generateVine(
                    world,
                    c.xCoord,
                    c.yCoord,
                    c.zCoord,
                    rand.nextBoolean() ? 3 : 2,
                    carveSet
                );
            }
        }
    }

    public void generateStalks(World world, int x, int y, int z, Random rand) {
        world.setBlock(
            x, y, z, SFBlocks.GRASS.getBlockInstance(), GrassTypes.STALKS.ordinal(), 2
        );
        if (rand.nextInt(4) == 0 && world.getBlock(x, y + 1, z).isAir(world, x, y + 1, z))
            world.setBlock(
                x,
                y + 1,
                z,
                SFBlocks.GRASS.getBlockInstance(),
                GrassTypes.STALKS.ordinal(),
                2
            );
    }

    private void
    generateVine(World world, int x, int y, int z, int l, HashSet<Coordinate> carveSet) {
        if (!GrassTypes.VINE.canExistAt(world, x, y, z))
            return;
        for (int k = 0; k < l; k++) {
            Coordinate c = new Coordinate(x, y - k, z);
            if ((carveSet == null || carveSet.contains(c)) && c.isEmpty(world)) {
                c.setBlock(
                    world, SFBlocks.GRASS.getBlockInstance(), GrassTypes.VINE.ordinal()
                );
            } else {
                break;
            }
        }
    }

    private void generateMushroom(
        World world, int x, int y, int z, int h, HashSet<Coordinate> carveSet
    ) {
        if (!GrassTypes.BLUE_MUSHROOM_STALK.canExistAt(world, x, y, z))
            return;
        for (int k = 0; k < h; k++) {
            Coordinate c = new Coordinate(x, y + k, z);
            if ((carveSet == null || carveSet.contains(c)) && c.isEmpty(world)) {
                c.setBlock(
                    world,
                    SFBlocks.GRASS.getBlockInstance(),
                    GrassTypes.BLUE_MUSHROOM_STALK.ordinal(),
                    2
                );
            } else {
                h = k - 1;
                break;
            }
        }
        world.setBlock(
            x,
            y + h,
            z,
            SFBlocks.GRASS.getBlockInstance(),
            GrassTypes.BLUE_MUSHROOM_TOP.ordinal(),
            2
        );
    }

    public void generateVine(World world, int x, int y, int z, Random rand) {
        this.generateVine(world, x, y, z, rand.nextBoolean() ? 3 : 2, null);
    }

    public void generateMushroom(World world, int x, int y, int z, Random rand) {
        this.generateMushroom(
            world, x, y, z, ReikaRandomHelper.getRandomBetween(0, 2, rand), null
        );
    }

    private void generateCasing(World world, Random rand, HashSet<Coordinate> carveSet) {
        HashSet<Coordinate> secondLayer = new HashSet();
        for (Coordinate c : carveSet) {
            for (Coordinate c2 : c.getAdjacentCoordinates()) {
                if (carveSet.contains(c2)) {
                    continue;
                }
                Block b = c2.getBlock(world);
                if (this.isSpecialCaveBlock(b))
                    continue;
                if (c2.yCoord
                    <= DecoratorPinkForest.getTrueTopAt(world, c2.xCoord, c2.zCoord)
                        - 15) {
                    /*
                    if (c2.softBlock(world) || b == Blocks.planks ||
                    !b.getMaterial().blocksMovement() || !b.isOpaqueCube() || b ==
                    Blocks.gravel || b == Blocks.sand || b == Blocks.dirt) {
                        c2.setBlock(world, Blocks.cobblestone);
                        secondLayer.add(c2);
                    }
                    else if (b.isReplaceableOreGen(world, c2.xCoord, c2.yCoord, c2.zCoord,
                    Blocks.stone) || ReikaBlockHelper.isOre(b, c.getBlockMetadata(world)))
                    { c2.setBlock(world, Blocks.mossy_cobblestone); secondLayer.add(c2);
                    }
                     */
                    c2.setBlock(world, SFBlocks.CAVESHIELD.getBlockInstance());
                    secondLayer.add(c2);
                }
            }
        }
        HashSet<Coordinate> realSecond = new HashSet();
        for (Coordinate c : secondLayer) {
            boolean allEmpty = true;
            for (Coordinate c2 : c.getAdjacentCoordinates()) {
                if (!carveSet.contains(c2) && !c2.isEmpty(world))
                    allEmpty = false;
            }
            if (allEmpty) {
                c.setBlock(world, Blocks.air);
                carveSet.add(c);
            } else {
                realSecond.add(c);
            }
        }
        for (Coordinate c : realSecond) {
            for (Coordinate c2 : c.getAdjacentCoordinates()) {
                if (carveSet.contains(c2) || realSecond.contains(c2))
                    continue;
                if (c2.softBlock(world))
                    c2.setBlock(world, Blocks.stone);
            }
        }
    }

    public boolean isSpecialCaveBlock(Block b) {
        return b == SFBlocks.CAVESHIELD.getBlockInstance()
            || b == SFBlocks.RESOURCENODE.getBlockInstance()
            || b == SFBlocks.SPAWNER.getBlockInstance()
            || b == SFBlocks.GASEMITTER.getBlockInstance();
    }

    private TileCaveSpawner generateSpawnerAt(World world, int x, int y, int z) {
        world.setBlock(x, y, z, SFBlocks.SPAWNER.getBlockInstance());
        TileCaveSpawner te = (TileCaveSpawner) world.getTileEntity(x, y, z);
        if (te == null) {
            te = new TileCaveSpawner();
            world.setTileEntity(x, y, z, te);
        }
        return te;
    }

    private void
    setSpawnParameters(TileCaveSpawner te, Random rand, int n, int ar, int sr) {
        caveSpawns.setRNG(rand);
        this.setSpawnParameters(te, caveSpawns.getRandomEntry().entityClass, n, ar, sr);
    }

    private void setSpawnParameters(
        TileCaveSpawner te, Class<? extends EntityMob> mob, int n, int ar, int sr
    ) {
        te.setSpawnParameters(mob, n, ar, sr);
    }

    public SpawnListEntry getRandomSpawn(Random rand) {
        caveSpawns.setRNG(rand);
        return caveSpawns.getRandomEntry();
    }

    private class Tunnel extends UraniumCavePiece {
        private final Coordinate endpoint;
        private final CentralCave cave;
        private final double startingAngle;
        private final double initialRun;

        private final double startingDX;
        private final double startingDZ;
        private final double radius;

        private DecimalPosition boltStart;
        private boolean isToBiomeEdge = true;
        private int targetY = -1;

        private ArrayList<DecimalPosition> path = new ArrayList();
        private ArrayList<DecimalPosition> boltPath;

        private Tunnel(CentralCave cc, Coordinate c, double d, double ang) {
            this(cc, c, d, ang, 3);
        }

        private Tunnel(CentralCave cc, Coordinate c, double d, double ang, double r) {
            super(cc.center);
            cave = cc;
            cave.tunnels.add(this);
            endpoint = c;
            initialRun = d;
            startingAngle = ang;
            radius = r;

            startingDX = Math.cos(Math.toRadians(startingAngle));
            startingDZ = Math.sin(Math.toRadians(startingAngle));

            boltStart
                = center.offset(startingDX * initialRun, 0, startingDZ * initialRun);
        }

        private void setBolt(ArrayList<DecimalPosition> b) {
            boltPath = b;
            boltStart = boltPath.remove(0);
        }

        @Override
        protected void calculate(World world, Random rand) {
            if (targetY < 0) {
                targetY = DecoratorPinkForest.getTrueTopAt(
                    world, endpoint.xCoord, endpoint.zCoord
                );
                while (world.getBlock(endpoint.xCoord, targetY + 1, endpoint.zCoord)
                       == Blocks.water) {
                    targetY++;
                }
                targetY += 5;
            }
            double dyf = 0; //(targetY-boltStart.yCoord)*ReikaRandomHelper.getRandomBetween(0,
                            //0.25, rand);
            Coordinate end = new Coordinate(endpoint.xCoord, targetY, endpoint.zCoord);
            double dd = ReikaMathLibrary.py3d(
                endpoint.xCoord - center.xCoord, 0, endpoint.zCoord - center.zCoord
            );
            int n = (int) Math.max(4, dd / 16);
            if (boltPath == null) {
                LightningBolt b = new LightningBolt(
                    new DecimalPosition(
                        boltStart.xCoord, boltStart.yCoord + dyf, boltStart.zCoord
                    ),
                    new DecimalPosition(end),
                    n
                );
                b.setRandom(rand);
                //b.variance = 10;//15;
                b.setVariance(10, 8, 10);
                b.maximize();

                boltPath = new ArrayList();

                for (int i = 0; i <= b.nsteps; i++) {
                    DecimalPosition pos = b.getPosition(i);
                    boltPath.add(pos);
                }
            }
            Spline path = new Spline(SplineType.CENTRIPETAL);
            if (!boltStart.equals(center))
                path.addPoint(new BasicSplinePoint(center));

            for (DecimalPosition pos : boltPath) {
                path.addPoint(new BasicSplinePoint(pos));
            }

            List<DecimalPosition> li = path.get(24, false);
            int last = 0;
            for (int i = 0; i < li.size(); i++) {
                DecimalPosition p = li.get(i);
                this.path.add(p);
                //int px = MathHelper.floor_double(p.xCoord);
                //int pz = MathHelper.floor_double(p.zCoord);
                double w = 0; //2.5;
                double r = 2.25;
                if (this.carveAt(world, p, r, w, /*this.getAngleAt(li, i)*/ 0)) {
                    last = i;
                }
            }
            DecimalPosition p = li.get(last);
            for (double i = -radius; i <= radius; i++) {
                for (double j = 1; j <= radius; j++) {
                    for (double k = -radius; k <= radius; k++) {
                        if (ReikaMathLibrary.isPointInsideEllipse(
                                i, j, k, radius + 0.5, radius * 0.75, radius + 0.5
                            )) {
                            int dx = MathHelper.floor_double(p.xCoord + i);
                            int dy = MathHelper.floor_double(p.yCoord + j);
                            int dz = MathHelper.floor_double(p.zCoord + k);
                            Coordinate c = new Coordinate(dx, dy, dz);
                            if (world.getBlock(dx, dy, dz) == Blocks.dirt) {
                                Block above = world.getBlock(dx, dy + 1, dz);
                                if (above == Blocks.water || above == Blocks.flowing_water
                                    || above.isAir(world, dx, dy + 1, dz)
                                    || DecoratorPinkForest.isTerrain(
                                        world, dx, dy + 1, dz
                                    )) {
                                    world.setBlock(dx, dy, dz, Blocks.water);
                                }
                            }
                        }
                    }
                }
            }
        }

        private double getAngleAt(List<DecimalPosition> li, int i) {
            DecimalPosition pre = i == 0 ? null : li.get(i - 1);
            DecimalPosition at = li.get(i);
            DecimalPosition post = i == li.size() - 1 ? null : li.get(i + 1);
            double ang1 = pre == null
                ? -1
                : Math.toDegrees(
                    Math.atan2(at.zCoord - pre.zCoord, at.xCoord - pre.xCoord)
                );
            double ang2 = post == null
                ? -1
                : Math.toDegrees(
                    Math.atan2(post.zCoord - at.zCoord, post.xCoord - at.xCoord)
                );
            if (pre == null)
                return ang2;
            else if (post == null)
                return ang1;
            return (ang1 + ang2) / 2D;
        }

        @Override
        protected boolean skipCarve(Coordinate c) {
            return c.to2D().getDistanceTo(cave.center.xCoord, 0, cave.center.zCoord)
                <= cave.innerCircleRadius + 4; //cave.footprint.containsKey(c.to2D());
        }

        @Override
        public String toString() {
            return "Tunnel " + cave.center + " > " + endpoint + " @ " + targetY;
        }

        @Override
        protected void generate(World world, Random rand) {
            super.generate(world, rand);

            double sc = cave.getBiomeScaleFactor(3);
            int n = (int) ReikaRandomHelper.getRandomBetween(4 * sc, 6 * sc, rand);
            for (int i = 0; i < 5; i++) {
                Coordinate c
                    = ReikaJavaLibrary.getRandomCollectionEntry(rand, carve.keySet());
                if (c.yCoord
                    >= DecoratorPinkForest.getTrueTopAt(world, c.xCoord, c.zCoord) - 15)
                    continue;
                Coordinate below = c.offset(0, -1, 0);
                while (cave.carves(below)) {
                    c = below;
                    below = c.offset(0, -1, 0);
                }
                TileCaveSpawner lgc = instance.generateSpawnerAt(
                    world, below.xCoord, below.yCoord, below.zCoord
                );
                UraniumCave.this.setSpawnParameters(
                    lgc,
                    EntityCaveSpider.class,
                    isToBiomeEdge ? 3 : 5,
                    isToBiomeEdge ? 12 : 8,
                    3
                );
                lgc.setInertTimeout(
                    isToBiomeEdge ? 1200 : 3600
                ); //was isToBiomeEdge ? 15 : 9;

                OreClusterType ore = (isToBiomeEdge ? OreSpawnLocation.CAVE_ENTRY_TUNNEL
                                                    : OreSpawnLocation.CAVE_NODE_TUNNEL)
                                         .getRandomOreSpawn();

                if (ore != null) {
                    BlockKey oreBlock = ore.getRandomBlock(rand);
                    for (Coordinate c2 : below.getAdjacentCoordinates()) {
                        if (c2.softBlock(world)) {
                            c2.setBlock(world, oreBlock.blockID, oreBlock.metadata);
                        }
                    }
                }
            }
        }
    }

    static class CentralCave extends UraniumCavePiece {
        private static final int MIN_TUNNEL_WIDTH = 3;

        //private final int rmax = 40; //was 24 then 36
        //private final LobulatedCurve outer = LobulatedCurve.fromMinMaxRadii(18, rmax, 5,
        //true); //was 16

        private final HashMap<Coordinate, Integer> footprint = new HashMap();

        private final ArrayList<Tunnel> tunnels = new ArrayList();
        private ResourceNodeRoom nodeRoom;

        private SimplexNoiseGenerator floorHeightNoise;
        private SimplexNoiseGenerator ceilingHeightNoise;

        private SimplexNoiseGenerator xOffset1;
        private SimplexNoiseGenerator zOffset1;
        private SimplexNoiseGenerator xOffset2;
        private SimplexNoiseGenerator zOffset2;

        private DecimalPosition innerCircleOffset;
        private DecimalPosition innerCircleCenter;
        private double innerCircleRadius;
        private double outerCircleRadius;

        private int lowestFloor = 255;

        private int biomeSize;

        public CentralCave(int x, int y, int z) {
            super(new DecimalPosition(x + 0.5, y + 0.5, z + 0.5));
        }

        public double getBiomeScaleFactor() {
            return this.getBiomeScaleFactor(4);
        }

        public double getBiomeScaleFactor(double exp) {
            return Math.pow(biomeSize, exp) / Math.pow(4, exp);
        }

        public boolean carves(Coordinate c) {
            if (carve.containsKey(c))
                return true;
            if (nodeRoom != null && nodeRoom.carve.containsKey(c))
                return true;
            for (Tunnel t : tunnels) {
                if (t.carve.containsKey(c))
                    return true;
            }
            return false;
        }

        @Override
        protected void calculate(World world, Random rand) {
            biomeSize = ReikaWorldHelper.getBiomeSize(world);

            floorHeightNoise
                = (SimplexNoiseGenerator) new SimplexNoiseGenerator(rand.nextLong())
                      .setFrequency(1 / 16D)
                      .addOctave(2.6, 0.17);
            ceilingHeightNoise
                = (SimplexNoiseGenerator) new SimplexNoiseGenerator(rand.nextLong())
                      .setFrequency(1 / 8D)
                      .addOctave(1.34, 0.41);

            double f = 1 / 5D;
            xOffset1 = (SimplexNoiseGenerator) new SimplexNoiseGenerator(rand.nextLong())
                           .setFrequency(f);
            zOffset1 = (SimplexNoiseGenerator) new SimplexNoiseGenerator(rand.nextLong())
                           .setFrequency(f);
            xOffset2 = (SimplexNoiseGenerator) new SimplexNoiseGenerator(rand.nextLong())
                           .setFrequency(f);
            zOffset2 = (SimplexNoiseGenerator) new SimplexNoiseGenerator(rand.nextLong())
                           .setFrequency(f);

            if (innerCircleOffset == null) {
                int dr = 10;
                //outerCircleOffset = new
                //DecimalPosition(ReikaRandomHelper.getRandomPlusMinus(0, dr, rand), 0,
                //ReikaRandomHelper.getRandomPlusMinus(0, dr, rand));
                /*do {
                outerCircleRadius = ReikaRandomHelper.getRandomBetween(24D, 36D, rand);
                innerCircleRadius = ReikaRandomHelper.getRandomBetween(6D, 15D, rand);
            } while(outerCircleRadius-innerCircleRadius >= 9);
                 */
                outerCircleRadius = ReikaRandomHelper.getRandomBetween(24D, 36D, rand);
                innerCircleRadius = ReikaRandomHelper.getRandomBetween(
                    Math.max(6D, outerCircleRadius - MIN_TUNNEL_WIDTH * 6),
                    outerCircleRadius - MIN_TUNNEL_WIDTH * 4,
                    rand
                );

                double biomeSize = ReikaWorldHelper.getBiomeSize(world);
                biomeSize = biomeSize * biomeSize / 16D;
                outerCircleRadius *= Math.max(1, biomeSize);
                innerCircleRadius *= Math.max(1, biomeSize);

                double maxr
                    = outerCircleRadius - innerCircleRadius - MIN_TUNNEL_WIDTH - 3;
                double offr = ReikaRandomHelper.getRandomBetween(maxr * 0.75, maxr, rand);
                double offa = rand.nextDouble() * 360;
                double offX = offr * Math.cos(Math.toRadians(offa));
                double offZ = offr * Math.sin(Math.toRadians(offa));
                //innerCircleCenter =
                //center.offset(ReikaRandomHelper.getRandomPlusMinus(0, maxr, rand), 0,
                //ReikaRandomHelper.getRandomPlusMinus(0, maxr, rand));
                innerCircleOffset = new DecimalPosition(offX, 0, offZ);
                //outerCircleCenter = center.offset(outerCircleOffset);
            }

            innerCircleCenter = center.offset(innerCircleOffset);

            double r
                = outerCircleRadius; //+outerCircleOffset.xCoord+outerCircleOffset.zCoord;

            for (double i = -r; i <= r; i++) {
                for (double k = -r; k <= r; k++) {
                    int x = MathHelper.floor_double(center.xCoord + i);
                    int z = MathHelper.floor_double(center.zCoord + k);
                    double di = i - innerCircleOffset.xCoord;
                    double dk = k - innerCircleOffset.zCoord;
                    boolean flag = false;
                    int floor = Integer.MAX_VALUE;
                    //	if (ReikaMathLibrary.py3d(i, 0, k) <= outerCircleRadius) {
                    //	if (ReikaMathLibrary.py3d(di, 0, dk) >= innerCircleRadius) {
                    double min = (int
                    ) (-6
                       + MathHelper.clamp_double(
                           floorHeightNoise.getValue(x, z) * 3, -1.75, 1.75
                       ));
                    double max = (int
                    ) (6
                       + MathHelper.clamp_double(
                           ceilingHeightNoise.getValue(x, z) * 2, -2, 2
                       ));
                    for (double h = min; h <= max; h++) {
                        /* not working
                                double d1 = 1;
                                double d2 = 1;
                                int diff = Math.min(h-min, max-h);
                                if (diff <= 2) {
                                    d1 *= diff/3D;
                                    d2 += (3-diff)*2;
                                }
                                double r1 = outerCircleRadius*d1;
                                double r2 = innerCircleRadius*d2;
                         */

                        //add noise to edges
                        if (h < -5.2)
                            continue; //flatten the floor a little
                        double dn = 4; //5;//3;
                        double r1 = outerCircleRadius;
                        double r2 = innerCircleRadius;
                        int diff = (int) Math.min(h - min, max - h);
                        if (diff <= 2) {
                            int step = 3 - diff; //1-3
                            dn += step * 0.7;

                            r1 -= step * 2;
                            r2 += step;
                        }
                        double i2 = i
                            + MathHelper.clamp_double(
                                xOffset1.getValue(x, z) * dn, -3, 3
                            );
                        double k2 = k
                            + MathHelper.clamp_double(
                                zOffset1.getValue(x, z) * dn, -3, 3
                            );
                        double di2 = di
                            + MathHelper.clamp_double(
                                xOffset2.getValue(x, z) * dn, -2, 2
                            );
                        double dk2 = dk
                            + MathHelper.clamp_double(
                                zOffset2.getValue(x, z) * dn, -2, 2
                            );
                        if (ReikaMathLibrary.py3d(i2, 0, k2) <= r1) {
                            floor = Math.min(floor, (int) (center.yCoord + h));
                            if (ReikaMathLibrary.py3d(di2, 0, dk2) >= r2) {
                                Coordinate c
                                    = new Coordinate(x, (int) (center.yCoord + h), z);
                                carve.put(c, (int) center.yCoord);
                            }
                            flag = true;
                        }
                    }
                    //}
                    //}
                    if (flag) {
                        Coordinate c = new Coordinate(x, 0, z);
                        footprint.put(c, floor);
                        lowestFloor = Math.min(floor, lowestFloor);
                    }
                }
            }
        }

        @Override
        protected boolean skipCarve(Coordinate c) {
            return false;
        }
    }

    private static class ResourceNodeRoom extends UraniumCavePiece {
        private final HashMap<DecimalPosition, Integer> disks = new HashMap();
        private final HashSet<Coordinate> adjacent = new HashSet();
        private final HashSet<Coordinate> adjacentFloor = new HashSet();

        private final CentralCave cave;

        private Coordinate resourceNode;
        private final HashSet<Coordinate> nodeClearArea = new HashSet();
        private final HashSet<Coordinate> minerArea = new HashSet();
        private final HashSet<Coordinate> crackDisallow = new HashSet();

        protected ResourceNodeRoom(CentralCave cc, DecimalPosition p) {
            super(p);
            cave = cc;
        }

        void generateOreSpike(World world, Random rand, BlockKey ore) {
            /*
            placeOreIfEmpty(world, ore, resourceNode.offset(0, 1, 0));
            placeOreIfEmpty(world, ore, resourceNode.offset(0, 2, 0));
            placeOreIfEmpty(world, ore, resourceNode.offset(0, 2, 0));
             */
            for (int i = 1; i <= 3; i++) {
                Coordinate c = resourceNode.offset(0, i, 0);
                if (c.softBlock(world)) {
                    c.setBlock(world, ore.blockID, ore.metadata);
                    if (i <= 2) {
                        for (Coordinate c2 : c.getAdjacentCoordinates()) {
                            if (c2.yCoord == c.yCoord && c2.softBlock(world)) {
                                c2.setBlock(world, ore.blockID, ore.metadata);
                            }
                        }
                    }
                    if (i == 1) {
                        Coordinate c2 = resourceNode.offset(1, i, 0);
                        if (c2.yCoord == c.yCoord && c2.softBlock(world)) {
                            c2.setBlock(world, ore.blockID, ore.metadata);
                        }
                        c2 = resourceNode.offset(-1, i, 0);
                        if (c2.yCoord == c.yCoord && c2.softBlock(world)) {
                            c2.setBlock(world, ore.blockID, ore.metadata);
                        }
                        c2 = resourceNode.offset(0, i, 1);
                        if (c2.yCoord == c.yCoord && c2.softBlock(world)) {
                            c2.setBlock(world, ore.blockID, ore.metadata);
                        }
                        c2 = resourceNode.offset(0, i, -1);
                        if (c2.yCoord == c.yCoord && c2.softBlock(world)) {
                            c2.setBlock(world, ore.blockID, ore.metadata);
                        }
                    }
                } else {
                    break;
                }
            }
        }

        @Override
        protected void calculate(World world, Random rand) {
            int n = ReikaRandomHelper.getRandomBetween(4, 7, rand); //was 3-6
            double dr = 7; //8;//5;
            for (int i = 0; i < n; i++) {
                DecimalPosition c = center.offset(
                    ReikaRandomHelper.getRandomPlusMinus(1, dr, rand),
                    ReikaRandomHelper.getRandomPlusMinus(0, 1.75D, rand),
                    ReikaRandomHelper.getRandomPlusMinus(1, dr, rand)
                );
                int r = ReikaRandomHelper.getRandomBetween(4, 7, rand);
                disks.put(c, r);
            }
            for (Entry<DecimalPosition, Integer> e : disks.entrySet()) {
                DecimalPosition ctr = e.getKey();
                int r = e.getValue();
                int ry = (int) (r / 2.5);
                for (int i = -r; i <= r; i++) {
                    for (int j = -ry; j <= ry; j++) {
                        for (int k = -r; k <= r; k++) {
                            if (ReikaMathLibrary.isPointInsideEllipse(
                                    i, j, k, r, ry, r
                                )) {
                                int dx = MathHelper.floor_double(ctr.xCoord + i);
                                int dy = MathHelper.floor_double(ctr.yCoord + j);
                                int dz = MathHelper.floor_double(ctr.zCoord + k);
                                Coordinate c = new Coordinate(dx, dy, dz);
                                carve.put(c, dy);
                                adjacent.addAll(c.getAdjacentCoordinates());
                            }
                        }
                    }
                }
            }
            boolean flag = true;
            while (flag) {
                adjacent.removeAll(carve.keySet());
                flag = false;
                for (Coordinate c : adjacent) {
                    int adjacentSides = 0;
                    for (Coordinate c2 : c.getAdjacentCoordinates()) {
                        if (!carve.containsKey(c2)) {
                            adjacentSides++;
                        }
                    }
                    if (adjacentSides <= 1) {
                        carve.put(c, c.yCoord);
                        flag = true;
                    }
                }
            }
            for (Coordinate c : adjacent) {
                if (carve.containsKey(c.offset(0, 1, 0))) {
                    boolean flag2 = false;
                    for (Coordinate c2 : c.getAdjacentCoordinates()) {
                        if (!c2.isEmpty(world)) {
                            flag2 = true;
                            break;
                        }
                    }
                    if (flag2)
                        adjacentFloor.add(c);
                }
            }
        }

        @Override
        protected void generate(World world, Random rand) {
            super.generate(world, rand);

            //this.generateResourceNode(world, rand);
        }

        void generateResourceNode(World world, Random rand) {
            if (resourceNode == null) {
                Coordinate c
                    = ReikaJavaLibrary.getRandomCollectionEntry(rand, disks.keySet())
                          .getCoordinate(); //new Coordinate(center);
                Coordinate below = c.offset(0, -1, 0);
                while (cave.carves(below)) {
                    c = below;
                    below = c.offset(0, -1, 0);
                }
                resourceNode = below;
            }

            this.generateResourceNodeAt(world, rand);
        }

        private void generateResourceNodeAt(World world, Random rand) {
            resourceNode.setBlock(world, SFBlocks.RESOURCENODE.getBlockInstance());
            TileResourceNode te = (TileResourceNode) resourceNode.getTileEntity(world);
            te.generate(rand);

            HashSet<Coordinate> cleared = new HashSet();
            int r = 2; //1;
            for (int i = -r; i <= r; i++) {
                for (int k = -r; k <= r; k++) {
                    if (Math.abs(i) == 2 && Math.abs(k) == 2)
                        continue;
                    Coordinate below = resourceNode.offset(i, -1, k);
                    Coordinate at = resourceNode.offset(i, 0, k);
                    nodeClearArea.add(at.to2D());
                    crackDisallow.add(at);
                    crackDisallow.add(below);
                    if (i != 0 || k != 0) {
                        at.setBlock(world, SFBlocks.CAVESHIELD.getBlockInstance(), 1);
                    }
                    below.setBlock(world, SFBlocks.CAVESHIELD.getBlockInstance(), 1);
                    for (int j = 1; j <= 4; j++) {
                        cleared.add(at.offset(0, j, 0));
                    }
                }
            }
            ForgeDirection axis = ForgeDirection.VALID_DIRECTIONS[rand.nextInt(4) + 2];
            /*
            for (int j = 0; j <= 13; j++) {

            }*/
            FilledBlockArray arr = MinerStructure.getMinerStructure(
                world,
                resourceNode.xCoord,
                resourceNode.yCoord + 1,
                resourceNode.zCoord,
                axis
            );
            for (int x = arr.getMinX() - 1; x <= arr.getMaxX() + 1; x++) {
                for (int y = arr.getMinY() - 1; y <= arr.getMaxY() + 1; y++) {
                    for (int z = arr.getMinZ() - 1; z <= arr.getMaxZ() + 1; z++) {
                        Coordinate c = new Coordinate(x, y, z);
                        if (!crackDisallow.contains(c))
                            minerArea.add(c);
                    }
                }
            }

            for (Coordinate c2 : cleared) {
                for (Coordinate c3 : c2.getAdjacentCoordinates()) {
                    if (!carve.containsKey(c3) && !cleared.contains(c3)
                        && !instance.isSpecialCaveBlock(c3.getBlock(world))) {
                        c3.setBlock(world, SFBlocks.CAVESHIELD.getBlockInstance());
                    }
                }
                c2.setBlock(world, Blocks.air);
            }
        }

        private void crackBlocks(World world) {
            HashSet<Coordinate> placed = new HashSet();
            for (Coordinate c2 : minerArea) {
                if (c2.getBlock(world) == SFBlocks.CAVESHIELD.getBlockInstance()) {
                    placed.add(c2);
                } else {
                    //ReikaJavaLibrary.pConsole(c2.getBlockKey(world).getLocalized());
                }
            }

            boolean flag = true;
            while (flag) {
                flag = false;
                Iterator<Coordinate> it = placed.iterator();
                while (it.hasNext()) {
                    Coordinate c = it.next();
                    for (Coordinate c2 : c.getAdjacentCoordinates()) {
                        if (placed.contains(c2)) {
                            it.remove();
                            flag = true;
                            break;
                        }
                    }
                }
            }

            for (Coordinate c2 : placed) {
                for (Coordinate c3 : c2.getAdjacentCoordinates()) {
                    Block b = c3.getBlock(world);
                    if (!carve.containsKey(c3) && !instance.isSpecialCaveBlock(b)
                        && !(
                            b == SFBlocks.TERRAIN.getBlockInstance()
                            && c3.getBlockMetadata(world) == TerrainType.CRACKS.ordinal()
                        )) {
                        c3.setBlock(world, SFBlocks.CAVESHIELD.getBlockInstance());
                    }
                }
            }
            for (Coordinate c2 : placed) {
                c2.setBlock(
                    world,
                    SFBlocks.TERRAIN.getBlockInstance(),
                    TerrainType.CRACKS.ordinal()
                );
            }
        }
    }

    private static abstract class UraniumCavePiece {
        protected final DecimalPosition center;

        protected final HashMap<Coordinate, Integer> carve = new HashMap();

        protected UraniumCavePiece(DecimalPosition p) {
            center = p;
        }

        protected abstract void calculate(World world, Random rand);

        protected void generate(World world, Random rand) {
            for (Coordinate c : carve.keySet()) {
                c.setBlock(world, Blocks.air);
                /*
                for (Coordinate c2 : c.getAdjacentCoordinates()) {
                    if (this.skipCarve(c2))
                        continue;
                    if (c2.yCoord <= 58 && c2.softBlock(world) && !carve.containsKey(c2))
                { c2.setBlock(world, Blocks.stone);
                    }
                }
                 */
            }
        }

        protected final boolean hasAdjacentHorizontalCarve(Coordinate c) {
            for (int i = 0; i < 4; i++) {
                ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i + 2];
                if (carve.containsKey(c.offset(dir, 1)))
                    return true;
            }
            return false;
        }

        protected final boolean
        carveAt(World world, DecimalPosition p, double r, double w, double angle) {
            return this.carveAt(world, p, r, r, w, angle);
        }

        protected final boolean carveAt(
            World world, DecimalPosition p, double r, double h, double w, double angle
        ) {
            angle += 90;
            boolean flag = false;
            double ax = w > 0 ? Math.abs(Math.cos(Math.toRadians(angle))) : 0;
            double az = w > 0 ? Math.abs(Math.sin(Math.toRadians(angle))) : 0;
            for (double i = -r; i <= r; i++) {
                for (double j = -r; j <= r; j++) {
                    for (double k = -r; k <= r; k++) {
                        if (ReikaMathLibrary.isPointInsideEllipse(
                                i, j, k, r + 0.5 + ax * w, h + 0.5, r + 0.5 + az * w
                            )) {
                            int dx = MathHelper.floor_double(p.xCoord + i);
                            int dy = MathHelper.floor_double(p.yCoord + j);
                            int dz = MathHelper.floor_double(p.zCoord + k);
                            Coordinate c = new Coordinate(dx, dy, dz);
                            if (this.skipCarve(c))
                                continue;
                            Block b = c.getBlock(world);
                            if (c.isEmpty(world) || instance.isSpecialCaveBlock(b)
                                || DecoratorPinkForest.isTerrain(
                                    world, c.xCoord, c.yCoord, c.zCoord
                                )) {
                                if (!carve.containsKey(c)) {
                                    flag = true;
                                }
                                carve.put(c, MathHelper.floor_double(p.yCoord));
                            }
                        }
                    }
                }
            }
            return flag;
        }

        protected boolean skipCarve(Coordinate c) {
            return false;
        }
    }

    public static class CachedCave {
        public final Coordinate center;
        public final double outerRadius;
        public final double innerRadius;
        final DecimalPosition innerOffset;
        public final DecimalPosition nodeRoom;
        public final Coordinate nodeTile;
        final HashMap<Coordinate, CachedTunnel> tunnels = new HashMap();
        final int biomeSize;

        private CentralCave reference;

        CachedCave(CentralCave cc) {
            center = new Coordinate(cc.center);
            outerRadius = cc.outerCircleRadius;
            innerRadius = cc.innerCircleRadius;
            innerOffset = cc.innerCircleOffset;
            nodeRoom = cc.nodeRoom.center;
            nodeTile = cc.nodeRoom.resourceNode;
            for (Tunnel t : cc.tunnels) {
                tunnels.put(t.endpoint, new CachedTunnel(t));
            }
            biomeSize = cc.biomeSize;
            reference = cc;
        }

        public ResourceNodeRoom reconstructNode(CentralCave cc, Coordinate node) {
            ResourceNodeRoom rm = new ResourceNodeRoom(cc, nodeRoom);
            rm.resourceNode = node;
            return rm;
        }

        public CentralCave reconstruct() {
            CentralCave ret
                = new CentralCave(center.xCoord, center.yCoord, center.zCoord);
            ret.outerCircleRadius = outerRadius;
            ret.innerCircleRadius = innerRadius;
            ret.innerCircleOffset = innerOffset;
            ret.biomeSize = biomeSize;
            reference = ret;
            return ret;
        }

        public CachedCave(
            Coordinate ctr,
            DecimalPosition node,
            Coordinate tile,
            double radius,
            double inner,
            DecimalPosition off,
            HashMap<Coordinate, CachedTunnel> map,
            int bsize
        ) {
            center = ctr;
            nodeRoom = node;
            nodeTile = tile;
            outerRadius = radius;
            innerRadius = inner;
            innerOffset = off;
            tunnels.putAll(map);
            biomeSize = bsize;
        }

        public boolean isInside(double x, double y, double z) {
            if (reference != null) {
                return reference.carves(new Coordinate(x, y, z));
            }
            if (center.getDistanceTo(x, y, z) <= outerRadius
                && Math.abs(center.yCoord - y) <= 10)
                return true;
            if (nodeRoom.getDistanceTo(x, y, z) <= 9)
                return true;
            for (CachedTunnel ct : tunnels.values()) {
                if (ct.isIn(center, x, y, z))
                    return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return center + " @ R = [" + innerRadius + ", " + outerRadius + "] + "
                + innerOffset + " & " + nodeRoom + " via " + tunnels;
        }
    }

    public static class CachedTunnel {
        public final Coordinate endpoint;
        public final double angle;
        public final double run;
        public final boolean isEdge;
        //private ArrayList<DecimalPosition> roughPath = new ArrayList();
        private ArrayList<DecimalPosition> bolt;

        private CachedTunnel(
            Coordinate c,
            double d,
            double a,
            ArrayList<DecimalPosition> path,
            boolean edge
        ) {
            endpoint = c;
            angle = a;
            run = d;
            bolt = path;
            isEdge = edge;
        }

        private CachedTunnel(Tunnel t) {
            endpoint = t.endpoint;
            angle = t.startingAngle;
            run = t.initialRun;
            isEdge = t.isToBiomeEdge;
            bolt = new ArrayList(t.boltPath);
            bolt.add(0, t.boltStart);
            /*
            for (int i = 0; i < t.path.size(); i++) {
                if (i%8 == 0) {
                    DecimalPosition p = t.path.get(i);
                    roughPath.add(p);
                }
            }*/
        }

        private boolean isIn(Coordinate ctr, double x, double y, double z) {
            /*
            for (DecimalPosition p : roughPath) {
                if (p.getDistanceTo(x, y, z) <= 7)
                    return true;
            }
            return false;
             */
            for (int i = -1; i < bolt.size() - 1; i++) {
                DecimalPosition p1 = i == -1 ? new DecimalPosition(ctr) : bolt.get(i);
                DecimalPosition p2 = bolt.get(i + 1);
                if (ReikaVectorHelper.getDistFromPointToLine(
                        p1.xCoord,
                        p1.yCoord,
                        p1.zCoord,
                        p2.xCoord,
                        p2.yCoord,
                        p2.zCoord,
                        x,
                        y,
                        z
                    )
                    <= 5) {
                    return true;
                }
            }
            return false;
        }

        public NBTTagCompound writeToTag() {
            NBTTagCompound ret = new NBTTagCompound();
            endpoint.writeToNBT("end", ret);
            ret.setDouble("angle", angle);
            ret.setDouble("run", run);
            ret.setBoolean("edge", isEdge);
            NBTTagList li = new NBTTagList();
            for (DecimalPosition p : bolt) {
                li.appendTag(p.writeToTag());
            }
            ret.setTag("bolt", li);
            return ret;
        }

        public static CachedTunnel readTag(NBTTagCompound NBT) {
            ArrayList<DecimalPosition> li = new ArrayList();
            NBTTagList list = NBT.getTagList("bolt", NBTTypes.COMPOUND.ID);
            for (Object o : list.tagList) {
                li.add(DecimalPosition.readTag((NBTTagCompound) o));
            }
            return new CachedTunnel(
                Coordinate.readFromNBT("end", NBT),
                NBT.getDouble("run"),
                NBT.getDouble("angle"),
                li,
                NBT.getBoolean("edge")
            );
        }
    }
}
