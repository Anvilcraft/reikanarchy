package Reika.DragonAPI.Instantiable.Worldgen;

import java.util.LinkedList;
import java.util.Random;

import Reika.DragonAPI.Instantiable.IO.ModLogger;
import Reika.DragonAPI.Libraries.Java.ReikaJVMParser;
import Reika.DragonAPI.Libraries.Java.ReikaJavaLibrary;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;

public abstract class StackableBiomeDecorator extends BiomeDecorator {
    private static LinkedList<DecoState> stateStack = new LinkedList();

    public static int STATE_STACK_LIMIT;

    static {
        int limit
            = ReikaJVMParser.getArgumentInteger("-DragonAPI_DecoratorStackLimit", 250);
        STATE_STACK_LIMIT = limit > 0 ? limit : 250;
        ReikaJavaLibrary.pConsole("Biome state stack limit: " + STATE_STACK_LIMIT);
    }

    @Override
    public final void
    decorateChunk(World world, Random rand, BiomeGenBase biome, int x, int z) {
        if (!stateStack.isEmpty()) {
            //this.getLogger().logError("Already decorating in biome "+this.toString()+"!
            //Generation will attempt to continue, but worldgen errors may occur.");//
            //State stack: "+stateStack.size()+":"+stateStack);
        }

        if (stateStack.size()
            >= STATE_STACK_LIMIT) { //generally only happens on large biome worlds
            this.getLogger().logError(
                "STATE STACK IS TOO LARGE [" + stateStack.size()
                + "] TO SAFELY CONTINUE, ABORTING " + this + " DECORATION IN CHUNK " + x
                + ", " + z
            );
            return;
        }

        stateStack.addLast(new DecoState(world, rand, x, z));
        this.updateFieldsFromStack();
        this.genDecorations(biome);
        stateStack.removeLast();
        this.updateFieldsFromStack();
    }

    protected abstract ModLogger getLogger();

    private void updateFieldsFromStack() {
        DecoState s = stateStack.isEmpty() ? null : stateStack.getLast();
        currentWorld = s != null ? s.world : null;
        randomGenerator = s != null ? s.rand : null;
        chunk_X = s != null ? s.chunkX : 0;
        chunk_Z = s != null ? s.chunkZ : 0;
    }

    private static class DecoState {
        private final World world;
        private final Random rand;
        private final int chunkX;
        private final int chunkZ;

        private DecoState(World w, Random r, int x, int z) {
            world = w;
            rand = r;
            chunkX = x;
            chunkZ = z;
        }

        @Override
        public String toString() {
            return chunkX + ", " + chunkZ + " in DIM" + world.provider.dimensionId;
        }
    }
}
