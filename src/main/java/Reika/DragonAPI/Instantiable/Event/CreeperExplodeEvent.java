package Reika.DragonAPI.Instantiable.Event;

import net.minecraft.entity.monster.EntityCreeper;
import net.minecraftforge.event.entity.living.LivingEvent;

public class CreeperExplodeEvent extends LivingEvent {
    public final EntityCreeper creeper;

    public CreeperExplodeEvent(EntityCreeper e) {
        super(e);
        creeper = e;
    }
}
