package Reika.DragonAPI.Interfaces;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.World;

public interface CustomMapColorBiome {
    @SideOnly(Side.CLIENT)
    public int getMapColor(World world, int x, int z);
}
