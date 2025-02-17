package Reika.VoidMonster.API;

import Reika.DragonAPI.Instantiable.Event.Base.WorldPositionEvent;
import cpw.mods.fml.common.eventhandler.Cancelable;
import net.minecraft.block.Block;
import net.minecraft.world.World;

@Cancelable
/**
   Note that cancellation will have no effect on torches or glowstone; those cannot be
   disabled.
 */
public class VoidMonsterEatLightEvent extends WorldPositionEvent {
    public final Block block;
    public final int metadata;

    public VoidMonsterEatLightEvent(World world, int x, int y, int z, Block b, int meta) {
        super(world, x, y, z);
        block = b;
        metadata = meta;
    }
}
