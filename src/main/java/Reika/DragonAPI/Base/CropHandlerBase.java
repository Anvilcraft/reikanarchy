package Reika.DragonAPI.Base;

import Reika.DragonAPI.Interfaces.Registry.CropHandler;
import net.minecraft.world.World;

public abstract class CropHandlerBase extends ModHandlerBase implements CropHandler {
    public void editTileDataForHarvest(World world, int x, int y, int z) {}
}
