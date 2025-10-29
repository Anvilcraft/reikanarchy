package Reika.DragonAPI.Instantiable.Event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;

@Cancelable
public class LivingFarDespawnEvent extends LivingEvent {
    public LivingFarDespawnEvent(EntityLiving entity) {
        super(entity);
    }

    public static boolean fire(EntityLiving e) {
        return !MinecraftForge.EVENT_BUS.post(new LivingFarDespawnEvent(e));
    }
}
