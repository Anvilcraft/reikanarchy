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

import Reika.DragonAPI.Libraries.Registry.ReikaParticleHelper;
import Reika.DragonAPI.ModInteract.Lua.LuaMethod;
import Reika.RotaryCraft.TileEntities.Decorative.TileEntityParticleEmitter;
import net.minecraft.tileentity.TileEntity;

public class LuaSetParticle extends LuaMethod {
    public LuaSetParticle() {
        super("setParticle", TileEntityParticleEmitter.class);
    }

    @Override
    protected Object[] invoke(TileEntity te, Object[] args)
        throws LuaMethodException, InterruptedException {
        TileEntityParticleEmitter part = (TileEntityParticleEmitter) te;
        if (args[0] instanceof String) {
            part.particleType = ReikaParticleHelper.getByString((String) args[0]);
        } else {
            int index = ((Double) args[0]).intValue();
            part.particleType = ReikaParticleHelper.particleList[index];
        }
        return null;
    }

    @Override
    public String getDocumentation() {
        return "Sets the particle setting.\nArgs: Setting ordinal or name\nReturns: Nothing";
    }

    @Override
    public String getArgsAsString() {
        return "int settingOrdinal OR String particleName";
    }

    @Override
    public ReturnType getReturnType() {
        return ReturnType.VOID;
    }
}
