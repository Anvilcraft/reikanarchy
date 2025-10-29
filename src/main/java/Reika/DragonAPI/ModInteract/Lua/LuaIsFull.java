package Reika.DragonAPI.ModInteract.Lua;

import Reika.DragonAPI.Libraries.ReikaInventoryHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

public class LuaIsFull extends LuaMethod {
    public LuaIsFull() {
        super("isFull", IInventory.class);
    }

    @Override
    protected Object[] invoke(TileEntity te, Object[] args)
        throws LuaMethodException, InterruptedException {
        IInventory ii = (IInventory) te;
        return new Object[] { ReikaInventoryHelper.isFull(ii) };
    }

    @Override
    public String getDocumentation() {
        return "Checks if an inventory is completely full.\nArgs: None\nReturns: true/false";
    }

    @Override
    public String getArgsAsString() {
        return "";
    }

    @Override
    public ReturnType getReturnType() {
        return ReturnType.BOOLEAN;
    }
}
