package Reika.DragonAPI.Instantiable.Event.Client;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.client.audio.ISound;
import net.minecraftforge.common.MinecraftForge;

public class SoundAttenuationDistanceEvent extends Event {
    public final ISound sound;
    public final float originalDistance;

    public float distance;

    public SoundAttenuationDistanceEvent(ISound s, float d) {
        sound = s;
        originalDistance = d;
        distance = originalDistance;
    }

    public static float fire(float d, ISound s) {
        SoundAttenuationDistanceEvent evt = new SoundAttenuationDistanceEvent(s, d);
        MinecraftForge.EVENT_BUS.post(evt);
        return evt.distance;
    }
}
