package Reika.ChromatiCraft.API;

import Reika.ChromatiCraft.API.CrystalElementAccessor.CrystalElementProxy;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

/** Use this to fetch and/or compare against ChromatiCraft foliage blocks. */
public interface DyeTreeAPI {
    public boolean isCCLeaf(Block b);

    public ItemStack getDyeSapling(CrystalElementProxy e);

    public ItemStack getDyeFlower(CrystalElementProxy e);

    public ItemStack getDyeLeaf(CrystalElementProxy e, boolean natural);

    public Block getDyeSapling();

    public Block getDyeFlower();

    public Block getDyeLeaf(boolean natural);

    public Block getRainbowLeaf();

    public Block getRainbowSapling();

    public Block getDecoFlower();
}
