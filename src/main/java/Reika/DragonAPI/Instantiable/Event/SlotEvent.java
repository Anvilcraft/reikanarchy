package Reika.DragonAPI.Instantiable.Event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class SlotEvent extends Event {
    public final int slotID;
    public final IInventory inventory;

    public SlotEvent(int id, IInventory ii) {
        slotID = id;
        inventory = ii;
    }

    /**
     Fired when trying to remove a stack from a slot.
     Cancel the event to prevent the normal pickup/split/swap behavior
   */
    @Cancelable
    public static class ClickItemInSlotEvent extends SlotEvent {
        public final int buttonID;
        public final ItemStack itemInSlot;
        public final Slot slot;
        public final EntityPlayer player;

        public ClickItemInSlotEvent(
            int id, IInventory ii, Slot s, EntityPlayer ep, int button
        ) {
            super(id, ii);
            slot = s;
            itemInSlot = slot.getStack();
            player = ep;
            buttonID = button;
        }

        public static boolean fire(Slot s, EntityPlayer ep, int button) {
            if (MinecraftForge.EVENT_BUS.post(
                    new ClickItemInSlotEvent(s.getSlotIndex(), s.inventory, s, ep, button)
                ))
                return false;
            return s.canTakeStack(ep);
        }
    }

    public static class AddToSlotEvent extends SlotEvent {
        private final ItemStack added;
        private final ItemStack previous;

        private static Slot currentSlotCall;

        public AddToSlotEvent(int id, IInventory ii, ItemStack is, ItemStack pre) {
            super(id, ii);
            added = is;
            previous = pre;
        }

        public final ItemStack getItem() {
            return added != null ? added.copy() : added;
        }

        public final ItemStack getPreviousItem() {
            return previous != null ? previous.copy() : previous;
        }

        public static void fire(Slot s, ItemStack is) {
            if (currentSlotCall == s) //prevent AE SOE
                return;
            currentSlotCall = s;
            MinecraftForge.EVENT_BUS.post(
                new AddToSlotEvent(s.getSlotIndex(), s.inventory, is, s.getStack())
            );
            currentSlotCall = null;
        }
    }

    public static class RemoveFromSlotEvent extends SlotEvent {
        private final ItemStack removed;
        public final EntityPlayer player;

        public RemoveFromSlotEvent(int id, IInventory ii, ItemStack is, EntityPlayer ep) {
            super(id, ii);
            removed = is;
            player = ep;
        }

        public final ItemStack getItem() {
            return removed != null ? removed.copy() : removed;
        }
    }

    @Cancelable
    public static class InitialClickEvent extends SlotEvent {
        public final Container container;
        public final EntityPlayer player;
        public final int mouseButton;
        public final int modifiers;

        public InitialClickEvent(Container c, int id, int b, int m, EntityPlayer ep) {
            super(id, getInvFromSlot(c, id));
            container = c;
            player = ep;
            mouseButton = b;
            modifiers = m;
        }

        private static IInventory getInvFromSlot(Container c, int id) {
            if (id < 0)
                return null;
            if (id >= c.inventorySlots.size())
                return null;
            Slot s = c.getSlot(id);
            return s != null ? s.inventory : null;
        }

        public static boolean
        fire(Container c, int idx, int button, int modifiers, EntityPlayer ep) {
            return MinecraftForge.EVENT_BUS.post(
                new InitialClickEvent(c, idx, button, modifiers, ep)
            );
        }
    }
}
