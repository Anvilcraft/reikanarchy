package Reika.DragonAPI.Interfaces.Item;

import net.minecraft.item.ItemStack;

public interface ActivatedInventoryItem {
    public ItemStack[] getInventory(ItemStack is);

    public void decrementSlot(ItemStack is, int slot, int amt);

    public boolean isSlotActive(ItemStack is, int slot);

    public int getInventorySize(ItemStack is);

    public ItemStack getItem(ItemStack is, int slot);
}
