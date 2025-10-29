package Reika.DragonAPI.Interfaces.Item;

import net.minecraft.item.ItemStack;

public interface MultisheetItem extends IndexedItemSprites {
    public String getSpritesheet(ItemStack is);
}
