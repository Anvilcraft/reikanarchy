package Reika.DragonAPI.Auxiliary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import Reika.DragonAPI.Auxiliary.Trackers.WorldgenProfiler;
import Reika.DragonAPI.Instantiable.Data.Immutable.Coordinate;
import Reika.DragonAPI.Instantiable.Event.ChunkGenerationEvent;
import Reika.DragonAPI.Instantiable.Event.ChunkPopulationEvent;
import Reika.DragonAPI.Instantiable.Event.ChunkRequestEvent;
import Reika.DragonAPI.Instantiable.Event.ChunkRequestEvent.ChunkRequestWatcher;
import Reika.DragonAPI.Instantiable.Event.Client.SinglePlayerLogoutEvent;
import Reika.DragonAPI.Instantiable.Event.SetBlockEvent;
import Reika.DragonAPI.ModInteract.DeepInteract.PlanetDimensionHandler;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.common.MinecraftForge;

public class WorldGenInterceptionRegistry implements ChunkRequestWatcher {
    public static final WorldGenInterceptionRegistry instance
        = new WorldGenInterceptionRegistry();

    public static boolean skipLighting = false;

    private int runningChunkDecoration = 0;
    private final HashMap<Coordinate, BlockSetData> data = new HashMap();
    private final ArrayList<BlockSetWatcher> watchers = new ArrayList();
    private final ArrayList<IWGWatcher> IWGwatchers = new ArrayList();
    private final ArrayList<DecorationWatcher> decorationWatchers = new ArrayList();
    private final ArrayList<InterceptionException> exceptions = new ArrayList();
    private boolean dispatchingChanges = false;

    //breaks due to concurrency
    private final LinkedList<IWorldGenerator> currentlyRunningGenerators
        = new LinkedList();
    private final LinkedList<Integer> currentlyRunningChunkX = new LinkedList();
    private final LinkedList<Integer> currentlyRunningChunkZ = new LinkedList();

    private WorldGenInterceptionRegistry() {
        MinecraftForge.EVENT_BUS.register(this);
        ChunkRequestEvent.addListener(this);
    }

    public void addWatcher(BlockSetWatcher w) {
        watchers.add(w);
    }

    public void addIWGWatcher(IWGWatcher w) {
        IWGwatchers.add(w);
    }

    public void addDecorationWatcher(DecorationWatcher w) {
        decorationWatchers.add(w);
    }

    public void addException(InterceptionException e) {
        exceptions.add(e);
    }

    @Override
    public int watcherSortIndex() {
        return 0;
    }

    @Override
    public void onChunkRequested(WorldServer w, ChunkProviderServer p, int cx2, int cz2) {
        if (WorldgenProfiler.profilingEnabled()) {
            boolean gen = !ChunkRequestEvent.chunkIsLoaded()
                && !ChunkRequestEvent.chunkExistsOnDisk();
            if (gen) {
                WorldgenProfiler.startChunk(cx2, cz2);
            }

            if (!currentlyRunningGenerators.isEmpty()) {
                long time = System.nanoTime();
                IWorldGenerator spiller = currentlyRunningGenerators.getLast();
                int cx = currentlyRunningChunkX.getLast();
                int cz = currentlyRunningChunkZ.getLast();
                if (cx != cx2 || cz != cz2) {
                    WorldgenProfiler.onChunkSpills(spiller, cx, cz, cx2, cz2, time, gen);
                }
                //long dur = System.nanoTime()-time;
                //WorldgenProfiler.subtractTime(spiller, dur);
            }
        }
    }

    @SubscribeEvent
    public void prePopulation(ChunkGenerationEvent evt) {
        if (!watchers.isEmpty())
            runningChunkDecoration++;

        //DragonAPICore.log("Running base gen for
        //"+evt.getChunk().getChunkCoordIntPair());
    }

    @SubscribeEvent
    public void onSetBlock(SetBlockEvent.Pre evt) {
        if (watchers.isEmpty())
            return;
        if (runningChunkDecoration <= 0)
            return;
        if (dispatchingChanges) {
            return;
        }
        if (PlanetDimensionHandler.isGalacticWorld(evt.world
            )) //it is his world anyways, and this just breaks there
            return;
        for (InterceptionException e : exceptions)
            if (e.doesExceptionApply(
                    evt.world,
                    evt.xCoord,
                    evt.yCoord,
                    evt.zCoord,
                    evt.newBlock,
                    evt.newMeta
                ))
                return;
        Coordinate c = new Coordinate(evt.xCoord, evt.yCoord, evt.zCoord);
        data.put(
            c,
            new BlockSetData(
                c, evt.currentBlock, evt.currentMeta, evt.newBlock, evt.newMeta
            )
        );
    }

    public void postPopulation(World world, int cx, int cz) {
        //ReikaJavaLibrary.pConsole("Watching chunk "+cx+","+cz+" block change set with
        //"+set.size()+" blocks");
        dispatchingChanges = true;
        if (!data.isEmpty()) {
            Map<Coordinate, BlockSetData> map
                = Collections.unmodifiableMap(new HashMap(data));
            for (BlockSetWatcher w : watchers) {
                w.onChunkGeneration(world, map);
            }
            //if (WorldgenProfiler.profilingEnabled() &&
            //!currentlyRunningGenerators.isEmpty()) {
            //	WorldgenProfiler.registerBlockChanges(currentlyRunningGenerators.getLast(),
            //map.size());
            //}
        }
        dispatchingChanges = false;
        data.clear();
        runningChunkDecoration--;
        if (runningChunkDecoration < 0)
            runningChunkDecoration = 0;
        //DragonAPICore.log("Finished decoration for "+cx+", "+cz);

        if (WorldgenProfiler.profilingEnabled()) {
            WorldgenProfiler.finishChunk(System.nanoTime(), cx, cz);
        }
    }

    @SubscribeEvent
    public void onSSPLogout(SinglePlayerLogoutEvent evt) {
        data.clear();
        runningChunkDecoration = 0;
        dispatchingChanges = false;
    }

    public static void interceptChunkPopulation(
        int cx, int cz, World world, IChunkProvider generator, IChunkProvider loader
    ) {
        //DragonAPICore.log("Running decoration for "+cx+", "+cz);

        /*
        if (!instance.currentlyRunningGenerators.isEmpty()) {
            IWorldGenerator spiller = instance.currentlyRunningGenerators.getLast();
            int cx0 = instance.currentlyRunningChunkX.getLast();
            int cz0 = instance.currentlyRunningChunkZ.getLast();
            //DragonAPICore.log("Generator "+spiller.getClass()+" has spilled from
        ["+cx0+", "+cz0+"] into adjacent chunk ["+cx+", "+cz+"]!"); if
        (WorldgenProfiler.profilingEnabled()) { WorldgenProfiler.onChunkSpills(spiller,
        cx0, cz0, cx, cz);
            }
        }*/

        ChunkPopulationEvent evt
            = new ChunkPopulationEvent(world, cx, cz, generator, loader);
        if (MinecraftForge.EVENT_BUS.post(evt)) {
        } else {
            GameRegistry.generateWorld(cx, cz, world, generator, loader);
        }
        instance.postPopulation(world, cx, cz);
    }

    public static void interceptIWG(
        IWorldGenerator gen,
        Random random,
        int cx,
        int cz,
        World world,
        IChunkProvider generator,
        IChunkProvider loader
    ) {
        for (IWGWatcher w : instance.IWGwatchers) {
            if (!w.canIWGRun(gen, random, cx, cz, world, generator, loader))
                return;
        }

        //if (!instance.currentlyRunningGenerators.isEmpty()) {
        //	ReikaJavaLibrary.pConsole("Running generator "+gen.getClass()+" from 'inside'
        //"+instance.currentlyRunningGenerators+"!");
        //}

        if (WorldgenProfiler.profilingEnabled()) {
            instance.currentlyRunningChunkX.add(cx);
            instance.currentlyRunningChunkZ.add(cz);
            instance.currentlyRunningGenerators.add(gen);
        }

        int id = 0;
        if (WorldgenProfiler.profilingEnabled()) {
            id = world.provider.dimensionId;
            WorldgenProfiler.startGenerator(id, gen, cx, cz);
        }

        gen.generate(random, cx, cz, world, generator, loader);

        if (WorldgenProfiler.profilingEnabled()) {
            WorldgenProfiler.onRunGenerator(id, gen, cx, cz);

            instance.currentlyRunningGenerators.removeLast();
            instance.currentlyRunningChunkX.removeLast();
            instance.currentlyRunningChunkZ.removeLast();
        }
    }

    public static void
    runBiomeDecorator(BiomeGenBase b, World world, Random rand, int x, int z) {
        for (DecorationWatcher w : instance.decorationWatchers) {
            if (!w.canDecorate(b, world, rand, x, z))
                return;
        }
        b.decorate(world, rand, x, z);
    }

    public static interface BlockSetWatcher {
        public void onChunkGeneration(World world, Map<Coordinate, BlockSetData> set);
    }

    public static interface InterceptionException {
        public boolean
        doesExceptionApply(World world, int x, int y, int z, Block set, int meta);
    }

    public static interface IWGWatcher {
        public boolean canIWGRun(
            IWorldGenerator gen,
            Random random,
            int cx,
            int cz,
            World world,
            IChunkProvider generator,
            IChunkProvider loader
        );
    }

    public static interface DecorationWatcher {
        public boolean
        canDecorate(BiomeGenBase biome, World world, Random random, int cx, int cz);
    }

    public final class BlockSetData {
        public final Coordinate location;
        public final Block oldBlock;
        public final int oldMetadata;
        public final Block newBlock;
        public final int newMetadata;

        private BlockSetData(Coordinate c, Block old, int oldmeta, Block b, int meta) {
            location = c;
            oldBlock = old;
            oldMetadata = oldmeta;
            newBlock = b;
            newMetadata = meta;
        }

        public TileEntity getTileEntity(World world) {
            return location.getTileEntity(world);
        }

        public void revert(World world) {
            location.setBlock(world, oldBlock, oldMetadata, 2);
        }
    }
}
