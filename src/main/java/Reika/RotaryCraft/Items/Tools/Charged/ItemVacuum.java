/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.Items.Tools.Charged;

import java.util.List;

import Reika.DragonAPI.Libraries.MathSci.ReikaMathLibrary;
import Reika.DragonAPI.Libraries.ReikaInventoryHelper;
import Reika.DragonAPI.Libraries.ReikaPlayerAPI;
import Reika.RotaryCraft.Base.ItemChargedTool;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemVacuum extends ItemChargedTool {
    public ItemVacuum(int tex) {
        super(tex);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer ep) {
        if (is.getItemDamage() <= 0) {
            this.noCharge();
            return is;
        }
        this.warnCharge(is);
        if (ep.isSneaking()) {
            this.empty(is, world, ep);
            return new ItemStack(is.getItem(), is.stackSize, is.getItemDamage() - 2);
        }
        AxisAlignedBB range = AxisAlignedBB.getBoundingBox(
            ep.posX - 8, ep.posY - 8, ep.posZ - 8, ep.posX + 8, ep.posY + 8, ep.posZ + 8
        );
        List inrange = world.getEntitiesWithinAABB(EntityItem.class, range);
        for (int i = 0; i < inrange.size(); i++) {
            EntityItem ent = (EntityItem) inrange.get(i);
            ItemStack is2 = ent.getEntityItem();
            if (ReikaInventoryHelper.canAcceptMoreOf(is2, ep.inventory)) {
                double dx = (ep.posX - ent.posX);
                double dy = (ep.posY - ent.posY);
                double dz = (ep.posZ - ent.posZ);
                double ddt = ReikaMathLibrary.py3d(dx, dy, dz);
                ent.motionX += dx / ddt / ddt / 2;
                ent.motionY += dy / ddt / ddt / 2;
                ent.motionZ += dz / ddt / ddt / 2;
                if (ent.posY < ep.posY)
                    ent.motionY += 0.1;
                if (!world.isRemote)
                    ent.velocityChanged = true;
            }
        }
        List inbox2 = world.getEntitiesWithinAABB(EntityXPOrb.class, range);
        for (int i = 0; i < inbox2.size(); i++) {
            EntityXPOrb ent = (EntityXPOrb) inbox2.get(i);
            double dx = (ep.posX - ent.posX);
            double dy = (ep.posY - ent.posY);
            double dz = (ep.posZ - ent.posZ);
            double ddt = ReikaMathLibrary.py3d(dx, dy, dz);
            ent.motionX += dx / ddt / ddt / 2;
            ent.motionY += dy / ddt / ddt / 2;
            ent.motionZ += dz / ddt / ddt / 2;
            if (ent.posY < ep.posY)
                ent.motionY += 0.1;
            if (!world.isRemote)
                ent.velocityChanged = true;
        }
        return new ItemStack(is.getItem(), is.stackSize, is.getItemDamage() - 1);
    }

    private void empty(ItemStack is, World world, EntityPlayer ep) {
        MovingObjectPosition mov = ReikaPlayerAPI.getLookedAtBlock(ep, 5, false);
        //ReikaChatHelper.write(mov.blockX+", "+mov.blockY+", "+mov.blockZ);
        if (mov == null)
            return;
        int x = mov.blockX;
        int y = mov.blockY;
        int z = mov.blockZ;
        Block b = world.getBlock(x, y, z);
        ;
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile == null || !(tile instanceof IInventory))
            return;
        IInventory ii = (IInventory) tile;
        ReikaInventoryHelper.spillAndEmptyInventory(world, x, y, z, ii);
    }
}
