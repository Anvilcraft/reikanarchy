package Reika.DragonAPI.ModInteract.Lua;

import java.util.ArrayList;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class LuaGetTanks extends LuaMethod {
    public LuaGetTanks() {
        super("getTanks", IFluidHandler.class);
    }

    @Override
    protected Object[] invoke(TileEntity te, Object[] args)
        throws LuaMethodException, InterruptedException {
        IFluidHandler ifl = (IFluidHandler) te;
        ArrayList<FluidTankInfo> li = new ArrayList();
        for (int i = 0; i < 6 * 0 + 1; i++) {
            ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];
            FluidTankInfo[] info = ifl.getTankInfo(dir);
            for (int k = 0; k < info.length; k++) {
                FluidTankInfo ifo = info[k];
                if (!li.contains(ifo)) {
                    li.add(ifo);
                }
            }
        }
        Object[] o = new Object[li.size() * 4];
        for (int i = 0; i < li.size(); i++) {
            FluidTankInfo info = li.get(i);
            if (info.fluid != null) {
                o[i * 3] = info.fluid.getFluid().getLocalizedName();
                o[i * 3 + 1] = info.fluid.amount;
                o[i * 3 + 2] = info.capacity;
                o[i * 3 + 3] = info.fluid.getFluid().getName();
            } else {
                o[i * 3] = null;
                o[i * 3 + 1] = 0;
                o[i * 3 + 2] = info.capacity;
                o[i * 3 + 3] = null;
            }
        }
        return o;
    }

    @Override
    public String getDocumentation() {
        return "Returns all the fluid tanks.\nArgs: None\nReturns: List of [Fluid, Amount, Capacity, Internal ID]";
    }

    @Override
    public String getArgsAsString() {
        return "";
    }

    @Override
    public ReturnType getReturnType() {
        return ReturnType.ARRAY;
    }
}
