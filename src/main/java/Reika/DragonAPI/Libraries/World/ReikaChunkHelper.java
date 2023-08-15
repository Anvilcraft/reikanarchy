/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.Libraries.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.Instantiable.Data.Immutable.Coordinate;
import Reika.DragonAPI.Interfaces.Entity.DestroyOnUnload;
import Reika.DragonAPI.Libraries.Java.ReikaJavaLibrary;
import Reika.DragonAPI.Libraries.MathSci.ReikaMathLibrary;
import Reika.DragonAPI.Libraries.ReikaEntityHelper.ClassEntitySelector;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.ChunkProviderServer;

public final class ReikaChunkHelper extends DragonAPICore {
    /** Returns the chunk at the given coords. Args: World, x, z */
    public static Chunk getChunk(World world, int x, int z) {
        Chunk in = world.getChunkFromBlockCoords(x, z);
        return in;
    }

    /** Regens a chunk from the seed. Args: World, x, z (block coords) */
    public static void regenChunk(WorldServer world, int x, int z) {
        ChunkProviderServer prov = (ChunkProviderServer) world.getChunkProvider();
        IChunkProvider gen = prov.currentChunkProvider;
        Chunk regen = gen.provideChunk(x >> 4, z >> 4);
        copyChunk(world.getChunkFromBlockCoords(x, z), regen);

        gen.populate(prov, x >> 4, z >> 4);
        GameRegistry.generateWorld(x >> 4, z >> 4, world, gen, prov);
        regen.setChunkModified();
        world.getBiomeGenForCoords(x + 16, z + 16).decorate(world, rand, x, z);
    }

    private static void copyChunk(Chunk tgt, Chunk repl) {
        tgt.setStorageArrays(repl.getBlockStorageArray());
        tgt.setBiomeArray(repl.getBiomeArray());
        for (ChunkPosition cc :
             ((Collection<ChunkPosition>) tgt.chunkTileEntityMap.keySet())) {
            tgt.removeTileEntity(cc.chunkPosX, cc.chunkPosY, cc.chunkPosZ);
        }
        tgt.chunkTileEntityMap = repl.chunkTileEntityMap;
        for (TileEntity te : ((Collection<TileEntity>) tgt.chunkTileEntityMap.values())) {
            tgt.addTileEntity(te);
        }
        //tgt.entityLists = repl.entityLists;
    }

    /**
     * Deletes ALL entities of all kinds (except players) in a chunk.
     * Only goes up to y=255! Args: World, x, z
     */
    public static void emptyChunk(World world, int x, int z) {
        while (x % 16 > 0) {
            x--;
        }
        while (z % 16 > 0) {
            z--;
        }
        AxisAlignedBB chunk = AxisAlignedBB.getBoundingBox(x, 0, z, x + 16, 255, z + 16);
        List inChunk = world.getEntitiesWithinAABB(Entity.class, chunk);
        for (int i = 0; i < inChunk.size(); i++) {
            Entity ent = (Entity) inChunk.get(i);
            if (!(ent instanceof EntityPlayer))
                ent.setDead();
        }
    }

    /**
     * Deletes all the blocks in an entire chunk, except the specified ID. Enter -1 to
     * empty the chunk entirely. Args: World, x, z, id to save
     */
    public static void deleteChunk(World world, int x, int z, Block id) {
        Iterator it = Block.blockRegistry.iterator();
        while (it.hasNext()) {
            Block b = (Block) it.next();
            if (id != b)
                removeIDFromChunk(world, x, z, b);
        }
    }

    /**
     * Deletes all entities of the given class in a chunk.
     * Only goes up to y=255! Args: World, x, z
     */
    public static void removeFromChunk(World world, int x, int z, Class entityClass) {
        while (x % 16 > 0) {
            x--;
        }
        while (z % 16 > 0) {
            z--;
        }
        AxisAlignedBB chunk = AxisAlignedBB.getBoundingBox(x, 0, z, x + 16, 255, z + 16);
        List inChunk = world.getEntitiesWithinAABB(entityClass, chunk);
        for (int i = 0; i < inChunk.size(); i++) {
            Entity ent = (Entity) inChunk.get(i);
            ent.setDead();
        }
    }

    /**
     * Removes all blocks of specified ID and metadata from the chunk (replaces with
     * air). Set metadata to -1 for all. Args: World, x, z, Block ID, metadata
     */
    public static void
    removeBlocksFromChunk(World world, int x, int z, Block id, int meta) {
        if (meta == -1) {
            removeIDFromChunk(world, x, z, id);
            return;
        }
        replaceBlocksInChunk(world, x, z, id, meta, Blocks.air, 0);
    }

    /**
     * Replaces all blocks of specified ID and metadata in the chunk with specified ID
     * and metadata. Set metadata to -1 for all. Args: World, x, z, Block ID, metadata,
     * ID-to, meta-to
     */
    public static void replaceBlocksInChunk(
        World world, int x, int z, Block id, int meta, Block setid, int setmeta
    ) {
        boolean nx = false;
        boolean nz = false;
        if (x < 0) {
            x = -x;
            nx = true;
        }
        if (z < 0) {
            z = -z;
            nz = true;
        }
        while (x % 16 > 0) {
            if (nx)
                x++;
            else
                x--;
        }
        while (z % 16 > 0) {
            if (nx)
                z++;
            else
                z--;
        }
        if (nx)
            x = -x;
        if (nz)
            z = -z;
        if (meta == -1) {
            replaceIDInChunk(world, x, z, id, setid, setmeta);
            return;
        }
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                for (int k = 0; k < 256; k++) {
                    Block idread = world.getBlock(x + i, k, z + j);
                    int metaread = world.getBlockMetadata(x + i, k, z + j);
                    if (idread == id && metaread == meta)
                        world.setBlock(x + i, k, z + j, setid, setmeta, 3);
                }
            }
        }
    }

    private static void
    replaceIDInChunk(World world, int x, int z, Block id, Block setid, int setmeta) {
        boolean nx = false;
        boolean nz = false;
        if (x < 0) {
            x = -x;
            nx = true;
        }
        if (z < 0) {
            z = -z;
            nz = true;
        }
        while (x % 16 > 0) {
            if (nx)
                x++;
            else
                x--;
        }
        while (z % 16 > 0) {
            if (nx)
                z++;
            else
                z--;
        }
        if (nx)
            x = -x;
        if (nz)
            z = -z;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                for (int k = 0; k < 256; k++) {
                    Block idread = world.getBlock(x + i, k, z + j);
                    if (idread == id)
                        world.setBlock(x + i, k, z + j, setid, setmeta, 3);
                }
            }
        }
    }

    private static void removeIDFromChunk(World world, int x, int z, Block id) {
        replaceIDInChunk(world, x, z, id, Blocks.air, 0);
    }

    /**
     * Returns the distance (3d cartesian) to the nearest entity of this species.
     * Args: World, entity, x,y,z of source, search range
     */
    public static double
    getPoplnDensity(World world, Entity entity, double x, double y, double z, double r) {
        double dist;
        Entity entityfound = world.findNearestEntityWithinAABB(
            entity.getClass(),
            AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1).expand(r, r, r),
            entity
        );
        if (entityfound == null)
            return -1; // If no entity in range, return -1
        dist = ReikaMathLibrary.py3d(
            x - entityfound.posX, y - entityfound.posY, z - entityfound.posZ
        );
        return dist;
    }

    /**
     * s/e. Returns the number of the specified entity class in the chunk containing by
     * x, z. Can only detect entities 'within' the chunk. Args: World, entity class, x, z
     */
    public static int getChunkPopln(World world, Class entity, int x, int z) {
        while (x % 16 > 0)
            x--;
        while (z % 16 > 0)
            z--;
        int entitiesfound
            = world
                  .getEntitiesWithinAABB(
                      entity, AxisAlignedBB.getBoundingBox(x, 0, z, x + 16, 255, z + 16)
                  )
                  .size();
        return entitiesfound;
    }

    /**
     * s/e. Returns the number of the specified entity class in the chunk range
     * containing by x, z. Can only detect entities 'within' the chunk. Args: World,
     * entity class, xmin, zmin, xmax, zmax
     */
    public static int
    getChunkRangePopln(World world, Class entity, int x, int z, int x2, int z2) {
        while (x % 16 > 0)
            x--;
        while (z % 16 > 0)
            z--;
        while (x % 16 > 0)
            x2--;
        while (z % 16 > 0)
            z2++;
        int entitiesfound
            = world
                  .getEntitiesWithinAABB(
                      entity, AxisAlignedBB.getBoundingBox(x, 0, z, x2, 255, z2)
                  )
                  .size();
        return entitiesfound;
    }

    public static Collection<Entity> getEntities(Chunk ch, ClassEntitySelector sel) {
        Collection<Entity> c = new ArrayList();
        for (int i = 0; i < ch.entityLists.length; i++) {
            List<Entity> li = ch.entityLists[i];
            for (Entity e : li) {
                if (sel == null || sel.isEntityApplicable(e)) {
                    c.add(e);
                }
            }
        }
        return c;
    }

    public static void clearEntities(Chunk ch, ClassEntitySelector sel) {
        for (int i = 0; i < ch.entityLists.length; i++) {
            List<Entity> li = ch.entityLists[i];
            for (Entity e : li) {
                if (sel == null || sel.isEntityApplicable(e)) {
                    e.setDead();
                }
            }
        }
    }

    public static void clearUnloadableEntities(Chunk ch) {
        for (int i = 0; i < ch.entityLists.length; i++) {
            List<Entity> li = ch.entityLists[i];
            for (Entity e : li) {
                if (e instanceof DestroyOnUnload) {
                    ((DestroyOnUnload) e).destroy();
                }
            }
        }
    }

    public static boolean
    chunkContainsBiome(World world, int cx, int cz, BiomeGenBase b) {
        int x = cx << 4;
        int z = cz << 4;
        return chunkContainsBiomeBlockCoords(world, x, z, b);
    }

    public static boolean
    chunkContainsBiomeBlockCoords(World world, int x, int z, BiomeGenBase b) {
        return world.getBiomeGenForCoords(x, z) == b
            || world.getBiomeGenForCoords(x + 15, z) == b
            || world.getBiomeGenForCoords(x, z + 15) == b
            || world.getBiomeGenForCoords(x + 15, z + 15) == b;
    }

    public static boolean
    chunkContainsBiomeType(World world, int cx, int cz, Class<? extends BiomeGenBase> c) {
        int x = cx << 4;
        int z = cz << 4;
        return chunkContainsBiomeTypeBlockCoords(world, x, z, c);
    }

    public static boolean chunkContainsBiomeTypeBlockCoords(
        World world, int x, int z, Class<? extends BiomeGenBase> c
    ) {
        return c.isInstance(world.getBiomeGenForCoords(x, z))
            || c.isInstance(world.getBiomeGenForCoords(x + 15, z))
            || c.isInstance(world.getBiomeGenForCoords(x, z + 15))
            || c.isInstance(world.getBiomeGenForCoords(x + 15, z + 15));
    }

    private static ExtendedBlockStorage getStorageInChunk(Chunk c, int y) {
        return c.getBlockStorageArray()[y >> 4];
    }

    private static ExtendedBlockStorage getOrCreateStorageInChunk(Chunk c, int y) {
        ExtendedBlockStorage exb = getStorageInChunk(c, y);
        if (exb == null) {
            exb = new ExtendedBlockStorage(y >> 4 << 4, !c.worldObj.provider.hasNoSky);
            c.getBlockStorageArray()[y >> 4] = exb;
        }
        return exb;
    }

    public static Block[] getChunkAsColumnData(Chunk c) {
        Block[] data = new Block[65536];
        for (int i = 0; i < 16; i++) {
            for (int k = 0; k < 16; k++) {
                for (int j = 0; j < 256; j++) {
                    int idx = j + 256 * (i * 16 + k);
                    Block b = null;
                    ExtendedBlockStorage exb = getStorageInChunk(c, j);
                    if (exb != null)
                        b = exb.getBlockByExtId(i, j & 15, k);
                    if (b == Blocks.air)
                        b = null;
                    data[idx] = b;
                }
            }
        }
        return data;
    }

    public static void writeBlockColumnToChunk(Chunk c, Block[] data) {
        for (int i = 0; i < 16; i++) {
            for (int k = 0; k < 16; k++) {
                for (int j = 0; j < 256; j++) {
                    int idx = j + 256 * (i * 16 + k);
                    if (data[idx] == null)
                        data[idx] = Blocks.air;
                    ExtendedBlockStorage exb = getOrCreateStorageInChunk(c, j);
                    exb.func_150818_a(i, j & 15, k, data[idx]);
                }
            }
        }
    }

    public static Chunk getRandomLoadedChunk(World world) {
        ChunkCoordIntPair p = (ChunkCoordIntPair
        ) ReikaJavaLibrary.getRandomCollectionEntry(rand, world.activeChunkSet);
        return world.getChunkFromChunkCoords(p.chunkXPos, p.chunkZPos);
    }

    public static boolean isSpawn(Chunk c) {
        ChunkCoordinates spawn = c.worldObj.getSpawnPoint();
        return c.xPosition == (spawn.posX >> 4) && c.zPosition == (spawn.posZ >> 4);
    }

    public static Coordinate searchForBlock(World world, int x, int z, Block b) {
        Chunk c = world.getChunkFromChunkCoords(x, z);
        int top = c.getTopFilledSegment() + 15;
        for (int j = 0; j < 256 && j <= top; j++) {
            for (int i = 0; i < 16; i++) {
                for (int k = 0; k < 16; k++) {
                    int idx = j + 256 * (i * 16 + k);
                    ExtendedBlockStorage exb = getStorageInChunk(c, j);
                    Block at = exb != null ? exb.getBlockByExtId(i, j & 15, k) : null;
                    if (at == b)
                        return new Coordinate(
                            c.xPosition * 16 + i, j, c.zPosition * 16 + k
                        );
                }
            }
        }
        return null;
    }

    public static ArrayList<Coordinate> getChunkCoords(int x, int z) {
        ArrayList<Coordinate> ret = new ArrayList();
        for (int i = 0; i < 16; i++) {
            for (int k = 0; k < 16; k++) {
                ret.add(new Coordinate(x + i, 0, z + k));
            }
        }
        return ret;
    }
}
