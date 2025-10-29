package Reika.DragonAPI.Instantiable.Event;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.Event.HasResult;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

@HasResult
public class AttackAggroEvent extends LivingAttackEvent {
    public AttackAggroEvent(EntityMob entity, DamageSource source, float ammount) {
        super(entity, source, ammount);
    }

    public static boolean fire(EntityMob e, DamageSource src, float amt) {
        Event evt = new AttackAggroEvent(e, src, amt);
        MinecraftForge.EVENT_BUS.post(evt);
        switch (evt.getResult()) {
            case ALLOW:
                return true;
            case DENY:
                return false;
            case DEFAULT:
            default:
                Entity att = src.getEntity();
                return att != e
                    && (e instanceof EntityPigZombie ? att instanceof EntityPlayer : true
                    );
        }
    }
}
