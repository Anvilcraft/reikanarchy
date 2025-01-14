package Reika.Satisforestry.Biome.Biomewide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

import Reika.CritterPet.API.TamedCritter;
import Reika.DragonAPI.Instantiable.Data.Immutable.WorldChunk;
import Reika.DragonAPI.Instantiable.Data.Immutable.WorldLocation;
import Reika.DragonAPI.Instantiable.Data.Maps.TileEntityCache;
import Reika.DragonAPI.Instantiable.Data.Maps.TileEntityCache.LocationEntry;
import Reika.DragonAPI.Instantiable.Event.EntityRemovedEvent;
import Reika.DragonAPI.Instantiable.Event.SpiderLightPassivationEvent;
import Reika.DragonAPI.Libraries.Java.ReikaRandomHelper;
import Reika.DragonAPI.Libraries.ReikaEntityHelper;
import Reika.DragonAPI.Libraries.ReikaNBTHelper.NBTTypes;
import Reika.DragonAPI.Libraries.ReikaPlayerAPI;
import Reika.Satisforestry.API.PointSpawnLocation;
import Reika.Satisforestry.API.SFAPI.PinkForestSpawningHandler;
import Reika.Satisforestry.Biome.BiomeFootprint;
import Reika.Satisforestry.Blocks.BlockCaveSpawner.TileCaveSpawner;
import Reika.Satisforestry.Blocks.PointSpawnBlock;
import Reika.Satisforestry.Blocks.PointSpawnBlock.PointSpawnTile;
import Reika.Satisforestry.Entity.EntityEliteStinger;
import Reika.Satisforestry.Entity.SpawnPointEntity;
import Reika.Satisforestry.Satisforestry;
import com.google.common.base.Strings;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public final class PointSpawnSystem implements PinkForestSpawningHandler {
    private static final HashMap<String, SpawnPointDefinition> spawnerTypes
        = new HashMap();
    private static final HashMap<Class<? extends SpawnPoint>, String> spawnerTypeClasses
        = new HashMap();

    public static final PointSpawnSystem instance = new PointSpawnSystem();

    private static final String SPAWN_NBT_TAG = "PinkForestPointSpawn";
    private static final String KILLED_NBT_TAG = "playerKilled";
    private static final String HOSTILE_NBT_TAG = "alwaysHostile";

    private final LizardDoggoSpawner doggos;
    private final RoadGuardSpawner guards;

    private final HashSet<WorldLocation> locationsUsed = new HashSet();
    private final HashMap<Integer, TileEntityCache<SpawnPoint>> spawns = new HashMap();

    private PointSpawnSystem() {
        doggos = new LizardDoggoSpawner();
        guards = new RoadGuardSpawner();
    }

    @SubscribeEvent
    public void clearDropsForClearedEntities(LivingDropsEvent evt) {
        Entity e = evt.source.getEntity();
        if (e instanceof EntityPlayer) {
            return;
        }
        if (evt.entityLiving instanceof EntityLiving) {
            SpawnPoint p = this.getSpawn((EntityLiving) evt.entityLiving);
            if (p != null && p.clearNonPlayerDrops()) {
                evt.drops.clear();
                evt.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void deactivateSpawner(LivingDeathEvent evt) {
        if (evt.entityLiving instanceof EntityLiving
            && this.isValidPlayerKill(evt.source, (EntityLiving) evt.entityLiving)) {
            SpawnPoint p = this.getSpawn((EntityLiving) evt.entityLiving);
            if (p != null) {
                this.tagEntityAsKilled((EntityLiving) evt.entityLiving);
            }
        }
    }

    private boolean isValidPlayerKill(DamageSource source, EntityLiving el) {
        Entity e = source.getEntity();
        if (e == null || e.worldObj == null)
            return false;
        if (e instanceof EntityPlayer && !ReikaPlayerAPI.isFake((EntityPlayer) e))
            return true;
        if (e instanceof TamedCritter) {
            String n = ((TamedCritter) e).getMobOwner();
            if (n != null) {
                EntityPlayer ep = e.worldObj.getPlayerEntityByName(n);
                if (ep == null || ReikaPlayerAPI.isFake(ep))
                    return false;
                return ep.getDistanceSqToEntity(el) <= 256;
            }
        }
        return false;
    }

    @SubscribeEvent
    public void spidersAlwaysHostile(SpiderLightPassivationEvent evt) {
        if (evt.spider instanceof EntityEliteStinger
            || this.isAlwaysHostile(evt.spider)) {
            evt.threshold = Float.POSITIVE_INFINITY;
        }
    }

    @SubscribeEvent
    public void handleEntityDespawn(EntityRemovedEvent evt) {
        if (evt.entity instanceof EntityLiving) {
            this.onEntityRemoved((EntityLiving) evt.entity);
        }
    }

    @SubscribeEvent
    public void runPointSpawners(LivingUpdateEvent evt) {
        if (!evt.entityLiving.worldObj.isRemote) {
            if (evt.entityLiving instanceof EntityPlayer) {
                this.tick((EntityPlayer) evt.entityLiving);
            } else if (evt.entityLiving instanceof EntityLiving) {
                int follow
                    = getTag((EntityLiving) evt.entityLiving, TileCaveSpawner.FOLLOW_TAG);
                if (follow > 0) {
                    WorldLocation s
                        = this.getSpawnLocation((EntityLiving) evt.entityLiving);
                    if (s != null) {
                        double dist = evt.entityLiving.getDistanceSq(
                            s.xCoord + 0.5, s.yCoord + 0.5, s.zCoord + 0.5
                        );
                        if (dist >= follow * follow)
                            evt.entityLiving.setDead();
                    }
                }
            }
        }
    }

    public static void registerSpawnerType(SpawnPointDefinition c) {
        spawnerTypes.put(c.getID(), c);
        spawnerTypeClasses.put(c.getSpawnerClass(), c.getID());
    }

    public void
    createSpawnPoints(World world, int x, int z, BiomeFootprint bf, Random rand) {
        Collection<SpawnPoint> spawns = doggos.createDoggoSpawnPoints(world, bf, rand);
        Satisforestry.logger.log(
            "Doggo spawn locations around " + x + ", " + z + ": " + spawns.size()
        );
        this.addSpawnPoints(spawns);

        spawns = guards.createSpawnPoints(world, bf, rand);
        Satisforestry.logger.log(
            "Road Guard spawn locations around " + x + ", " + z + ": " + spawns.size()
        );
        this.addSpawnPoints(spawns);
    }

    public void addSpawnPoints(Collection<SpawnPoint> c) {
        for (SpawnPoint s : c) {
            this.addSpawnPoint(s);
        }
    }

    public void addSpawnPoint(SpawnPoint s) {
        if (Strings.isNullOrEmpty(s.id))
            s.id = spawnerTypeClasses.get(s.getClass());
        if (Strings.isNullOrEmpty(s.id))
            throw new IllegalArgumentException("Untyped spawner " + s + "!");
        if (spawnerTypes.get(s.id) == null)
            throw new IllegalArgumentException("Unregistered spawner type " + s.id + "!");
        if (s.location == null)
            throw new IllegalArgumentException(
                "Spawnpoint " + s + " has a null location!"
            );
        TileEntityCache<SpawnPoint> map = spawns.get(s.location.dimensionID);
        if (map == null) {
            map = new TileEntityCache();
            spawns.put(s.location.dimensionID, map);
        }
        map.put(s);
        locationsUsed.add(s.location);
    }

    public void clear() {
        spawns.clear();
        locationsUsed.clear();
    }

    public void loadSpawnPoints(NBTTagList li) {
        this.loadSpawnPoints(li, null);
    }

    public void loadLegacyDoggoSpawns(NBTTagList li) {
        this.loadSpawnPoints(li, "doggo");
    }

    private void loadSpawnPoints(NBTTagList li, String typeOverride) {
        for (Object o : li.tagList) {
            NBTTagCompound tag = (NBTTagCompound) o;
            if (!Strings.isNullOrEmpty(typeOverride))
                tag.setString("spawnerType", typeOverride);
            SpawnPoint c = this.constructSpawn(tag);
            if (c == null)
                continue;
            c.readFromTag(tag);
            if (c.isDead())
                continue;
            this.addSpawnPoint(c);
        }
    }

    private SpawnPoint constructSpawn(NBTTagCompound tag) {
        WorldLocation loc = WorldLocation.readFromNBT("loc", tag);
        if (loc == null)
            return null;
        String type = tag.getString("spawnerType");
        SpawnPointDefinition c = spawnerTypes.get(type);
        if (c == null) {
            Satisforestry.logger.logError(
                "Could not construct spawnpoint of unrecognized/null-mapped type '" + type
                + "': " + tag
            );
            return null;
        }
        SpawnPoint s = c.construct(loc);
        if (s != null)
            s.id = type;
        return s;
    }

    public void saveSpawnPoints(NBTTagList li) {
        for (TileEntityCache<SpawnPoint> map : spawns.values()) {
            for (SpawnPoint loc : map.values()) {
                if (loc.isDead())
                    continue;
                NBTTagCompound tag = new NBTTagCompound();
                loc.writeToTag(tag);
                if (Strings.isNullOrEmpty(loc.id)) {
                    Satisforestry.logger.logError(
                        "Could not save spawnpoint of unrecognized/null-mapped type '"
                        + loc.id + "': " + loc
                    );
                    continue;
                }
                tag.setString("spawnerType", loc.id);
                li.appendTag(tag);
            }
        }
    }

    public Collection<SpawnPoint> getWorldSpawns(World world) {
        TileEntityCache<SpawnPoint> map = spawns.get(world.provider.dimensionId);
        return map != null ? Collections.unmodifiableCollection(map.values())
                           : new ArrayList();
    }

    public PointSpawnLocation getNearestSpawnPoint(EntityPlayer ep, double r) {
        return this.getNearestSpawnPointOfType(ep, r, null);
    }

    public PointSpawnLocation getNearestSpawnPointOfType(
        EntityPlayer ep, double r, Class<? extends EntityLiving> c
    ) {
        if (!Satisforestry.isPinkForest(
                ep.worldObj,
                MathHelper.floor_double(ep.posX),
                MathHelper.floor_double(ep.posZ)
            ))
            return null;
        TileEntityCache<SpawnPoint> map = spawns.get(ep.worldObj.provider.dimensionId);
        if (map == null)
            return null;
        double dist = Double.POSITIVE_INFINITY;
        WorldLocation ret = null;
        for (WorldLocation loc : map.getAllLocationsNear(new WorldLocation(ep), r)) {
            SpawnPoint s = this.getSpawnAt(loc);
            if (s == null)
                continue;
            if (c != null && c != s.mobClass)
                continue;
            if (ret == null || loc.getDistanceTo(ep) < dist) {
                dist = loc.getDistanceTo(ep);
                ret = loc;
            }
        }
        return ret != null ? map.get(ret) : null;
    }

    public SpawnPoint getSpawnAt(WorldLocation loc) {
        if (loc == null)
            return null;
        if (loc.getBlock() instanceof PointSpawnBlock) {
            TileEntity tile
                = loc.getWorld().getTileEntity(loc.xCoord, loc.yCoord, loc.zCoord);
            return tile instanceof PointSpawnTile ? ((PointSpawnTile) tile).getSpawner()
                                                  : null;
        }
        TileEntityCache<SpawnPoint> map = spawns.get(loc.dimensionID);
        if (map == null)
            return null;
        return map.get(loc);
    }

    public SpawnPoint getSpawn(EntityLiving e) {
        return this.getSpawnAt(this.getSpawnLocation(e));
    }

    public PointSpawnLocation getEntitySpawnPoint(EntityLiving e) {
        return this.getSpawn(e);
    }

    private WorldLocation getSpawnLocation(EntityLiving e) {
        if (e instanceof SpawnPointEntity) {
            return ((SpawnPointEntity) e).getSpawn();
        } else {
            NBTTagCompound tag = e.getEntityData().getCompoundTag(SPAWN_NBT_TAG);
            if (tag.hasNoTags())
                return null;
            return WorldLocation.readFromNBT("location", tag);
        }
    }

    private void onEntityRemoved(EntityLiving e) {
        if (e.worldObj == null || e.worldObj.isRemote)
            return;
        SpawnPoint spawn = this.getSpawn(e);
        if (spawn != null) {
            spawn.removeEntity(e);
        }
    }

    private void tagEntityAsKilled(EntityLiving e) {
        setTag(e, KILLED_NBT_TAG, true);
    }

    public static void setTag(EntityLiving e, String key, int value) {
        NBTTagCompound tag = e.getEntityData().getCompoundTag(SPAWN_NBT_TAG);
        tag.setInteger(key, value);
        e.getEntityData().setTag(SPAWN_NBT_TAG, tag);
    }

    public static void setTag(EntityLiving e, String key, boolean flag) {
        NBTTagCompound tag = e.getEntityData().getCompoundTag(SPAWN_NBT_TAG);
        tag.setBoolean(key, flag);
        e.getEntityData().setTag(SPAWN_NBT_TAG, tag);
    }

    public static boolean hasTag(EntityLiving e, String key) {
        return e.getEntityData().getCompoundTag(SPAWN_NBT_TAG).getBoolean(key);
    }

    public static int getTag(EntityLiving e, String key) {
        return e.getEntityData().getCompoundTag(SPAWN_NBT_TAG).getInteger(key);
    }

    public boolean isAlwaysHostile(EntityLiving e) {
        return this.hasTag(e, HOSTILE_NBT_TAG);
    }

    private void tick(EntityPlayer ep) {
        World world = ep.worldObj;
        if (world == null || world.isRemote)
            return;
        long time = world.getTotalWorldTime();
        if (time % 5 == 0
            && Satisforestry.isPinkForest(
                ep.worldObj,
                MathHelper.floor_double(ep.posX),
                MathHelper.floor_double(ep.posZ)
            )) {
            TileEntityCache<SpawnPoint> map = spawns.get(world.provider.dimensionId);
            if (map == null)
                return;
            int dist = this.getTickDist(time);
            if (dist < 0) {
                for (SpawnPoint loc : map.values()) {
                    loc.tick(ep.worldObj, ep);
                }
            } else {
                for (WorldLocation loc :
                     map.getAllLocationsNear(new WorldLocation(ep), dist)) {
                    map.get(loc).tick(ep.worldObj, ep);
                }
            }
        }
    }

    private int getTickDist(long time) {
        if (time % 100 == 0)
            return -1;
        if (time % 40 == 0)
            return 128;
        return time % 20 == 0 ? 64 : 32;
    }

    public void removeSpawner(SpawnPoint p) {
        TileEntityCache<SpawnPoint> map = spawns.get(p.getDimension());
        if (map == null)
            return;
        map.remove(p);
        p.isDead = true;
    }

    public static abstract class SpawnPoint implements PointSpawnLocation, LocationEntry {
        private final WorldLocation location;

        private int numberToSpawn;
        private double activationRadius;
        private Class<? extends EntityLiving> mobClass;
        private String mobType;
        private int emptyTimeout = -1;

        private int existingCount = 0;
        private int playerKilled = 0;

        private long lastTick;
        private long emptyTicks;
        private boolean isDead;

        private String id;

        private final ArrayList<UUID> spawnedMobs = new ArrayList();

        /*
        protected SpawnPoint(World world, Coordinate c) {
            this(new WorldLocation(world, c));
        }
         */
        protected SpawnPoint(WorldLocation loc) {
            location = loc;
        }

        protected final WorldChunk getChunk() {
            //WorldLocation loc = this.getLocation();
            //return new WorldChunk(loc.dimensionID, loc.xCoord >> 4, loc.zCoord >> 4);
            return this.getLocation().getChunk();
        }

        public WorldLocation getLocation() {
            return location;
        }

        public int getDimension() {
            return this.getLocation().dimensionID;
        }

        public int getX() {
            return this.getLocation().xCoord;
        }

        public int getY() {
            return this.getLocation().yCoord;
        }

        public int getZ() {
            return this.getLocation().zCoord;
        }

        public void setSpawnParameters(Class<? extends EntityLiving> c, int n, double r) {
            mobClass = c;
            mobType = (String) EntityList.classToStringMapping.get(c);
            numberToSpawn = n;
            activationRadius = r;
        }

        @Override
        public final String toString() {
            return this.getClass().getName() + " @ " + this.getLocation().toString()
                + this.getInfoString() + " [" + mobType + " x" + numberToSpawn + "]";
        }

        protected String getInfoString() {
            return "";
        }

        @Override
        public final int hashCode() {
            return this.getLocation().hashCode();
        }

        @Override
        public final boolean equals(Object o) {
            return o instanceof SpawnPoint
                && this.getLocation().equals(((SpawnPoint) o).getLocation());
        }

        public final boolean isDead() {
            return isDead;
        }

        public void writeToTag(NBTTagCompound ret) {
            if (location != null)
                location.writeToNBT("loc", ret);
            ret.setDouble("radius", activationRadius);
            ret.setInteger("count", numberToSpawn);
            ret.setInteger("exists", existingCount);
            ret.setInteger("killed", playerKilled);
            ret.setInteger("timeout", emptyTimeout);
            ret.setLong("tick", lastTick);
            ret.setLong("empty", emptyTicks);
            ret.setBoolean("dead", isDead);
            if (!Strings.isNullOrEmpty(mobType))
                ret.setString("mob", mobType);
            if (mobClass != null)
                ret.setString("type", mobClass.getName());

            NBTTagList li = new NBTTagList();
            for (UUID uid : spawnedMobs) {
                li.appendTag(new NBTTagString(uid.toString()));
            }
            ret.setTag("spawned", li);
        }

        public void readFromTag(NBTTagCompound NBT) {
            activationRadius = NBT.getDouble("radius");
            numberToSpawn = NBT.getInteger("count");
            existingCount = NBT.getInteger("exists");
            playerKilled = NBT.getInteger("killed");
            emptyTimeout = NBT.getInteger("timeout");
            lastTick = NBT.getLong("tick");
            emptyTicks = NBT.getLong("empty");
            isDead = NBT.getBoolean("dead");
            mobType = NBT.hasKey("mob") ? NBT.getString("mob") : "";
            try {
                String cl = NBT.getString("type");
                mobClass = Strings.isNullOrEmpty(cl)
                    ? null
                    : (Class<? extends EntityLiving>) Class.forName(cl);
            } catch (ClassNotFoundException e) {
                Satisforestry.logger.logError(
                    "Could not find mob type for spawn point: " + NBT
                );
                mobClass = (Class<? extends EntityLiving>)
                               EntityList.stringToClassMapping.get(mobType);
            }

            spawnedMobs.clear();
            NBTTagList li = NBT.getTagList("spawned", NBTTypes.STRING.ID);
            for (Object o : li.tagList) {
                spawnedMobs.add(UUID.fromString(((NBTTagString) o).func_150285_a_()));
            }
        }

        protected void tick(World world, EntityPlayer ep) {
            if (isDead)
                return;
            long time = world.getTotalWorldTime();
            if (time == lastTick)
                return;
            lastTick = time;
            WorldLocation loc = this.getLocation();
            if (loc == null)
                return;
            if (playerKilled >= numberToSpawn && !this.isClearingPermanent()
                && this.isEmptyTimeoutActive(world)) {
                emptyTicks++;
                if (emptyTicks >= emptyTimeout) {
                    playerKilled = 0;
                    emptyTicks = 0;
                }
            }
            if (mobClass == null || Strings.isNullOrEmpty(mobType))
                return;
            if (EntityMob.class.isAssignableFrom(mobClass)
                && world.difficultySetting == EnumDifficulty.PEACEFUL)
                return;
            if (ep == null)
                ep = world.getClosestPlayer(
                    loc.xCoord + 0.5, loc.yCoord + 0.5, loc.zCoord + 0.5, -1
                );
            if (ep == null)
                return;
            int amt = numberToSpawn - existingCount - playerKilled;
            int last = existingCount;
            int last2 = playerKilled;
            double dist
                = ep.getDistanceSq(loc.xCoord + 0.5, loc.yCoord + 0.5, loc.zCoord + 0.5);
            if (amt > 0) {
                if (dist <= activationRadius * activationRadius) {
                    ArrayList<EntityLiving> spawned = new ArrayList();
                    for (int i = 0; i < amt; i++) {
                        if (this.attemptSpawn(world, loc, spawned)) {
                            existingCount++;
                        }
                    }
                }
            }
            if (playerKilled < numberToSpawn) {
                double reset = this.getResetRadius();
                //ReikaJavaLibrary.pConsole(this+"
                //["+numberToSpawn+"/"+existingCount+"/"+playerKilled+"] =
                //"+Math.sqrt(dist)+"/"+reset, playerKilled > 0);
                if (reset >= 0 && dist >= reset * reset) {
                    double reset2 = this.getAutoClearRadius();
                    this.resetMobs(world, dist >= reset2 * reset2);
                }
            }
            if ((existingCount != last || playerKilled != last2) && location != null)
                BiomewideFeatureGenerator.instance.save(world);
        }

        public final double getActivationRadius() {
            return activationRadius;
        }

        public double getResetRadius() {
            return activationRadius * 4;
        }

        public double getAutoClearRadius() {
            return this.getResetRadius() * 1.5;
        }

        protected final void resetMobs(World world, boolean deleteSpawned) {
            if (deleteSpawned) {
                for (UUID uid : spawnedMobs) {
                    Entity e = ReikaEntityHelper.getEntityByUID(world, uid);
                    if (e instanceof EntityLiving) {
                        e.setDead();
                    }
                }
                existingCount = 0;
            }
            playerKilled = 0;
        }

        protected boolean isEmptyTimeoutActive(World world) {
            return false;
        }

        public final void setEmptyTimeout(int ticks) {
            emptyTimeout = ticks;
        }

        private final void removeEntity(EntityLiving e) {
            //ReikaJavaLibrary.pConsole("Removed "+e+" from "+this);
            spawnedMobs.remove(e.getUniqueID());
            existingCount--;
            if (existingCount < 0)
                existingCount = 0;
            if (this.canBeCleared()) {
                if (hasTag(e, KILLED_NBT_TAG))
                    playerKilled++;
                if (playerKilled >= numberToSpawn && this.isClearingPermanent())
                    this.delete();
            }
            if (location != null)
                BiomewideFeatureGenerator.instance.save(e.worldObj);
        }

        public final int getActiveSpawnCap() {
            return numberToSpawn - playerKilled;
        }

        public final int getCurrentlySpawned() {
            return existingCount;
        }

        public boolean canBeCleared() {
            return true;
        }

        protected boolean isClearingPermanent() {
            return emptyTimeout < 0;
        }

        protected void delete() {
            instance.removeSpawner(this);
        }

        protected final EntityLiving
        getRandomPlacedEntity(double r, World world, int cx, int cy, int cz) {
            EntityLiving e = this.constructEntity(world);
            if (e != null) {
                double minX = cx + 0.5 - r;
                double maxX = cx + 0.5 + r;
                double minZ = cz + 0.5 - r;
                double maxZ = cz + 0.5 + r;
                double x = ReikaRandomHelper.getRandomBetween(minX, maxX);
                double z = ReikaRandomHelper.getRandomBetween(minZ, maxZ);
                e.setLocationAndAngles(
                    x, cy + 1 + world.rand.nextDouble() * 1.5, z, 0, 0
                );
            }
            return e;
        }

        protected final EntityLiving constructEntity(World world) {
            return (EntityLiving) EntityList.createEntityByName(mobType, world);
        }

        protected abstract EntityLiving
        getSpawn(World world, int cx, int cy, int cz, Random rand);

        public final Class<? extends EntityLiving> getSpawnType() {
            return mobClass;
        }

        private boolean
        attemptSpawn(World world, WorldLocation loc, ArrayList<EntityLiving> spawned) {
            EntityLiving e
                = this.getSpawn(world, loc.xCoord, loc.yCoord, loc.zCoord, world.rand);
            if (e != null) {
                int i = 0;
                while (!this.canSpawnAt(e) && i < 5) {
                    e.setLocationAndAngles(
                        e.posX, e.posY + 0.5, e.posZ, e.rotationYaw, e.rotationPitch
                    );
                    i++;
                }
                if (this.canSpawnAt(e)) {
                    e.rotationYaw = world.rand.nextFloat() * 360;
                    this.setSpawnCallback(e, loc);
                    spawnedMobs.add(e.getUniqueID());
                    //e.onSpawnWithEgg((IEntityLivingData)null); no jockeys or potions
                    world.spawnEntityInWorld(e);
                    e.spawnExplosionParticle();
                    //this.worldObj.playAuxSFX(2004, xCoord, yCoord, zCoord, 0);
                    if (this.denyPassivation()) {
                        setTag(e, HOSTILE_NBT_TAG, true);
                    }
                    spawned.add(e);
                    //ReikaJavaLibrary.pConsole("Spawned "+e+" @ "+this+", has
                    //"+existingCount+"/"+numberToSpawn);
                    this.onEntitySpawned(e, spawned);
                    return true;
                }
            }
            return false;
        }

        private boolean canSpawnAt(EntityLiving e) {
            return e.getCanSpawnHere() && this.canSpawnEntityAt(e);
        }

        protected boolean canSpawnEntityAt(EntityLiving e) {
            return true;
        }

        protected boolean denyPassivation() {
            return false;
        }

        public boolean clearNonPlayerDrops() {
            return true;
        }

        protected void onEntitySpawned(EntityLiving e, ArrayList<EntityLiving> spawned) {}

        private void setSpawnCallback(EntityLiving e, WorldLocation loc) {
            if (e instanceof SpawnPointEntity) {
                ((SpawnPointEntity) e).setSpawn(loc);
            } else {
                NBTTagCompound tag = new NBTTagCompound();
                loc.writeToNBT("location", tag);
                e.getEntityData().setTag(SPAWN_NBT_TAG, tag);
            }
        }

        public boolean isBlock() {
            return false;
        }
    }

    public static interface SpawnPointDefinition {
        public SpawnPoint construct(WorldLocation loc);
        public String getID();
        public Class<? extends SpawnPoint> getSpawnerClass();
    }
}
