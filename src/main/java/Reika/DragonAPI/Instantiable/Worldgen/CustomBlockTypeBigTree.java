package Reika.DragonAPI.Instantiable.Worldgen;

import Reika.DragonAPI.Instantiable.Data.Immutable.BlockKey;
import net.minecraft.block.Block;

public final class CustomBlockTypeBigTree extends ModifiableBigTree {
    private final BlockKey wood;
    private final BlockKey leaves;

    public CustomBlockTypeBigTree(boolean updates, Block log, Block leaf) {
        this(updates, new BlockKey(log), new BlockKey(leaf));
    }

    public CustomBlockTypeBigTree(boolean updates, BlockKey log, BlockKey leaf) {
        super(updates);
        wood = log;
        leaves = leaf;
    }

    @Override
    public BlockKey getLogBlock(int x, int y, int z) {
        return wood;
    }

    @Override
    public BlockKey getLeafBlock(int x, int y, int z) {
        return leaves;
    }
}
