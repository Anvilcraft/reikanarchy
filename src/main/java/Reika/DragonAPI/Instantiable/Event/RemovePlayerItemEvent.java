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

import cpw.mods.fml.common.eventhandler.Cancelable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

@Cancelable
public class RemovePlayerItemEvent extends PlayerEvent {
    private final ItemStack item;

    public RemovePlayerItemEvent(EntityPlayer ep, ItemStack is) {
        super(ep);

        item = is;
    }

    public ItemStack getItem() {
        return item.copy();
    }
}
