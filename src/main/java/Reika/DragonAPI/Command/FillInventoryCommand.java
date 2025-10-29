package Reika.DragonAPI.Command;

import Reika.DragonAPI.Libraries.Registry.ReikaItemHelper;
import Reika.DragonAPI.Libraries.ReikaPlayerAPI;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;

public class FillInventoryCommand extends DragonCommandBase {
    @Override
    public void processCommand(ICommandSender ics, String[] args) {
        EntityPlayerMP ep = this.getCommandSenderAsPlayer(ics);
        ItemStack is = ep.getCurrentEquippedItem();
        if (is == null) {
            this.sendChatToSender(ics, EnumChatFormatting.RED + "No Item.");
            return;
        }
        MovingObjectPosition mov = ReikaPlayerAPI.getLookedAtBlock(ep, 5, false);
        if (mov != null) {
            TileEntity te = ep.worldObj.getTileEntity(mov.blockX, mov.blockY, mov.blockZ);
            if (te instanceof IInventory) {
                IInventory ii = (IInventory) te;
                for (int i = 0; i < ii.getSizeInventory(); i++) {
                    if (ii.isItemValidForSlot(i, is)) {
                        ii.setInventorySlotContents(
                            i,
                            ReikaItemHelper.getSizedItemStack(
                                is,
                                Math.min(
                                    is.getMaxStackSize(), ii.getInventoryStackLimit()
                                )
                            )
                        );
                    }
                }
            } else {
                this.sendChatToSender(ics, EnumChatFormatting.RED + "No Inventory.");
            }
        } else {
            this.sendChatToSender(ics, EnumChatFormatting.RED + "No Block.");
        }
    }

    @Override
    public String getCommandString() {
        return "fillinv";
    }

    @Override
    protected boolean isAdminOnly() {
        return true;
    }
}
