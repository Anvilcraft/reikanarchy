/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.Instantiable.Data.Immutable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;

public final class WorldChunk {
    public final int dimensionID;
    public final ChunkCoordIntPair chunk;

    public WorldChunk(World world, Chunk ch) {
        this(world, ch.getChunkCoordIntPair());
    }

    public WorldChunk(World world, ChunkCoordIntPair ch) {
        this(world.provider.dimensionId, ch);
    }

    public WorldChunk(World world, int x, int z) {
        this(world.provider.dimensionId, new ChunkCoordIntPair(x, z));
    }

    public WorldChunk(int dim, int x, int z) {
        this(dim, new ChunkCoordIntPair(x, z));
    }

    public WorldChunk(int dim, ChunkCoordIntPair ch) {
        dimensionID = dim;
        chunk = ch;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof WorldChunk) {
            WorldChunk c = (WorldChunk) o;
            return c.dimensionID == dimensionID && c.chunk.equals(chunk);
        }
        return false;
    }

    public Chunk load() {
        return DimensionManager.getWorld(dimensionID)
            .getChunkFromChunkCoords(chunk.chunkXPos, chunk.chunkZPos);
    }

    @Override
    public int hashCode() {
        return chunk.hashCode() ^ dimensionID;
    }

    @Override
    public String toString() {
        return "Chunk " + chunk.toString() + " in DIM" + dimensionID;
    }

    public static WorldChunk fromSerialString(String sg) {
        String[] parts = sg.split(",");
        return new WorldChunk(
            Integer.parseInt(parts[0]),
            Integer.parseInt(parts[1]),
            Integer.parseInt(parts[2])
        );
    }

    public String toSerialString() {
        return dimensionID + "," + chunk.chunkXPos + "," + chunk.chunkZPos;
    }

    public NBTTagCompound writeToTag() {
        NBTTagCompound ret = new NBTTagCompound();
        ret.setInteger("dimension", dimensionID);
        ret.setInteger("xCoord", chunk.chunkXPos);
        ret.setInteger("zCoord", chunk.chunkZPos);
        return ret;
    }

    public static WorldChunk readFromTag(NBTTagCompound tag) {
        return new WorldChunk(
            tag.getInteger("dimension"),
            tag.getInteger("xCoord"),
            tag.getInteger("zCoord")
        );
    }
}
