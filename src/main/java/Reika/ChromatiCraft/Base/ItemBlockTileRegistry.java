package Reika.ChromatiCraft.Base;

import Reika.ChromatiCraft.Items.ItemBlock.ItemBlockMultiType;
import Reika.ChromatiCraft.Registry.ChromaTiles;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockTileRegistry extends ItemBlockMultiType {
    public ItemBlockTileRegistry(Block b) {
        super(b);
    }

    @Override
    public final String getItemStackDisplayName(ItemStack is) {
        ChromaTiles c = ChromaTiles.getTileByCraftedItem(is);
        if (c == null)
            c = ChromaTiles.getTileFromIDandMetadata(field_150939_a, is.getItemDamage());
        return c != null ? c.getName() : super.getItemStackDisplayName(is);
    }
}
