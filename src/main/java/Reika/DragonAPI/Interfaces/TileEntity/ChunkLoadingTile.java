package Reika.DragonAPI.Interfaces.TileEntity;

import java.util.Collection;

import net.minecraft.world.ChunkCoordIntPair;

/** For TileEntities that load chunks. Only implement this on a TileEntity! */
public interface ChunkLoadingTile extends BreakAction {
    //public void setTicket(Ticket t);

    public Collection<ChunkCoordIntPair> getChunksToLoad();

    //public boolean loadChunk(ChunkCoordIntPair chip);
}
