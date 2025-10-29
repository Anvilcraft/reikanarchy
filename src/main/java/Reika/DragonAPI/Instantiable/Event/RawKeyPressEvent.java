package Reika.DragonAPI.Instantiable.Event;

import Reika.DragonAPI.Auxiliary.Trackers.KeyWatcher.Key;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;

public class RawKeyPressEvent extends Event {
    public final Key key;
    public final EntityPlayer player;

    public RawKeyPressEvent(Key k, EntityPlayer ep) {
        key = k;
        player = ep;
    }
}
