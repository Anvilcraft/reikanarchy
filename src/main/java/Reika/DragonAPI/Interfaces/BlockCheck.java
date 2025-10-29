package Reika.DragonAPI.Interfaces;
import Reika.DragonAPI.Instantiable.Data.Immutable.BlockKey;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public interface BlockCheck {
    public boolean matchInWorld(World world, int x, int y, int z);
    public boolean match(Block b, int meta);
    public boolean match(BlockCheck bc);
    public void place(World world, int x, int y, int z, int flags);
    public ItemStack asItemStack();

    @SideOnly(Side.CLIENT)
    public ItemStack getDisplay();

    public BlockKey asBlockKey();

    public static interface TileEntityCheck extends BlockCheck {
        @SideOnly(Side.CLIENT)
        public TileEntity getTileEntity();
    }
}
