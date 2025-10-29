package Reika.DragonAPI.Instantiable.Event;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.Event.HasResult;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

@HasResult
public class EnderAttackTPEvent extends LivingAttackEvent {
    public EnderAttackTPEvent(EntityEnderman entity, DamageSource source, float ammount) {
        super(entity, source, ammount);
    }

    public static boolean fire(EntityEnderman e, DamageSource src, float amt) {
        Event evt = new EnderAttackTPEvent(e, src, amt);
        MinecraftForge.EVENT_BUS.post(evt);
        switch (evt.getResult()) {
            case ALLOW:
                return true;
            case DENY:
                return false;
            case DEFAULT:
            default:
                return src instanceof EntityDamageSourceIndirect;
        }
    }
}
