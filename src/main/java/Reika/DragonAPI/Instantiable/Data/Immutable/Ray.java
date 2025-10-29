package Reika.DragonAPI.Instantiable.Data.Immutable;

import Reika.DragonAPI.Libraries.MathSci.ReikaPhysicsHelper;
import net.minecraft.util.Vec3;

public class Ray {
    public final DecimalPosition origin;
    public final Vec3 directionStep;

    public Ray(DecimalPosition c, double dx, double dy, double dz) {
        this(c, Vec3.createVectorHelper(dx, dy, dz));
    }

    public static Ray fromPolar(DecimalPosition c, double theta, double phi) {
        double[] xyz = ReikaPhysicsHelper.polarToCartesian(1, theta, phi);
        return new Ray(c, xyz[0], xyz[1], xyz[2]);
    }

    public Ray(DecimalPosition c, Vec3 vec) {
        origin = c;
        directionStep = vec;
    }

    public DecimalPosition getScaledPosition(double d) {
        return origin.offset(
            directionStep.xCoord * d, directionStep.yCoord * d, directionStep.zCoord * d
        );
    }

    @Override
    public String toString() {
        return origin + " > " + directionStep.toString();
    }
}
