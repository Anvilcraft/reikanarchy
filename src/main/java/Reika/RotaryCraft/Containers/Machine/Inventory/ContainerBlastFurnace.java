/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.Containers.Machine.Inventory;

import Reika.DragonAPI.Base.CoreContainer;
import Reika.DragonAPI.Instantiable.GUI.Slot.SlotApprovedItems;
import Reika.DragonAPI.Libraries.Registry.ReikaItemHelper;
import Reika.RotaryCraft.Auxiliary.ItemStacks;
import Reika.RotaryCraft.Registry.ItemRegistry;
import Reika.RotaryCraft.Registry.RotaryAchievements;
import Reika.RotaryCraft.TileEntities.Production.TileEntityBlastFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

public class ContainerBlastFurnace extends CoreContainer {
    private TileEntityBlastFurnace blast;

    public ContainerBlastFurnace(EntityPlayer player, TileEntityBlastFurnace te) {
        super(player, te);
        blast = te;
        int posX = blast.xCoord;
        int posY = blast.yCoord;
        int posZ = blast.zCoord;

        int id = 0;
        this.addSlotToContainer(new Slot(te, id, 26, 35));
        id++;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.addSlotToContainer(new Slot(te, id, 62 + j * 18, 17 + i * 18));
                id++;
            }
        }
        this.addSlotToContainer(new SlotFurnace(player, te, 10, 148, 35));
        this.addSlotToContainer(new Slot(te, 11, 26, 54));
        this.addSlotToContainer(new SlotFurnace(player, te, 12, 148, 17));
        this.addSlotToContainer(new SlotFurnace(player, te, 13, 148, 53));

        this.addSlotToContainer(new Slot(te, 14, 26, 16));

        this.addSlotToContainer(new SlotApprovedItems(te, te.PATTERN_SLOT, 123, 53)
                                    .addItem(ItemRegistry.CRAFTPATTERN.getItemInstance())
        );

        this.addPlayerInventory(player);
    }

    @Override
    public ItemStack slotClick(int ID, int mouse, int action, EntityPlayer ep) {
        if (ID >= 0 && ID < blast.getSizeInventory()) {
            if (inventorySlots.get(ID).getClass() == Slot.class) {
                if (mouse == 2) {
                    blast.lockedSlots[ID] = !blast.lockedSlots[ID];
                    blast.syncAllData(false);
                    return null; //prevent creative item dupe
                }
            }
        }
        ItemStack is = super.slotClick(ID, mouse, action, ep);
        if (ID >= 0 && ID < blast.getSizeInventory()) {
            if (ID == 10 || ID == 13 || ID == 12) {
                if (ReikaItemHelper.matchStacks(ItemStacks.steelingot, is)) {
                    RotaryAchievements.MAKESTEEL.triggerAchievement(ep);
                }
            }
        }
        return is;
    }

    @Override
    protected ItemStack onShiftClickSlot(EntityPlayer ep, int ID, ItemStack is) {
        if (ID < blast.getSizeInventory() && (ID == 10 || ID == 13 || ID == 12)) {
            if (ReikaItemHelper.matchStacks(ItemStacks.steelingot, is)) {
                RotaryAchievements.MAKESTEEL.triggerAchievement(ep);
            }
        }
        return null;
    }
}
