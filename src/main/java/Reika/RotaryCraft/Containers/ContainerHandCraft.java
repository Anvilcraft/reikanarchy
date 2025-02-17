/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.Containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;

public class ContainerHandCraft extends Container {
    /** The crafting matrix inventory (3x3). */
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    public IInventory craftResult = new InventoryCraftResult();
    private World worldObj;

    public ContainerHandCraft(EntityPlayer player, World par2World) {
        worldObj = par2World;
        this.addSlotToContainer(
            new SlotCrafting(player, craftMatrix, craftResult, 0, 124, 35)
        );
        int var6;
        int var7;
        for (var6 = 0; var6 < 3; ++var6)
            for (var7 = 0; var7 < 3; ++var7)
                this.addSlotToContainer(
                    new Slot(craftMatrix, var7 + var6 * 3, 30 + var7 * 18, 17 + var6 * 18)
                );
        for (var6 = 0; var6 < 3; ++var6)
            for (var7 = 0; var7 < 9; ++var7)
                this.addSlotToContainer(new Slot(
                    player.inventory, var7 + var6 * 9 + 9, 8 + var7 * 18, 84 + var6 * 18
                ));
        for (var6 = 0; var6 < 9; ++var6)
            this.addSlotToContainer(new Slot(player.inventory, var6, 8 + var6 * 18, 142));
        this.onCraftMatrixChanged(craftMatrix);
    }

    @Override
    public void onCraftMatrixChanged(IInventory par1IInventory) {
        craftResult.setInventorySlotContents(
            0, CraftingManager.getInstance().findMatchingRecipe(craftMatrix, worldObj)
        );
    }

    @Override
    public void onContainerClosed(EntityPlayer par1EntityPlayer) {
        super.onContainerClosed(par1EntityPlayer);
        if (!worldObj.isRemote) {
            for (int var2 = 0; var2 < 9; ++var2) {
                ItemStack var3 = craftMatrix.getStackInSlotOnClosing(var2);
                if (var3 != null)
                    par1EntityPlayer.dropPlayerItemWithRandomChoice(var3, true);
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
        return true;
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will
     * crash when someone does that.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        ItemStack var3 = null;
        Slot var4 = (Slot) inventorySlots.get(par2);

        if (var4 != null && var4.getHasStack()) {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if (par2 == 0) {
                if (!this.mergeItemStack(var5, 10, 46, true)) {
                    return null;
                }

                var4.onSlotChange(var5, var3);
            } else if (par2 >= 10 && par2 < 37) {
                if (!this.mergeItemStack(var5, 37, 46, false)) {
                    return null;
                }
            } else if (par2 >= 37 && par2 < 46) {
                if (!this.mergeItemStack(var5, 10, 37, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(var5, 10, 46, false)) {
                return null;
            }

            if (var5.stackSize == 0) {
                var4.putStack((ItemStack) null);
            } else {
                var4.onSlotChanged();
            }

            if (var5.stackSize == var3.stackSize) {
                return null;
            }

            var4.onPickupFromSlot(par1EntityPlayer, var5);
        }

        return var3;
    }
}
