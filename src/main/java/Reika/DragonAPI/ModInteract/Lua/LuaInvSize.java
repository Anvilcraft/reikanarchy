/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.ModInteract.Lua;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

public class LuaInvSize extends LuaMethod {
    public LuaInvSize() {
        super("getSizeInv", IInventory.class);
    }

    @Override
    protected Object[] invoke(TileEntity te, Object[] args)
        throws LuaMethodException, InterruptedException {
        return new Object[] { ((IInventory) te).getSizeInventory() };
    }

    @Override
    public String getDocumentation() {
        return "Returns the inventory size.\nArgs: None\nReturns: Inventory Size";
    }

    @Override
    public String getArgsAsString() {
        return "";
    }

    @Override
    public ReturnType getReturnType() {
        return ReturnType.INTEGER;
    }
}
