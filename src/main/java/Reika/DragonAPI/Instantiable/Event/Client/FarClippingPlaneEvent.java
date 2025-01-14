/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
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
