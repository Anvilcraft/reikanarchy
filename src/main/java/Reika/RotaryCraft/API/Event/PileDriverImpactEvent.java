package Reika.RotaryCraft.API.Event;

import Reika.DragonAPI.Instantiable.Event.TileEntityEvent;
import net.minecraft.tileentity.TileEntity;

public class PileDriverImpactEvent extends TileEntityEvent {
    public final int centerX;
    public final int centerY;
    public final int centerZ;

    public PileDriverImpactEvent(TileEntity te, int x, int y, int z) {
        super(te);

        centerX = x;
        centerY = y;
        centerZ = z;
    }
}
