/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.Instantiable.ParticleController;

import Reika.DragonAPI.Interfaces.MotionController;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class EntityLockMotionController implements MotionController {
    private final Entity target;

    private final double damping;

    private final double acceleration;
    private double velocityXZ = 0;

    private double accelerationY = 0;
    private double maxVelocityY;
    private double velocityY;

    public EntityLockMotionController(Entity e, double axz, double vy, double damping) {
        target = e;
        this.damping = damping;
        acceleration = axz;
        maxVelocityY = vy;
        velocityY = maxVelocityY;
    }

    public void update(Entity e) {
        accelerationY = -1 * 0.125 * (e.posY - (target.posY - target.height / 2) - 0.5);
        velocityY += accelerationY;
        velocityY = MathHelper.clamp_double(velocityY, -maxVelocityY, maxVelocityY);
        maxVelocityY *= damping;
        velocityXZ += acceleration;
    }

    @Override
    public double getMotionX(Entity e) {
        return -(e.posX - target.posX) * velocityXZ / e.getDistanceToEntity(target);
    }

    @Override
    public double getMotionY(Entity e) {
        return -(e.posY - (target.posY - target.height / 2)) * velocityXZ
            / e.getDistanceToEntity(target);
    }

    @Override
    public double getMotionZ(Entity e) {
        return -(e.posZ - target.posZ) * velocityXZ / e.getDistanceToEntity(target);
    }
}
