package Reika.DragonAPI.Interfaces.Registry;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public interface TileEnum {
    public Class<? extends TileEntity> getTEClass();
    public String getName();
    public Block getBlock();
    public int getBlockMetadata();
    public ItemStack getCraftedProduct();
    public ItemStack getCraftedProduct(TileEntity te);
    public int ordinal();
}
