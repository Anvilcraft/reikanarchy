/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.Command;

import java.util.List;

import Reika.DragonAPI.Libraries.ReikaNBTHelper;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerNBTCommand extends DragonCommandBase {
    @Override
    public void processCommand(ICommandSender ics, String[] args) {
        EntityPlayer caller = this.getCommandSenderAsPlayer(ics);
        EntityPlayer ep
            = args.length == 0 ? caller : caller.worldObj.getPlayerEntityByName(args[0]);
        this.sendChatToSender(ics, "NBT data for " + ep.getCommandSenderName() + ":");
        List<String> li = ReikaNBTHelper.parseNBTAsLines(ep.getEntityData());
        for (String s : li)
            this.sendChatToSender(ics, s);
    }

    @Override
    public String getCommandString() {
        return "playernbt";
    }

    @Override
    protected boolean isAdminOnly() {
        return false;
    }
}
