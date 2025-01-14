package Reika.Satisforestry.Biome.Biomewide;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Random;

import Reika.DragonAPI.Instantiable.Data.Immutable.Coordinate;
import Reika.DragonAPI.Instantiable.Data.Immutable.DecimalPosition;
import Reika.DragonAPI.Instantiable.Data.Immutable.WorldLocation;
import Reika.DragonAPI.Libraries.ReikaNBTHelper.NBTTypes;
import Reika.Satisforestry.Biome.BiomeFootprint;
import Reika.Satisforestry.Biome.Biomewide.MantaGenerator.MantaPath;
import Reika.Satisforestry.Biome.Biomewide.UraniumCave.CachedCave;
import Reika.Satisforestry.Biome.Biomewide.UraniumCave.CachedTunnel;
import Reika.Satisforestry.Biome.Biomewide.UraniumCave.CentralCave;
import Reika.Satisforestry.Biome.Generator.WorldGenCrashSite;
import Reika.Satisforestry.Biome.PinkForestPersistentData;
import Reika.Satisforestry.Entity.EntityFlyingManta;
import Reika.Satisforestry.Satisforestry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class BiomewideFeatureGenerator {
    public static final BiomewideFeatureGenerator instance
        = new BiomewideFeatureGenerator();

    private final HashMap<WorldLocation, CachedCave> caveNetworks = new HashMap();
    private final HashMap<WorldLocation, MantaPath> mantaPaths = new HashMap();
    private final HashSet<Integer> initialized = new HashSet();

    private BiomewideFeatureGenerator() {}

    public void clearOnUnload() {
        caveNetworks.clear();
        PointSpawnSystem.instance.clear();
        mantaPaths.clear();
        initialized.clear();
    }

    public void generateUniqueCenterFeatures(
        World world, int x, int z, Random rand, BiomeFootprint bf
    ) {
        initialized.add(world.provider.dimensionId);
        PinkForestPersistentData.initNetworkData(world);
        //bf.exportToImage(new File(world.getSaveHandler().getWorldDirectory(),
        //"pinkforest_footprint"));
        PointSpawnSystem.instance.createSpawnPoints(world, x, z, bf, rand);
        MantaPath path = mantaPaths.get(new WorldLocation(world, x, 0, z));
        if (path == null)
            path = MantaGenerator.instance.generatePathAroundBiome(world, bf, rand);
        Collection<Coordinate> rivers
            = PinkRivers.instance.generateRivers(world, x, z, rand, bf);
        boolean flag = false;
        if (!rivers.isEmpty()) {
            CachedCave at = caveNetworks.get(new WorldLocation(world, x, 0, z));
            CentralCave cc = UraniumCave.instance.generate(world, rand, x, z, rivers, at);
            if (cc != null) {
                caveNetworks.put(
                    new WorldLocation(world, cc.center.to2D()), new CachedCave(cc)
                );
                flag = true;
            }
        }
        if (!flag) {
            Satisforestry.logger.logError(
                "Failed to generate biomewide terrain features! River set: " + rivers
            );
        }
        if (path == null) {
            Satisforestry.logger.logError("Failed to generate manta path!");
        } else {
            mantaPaths.put(path.biomeCenter, path);
            EntityFlyingManta e = new EntityFlyingManta(world);
            path.clearBlocks(world);
            e.setPath(path);
            world.spawnEntityInWorld(e);
            Satisforestry.logger.log("Generated manta path around " + x + ", " + z);
        }
        this.save(world);
    }

    public void save(World world) {
        PinkForestPersistentData.initNetworkData(world).setDirty(true);
    }

    public boolean isInCave(World world, double x, double y, double z) {
        this.initializeWorldData(world);
        for (Entry<WorldLocation, CachedCave> e : caveNetworks.entrySet()) {
            if (e.getKey().dimensionID == world.provider.dimensionId) {
                CachedCave cv = e.getValue();
                if (cv.isInside(x, y, z)) {
                    return true;
                }
            }
        }
        return false;
    }

    public double getDistanceToCaveCenter(World world, double x, double y, double z) {
        this.initializeWorldData(world);
        double min = Double.POSITIVE_INFINITY;
        for (Entry<WorldLocation, CachedCave> e : caveNetworks.entrySet()) {
            if (e.getKey().dimensionID == world.provider.dimensionId) {
                CachedCave cv = e.getValue();
                double dist = cv.center.getDistanceTo(x, y, z);
                if (dist < min)
                    min = dist;
            }
        }
        return min;
    }

    public void initializeWorldData(World world) {
        if (!world.isRemote && !initialized.contains(world.provider.dimensionId)) {
            initialized.add(world.provider.dimensionId);
            PinkForestPersistentData.initNetworkData(world);
        }
    }

    public MantaPath getPathAround(World world, WorldLocation loc) {
        this.initializeWorldData(world);
        return mantaPaths.get(loc);
    }

    public void readFromNBT(NBTTagCompound NBT) {
        NBTTagList li = NBT.getTagList("caves", NBTTypes.COMPOUND.ID);
        for (Object o : li.tagList) {
            NBTTagCompound tag = (NBTTagCompound) o;
            Coordinate center = Coordinate.readTag(tag.getCompoundTag("center"));
            Coordinate tile = Coordinate.readTag(tag.getCompoundTag("tile"));
            DecimalPosition node = DecimalPosition.readTag(tag.getCompoundTag("node"));
            DecimalPosition off = DecimalPosition.readTag(tag.getCompoundTag("offset"));
            double radius = tag.getDouble("radius");
            double inner = tag.getDouble("inner");
            int bsize = tag.hasKey("biomesize") ? tag.getInteger("biomesize") : 4;
            HashMap<Coordinate, CachedTunnel> map = new HashMap();
            NBTTagList tunnels = tag.getTagList("tunnels", NBTTypes.COMPOUND.ID);
            for (Object o2 : tunnels.tagList) {
                CachedTunnel end = CachedTunnel.readTag((NBTTagCompound) o2);
                map.put(end.endpoint, end);
            }
            WorldLocation key = WorldLocation.readTag(tag.getCompoundTag("key"));
            caveNetworks.put(
                key, new CachedCave(center, node, tile, radius, inner, off, map, bsize)
            );
        }

        mantaPaths.clear();
        li = NBT.getTagList("mantas", NBTTypes.COMPOUND.ID);
        for (Object o : li.tagList) {
            NBTTagCompound tag = (NBTTagCompound) o;
            MantaPath path = MantaPath.readFromNBT(tag);
            if (path != null)
                mantaPaths.put(path.biomeCenter, path);
        }

        PointSpawnSystem.instance.clear();
        li = NBT.getTagList("spawnPoints", NBTTypes.COMPOUND.ID);
        PointSpawnSystem.instance.loadSpawnPoints(li);
        li = NBT.getTagList("doggoSpawns", NBTTypes.COMPOUND.ID);
        if (li.tagCount() > 0) {
            PointSpawnSystem.instance.loadLegacyDoggoSpawns(li);
        }

        WorldGenCrashSite.clearCache();
        WorldGenCrashSite.loadSavedPoints(NBT.getCompoundTag("crashSites"));
    }

    public void writeToNBT(NBTTagCompound NBT) {
        NBTTagList li = new NBTTagList();
        for (Entry<WorldLocation, CachedCave> e : caveNetworks.entrySet()) {
            NBTTagCompound cave = new NBTTagCompound();
            CachedCave cv = e.getValue();
            cave.setTag("key", e.getKey().writeToTag());
            cave.setTag("center", cv.center.writeToTag());
            cave.setTag("tile", cv.nodeTile.writeToTag());
            cave.setTag("node", cv.nodeRoom.writeToTag());
            cave.setTag("offset", cv.innerOffset.writeToTag());
            cave.setDouble("radius", cv.outerRadius);
            cave.setDouble("inner", cv.innerRadius);
            cave.setInteger("biomesize", cv.biomeSize);
            NBTTagList tunnels = new NBTTagList();
            for (CachedTunnel e2 : cv.tunnels.values()) {
                tunnels.appendTag(e2.writeToTag());
            }
            cave.setTag("tunnels", tunnels);
            li.appendTag(cave);
        }
        NBT.setTag("caves", li);

        li = new NBTTagList();
        for (MantaPath e : mantaPaths.values()) {
            li.appendTag(e.writeToNBT());
        }
        NBT.setTag("mantas", li);

        li = new NBTTagList();
        PointSpawnSystem.instance.saveSpawnPoints(li);
        NBT.setTag("spawnPoints", li);

        NBTTagCompound tag = new NBTTagCompound();
        WorldGenCrashSite.savePoints(tag);
        NBT.setTag("crashSites", tag);
    }
}
