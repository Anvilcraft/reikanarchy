package Reika.Satisforestry.Miner;

import Reika.DragonAPI.Instantiable.Data.BlockStruct.FilledBlockArray;
import Reika.DragonAPI.Instantiable.Data.BlockStruct.FilledBlockArray.BlockMatchFailCallback;
import Reika.DragonAPI.Libraries.ReikaDirectionHelper;
import Reika.Satisforestry.Blocks.BlockMinerMulti.MinerBlocks;
import Reika.Satisforestry.Registry.SFBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class MinerStructure {
    public static ForgeDirection
    getStructureDirection(World world, int x, int y, int z, BlockMatchFailCallback call) {
        for (int i = 2; i < 6; i++) {
            ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];
            FilledBlockArray arr = getMinerStructure(world, x, y, z, dir);
            if (arr.matchInWorld()) {
                return dir;
            }
        }
        if (call != null) {
            checkForErrors(world, x, y, z, call);
        }
        return null;
    }

    private static void
    checkForErrors(World world, int x, int y, int z, BlockMatchFailCallback call) {
        ForgeDirection fewestSide = null;
        int fewest = Integer.MAX_VALUE;
        for (int i = 2; i < 6; i++) {
            ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];
            FilledBlockArray arr = getMinerStructure(world, x, y, z, dir);
            int err = arr.countErrors();
            if (err < fewest || fewestSide == null) {
                fewestSide = dir;
                fewest = err;
            }
        }
        FilledBlockArray arr = getMinerStructure(world, x, y, z, fewestSide);
        arr.matchInWorld(call);
    }

    private static void setBlock(
        FilledBlockArray array,
        ForgeDirection dir,
        int x0,
        int y0,
        int z0,
        int dx,
        int dy,
        int dz,
        Block b
    ) {
        setBlock(array, dir, x0, y0, z0, dx, dy, dz, b, -1);
    }

    @SuppressWarnings("incomplete-switch")
    private static void setBlock(
        FilledBlockArray array,
        ForgeDirection dir,
        int x0,
        int y0,
        int z0,
        int dx,
        int dy,
        int dz,
        Block b,
        int meta
    ) {
        if (b == Blocks.air) {
            if (dx - x0 > 3 && dy - y0 > 5)
                return;
        }
        dx -= x0;
        dz -= z0;
        switch (dir) {
            case WEST:
                dx = -dx;
                dz = -dz;
                break;
            case NORTH: {
                int temp = -dx;
                dx = dz;
                dz = temp;
                break;
            }
            case SOUTH: {
                int temp = dx;
                dx = -dz;
                dz = temp;
                break;
            }
        }
        dx += x0;
        dz += z0;
        if (b == Blocks.air)
            array.setEmpty(dx, dy, dz, false, false);
        else
            array.setBlock(dx, dy, dz, b, meta);
    }

    public static void
    toggleRSLamps(TileNodeHarvester te, ForgeDirection dir, boolean set) {
        ForgeDirection left = ReikaDirectionHelper.getLeftBy90(dir);
        Block b = set ? Blocks.redstone_lamp : Blocks.air;
        te.worldObj.setBlock(
            te.xCoord + 3 * dir.offsetX + 2 * left.offsetX,
            te.yCoord + 8,
            te.zCoord + 2 * left.offsetZ + 3 * dir.offsetZ,
            b
        );
        te.worldObj.setBlock(
            te.xCoord + 3 * dir.offsetX + 2 * left.offsetX,
            te.yCoord + 9,
            te.zCoord + 2 * left.offsetZ + 3 * dir.offsetZ,
            b
        );
        te.worldObj.setBlock(
            te.xCoord + 3 * dir.offsetX + 2 * left.offsetX,
            te.yCoord + 10,
            te.zCoord + 2 * left.offsetZ + 3 * dir.offsetZ,
            b
        );
    }

    //default dir is EAST
    public static FilledBlockArray
    getMinerStructure(World world, int x, int y, int z, ForgeDirection dir) {
        FilledBlockArray array = new FilledBlockArray(world);
        Block b = SFBlocks.MINERMULTI.getBlockInstance();
        int i = x - 3;
        int j = y;
        int k = z - 2;

        //world.setBlock(i + 3, j + 0, k + 2, SFBlocks.RESOURCENODE.getBlockInstance());

        setBlock(array, dir, x, y, z, i + 6, j + 8, k + 0, Blocks.redstone_lamp);
        setBlock(array, dir, x, y, z, i + 6, j + 9, k + 0, Blocks.redstone_lamp);
        setBlock(array, dir, x, y, z, i + 6, j + 10, k + 0, Blocks.redstone_lamp);

        setBlock(array, dir, x, y, z, i + 0, j + 0, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 0, j + 0, k + 1, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 0, j + 0, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 0, j + 0, k + 3, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 0, j + 0, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 0, j + 1, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 0, j + 1, k + 1, b, MinerBlocks.GRAY.ordinal());
        setBlock(array, dir, x, y, z, i + 0, j + 1, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 0, j + 1, k + 3, b, MinerBlocks.GRAY.ordinal());
        setBlock(array, dir, x, y, z, i + 0, j + 1, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 0, j + 2, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 0, j + 2, k + 1, b, MinerBlocks.GRAY.ordinal());
        setBlock(array, dir, x, y, z, i + 0, j + 2, k + 2, b, MinerBlocks.GRAY.ordinal());
        setBlock(array, dir, x, y, z, i + 0, j + 2, k + 3, b, MinerBlocks.GRAY.ordinal());
        setBlock(array, dir, x, y, z, i + 0, j + 2, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 0, j + 3, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 0, j + 3, k + 1, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 0, j + 3, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 0, j + 3, k + 3, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 0, j + 3, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 0, j + 4, k + 0, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 0, j + 4, k + 1, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 0, j + 4, k + 2, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 0, j + 4, k + 3, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 0, j + 4, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 0, j + 5, k + 0, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 0, j + 5, k + 1, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 0, j + 5, k + 2, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 0, j + 5, k + 3, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 0, j + 5, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 0, j + 6, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 0, j + 6, k + 1, b, MinerBlocks.GRAY.ordinal());
        setBlock(array, dir, x, y, z, i + 0, j + 6, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 0, j + 6, k + 3, b, MinerBlocks.GRAY.ordinal());
        setBlock(array, dir, x, y, z, i + 0, j + 6, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 0, j + 7, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 0, j + 7, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 0, j + 7, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 0, j + 7, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 0, j + 7, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 0, j + 8, k + 0, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 0, j + 8, k + 1, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 0, j + 8, k + 2, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 0, j + 8, k + 3, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 0, j + 8, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 0, j + 9, k + 0, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 0, j + 9, k + 1, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 0, j + 9, k + 2, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 0, j + 9, k + 3, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 0, j + 9, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 0, j + 10, k + 0, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 0, j + 10, k + 1, b, MinerBlocks.DARK.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 0, j + 10, k + 2, b, MinerBlocks.DARK.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 0, j + 10, k + 3, b, MinerBlocks.DARK.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 0, j + 10, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 0, j + 11, k + 0, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 0, j + 11, k + 1, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 0, j + 11, k + 2, b, MinerBlocks.DARK.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 0, j + 11, k + 3, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 0, j + 11, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 0, j + 12, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 0, j + 12, k + 1, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 0, j + 12, k + 2, b, MinerBlocks.DARK.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 0, j + 12, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 0, j + 12, k + 4, Blocks.air); /*
         setBlock(array, dir, x, y, z, i + 0, j + 13, k + 0, Blocks.air);
         setBlock(array, dir, x, y, z, i + 0, j + 13, k + 1, Blocks.air);
         setBlock(array, dir, x, y, z, i + 0, j + 13, k + 2, Blocks.air);
         setBlock(array, dir, x, y, z, i + 0, j + 13, k + 3, Blocks.air);
         setBlock(array, dir, x, y, z, i + 0, j + 13, k + 4, Blocks.air);*/
        setBlock(array, dir, x, y, z, i + 1, j + 0, k + 0, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 1, j + 0, k + 1, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 1, j + 0, k + 2, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 1, j + 0, k + 3, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 1, j + 0, k + 4, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 1, j + 1, k + 0, b, MinerBlocks.DARK.ordinal());
        setBlock(
            array, dir, x, y, z, i + 1, j + 1, k + 1, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 1, j + 1, k + 2, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 1, j + 1, k + 3, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 1, j + 1, k + 4, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 1, j + 2, k + 0, b, MinerBlocks.DARK.ordinal());
        setBlock(
            array, dir, x, y, z, i + 1, j + 2, k + 1, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 1, j + 2, k + 2, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 1, j + 2, k + 3, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 1, j + 2, k + 4, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 1, j + 3, k + 0, b, MinerBlocks.DARK.ordinal());
        setBlock(
            array, dir, x, y, z, i + 1, j + 3, k + 1, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 1, j + 3, k + 2, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 1, j + 3, k + 3, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 1, j + 3, k + 4, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 1, j + 4, k + 0, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 1, j + 4, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 1, j + 4, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 1, j + 4, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 1, j + 4, k + 4, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 1, j + 5, k + 0, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 1, j + 5, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 1, j + 5, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 1, j + 5, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 1, j + 5, k + 4, b, MinerBlocks.DARK.ordinal());
        setBlock(
            array, dir, x, y, z, i + 1, j + 6, k + 0, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 1, j + 6, k + 1, b, MinerBlocks.GRAY.ordinal());
        setBlock(array, dir, x, y, z, i + 1, j + 6, k + 2, b, MinerBlocks.GRAY.ordinal());
        setBlock(array, dir, x, y, z, i + 1, j + 6, k + 3, b, MinerBlocks.GRAY.ordinal());
        setBlock(
            array, dir, x, y, z, i + 1, j + 6, k + 4, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 1, j + 7, k + 0, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 1, j + 7, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 1, j + 7, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 1, j + 7, k + 3, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 1, j + 7, k + 4, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 1, j + 8, k + 0, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 1, j + 8, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 1, j + 8, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 1, j + 8, k + 3, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 1, j + 8, k + 4, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 1, j + 9, k + 0, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 1, j + 9, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 1, j + 9, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 1, j + 9, k + 3, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 1, j + 9, k + 4, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 1, j + 10, k + 0, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 1, j + 10, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 1, j + 10, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 1, j + 10, k + 3, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 1, j + 10, k + 4, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 1, j + 11, k + 0, b, MinerBlocks.DARK.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 1, j + 11, k + 1, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 1, j + 11, k + 2, b, MinerBlocks.GRAY.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 1, j + 11, k + 3, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 1, j + 11, k + 4, b, MinerBlocks.DARK.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 1, j + 12, k + 0, b, MinerBlocks.DARK.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 1, j + 12, k + 1, b, MinerBlocks.DARK.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 1, j + 12, k + 2, b, MinerBlocks.DARK.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 1, j + 12, k + 3, b, MinerBlocks.DARK.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 1, j + 12, k + 4, b, MinerBlocks.DARK.ordinal()
        ); /*
setBlock(array, dir, x, y, z, i + 1, j + 13, k + 0, Blocks.air);
setBlock(array, dir, x, y, z, i + 1, j + 13, k + 1, Blocks.air);
setBlock(array, dir, x, y, z, i + 1, j + 13, k + 2, Blocks.air);
setBlock(array, dir, x, y, z, i + 1, j + 13, k + 3, Blocks.air);
setBlock(array, dir, x, y, z, i + 1, j + 13, k + 4, Blocks.air);*/
        setBlock(array, dir, x, y, z, i + 2, j + 0, k + 0, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 2, j + 0, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 0, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 0, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 0, k + 4, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 2, j + 1, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 1, k + 1, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 2, j + 1, k + 2, b, MinerBlocks.DRILL.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 2, j + 1, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 1, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 2, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 2, k + 1, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 2, j + 2, k + 2, b, MinerBlocks.DRILL.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 2, j + 2, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 2, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 3, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 3, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 3, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 3, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 3, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 4, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 4, k + 1, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 2, j + 4, k + 2, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 2, j + 4, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 4, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 5, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 5, k + 1, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 2, j + 5, k + 2, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 2, j + 5, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 5, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 6, k + 0, b, MinerBlocks.GRAY.ordinal());
        setBlock(array, dir, x, y, z, i + 2, j + 6, k + 1, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 2, j + 6, k + 2, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 2, j + 6, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 6, k + 4, b, MinerBlocks.GRAY.ordinal());
        setBlock(
            array, dir, x, y, z, i + 2, j + 7, k + 0, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 2, j + 7, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 7, k + 2, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 2, j + 7, k + 3, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 2, j + 7, k + 4, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 2, j + 8, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 8, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 8, k + 2, b, MinerBlocks.GRAY.ordinal());
        setBlock(array, dir, x, y, z, i + 2, j + 8, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 8, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 9, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 9, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 9, k + 2, b, MinerBlocks.GRAY.ordinal());
        setBlock(array, dir, x, y, z, i + 2, j + 9, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 9, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 10, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 10, k + 1, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 2, j + 10, k + 2, b, MinerBlocks.GRAY.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 2, j + 10, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 10, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 11, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 11, k + 1, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 2, j + 11, k + 2, b, MinerBlocks.GRAY.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 2, j + 11, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 2, j + 11, k + 4, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 2, j + 12, k + 0, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 2, j + 12, k + 1, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 2, j + 12, k + 2, b, MinerBlocks.HUB.ordinal());
        setBlock(
            array, dir, x, y, z, i + 2, j + 12, k + 3, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 2, j + 12, k + 4, b, MinerBlocks.ORANGE.ordinal()
        ); /*
setBlock(array, dir, x, y, z, i + 2, j + 13, k + 0, Blocks.air);
setBlock(array, dir, x, y, z, i + 2, j + 13, k + 1, Blocks.air);
setBlock(array, dir, x, y, z, i + 2, j + 13, k + 2, Blocks.air);
setBlock(array, dir, x, y, z, i + 2, j + 13, k + 3, Blocks.air);
setBlock(array, dir, x, y, z, i + 2, j + 13, k + 4, Blocks.air);*/
        setBlock(array, dir, x, y, z, i + 3, j + 0, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 3, j + 0, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 3, j + 0, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 3, j + 0, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 3, j + 1, k + 0, b, MinerBlocks.DARK.ordinal());
        setBlock(
            array, dir, x, y, z, i + 3, j + 1, k + 1, b, MinerBlocks.DRILL.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 3, j + 1, k + 2, b, MinerBlocks.DRILL.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 3, j + 1, k + 3, b, MinerBlocks.DRILL.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 3, j + 1, k + 4, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 3, j + 2, k + 0, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 3, j + 2, k + 1, b, MinerBlocks.DRILL.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 3, j + 2, k + 2, b, MinerBlocks.DRILL.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 3, j + 2, k + 3, b, MinerBlocks.DRILL.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 3, j + 2, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 3, j + 3, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 3, j + 3, k + 1, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 3, j + 3, k + 2, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 3, j + 3, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 3, j + 3, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 3, j + 4, k + 0, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 3, j + 4, k + 1, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 3, j + 4, k + 2, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 3, j + 4, k + 3, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 3, j + 4, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 3, j + 5, k + 0, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 3, j + 5, k + 1, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 3, j + 5, k + 2, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 3, j + 5, k + 3, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 3, j + 5, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 3, j + 6, k + 0, b, MinerBlocks.GRAY.ordinal());
        setBlock(
            array, dir, x, y, z, i + 3, j + 6, k + 1, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 3, j + 6, k + 2, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 3, j + 6, k + 3, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 3, j + 6, k + 4, b, MinerBlocks.GRAY.ordinal());
        setBlock(
            array, dir, x, y, z, i + 3, j + 7, k + 0, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 3, j + 7, k + 1, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 3, j + 7, k + 2, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 3, j + 7, k + 3, b, MinerBlocks.DARK.ordinal());
        setBlock(
            array, dir, x, y, z, i + 3, j + 7, k + 4, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 3, j + 8, k + 0, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 3, j + 8, k + 1, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 3, j + 8, k + 2, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 3, j + 8, k + 3, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 3, j + 8, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 3, j + 9, k + 0, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 3, j + 9, k + 1, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 3, j + 9, k + 2, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 3, j + 9, k + 3, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 3, j + 9, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 3, j + 10, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 3, j + 10, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 3, j + 10, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 3, j + 10, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 3, j + 10, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 3, j + 11, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 3, j + 11, k + 1, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 3, j + 11, k + 2, b, MinerBlocks.GRAY.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 3, j + 11, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 3, j + 11, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 3, j + 12, k + 0, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 3, j + 12, k + 1, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 3, j + 12, k + 2, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 3, j + 12, k + 3, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 3, j + 12, k + 4, b, MinerBlocks.POWER.ordinal()
        ); /*
setBlock(array, dir, x, y, z, i + 3, j + 13, k + 0, Blocks.air);
setBlock(array, dir, x, y, z, i + 3, j + 13, k + 1, Blocks.air);
setBlock(array, dir, x, y, z, i + 3, j + 13, k + 2, Blocks.air);
setBlock(array, dir, x, y, z, i + 3, j + 13, k + 3, Blocks.air);*/
        setBlock(
            array, dir, x, y, z, i + 3, j + 13, k + 4, b, MinerBlocks.POWER.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 4, j + 0, k + 0, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 4, j + 0, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 4, j + 0, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 4, j + 0, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 4, j + 0, k + 4, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 4, j + 1, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 4, j + 1, k + 1, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 4, j + 1, k + 2, b, MinerBlocks.DRILL.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 4, j + 1, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 4, j + 1, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 4, j + 2, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 4, j + 2, k + 1, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 4, j + 2, k + 2, b, MinerBlocks.DRILL.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 4, j + 2, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 4, j + 2, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 4, j + 3, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 4, j + 3, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 4, j + 3, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 4, j + 3, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 4, j + 3, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 4, j + 4, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 4, j + 4, k + 1, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 4, j + 4, k + 2, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 4, j + 4, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 4, j + 4, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 4, j + 5, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 4, j + 5, k + 1, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 4, j + 5, k + 2, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 4, j + 5, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 4, j + 5, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 4, j + 6, k + 0, b, MinerBlocks.GRAY.ordinal());
        setBlock(array, dir, x, y, z, i + 4, j + 6, k + 1, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 4, j + 6, k + 2, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 4, j + 6, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 4, j + 6, k + 4, b, MinerBlocks.GRAY.ordinal());
        setBlock(
            array, dir, x, y, z, i + 4, j + 7, k + 0, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 4, j + 7, k + 1, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 4, j + 7, k + 2, b, MinerBlocks.DARK.ordinal());
        setBlock(
            array, dir, x, y, z, i + 4, j + 7, k + 3, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 4, j + 7, k + 4, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 4, j + 8, k + 0, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 4, j + 8, k + 1, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 4, j + 8, k + 2, b, MinerBlocks.GRAY.ordinal());
        setBlock(
            array, dir, x, y, z, i + 4, j + 8, k + 3, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 4, j + 8, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 4, j + 9, k + 0, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 4, j + 9, k + 1, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 4, j + 9, k + 2, b, MinerBlocks.GRAY.ordinal());
        setBlock(
            array, dir, x, y, z, i + 4, j + 9, k + 3, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 4, j + 9, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 4, j + 10, k + 0, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 4, j + 10, k + 1, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 4, j + 10, k + 2, b, MinerBlocks.GRAY.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 4, j + 10, k + 3, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 4, j + 10, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 4, j + 11, k + 0, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 4, j + 11, k + 1, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 4, j + 11, k + 2, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 4, j + 11, k + 3, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 4, j + 11, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 4, j + 12, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 4, j + 12, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 4, j + 12, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 4, j + 12, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 4, j + 12, k + 4, Blocks.air); /*
         setBlock(array, dir, x, y, z, i + 4, j + 13, k + 0, Blocks.air);
         setBlock(array, dir, x, y, z, i + 4, j + 13, k + 1, Blocks.air);
         setBlock(array, dir, x, y, z, i + 4, j + 13, k + 2, Blocks.air);
         setBlock(array, dir, x, y, z, i + 4, j + 13, k + 3, Blocks.air);
         setBlock(array, dir, x, y, z, i + 4, j + 13, k + 4, Blocks.air);*/
        setBlock(array, dir, x, y, z, i + 5, j + 0, k + 0, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 5, j + 0, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 0, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 0, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 0, k + 4, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 5, j + 1, k + 0, b, MinerBlocks.DARK.ordinal());
        setBlock(
            array, dir, x, y, z, i + 5, j + 1, k + 1, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 5, j + 1, k + 2, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 5, j + 1, k + 3, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 5, j + 1, k + 4, b, MinerBlocks.DARK.ordinal());
        setBlock(
            array, dir, x, y, z, i + 5, j + 2, k + 0, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 5, j + 2, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 2, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 2, k + 3, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 5, j + 2, k + 4, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 5, j + 3, k + 0, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 5, j + 3, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 3, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 3, k + 3, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 5, j + 3, k + 4, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 5, j + 4, k + 0, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 5, j + 4, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 4, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 4, k + 3, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 5, j + 4, k + 4, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 5, j + 5, k + 0, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 5, j + 5, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 5, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 5, k + 3, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 5, j + 5, k + 4, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 5, j + 6, k + 0, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 5, j + 6, k + 1, b, MinerBlocks.GRAY.ordinal());
        setBlock(array, dir, x, y, z, i + 5, j + 6, k + 2, b, MinerBlocks.GRAY.ordinal());
        setBlock(array, dir, x, y, z, i + 5, j + 6, k + 3, b, MinerBlocks.GRAY.ordinal());
        setBlock(
            array, dir, x, y, z, i + 5, j + 6, k + 4, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 5, j + 7, k + 0, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 5, j + 7, k + 1, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 5, j + 7, k + 2, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 5, j + 7, k + 3, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 5, j + 7, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 8, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 8, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 8, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 8, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 8, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 9, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 9, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 9, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 9, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 9, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 10, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 10, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 10, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 10, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 10, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 11, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 11, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 11, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 11, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 11, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 12, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 12, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 12, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 12, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 5, j + 12, k + 4, Blocks.air); /*
         setBlock(array, dir, x, y, z, i + 5, j + 13, k + 0, Blocks.air);
         setBlock(array, dir, x, y, z, i + 5, j + 13, k + 1, Blocks.air);
         setBlock(array, dir, x, y, z, i + 5, j + 13, k + 2, Blocks.air);
         setBlock(array, dir, x, y, z, i + 5, j + 13, k + 3, Blocks.air);
         setBlock(array, dir, x, y, z, i + 5, j + 13, k + 4, Blocks.air);*/
        setBlock(array, dir, x, y, z, i + 6, j + 0, k + 0, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 6, j + 0, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 0, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 0, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 0, k + 4, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 6, j + 1, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 1, k + 1, b, MinerBlocks.GRAY.ordinal());
        setBlock(array, dir, x, y, z, i + 6, j + 1, k + 2, b, MinerBlocks.GRAY.ordinal());
        setBlock(array, dir, x, y, z, i + 6, j + 1, k + 3, b, MinerBlocks.GRAY.ordinal());
        setBlock(array, dir, x, y, z, i + 6, j + 1, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 2, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 2, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 2, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 2, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 2, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 3, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 3, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 3, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 3, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 3, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 4, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 4, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 4, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 4, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 4, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 5, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 5, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 5, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 5, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 5, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 6, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 6, k + 1, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 6, j + 6, k + 2, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 6, j + 6, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 6, k + 4, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 6, j + 7, k + 0, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 6, j + 7, k + 1, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 6, j + 7, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 7, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 7, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 8, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 8, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 8, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 8, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 9, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 9, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 9, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 9, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 10, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 10, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 10, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 10, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 11, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 11, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 11, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 11, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 11, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 12, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 12, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 12, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 12, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 6, j + 12, k + 4, Blocks.air); /*
         setBlock(array, dir, x, y, z, i + 6, j + 13, k + 0, Blocks.air);
         setBlock(array, dir, x, y, z, i + 6, j + 13, k + 1, Blocks.air);
         setBlock(array, dir, x, y, z, i + 6, j + 13, k + 2, Blocks.air);
         setBlock(array, dir, x, y, z, i + 6, j + 13, k + 3, Blocks.air);
         setBlock(array, dir, x, y, z, i + 6, j + 13, k + 4, Blocks.air);*/
        setBlock(array, dir, x, y, z, i + 7, j + 0, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 0, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 0, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 0, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 0, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 1, k + 0, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 7, j + 1, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 1, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 1, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 1, k + 4, b, MinerBlocks.DARK.ordinal());
        setBlock(
            array, dir, x, y, z, i + 7, j + 2, k + 0, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 7, j + 2, k + 1, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 7, j + 2, k + 2, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 7, j + 2, k + 3, b, MinerBlocks.DARK.ordinal());
        setBlock(
            array, dir, x, y, z, i + 7, j + 2, k + 4, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 7, j + 3, k + 0, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 7, j + 3, k + 1, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 7, j + 3, k + 2, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 7, j + 3, k + 3, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 7, j + 3, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 4, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 4, k + 1, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 7, j + 4, k + 2, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 7, j + 4, k + 3, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 7, j + 4, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 5, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 5, k + 1, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 7, j + 5, k + 2, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 7, j + 5, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 5, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 6, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 6, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 6, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 6, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 6, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 7, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 7, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 7, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 7, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 7, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 8, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 8, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 8, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 8, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 8, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 9, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 9, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 9, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 9, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 9, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 10, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 10, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 10, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 10, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 10, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 11, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 11, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 11, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 11, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 11, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 12, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 12, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 12, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 12, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 7, j + 12, k + 4, Blocks.air); /*
         setBlock(array, dir, x, y, z, i + 7, j + 13, k + 0, Blocks.air);
         setBlock(array, dir, x, y, z, i + 7, j + 13, k + 1, Blocks.air);
         setBlock(array, dir, x, y, z, i + 7, j + 13, k + 2, Blocks.air);
         setBlock(array, dir, x, y, z, i + 7, j + 13, k + 3, Blocks.air);
         setBlock(array, dir, x, y, z, i + 7, j + 13, k + 4, Blocks.air);*/
        setBlock(array, dir, x, y, z, i + 8, j + 0, k + 0, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 8, j + 0, k + 1, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 8, j + 0, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 0, k + 3, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 8, j + 0, k + 4, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 8, j + 1, k + 0, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 8, j + 1, k + 1, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 8, j + 1, k + 2, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 8, j + 1, k + 3, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 8, j + 1, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 2, k + 0, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 8, j + 2, k + 1, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 8, j + 2, k + 2, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 8, j + 2, k + 3, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 8, j + 2, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 3, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 3, k + 1, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 8, j + 3, k + 2, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 8, j + 3, k + 3, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 8, j + 3, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 4, k + 0, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 8, j + 4, k + 1, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 8, j + 4, k + 2, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 8, j + 4, k + 3, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 8, j + 4, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 5, k + 0, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 8, j + 5, k + 1, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 8, j + 5, k + 2, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 8, j + 5, k + 3, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 8, j + 5, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 6, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 6, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 6, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 6, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 6, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 7, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 7, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 7, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 7, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 7, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 8, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 8, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 8, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 8, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 8, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 9, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 9, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 9, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 9, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 9, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 10, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 10, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 10, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 10, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 10, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 11, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 11, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 11, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 11, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 11, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 12, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 12, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 12, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 12, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 8, j + 12, k + 4, Blocks.air); /*
         setBlock(array, dir, x, y, z, i + 8, j + 13, k + 0, Blocks.air);
         setBlock(array, dir, x, y, z, i + 8, j + 13, k + 1, Blocks.air);
         setBlock(array, dir, x, y, z, i + 8, j + 13, k + 2, Blocks.air);
         setBlock(array, dir, x, y, z, i + 8, j + 13, k + 3, Blocks.air);
         setBlock(array, dir, x, y, z, i + 8, j + 13, k + 4, Blocks.air);*/
        setBlock(array, dir, x, y, z, i + 9, j + 0, k + 0, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 9, j + 0, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 0, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 0, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 0, k + 4, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 9, j + 1, k + 0, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 9, j + 1, k + 1, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 9, j + 1, k + 2, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 9, j + 1, k + 3, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 9, j + 1, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 2, k + 0, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 9, j + 2, k + 1, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 9, j + 2, k + 2, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 9, j + 2, k + 3, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 9, j + 2, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 3, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 3, k + 1, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 9, j + 3, k + 2, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 9, j + 3, k + 3, b, MinerBlocks.DARK.ordinal());
        setBlock(array, dir, x, y, z, i + 9, j + 3, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 4, k + 0, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 9, j + 4, k + 1, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 9, j + 4, k + 2, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 9, j + 4, k + 3, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 9, j + 4, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 5, k + 0, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 9, j + 5, k + 1, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 9, j + 5, k + 2, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 9, j + 5, k + 3, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 9, j + 5, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 6, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 6, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 6, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 6, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 6, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 7, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 7, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 7, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 7, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 7, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 8, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 8, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 8, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 8, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 8, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 9, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 9, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 9, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 9, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 9, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 10, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 10, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 10, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 10, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 10, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 11, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 11, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 11, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 11, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 11, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 12, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 12, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 12, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 12, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 9, j + 12, k + 4, Blocks.air); /*
         setBlock(array, dir, x, y, z, i + 9, j + 13, k + 0, Blocks.air);
         setBlock(array, dir, x, y, z, i + 9, j + 13, k + 1, Blocks.air);
         setBlock(array, dir, x, y, z, i + 9, j + 13, k + 2, Blocks.air);
         setBlock(array, dir, x, y, z, i + 9, j + 13, k + 3, Blocks.air);
         setBlock(array, dir, x, y, z, i + 9, j + 13, k + 4, Blocks.air);*/
        setBlock(
            array, dir, x, y, z, i + 10, j + 0, k + 0, b, MinerBlocks.DARK.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 10, j + 0, k + 1, b, MinerBlocks.DARK.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 10, j + 0, k + 2, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 10, j + 0, k + 3, b, MinerBlocks.DARK.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 10, j + 0, k + 4, b, MinerBlocks.DARK.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 10, j + 1, k + 0, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 10, j + 1, k + 1, b, MinerBlocks.ORANGE.ordinal()
        );

        setBlock(
            array, dir, x, y, z, i + 10, j + 1, k + 2, b, MinerBlocks.CONVEYOR.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 11, j + 1, k + 2, b, MinerBlocks.CONVEYOR.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 11, j + 1, k + 1, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 11, j + 1, k + 3, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 11, j + 2, k + 2, b, MinerBlocks.SILVER.ordinal()
        );

        setBlock(
            array, dir, x, y, z, i + 10, j + 1, k + 3, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 10, j + 1, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 2, k + 0, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 10, j + 2, k + 1, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 10, j + 2, k + 2, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 10, j + 2, k + 3, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 10, j + 2, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 3, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 3, k + 1, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 10, j + 3, k + 2, b, MinerBlocks.DARK.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 10, j + 3, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 3, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 4, k + 0, Blocks.air);
        setBlock(
            array, dir, x, y, z, i + 10, j + 4, k + 1, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 10, j + 4, k + 2, b, MinerBlocks.SILVER.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 10, j + 4, k + 3, b, MinerBlocks.ORANGE.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 10, j + 4, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 5, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 5, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 5, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 5, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 5, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 6, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 6, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 6, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 6, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 6, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 7, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 7, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 7, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 7, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 7, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 8, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 8, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 8, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 8, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 8, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 9, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 9, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 9, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 9, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 9, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 10, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 10, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 10, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 10, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 10, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 11, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 11, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 11, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 11, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 11, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 12, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 12, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 12, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 12, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 10, j + 12, k + 4, Blocks.air); /*
         setBlock(array, dir, x, y, z, i + 10, j + 13, k + 0, Blocks.air);
         setBlock(array, dir, x, y, z, i + 10, j + 13, k + 1, Blocks.air);
         setBlock(array, dir, x, y, z, i + 10, j + 13, k + 2, Blocks.air);
         setBlock(array, dir, x, y, z, i + 10, j + 13, k + 3, Blocks.air);
         setBlock(array, dir, x, y, z, i + 10, j + 13, k + 4, Blocks.air);*/
        setBlock(
            array, dir, x, y, z, i + 11, j + 0, k + 0, b, MinerBlocks.DARK.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 11, j + 0, k + 1, b, MinerBlocks.DARK.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 11, j + 0, k + 2, b, MinerBlocks.DARK.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 11, j + 0, k + 3, b, MinerBlocks.DARK.ordinal()
        );
        setBlock(
            array, dir, x, y, z, i + 11, j + 0, k + 4, b, MinerBlocks.DARK.ordinal()
        );
        setBlock(array, dir, x, y, z, i + 11, j + 1, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 1, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 2, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 2, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 2, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 2, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 3, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 3, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 3, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 3, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 3, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 4, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 4, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 4, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 4, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 4, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 5, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 5, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 5, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 5, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 5, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 6, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 6, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 6, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 6, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 6, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 7, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 7, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 7, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 7, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 7, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 8, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 8, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 8, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 8, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 8, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 9, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 9, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 9, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 9, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 9, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 10, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 10, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 10, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 10, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 10, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 11, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 11, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 11, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 11, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 11, k + 4, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 12, k + 0, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 12, k + 1, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 12, k + 2, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 12, k + 3, Blocks.air);
        setBlock(array, dir, x, y, z, i + 11, j + 12, k + 4, Blocks.air); /*
         setBlock(array, dir, x, y, z, i + 11, j + 13, k + 0, Blocks.air);
         setBlock(array, dir, x, y, z, i + 11, j + 13, k + 1, Blocks.air);
         setBlock(array, dir, x, y, z, i + 11, j + 13, k + 2, Blocks.air);
         setBlock(array, dir, x, y, z, i + 11, j + 13, k + 3, Blocks.air);
         setBlock(array, dir, x, y, z, i + 11, j + 13, k + 4, Blocks.air);*/

        return array;
    }
}
