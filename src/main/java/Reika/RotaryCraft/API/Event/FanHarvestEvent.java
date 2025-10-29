package Reika.RotaryCraft.API.Event;

import Reika.DragonAPI.Instantiable.Event.TileEntityEvent;
import net.minecraft.tileentity.TileEntity;

public class FanHarvestEvent extends TileEntityEvent {
    public final int x;
    public final int y;
    public final int z;

    public FanHarvestEvent(TileEntity te, int x, int y, int z) {
        super(te);

        this.x = x;
        this.y = y;
        this.z = z;
    }
}
