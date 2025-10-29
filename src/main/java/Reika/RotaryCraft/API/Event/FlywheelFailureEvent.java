package Reika.RotaryCraft.API.Event;

import Reika.DragonAPI.Instantiable.Event.TileEntityEvent;
import net.minecraft.tileentity.TileEntity;

public class FlywheelFailureEvent extends TileEntityEvent {
    public final float explosivePower;

    public FlywheelFailureEvent(TileEntity te, float power) {
        super(te);

        explosivePower = power;
    }
}
