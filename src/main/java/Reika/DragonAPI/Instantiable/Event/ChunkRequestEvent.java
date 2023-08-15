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

import java.util.Arrays;

import Reika.DragonAPI.Interfaces.Callbacks.EventWatchers;
import Reika.DragonAPI.Interfaces.Callbacks.EventWatchers.EventWatcher;
import Reika.DragonAPI.Libraries.Java.ReikaArrayHelper;
import Reika.DragonAPI.Libraries.World.ReikaWorldHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.ChunkProviderServer;

/**
 * This is fired BEFORE the chunk is actually provided (and generated/loaded from disk if
 * necessary! DO NOT attempt to access the chunk's data!
 */
public class ChunkRequestEvent {
    private static ChunkRequestWatcher[] listeners = null;

    private static WorldServer world;
    private static ChunkProviderServer provider;
    private static int chunkX;
    private static int chunkZ;

    public static void addListener(ChunkRequestWatcher l) {
        listeners
            = ReikaArrayHelper.addToFastArray(listeners, l, ChunkRequestWatcher.class);
        Arrays.sort(listeners, EventWatchers.comparator);
    }

    public static boolean chunkIsLoaded() {
        return provider.loadedChunkHashMap.getValueByKey(
                   ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ)
               )
            != null;
    }

    /** i.e. does NOT need to be generated/decorated. */
    public static boolean chunkExistsOnDisk() {
        return ReikaWorldHelper.isChunkGeneratedChunkCoords(world, chunkX, chunkZ);
    }

    public static void fire(WorldServer w, ChunkProviderServer p, int x, int z) {
        if (listeners == null)
            return;
        world = w;
        provider = p;
        chunkX = x;
        chunkZ = z;
        for (ChunkRequestWatcher l : listeners) {
            l.onChunkRequested(w, p, x, z);
        }
        world = null;
        provider = null;
        chunkX = chunkZ = 0;
    }

    public static interface ChunkRequestWatcher extends EventWatcher {
        void onChunkRequested(WorldServer w, ChunkProviderServer p, int x, int z);
    }
}
