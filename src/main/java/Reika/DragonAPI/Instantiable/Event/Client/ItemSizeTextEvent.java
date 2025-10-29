package Reika.DragonAPI.Instantiable.Event.Client;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class ItemSizeTextEvent extends Event {
    private final ItemStack item;

    public final String originalString;
    public String newString;

    public ItemSizeTextEvent(ItemStack is, String s) {
        item = is;
        originalString = s;
        newString = originalString;
    }

    public ItemStack getItem() {
        return item.copy();
    }

    public static String fire(ItemStack is, String s) {
        ItemSizeTextEvent evt = new ItemSizeTextEvent(is, s);
        MinecraftForge.EVENT_BUS.post(evt);
        return evt.newString;
    }
}
