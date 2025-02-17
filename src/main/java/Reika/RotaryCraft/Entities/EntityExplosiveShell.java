/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.Entities;

import java.util.List;

import Reika.DragonAPI.Libraries.Registry.ReikaParticleHelper;
import Reika.DragonAPI.Libraries.World.ReikaWorldHelper;
import Reika.RotaryCraft.API.Interfaces.TargetEntity;
import Reika.RotaryCraft.Base.EntityRailgunShotBase;
import Reika.RotaryCraft.Items.ItemExplosiveShell;
import Reika.RotaryCraft.Registry.MachineRegistry;
import Reika.RotaryCraft.TileEntities.Weaponry.Turret.TileEntityRailGun;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityExplosiveShell extends EntityRailgunShotBase {
    public static final float EXPLOSION = 8F;

    public EntityExplosiveShell(World world) {
        super(world);
    }

    public EntityExplosiveShell(
        World world,
        double x,
        double y,
        double z,
        double vx,
        double vy,
        double vz,
        TileEntityRailGun r
    ) {
        super(
            world, x, y, z, 0, 0, 0, r, ItemExplosiveShell.ExplosiveRailGunAmmo.instance
        );
        motionX = vx;
        motionY = vy;
        motionZ = vz;
        //ReikaJavaLibrary.pConsole(vx+" , "+vy+" , "+vz);
        if (!world.isRemote)
            velocityChanged = true;
    }

    @Override
    public void onImpact(MovingObjectPosition mov) {
        if (mov != null
            && MachineRegistry.getMachine(worldObj, mov.blockX, mov.blockY, mov.blockZ)
                == MachineRegistry.RAILGUN) {
            this.setDead();
            return;
        }
        if (isDead)
            return;
        World world = worldObj;
        double x = posX;
        double y = posY;
        double z = posZ;
        int x0 = (int) x;
        int y0 = (int) y;
        int z0 = (int) z;
        //ReikaChatHelper.writeCoords(world, x, y, z);
        //ReikaChatHelper.writeBlockAtCoords(world, x0, y0, z0);
        world.newExplosion(this, x0, y0, z0, EXPLOSION, true, true);
        AxisAlignedBB box
            = AxisAlignedBB.getBoundingBox(x, y, z, x, y, z).expand(8, 8, 8);
        for (Entity e :
             ((List<Entity>) worldObj.getEntitiesWithinAABB(Entity.class, box))) {
            this.applyAttackEffectsToEntity(world, e);
        }
        this.setDead();
        //ent.attackEntityFrom(DamageSource.outOfWorld,
        //el.getHealth()*(1+el.getTotalArmorValue()));
    }

    @Override
    protected int getAttackDamage() {
        return 0;
    }

    @Override
    protected void applyAttackEffectsToEntity(World world, Entity el) {
        if (el instanceof TargetEntity) {
            ((TargetEntity) el).onRailgunImpact(gun, this.getType());
        }
    }

    @Override
    public void onUpdate() {
        ticksExisted++;
        boolean hit = false;
        int x = MathHelper.floor_double(posX);
        int y = MathHelper.floor_double(posY);
        int z = MathHelper.floor_double(posZ);
        Block id = worldObj.getBlock(x, y, z);
        MachineRegistry m = MachineRegistry.getMachine(worldObj, posX, posY, posZ);
        List mobs = worldObj.getEntitiesWithinAABB(
            EntityLivingBase.class, this.getBoundingBox().expand(1, 1, 1)
        );
        //ReikaJavaLibrary.pConsole("ID: "+id+" and "+mobs.size()+" mobs");
        hit
            = (mobs.size() > 0
               || (m != MachineRegistry.RAILGUN && id != Blocks.air
                   && !ReikaWorldHelper.softBlocks(worldObj, x, y, z)));
        //ReikaJavaLibrary.pConsole(hit+"   by "+id+"  or mobs "+mobs.size());
        if (hit) {
            //ReikaChatHelper.write("HIT  @  "+ticksExisted+"  by "+(mobs.size() > 0));
            this.onImpact(null);
            this.setDead();
            return;
        }

        if (!worldObj.isRemote
            && (shootingEntity != null && shootingEntity.isDead
                || !worldObj.blockExists(x, y, z)))
            this.setDead();
        else {
            if (ticksExisted > 80 && !worldObj.isRemote)
                this.onImpact(null);
            this.onEntityUpdate();
            Vec3 var15 = Vec3.createVectorHelper(posX, posY, posZ);
            Vec3 var2
                = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
            MovingObjectPosition var3 = worldObj.rayTraceBlocks(var15, var2);
            var15 = Vec3.createVectorHelper(posX, posY, posZ);
            var2
                = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
            if (var3 != null)
                var2 = Vec3.createVectorHelper(
                    var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord
                );
            Entity var4 = null;
            List var5 = worldObj.getEntitiesWithinAABBExcludingEntity(
                this,
                boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D)
            );
            double var6 = 0.0D;
            for (int var8 = 0; var8 < var5.size(); ++var8) {
                Entity var9 = (Entity) var5.get(var8);
                if (var9.canBeCollidedWith() && (!var9.isEntityEqual(shootingEntity))) {
                    float var10 = 0.3F;
                    AxisAlignedBB var11 = var9.boundingBox.expand(var10, var10, var10);
                    MovingObjectPosition var12 = var11.calculateIntercept(var15, var2);
                    if (var12 != null) {
                        double var13 = var15.distanceTo(var12.hitVec);
                        if (var13 < var6 || var6 == 0.0D) {
                            var4 = var9;
                            var6 = var13;
                        }
                    }
                }
            }
            if (var4 != null)
                var3 = new MovingObjectPosition(var4);
            if (var3 != null)
                this.onImpact(var3);
            posX += motionX;
            posY += motionY;
            posZ += motionZ;
            if (this.isInWater()) {
                worldObj.createExplosion(this, posX, posY, posZ, 3F, false);
                for (int var19 = 0; var19 < 4; ++var19) {
                    float var18 = 0.25F;
                    ReikaParticleHelper.BUBBLE.spawnAt(
                        worldObj,
                        posX - motionX * var18,
                        posY - motionY * var18,
                        posZ - motionZ * var18,
                        motionX,
                        motionY,
                        motionZ
                    );
                }
            }
            this.setPosition(posX, posY, posZ);
        }

        //ReikaJavaLibrary.pConsole(motionX);
    }
}
