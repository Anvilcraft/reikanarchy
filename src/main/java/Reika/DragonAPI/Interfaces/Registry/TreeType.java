package Reika.DragonAPI.Interfaces.Registry;

import java.util.List;

import Reika.DragonAPI.Instantiable.Data.Immutable.BlockBox;
import Reika.DragonAPI.Instantiable.Data.Immutable.BlockKey;
import net.minecraft.block.Block;

public interface TreeType {
    public BlockKey getItem();
    public Block getLogID();
    public Block getLeafID();
    public Block getSaplingID();
    public List<Integer> getLogMetadatas();
    public List<Integer> getLeafMetadatas();
    public int getSaplingMeta();
    public boolean canBePlacedSideways();
    public boolean exists();
    public BlockKey getBasicLeaf();
    public BlockBox getTypicalMaximumSize();
}
