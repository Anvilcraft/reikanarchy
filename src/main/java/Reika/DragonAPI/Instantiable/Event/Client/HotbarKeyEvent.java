package Reika.DragonAPI.Instantiable.Event.Client;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

@Cancelable
public class HotbarKeyEvent extends Event {
    public final GuiContainer gui;

    public final int hotbarSlot;
    public final int slotNumber;

    public final int keyCode;

    public HotbarKeyEvent(GuiContainer g, Slot s, int idx, int key) {
        gui = g;
        hotbarSlot = idx;
        slotNumber = s.slotNumber;
        keyCode = key;
    }

    public ItemStack getItem() {
        return Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(hotbarSlot);
    }

    public static boolean fire(GuiContainer gui, Slot s, int idx, int key) {
        return key
            == Minecraft.getMinecraft().gameSettings.keyBindsHotbar[idx].getKeyCode()
            && !MinecraftForge.EVENT_BUS.post(new HotbarKeyEvent(gui, s, idx, key));
    }
}
