package Reika.DragonAPI.Instantiable.Event;

import Reika.DragonAPI.DragonOptions;
import Reika.DragonAPI.Instantiable.Event.Base.WorldPositionEvent;
import cpw.mods.fml.common.eventhandler.Cancelable;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

@Cancelable
public class FireSpreadEvent extends WorldPositionEvent {
    public FireSpreadEvent(World world, int x, int y, int z) {
        super(world, x, y, z);
    }

    public static boolean fire(World world, int x, int y, int z) {
        boolean rule = world.getGameRules().getGameRuleBooleanValue("doFireTick");
        if (!rule)
            return false;
        if (DragonOptions.STOPUNLOADSPREAD.getState()
            && !world.checkChunksExist(x - 6, y - 6, z - 6, x + 6, y + 6, z + 6)) {
            return false;
        }
        boolean flag = MinecraftForge.EVENT_BUS.post(new FireSpreadEvent(world, x, y, z));
        return !flag;
    }
}
