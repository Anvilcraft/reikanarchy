package Reika.DragonAPI.ModInteract.Lua;

import Reika.DragonAPI.Base.TileEntityBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class LuaGetPlacer extends LuaMethod {
    public LuaGetPlacer() {
        super("getPlacer", TileEntityBase.class);
    }

    @Override
    protected Object[] invoke(TileEntity te, Object[] args)
        throws LuaMethodException, InterruptedException {
        TileEntityBase tile = (TileEntityBase) te;
        EntityPlayer ep = tile.getPlacer();
        return new Object[] { ep.getCommandSenderName(), ep.getUniqueID() };
    }

    @Override
    public String getDocumentation() {
        return "Returns the player who placed the machine.\nArgs: None\nReturns: [Name, UUID]";
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
