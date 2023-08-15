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

import Reika.DragonAPI.Instantiable.Data.Immutable.DecimalPosition;
import Reika.DragonAPI.Instantiable.Orbit;
import Reika.DragonAPI.Interfaces.PositionController;
import net.minecraft.entity.Entity;

public class OrbitMotionController implements PositionController {
    private double originX;
    private double originY;
    private double originZ;

    public final Orbit orbit;

    private double theta;
    public double thetaSpeed = 1;

    private DecimalPosition position;

    private Entity trackedEntity;

    public OrbitMotionController(Orbit o, double x, double y, double z) {
        orbit = o;
        originX = x;
        originY = y;
        originZ = z;
        position = new DecimalPosition(x, y, z);
        position = orbit.getPosition(originX, originY, originZ, theta);
    }

    public OrbitMotionController trackEntity(Entity e) {
        trackedEntity = e;
        originX = e.posX;
        originY = e.posY;
        originZ = e.posZ;
        position = orbit.getPosition(originX, originY, originZ, theta);
        return this;
    }

    @Override
    public void update(Entity e) {
        if (trackedEntity != null) {
            originX = trackedEntity.posX;
            originY = trackedEntity.posY;
            originZ = trackedEntity.posZ;
        }
        //ReikaJavaLibrary.pConsole(this.hashCode()+": "+new DecimalPosition(originX,
        //originY, originZ).equals(new DecimalPosition(trackedEntity))+": "+new
        //DecimalPosition(originX, originY, originZ)+","+new
        //DecimalPosition(trackedEntity));
        position = orbit.getPosition(originX, originY, originZ, theta);
        theta += thetaSpeed;
    }

    @Override
    public double getPositionX(Entity e) {
        return position.xCoord;
    }

    @Override
    public double getPositionY(Entity e) {
        return position.yCoord;
    }

    @Override
    public double getPositionZ(Entity e) {
        return position.zCoord;
    }
}
