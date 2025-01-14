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

public class LuaSetTorque extends LuaMethod {
    public LuaSetTorque() {
        super("setTorque", TileEntityAdvancedGear.class);
    }

    @Override
    protected Object[] invoke(TileEntity te, Object[] args)
        throws LuaMethodException, InterruptedException {
        TileEntityAdvancedGear adv = (TileEntityAdvancedGear) te;
        if (adv.getGearType() == GearType.COIL) {
            int tq = ((Double) args[0]).intValue();
            adv.setReleaseTorque(tq);
        }
        return null;
    }

    @Override
    public String getDocumentation() {
        return "Sets the coil torque.\nArgs: Desired Torque\nReturns: Nothing";
    }

    @Override
    public String getArgsAsString() {
        return "int torque";
    }

    @Override
    public ReturnType getReturnType() {
        return ReturnType.VOID;
    }
}
