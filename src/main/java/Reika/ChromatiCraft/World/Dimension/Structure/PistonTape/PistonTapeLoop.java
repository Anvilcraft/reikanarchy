package Reika.ChromatiCraft.World.Dimension.Structure.PistonTape;

import java.util.HashMap;
import java.util.Random;

import Reika.ChromatiCraft.Base.StructurePiece;
import Reika.ChromatiCraft.World.Dimension.Structure.PistonTape.PistonTapeSlice.LoopSliceDimensions;
import Reika.ChromatiCraft.World.Dimension.Structure.PistonTapeGenerator;
import Reika.DragonAPI.Instantiable.Data.Immutable.Coordinate;
import Reika.DragonAPI.Instantiable.RGBColorData;
import Reika.DragonAPI.Instantiable.Worldgen.ChunkSplicedGenerationCache;
import Reika.DragonAPI.Libraries.ReikaDirectionHelper;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class PistonTapeLoop extends StructurePiece<PistonTapeGenerator> {
    public final ForgeDirection facing;
    public final int busWidth;

    public final LoopDimensions dimensions;
    final TapeStage level;
    private final PistonTapeSlice[] bits;

    protected PistonTapeLoop(PistonTapeGenerator s, ForgeDirection dir, TapeStage t) {
        super(s);
        level = t;
        facing = dir;

        busWidth = t.bitsPerDoor;
        bits = new PistonTapeSlice[busWidth];
        dimensions = LoopDimensions.createFor(level.doorCount);
        for (int i = 0; i < bits.length; i++) {
            bits[i] = new PistonTapeSlice(s, facing, i, this, dimensions);
        }
    }

    void randomize(Random rand) {
        for (int i = 0; i < bits.length; i++) {
            bits[i].randomizeSolution(rand);
        }
    }

    RGBColorData getColor(int subBit, int pos) {
        return bits[subBit].getColor(pos);
    }

    @Override
    public void generate(ChunkSplicedGenerationCache world, int x, int y, int z) {
        ForgeDirection dir = ReikaDirectionHelper.getLeftBy90(facing);
        for (int i = 0; i < bits.length; i++) {
            PistonTapeSlice p = bits[i];
            p.generate(world, x + dir.offsetX * i, y, z + dir.offsetZ * i);
        }
    }

    public int doorCount() {
        return level.doorCount;
    }

    public boolean cycle(World world) {
        boolean flag = true;
        for (PistonTapeSlice s : bits) {
            flag &= s.cycle(world);
        }
        return flag;
    }

    public Coordinate getEmitter(int idx) {
        return bits[idx].emitter;
    }

    public Coordinate getTarget(int idx) {
        return bits[idx].target;
    }

    static class LoopDimensions {
        private static final int MIN_HEIGHT = 4; // was 3
        private static final int MAX_HEIGHT = 6; // was 5

        public final int totalHeight;
        public final int totalDepth;

        public final int bitLength;

        private final HashMap<Integer, LoopSliceDimensions> slices = new HashMap();

        protected LoopDimensions(int d, int h) {
            totalHeight = h;
            totalDepth = d;

            bitLength = (totalHeight - 1) * 2 + (totalDepth - 1) * 2 + 2;
        }

        @Override
        public final String toString() {
            return totalDepth + "x" + totalHeight;
        }

        private static LoopDimensions createFor(int stages) {
            int l = stages * 2;
            int bestH = -1;
            int bestD = -1;
            int best = l + 5;
            for (int h = MIN_HEIGHT; h <= MAX_HEIGHT; h++) {
                int d = MathHelper.ceiling_double_int(l / (float) h);
                int excess = d * h - l;
                if (excess < best) {
                    bestH = h;
                    bestD = d;
                    best = excess;
                }
            }
            return new LoopDimensions(bestD, bestH);
        }

        public final LoopSliceDimensions slice(int idx) {
            LoopSliceDimensions ret
                = new LoopSliceDimensions(idx, totalDepth, totalHeight);
            slices.put(idx, ret);
            return ret;
        }
    }
}
