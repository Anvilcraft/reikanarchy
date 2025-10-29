package Reika.RotaryCraft.API.Event;

import Reika.DragonAPI.Instantiable.Event.TileEntityEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class WorktableCraftEvent extends TileEntityEvent {
    public final boolean automated;
    public final EntityPlayer player;
    private final ItemStack item;

    public WorktableCraftEvent(
        TileEntity te, EntityPlayer ep, boolean wasAuto, ItemStack is
    ) {
        super(te);
        automated = wasAuto;
        player = ep;
        item = is;
    }

    public final ItemStack getItem() {
        return item != null ? item.copy() : null;
    }
}
