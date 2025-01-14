package Reika.Satisforestry.Config;

import java.util.HashMap;

import Reika.DragonAPI.Libraries.Registry.ReikaItemHelper;
import net.minecraft.item.ItemStack;

public class ResourceItem extends NodeResource<ItemStack> {
    public ResourceItem(String s, String n, int w, int c, HashMap<String, Object> map) {
        super(s, n, w, c, map);
    }

    @Override
    public ItemStack getItem(NodeItem obj) {
        return obj.item.copy();
    }

    @Override
    public boolean matchItem(NodeItem obj, ItemStack is) {
        return ReikaItemHelper.matchStacks(is, obj.item);
    }

    @Override
    public String getDisplayName(ItemStack item) {
        return item.getDisplayName();
    }
}
