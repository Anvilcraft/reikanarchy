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
import net.minecraftforge.common.MinecraftForge;

public class LightmapEvent extends Event {
    public static void fire() {
        MinecraftForge.EVENT_BUS.post(new LightmapEvent());
    }
}
