/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.World;

import java.util.ArrayList;
import java.util.Random;

import Reika.ChromatiCraft.API.Event.RainbowTreeEvent;
import Reika.ChromatiCraft.Auxiliary.Structure.RainbowTreeBlueprint;
import Reika.ChromatiCraft.Block.Dye.BlockRainbowLeaf.LeafMetas;
import Reika.ChromatiCraft.ChromatiCraft;
import Reika.ChromatiCraft.Registry.ChromaBlocks;
import Reika.DragonAPI.Instantiable.Data.BlockStruct.BlockArray;
import Reika.DragonAPI.Instantiable.Data.Immutable.Coordinate;
import Reika.DragonAPI.Interfaces.Registry.TreeType;
import Reika.DragonAPI.Libraries.Java.ReikaJavaLibrary;
import Reika.DragonAPI.Libraries.Registry.ReikaTreeHelper;
import Reika.DragonAPI.Libraries.World.ReikaWorldHelper;
import Reika.DragonAPI.ModInteract.ItemHandlers.ForestryHandler;
import Reika.DragonAPI.ModRegistry.ModWoodList;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class RainbowTreeGenerator {
    private static final RainbowTreeGenerator instance = new RainbowTreeGenerator();

    private final ArrayList<TreeType> validLogs = new ArrayList();

    private RainbowTreeGenerator() {
        for (int i = 0; i < ReikaTreeHelper.treeList.length; i++) {
            ReikaTreeHelper tree = ReikaTreeHelper.treeList[i];
            if (ChromatiCraft.config.shouldGenerateLogType(tree)) {
                validLogs.add(tree);
            } else {
            }
        }

        for (int i = 0; i < ModWoodList.woodList.length; i++) {
            ModWoodList tree = ModWoodList.woodList[i];
            if (tree.exists()) {
                if (this.isLogTypeEverAllowed(tree)) {
                    if (ChromatiCraft.config.shouldGenerateLogType(tree)) {
                        validLogs.add(tree);
                    } else {
                    }
                } else {
                }
            } else {
            }
        }
    }

    public boolean isLogTypeEverAllowed(ModWoodList wood) {
        if (!wood.canBePlacedSideways())
            return false;

        switch (wood) {
            case GLOW:
            case BLOODWOOD:
            case NATURADARKWOOD:
            case FUSEWOOD:
            case BXLREDWOOD:
            case BAMBOO:
            case IC2RUBBER:
            case MFRRUBBER:
            case MINEWOOD:
            case SEQUOIA:
            case SORTING:
            case TIMEWOOD:
            case TRANSFORMATION:
            case TWILIGHTOAK:
            case TAINTED:
                return false;
            default:
                return true;
        }
    }

    public static final RainbowTreeGenerator getInstance() {
        return instance;
    }

    public boolean checkRainbowTreeSpace(World world, int x, int y, int z) {
        x -= 5;
        y -= 2;
        z -= 5;

        BlockArray blocks
            = RainbowTreeBlueprint.getBlueprint(world, x, y, z, ReikaTreeHelper.OAK);

        for (int i = 0; i < blocks.getSize(); i++) {
            Coordinate c = blocks.getNthBlock(i);
            int dx = c.xCoord;
            int dy = c.yCoord;
            int dz = c.zCoord;
            if (dy < y + 2)
                continue;
            Block id = world.getBlock(dx, dy, dz);
            if (id != Blocks.leaves && id != Blocks.leaves2 && id != Blocks.web) {
                if (id != ChromaBlocks.RAINBOWSAPLING.getBlockInstance()
                    && id != ChromaBlocks.RAINBOWLEAF.getBlockInstance()
                    && id != ForestryHandler.BlockEntry.SAPLING.getBlock()) {
                    if (!ReikaWorldHelper.softBlocks(world, dx, dy, dz)) {
                        //ReikaJavaLibrary.pConsole(id+"@"+Arrays.toString(xyz));
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void generateLargeRainbowTree(World world, int x, int y, int z, Random rand) {
        //to compensate for the offset Mithion's generator added
        x -= 5;
        y -= 2;
        z -= 5;
        ChromatiCraft.logger.debug(
            "Generating Rainbow Tree @ " + x + ", " + y + ", " + z
        );
        MinecraftForge.EVENT_BUS.post(new RainbowTreeEvent(world, x, y, z, rand));

        for (int dx = x + 4; dx <= x + 6; dx++) {
            for (int dz = z + 4; dz <= z + 6; dz++) {
                Block block = world.getBlock(dx, y + 2, dz);
                if (block == ChromaBlocks.RAINBOWSAPLING.getBlockInstance())
                    world.setBlockToAir(dx, y + 2, dz);
            }
        }

        for (int dx = x + 2; dx <= x + 7; dx++) {
            for (int dz = z + 2; dz <= z + 7; dz++) {
                for (int dy = y + 1; dy > y - 3; dy--) {
                    if (ReikaWorldHelper.softBlocks(world, dx, dy, dz))
                        world.setBlock(
                            dx, dy, dz, dy == y + 1 ? Blocks.grass : Blocks.dirt
                        );
                }
            }
        }

        RainbowTreeBlueprint.getBlueprint(world, x, y, z, this.getLogType(rand)).place();
    }

    public boolean
    tryGenerateSmallRainbowTree(World world, int x, int y, int z, Random rand) {
        return this.tryGenerateSmallRainbowTree(world, x, y, z, rand, 1);
    }

    public boolean
    tryGenerateSmallRainbowTree(World world, int x, int y, int z, Random rand, float hf) {
        Block id = ChromaBlocks.RAINBOWLEAF.getBlockInstance();
        TreeType wood = this.getLogType(rand);

        int h0 = 2 + rand.nextInt(3);
        int h = h0 + MathHelper.ceiling_float_int(hf * (5 + rand.nextInt(7)));
        for (int i = 1; i <= h; i++) {
            if (world.getBlock(x, y + i, z) != Blocks.air) {
                h = i;
                break;
            }
        }
        while (h > 2 && world.getBlock(x, y + h + 4, z) != Blocks.air)
            h--;
        if (h <= 3)
            return false;
        h0 = Math.min(h0, h / 2);
        int w = 0;
        for (int j = 0; j < h; j++) {
            int dy = y + j;
            world.setBlock(x, dy, z, wood.getLogID(), wood.getLogMetadatas().get(0), 3);
            if (j >= h0) {
                boolean canUp = w < 5 && h - j - 1 > w;
                boolean canDown = w > 1;
                boolean up = (rand.nextBoolean() && canUp) || !canDown;
                if (up) {
                    w++;
                } else {
                    w--;
                }
                //ReikaJavaLibrary.pConsole("j = "+j+"/"+h+", "+(up ? "up" : "down")+" to
                //"+w);
                int r = w;
                for (int i = -r; i <= r; i++) {
                    for (int k = -r; k <= r; k++) {
                        if ((i != 0 || k != 0) && Math.abs(i) + Math.abs(k) <= w + 1) {
                            int dx = x + i;
                            int dz = z + k;
                            if (world.getBlock(dx, dy, dz)
                                    .canBeReplacedByLeaves(world, dx, dy, dz))
                                world.setBlock(
                                    dx, dy, dz, id, LeafMetas.SMALL.ordinal(), 3
                                );
                        }
                    }
                }
            }
        }
        for (int i = -1; i <= 1; i++) {
            for (int k = -1; k <= 1; k++) {
                if (i == 0 || k == 0) {
                    int dx = x + i;
                    int dz = z + k;
                    if (world.getBlock(dx, y + h, dz)
                            .canBeReplacedByLeaves(world, dx, y + h, dz))
                        world.setBlock(dx, y + h, dz, id, LeafMetas.SMALL.ordinal(), 3);
                }
            }
        }
        if (world.getBlock(x, y + h + 1, z).canBeReplacedByLeaves(world, x, y + h + 1, z))
            world.setBlock(x, y + h + 1, z, id, LeafMetas.SMALL.ordinal(), 3);
        return true;
    }

    private TreeType getLogType(Random rand) {
        return ReikaJavaLibrary.getRandomListEntry(rand, validLogs);
    }
}
