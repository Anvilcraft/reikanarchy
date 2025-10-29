package Reika.RotaryCraft.API.Event;

import Reika.DragonAPI.Instantiable.Event.TileEntityEvent;
import Reika.DragonAPI.Libraries.MathSci.ReikaMathLibrary;
import net.minecraft.tileentity.TileEntity;

public class NoteEvent extends TileEntityEvent {
    public final int notePitch;
    public final int noteChannel;
    public final int length;

    public NoteEvent(TileEntity te, int pitch, int length, int channel) {
        super(te);
        noteChannel = channel;
        notePitch = pitch;
        this.length = length;
    }

    public double getDistanceTo(TileEntity te) {
        return ReikaMathLibrary.py3d(
            te.xCoord - this.getTileX(),
            te.yCoord - this.getTileY(),
            te.zCoord - this.getTileZ()
        );
    }
}
