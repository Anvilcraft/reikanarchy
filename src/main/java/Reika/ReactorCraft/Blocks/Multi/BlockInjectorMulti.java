/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ReactorCraft.Blocks.Multi;

import Reika.DragonAPI.Instantiable.Data.BlockStruct.BlockArray;
import Reika.DragonAPI.Instantiable.Data.BlockStruct.FilledBlockArray.BlockMatchFailCallback;
import Reika.DragonAPI.Instantiable.Data.BlockStruct.StructuredBlockArray;
import Reika.DragonAPI.Instantiable.Data.Immutable.BlockKey;
import Reika.DragonAPI.Instantiable.Data.Immutable.Coordinate;
import Reika.DragonAPI.Libraries.ReikaDirectionHelper;
import Reika.DragonAPI.Libraries.World.ReikaWorldHelper;
import Reika.ReactorCraft.Auxiliary.NeutronBlock;
import Reika.ReactorCraft.Base.BlockReCMultiBlock;
import Reika.ReactorCraft.Entities.EntityNeutron;
import Reika.ReactorCraft.Registry.ReactorTiles;
import Reika.ReactorCraft.TileEntities.Fusion.TileEntityFusionInjector;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockInjectorMulti extends BlockReCMultiBlock implements NeutronBlock {
    public BlockInjectorMulti(Material par2Material) {
        super(par2Material);
    }

    @Override
    public int getNumberVariants() {
        return 8;
    }

    @Override
    public Boolean checkForFullMultiBlock(
        World world, int x, int y, int z, ForgeDirection dir, BlockMatchFailCallback call
    ) {
        dir = ReikaWorldHelper.checkForAdjBlock(world, x, y, z, this, 7);
        if (dir == null)
            return false;
        ForgeDirection left = ReikaDirectionHelper.getLeftBy90(dir);
        StructuredBlockArray blocks = new StructuredBlockArray(world);
        blocks.recursiveAddWithBounds(
            world, x, y, z, this, x - 8, y - 5, z - 8, x + 8, y + 5, z + 8
        );
        while (world.getBlock(x, y - 1, z) == this
               && world.getBlockMetadata(x, y - 1, z) == 5)
            y--;
        if (!this.checkTop(world, x, y, z, dir, left, blocks, call))
            return false;
        if (!this.checkBottom(world, x, y, z, dir, left, blocks, call))
            return false;
        if (!this.checkSides(world, x, y, z, dir, left, blocks, call))
            return false;
        if (!this.checkCorners(world, x, y, z, dir, left, blocks, call))
            return false;
        if (!this.checkFiller(world, x, y, z, dir, left, blocks, call))
            return false;
        if (!this.checkPipes(world, x, y, z, dir, left, blocks, call))
            return false;
        return true;
    }

    private boolean
    checkAt(World world, int dx, int dy, int dz, int metas, BlockMatchFailCallback call) {
        return this.checkAt(world, dx, dy, dz, this, metas, call);
    }

    private boolean checkAt(
        World world,
        int dx,
        int dy,
        int dz,
        Block bs,
        int metas,
        BlockMatchFailCallback call
    ) {
        Block b = world.getBlock(dx, dy, dz);
        int meta = world.getBlockMetadata(dx, dy, dz);
        if (b != bs || meta != metas) {
            if (call != null)
                call.onBlockFailure(world, dx, dy, dz, new BlockKey(bs, metas));
            return false;
        }
        return true;
    }

    private boolean checkCorners(
        World world,
        int x,
        int y,
        int z,
        ForgeDirection dir,
        ForgeDirection left,
        StructuredBlockArray blocks,
        BlockMatchFailCallback call
    ) {
        for (int i = 0; i <= 4; i++) {
            if (!this.checkAt(
                    world,
                    x + dir.offsetX * i + left.offsetX,
                    y + 3,
                    z + dir.offsetZ * i + left.offsetZ,
                    4,
                    call
                )) {
                return false;
            }

            if (!this.checkAt(
                    world,
                    x + dir.offsetX * i - left.offsetX,
                    y + 3,
                    z + dir.offsetZ * i - left.offsetZ,
                    4,
                    call
                )) {
                return false;
            }
        }
        for (int i = 5; i <= 6; i++) {
            if (!this.checkAt(
                    world,
                    x + dir.offsetX * i + left.offsetX,
                    y + 2,
                    z + dir.offsetZ * i + left.offsetZ,
                    4,
                    call
                )) {
                return false;
            }

            if (!this.checkAt(
                    world,
                    x + dir.offsetX * i - left.offsetX,
                    y + 2,
                    z + dir.offsetZ * i - left.offsetZ,
                    4,
                    call
                )) {
                return false;
            }
        }
        for (int i = 7; i <= 8; i++) {
            if (!this.checkAt(
                    world,
                    x + dir.offsetX * i + left.offsetX,
                    y + 1,
                    z + dir.offsetZ * i + left.offsetZ,
                    4,
                    call
                )) {
                return false;
            }

            if (!this.checkAt(
                    world,
                    x + dir.offsetX * i - left.offsetX,
                    y + 1,
                    z + dir.offsetZ * i - left.offsetZ,
                    4,
                    call
                )) {
                return false;
            }
        }

        for (int i = 0; i <= 8; i++) {
            if (!this.checkAt(
                    world,
                    x + dir.offsetX * i + left.offsetX,
                    y - 1,
                    z + dir.offsetZ * i + left.offsetZ,
                    1,
                    call
                )) {
                return false;
            }
        }
        for (int i = 0; i <= 8; i++) {
            if (!this.checkAt(
                    world,
                    x + dir.offsetX * i - left.offsetX,
                    y - 1,
                    z + dir.offsetZ * i - left.offsetZ,
                    1,
                    call
                )) {
                return false;
            }
        }

        for (int k = 0; k <= 2; k++) {
            if (!this.checkAt(
                    world, x + left.offsetX, y + k, z + left.offsetZ, 6, call
                )) {
                return false;
            }

            if (!this.checkAt(
                    world, x - left.offsetX, y + k, z - left.offsetZ, 6, call
                )) {
                return false;
            }
        }

        if (!this.checkAt(
                world,
                x + left.offsetX + dir.offsetX * 8,
                y,
                z + left.offsetZ + dir.offsetZ * 8,
                6,
                call
            )) {
            return false;
        }

        if (!this.checkAt(
                world,
                x - left.offsetX + dir.offsetX * 8,
                y,
                z - left.offsetZ + dir.offsetZ * 8,
                6,
                call
            )) {
            return false;
        }

        return true;
    }

    private boolean checkTop(
        World world,
        int x,
        int y,
        int z,
        ForgeDirection dir,
        ForgeDirection left,
        StructuredBlockArray blocks,
        BlockMatchFailCallback call
    ) {
        for (int i = 0; i <= 4; i++) {
            if (!this.checkAt(
                    world, x + dir.offsetX * i, y + 3, z + dir.offsetZ * i, 3, call
                )) {
                return false;
            }
        }
        for (int i = 5; i <= 6; i++) {
            if (!this.checkAt(
                    world, x + dir.offsetX * i, y + 2, z + dir.offsetZ * i, 3, call
                )) {
                return false;
            }
        }
        for (int i = 7; i <= 8; i++) {
            if (!this.checkAt(
                    world, x + dir.offsetX * i, y + 1, z + dir.offsetZ * i, 3, call
                )) {
                return false;
            }
        }
        return true;
    }

    private boolean checkBottom(
        World world,
        int x,
        int y,
        int z,
        ForgeDirection dir,
        ForgeDirection left,
        StructuredBlockArray blocks,
        BlockMatchFailCallback call
    ) {
        for (int i = 0; i <= 8; i++) {
            if (!this.checkAt(
                    world, x + dir.offsetX * i, y - 1, z + dir.offsetZ * i, 0, call
                ))
                return false;
        }
        return true;
    }

    private boolean checkSides(
        World world,
        int x,
        int y,
        int z,
        ForgeDirection dir,
        ForgeDirection left,
        StructuredBlockArray blocks,
        BlockMatchFailCallback call
    ) {
        for (int i = 1; i <= 1; i++) {
            if (!this.checkAt(
                    world,
                    x + dir.offsetX * i + left.offsetX,
                    y,
                    z + dir.offsetZ * i + left.offsetZ,
                    2,
                    call
                ))
                return false;

            if (!this.checkAt(
                    world,
                    x + dir.offsetX * i - left.offsetX,
                    y,
                    z + dir.offsetZ * i - left.offsetZ,
                    2,
                    call
                ))
                return false;
        }
        for (int i = 3; i <= 7; i++) {
            if (!this.checkAt(
                    world,
                    x + dir.offsetX * i + left.offsetX,
                    y,
                    z + dir.offsetZ * i + left.offsetZ,
                    2,
                    call
                ))
                return false;

            if (!this.checkAt(
                    world,
                    x + dir.offsetX * i - left.offsetX,
                    y,
                    z + dir.offsetZ * i - left.offsetZ,
                    2,
                    call
                ))
                return false;
        }
        for (int i = 1; i <= 6; i++) {
            if (!this.checkAt(
                    world,
                    x + dir.offsetX * i + left.offsetX,
                    y + 1,
                    z + dir.offsetZ * i + left.offsetZ,
                    2,
                    call
                ))
                return false;

            if (!this.checkAt(
                    world,
                    x + dir.offsetX * i - left.offsetX,
                    y + 1,
                    z + dir.offsetZ * i - left.offsetZ,
                    2,
                    call
                ))
                return false;
        }
        for (int i = 1; i <= 4; i++) {
            if (!this.checkAt(
                    world,
                    x + dir.offsetX * i + left.offsetX,
                    y + 2,
                    z + dir.offsetZ * i + left.offsetZ,
                    2,
                    call
                ))
                return false;

            if (!this.checkAt(
                    world,
                    x + dir.offsetX * i - left.offsetX,
                    y + 2,
                    z + dir.offsetZ * i - left.offsetZ,
                    2,
                    call
                ))
                return false;
        }
        for (int i = 0; i <= 2; i++) {
            if (!this.checkAt(world, x, y + i, z, 5, call))
                return false;
        }

        if (!this.checkAt(
                world,
                x + dir.offsetX * 2 + left.offsetX,
                y,
                z + dir.offsetZ * 2 + left.offsetZ,
                Blocks.air,
                0,
                call
            ))
            return false;
        if (!this.checkAt(
                world,
                x + dir.offsetX * 2 - left.offsetX,
                y,
                z + dir.offsetZ * 2 - left.offsetZ,
                Blocks.air,
                0,
                call
            ))
            return false;

        return true;
    }

    private boolean checkFiller(
        World world,
        int x,
        int y,
        int z,
        ForgeDirection dir,
        ForgeDirection left,
        StructuredBlockArray blocks,
        BlockMatchFailCallback call
    ) {
        for (int i = 1; i <= 1; i++) {
            if (!this.checkAt(
                    world, x + dir.offsetX * i, y, z + dir.offsetZ * i, 7, call
                ))
                return false;
        }
        for (int i = 1; i <= 6; i++) {
            if (!this.checkAt(
                    world, x + dir.offsetX * i, y + 1, z + dir.offsetZ * i, 7, call
                ))
                return false;
        }
        for (int i = 1; i <= 4; i++) {
            if (!this.checkAt(
                    world, x + dir.offsetX * i, y + 2, z + dir.offsetZ * i, 7, call
                ))
                return false;
        }
        return true;
    }

    private boolean checkPipes(
        World world,
        int x,
        int y,
        int z,
        ForgeDirection dir,
        ForgeDirection left,
        StructuredBlockArray blocks,
        BlockMatchFailCallback call
    ) {
        for (int i = 3; i <= 8; i++) {
            Block b = world.getBlock(x + dir.offsetX * i, y, z + dir.offsetZ * i);
            int meta
                = world.getBlockMetadata(x + dir.offsetX * i, y, z + dir.offsetZ * i);
            if (ReactorTiles.getMachineFromIDandMetadata(b, meta)
                != ReactorTiles.MAGNETPIPE) {
                if (call != null)
                    call.onBlockFailure(
                        world,
                        x + dir.offsetX * i,
                        y,
                        z + dir.offsetZ * i,
                        new BlockKey(ReactorTiles.MAGNETPIPE)
                    );
                return false;
            }
        }
        if (ReactorTiles.getTE(world, x + dir.offsetX * 2, y, z + dir.offsetZ * 2)
            != ReactorTiles.INJECTOR) {
            if (call != null)
                call.onBlockFailure(
                    world,
                    x + dir.offsetX * 2,
                    y,
                    z + dir.offsetZ * 2,
                    new BlockKey(ReactorTiles.INJECTOR)
                );
            return false;
        }
        return true;
    }

    @Override
    public void
    onCreateFullMultiBlock(World world, int x, int y, int z, Boolean complete) {
        BlockArray blocks = new BlockArray();
        blocks.recursiveAddWithBounds(
            world, x, y, z, this, x - 8, y - 5, z - 8, x + 8, y + 5, z + 8
        );
        for (int i = 0; i < blocks.getSize(); i++) {
            Coordinate c = blocks.getNthBlock(i);
            int meta = c.getBlockMetadata(world);
            if (meta < 8) {
                world.setBlockMetadataWithNotify(
                    c.xCoord, c.yCoord, c.zCoord, meta + 8, 3
                );
            }
            if (meta == 0) {
                if (ReactorTiles.getTE(world, c.xCoord, c.yCoord + 1, c.zCoord)
                    == ReactorTiles.INJECTOR) {
                    TileEntityFusionInjector te = (TileEntityFusionInjector
                    ) world.getTileEntity(c.xCoord, c.yCoord + 1, c.zCoord);
                    te.setHasMultiBlock(true);
                }
            }
        }
    }

    @Override
    public void breakMultiBlock(World world, int x, int y, int z) {
        BlockArray blocks = new BlockArray();
        blocks.recursiveAddWithBounds(
            world, x, y, z, this, x - 8, y - 5, z - 8, x + 8, y + 5, z + 8
        );
        for (int i = 0; i < blocks.getSize(); i++) {
            Coordinate c = blocks.getNthBlock(i);
            int meta = c.getBlockMetadata(world);
            if (meta >= 8) {
                world.setBlockMetadataWithNotify(
                    c.xCoord, c.yCoord, c.zCoord, meta - 8, 3
                );
            }
            if (meta == 8) {
                if (ReactorTiles.getTE(world, c.xCoord, c.yCoord + 1, c.zCoord)
                    == ReactorTiles.INJECTOR) {
                    TileEntityFusionInjector te = (TileEntityFusionInjector
                    ) world.getTileEntity(c.xCoord, c.yCoord + 1, c.zCoord);
                    te.setHasMultiBlock(false);
                }
            }
        }
    }

    @Override
    protected String getIconBaseName() {
        return "injector";
    }

    @Override
    public int
    getTextureIndex(IBlockAccess world, int x, int y, int z, int side, int meta) {
        if (meta >= 8) {
            if (meta == 13)
                return 0;
            int index = 10 + this.getTextureIndex(world, x, y, z, side, meta - 8);
            if (side > 1 && (index == 19 || index == 23 || index == 24 || index == 25))
                index = 9;
            if (meta == 12 && side == 1) {
                boolean s = world.getBlock(x + 1, y, z) == this
                    && world.getBlockMetadata(x + 1, y, z) == 11;
                boolean s0 = world.getBlock(x - 1, y, z) == this
                    && world.getBlockMetadata(x - 1, y, z) == 11;
                boolean s1 = world.getBlock(x, y, z + 1) == this
                    && world.getBlockMetadata(x, y, z + 1) == 11;
                int a = 25;
                int b = 23;
                if (!s && !s0) {
                    a = 19;
                    b = 24;
                }
                index = (s || s1) ? a : b;
            }
            if (meta == 11 && side == 1)
                index = 9;
            return index;
        }
        switch (meta) {
            case 0:
                if (side == 0)
                    return 9;
                return side < 2 ? 0 : 4;
            case 1:
                if (side == 1)
                    return 9;
                if (side == 3 || side == 2) {
                    if (world.getBlock(x + 1, y, z) == this
                        && (world.getBlockMetadata(x + 1, y, z) & 7) == 0)
                        return side == 3 ? 5 : 6;
                    if (world.getBlock(x - 1, y, z) == this
                        && (world.getBlockMetadata(x - 1, y, z) & 7) == 0)
                        return side == 3 ? 6 : 5;
                }
                if (side == 4 || side == 5) {
                    if (world.getBlock(x, y, z + 1) == this
                        && (world.getBlockMetadata(x, y, z + 1) & 7) == 0)
                        return side == 4 ? 5 : 6;
                    if (world.getBlock(x, y, z - 1) == this
                        && (world.getBlockMetadata(x, y, z - 1) & 7) == 0)
                        return side == 4 ? 6 : 5;
                }
                return 9;
            case 2:
                ForgeDirection dir = dirs[side];
                int dx = x + dir.offsetX;
                int dy = y + dir.offsetY;
                int dz = z + dir.offsetZ;
                Block b = world.getBlock(dx, dy, dz);
                if (b == Blocks.air)
                    return 9;
                return b == ReactorTiles.MAGNETPIPE.getBlock() ? 0 : 9;
            case 3:
                if (side == 1)
                    return 9;
                return side < 2 ? 0 : 3;
            case 4:
                if (side == 1)
                    return 9;
                if (side == 3 || side == 2) {
                    if (world.getBlock(x + 1, y, z) == this
                        && (world.getBlockMetadata(x + 1, y, z) & 7) == 3)
                        return side == 3 ? 8 : 7;
                    if (world.getBlock(x - 1, y, z) == this
                        && (world.getBlockMetadata(x - 1, y, z) & 7) == 3)
                        return side == 3 ? 7 : 8;
                }
                if (side == 4 || side == 5) {
                    if (world.getBlock(x, y, z + 1) == this
                        && (world.getBlockMetadata(x, y, z + 1) & 7) == 3)
                        return side == 4 ? 8 : 7;
                    if (world.getBlock(x, y, z - 1) == this
                        && (world.getBlockMetadata(x, y, z - 1) & 7) == 3)
                        return side == 4 ? 7 : 8;
                }
                return 9;
            case 5:
                return 22;
            case 6:
                if (world.getBlock(x + 1, y, z) == this
                    && (world.getBlockMetadata(x + 1, y, z) & 7) == 5) {
                    if (side == 3)
                        return 2;
                    if (side == 2)
                        return 1;
                }
                if (world.getBlock(x - 1, y, z) == this
                    && (world.getBlockMetadata(x - 1, y, z) & 7) == 5) {
                    if (side == 3)
                        return 1;
                    if (side == 2)
                        return 2;
                }
                if (world.getBlock(x, y, z + 1) == this
                    && (world.getBlockMetadata(x, y, z + 1) & 7) == 5) {
                    if (side == 5)
                        return 1;
                    if (side == 4)
                        return 2;
                }
                if (world.getBlock(x, y, z - 1) == this
                    && (world.getBlockMetadata(x, y, z - 1) & 7) == 5) {
                    if (side == 4)
                        return 1;
                    if (side == 5)
                        return 2;
                }

                if (world.getBlock(x + 1, y, z) != this
                    && world.getBlock(x - 1, y, z) != this) {
                    if (ReactorTiles.getTE(world, x + 1, y, z)
                        == ReactorTiles.MAGNETPIPE) {
                        if (side == 3)
                            return 2;
                        if (side == 2)
                            return 1;
                        if (side == 5)
                            return 0;
                    }
                    if (ReactorTiles.getTE(world, x - 1, y, z)
                        == ReactorTiles.MAGNETPIPE) {
                        if (side == 3)
                            return 1;
                        if (side == 2)
                            return 2;
                        if (side == 4)
                            return 0;
                    }
                }
                if (world.getBlock(x, y, z + 1) != this
                    && world.getBlock(x, y, z - 1) != this) {
                    if (ReactorTiles.getTE(world, x, y, z + 1)
                        == ReactorTiles.MAGNETPIPE) {
                        if (side == 5)
                            return 1;
                        if (side == 4)
                            return 2;
                        if (side == 3)
                            return 0;
                    }
                    if (ReactorTiles.getTE(world, x, y, z - 1)
                        == ReactorTiles.MAGNETPIPE) {
                        if (side == 4)
                            return 1;
                        if (side == 5)
                            return 2;
                        if (side == 2)
                            return 0;
                    }
                }
                return 9;
            case 7:
                return 0;
            default:
                return 0;
        }
    }

    @Override
    public int getItemTextureIndex(int meta, int side) {
        meta = meta & 7;
        if (meta == 0) {
            return side == 0 ? 9 : side == 1 ? 0 : 4;
        }
        if (meta == 1) {
            return side == 1 ? 0 : side == 0 ? 9 : 28;
        }
        if (meta == 4) {
            return side == 0 ? 0 : side == 1 ? 9 : 27;
        }
        if (meta == 3) {
            return side == 1 ? 9 : side == 0 ? 0 : 3;
        }
        if (meta == 6) {
            return side < 2 ? 21 : 26;
        }
        if (meta == 2)
            return 9;
        if (meta == 7)
            return 0;
        if (meta == 5)
            return 22;
        if (meta == 5 || meta == 7 || meta == 3 || meta == 0 || meta == 2)
            return 22;
        return 21;
    }

    @Override
    public boolean canTriggerMultiBlockCheck(World world, int x, int y, int z, int meta) {
        return meta == 5;
    }

    @Override
    public int getNumberTextures() {
        return 29;
    }

    @Override
    protected TileEntity getTileEntityForPosition(World world, int x, int y, int z) {
        BlockArray blocks = new BlockArray();
        blocks.recursiveAddWithBounds(
            world, x, y, z, this, x - 8, y - 5, z - 8, x + 8, y + 5, z + 8
        );
        for (int i = 0; i < blocks.getSize(); i++) {
            Coordinate c = blocks.getNthBlock(i);
            if (ReactorTiles.getTE(world, c.xCoord, c.yCoord + 1, c.zCoord)
                == ReactorTiles.INJECTOR) {
                TileEntityFusionInjector te = (TileEntityFusionInjector
                ) world.getTileEntity(c.xCoord, c.yCoord + 1, c.zCoord);
                return te;
            }
        }
        return null;
    }

    @Override
    public boolean onNeutron(EntityNeutron e, World world, int x, int y, int z) {
        return false;
    }
}
