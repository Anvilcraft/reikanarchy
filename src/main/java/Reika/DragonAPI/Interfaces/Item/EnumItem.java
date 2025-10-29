package Reika.DragonAPI.Interfaces.Item;

import Reika.DragonAPI.Interfaces.Registry.RegistrationList;
import net.minecraft.item.ItemStack;

public interface EnumItem {
    public RegistrationList getRegistry(ItemStack is);
}
