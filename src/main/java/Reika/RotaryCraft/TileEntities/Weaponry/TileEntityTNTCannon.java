/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.TileEntities.Weaponry;

import java.util.List;

import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.Libraries.MathSci.ReikaMathLibrary;
import Reika.DragonAPI.Libraries.MathSci.ReikaPhysicsHelper;
import Reika.DragonAPI.Libraries.Registry.ReikaItemHelper;
import Reika.DragonAPI.Libraries.ReikaInventoryHelper;
import Reika.RotaryCraft.API.Interfaces.CannonExplosive;
import Reika.RotaryCraft.API.Interfaces.CannonExplosive.ExplosiveEntity;
import Reika.RotaryCraft.Base.TileEntity.TileEntityLaunchCannon;
import Reika.RotaryCraft.Entities.EntityCustomTNT;
import Reika.RotaryCraft.Registry.MachineRegistry;
import Reika.RotaryCraft.RotaryCraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class TileEntityTNTCannon extends TileEntityLaunchCannon {
    public static final double gTNT
        = 7.5; //Calculated from EntityTNTPrimed; vy -= 0.04, *0.98, 20x a sec

    public static final double torquecap = 32768D;

    public int selectedFuse;

    //private final ArrayList<EntityTNTPrimed> fired = new ArrayList();

    //Make torque affect max incline angle, speed max distance

    @Override
    public int getMaxLaunchVelocity() {
        return (int) Math.sqrt(power / 67.5D);
    }

    @Override
    public int getMaxTheta() {
        if (torque > torquecap)
            return 90;
        int ang = 2 * (int) Math.ceil(Math.toDegrees(Math.asin(torque / torquecap)));
        if (ang > 90)
            return 90;
        return ang;
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return false;
    }

    @Override
    public double getMaxLaunchDistance() {
        double v = this.getMaxLaunchVelocity();
        double vy = v * Math.sin(Math.toRadians(45));
        double t = vy / 9.81D;
        return t * vy; //vx = vy @ 45
    }

    @Override
    public void updateEntity(World world, int x, int y, int z, int meta) {
        super.updateTileEntity();
        this.getSummativeSidedPower();

        if (DragonAPICore.debugtest)
            ReikaInventoryHelper.addToIInv(Blocks.tnt, this);

        if (power < MINPOWER)
            return;
        tickcount++;
        if (tickcount < this.getOperationTime())
            return;
        tickcount = 0;
        if (targetMode)
            this.calcTarget(world, x, y, z);
        int slot = this.canFire();
        if (slot >= 0)
            this.fire(world, x, y, z, slot);
        //this.syncTNTData(world, x, y, z);
        if (targetMode) {
            AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1)
                                    .expand(256, 256, 256);
            List<EntityTNTPrimed> in
                = world.getEntitiesWithinAABB(EntityTNTPrimed.class, box);
            for (EntityTNTPrimed tnt : in) {
                if (!tnt.onGround) {
                    //Nullify air resistance
                    tnt.motionX /= 0.869800000190734863D;
                    tnt.motionZ /= 0.869800000190734863D;
                    if (!world.isRemote)
                        tnt.velocityChanged = true;
                } else {
                    tnt.motionX = 0;
                    tnt.motionZ = 0;
                    if (!world.isRemote)
                        tnt.velocityChanged = true;
                }
            }
        }
    }

    /*
    private void syncTNTData(World world, int x, int y, int z) {
        if (!world.isRemote)
            return;
        Iterator<EntityTNTPrimed> it = fired.iterator();
        while (it.hasNext()) {
            EntityTNTPrimed tnt = it.next();
            if (tnt.ticksExisted < this.getMinFuse()) {
                //ReikaJavaLibrary.pConsole(tnt+":"+this.getSide());
                tnt.fuse = this.getFuseTime();
            }
            else {
                if (tnt.fuse < 0)
                    tnt.setDead();
                if (tnt.isDead)
                    it.remove();
            }
        }
    }*/

    private int getMinFuse() {
        return 5;
    }

    private void calcTarget(World world, int x, int y, int z) {
        double dx = target[0] - x - 0.5;
        double dy = target[1] - y - 1;
        double dz = target[2] - z - 0.5;
        double dl = ReikaMathLibrary.py3d(dx, 0, dz); //Horiz distance
        double g = 8.4695 * ReikaMathLibrary.doubpow(dl, 0.2701);
        if (dy > 0)
            g *= (0.8951 * ReikaMathLibrary.doubpow(dy, 0.0601));
        velocity = 10;
        theta = 0;
        while (theta <= 0) {
            velocity++;
            double s = ReikaMathLibrary.intpow(velocity, 4)
                - g * (g * dl * dl + 2 * dy * velocity * velocity);
            double a = velocity * velocity + Math.sqrt(s);
            theta = (int) Math.toDegrees(Math.atan(a / (g * dl)));
            phi = (int) Math.toDegrees(Math.atan2(dz, dx));
        }
    }

    protected int canFire() {
        for (int i = 0; i < inv.length; i++) {
            ItemStack is = inv[i];
            if (is != null) {
                if (ReikaItemHelper.matchStackWithBlock(is, Blocks.tnt))
                    return i;
                if (is.getItem() instanceof CannonExplosive)
                    return i;
            }
        }
        return -1;
    }

    @Override
    protected boolean fire(World world, int x, int y, int z, int slot) {
        ItemStack in = inv[slot];
        ReikaInventoryHelper.decrStack(slot, inv);
        world.playSoundEffect(
            x + 0.5,
            y + 0.5,
            z + 0.5,
            "random.explode",
            0.7F + 0.3F * rand.nextFloat() * 12,
            0.1F * rand.nextFloat()
        );
        world.spawnParticle("hugeexplosion", x + 0.5, y + 0.5, z + 0.5, 1.0D, 0.0D, 0.0D);
        double dx = x + 0.5;
        double dy = y + 1.5 - 0.0625;
        double dz = z + 0.5;
        int fuse = this.getFuseTime();

        Entity tnt = null;
        if (ReikaItemHelper.matchStackWithBlock(in, Blocks.tnt)) {
            tnt = new EntityCustomTNT(world, dx, dy, dz, null, fuse);
        } else if (in.getItem() instanceof CannonExplosive) {
            tnt = ((CannonExplosive) in.getItem()).getExplosiveEntity(in);
            tnt.setPosition(dx, dy, dz);
            ((ExplosiveEntity) tnt).setFuse(fuse);
        }
        if (tnt == null) {
            RotaryCraft.logger.logError(
                "Invalid item in TNT cannon yet firing was attempted!"
            );
            return false;
        }

        double[] xyz = ReikaPhysicsHelper.polarToCartesian(velocity / 20D, theta, phi);
        tnt.motionX = xyz[0];
        tnt.motionY = xyz[1];
        tnt.motionZ = xyz[2];
        if (!world.isRemote) {
            tnt.velocityChanged = true;
            world.spawnEntityInWorld(tnt);
        }
        //fired.add(tnt);
        return true;
    }

    private int getFuseTime() {
        return targetMode ? 50 : Math.max(this.getMinFuse(), selectedFuse);
    }

    @Override
    protected void readSyncTag(NBTTagCompound NBT) {
        super.readSyncTag(NBT);

        selectedFuse = NBT.getInteger("selfuse");
    }

    @Override
    protected void writeSyncTag(NBTTagCompound NBT) {
        super.writeSyncTag(NBT);

        NBT.setInteger("selfuse", selectedFuse);
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    protected void animateWithTick(World world, int x, int y, int z) {}

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public MachineRegistry getTile() {
        return MachineRegistry.TNTCANNON;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack is) {
        return ReikaItemHelper.matchStackWithBlock(is, Blocks.tnt);
    }

    @Override
    public int getRedstoneOverride() {
        if (this.canFire() == -1)
            return 15;
        return 0;
    }
}
