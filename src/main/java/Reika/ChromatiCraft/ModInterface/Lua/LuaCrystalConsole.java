/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.ModInterface.Lua;

import Reika.ChromatiCraft.TileEntity.TileEntityCrystalConsole;
import Reika.DragonAPI.ModInteract.Lua.LuaMethod;
import net.minecraft.tileentity.TileEntity;

public class LuaCrystalConsole extends LuaMethod {
    public LuaCrystalConsole() {
        super("setState", TileEntityCrystalConsole.class);
    }

    @Override
    protected Object[] invoke(TileEntity te, Object[] args)
        throws LuaMethodException, InterruptedException {
        TileEntityCrystalConsole tile = (TileEntityCrystalConsole) te;
        int slot = ((Double) args[0]).intValue();
        boolean on = (Boolean) args[1];
        tile.toggle(slot, on);
        return null;
    }

    @Override
    public String getDocumentation() {
        return "Sets a console state.\nArgs: Slot, State\nReturns: Nothing";
    }

    @Override
    public String getArgsAsString() {
        return "int slot, boolean on";
    }

    @Override
    public ReturnType getReturnType() {
        return ReturnType.VOID;
    }
}
