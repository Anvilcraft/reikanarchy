package Reika.DragonAPI.Interfaces.TileEntity;

import Reika.DragonAPI.Interfaces.TextureFetcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface RenderFetcher {
    @SideOnly(Side.CLIENT)
    public abstract TextureFetcher getRenderer();
}
