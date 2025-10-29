package Reika.ChromatiCraft.API.Interfaces;

import Reika.DragonAPI.Interfaces.Item.CustomEnchantingCategory;
import cpw.mods.fml.common.eventhandler.Event.Result;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

public interface EnchantableItem extends CustomEnchantingCategory {
    public Result getEnchantValidity(Enchantment e, ItemStack is);
}
