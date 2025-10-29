package Reika.DragonAPI.Command;

import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.Libraries.IO.ReikaChatHelper;
import net.minecraft.command.ICommandSender;

public class TestControlCommand extends DragonCommandBase {
    private final String tag = "debugtest";

    @Override
    public void processCommand(ICommandSender ics, String[] args) {
        DragonAPICore.debugtest = !DragonAPICore.debugtest;
        ReikaChatHelper.sendChatToAllOnServer(
            "Debug Test Mode: " + DragonAPICore.debugtest
        );
    }

    @Override
    public String getCommandString() {
        return tag;
    }

    @Override
    protected boolean isAdminOnly() {
        return true;
    }
}
