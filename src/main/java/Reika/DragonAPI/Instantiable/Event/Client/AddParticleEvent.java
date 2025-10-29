package Reika.DragonAPI.Instantiable.Event.Client;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.particle.EntityFX;

@Cancelable
@Deprecated
@SideOnly(Side.CLIENT)
public class AddParticleEvent extends Event {
    private static final AddParticleEvent instance = new AddParticleEvent(null);

    private EntityFX particle;

    public AddParticleEvent(EntityFX fx) {
        particle = fx;
    }

    public EntityFX getParticle() {
        return particle;
    }

    public static AddParticleEvent getForParticle(EntityFX fx
    ) { //singleton'ed since extremely high-traffic
        instance.particle = fx;
        instance.setCanceled(false);
        return instance;
    }
}
