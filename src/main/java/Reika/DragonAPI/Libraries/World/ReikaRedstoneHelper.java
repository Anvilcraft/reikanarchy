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

import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.Interfaces.Block.WireBlock;
import Reika.DragonAPI.Libraries.Java.ReikaArrayHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public final class ReikaRedstoneHelper extends DragonAPICore {
    /**
     * Returns true on the postive redstone edge. Args: World, x, y, z, last power state
     */
    @Deprecated
    public static boolean
    isPositiveEdge(World world, int x, int y, int z, boolean lastPower) {
        if (world.isBlockIndirectlyGettingPowered(x, y, z) == lastPower)
            return false;
        if (!world.isBlockIndirectlyGettingPowered(x, y, z))
            return false;
        return true;
    }

    /**
     * Returns true on the postive redstone edge on a side. Args: World, x, y, z, last
     * power state, last repeater-power state, side
     */
    public static boolean isPositiveEdgeOnSide(
        World world,
        int x,
        int y,
        int z,
        boolean lastPower,
        boolean lastRepeat,
        ForgeDirection side
    ) {
        boolean pwr = isPoweredOnSide(world, x, y, z, side);
        boolean rpt = isReceivingPowerFromRepeater(world, x, y, z, side);
        //DragonAPICore.log(((sided || repeat) && pwr && !lastPower)+" for "+lastPower);
        return (pwr || rpt) && !lastPower && !lastRepeat;
    }

    public static boolean
    isPoweredOnSide(World world, int x, int y, int z, ForgeDirection side) {
        int dx = x + side.offsetX;
        int dy = y + side.offsetY;
        int dz = z + side.offsetZ;
        if (BlockRedstoneWire.func_150176_g(world, x, y, z, side.ordinal())) {
            Block b = world.getBlock(dx, dy, dz);
            if (b instanceof WireBlock) {
                return ((WireBlock) b)
                           .isConnectedTo(world, dx, dy, dz, side.getOpposite().ordinal())
                    && ((WireBlock) b).getPowerState(world, dx, dy, dz) > 0;
            } else if (b instanceof BlockRedstoneWire) {
                return world.getBlockMetadata(dx, dy, dz) > 0;
            }
        }
        return world.getIndirectPowerOutput(dx, dy, dz, side.ordinal());
    }

    /**
     * Is the block receiving power from a repeater on a side. Args: World, x, y, z, side
     */
    public static boolean
    isReceivingPowerFromRepeater(World world, int x, int y, int z, ForgeDirection side) {
        Block b = world.getBlock(x + side.offsetX, y + side.offsetY, z + side.offsetZ);
        int meta = world.getBlockMetadata(
            x + side.offsetX, y + side.offsetY, z + side.offsetZ
        );
        boolean dir = false;
        if (b != Blocks.air) {
            if (b instanceof BlockRedstoneDiode) {
                BlockRedstoneDiode lgc = (BlockRedstoneDiode) b;
                int direct = lgc.getDirection(meta);
                int dx = Direction.offsetX[direct];
                int dz = Direction.offsetZ[direct];
                dir = (dx == side.offsetX && dz == side.offsetZ);
            }
        }
        boolean power = (b == Blocks.powered_comparator || b == Blocks.powered_repeater);
        return power && dir;
    }

    /**
     * Returns true on the negative redstone edge. Args: World, x, y, z, last power
     * state
     */
    @Deprecated
    public static boolean
    isNegativeEdge(World world, int x, int y, int z, boolean lastPower) {
        if (world.isBlockIndirectlyGettingPowered(x, y, z) == lastPower)
            return false;
        if (world.isBlockIndirectlyGettingPowered(x, y, z))
            return false;
        return true;
    }

    /** Returns true on a redstone edge. Args: World, x, y, z, last power state */
    @Deprecated
    public static boolean isEdge(World world, int x, int y, int z, boolean lastPower) {
        if (world.isBlockIndirectlyGettingPowered(x, y, z) == lastPower)
            return false;
        return true;
    }

    /**
     * Returns true if the redstone signal is greater or equal to a value. Args: World,
     * x, y, z, level
     */
    public static boolean isRedstoneAtLevel(World world, int x, int y, int z, int level) {
        if (level > 15) {
            DragonAPICore.logError("Redstone level " + level + " is impossible!");
            return false;
        }
        int pwr = world.getBlockPowerInput(x, y, z);
        return (pwr >= level);
    }

    /**
     * Returns true if the redstone signal is either zero or full (15). Args: World, x,
     * y, z
     */
    public static boolean isBinaryValue(World world, int x, int y, int z) {
        int pwr = world.getBlockPowerInput(x, y, z);
        return (pwr == 0 || pwr == 15);
    }

    /**
     * Returns true if the redstone signal to x,y,z is alternating. Supply a larger
     * lastPower array for more lenience.
     */
    public static boolean
    isGettingACRedstone(World world, int x, int y, int z, boolean[] lastPower) {
        boolean currentpower = world.isBlockIndirectlyGettingPowered(x, y, z);
        boolean ac = false;
        for (int i = 0; i < lastPower.length && !ac; i++) {
            if (lastPower[i] != currentpower)
                ac = true;
        }

        ReikaArrayHelper.cycleArray(lastPower, currentpower);

        return ac;
    }
}
