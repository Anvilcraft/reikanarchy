package Reika.DragonAPI.Instantiable.Data.Immutable;

import net.minecraftforge.common.util.ForgeDirection;

public final class ScaledDirection {
    public final int offsetX;
    public final int offsetY;
    public final int offsetZ;

    public final int distance;
    public final ForgeDirection direction;

    public ScaledDirection(ForgeDirection dir, int dist) {
        direction = dir;
        distance = dist;

        offsetX = dir.offsetX * dist;
        offsetY = dir.offsetY * dist;
        offsetZ = dir.offsetZ * dist;
    }
}
