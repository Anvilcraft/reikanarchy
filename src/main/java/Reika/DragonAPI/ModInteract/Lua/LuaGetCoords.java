package Reika.DragonAPI.ModInteract.Lua;

import net.minecraft.tileentity.TileEntity;

public class LuaGetCoords extends LuaMethod {
    public LuaGetCoords() {
        super("getCoords", TileEntity.class);
    }

    @Override
    protected Object[] invoke(TileEntity te, Object[] args)
        throws LuaMethodException, InterruptedException {
        return new Object[] { te.xCoord, te.yCoord, te.zCoord };
    }

    @Override
    public String getDocumentation() {
        return "Returns the TileEntity coordinates.\nArgs: None\nReturns: [x,y,z]";
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
