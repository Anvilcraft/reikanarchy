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
import Reika.RotaryCraft.TileEntities.Transmission.TileEntityAdvancedGear.GearType;
import net.minecraft.tileentity.TileEntity;

public class LuaGetRatio extends LuaMethod {
    public LuaGetRatio() {
        super("getRatio", TileEntityAdvancedGear.class);
    }

    @Override
    protected Object[] invoke(TileEntity te, Object[] args)
        throws LuaMethodException, InterruptedException {
        TileEntityAdvancedGear adv = (TileEntityAdvancedGear) te;
        return adv.getGearType() == GearType.CVT ? new Object[] { adv.getRatio() } : null;
    }

    @Override
    public String getDocumentation() {
        return "Returns the CVT ratio.\nArgs: None\nReturns: Ratio";
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
