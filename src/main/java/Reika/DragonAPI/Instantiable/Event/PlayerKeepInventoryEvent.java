package Reika.DragonAPI.Instantiable.Event;

import cpw.mods.fml.common.eventhandler.Event.HasResult;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;

@HasResult
public class PlayerKeepInventoryEvent extends PlayerEvent {
    public final EntityPlayer copy;

    public PlayerKeepInventoryEvent(EntityPlayer player, EntityPlayer cp) {
        super(player);
        copy = cp;
    }

    public static boolean fire(EntityPlayer ep) {
        return fire(ep, ep);
    }

    public static boolean fire(EntityPlayer copy, EntityPlayer ep) {
        PlayerKeepInventoryEvent evt = new PlayerKeepInventoryEvent(ep, copy);
        MinecraftForge.EVENT_BUS.post(evt);
        switch (evt.getResult()) {
            case ALLOW:
                return true;
            case DENY:
                return false;
            case DEFAULT:
            default:
                return ep.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory"
                );
        }
    }
}
