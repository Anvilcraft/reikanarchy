package Reika.RotaryCraft.API.Event;

import Reika.DragonAPI.Instantiable.Event.TileEntityEvent;
import net.minecraft.tileentity.TileEntity;

public class VacuumXPAbsorbEvent extends TileEntityEvent {
    public final int amount;

    public VacuumXPAbsorbEvent(TileEntity te, int xp) {
        super(te);
        amount = xp;
    }
}
