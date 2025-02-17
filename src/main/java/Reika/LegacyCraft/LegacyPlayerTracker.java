/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.LegacyCraft;

import Reika.DragonAPI.Auxiliary.Trackers.PlayerHandler.PlayerTracker;
import Reika.DragonAPI.Libraries.IO.ReikaSoundHelper;
import net.minecraft.entity.player.EntityPlayer;

public class LegacyPlayerTracker implements PlayerTracker {
    @Override
    public void onPlayerLogin(EntityPlayer ep) {}

    @Override
    public void onPlayerLogout(EntityPlayer player) {}

    @Override
    public void onPlayerChangedDimension(EntityPlayer player, int dimFrom, int dimTo) {
        if ((dimFrom == -1 || dimTo == -1)
            && dimFrom + dimTo == -1) { //one being Nether and one overworld
            ReikaSoundHelper.playSoundFromServer(
                player.worldObj,
                player.posX,
                player.posY,
                player.posZ,
                "portal.travel",
                0.55F,
                1,
                true
            );
        }
    }

    @Override
    public void onPlayerRespawn(EntityPlayer player) {}
}
