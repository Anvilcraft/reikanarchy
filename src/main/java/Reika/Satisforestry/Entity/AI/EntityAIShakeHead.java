package Reika.Satisforestry.Entity.AI;

import Reika.Satisforestry.Entity.EntitySpitter;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigate;

public class EntityAIShakeHead extends EntityAIBase {
    private EntitySpitter spitter;
    private PathNavigate pathfinder;

    public EntityAIShakeHead(EntitySpitter e) {
        spitter = e;
        pathfinder = e.getNavigator();
        this.setMutexBits(8);
    }

    @Override
    public boolean shouldExecute() {
        return this.doCheck(true);
    }

    private boolean doCheck(boolean start) {
        return spitter.getEntityToAttack() == null && spitter.onGround
            && (start ? spitter.canInitiateHeadshake() : spitter.canContinueHeadshake());
    }

    @Override
    public boolean continueExecuting() {
        return this.doCheck(false);
    }

    @Override
    public void startExecuting() {
        //ReikaJavaLibrary.pConsole(spitter.getSpitterType()+" "+new
        //DecimalPosition(spitter)+" executing "+this);
        pathfinder.clearPathEntity();
        spitter.initiateHeadShake();
    }

    @Override
    public void resetTask() {
        pathfinder.clearPathEntity();
    }

    @Override
    public void updateTask() {
        pathfinder.clearPathEntity();
        //int shake = spitter.getHeadshake();
        //ReikaJavaLibrary.pConsole(spitter.getSpitterType()+" "+new
        //DecimalPosition(spitter)+" ticking "+this);
    }
}