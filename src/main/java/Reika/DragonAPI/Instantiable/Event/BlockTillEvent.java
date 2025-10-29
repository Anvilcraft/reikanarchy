package Reika.DragonAPI.Instantiable.Event;

import Reika.DragonAPI.Instantiable.Event.Base.WorldPositionEvent;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class BlockTillEvent extends WorldPositionEvent {
    public final Block originalBlock;
    public final int originalMetadata;

    public Block tilledBlock = Blocks.farmland;
    public int tilledMeta = 0;

    public BlockTillEvent(World world, int x, int y, int z, Block b, int meta) {
        super(world, x, y, z);
        originalBlock = b;
        originalMetadata = meta;
    }

    public static void fire(World world, int x, int y, int z) {
        BlockTillEvent evt = new BlockTillEvent(
            world, x, y, z, world.getBlock(x, y, z), world.getBlockMetadata(x, y, z)
        );
        MinecraftForge.EVENT_BUS.post(evt);
        world.setBlock(x, y, z, evt.tilledBlock, evt.tilledMeta, 3);
    }
}
