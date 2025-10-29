package Reika.RotaryCraft.API.Event;

import Reika.DragonAPI.Instantiable.Event.TileEntityEvent;
import net.minecraft.tileentity.TileEntity;

public class LightBridgePowerLossEvent extends TileEntityEvent {
    public LightBridgePowerLossEvent(TileEntity te) {
        super(te);
    }
}
