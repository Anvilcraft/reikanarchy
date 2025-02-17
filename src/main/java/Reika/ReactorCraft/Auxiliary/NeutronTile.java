/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ReactorCraft.Auxiliary;

import Reika.ReactorCraft.Entities.EntityNeutron;
import net.minecraft.world.World;

public interface NeutronTile {
    public abstract boolean onNeutron(EntityNeutron e, World world, int x, int y, int z);
}
