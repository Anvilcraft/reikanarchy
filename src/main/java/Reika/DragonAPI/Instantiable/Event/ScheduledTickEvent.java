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

import Reika.DragonAPI.APIPacketHandler.PacketIDs;
import Reika.DragonAPI.DragonAPIInit;
import Reika.DragonAPI.Instantiable.Data.Immutable.BlockKey;
import Reika.DragonAPI.Instantiable.Data.Immutable.DecimalPosition;
import Reika.DragonAPI.Instantiable.Data.Immutable.WorldLocation;
import Reika.DragonAPI.Instantiable.Data.Maps.TimerMap.FreezableTimer;
import Reika.DragonAPI.Instantiable.IO.PacketTarget;
import Reika.DragonAPI.Interfaces.Registry.SoundEnum;
import Reika.DragonAPI.Libraries.IO.ReikaPacketHelper;
import Reika.DragonAPI.Libraries.IO.ReikaSoundHelper;
import Reika.DragonAPI.Libraries.ReikaEntityHelper;
import Reika.DragonAPI.Libraries.World.ReikaWorldHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public final class ScheduledTickEvent extends Event implements FreezableTimer {
    private final ScheduledEvent action;

    public ScheduledTickEvent(ScheduledEvent evt) {
        action = evt;
    }

    public final void fire() {
        MinecraftForge.EVENT_BUS.post(this);
        action.fire();
    }

    public final boolean runOnSide(Side s) {
        return action.runOnSide(s);
    }

    public final boolean isFrozen() {
        return action instanceof DelayableSchedulableEvent
            && !((DelayableSchedulableEvent) action).canTick();
    }

    /*
    public static class ScheduledPlayerEvent implements ScheduledEvent {

        private final EntityPlayer player;

        public ScheduledPlayerEvent(EntityPlayer ep) {
            player = ep;
        }

        @Override
        public void fire() {

        }

    }
     */
    public static class ScheduledSoundEvent implements ScheduledEvent {
        private final SoundEnum sound;
        private final float volume;
        private final float pitch;

        private World world;
        private double posX;
        private double posY;
        private double posZ;

        private Entity entity;

        public boolean attenuate = true;
        public int broadcastRange = 64;

        private ScheduledSoundEvent(SoundEnum s, float v, float p) {
            sound = s;
            volume = v;
            pitch = p;
        }

        public ScheduledSoundEvent(SoundEnum s, Entity e, float v, float p) {
            this(s, v, p);
            entity = e;
        }

        public ScheduledSoundEvent(
            SoundEnum s, World w, double x, double y, double z, float v, float p
        ) {
            this(s, v, p);
            world = w;
            posX = x;
            posY = y;
            posZ = z;
        }

        @Override
        public void fire() {
            if (attenuate)
                sound.playSound(
                    this.getWorld(),
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    volume,
                    pitch,
                    true
                );
            else
                sound.playSoundNoAttenuation(
                    this.getWorld(),
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    volume,
                    pitch,
                    broadcastRange
                );
        }

        protected Entity getEntity() {
            return entity;
        }

        private World getWorld() {
            return entity != null ? entity.worldObj : world;
        }

        private double getX() {
            return entity != null ? entity.posX : posX;
        }

        private double getY() {
            return entity != null ? entity.posY : posY;
        }

        private double getZ() {
            return entity != null ? entity.posZ : posZ;
        }

        @Override
        public boolean runOnSide(Side s) {
            return s == Side.SERVER;
        }
    }

    public static class ScheduledSoundEventClient implements ScheduledEvent {
        private final SoundEnum sound;
        private final float volume;
        private final float pitch;

        private World world;
        private double posX;
        private double posY;
        private double posZ;

        private Entity entity;

        public boolean attenuate = true;

        private ScheduledSoundEventClient(SoundEnum s, float v, float p) {
            sound = s;
            volume = v;
            pitch = p;
        }

        public ScheduledSoundEventClient(SoundEnum s, Entity e, float v, float p) {
            this(s, v, p);
            entity = e;
        }

        public ScheduledSoundEventClient(
            SoundEnum s, World w, double x, double y, double z, float v, float p
        ) {
            this(s, v, p);
            world = w;
            posX = x;
            posY = y;
            posZ = z;
        }

        @Override
        public void fire() {
            ReikaSoundHelper.playClientSound(
                sound, this.getX(), this.getY(), this.getZ(), volume, pitch, attenuate
            );
        }

        protected Entity getEntity() {
            return entity;
        }

        private double getX() {
            return entity != null ? entity.posX : posX;
        }

        private double getY() {
            return entity != null ? entity.posY : posY;
        }

        private double getZ() {
            return entity != null ? entity.posZ : posZ;
        }

        @Override
        public boolean runOnSide(Side s) {
            return s == Side.CLIENT;
        }
    }

    public static class DelayedAttack implements ScheduledEvent {
        private final EntityLivingBase target;
        private final DamageSource source;
        private final float amount;

        public DelayedAttack(EntityLivingBase e, DamageSource src, float amt) {
            target = e;
            source = src;
            amount = amt;
        }

        @Override
        public void fire() {
            target.attackEntityFrom(source, amount);
        }

        @Override
        public boolean runOnSide(Side s) {
            return s == Side.SERVER;
        }
    }

    public static class DelayedKnockback implements ScheduledEvent {
        private final EntityLivingBase target;
        private final DecimalPosition position;
        private final double amount;
        private final double exponent;

        public DelayedKnockback(EntityLivingBase e, DecimalPosition from, double amt) {
            this(e, from, amt, 0);
        }

        public DelayedKnockback(
            EntityLivingBase e, DecimalPosition from, double amt, double exp
        ) {
            target = e;
            amount = amt;
            position = from;
            exponent = exp;
        }

        @Override
        public void fire() {
            ReikaEntityHelper.knockbackEntityFromPos(
                position.xCoord,
                position.yCoord,
                position.zCoord,
                target,
                amount,
                exponent
            );
        }

        @Override
        public boolean runOnSide(Side s) {
            return s == Side.SERVER;
        }
    }

    public static class ScheduledBlockPlace implements ScheduledEvent {
        private final BlockKey block;
        private final WorldLocation location;

        public ScheduledBlockPlace(World world, int x, int y, int z, Block b) {
            this(world, x, y, z, b, 0);
        }

        public ScheduledBlockPlace(World world, int x, int y, int z, Block b, int meta) {
            block = new BlockKey(b, meta);
            location = new WorldLocation(world, x, y, z);
        }

        @Override
        public void fire() {
            location.setBlock(block.blockID, block.metadata);
            World world = location.getWorld();
            int x = location.xCoord;
            int y = location.yCoord;
            int z = location.zCoord;
            ReikaPacketHelper.sendDataPacketWithRadius(
                DragonAPIInit.packetChannel,
                PacketIDs.BREAKPARTICLES.ordinal(),
                world,
                x,
                y,
                z,
                128,
                Block.getIdFromBlock(block.blockID),
                block.metadata
            );
            ReikaSoundHelper.playBreakSound(world, x, y, z, block.blockID);
        }

        @Override
        public boolean runOnSide(Side s) {
            return s == Side.SERVER;
        }
    }

    public static class ScheduledBlockBreak implements ScheduledEvent {
        private final WorldLocation location;

        public ScheduledBlockBreak(WorldLocation loc) {
            location = loc;
        }

        @Override
        public void fire() {
            ReikaWorldHelper.dropAndDestroyBlockAt(
                location.getWorld(),
                location.xCoord,
                location.yCoord,
                location.zCoord,
                null,
                true,
                true
            );
        }

        @Override
        public boolean runOnSide(Side s) {
            return s == Side.SERVER;
        }
    }

    public static class ScheduledEntitySpawn<E extends Entity> implements ScheduledEvent {
        protected final E entity;

        public ScheduledEntitySpawn(E e) {
            entity = e;
        }

        @Override
        public void fire() {
            entity.worldObj.spawnEntityInWorld(entity);
        }

        @Override
        public boolean runOnSide(Side s) {
            return s == Side.SERVER;
        }
    }

    public static final class ScheduledPacket implements ScheduledEvent {
        public final String channel;
        public final int packetID;
        private final PacketTarget target;
        private final int[] data;

        public ScheduledPacket(
            String ch, int id, World world, int x, int y, int z, int r, int... data
        ) {
            this(
                ch,
                id,
                new PacketTarget.RadiusTarget(world, x + 0.5, y + 0.5, z + 0.5, r),
                data
            );
        }

        public ScheduledPacket(String ch, int id, PacketTarget pt, int... data) {
            channel = ch;
            packetID = id;
            target = pt;
            this.data = data;
        }

        @Override
        public void fire() {
            ReikaPacketHelper.sendDataPacket(channel, packetID, target, data);
        }

        @Override
        public boolean runOnSide(Side s) {
            return s == Side.SERVER;
        }
    }

    public static final class ScheduledStringPacket implements ScheduledEvent {
        public final String channel;
        public final int packetID;
        private final PacketTarget target;
        private final String data;

        public ScheduledStringPacket(String ch, int id, PacketTarget pt, String data) {
            channel = ch;
            packetID = id;
            target = pt;
            this.data = data;
        }

        @Override
        public void fire() {
            ReikaPacketHelper.sendStringPacket(channel, packetID, data, target);
        }

        @Override
        public boolean runOnSide(Side s) {
            return s == Side.SERVER;
        }
    }

    public static interface ScheduledEvent {
        public abstract void fire();

        public abstract boolean runOnSide(Side s);
    }

    public static interface DelayableSchedulableEvent extends ScheduledEvent {
        public boolean canTick();
    }

    @Override
    public void call() {
        if (action.runOnSide(FMLCommonHandler.instance().getEffectiveSide()))
            this.fire();
    }
}
