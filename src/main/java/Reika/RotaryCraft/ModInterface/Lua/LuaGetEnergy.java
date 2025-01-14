/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.ModInterface.Lua;

import Reika.DragonAPI.ModInteract.Lua.LuaMethod;
import Reika.RotaryCraft.TileEntities.Transmission.TileEntityAdvancedGear;
import net.minecraft.tileentity.TileEntity;

public class LuaGetEnergy extends LuaMethod {
    public LuaGetEnergy() {
        super("getEnergy", TileEntityAdvancedGear.class);
    }

    @Override
    protected Object[] invoke(TileEntity te, Object[] args)
        throws LuaMethodException, InterruptedException {
        TileEntityAdvancedGear adv = (TileEntityAdvancedGear) te;
        return adv.getGearType().storesEnergy()
            ? new Object[] { adv.getEnergy() / 20, adv.getMaxStorageCapacity() }
            : null;
    }

    @Override
    public String getDocumentation() {
        return "Returns the stored energy.\nArgs: None\nReturns: [Energy,Capacity]";
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
