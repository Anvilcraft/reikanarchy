package Reika.DragonAPI.Instantiable.Event;

import Reika.DragonAPI.Instantiable.Event.Base.WorldPositionEvent;
import cpw.mods.fml.common.eventhandler.Cancelable;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

/** Fired right before a block is burned by fire. Cancel it to stop the consumption. */
@Cancelable
public class BlockConsumedByFireEvent extends WorldPositionEvent {
    public BlockConsumedByFireEvent(World world, int x, int y, int z) {
        super(world, x, y, z);
    }

    public static final boolean fireFromSetBlockAir(World world, int x, int y, int z) {
        if (fire(world, x, y, z))
            return world.setBlockToAir(x, y, z);
        else
            return false;
    }

    public static final boolean
    fireFromSetBlock(World world, int x, int y, int z, Block b, int meta, int flags) {
        if (fire(world, x, y, z))
            return world.setBlock(x, y, z, b, meta, flags);
        else
            return false;
    }

    public static final boolean fire(World world, int x, int y, int z) {
        return !MinecraftForge.EVENT_BUS.post(new BlockConsumedByFireEvent(world, x, y, z)
        );
    }
}
