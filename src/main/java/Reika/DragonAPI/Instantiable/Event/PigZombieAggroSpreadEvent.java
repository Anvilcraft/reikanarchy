package Reika.DragonAPI.Instantiable.Event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

@Cancelable
public class PigZombieAggroSpreadEvent extends LivingAttackEvent {
    public final EntityPigZombie sourceEntity;

    public PigZombieAggroSpreadEvent(
        EntityPigZombie attacked,
        EntityPigZombie entity,
        DamageSource source,
        float ammount
    ) {
        super(entity, source, ammount);
        sourceEntity = attacked;
    }

    public static boolean
    fire(EntityPigZombie e, Entity other, DamageSource src, float amt) {
        if (!(other instanceof EntityPigZombie))
            return false;
        PigZombieAggroSpreadEvent evt
            = new PigZombieAggroSpreadEvent(e, (EntityPigZombie) other, src, amt);
        boolean flag = MinecraftForge.EVENT_BUS.post(evt);
        return !flag;
    }
}
