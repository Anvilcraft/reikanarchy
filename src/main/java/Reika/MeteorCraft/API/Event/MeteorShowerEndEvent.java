/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.MeteorCraft.API.Event;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.world.World;

public class MeteorShowerEndEvent extends Event {
    public final World world;

    public final long duration;

    public MeteorShowerEndEvent(World world, long dura) {
        this.world = world;
        duration = dura;
    }
}
