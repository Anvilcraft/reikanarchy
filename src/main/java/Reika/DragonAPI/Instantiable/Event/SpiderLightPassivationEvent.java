package Reika.DragonAPI.Instantiable.Event;

import net.minecraft.entity.monster.EntitySpider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;

public class SpiderLightPassivationEvent extends LivingEvent {
    public final EntitySpider spider;
    public final float originalThreshold;
    public final float lightLevel;

    public float threshold;

    public SpiderLightPassivationEvent(EntitySpider e, float t, float l) {
        super(e);
        spider = e;
        originalThreshold = t;
        lightLevel = l;
        threshold = originalThreshold;
    }

    public static float fire(float thresh, EntitySpider e, float light) {
        SpiderLightPassivationEvent evt
            = new SpiderLightPassivationEvent(e, thresh, light);
        MinecraftForge.EVENT_BUS.post(evt);
        return evt.threshold;
    }
}
