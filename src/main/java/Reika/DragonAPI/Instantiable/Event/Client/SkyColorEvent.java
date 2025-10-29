package Reika.DragonAPI.Instantiable.Event.Client;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;

public class SkyColorEvent extends Event {
    public final int originalColor;
    public int color;

    public SkyColorEvent(int c) {
        originalColor = c;
        color = originalColor;
    }

    public static int fire(int c) {
        SkyColorEvent evt = new SkyColorEvent(c);
        MinecraftForge.EVENT_BUS.post(evt);
        return evt.color;
    }
}
