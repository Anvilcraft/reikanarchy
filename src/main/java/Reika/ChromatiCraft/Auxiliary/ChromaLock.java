/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Auxiliary;

import Reika.DragonAPI.Auxiliary.Trackers.PlayerHandler.PlayerTracker;
import Reika.DragonAPI.Libraries.IO.ReikaChatHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

public final class ChromaLock implements PlayerTracker {
    public static final ChromaLock instance = new ChromaLock();

    private static final String message = getMessage();

    private ChromaLock() {}

    private static String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(EnumChatFormatting.RED);
        sb.append("================= CHROMATICRAFT =======================\n");
        sb.append(EnumChatFormatting.RESET);
        sb.append(
            "NOTICE: It has been detected that third-party plugins are being used to disable parts of ChromatiCraft.\n"
        );
        sb.append(
            "This is frequently done to sell access to mod content, and is against the Terms of Use.\n"
        );
        sb.append(
            "All ChromatiCraft functionality is temporarily disabled, and will be restored if the disables are removed.\n"
        );
        sb.append("See your logs or contact Reika for further information.\n");
        sb.append(EnumChatFormatting.RED);
        sb.append("=====================================================");
        sb.append(EnumChatFormatting.RESET);
        return sb.toString();
    }

    @Override
    public void onPlayerLogin(EntityPlayer ep) {
        ReikaChatHelper.sendChatToPlayer(ep, message);
    }

    @Override
    public void onPlayerLogout(EntityPlayer player) {}

    @Override
    public void onPlayerChangedDimension(EntityPlayer player, int from, int to) {}

    @Override
    public void onPlayerRespawn(EntityPlayer player) {}
}
