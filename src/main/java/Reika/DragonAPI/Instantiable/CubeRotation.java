package Reika.DragonAPI.Instantiable;

import java.util.Random;

public class CubeRotation {
    public double angX;
    public double angY;
    public double angZ;

    public double rvX;
    public double rvY;
    public double rvZ;

    public CubeRotation() {}

    public CubeRotation randomize(Random rand) {
        angX = rand.nextInt(360);
        angY = rand.nextInt(360);
        angZ = rand.nextInt(360);
        return this;
    }
}
