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
import Reika.DragonAPI.Libraries.ReikaFluidHelper;
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
public abstract class PoweredLiquidIO
    extends PoweredLiquidBase implements IFluidHandler, PipeConnector, IPipeConnection {
    protected final HybridTank output = new HybridTank(
        ReikaStringParser.stripSpaces(
            this.getTEName().toLowerCase(Locale.ENGLISH) + "out"
        ),
        this.getOutputCapacity()
    );
    protected final HybridTank input = new HybridTank(
        ReikaStringParser.stripSpaces(
            this.getTEName().toLowerCase(Locale.ENGLISH) + "in"
        ),
        this.getInputCapacity()
    );

    @Override
    public final FluidStack
    drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return this.canDrain(from, resource.getFluid())
            ? output.drain(resource.amount, doDrain)
            : null;
    }

    public abstract Fluid getInputFluid();

    @Override
    public final FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        if (this.canDrain(from, null))
            return output.drain(maxDrain, doDrain);
        return null;
    }

    @Override
    public final boolean canDrain(ForgeDirection from, Fluid fluid) {
        return this.canOutputTo(from)
            && ReikaFluidHelper.isFluidDrainableFromTank(fluid, output);
    }

    @Override
    public final FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] { input.getInfo(), output.getInfo() };
    }

    public final int getInputLevel() {
        return input.getLevel();
    }

    public final int getOutputLevel() {
        return output.getLevel();
    }

    public final Fluid getFluidInInput() {
        return input.getActualFluid();
    }

    public final Fluid getFluidInOutput() {
        return output.getActualFluid();
    }

    public final void removeLiquid(int amt) {
        output.removeLiquid(amt);
    }

    public final void addLiquid(int amt) {
        input.addLiquid(amt, this.getInputFluid());
    }

    @Override
    public final boolean canFill(ForgeDirection from, Fluid fluid) {
        return this.canReceiveFrom(from) && this.isValidFluid(fluid);
    }

    public boolean isValidFluid(Fluid f) {
        return f.equals(this.getInputFluid());
    }

    public int getInputCapacity() {
        return this.getCapacity();
    }

    public int getOutputCapacity() {
        return this.getCapacity();
    }

    @Override
    public final int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (!this.canFill(from, resource.getFluid()))
            return 0;
        return input.fill(resource, doFill);
    }

    public abstract boolean canOutputTo(ForgeDirection to);

    public abstract boolean canReceiveFrom(ForgeDirection from);

    @Override
    public final boolean canConnectToPipeOnSide(MachineRegistry p, ForgeDirection side) {
        return (this.canReceiveFrom(side) && this.canIntakeFromPipe(p))
            || (this.canOutputTo(side) && this.canOutputToPipe(p))
            && this.canConnectToPipe(p);
    }

    public abstract boolean canIntakeFromPipe(MachineRegistry p);

    public abstract boolean canOutputToPipe(MachineRegistry p);

    public final ConnectOverride
    overridePipeConnection(PipeType type, ForgeDirection side) {
        return type == PipeType.FLUID
            ? ((this.canOutputTo(side) || this.canReceiveFrom(side))
                   ? ConnectOverride.CONNECT
                   : ConnectOverride.DISCONNECT)
            : ConnectOverride.DEFAULT;
    }

    @Override
    public final Flow getFlowForSide(ForgeDirection side) {
        if (this.canOutputTo(side))
            return Flow.OUTPUT;
        else if (this.canReceiveFrom(side))
            return Flow.INPUT;
        return Flow.NONE;
    }

    @Override
    protected void readSyncTag(NBTTagCompound NBT) {
        super.readSyncTag(NBT);

        input.readFromNBT(NBT);
        output.readFromNBT(NBT);
    }

    @Override
    protected void writeSyncTag(NBTTagCompound NBT) {
        super.writeSyncTag(NBT);

        input.writeToNBT(NBT);
        output.writeToNBT(NBT);
    }
}
