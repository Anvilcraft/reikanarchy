package Reika.DragonAPI.Interfaces.Item;

import net.minecraft.item.ItemStack;

public interface IndexedItemSprites {
    public int getItemSpriteIndex(ItemStack is);

    public String getTexture(ItemStack is);

    public Class getTextureReferenceClass();
}
