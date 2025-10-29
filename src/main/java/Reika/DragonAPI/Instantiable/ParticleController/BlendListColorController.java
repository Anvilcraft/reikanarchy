package Reika.DragonAPI.Instantiable.ParticleController;

import Reika.DragonAPI.Instantiable.Rendering.ColorBlendList;
import Reika.DragonAPI.Interfaces.ColorController;
import net.minecraft.entity.Entity;

public class BlendListColorController implements ColorController {
    private final ColorBlendList colors;

    public BlendListColorController(ColorBlendList cbl) {
        colors = cbl;
    }

    @Override
    public void update(Entity e) {}

    @Override
    public int getColor(Entity e) {
        return colors.getColor(e.ticksExisted);
    }
}
