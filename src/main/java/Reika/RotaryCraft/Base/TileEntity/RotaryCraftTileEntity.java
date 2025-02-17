/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.Base.TileEntity;

import Reika.DragonAPI.ASM.DependentMethodStripper.ModDependent;
import Reika.DragonAPI.Base.TileEntityRegistryBase;
import Reika.DragonAPI.Instantiable.StepTimer;
import Reika.DragonAPI.Interfaces.TextureFetcher;
import Reika.DragonAPI.Interfaces.TileEntity.RenderFetcher;
import Reika.DragonAPI.Libraries.IO.ReikaChatHelper;
import Reika.DragonAPI.Libraries.MathSci.ReikaEngLibrary;
import Reika.DragonAPI.Libraries.MathSci.ReikaThermoHelper;
import Reika.DragonAPI.Libraries.World.ReikaBlockHelper;
import Reika.DragonAPI.ModList;
import Reika.RotaryCraft.API.Interfaces.BasicMachine;
import Reika.RotaryCraft.Auxiliary.RotaryRenderList;
import Reika.RotaryCraft.Base.RotaryModelBase;
import Reika.RotaryCraft.Base.RotaryTERenderer;
import Reika.RotaryCraft.Registry.BlockRegistry;
import Reika.RotaryCraft.Registry.ConfigRegistry;
import Reika.RotaryCraft.Registry.MachineRegistry;
import Reika.RotaryCraft.RotaryCraft;
import Reika.RotaryCraft.TileEntities.Transmission.TileEntityBeltHub;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import li.cil.oc.api.network.Visibility;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class RotaryCraftTileEntity
    extends TileEntityRegistryBase<MachineRegistry>
    implements RenderFetcher, BasicMachine {
    protected RotaryModelBase rmb;
    protected int tickcount = 0;
    /** Rotational position. */
    public float phi = 0;

    /** For emp */
    private boolean disabled;

    public int[] paint = { -1, -1, -1 };

    protected StepTimer second = new StepTimer(20);

    public boolean isFlipped = false;

    @Override
    public final boolean canUpdate() {
        return !RotaryCraft.instance.isLocked();
    }

    @Override
    public final boolean allowTickAcceleration() {
        return this.getTile().allowsAcceleration();
    }

    public int getTick() {
        return tickcount;
    }

    @Override
    protected abstract void animateWithTick(World world, int x, int y, int z);

    @Override
    public abstract MachineRegistry getTile();

    @Override
    @SideOnly(Side.CLIENT)
    public final TextureFetcher getRenderer() {
        return this.getTileRenderer();
    }

    @SideOnly(Side.CLIENT)
    public final RotaryTERenderer getTileRenderer() {
        if (this.getTile().hasRender())
            return RotaryRenderList.getRenderForMachine(this.getTile());
        else
            return null;
    }

    public final String getMultiValuedName() {
        if (this.getTile().isMultiNamed())
            return this.getTile().getMultiName(this);
        return this.getTile().getName();
    }

    //public abstract int getMachineIndex();

    public final MachineRegistry getMachine(ForgeDirection dir) {
        int x = xCoord + dir.offsetX;
        int y = yCoord + dir.offsetY;
        int z = zCoord + dir.offsetZ;
        TileEntity te = this.getAdjacentTileEntity(dir);
        return te instanceof RotaryCraftTileEntity
            ? ((RotaryCraftTileEntity) te).getTile()
            : null;
        //return MachineRegistry.getMachine(worldObj, x, y, z);
    }

    public final void giveNoSuperWarning() {
        RotaryCraft.logger.logError(
            "TileEntity " + this.getName() + " does not call super()!"
        );
        ReikaChatHelper.write("TileEntity " + this.getName() + " does not call super()!");
    }

    @Override
    public final boolean shouldRenderInPass(int pass) {
        if (!this.isInWorld())
            return true;
        if (pass == 0)
            return true;
        if (this.getTile().hasModel() && this instanceof TileEntityIOMachine)
            return true;
        if (pass == 1 && (this.hasModelTransparency() || this.getTile().renderInPass1()))
            return true;
        return false;
    }

    public abstract boolean hasModelTransparency();

    @Override
    protected void writeSyncTag(NBTTagCompound NBT) {
        super.writeSyncTag(NBT);

        if (isFlipped)
            NBT.setBoolean("flip", isFlipped);
    }

    @Override
    protected void readSyncTag(NBTTagCompound NBT) {
        super.readSyncTag(NBT);

        isFlipped = NBT.getBoolean("flip");
    }

    @Override
    public void writeToNBT(NBTTagCompound NBT) {
        super.writeToNBT(NBT);
        NBT.setInteger("tick", tickcount);

        NBT.setBoolean("emp", disabled);
    }

    @Override
    public void readFromNBT(NBTTagCompound NBT) {
        super.readFromNBT(NBT);
        tickcount = NBT.getInteger("tick");
        disabled = NBT.getBoolean("emp");
    }

    public boolean isSelfBlock() {
        if (worldObj.getBlock(xCoord, yCoord, zCoord) != this.getTileEntityBlockID())
            return false;
        int meta = this.getTile().getBlockMetadata();
        return meta == worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
    }

    @Override
    public boolean isPlayerAccessible(EntityPlayer var1) {
        if (ConfigRegistry.LOCKMACHINES.getState()
            && !var1.getCommandSenderName().equals(placer)) {
            ReikaChatHelper.write(
                "This " + this.getName() + " is locked and can only be used by " + placer
                + "!"
            );
            return false;
        }
        return super.isPlayerAccessible(var1);
    }

    @Override
    public final String getTEName() {
        MachineRegistry m = this.getTile();
        if (m.isMultiNamed())
            return m.getMultiName(this);
        return m.getName();
    }

    public boolean isShutdown() {
        return disabled;
    }

    public void onEMP() {
        disabled = true;
        if (this instanceof TileEntityIOMachine) {
            TileEntityIOMachine io = (TileEntityIOMachine) this;
            io.power = 0;
            io.torque = 0;
            io.omega = 0;
        }
    }

    public int getTextureStateForSide(int s) {
        switch (this.getBlockMetadata()) {
            case 0:
                return s == 4 ? this.getActiveTexture() : 0;
            case 1:
                return s == 5 ? this.getActiveTexture() : 0;
            case 2:
                return s == 2 ? this.getActiveTexture() : 0;
            case 3:
                return s == 3 ? this.getActiveTexture() : 0;
        }
        return 0;
    }

    protected int getActiveTexture() {
        return 0;
    }

    public IIcon getIconForSide(ForgeDirection dir) {
        return BlockRegistry.DECO.getBlockInstance().getIcon(0, 0);
    }

    public boolean hasIconOverride(int side) {
        return false;
    }

    public boolean matchMachine(IBlockAccess world, int x, int y, int z) {
        Block id = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        Block id2 = this.getTileEntityBlockID();
        int meta2 = this.getTile().getBlockMetadata();
        return id2 == id && meta2 == meta;
    }

    public void onRedirect() {}

    @Override
    @ModDependent(ModList.OPENCOMPUTERS)
    public final Visibility getOCNetworkVisibility() {
        if (this.getTile().isTransmissionMachine())
            return this.getTile().isAdvancedTransmission() ? Visibility.Network
                                                           : Visibility.None;
        else if (this.getTile().isPipe())
            return Visibility.None;
        else
            return this instanceof TileEntityBeltHub ? Visibility.None
                                                     : Visibility.Network;
    }

    public double heatEnergyPerDegree() {
        return ReikaThermoHelper.STEEL_HEAT
            * ReikaBlockHelper.getBlockVolume(worldObj, xCoord, yCoord, zCoord)
            * ReikaEngLibrary.rhoiron;
    }
}
