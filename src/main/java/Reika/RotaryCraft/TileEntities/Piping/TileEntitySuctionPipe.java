/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.TileEntities.Piping;

import Reika.RotaryCraft.Base.TileEntity.TileEntityPiping;
import Reika.RotaryCraft.Registry.MachineRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

public class TileEntitySuctionPipe extends TileEntityPiping {
    private Fluid fluid;
    private int level;

    @Override
    public Block getPipeBlockType() {
        return Blocks.nether_brick;
    }

    @Override
    public int getFluidLevel() {
        return level;
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry p, ForgeDirection dir) {
        return p == MachineRegistry.HOSE || p == MachineRegistry.FUELLINE
            || p.isStandardPipe();
    }

    @Override
    protected void setFluid(Fluid f) {
        fluid = f;
    }

    @Override
    protected void setLevel(int amt) {
        level = amt;
    }

    @Override
    protected boolean interactsWithMachines() {
        return true;
    }

    @Override
    protected void onIntake(TileEntity te) {}

    @Override
    public boolean canReceiveFromPipeOn(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean canEmitToPipeOn(ForgeDirection side) {
        return true;
    }

    @Override
    public boolean canIntakeFromIFluidHandler(ForgeDirection side) {
        return true;
    }

    @Override
    public boolean canOutputToIFluidHandler(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean isValidFluid(Fluid f) {
        return true;
    }

    @Override
    public IIcon getBlockIcon() {
        return Blocks.nether_brick.getIcon(0, 0);
    }

    @Override
    public boolean hasLiquid() {
        return fluid != null && level > 0;
    }

    @Override
    public Fluid getFluidType() {
        return fluid;
    }

    @Override
    public MachineRegistry getTile() {
        return MachineRegistry.SUCTION;
    }
}
