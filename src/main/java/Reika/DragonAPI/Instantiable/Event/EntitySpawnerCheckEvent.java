package Reika.DragonAPI.Instantiable.Event;

import cpw.mods.fml.common.eventhandler.Event.HasResult;
import net.minecraft.entity.EntityLiving;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;

@HasResult
public class EntitySpawnerCheckEvent extends LivingEvent {
    public final MobSpawnerBaseLogic logic;

    public EntitySpawnerCheckEvent(MobSpawnerBaseLogic lgc, EntityLiving e) {
        super(e);
        logic = lgc;
    }

    public static boolean fire(EntityLiving e, MobSpawnerBaseLogic lgc) {
        EntitySpawnerCheckEvent evt = new EntitySpawnerCheckEvent(lgc, e);
        MinecraftForge.EVENT_BUS.post(evt);
        switch (evt.getResult()) {
            case ALLOW:
                return true;
            case DENY:
                return false;
            case DEFAULT:
            default:
                return e.getCanSpawnHere();
        }
    }
}
