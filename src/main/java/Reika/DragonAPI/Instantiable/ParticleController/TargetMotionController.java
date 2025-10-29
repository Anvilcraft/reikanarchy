package Reika.DragonAPI.Instantiable.ParticleController;

import Reika.DragonAPI.Interfaces.MotionController;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

public class TargetMotionController implements MotionController {
    public final double targetX;
    public final double targetY;
    public final double targetZ;

    private final double force;

    public TargetMotionController(TileEntity te, double f) {
        this(te.xCoord + 0.5, te.yCoord + 0.5, te.zCoord + 0.5, f);
    }

    public TargetMotionController(double x, double y, double z, double f) {
        targetX = x;
        targetY = y;
        targetZ = z;
        force = f;
    }

    public void update(Entity e) {}

    @Override
    public double getMotionX(Entity e) {
        return e.motionX + force * (targetX - e.posX);
    }

    @Override
    public double getMotionY(Entity e) {
        return e.motionY + force * (targetY - e.posY);
    }

    @Override
    public double getMotionZ(Entity e) {
        return e.motionZ + force * (targetZ - e.posZ);
    }
}
