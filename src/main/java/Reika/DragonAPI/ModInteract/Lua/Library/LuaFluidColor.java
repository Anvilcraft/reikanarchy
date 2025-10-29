package Reika.DragonAPI.ModInteract.Lua.Library;

import Reika.DragonAPI.Libraries.ReikaFluidHelper;
import Reika.DragonAPI.ModInteract.Lua.LibraryLuaMethod;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class LuaFluidColor extends LibraryLuaMethod {
    public LuaFluidColor() {
        super("fluidColor");
    }

    @Override
    protected Object[] invoke(TileEntity te, Object[] args)
        throws LuaMethodException, InterruptedException {
        String name = (String) args[0];
        Fluid f = FluidRegistry.getFluid(name);
        if (f == null)
            throw new IllegalArgumentException("No such fluid with name '" + name + "'.");
        return new Object[] { ReikaFluidHelper.getFluidColor(f) };
    }

    @Override
    public String getDocumentation() {
        return "Returns fluid color.\nArgs: fluidName\nReturns: Fluid color";
    }

    @Override
    public String getArgsAsString() {
        return "String fluidName";
    }

    @Override
    public ReturnType getReturnType() {
        return ReturnType.INTEGER;
    }
}
