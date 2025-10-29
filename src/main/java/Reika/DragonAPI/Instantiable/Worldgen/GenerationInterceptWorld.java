package Reika.DragonAPI.Instantiable.Worldgen;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import Reika.DragonAPI.Instantiable.Data.Immutable.BlockKey;
import Reika.DragonAPI.Instantiable.Data.Immutable.Coordinate;
import Reika.DragonAPI.Instantiable.Data.Maps.BlockMap;
import Reika.DragonAPI.Instantiable.Data.Maps.MultiMap;
import Reika.DragonAPI.Instantiable.Data.Maps.MultiMap.CollectionType;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.util.ForgeDirection;

@Deprecated
/** This class was, in hindsight, a dumb idea. */
public final class GenerationInterceptWorld extends World {
    private World delegate;

    private final HashSet<BlockKey> disallowedBlocks = new HashSet();
    private final MultiMap<BlockKey, BlockKey> disallowedChanges
        = new MultiMap(CollectionType.HASHSET);
    private final HashSet<Coordinate> changeList = new HashSet();
    private final Collection<TileHook> hooks = new ArrayList();
    private final BlockMap<BlockKey> overrides = new BlockMap();

    @Deprecated
    public GenerationInterceptWorld() {
        super(
            new NoSaveHandler(),
            null,
            new WorldSettings(0, GameType.NOT_SET, false, false, WorldType.DEFAULT),
            null,
            null
        );
    }

    public void link(World world) {
        //changeList.clear();

        if (delegate == world)
            return;

        /*
        if (world == null) {
            try {
                delegate = null;
                boolean obf = !ReikaObfuscationHelper.isDeObfEnvironment();
                ReikaReflectionHelper.setFinalField(World.class, obf ? "field_73019_z" :
        "saveHandler", this, null); ReikaReflectionHelper.setFinalField(World.class, obf ?
        "field_73011_w" : "provider", this, null); worldInfo = null;
                ReikaReflectionHelper.setFinalField(World.class, obf ? "field_72984_F" :
        "theProfiler", this, null); mapStorage = null; chunkProvider = null;
                ReikaReflectionHelper.setFinalField(World.class, obf ? "perWorldStorage" :
        "perWorldStorage", this, null); villageCollectionObj = null;
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
            return;
        }
         */

        delegate = world;

        /*
        try {
            boolean obf = !ReikaObfuscationHelper.isDeObfEnvironment();
            ReikaReflectionHelper.setFinalField(World.class, obf ? "field_73019_z" :
        "saveHandler", this, world.getSaveHandler());
            ReikaReflectionHelper.setFinalField(World.class, obf ? "field_73011_w" :
        "provider", this, world.provider); worldInfo = world.getWorldInfo();
            ReikaReflectionHelper.setFinalField(World.class, obf ? "field_72984_F" :
        "theProfiler", this, world.theProfiler); mapStorage = world.mapStorage;
            chunkProvider = world.getChunkProvider();
            ReikaReflectionHelper.setFinalField(World.class, obf ? "perWorldStorage" :
        "perWorldStorage", this, world.perWorldStorage); villageCollectionObj =
        world.villageCollectionObj;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
         */
    }

    public void disallowBlock(Block b) {
        disallowedBlocks.add(new BlockKey(b));
    }

    public void disallowBlock(Block b, int meta) {
        disallowedBlocks.add(new BlockKey(b, meta));
    }

    public void disallowBlock(BlockKey bk) {
        disallowedBlocks.add(bk);
    }

    public void disallowBlockChange(Block b, Block b2) {
        this.disallowBlockChange(new BlockKey(b), new BlockKey(b2));
    }

    public void disallowBlockChange(BlockKey b, BlockKey b2) {
        disallowedChanges.addValue(b, b2);
    }

    public void addHook(TileHook th) {
        hooks.add(th);
    }

    public void addGetOverride(Block b, int meta, Block overb) {
        this.addGetOverride(b, meta, overb, 0);
    }

    public void addGetOverride(Block b, int meta, Block overb, int overm) {
        this.addGetOverride(b, meta, new BlockKey(overb, overm));
    }

    public void addGetOverride(Block b, int meta, BlockKey override) {
        overrides.put(b, meta, override);
    }

    private boolean check(int x, int y, int z, Block b, int meta) {
        BlockKey bk = new BlockKey(b, meta);
        if (disallowedBlocks.contains(bk))
            return false;
        if (disallowedChanges.get(BlockKey.getAt(delegate, x, y, z)).contains(bk))
            return false;
        return true;
    }

    @Override
    public boolean setBlock(int x, int y, int z, Block b) {
        boolean flag = this.check(x, y, z, b, 0) ? delegate.setBlock(x, y, z, b) : false;
        if (flag) {
            this.markHook(x, y, z);
        }
        return flag;
    }

    @Override
    public boolean setBlock(int x, int y, int z, Block b, int meta, int flags) {
        boolean flag = this.check(x, y, z, b, meta)
            ? delegate.setBlock(x, y, z, b, meta, flags)
            : false;
        if (flag) {
            this.markHook(x, y, z);
        }
        return flag;
    }

    @Override
    public boolean setBlockMetadataWithNotify(int x, int y, int z, int meta, int flags) {
        boolean flag = this.check(x, y, z, delegate.getBlock(x, y, z), meta)
            ? delegate.setBlockMetadataWithNotify(x, y, z, meta, flags)
            : false;
        if (flag) {
            this.markHook(x, y, z);
        }
        return flag;
    }

    @Override
    public void setTileEntity(int x, int y, int z, TileEntity te) {
        //if (changeList.contains(new Coordinate(x, y, z)))
        delegate.setTileEntity(x, y, z, te);
        this.markHook(x, y, z);
    }

    /**/
    @Override
    public Block getBlock(int x, int y, int z) {
        Block ret = delegate.getBlock(x, y, z);
        BlockKey over = overrides.get(ret, delegate.getBlockMetadata(x, y, z));
        return over != null ? over.blockID : ret;
    }

    @Override
    public int getBlockMetadata(int x, int y, int z) {
        int ret = delegate.getBlockMetadata(x, y, z);
        BlockKey over = overrides.get(delegate.getBlock(x, y, z), ret);
        return over != null ? over.metadata : ret;
    }

    @Override
    public TileEntity getTileEntity(int x, int y, int z) {
        return delegate.getTileEntity(x, y, z);
    }

    @Override
    public int getBlockLightValue(int x, int y, int z) {
        return delegate.getBlockLightValue(x, y, z);
    }

    @Override
    public int getTopSolidOrLiquidBlock(int x, int z) {
        return delegate.getTopSolidOrLiquidBlock(x, z);
    }

    @Override
    protected boolean chunkExists(int x, int z) {
        return delegate.getChunkProvider().chunkExists(x, z); //its internal code
    }

    @Override
    public Chunk getChunkFromChunkCoords(int x, int z) {
        return delegate.getChunkFromChunkCoords(x, z);
    }

    @Override
    public BiomeGenBase getBiomeGenForCoordsBody(int x, int z) {
        return delegate.getBiomeGenForCoordsBody(x, z);
    }

    @Override
    public boolean
    isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default) {
        return delegate.isSideSolid(x, y, z, side, _default);
    }

    @Override
    public IChunkProvider getChunkProvider() {
        return delegate.getChunkProvider();
    }

    @Override
    public WorldInfo getWorldInfo() {
        return delegate.getWorldInfo();
    }

    @Override
    public GameRules getGameRules() {
        return delegate.getGameRules();
    }

    @Override
    public boolean isBlockNormalCubeDefault(int x, int y, int z, boolean def) {
        return delegate.isBlockNormalCubeDefault(x, y, z, def);
    }

    @Override
    public String getProviderName() {
        return delegate.getProviderName();
    }

    private void markHook(int x, int y, int z) {
        changeList.add(new Coordinate(x, y, z));
    }

    /*
    private void runHooks(int x, int y, int z) {
        TileEntity te = delegate.getTileEntity(x, y, z);
        for (TileHook th : hooks) {
            if (th.shouldRun(delegate, x, y, z))
                th.onTileChanged(te);
        }
    }
     */

    public void runHooks() {
        for (TileHook th : hooks) {
            for (Coordinate c : changeList) {
                if (th.shouldRun(delegate, c.xCoord, c.yCoord, c.zCoord)) {
                    TileEntity te = c.getTileEntity(delegate);
                    th.onTileChanged(te);
                }
            }
        }
        changeList.clear();
    }

    @Override
    public boolean spawnEntityInWorld(Entity e) {
        return delegate.spawnEntityInWorld(e);
    }

    @Override
    protected IChunkProvider createChunkProvider() {
        return null;
    }

    @Override
    protected int func_152379_p() {
        return 10; //chunk load distance
    }

    @Override
    public Entity getEntityByID(int id) {
        return delegate.getEntityByID(id);
    }

    @Override
    public void updateEntities() { //not in worldgen!
    }

    @Override
    public void updateEntity(Entity e) { //not in worldgen!
    }

    @Override
    public void updateEntityWithOptionalForce(Entity e, boolean flag) { //not in worldgen!
    }

    private static class NoSaveHandler implements ISaveHandler {
        @Override
        public WorldInfo loadWorldInfo() {
            return null;
        }

        @Override
        public void checkSessionLock() throws MinecraftException {}

        @Override
        public IChunkLoader getChunkLoader(WorldProvider p_75763_1_) {
            return null;
        }

        @Override
        public void
        saveWorldInfoWithPlayer(WorldInfo p_75755_1_, NBTTagCompound p_75755_2_) {}

        @Override
        public void saveWorldInfo(WorldInfo p_75761_1_) {}

        @Override
        public IPlayerFileData getSaveHandler() {
            return null;
        }

        @Override
        public void flush() {}

        @Override
        public File getWorldDirectory() {
            return null;
        }

        @Override
        public File getMapFileFromName(String p_75758_1_) {
            return null;
        }

        @Override
        public String getWorldDirectoryName() {
            return null;
        }
    }

    public static interface TileHook {
        /** May be null! */
        public void onTileChanged(TileEntity te);

        public boolean shouldRun(World world, int x, int y, int z);
    }
}
