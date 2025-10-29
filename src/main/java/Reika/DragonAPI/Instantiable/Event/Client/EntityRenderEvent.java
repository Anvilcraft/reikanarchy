package Reika.DragonAPI.Instantiable.Event.Client;

import java.util.Arrays;

import Reika.DragonAPI.Interfaces.Callbacks.EventWatchers;
import Reika.DragonAPI.Interfaces.Callbacks.EventWatchers.EventWatcher;
import Reika.DragonAPI.Libraries.Java.ReikaArrayHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;

public class EntityRenderEvent {
    private static EntityRenderWatcher[] listeners = null;

    public static void addListener(EntityRenderWatcher l) {
        listeners
            = ReikaArrayHelper.addToFastArray(listeners, l, EntityRenderWatcher.class);
        Arrays.sort(listeners, EventWatchers.comparator);
    }

    @SideOnly(Side.CLIENT)
    public static void fire(
        Render r, Entity e, double par2, double par4, double par6, float par8, float par9
    ) {
        if (listeners != null) {
            for (EntityRenderWatcher l : listeners) {
                if (l.tryRenderEntity(r, e, par2, par4, par6, par8, par9)) {
                    return;
                }
            }
        }
        r.doRender(e, par2, par4, par6, par8, par9);
    }

    public static interface EntityRenderWatcher extends EventWatcher {
        @SideOnly(Side.CLIENT)
        boolean tryRenderEntity(
            Render r,
            Entity e,
            double par2,
            double par4,
            double par6,
            float par8,
            float par9
        );
    }
}
