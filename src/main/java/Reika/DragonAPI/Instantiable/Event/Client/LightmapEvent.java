package Reika.DragonAPI.Instantiable.Event.Client;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;

public class LightmapEvent extends Event {
    public static void fire() {
        MinecraftForge.EVENT_BUS.post(new LightmapEvent());
    }
}
