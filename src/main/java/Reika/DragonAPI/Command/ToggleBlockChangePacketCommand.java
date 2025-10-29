package Reika.DragonAPI.Command;

import Reika.DragonAPI.Extras.ChangePacketRenderer;
import net.minecraft.command.ICommandSender;

public class ToggleBlockChangePacketCommand extends DragonClientCommand {
    @Override
    public void processCommand(ICommandSender ics, String[] args) {
        ChangePacketRenderer.isActive = args.length > 0 && Boolean.parseBoolean(args[0]);
        ChangePacketRenderer.instance.clear();
    }

    @Override
    public String getCommandString() {
        return "chgpkt";
    }
}
