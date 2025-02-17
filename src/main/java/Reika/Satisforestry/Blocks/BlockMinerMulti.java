package Reika.Satisforestry.Blocks;

import java.util.Locale;

import Reika.DragonAPI.ASM.DependentMethodStripper.ModDependent;
import Reika.DragonAPI.Instantiable.Data.BlockStruct.FilledBlockArray.BlockMatchFailCallback;
import Reika.DragonAPI.Libraries.Registry.ReikaItemHelper;
import Reika.DragonAPI.Libraries.ReikaInventoryHelper;
import Reika.DragonAPI.ModInteract.ItemHandlers.BCPipeHandler;
import Reika.DragonAPI.ModInteract.ItemHandlers.ThermalDuctHandler;
import Reika.DragonAPI.ModList;
import Reika.Satisforestry.Miner.MinerStructure;
import Reika.Satisforestry.Miner.TileNodeHarvester;
import Reika.Satisforestry.Registry.SFBlocks;
import buildcraft.transport.Pipe;
import buildcraft.transport.TileGenericPipe;
import buildcraft.transport.pipes.PipeItemsWood;
import cofh.api.transport.IItemDuct;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockMinerMulti extends BlockSFMultiBase<ForgeDirection> {
    public static enum MinerBlocks {
        ORANGE,
        DARK,
        SILVER,
        GRAY,
        DRILL,
        CONVEYOR,
        HUB,
        POWER,
        ;

        public static final MinerBlocks[] list = values();
    }

    public BlockMinerMulti(Material mat) {
        super(mat);
    }

    @Override
    public boolean hasTileEntity(int meta) {
        if (meta < 8)
            return false;
        MinerBlocks mb = MinerBlocks.list[meta & 7];
        return mb == MinerBlocks.HUB || mb == MinerBlocks.POWER
            || mb == MinerBlocks.CONVEYOR;
    }

    @Override
    protected boolean shouldTurnToSlab(int meta) {
        return MinerBlocks.list[meta & 7] == MinerBlocks.DARK;
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        if (meta < 8)
            return null;
        switch (MinerBlocks.list[meta & 7]) {
            case HUB:
                return new TileShaftConnection();
            case POWER:
                return new TilePowerConnection();
            case CONVEYOR:
                return new TileMinerConveyorPort();
            default:
                return null;
        }
    }

    @Override
    protected String getIconFolderName() {
        return "miner";
    }

    @Override
    protected String getLocaleKeyName() {
        return "sfminer";
    }

    @Override
    public ForgeDirection checkForFullMultiBlock(
        World world,
        int x,
        int y,
        int z,
        ForgeDirection placeDir,
        BlockMatchFailCallback call
    ) {
        TileEntity te = this.getTileEntityForPosition(world, x, y, z);
        if (te instanceof TileNodeHarvester) {
            return MinerStructure.getStructureDirection(
                world, te.xCoord, te.yCoord, te.zCoord, call
            );
        }
        return null;
    }

    @Override
    public int getNumberVariants() {
        return MinerBlocks.list.length;
    }

    public static class TileMinerConveyorPort
        extends TileMinerConnection<TileNodeHarvester> implements ISidedInventory {
        private ItemStack currentSlot;

        @Override
        public boolean canUpdate() {
            return true;
        }

        @Override
        public void updateEntity() {
            if (currentSlot == null || currentSlot.stackSize <= 0)
                return;
            TileNodeHarvester root = this.getRoot();
            if (root == null || !root.hasStructure())
                return;
            ForgeDirection dir = root.getDirection();
            if (dir == null)
                return;
            TileEntity te = worldObj.getTileEntity(
                xCoord + dir.offsetX, yCoord, zCoord + dir.offsetZ
            );
            if (te instanceof IInventory && this.canAutoEject(te))
                this.tryAddToInv((IInventory) te);
            else if (ModList.BCTRANSPORT.isLoaded() && worldObj.getBlock(xCoord+dir.offsetX, yCoord, zCoord+dir.offsetZ) == BCPipeHandler.getInstance().pipeID)
                this.handlePipeTransport(dir, te);
            else if (ModList.THERMALDYNAMICS.isLoaded() && ThermalDuctHandler.getInstance().isDuct(worldObj.getBlock(xCoord+dir.offsetX, yCoord, zCoord+dir.offsetZ)))
                this.handleTDDuctTransport(dir, te);
        }

        private boolean canAutoEject(TileEntity te) {
            String n = te.getClass().getName().toLowerCase(Locale.ENGLISH);
            if (n.equals(
                    "blusunrize.immersiveengineering.common.blocks.metal.TileEntityConveyorBelt"
                ))
                return true;
            return false;
        }

        @ModDependent(ModList.BCTRANSPORT)
        private void handlePipeTransport(ForgeDirection dir, TileEntity te) {
            Pipe p = ((TileGenericPipe) te).pipe;
            if (p instanceof PipeItemsWood)
                ((PipeItemsWood) p).receiveEnergy(dir.getOpposite(), 100, false);
        }

        @ModDependent(ModList.THERMALDYNAMICS)
        private void handleTDDuctTransport(ForgeDirection dir, TileEntity te) {
            if (te instanceof IItemDuct)
                currentSlot = ((IItemDuct) te).insertItem(dir, currentSlot);
        }

        private void tryAddToInv(IInventory te) {
            ItemStack is = ReikaItemHelper.getSizedItemStack(currentSlot, 1);
            if (ReikaInventoryHelper.addToIInv(is, te)) {
                currentSlot.stackSize--;
                if (currentSlot.stackSize <= 0)
                    currentSlot = null;
            }
        }

        @Override
        public int getSizeInventory() {
            return 1;
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return currentSlot;
        }

        @Override
        public ItemStack decrStackSize(int slot, int decr) {
            return ReikaInventoryHelper.decrStackSize(this, slot, decr);
        }

        @Override
        public ItemStack getStackInSlotOnClosing(int slot) {
            return ReikaInventoryHelper.getStackInSlotOnClosing(this, slot);
        }

        @Override
        public void setInventorySlotContents(int slot, ItemStack is) {
            currentSlot = is;
        }

        @Override
        public String getInventoryName() {
            return this.getName();
        }

        @Override
        public boolean hasCustomInventoryName() {
            return false;
        }

        @Override
        public int getInventoryStackLimit() {
            return 64;
        }

        @Override
        public boolean isUseableByPlayer(EntityPlayer ep) {
            return false;
        }

        @Override
        public void openInventory() {}

        @Override
        public void closeInventory() {}

        @Override
        public boolean isItemValidForSlot(int slot, ItemStack is) {
            return false;
        }

        @Override
        public int[] getAccessibleSlotsFromSide(int side) {
            return new int[] { 0 };
        }

        @Override
        public boolean canInsertItem(int slot, ItemStack is, int side) {
            return false;
        }

        @Override
        public boolean canExtractItem(int slot, ItemStack is, int side) {
            return this.getRoot() != null
                && ForgeDirection.VALID_DIRECTIONS[side] == this.getRoot().getDirection();
        }

        @Override
        public void writeToNBT(NBTTagCompound NBT) {
            super.writeToNBT(NBT);

            NBTTagCompound tag = new NBTTagCompound();
            if (currentSlot != null)
                currentSlot.writeToNBT(tag);
            NBT.setTag("contents", tag);
        }

        @Override
        public void readFromNBT(NBTTagCompound NBT) {
            super.readFromNBT(NBT);

            NBTTagCompound tag = NBT.getCompoundTag("contents");
            currentSlot = ItemStack.loadItemStackFromNBT(tag);
        }
    }

    @Override
    protected SFBlocks getTileBlockType() {
        return SFBlocks.HARVESTER;
    }

    @Override
    protected int getBlockSearchXZ() {
        return 12;
    }

    @Override
    protected int getBlockSearchY() {
        return 16;
    }
}
