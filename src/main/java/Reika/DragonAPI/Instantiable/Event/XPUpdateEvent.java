package Reika.DragonAPI.Instantiable.Event;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraftforge.common.MinecraftForge;

public class XPUpdateEvent extends Event {
    public final EntityXPOrb xp;

    public XPUpdateEvent(EntityXPOrb e) {
        xp = e;
    }

    public static void fire(EntityXPOrb e) {
        MinecraftForge.EVENT_BUS.post(new XPUpdateEvent(e));
    }
}
