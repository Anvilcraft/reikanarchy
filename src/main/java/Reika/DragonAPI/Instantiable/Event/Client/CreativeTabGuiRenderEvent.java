package Reika.DragonAPI.Instantiable.Event.Client;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;

@Cancelable
@SideOnly(Side.CLIENT)
/** Cancel the event to disable the "vanilla" rendering */
public class CreativeTabGuiRenderEvent extends Event {
    public final GuiContainerCreative gui;
    public final CreativeTabs tab;
    public final GuiTextField textField;
    public final int tabPage;
    public final int guiXSize;
    public final int guiYSize;

    public CreativeTabGuiRenderEvent(
        GuiContainerCreative gui,
        CreativeTabs tab,
        GuiTextField search,
        int tabPage,
        int xSize,
        int ySize
    ) {
        this.gui = gui;
        this.tab = tab;
        textField = search;
        this.tabPage = tabPage;
        guiXSize = xSize;
        guiYSize = ySize;
    }

    public static boolean fire(
        GuiContainerCreative gui,
        CreativeTabs tab,
        GuiTextField search,
        int tabPage,
        int xSize,
        int ySize
    ) {
        return MinecraftForge.EVENT_BUS.post(
            new CreativeTabGuiRenderEvent(gui, tab, search, tabPage, xSize, ySize)
        );
    }

    public static void fireFromTextureDraw(
        GuiContainerCreative gui,
        int left,
        int top,
        int u,
        int v,
        int xSize,
        int ySize,
        CreativeTabs tab,
        GuiTextField search,
        int tabPage
    ) {
        if (!fire(gui, tab, search, tabPage, xSize, ySize)) {
            gui.drawTexturedModalRect(gui.guiLeft, gui.guiTop, u, v, xSize, ySize);
        }
    }
}
