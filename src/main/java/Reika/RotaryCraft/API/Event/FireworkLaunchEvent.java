package Reika.RotaryCraft.API.Event;

import Reika.DragonAPI.Instantiable.Event.TileEntityEvent;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.tileentity.TileEntity;

public class FireworkLaunchEvent extends TileEntityEvent {
    public final EntityFireworkRocket entity;

    public FireworkLaunchEvent(TileEntity te, EntityFireworkRocket fw) {
        super(te);

        entity = fw;
    }
}
