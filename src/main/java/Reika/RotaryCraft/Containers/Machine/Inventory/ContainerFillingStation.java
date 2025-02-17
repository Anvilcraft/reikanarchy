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

import Reika.DragonAPI.Instantiable.GUI.ArmorSlot;
import Reika.DragonAPI.Libraries.IO.ReikaPacketHelper;
import Reika.RotaryCraft.Base.ContainerIOMachine;
import Reika.RotaryCraft.RotaryCraft;
import Reika.RotaryCraft.TileEntities.Auxiliary.TileEntityFillingStation;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerFillingStation extends ContainerIOMachine {
    private TileEntityFillingStation tile;

    public ContainerFillingStation(EntityPlayer player, TileEntityFillingStation te) {
        super(player, te);
        tile = te;

        this.addSlotNoClick(0, 106, 71);
        this.addSlot(1, 54, 21);
        this.addSlotNoClick(2, 134, 71);
        this.addSlot(3, 106, 21);

        this.addPlayerInventoryWithOffset(player, 0, 21);

        for (int i = 0; i < 4; i++) {
            this.addSlotToContainer(new ArmorSlot(
                player, player.inventory.getSizeInventory() - 1 - i, 20, 21 + i * 18, i
            ));
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, tile, "tank");
    }
}
