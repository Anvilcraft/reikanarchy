package Reika.DragonAPI.Instantiable.Data.Immutable;

import Reika.DragonAPI.Libraries.ReikaDirectionHelper.CubeDirections;

public final class ScaledCubeDirection {
    public final int offsetX;
    public final int offsetZ;

    public final int distance;
    public final CubeDirections direction;

    public ScaledCubeDirection(CubeDirections dir, int dist) {
        direction = dir;
        distance = dist;

        offsetX = dir.directionX * dist;
        offsetZ = dir.directionZ * dist;
    }
}
