package Reika.DragonAPI.Instantiable.GUI.Slot;

import Reika.DragonAPI.Instantiable.Event.SlotEvent;
import Reika.DragonAPI.Instantiable.Event.SlotEvent.AddToSlotEvent;
import Reika.DragonAPI.Instantiable.Event.SlotEvent.RemoveFromSlotEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class EventSlot extends Slot {
    public EventSlot(IInventory ii, int id, int x, int y) {
        super(ii, id, x, y);
    }

    @Override
    public void onPickupFromSlot(EntityPlayer ep, ItemStack is) {
        if (!MinecraftForge.EVENT_BUS.post(
                new RemoveFromSlotEvent(slotNumber, inventory, is, ep)
            ))
            super.onPickupFromSlot(ep, is);
    }

    @Override
    public void putStack(ItemStack is) {
        if (!MinecraftForge.EVENT_BUS.post(
                new AddToSlotEvent(slotNumber, inventory, is, this.getStack())
            ))
            super.putStack(is);
    }

    @Override
    public void onSlotChanged() {
        super.onSlotChanged();
        MinecraftForge.EVENT_BUS.post(new SlotEvent(slotNumber, inventory));
    }
}
