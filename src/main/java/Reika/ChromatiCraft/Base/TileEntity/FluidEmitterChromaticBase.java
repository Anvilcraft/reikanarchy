/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Base.TileEntity;

import java.util.Locale;

import Reika.DragonAPI.ASM.APIStripper.Strippable;
import Reika.DragonAPI.Instantiable.HybridTank;
import Reika.DragonAPI.Libraries.Java.ReikaStringParser;
import Reika.DragonAPI.Libraries.ReikaFluidHelper;
import buildcraft.api.transport.IPipeConnection;
import buildcraft.api.transport.IPipeTile.PipeType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

@Strippable(value = { "buildcraft.api.transport.IPipeConnection" })
public abstract class FluidEmitterChromaticBase
    extends TileEntityChromaticBase implements IFluidHandler, IPipeConnection {
    protected final HybridTank tank = new HybridTank(
        ReikaStringParser.stripSpaces(this.getTEName().toLowerCase(Locale.ENGLISH)),
        this.getCapacity()
    );

    public abstract int getCapacity();

    @Override
    public final FluidStack
    drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return this.canDrain(from, resource.getFluid())
            ? tank.drain(resource.amount, doDrain)
            : null;
    }

    @Override
    public final FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        if (this.canDrain(from, null))
            return tank.drain(maxDrain, doDrain);
        return null;
    }

    @Override
    public final boolean canDrain(ForgeDirection from, Fluid fluid) {
        return this.canOutputTo(from)
            && ReikaFluidHelper.isFluidDrainableFromTank(fluid, tank);
    }

    @Override
    public final FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] { tank.getInfo() };
    }

    public final int getLevel() {
        return tank.getLevel();
    }

    public Fluid getContainedFluid() {
        return tank.getActualFluid();
    }

    public final void removeLiquid(int amt) {
        tank.removeLiquid(amt);
    }

    @Override
    public final boolean canFill(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public final int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return 0;
    }

    public abstract boolean canOutputTo(ForgeDirection to);

    public final ConnectOverride
    overridePipeConnection(PipeType type, ForgeDirection side) {
        return type == PipeType.FLUID
            ? (this.canOutputTo(side) ? ConnectOverride.CONNECT
                                      : ConnectOverride.DISCONNECT)
            : ConnectOverride.DEFAULT;
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
}
