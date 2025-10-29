package Reika.DragonAPI.Interfaces;

import net.minecraft.entity.Entity;

public interface PositionController {
    public void update(Entity e);
    public double getPositionX(Entity e);
    public double getPositionY(Entity e);
    public double getPositionZ(Entity e);
}
