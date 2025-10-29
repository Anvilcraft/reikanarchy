package Reika.DragonAPI.Command;

import Reika.DragonAPI.Auxiliary.Trackers.DonatorController;
import Reika.DragonAPI.Auxiliary.Trackers.PatreonController;
import Reika.DragonAPI.Libraries.IO.ReikaChatHelper;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;

public class DonatorCommand extends DragonCommandBase {
    @Override
    public String getCommandString() {
        return "dragondonators";
    }

    @Override
    public void processCommand(ICommandSender ics, String[] args) {
        EntityPlayerMP ep = getCommandSenderAsPlayer(ics);
        ReikaChatHelper.sendChatToPlayer(
            ep,
            EnumChatFormatting.AQUA.toString(
            ) + "Thank you to all these people whose donations made the following mods possible:\n\n"
        );
        ReikaChatHelper.sendChatToPlayer(ep, DonatorController.instance.getDisplayList());
        ReikaChatHelper.sendChatToPlayer(
            ep,
            EnumChatFormatting.WHITE.toString()
                + "\n--------------------------------------\n"
        );
        ReikaChatHelper.sendChatToPlayer(ep, PatreonController.instance.getDisplayList());
    }

    @Override
    protected boolean isAdminOnly() {
        return false;
    }
}
