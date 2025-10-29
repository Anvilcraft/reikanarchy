package Reika.DragonAPI.Interfaces.TileEntity;

import net.minecraft.entity.player.EntityPlayer;

public interface PlayerBreakHook {
    /** Return false to cancel the block break. */
    public boolean breakByPlayer(EntityPlayer ep);

    public boolean isBreakable(EntityPlayer ep);
}
