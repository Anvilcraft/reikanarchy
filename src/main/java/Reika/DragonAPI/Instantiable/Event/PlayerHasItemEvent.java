package Reika.DragonAPI.Instantiable.Event;

import cpw.mods.fml.common.eventhandler.Event.HasResult;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

@HasResult
public class PlayerHasItemEvent extends PlayerEvent {
    private final ItemStack item;

    public PlayerHasItemEvent(EntityPlayer ep, ItemStack is) {
        super(ep);

        item = is;
    }

    public ItemStack getItem() {
        return item.copy();
    }
}
