/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.TileEntities.Weaponry.Turret;

import java.util.List;

import Reika.DragonAPI.Libraries.IO.ReikaSoundHelper;
import Reika.DragonAPI.Libraries.MathSci.ReikaMathLibrary;
import Reika.DragonAPI.Libraries.Registry.ReikaItemHelper;
import Reika.DragonAPI.Libraries.ReikaEntityHelper;
import Reika.DragonAPI.Libraries.ReikaInventoryHelper;
import Reika.DragonAPI.Libraries.World.ReikaWorldHelper;
import Reika.DragonAPI.ModRegistry.InterfaceCache;
import Reika.RotaryCraft.API.Interfaces.FlyingMob;
import Reika.RotaryCraft.API.Interfaces.TargetEntity;
import Reika.RotaryCraft.Auxiliary.ItemStacks;
import Reika.RotaryCraft.Base.TileEntity.TileEntityInventoriedCannon;
import Reika.RotaryCraft.Entities.EntityFlakShot;
import Reika.RotaryCraft.Registry.MachineRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class TileEntityAAGun
    extends TileEntityInventoriedCannon implements ISidedInventory {
    @Override
    public int getRange() {
        return 128;
    }

    @Override
    public int getMaxRange() {
        return 128;
    }

    @Override
    public boolean hasAmmo() {
        return ReikaInventoryHelper.checkForItemStack(ItemStacks.scrap, inv, false);
    }

    @Override
    protected float getAimingSpeed() {
        return 2;
    }

    @Override
    public void updateEntity(World world, int x, int y, int z, int meta) {
        super.updateTileEntity();
        if (power < MINPOWER || torque < MINTORQUE)
            return;
        if (!this.hasAmmo())
            return;
        if (!this.isAimingAtTarget(world, x, y, z, target))
            return;
        if (tickcount < this.getOperationTime())
            return;
        //ReikaJavaLibrary.pConsole(Arrays.toString(target), Side.SERVER);
        tickcount = 0;
        if (target[3] == 1) {
            this.fire(world, target);
        }
    }

    @Override
    public int getOperationTime() {
        return 6;
    }

    @Override
    protected double[] getTarget(World world, int x, int y, int z) {
        double[] xyzb = new double[4];
        int r = this.getRange();
        AxisAlignedBB range = AxisAlignedBB.getBoundingBox(
            x - r, y - r, z - r, x + 1 + r, y + 1 + r, z + 1 + r
        );
        List<Entity> inrange = world.getEntitiesWithinAABB(Entity.class, range);
        double mindist = this.getRange() + 2;
        Entity i_at_min = null;
        for (Entity ent : inrange) {
            double dist = ReikaMathLibrary.py3d(
                ent.posX - x - 0.5, ent.posY - y - 0.5, ent.posZ - z - 0.5
            );
            if (this.isValidTarget(ent)) {
                if (ReikaWorldHelper.canBlockSee(
                        world, x, y, z, ent.posX, ent.posY, ent.posZ, this.getRange()
                    )) {
                    double dy = -(ent.posY - y);
                    double reqtheta
                        = -90 + Math.toDegrees(Math.abs(Math.acos(dy / dist)));
                    if ((reqtheta <= dir * MAXLOWANGLE && dir == -1)
                        || (reqtheta >= dir * MAXLOWANGLE && dir == 1)) {
                        if (dist < mindist) {
                            mindist = dist;
                            i_at_min = ent;
                        }
                    }
                }
            }
        }
        if (i_at_min == null)
            return xyzb;
        closestMob = i_at_min;
        xyzb[0] = closestMob.posX + this.randomOffset();
        xyzb[1]
            = closestMob.posY + closestMob.getEyeHeight() * 0.25 + this.randomOffset();
        xyzb[2] = closestMob.posZ + this.randomOffset();
        xyzb[3] = 1;
        return xyzb;
    }

    @Override
    public void fire(World world, double[] xyz) {
        ReikaSoundHelper.playSoundAtBlock(
            world, xCoord, yCoord, zCoord, "random.explode", 1F, 1.3F
        );
        ReikaSoundHelper.playSoundAtBlock(
            world, xCoord, yCoord, zCoord, "random.explode", 1F, 0.5F
        );

        if (rand.nextBoolean()) {
            int slot
                = ReikaInventoryHelper.locateInInventory(ItemStacks.scrap, inv, false);
            ReikaInventoryHelper.decrStack(slot, inv);
        }

        double speed = 2;
        double[] v = new double[3];
        v[0] = xyz[0] - xCoord;
        v[1] = xyz[1] - yCoord;
        v[2] = xyz[2] - zCoord;
        double dd = ReikaMathLibrary.py3d(v[0], v[1], v[2]);
        for (int i = 0; i < 3; i++)
            v[i] /= dd;
        for (int i = 0; i < 3; i++)
            v[i] *= speed;
        dd = ReikaMathLibrary.py3d(v[0], v[1], v[2]);
        double dx = v[0] / dd;
        double dy = v[1] / dd;
        double dz = v[2] / dd;

        //ReikaJavaLibrary.pConsole(dx+"  "+dy+"  "+dz);
        if (!world.isRemote) {
            double y = this.getFiringPositionY(dy);
            EntityFlakShot flak = new EntityFlakShot(
                world,
                xCoord + 0.5 + dx,
                y,
                zCoord + 0.5 + dz,
                3 * v[0],
                3 * v[1],
                3 * v[2],
                this
            );
            world.spawnEntityInWorld(flak);
        }
    }

    @Override
    protected double randomOffset() {
        return -1;
    }

    @Override
    protected boolean isValidTarget(Entity ent) {
        if (ent instanceof TargetEntity)
            return ((TargetEntity) ent).shouldTarget(this, placerUUID);
        if (!(ent instanceof EntityLivingBase))
            return false;
        EntityLivingBase elb = (EntityLivingBase) ent;
        if (elb.isDead || elb.getHealth() <= 0)
            return false;
        if (ent.onGround || ent.isInWater() || ent.isInsideOfMaterial(Material.lava))
            return false;
        if (elb instanceof EntityFlying && ReikaEntityHelper.isHostile(elb)) {
            return ReikaMathLibrary.py3d(
                       ent.posX - xCoord - 0.5,
                       ent.posY - yCoord - 0.5,
                       ent.posZ - zCoord - 0.5
                   )
                > 2;
        }
        if (ent instanceof EntityBlaze || ent instanceof EntityWither
            || ent instanceof EntityDragon) {
            return ReikaMathLibrary.py3d(
                       ent.posX - xCoord - 0.5,
                       ent.posY - yCoord - 0.5,
                       ent.posZ - zCoord - 0.5
                   )
                > 2;
        }
        if (ent instanceof FlyingMob) {
            FlyingMob fm = (FlyingMob) ent;
            return fm.isCurrentlyFlying() && fm.isHostile()
                && ReikaMathLibrary.py3d(
                       ent.posX - xCoord - 0.5,
                       ent.posY - yCoord - 0.5,
                       ent.posZ - zCoord - 0.5
                   )
                > 2;
        }
        if (InterfaceCache.BCROBOT.instanceOf(ent)) {
            return true;
        }
        return false;
    }

    @Override
    public MachineRegistry getTile() {
        return MachineRegistry.ANTIAIR;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    public int getSizeInventory() {
        return 27;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return ReikaItemHelper.matchStacks(itemstack, ItemStacks.scrap);
    }
}
