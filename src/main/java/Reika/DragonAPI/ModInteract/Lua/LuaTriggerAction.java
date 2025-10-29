package Reika.DragonAPI.ModInteract.Lua;

import Reika.DragonAPI.Interfaces.TileEntity.TriggerableAction;
import net.minecraft.tileentity.TileEntity;

public class LuaTriggerAction extends LuaMethod {
    public LuaTriggerAction() {
        super("trigger", TriggerableAction.class);
    }

    @Override
    protected Object[] invoke(TileEntity te, Object[] args)
        throws LuaMethodException, InterruptedException {
        return new Object[] { ((TriggerableAction) te).trigger() };
    }

    @Override
    public String getDocumentation() {
        return "Triggers the block to attempt to perform its action.\nArgs: None\nReturns: Success true/false";
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
