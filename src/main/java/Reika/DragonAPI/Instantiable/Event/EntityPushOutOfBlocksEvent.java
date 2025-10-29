package Reika.DragonAPI.Instantiable.Event;

import java.util.List;

import cpw.mods.fml.common.eventhandler.Cancelable;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;

@Cancelable
public class EntityPushOutOfBlocksEvent extends EntityEvent {
    public EntityPushOutOfBlocksEvent(Entity e) {
        super(e);
    }

    public static boolean fire(Entity e, List list, int i, int j, int k) {
        return MinecraftForge.EVENT_BUS.post(new EntityPushOutOfBlocksEvent(e))
            || list.isEmpty() && !e.worldObj.func_147469_q(i, j, k);
    }
}
