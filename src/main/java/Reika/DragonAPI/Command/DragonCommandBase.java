package Reika.DragonAPI.Command;

import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.Libraries.IO.ReikaChatHelper;
import Reika.DragonAPI.Libraries.Java.ReikaJavaLibrary;
import Reika.DragonAPI.Libraries.ReikaPlayerAPI;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;

public abstract class DragonCommandBase extends CommandBase {
    public abstract String getCommandString();

    @Override
    public final String getCommandName() {
        return this.getCommandString();
    }

    @Override
    public final String getCommandUsage(ICommandSender icommandsender) {
        return "/" + this.getCommandString();
    }

    protected static final void sendChatToSender(ICommandSender ics, String s) {
        if (ics instanceof EntityPlayerMP)
            ReikaChatHelper.sendChatToPlayer(getCommandSenderAsPlayer(ics), s);
        else if (ics instanceof EntityPlayer)
            ReikaChatHelper.sendChatToPlayer((EntityPlayer) ics, s);
        else if (ics instanceof CommandBlockLogic)
            ((CommandBlockLogic) ics).addChatMessage(new ChatComponentTranslation(s));
        else
            ReikaJavaLibrary.pConsole(s);
    }

    @Override
    public final int getRequiredPermissionLevel() {
        return this.isAdminOnly() ? 4 : 0;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        if (DragonAPICore.isSinglePlayer())
            return true;
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP ep = (EntityPlayerMP) sender;
            return !this.isAdminOnly() || ReikaPlayerAPI.isAdmin(ep);
        }
        return super.canCommandSenderUseCommand(sender);
    }

    protected abstract boolean isAdminOnly();
}
