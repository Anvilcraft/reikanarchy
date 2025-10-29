package Reika.DragonAPI.Interfaces.TileEntity;

import Reika.DragonAPI.Interfaces.Location;

public interface Connectable<L extends Location> extends BreakAction, SimpleConnection {
    public boolean isEmitting();

    public void reset();

    public void resetOther();

    public L getConnection();

    public boolean hasValidConnection();
}
