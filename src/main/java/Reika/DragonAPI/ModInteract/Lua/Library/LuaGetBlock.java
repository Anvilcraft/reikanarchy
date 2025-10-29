package Reika.DragonAPI.ModInteract.Lua.Library;

import Reika.DragonAPI.ModInteract.Lua.LibraryLuaMethod;
import net.minecraft.tileentity.TileEntity;

public class LuaGetBlock extends LibraryLuaMethod {
    public LuaGetBlock() {
        super("getBlock");
    }

    @Override
    protected Object[] invoke(TileEntity te, Object[] args)
        throws LuaMethodException, InterruptedException {
        int x = ((Double) args[0]).intValue();
        int y = ((Double) args[1]).intValue();
        int z = ((Double) args[2]).intValue();
        return new Object[] { te.worldObj.getBlock(x, y, z).getUnlocalizedName(),
                              te.worldObj.getBlockMetadata(x, y, z) };
    }

    @Override
    public String getDocumentation() {
        return "Returns block and metadata at position.\nArgs: x, y, z\nReturns: {Block Name, Metadata}";
    }

    @Override
    public String getArgsAsString() {
        return "int x, int y, int z";
    }

    @Override
    public ReturnType getReturnType() {
        return ReturnType.ARRAY;
    }
}
