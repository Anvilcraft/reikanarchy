package Reika.DragonAPI.Instantiable.AI;

import Reika.DragonAPI.Instantiable.Data.Immutable.Coordinate;
import Reika.DragonAPI.Interfaces.TileEntity.MobRepellent;
import net.minecraft.entity.EntityLiving;
import net.minecraft.tileentity.TileEntity;

public class AITaskAvoidMachine extends AITaskAvoidLocation {
    private final MobRepellent tile;

    public AITaskAvoidMachine(EntityLiving e, double sp, MobRepellent te) {
        super(e, sp, new Coordinate((TileEntity) te));
        tile = te;
    }

    @Override
    public final boolean shouldExecute() {
        return tile.canRepel(entity);
    }
}
