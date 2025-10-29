package Reika.DragonAPI.Interfaces.Block;

import java.util.HashSet;

import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public interface ConnectedTextureGlass {
    /**
     * Returns the unconnected sides. Each integer represents one of 8 adjacent corners
     * to a face, with the same numbering convention as is found on a calculator or
     * computer number pad.
     */
    public HashSet<Integer>
    getEdgesForFace(IBlockAccess world, int x, int y, int z, ForgeDirection face);
    public IIcon getIconForEdge(IBlockAccess world, int x, int y, int z, int edge);

    public IIcon getIconForEdge(int itemMeta, int edge);
    public boolean renderCentralTextureForItem(int meta);
}
