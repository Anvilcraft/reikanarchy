package Reika.DragonAPI.Instantiable.Event.Client;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.Event.HasResult;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

@HasResult
public class ItemEffectRenderEvent extends Event {
    private final ItemStack item;
    public final int renderPass;

    public ItemEffectRenderEvent(ItemStack is, int pass) {
        item = is;
        renderPass = pass;
    }

    public ItemStack getItem() {
        return item.copy();
    }

    public static final boolean fire(ItemStack is, int pass) {
        ItemEffectRenderEvent evt = new ItemEffectRenderEvent(is, pass);
        MinecraftForge.EVENT_BUS.post(evt);
        switch (evt.getResult()) {
            case ALLOW:
                return true;
            case DENY:
                return false;
            case DEFAULT:
            default:
                return is.hasEffect(pass);
        }
    }
}
