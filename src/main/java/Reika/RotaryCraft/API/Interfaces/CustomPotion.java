package Reika.RotaryCraft.API.Interfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;

/**
 * Implement this on item classes that represent custom potion bottles in order to allow
 * the Aerosolizer to use them. This is not necessary if you extend ItemPotion.
 */
public interface CustomPotion {
    public Potion getPotion(ItemStack is);
    public int getAmplifier(ItemStack is);
    public boolean isExtended(ItemStack is);
}
