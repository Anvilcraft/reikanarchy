/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Block.Dimension.Structure.PistonTape;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import Reika.ChromatiCraft.Auxiliary.Interfaces.LaserPulseEffect;
import Reika.ChromatiCraft.Base.BlockDimensionStructureTile;
import Reika.ChromatiCraft.Base.DimensionStructureGenerator.DimensionStructureType;
import Reika.ChromatiCraft.Base.TileEntity.StructureBlockTile;
import Reika.ChromatiCraft.Block.BlockChromaDoor;
import Reika.ChromatiCraft.Entity.EntityLaserPulse;
import Reika.ChromatiCraft.Entity.EntityPistonSpline;
import Reika.ChromatiCraft.Registry.ChromaISBRH;
import Reika.ChromatiCraft.Registry.ChromaSounds;
import Reika.ChromatiCraft.Render.Particle.EntityCCBlurFX;
import Reika.ChromatiCraft.World.Dimension.Structure.PistonTape.TapeStage;
import Reika.ChromatiCraft.World.Dimension.Structure.PistonTapeGenerator;
import Reika.DragonAPI.ASM.APIStripper.Strippable;
import Reika.DragonAPI.ASM.DependentMethodStripper.ModDependent;
import Reika.DragonAPI.Instantiable.Data.Immutable.Coordinate;
import Reika.DragonAPI.Instantiable.Data.Immutable.DecimalPosition;
import Reika.DragonAPI.Instantiable.Effects.EntityBlurFX;
import Reika.DragonAPI.Instantiable.Math.Spline;
import Reika.DragonAPI.Instantiable.Math.Spline.BasicSplinePoint;
import Reika.DragonAPI.Instantiable.Math.Spline.SplineType;
import Reika.DragonAPI.Instantiable.RGBColorData;
import Reika.DragonAPI.Libraries.Java.ReikaRandomHelper;
import Reika.DragonAPI.Libraries.ReikaDirectionHelper;
import Reika.DragonAPI.ModList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

@Strippable(value = { "mcp.mobius.waila.api.IWailaDataProvider" })
public class BlockPistonTarget
    extends BlockDimensionStructureTile implements LaserPulseEffect, IWailaDataProvider {
    private IIcon emitterOverlay;
    private IIcon doorOverlay;

    public BlockPistonTarget(Material mat) {
        super(mat);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        switch (meta) {
            case 0:
                return new PistonEmitterTile();
            case 1:
                return new PistonDoorTile();
            default:
                return null;
        }
    }

    @Override
    public IIcon getIcon(int s, int meta) {
        return blockIcon;
    }

    @Override
    public void registerBlockIcons(IIconRegister ico) {
        blockIcon = ico.registerIcon("chromaticraft:dimstruct/piston_target");
        doorOverlay = ico.registerIcon("chromaticraft:dimstruct/piston_emitter_overlay");
        emitterOverlay
            = ico.registerIcon("chromaticraft:dimstruct/piston_target_overlay");
    }

    @Override
    public boolean onRightClicked(
        World world,
        int x,
        int y,
        int z,
        EntityPlayer ep,
        int s,
        float a,
        float b,
        float c
    ) {
        return true;
    }

    public boolean onImpact(World world, int x, int y, int z, EntityLaserPulse e) {
        if (world.getBlockMetadata(x, y, z) == 0) {
            PistonEmitterTile te = (PistonEmitterTile) world.getTileEntity(x, y, z);
            if (te.getFacing()
                == e.direction.getCardinal() /* && te.color.matchColor(e.color)*/) {
                te.fire(e.color);
            }
        }
        return true;
    }

    public boolean
    receiveSplineParticle(World world, int x, int y, int z, EntityPistonSpline e) {
        if (world.getBlockMetadata(x, y, z) == 1) {
            PistonDoorTile te = (PistonDoorTile) world.getTileEntity(x, y, z);
            if (te.color.matchColor(e.color)) {
                te.receive();
            } else {
                ChromaSounds.ERROR.playSoundAtBlock(te);
            }
        }
        return true;
    }

    public IIcon getOverlay(IBlockAccess world, int x, int y, int z, int meta) {
        return meta == 0 ? emitterOverlay : doorOverlay;
    }

    @Override
    public int getRenderType() {
        return ChromaISBRH.piston.getRenderID();
    }

    /*
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
     */
    @Override
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public boolean canRenderInPass(int pass) {
        ChromaISBRH.piston.setRenderPass(pass);
        return pass <= 1;
    }

    @Override
    @ModDependent(ModList.WAILA)
    public ItemStack getWailaStack(IWailaDataAccessor acc, IWailaConfigHandler config) {
        return null;
    }

    @Override
    @ModDependent(ModList.WAILA)
    public final List<String> getWailaHead(
        ItemStack is, List<String> tip, IWailaDataAccessor acc, IWailaConfigHandler config
    ) {
        return tip;
    }

    @Override
    @ModDependent(ModList.WAILA)
    public final List<String> getWailaBody(
        ItemStack is, List<String> tip, IWailaDataAccessor acc, IWailaConfigHandler config
    ) {
        TileEntity te = acc.getTileEntity();
        if (te instanceof PistonDoorTile) {
            tip.add("Color: " + ((PistonDoorTile) te).color.getName());
        }
        return tip;
    }

    @ModDependent(ModList.WAILA)
    public final List<String> getWailaTail(
        ItemStack itemStack,
        List<String> currenttip,
        IWailaDataAccessor acc,
        IWailaConfigHandler config
    ) {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(
        EntityPlayerMP player,
        TileEntity te,
        NBTTagCompound tag,
        World world,
        int x,
        int y,
        int z
    ) {
        return tag;
    }

    public static class PistonDoorTile extends PistonTargetTile {
        private static final int DURATION = 50;

        private Coordinate door = new Coordinate(0, 0, 0);
        private int active;
        protected RGBColorData color = RGBColorData.white();

        private void receive() {
            if (this.getFacing() == null)
                return;
            if (worldObj.isRemote) {
                this.receptionParticles();
            } else {
                this.setActive(true);
                this.getStage().validate(this.getDoor());
                ChromaSounds.CAST.playSoundAtBlock(this);
            }
        }

        @SideOnly(Side.CLIENT)
        private void receptionParticles() {
            Random rand = new Random();
            for (int i = 0; i < 16; i++) {
                double x = xCoord + rand.nextDouble();
                double y = yCoord + rand.nextDouble();
                double z = zCoord + rand.nextDouble();
                switch (this.getFacing()) {
                    case DOWN:
                        y = yCoord;
                        break;
                    case UP:
                        y = yCoord + 1;
                        break;
                    case WEST:
                        x = xCoord;
                        break;
                    case EAST:
                        x = xCoord + 1;
                        break;
                    case NORTH:
                        z = zCoord;
                        break;
                    case SOUTH:
                        z = zCoord + 1;
                        break;
                    default:
                        break;
                }
                float s = (float) ReikaRandomHelper.getRandomBetween(0.8, 1.5);
                int l = ReikaRandomHelper.getRandomBetween(10, 40);
                EntityBlurFX fx = new EntityCCBlurFX(worldObj, x, y, z);
                fx.setColor(color.getRenderColor()).setScale(s).setLife(l);
                Minecraft.getMinecraft().effectRenderer.addEffect(fx);
            }
        }

        private void setActive(boolean active) {
            this.active = active ? DURATION : 0;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

            this.checkDoor();
        }

        private void checkDoor() {
            boolean open = true;
            for (Coordinate c : this.getAllTiles()) {
                PistonDoorTile te = (PistonDoorTile) c.getTileEntity(worldObj);
                if (!te.isActive()) {
                    open = false;
                    break;
                }
            }
            BlockChromaDoor.setOpen(
                worldObj, door.xCoord, door.yCoord, door.zCoord, open
            );
        }

        private Collection<Coordinate> getAllTiles() {
            ArrayList<Coordinate> li = new ArrayList();
            ForgeDirection left = ReikaDirectionHelper.getLeftBy90(this.getFacing());
            Coordinate root = new Coordinate(
                xCoord - left.offsetX * this.getSubIndex(),
                yCoord,
                zCoord - left.offsetZ * this.getSubIndex()
            );
            for (int i = 0; i < this.getDoorBusWidth(); i++) {
                li.add(root.offset(left, i));
            }
            return li;
        }

        public boolean isActive() {
            return active > 0;
        }

        @Override
        public boolean canUpdate() {
            return true;
        }

        @Override
        public void updateEntity() {
            if (active > 0) {
                active--;
                if (active == 0)
                    this.setActive(false);
            }
        }

        @Override
        public void writeToNBT(NBTTagCompound NBT) {
            super.writeToNBT(NBT);

            color.writeToNBT(NBT);
            door.writeToNBT("doorLoc", NBT);
        }

        @Override
        public void readFromNBT(NBTTagCompound NBT) {
            super.readFromNBT(NBT);

            color.readFromNBT(NBT);
            door = Coordinate.readFromNBT("doorLoc", NBT);
        }

        public void setTarget(Coordinate c) {
            door = c;
        }

        public void setColor(RGBColorData c) {
            color = c;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }

        @Override
        public int getRenderColor() {
            return color.getRenderColor();
        }
    }

    public static class PistonEmitterTile extends PistonTargetTile {
        private int doorIndex;
        private Coordinate target = new Coordinate(0, 0, 0);
        private Spline path;

        @Override
        public void writeToNBT(NBTTagCompound NBT) {
            super.writeToNBT(NBT);

            target.writeToNBT("target", NBT);
            NBT.setInteger("door", doorIndex);
        }

        @Override
        public void readFromNBT(NBTTagCompound NBT) {
            super.readFromNBT(NBT);

            target = Coordinate.readFromNBT("target", NBT);
            doorIndex = NBT.getInteger("door");
        }

        private void fire(RGBColorData clr) {
            if (path == null) {
                ChromaSounds.ERROR.playSoundAtBlock(this);
                return;
            }
            EntityPistonSpline e = new EntityPistonSpline(worldObj, path, clr);
            if (!worldObj.isRemote) {
                worldObj.spawnEntityInWorld(e);
            }
        }

        public void setTarget(
            int door, Coordinate tgt, ForgeDirection hallDir, ArrayList<Coordinate> hall
        ) {
            ForgeDirection dir = ReikaDirectionHelper.getLeftBy90(hallDir);
            Random rand = new Random(new Coordinate(this).hashCode());
            rand.nextBoolean();
            doorIndex = door;
            target = tgt;
            path = new Spline(SplineType.CHORDAL);
            DecimalPosition p1 = new DecimalPosition(this);
            DecimalPosition p2 = new DecimalPosition(tgt);
            path.addPoint(new BasicSplinePoint(p1));
            for (Coordinate c : hall) {
                DecimalPosition p = DecimalPosition.getRandomWithin(c, rand);
                path.addPoint(new BasicSplinePoint(p));
            }
            Coordinate last = hall.get(hall.size() - 1);
            int dl = 3 + this.getDoorBusWidth();
            int dx = last.xCoord + dl * hallDir.offsetX;
            int dz = last.zCoord + dl * hallDir.offsetZ;
            int tx = tgt.xCoord + dir.offsetX * 3;
            int tz = tgt.zCoord + dir.offsetZ * 3;
            int diff = hallDir.offsetX * (tx - dx) + hallDir.offsetZ * (tz - dz);
            while (diff >= 0) {
                double rx = dx + 0.5 - 1.25 + rand.nextDouble() * 2.5;
                double rz = dz + 0.5 - 1.25 + rand.nextDouble() * 2.5;
                double ry = tgt.yCoord + 0.5 - 1.25 + rand.nextDouble() * 2.5;
                DecimalPosition p = new DecimalPosition(rx, ry, rz);
                path.addPoint(new BasicSplinePoint(p));
                dx += hallDir.offsetX * dl;
                dz += hallDir.offsetZ * dl;
                diff = hallDir.offsetX * (tx - dx) + hallDir.offsetZ * (tz - dz);
            }
            /*
            int n = 3+doorIndex;
            for (int i = 0; i < n; i++) {
                double f = (i+1D)/(n+1D);
                DecimalPosition p0 = DecimalPosition.interpolate(p1, p2, f);
                double dx = ReikaRandomHelper.getRandomPlusMinus(0, 2D);
                double dy = ReikaRandomHelper.getRandomPlusMinus(0, 1.5D);
                double dz = ReikaRandomHelper.getRandomPlusMinus(0, 2D);
                DecimalPosition p = p0.offset(dx, dy, dz);
                while (!p.getCoordinate().isEmpty(worldObj)) {
                    dx = ReikaRandomHelper.getRandomPlusMinus(0, 2D);
                    dy = ReikaRandomHelper.getRandomPlusMinus(0, 1.5D);
                    dz = ReikaRandomHelper.getRandomPlusMinus(0, 2D);
                    p = p0.offset(dx, dy, dz);
                }
                path.addPoint(new BasicSplinePoint(p));
            }
             */
            path.addPoint(new BasicSplinePoint(p2));

            /*
            List<DecimalPosition> li = path.get(32, false);
            for (DecimalPosition p : li) {
                Coordinate c = p.getCoordinate();
                c.setBlock(worldObj, Blocks.brick_block);
            }
             */
        }
    }

    public static abstract class PistonTargetTile
        extends StructureBlockTile<PistonTapeGenerator> {
        private int doorBusWidth;
        private int stageIndex;
        private int doorIndex;
        private int doorColorIndex;
        private ForgeDirection facing;

        @Override
        public void writeToNBT(NBTTagCompound NBT) {
            super.writeToNBT(NBT);

            NBT.setInteger("index", doorColorIndex);
            NBT.setInteger("stage", stageIndex);
            NBT.setInteger("door", doorIndex);
            NBT.setInteger("bus", doorBusWidth);
            if (facing != null)
                NBT.setInteger("facing", facing.ordinal());
        }

        @Override
        public void readFromNBT(NBTTagCompound NBT) {
            super.readFromNBT(NBT);

            doorColorIndex = NBT.getInteger("index");
            stageIndex = NBT.getInteger("stage");
            doorIndex = NBT.getInteger("door");
            doorBusWidth = NBT.getInteger("bus");
            if (NBT.hasKey("facing"))
                facing = ForgeDirection.VALID_DIRECTIONS[NBT.getInteger("facing")];
        }

        public ForgeDirection getFacing() {
            return facing;
        }

        public int getSubIndex() {
            return doorColorIndex;
        }

        public int getDoorBusWidth() {
            return doorBusWidth;
        }

        public final void
        setData(ForgeDirection dir, int stage, int door, int idx, int w) {
            doorColorIndex = idx;
            stageIndex = stage;
            doorIndex = door;
            facing = dir;
            doorBusWidth = w;
        }

        @Override
        public DimensionStructureType getType() {
            return DimensionStructureType.PISTONTAPE;
        }

        public int getRenderColor() {
            return 0xffffff;
        }

        public final TapeStage getStage() {
            return this.getGenerator().getStage(stageIndex);
        }

        public final int getDoor() {
            return doorIndex;
        }
    }
}
