package Reika.DragonAPI.ModInteract.Lua;

import net.minecraft.tileentity.TileEntity;

public abstract class LibraryLuaMethod extends LuaMethod {
    public LibraryLuaMethod(String name) {
        super(name, TileEntity.class);
    }

    public final boolean isDocumented() {
        return false;
    }
}
