package Reika.ChromatiCraft.API;

import Reika.ChromatiCraft.API.CrystalElementAccessor.CrystalElementProxy;
import net.minecraft.entity.player.EntityPlayer;

public interface PlayerBufferAPI {
    /**
     * Subtracts the specified amount of (or, if the player has less, all of their)
     * energy of a given color from a player's buffer.
     */
    public void removeFromPlayer(EntityPlayer ep, CrystalElementProxy color, int amt);
}
