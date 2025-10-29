package Reika.DragonAPI.Interfaces.Block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.IBlockAccess;

/**
 * For non-cube blocks that are intended to be underwater yet not have the "walls" of
 * water around a void. Avoids needing to make it Material.water.
 */
public interface Submergeable {
    public boolean isSubmergeable(IBlockAccess iba, int x, int y, int z);

    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass();

    public boolean canRenderInPass(int pass);

    @SideOnly(Side.CLIENT)
    public boolean renderLiquid(int meta);
}
