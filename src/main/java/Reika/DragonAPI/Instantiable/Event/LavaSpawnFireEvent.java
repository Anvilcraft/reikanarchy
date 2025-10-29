package Reika.DragonAPI.Instantiable.Event;

import Reika.DragonAPI.Instantiable.Event.Base.WorldPositionEvent;
import cpw.mods.fml.common.eventhandler.Cancelable;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

@Cancelable
public class LavaSpawnFireEvent extends WorldPositionEvent {
    public LavaSpawnFireEvent(World world, int x, int y, int z) {
        super(world, x, y, z);
    }

    public static boolean fire(Block liquid, World world, int x, int y, int z) {
        LavaSpawnFireEvent evt = new LavaSpawnFireEvent(world, x, y, z);
        MinecraftForge.EVENT_BUS.post(evt);
        return !evt.isCanceled() && evt.getBlock().getMaterial().getCanBurn();
    }
}
