package Reika.DragonAPI.Instantiable.Event.Client;

import cpw.mods.fml.common.eventhandler.Event;

public class FarClippingPlaneEvent extends Event {
    public float farClippingPlaneDistance;

    public final float partialTickTime;
    public final int unknownParamInt;

    public FarClippingPlaneEvent(float ptick, int par2, float plane) {
        partialTickTime = ptick;
        unknownParamInt = par2;
        farClippingPlaneDistance = plane;
    }
}
