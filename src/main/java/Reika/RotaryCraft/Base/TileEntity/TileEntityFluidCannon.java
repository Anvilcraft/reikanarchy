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

import java.util.Locale;

import Reika.DragonAPI.ASM.APIStripper.Strippable;
import Reika.DragonAPI.Instantiable.HybridTank;
import Reika.DragonAPI.Libraries.Java.ReikaStringParser;
import Reika.RotaryCraft.Auxiliary.Interfaces.PipeConnector;
import Reika.RotaryCraft.Base.TileEntity.TileEntityPiping.Flow;
import Reika.RotaryCraft.Registry.MachineRegistry;
import buildcraft.api.transport.IPipeConnection;
import buildcraft.api.transport.IPipeTile.PipeType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

@Strippable(value = { "buildcraft.api.transport.IPipeConnection" })
public abstract class TileEntityFluidCannon extends TileEntityAimedCannon
    implements IFluidHandler, PipeConnector, IPipeConnection {
    protected final HybridTank tank = new HybridTank(
        ReikaStringParser.stripSpaces(this.getTEName().toLowerCase(Locale.ENGLISH)),
        this.getCapacity()
    );

    @Override
    public final boolean hasAmmo() {
        return !tank.isEmpty();
    }

    protected int getCapacity() {
        return 1000;
    }

    @Override
    public final FluidStack
    drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    public final FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public final boolean canDrain(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public final FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] { tank.getInfo() };
    }

    public final int getLevel() {
        return tank.getLevel();
    }

    public final Fluid getContainedFluid() {
        return tank.getActualFluid();
    }

    public final void addLiquid(Fluid f, int amt) {
        if (this.isValidFluid(f))
            tank.addLiquid(amt, f);
    }

    @Override
    public final boolean canFill(ForgeDirection from, Fluid fluid) {
        return this.canReceiveFrom(from) && this.isValidFluid(fluid);
    }

    public abstract boolean isValidFluid(Fluid f);

    @Override
    public final int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (!this.canFill(from, resource.getFluid()))
            return 0;
        return tank.fill(resource, doFill);
    }

    public abstract boolean canReceiveFrom(ForgeDirection from);

    @Override
    public final boolean canConnectToPipeOnSide(MachineRegistry p, ForgeDirection side) {
        return this.canReceiveFrom(side.getOpposite()) && this.canConnectToPipe(p);
    }

    public final ConnectOverride
    overridePipeConnection(PipeType type, ForgeDirection side) {
        return type == PipeType.FLUID
            ? (this.canReceiveFrom(side) ? ConnectOverride.CONNECT
                                         : ConnectOverride.DISCONNECT)
            : ConnectOverride.DEFAULT;
    }

    @Override
    public final Flow getFlowForSide(ForgeDirection side) {
        return this.canReceiveFrom(side) ? Flow.INPUT : Flow.NONE;
    }

    @Override
    protected void readSyncTag(NBTTagCompound NBT) {
        super.readSyncTag(NBT);

        tank.readFromNBT(NBT);
    }

    @Override
    protected void writeSyncTag(NBTTagCompound NBT) {
        super.writeSyncTag(NBT);

        tank.writeToNBT(NBT);
    }

    public final boolean isEmpty() {
        return tank.isEmpty();
    }

    public final boolean isFull() {
        return tank.isFull();
    }

    @Override
    protected final double randomOffset() {
        return 0;
    }

    @Override
    public final int getOperationTime() {
        return 0;
    }
}
