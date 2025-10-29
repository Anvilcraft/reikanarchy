package Reika.RotaryCraft.API.Event;

import Reika.DragonAPI.Instantiable.Event.TileEntityEvent;
import net.minecraft.tileentity.TileEntity;

public class JetEngineEnterFailureEvent extends TileEntityEvent {
    public JetEngineEnterFailureEvent(TileEntity te) {
        super(te);
    }
}
