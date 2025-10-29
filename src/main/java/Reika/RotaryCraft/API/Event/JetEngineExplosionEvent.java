package Reika.RotaryCraft.API.Event;

import Reika.DragonAPI.Instantiable.Event.TileEntityEvent;
import net.minecraft.tileentity.TileEntity;

public class JetEngineExplosionEvent extends TileEntityEvent {
    public JetEngineExplosionEvent(TileEntity te) {
        super(te);
    }
}
