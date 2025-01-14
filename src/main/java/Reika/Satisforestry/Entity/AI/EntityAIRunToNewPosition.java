package Reika.Satisforestry.Entity.AI;

import Reika.DragonAPI.Libraries.Java.ReikaRandomHelper;
import Reika.DragonAPI.Libraries.MathSci.ReikaPhysicsHelper;
import Reika.Satisforestry.Entity.EntitySpitter;
import net.minecraft.util.Vec3;

public class EntityAIRunToNewPosition extends EntityAISpitterReposition {
    public EntityAIRunToNewPosition(EntitySpitter e) {
        super(e, 0, e.getSpitterType().getPursuitDistance() * 0.9);
    }

    @Override
    protected Vec3 getTargetPosition() {
        //return RandomPositionGenerator.findRandomTarget(entity, 4, 3);
        double dist = ReikaRandomHelper.getRandomBetween(
            Math.sqrt(maxDistSq) - 4, Math.sqrt(maxDistSq) - 1, entity.getRNG()
        );
        double[] xyz = ReikaPhysicsHelper.polarToCartesianFast(
            dist, 0, entity.getRNG().nextDouble() * 360
        );
        return Vec3.createVectorHelper(
            attackTarget.posX + xyz[0],
            attackTarget.posY + xyz[1],
            attackTarget.posZ + xyz[2]
        );
    }

    @Override
    protected boolean needsTargetPosition() {
        return true;
    }

    @Override
    protected boolean isRunning() {
        return true;
    }

    @Override
    protected double getStoppingDistance() {
        return 0.5;
    }
}