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

public class LuaSetSpeed extends LuaMethod {
    public LuaSetSpeed() {
        super("setSpeed", TileEntityAdvancedGear.class);
    }

    @Override
    protected Object[] invoke(TileEntity te, Object[] args)
        throws LuaMethodException, InterruptedException {
        TileEntityAdvancedGear adv = (TileEntityAdvancedGear) te;
        if (adv.getGearType() == GearType.COIL) {
            int s = ((Double) args[0]).intValue();
            adv.setReleaseOmega(s);
        }
        return null;
    }

    @Override
    public String getDocumentation() {
        return "Sets the coil speed.\nArgs: Desired Speed\nReturns: Nothing";
    }

    @Override
    public String getArgsAsString() {
        return "int speed";
    }

    @Override
    public ReturnType getReturnType() {
        return ReturnType.VOID;
    }
}
