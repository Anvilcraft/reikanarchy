package Reika.ChromatiCraft.ModInterface.Lua;

import Reika.ChromatiCraft.ModInterface.Bees.TileEntityLumenAlveary;
import Reika.DragonAPI.ASM.DependentMethodStripper.ModDependent;
import Reika.DragonAPI.ModInteract.Lua.LuaMethod;
import Reika.DragonAPI.ModInteract.Lua.LuaMethod.ModDependentMethod;
import Reika.DragonAPI.ModList;
import forestry.api.multiblock.IAlvearyController;
import net.minecraft.tileentity.TileEntity;

@ModDependentMethod(ModList.FORESTRY)
public abstract class AlvearyLuaMethod extends LuaMethod {
    protected AlvearyLuaMethod(String name) {
        super(name, TileEntityLumenAlveary.class);
    }

    @Override
    protected final Object[] invoke(TileEntity te, Object[] args)
        throws LuaMethodException, InterruptedException {
        TileEntityLumenAlveary tile = (TileEntityLumenAlveary) te;
        IAlvearyController iac = tile.getMultiblockLogic().getController();
        return this.invoke(tile, iac, args);
    }

    @ModDependent(ModList.FORESTRY)
    protected abstract Object[] invoke(
        TileEntityLumenAlveary tile, IAlvearyController iac, Object[] args
    );
}
