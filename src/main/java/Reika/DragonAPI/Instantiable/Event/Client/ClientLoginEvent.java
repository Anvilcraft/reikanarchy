package Reika.DragonAPI.Instantiable.Event.Client;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;

@SideOnly(Side.CLIENT)
public class ClientLoginEvent extends Event {
    public final EntityPlayer player;

    public final boolean newLogin;

    public ClientLoginEvent(EntityPlayer ep, boolean log) {
        player = ep;
        newLogin = log;
    }
}
