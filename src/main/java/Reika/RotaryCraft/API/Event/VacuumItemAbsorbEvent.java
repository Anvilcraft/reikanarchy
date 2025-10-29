package Reika.RotaryCraft.API.Event;

import Reika.DragonAPI.Instantiable.Event.TileEntityEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class VacuumItemAbsorbEvent extends TileEntityEvent {
    private final ItemStack item;

    public VacuumItemAbsorbEvent(TileEntity te, ItemStack item) {
        super(te);
        this.item = item;
    }

    public final ItemStack getItem() {
        return item != null ? item.copy() : null;
    }
}
