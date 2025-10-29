package Reika.Satisforestry.API;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

public interface PinkTreeType {
    public String name();

    public int ordinal();

    @SideOnly(Side.CLIENT)
    public int getBasicRenderColor();

    public ItemStack getBaseLog();
    public ItemStack getBaseLeaf();
    public ItemStack getSapling();
}
