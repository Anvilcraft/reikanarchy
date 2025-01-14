/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.Instantiable.Event;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.Event.HasResult;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

@HasResult
public class MobTargetingEvent extends Event {
    public final EntityPlayer player;
    public final boolean defaultResult;

    public final World world;
    public final double x;
    public final double y;
    public final double z;
    public final double range;

    /**
     * General purpose test-result control. Does nothing if a test is not run (eg
     * creative players w/o forcing)
     */
    public MobTargetingEvent(
        EntityPlayer ep, World world, double x, double y, double z, double r, boolean flag
    ) {
        player = ep;
        defaultResult = flag;

        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        range = r;
    }

    public static boolean fire(
        EntityPlayer ep, World world, double x, double y, double z, double r, boolean flag
    ) {
        MobTargetingEvent evt = new MobTargetingEvent(ep, world, x, y, z, r, flag);
        MinecraftForge.EVENT_BUS.post(evt);
        switch (evt.getResult()) {
            case ALLOW:
                return true;
            case DEFAULT:
            default:
                return evt.defaultResult;
            case DENY:
                return false;
        }
    }

    public static Result
    firePre(EntityPlayer ep, World world, double x, double y, double z, double r) {
        MobTargetingEvent.Pre evt = new MobTargetingEvent.Pre(ep, world, x, y, z, r);
        MinecraftForge.EVENT_BUS.post(evt);
        return evt.getResult();
    }

    public static EntityPlayer
    firePost(World world, double x, double y, double z, double r) {
        MobTargetingEvent.Post evt = new MobTargetingEvent.Post(world, x, y, z, r);
        MinecraftForge.EVENT_BUS.post(evt);
        return evt.forcedResult;
    }

    public static boolean
    fireAIPre(boolean orig, EntityAINearestAttackableTarget ai, Entity e) {
        if (e instanceof EntityPlayer) {
            EntityCreature src = ai.taskOwner;
            switch (firePre(
                (EntityPlayer) e,
                src.worldObj,
                src.posX,
                src.posY,
                src.posZ,
                Double.POSITIVE_INFINITY
            )) {
                case ALLOW:
                    return true;
                case DEFAULT:
                default:
                    return orig;
                case DENY:
                    return false;
            }
        }
        return orig;
    }

    public static EntityLivingBase
    fireAIPost(EntityLivingBase e, EntityAINearestAttackableTarget ai) {
        EntityCreature src = ai.taskOwner;
        EntityPlayer ep = firePost(
            src.worldObj, src.posX, src.posY, src.posZ, Double.POSITIVE_INFINITY
        );
        return ep != null ? ep : e;
    }

    @HasResult
    /** Use this to force a test, like for creative players, or to stop it entirely */
    public static class Pre extends MobTargetingEvent {
        public Pre(EntityPlayer ep, World world, double x, double y, double z, double r) {
            super(ep, world, x, y, z, r, !ep.capabilities.disableDamage);
        }
    }

    /** Use this to force a given result */
    public static class Post extends MobTargetingEvent {
        public EntityPlayer forcedResult = null;

        public Post(World world, double x, double y, double z, double r) {
            super(null, world, x, y, z, r, false);
        }
    }
}
