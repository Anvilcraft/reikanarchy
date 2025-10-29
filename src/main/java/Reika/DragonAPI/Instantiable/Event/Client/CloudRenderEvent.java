package Reika.DragonAPI.Instantiable.Event.Client;

import Reika.DragonAPI.Exception.UnreachableCodeException;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.Event.HasResult;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.common.MinecraftForge;

@HasResult
public class CloudRenderEvent extends Event {
    public final GameSettings settings;
    public final boolean defaultResult;

    public CloudRenderEvent(GameSettings gs, boolean defaultValue) {
        settings = gs;
        defaultResult = defaultValue;
    }

    public static boolean fireNoRedirect(GameSettings gs) {
        return fire(gs, gs.renderDistanceChunks >= 4 && gs.clouds);
    }

    public static boolean fire(GameSettings gs) {
        return fire(gs, gs.shouldRenderClouds());
    }

    private static boolean fire(GameSettings gs, boolean defaultValue) {
        CloudRenderEvent evt = new CloudRenderEvent(gs, defaultValue);
        MinecraftForge.EVENT_BUS.post(evt);
        switch (evt.getResult()) {
            case ALLOW:
                return true;
            case DEFAULT:
                return defaultValue;
            case DENY:
                return false;
        }
        throw new UnreachableCodeException(evt.getResult());
    }
}
