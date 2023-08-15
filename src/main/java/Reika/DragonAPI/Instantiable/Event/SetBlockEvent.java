/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.Instantiable.Event;

import Reika.DragonAPI.Instantiable.Event.Base.WorldPositionEvent;
import Reika.DragonAPI.Libraries.World.ReikaWorldHelper;
import net.minecraft.block.Block;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;

/**
 * Fired both before, and when a setBlock propagates and succeeds inside a chunk. This is
 * fired both client and server side. This is called hundreds of thousands of times all
 * over the codebase, dozens of times a tick, so you need to be efficient.
 */
public abstract class SetBlockEvent extends WorldPositionEvent {
    /**
     * You can toggle this off briefly to bypass the events if you are doing a lot of
     * block sets (eg large scale worldgen) and you do not care about other interceptions
     * failing. ENSURE IT IS BACK ON AFTERWARDS.
     */
    public static boolean eventEnabledPre = true;

    /**
     * You can toggle this off briefly to bypass the events if you are doing a lot of
     * block sets (eg large scale worldgen) and you do not care about other interceptions
     * failing. ENSURE IT IS BACK ON AFTERWARDS.
     */
    public static boolean eventEnabledPost = true;

    /**
     * Is this being called as part of a chunk generation. Usually indicates irrelevance
     * to stuff like trackers. Also usually indicates being called thousands of times in
     * rapid succession. As a result, you MUST be efficient in handling these ones.
     */
    public final boolean isWorldgen;
    private final Chunk chunk;

    public final ChunkCoordIntPair chunkLocation;

    public SetBlockEvent(Chunk ch, int x, int y, int z) {
        super(ch.worldObj, ch.xPosition * 16 + x, y, ch.zPosition * 16 + z);
        chunk = ch;
        chunkLocation = new ChunkCoordIntPair(ch.xPosition, ch.zPosition);
        isWorldgen = !ReikaWorldHelper.isChunkPastCompletelyFinishedGenerating(
            world, ch.xPosition, ch.zPosition
        );
    }

    public static class Pre extends SetBlockEvent {
        public final Block currentBlock;
        public final Block newBlock;
        public final int currentMeta;
        public final int newMeta;

        public Pre(Chunk ch, int x, int y, int z, Block b, int meta) {
            super(ch, x, y, z);

            currentBlock = ch.getBlock(x, y, z); //USE CHUNK, NOT WORLD
            currentMeta = ch.getBlockMetadata(x, y, z);

            newBlock = b != null ? b : currentBlock; //is a call to setMeta
            newMeta = meta;
        }

        public static void fire(Chunk ch, int x, int y, int z, Block b, int meta) {
            if (eventEnabledPre)
                MinecraftForge.EVENT_BUS.post(new Pre(ch, x, y, z, b, meta));
        }

        public static void fire_meta(Chunk ch, int x, int y, int z, int meta) {
            if (eventEnabledPre)
                MinecraftForge.EVENT_BUS.post(new Pre(ch, x, y, z, null, meta));
        }
    }

    public static class Post extends SetBlockEvent {
        public Post(Chunk ch, int x, int y, int z) {
            super(ch, x, y, z);
        }

        public static void fire(Chunk ch, int x, int y, int z) {
            if (eventEnabledPost)
                MinecraftForge.EVENT_BUS.post(new Post(ch, x, y, z));
        }
    }
}
