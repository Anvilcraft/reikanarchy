/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.Instantiable.Worldgen;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Random;

import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.Instantiable.Data.Immutable.BlockKey;
import Reika.DragonAPI.Libraries.ReikaAABBHelper;
import Reika.DragonAPI.Libraries.World.ReikaBlockHelper;
import Reika.DragonAPI.Libraries.World.ReikaWorldHelper;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageCreationHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import net.minecraft.world.gen.structure.StructureVillagePieces.Village;

public class VillageBuilding implements IVillageCreationHandler {
    public final Class buildingClass;
    public final float weight;
    private final Constructor<VillagePiece> constructor;

    public final int xSize;
    public final int ySize;
    public final int zSize;

    public VillageBuilding(
        Class<? extends VillagePiece> c, float w, String n, int x, int y, int z
    ) {
        buildingClass = c;
        weight = w;
        try {
            constructor = (Constructor<VillagePiece>) c.getConstructor(
                Start.class,
                int.class,
                Random.class,
                StructureBoundingBox.class,
                int.class,
                int.class,
                int.class,
                int.class
            );
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("You must specify an appropriate constructor!", e);
        }
        MapGenStructureIO.func_143031_a(buildingClass, n);
        xSize = x;
        ySize = y;
        zSize = z;
    }

    @Override
    public PieceWeight getVillagePieceWeight(Random random, int i) {
        int c = MathHelper.getRandomIntegerInRange(random, 0 + i, 1 + i);
        return new PieceWeight(buildingClass, (int) weight, c);
    }

    @Override
    public final Class<?> getComponentClass() {
        return buildingClass;
    }

    @Override
    public final Object buildComponent(
        StructureVillagePieces.PieceWeight villagePiece,
        StructureVillagePieces.Start startPiece,
        List pieces,
        Random random,
        int p1,
        int p2,
        int p3,
        int p4,
        int p5
    ) {
        return this.buildComponent(startPiece, pieces, random, p1, p2, p3, p4, p5);
    }

    private VillagePiece buildComponent(
        StructureVillagePieces.Start startPiece,
        List pieces,
        Random random,
        int par3,
        int par4,
        int par5,
        int par6,
        int par7
    ) {
        StructureBoundingBox var8 = StructureBoundingBox.getComponentToAddBoundingBox(
            par3, par4, par5, 0, 0, 0, xSize, ySize, zSize, par6
        );
        return (!this.canVillageGoDeeper(var8))
                || (StructureComponent.findIntersecting(pieces, var8) != null)
            ? null
            : this.createInstance(startPiece, par7, random, var8, par6);
    }

    private VillagePiece createInstance(
        Start start, int par7, Random random, StructureBoundingBox var8, int par6
    ) {
        try {
            return constructor.newInstance(
                start, par7, random, var8, par6, xSize, ySize, zSize
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean canVillageGoDeeper(StructureBoundingBox var8) {
        return var8 != null && var8.minY > 10;
    }

    public static class PerVillageBuilding extends VillageBuilding {
        public final int perVillage;

        public PerVillageBuilding(
            Class<? extends VillagePiece> c, float w, String n, int x, int y, int z, int p
        ) {
            super(c, w, n, x, y, z);
            perVillage = p;
        }

        @Override
        public PieceWeight getVillagePieceWeight(Random random, int i) {
            int c = MathHelper.getRandomIntegerInRange(random, 0 + i, 1 + i);
            return new PerVillageWeight(
                buildingClass, Math.max(1, Math.round(weight)), c, perVillage
            );
        }
    }

    /*
    public static List applyFractionalWeights(List<PieceWeight> li) {
        ArrayList<PieceWeight> ret = new ArrayList();
        boolean hasFractions = false;
        for (PieceWeight pw : li) {
            if (pw instanceof FractionalWeight) {
                hasFractions = true;
                break;
            }
        }
        if (!hasFractions)
            return li;
        int nDec = 0;
        for (PieceWeight pw : li) {
            int dec = pw instanceof FractionalWeight ?
    ReikaMathLibrary.countDecimalPlaces(((FractionalWeight)pw).decimalWeight) : 0; nDec =
    Math.max(nDec, dec);
        }
        int fac = ReikaMathLibrary.intpow2(10, nDec);
        for (PieceWeight pw : li) {
            try {
                ret.add(clonePieceWeightModified(pw, fac));
            }
            catch (Exception e) {
                throw new RuntimeException("Could not clone PieceWeight: "+pw.getClass()+"
    : "+pw.toString(), e);
            }
        }
        return ret;
    }

    private static PieceWeight clonePieceWeightModified(PieceWeight pw, int f) throws
    Exception { int w = (int)(f*(pw instanceof FractionalWeight ?
    ((FractionalWeight)pw).decimalWeight : pw.villagePieceWeight)); return
    PieceWeight.class.getConstructor(Class.class, int.class,
    int.class).newInstance(pw.villagePieceClass, w, pw.villagePiecesLimit);
    }

    private static final class FractionalWeight extends PieceWeight {

        public final float decimalWeight;

        public FractionalWeight(Class c, float weight, int limit) {
            super(c, MathHelper.ceiling_float_int(weight), limit);
            decimalWeight = weight;
        }

    }*/

    public static final class PerVillageWeight extends PieceWeight {
        private final int chancePerVillage;

        public PerVillageWeight(Class c, int weight, int limit, int ch) {
            super(c, weight, limit);
            chancePerVillage = ch;
        }

        public boolean canGenerate(MapGenVillage.Start s) {
            return this.hash(s) % chancePerVillage == 0;
        }

        private long hash(MapGenVillage.Start s) {
            return ChunkCoordIntPair.chunkXZ2Int(s.func_143019_e(), s.func_143018_f());
        }
    }

    public static abstract class VillagePiece extends Village {
        private int xSize;
        private int ySize;
        private int zSize;

        private int averageGroundLevel = -1;

        private StructureBoundingBox structureBox;

        private static final BlockKey BASIC_SUPPORT = new BlockKey(Blocks.cobblestone);

        protected StructureVillagePieces.Start villageCore;
        private long villageHash;

        public VillagePiece() {
            super();
        }

        protected VillagePiece(
            StructureVillagePieces.Start start,
            int par2,
            Random rand,
            StructureBoundingBox bb,
            int par5,
            int x,
            int y,
            int z
        ) {
            super(start, par2);
            coordBaseMode = par5;
            boundingBox = bb;
            villageCore = start;
            if (villageCore != null) {
                villageHash = ChunkCoordIntPair.chunkXZ2Int(
                    start.getBoundingBox().getCenterX(),
                    start.getBoundingBox().getCenterZ()
                );
            }

            xSize = x;
            ySize = y;
            zSize = z;
            this.initSizes();
        }

        //public abstract int getMinimumSeparation();

        @Override
        public final NBTTagCompound func_143010_b() {
            NBTTagCompound ret = super.func_143010_b();
            ret.setLong("vposhash", villageHash);
            return ret;
        }

        @Override
        public final void func_143009_a(World world, NBTTagCompound tag) {
            super.func_143009_a(world, tag);

            xSize = tag.getInteger("sizeX");
            ySize = tag.getInteger("sizeY");
            zSize = tag.getInteger("sizeZ");
            this.initSizes();
            villageHash = tag.getLong("vposhash");
        }

        private void initSizes() {
            StructureBoundingBox box = boundingBox;
            if (box == null)
                return;
            xSize = Math.max(xSize, box.maxX - box.minX + 1);
            ySize = Math.max(ySize, box.maxY - box.minY + 1);
            zSize = Math.max(zSize, box.maxZ - box.minZ + 1);
        }

        @Override
        public final boolean
        addComponentParts(World world, Random rand, StructureBoundingBox box) {
            structureBox = box;
            this.initSizes();
            if (averageGroundLevel < 0) {
                averageGroundLevel = this.getAverageGroundLevel(world, box);

                if (averageGroundLevel < 0) {
                    return true;
                }

                boundingBox.offset(
                    0, averageGroundLevel - boundingBox.maxY + ySize - 1, 0
                );
            }

            /*
            int mind = this.getMinimumSeparation();
            if (mind > 0 && world instanceof WorldServer) {
                ChunkProviderGenerate gen =
            (ChunkProviderGenerate)((WorldServer)world).theChunkProviderServer.currentChunkProvider;
                MapGenVillage mgv = null;
                for (StructureStart s :
            ((Collection<StructureStart>)mgv.structureMap.values())) { if
            (s.isSizeableStructure()) { StructureBoundingBox box = s.getBoundingBox(); if
            (ReikaMathLibrary.py3d(dx, dy, dz))
                    }
                }
            }*/

            return this.generate(world, rand);
        }

        @Override
        public final int hashCode() {
            return structureBox.func_151535_h().hashCode() ^ this.getClass().hashCode();
        }

        @Override
        public boolean equals(Object o) {
            return o.getClass() == this.getClass()
                && ((VillagePiece) o)
                       .structureBox.func_151535_h()
                       .equals(structureBox.func_151535_h());
        }

        protected final void clearDroppedItems(World world) {
            AxisAlignedBB box
                = ReikaAABBHelper.structureToAABB(boundingBox).expand(2, 2, 2);
            List<EntityItem> li = world.getEntitiesWithinAABB(EntityItem.class, box);
            for (EntityItem ei : li) {
                ei.setDead();
            }
        }

        protected abstract boolean generate(World world, Random rand);

        protected final void
        placeBlockAtCurrentPosition(World world, int i, int j, int k, Block b) {
            this.placeBlockAtCurrentPosition(world, i, j, k, b, 0);
        }

        protected final void
        placeBlockAtCurrentPosition(World world, int i, int j, int k, Block b, int meta) {
            this.placeBlockAtCurrentPosition(world, i, j, k, b, meta, BASIC_SUPPORT);
        }

        protected final void placeBlockAtCurrentPosition(
            World world, int i, int j, int k, Block b, int meta, BlockKey support
        ) {
            //this.placeBlockAtCurrentPosition(world, b, meta, i, j, k, structureBox);

            int i1 = this.getXWithOffset(i, k);
            int j1 = this.getYWithOffset(j);
            int k1 = this.getZWithOffset(i, k);

            if (b == Blocks.wall_sign)
                meta = ReikaBlockHelper.getSignMetadataToConnectToWall(
                    world, i1, j1, k1, meta
                );

            //if (structureBox.isVecInside(i1, j1, k1))  {
            this.tryPlaceBlock(world, i1, j1, k1, b, meta, 3, support);
            //}
        }

        private void
        tryPlaceBlock(World world, int x, int y, int z, Block b, int meta, int flags) {
            this.tryPlaceBlock(world, x, y, z, b, meta, flags, BASIC_SUPPORT);
        }

        private void tryPlaceBlock(
            World world,
            int x,
            int y,
            int z,
            Block b,
            int meta,
            int flags,
            BlockKey support
        ) {
            Block b2 = world.getBlock(x, y, z);
            int meta2 = world.getBlockMetadata(x, y, z);
            if (b2 != b || meta2 != meta)
                world.setBlock(x, y, z, b, meta, flags);

            if (b.getMaterial().isSolid() && support != null && y == boundingBox.minY) {
                int dy = y - 1;
                while (dy > 0 && ReikaWorldHelper.softBlocks(world, x, dy, z)
                       && world.getBlock(x, dy, z) != b) {
                    world.setBlock(x, dy, z, support.blockID, support.metadata, 3);
                    dy--;
                }
            }
        }

        protected final void
        placeBlockAtFixedPosition(World world, int i, int j, int k, Block b) {
            this.placeBlockAtFixedPosition(world, i, j, k, b, 0);
        }

        protected final Block getBlockAtFixedPosition(World world, int i, int j, int k) {
            return world.getBlock(
                i + boundingBox.minX, j + boundingBox.minY, k + boundingBox.minZ
            );
        }

        protected final int getMetaAtFixedPosition(World world, int i, int j, int k) {
            return world.getBlockMetadata(
                i + boundingBox.minX, j + boundingBox.minY, k + boundingBox.minZ
            );
        }

        protected final void
        placeBlockAtFixedPosition(World world, int i, int j, int k, Block b, int meta) {
            this.placeBlockAtFixedPosition(world, i, j, k, b, meta, BASIC_SUPPORT);
        }

        protected final void placeBlockAtFixedPosition(
            World world, int i, int j, int k, Block b, int meta, BlockKey support
        ) {
            this.tryPlaceBlock(
                world,
                i + boundingBox.minX,
                j + boundingBox.minY,
                k + boundingBox.minZ,
                b,
                meta,
                3,
                support
            );
        }

        protected final void
        placeBlockAtFixedPosition(World world, int i, int j, int k, BlockKey bk) {
            this.placeBlockAtFixedPosition(
                world, i, j, k, bk.blockID, Math.max(0, bk.metadata)
            );
        }

        protected final TileEntity placeTileEntityAtFixedPosition(
            World world, int i, int j, int k, Block b, int meta
        ) {
            this.tryPlaceBlock(
                world,
                i + boundingBox.minX,
                j + boundingBox.minY,
                k + boundingBox.minZ,
                b,
                meta,
                3
            );
            return world.getTileEntity(
                i + boundingBox.minX, j + boundingBox.minY, k + boundingBox.minZ
            );
        }

        protected final TileEntity
        generateTileEntity(World world, int i, int j, int k, Block b, int meta) {
            this.placeBlockAtCurrentPosition(world, i, j, k, b, meta);
            int x = this.getXWithOffset(i, k);
            int y = this.getYWithOffset(j);
            int z = this.getZWithOffset(i, k);
            return world.getTileEntity(x, y, z);
        }

        protected void clearVolume(World world) {
            this.clearVolume(world, 0, 0, 0, xSize, ySize, zSize);
        }

        protected void
        clearVolume(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
            for (int i = x1; i < x2; i++) {
                for (int k = z1; k < z2; k++) {
                    for (int j = y1; j < y2; j++) {
                        this.placeBlockAtFixedPosition(world, i, j, k, Blocks.air);
                    }
                }
            }
        }

        public int countIntersectingBlocks(World world) {
            int c = 0;
            for (int i = 0; i < xSize; i++) {
                for (int k = 0; k < zSize; k++) {
                    for (int j = 0; j < ySize; j++) {
                        Block b = this.getBlockAtFixedPosition(world, i, j, k);
                        if (!ReikaWorldHelper.softBlocks(b))
                            c++;
                    }
                }
            }
            return c;
        }

        public int getVolume() {
            return xSize * ySize * zSize;
        }

        public void rise(World world) {
            boolean flag = false;
            if (xSize <= 0 || zSize <= 0) {
                DragonAPICore.logError("Tried to raise a structure that had a zero size!"
                );
                Thread.dumpStack();
                return;
            }
            do {
                int c = 0;
                int t = 0;
                for (int i = 0; i < xSize; i++) {
                    for (int k = 0; k < zSize; k++) {
                        Block b = this.getBlockAtFixedPosition(world, i, 1, k);
                        if (b == Blocks.stone || b == Blocks.dirt || b == Blocks.grass
                            || b == Blocks.gravel || b == Blocks.sand
                            || b == Blocks.sandstone || b == Blocks.hardened_clay
                            || b == Blocks.stained_hardened_clay
                            || ReikaBlockHelper.isLiquid(b)) {
                            c++;
                        }
                        t++;
                    }
                }
                flag = c >= t / 4;
                if (boundingBox.maxY >= 255)
                    flag = false;
                if (flag) {
                    boundingBox.offset(0, 1, 0);
                }
            } while (flag);
        }
    }

    public static class StructureEntry {
        public final Class structureClass;
        public final String identifier;
        public final float weight;

        public final int xSize;
        public final int ySize;
        public final int zSize;

        public StructureEntry(
            Class<? extends VillagePiece> c, float w, String s, int x, int y, int z
        ) {
            structureClass = c;
            weight = w;
            identifier = s;
            xSize = x;
            ySize = y;
            zSize = z;
        }

        public VillageBuilding build() {
            return new VillageBuilding(
                structureClass, weight, identifier, xSize, ySize, zSize
            );
        }
    }

    public static class PerVillageStructureEntry extends StructureEntry {
        public final int perVillageChance;

        /** Higher 'chance' = less spawning; think <b>rand.nextInt()<b> */
        public PerVillageStructureEntry(
            Class<? extends VillagePiece> c, float w, String s, int x, int y, int z, int p
        ) {
            super(c, w, s, x, y, z);
            perVillageChance = p;
        }

        @Override
        public VillageBuilding build() {
            return new PerVillageBuilding(
                structureClass, weight, identifier, xSize, ySize, zSize, perVillageChance
            );
        }
    }
}
