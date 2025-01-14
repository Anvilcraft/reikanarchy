package Reika.ChromatiCraft.World.Dimension.Structure;

import java.util.Random;

import Reika.ChromatiCraft.Base.DimensionStructureGenerator;
import Reika.ChromatiCraft.Base.StructureData;
import Reika.ChromatiCraft.Registry.ChromaOptions;
import Reika.ChromatiCraft.World.Dimension.Structure.PistonTape.PistonTapeData;
import Reika.ChromatiCraft.World.Dimension.Structure.PistonTape.PistonTapeEntrance;
import Reika.ChromatiCraft.World.Dimension.Structure.PistonTape.PistonTapeLootRoom;
import Reika.ChromatiCraft.World.Dimension.Structure.PistonTape.PistonTapeStaircase;
import Reika.ChromatiCraft.World.Dimension.Structure.PistonTape.TapeStage;
import Reika.DragonAPI.Instantiable.Data.Immutable.Coordinate;
import Reika.DragonAPI.Libraries.ReikaDirectionHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class PistonTapeGenerator extends DimensionStructureGenerator {
    public static final boolean DEBUG = false;

    private TapeStage[] stages;

    private boolean isActive = false;

    public static final ForgeDirection DIRECTION = ForgeDirection.EAST;

    @Override
    protected void calculate(int chunkX, int chunkZ, Random rand) {
        int x = chunkX;
        int z = chunkZ;
        int y = 60 + rand.nextInt(30);
        posY = y; /*
         new TapeAssemblyArea(this).generate(world, x, y, z);
         x += length+2;
         this.generateDataTile(x, y+1, z);
         x += 2;
         for (int i = 0; i < length; i++) {
             int id = MIN_ID+rand.nextInt(MAX_ID-MIN_ID+1);
             while (generatedIDs.contains(id)) {
                 id = MIN_ID+rand.nextInt(MAX_ID-MIN_ID+1);
             }
             generatedIDs.add(id);
             HashSet<Coordinate> set = new HashSet();
             TapeStage ts = new TapeStage(new DoorKey(id, set), x, y, z);
             doors.add(ts);
             x++;
         }
         PistonTapeLoop pl = new PistonTapeLoop(this);
         pl.generate(world, x, y, z+12);
          */
        //this.generateDataTile(x-DIRECTION.offsetX*5, y, z-DIRECTION.offsetZ*5);
        PistonTapeParameters[] arr = this.getTapes();
        stages = new TapeStage[arr.length];
        ForgeDirection dir = DIRECTION;
        for (int i = 0; i < arr.length; i++) {
            PistonTapeParameters p = arr[i];
            TapeStage s = new TapeStage(this, i, p.busWidth, p.doorCount, dir, rand);
            ForgeDirection dir2 = ReikaDirectionHelper.getLeftBy90(dir);
            int dx = x + dir2.offsetX * 12;
            int dz = z + dir2.offsetZ * 12;
            s.generate(world, dx, y, dz);
            stages[i] = s;
            x += dir.offsetX * s.getDirectionLength();
            z += dir.offsetZ * s.getDirectionLength();
            y -= Math.max(6, s.height);
            dir = dir.getOpposite();
        }
        for (int i = 0; i < stages.length - 1; i++) {
            TapeStage s1 = stages[i];
            TapeStage s2 = stages[i + 1];
            PistonTapeStaircase p = new PistonTapeStaircase(this, s1, s2, rand);
            p.generate(world, x, y, z);
        }
        TapeStage last = stages[stages.length - 1];
        Coordinate end = last.getExit();
        new PistonTapeLootRoom(this, dir.getOpposite(), rand)
            .generate(world, end.xCoord, end.yCoord, end.zCoord);
        Coordinate start = stages[0].getEntrance().offset(DIRECTION.getOpposite(), 2);
        this.addDynamicStructure(
            new PistonTapeEntrance(this, start, DIRECTION), start.xCoord, start.zCoord
        );
    }

    @Override
    public StructureData createDataStorage() {
        return new PistonTapeData(this);
    }

    @Override
    protected int getCenterXOffset() {
        return 0;
    }

    @Override
    protected int getCenterZOffset() {
        return 0;
    }

    @Override
    protected boolean hasBeenSolved(World world) {
        for (TapeStage s : stages) {
            if (!s.isSolved())
                return false;
        }
        return true;
    }

    @Override
    protected void openStructure(World world) {
        for (TapeStage s : stages) {
            s.openAllDoors(world);
        }
    }

    @Override
    protected void clearCaches() {
        stages = null;
    }

    private PistonTapeParameters[] getTapes() {
        switch (ChromaOptions.getStructureDifficulty()) {
            case 1:
                return new PistonTapeParameters[] { new PistonTapeParameters(2, 5),
                                                    new PistonTapeParameters(2, 8),
                                                    new PistonTapeParameters(3, 6) };
            case 2:
                return new PistonTapeParameters[] { new PistonTapeParameters(2, 6),
                                                    new PistonTapeParameters(3, 8),
                                                    new PistonTapeParameters(3, 10) };
            case 3:
            default:
                return new PistonTapeParameters[] { new PistonTapeParameters(2, 8),
                                                    new PistonTapeParameters(3, 9),
                                                    new PistonTapeParameters(3, 12),
                                                    new PistonTapeParameters(4, 12) };
        }
    }

    private static class PistonTapeParameters {
        private final int busWidth;
        private final int doorCount;

        private PistonTapeParameters(int w, int l) {
            busWidth = w;
            doorCount = l;
        }
    }

    public TapeStage getStage(int i) {
        return stages[i];
    }

    public int stageCount() {
        return stages.length;
    }

    /*
    have only one set of emitters and targets, and move the bit blocks

    MAKE THE PLAYER BUILD THE TAPE **NOT** IN FRONT OF THE READ HEAD - MAKE IT MOVE THERE
    LATER DURING PLAYBACK

    ALSO, HAVE THE PLAYER BUILD IT INLINE, BUT THE MECHANISM SPLITS IT INTO THREE LINES OF
    THREE*//*

	static abstract class PulseTileCallback implements TileCallback {

		protected final Coordinate columnBase;

		protected PulseTileCallback(int x, int y, int z) {
			columnBase = new Coordinate(x, y, z);
		}

	}

	static class EmitterCallback extends PulseTileCallback {

		protected EmitterCallback(int x, int y, int z) {
			super(x, y, z);
		}

		@Override
		public void onTilePlaced(World world, int x, int y, int z, TileEntity te) {
			if (te instanceof EmitterTile) {
				((EmitterTile)te).renderAsFullBlock = true;
				((EmitterTile)te).silent = true;
				((EmitterTile)te).speedFactor = 1.25;
				((EmitterTile)te).setDirection(CubeDirections.SOUTH);
			}
		}
	}

	static class TargetCallback extends PulseTileCallback {

		private final DoorKey door;

		protected TargetCallback(DoorKey d, int x, int y, int z) {
			super(x, y, z);
			door = d;
		}

		@Override
		public void onTilePlaced(World world, int x, int y, int z, TileEntity te) {
			if (te instanceof TargetTile) {
				((TargetTile)te).renderAsFullBlock = true;
				((TargetTile)te).autoReset = PistonTapeData.STEP_DURATION;
				((TargetTile)te).setDirection(CubeDirections.SOUTH);

				int dy = te.yCoord-columnBase.yCoord;
				ColorData c = new ColorData(true, true, true);
				switch(dy) {
					case 0:
						c = door.getColor1();
						break;
					case 1:
						c = door.getColor2();
						break;
					case 2:
						c = door.getColor3();
						break;
				}
				((TargetTile)te).setColor(c);
			}
		}
	}*/
}
