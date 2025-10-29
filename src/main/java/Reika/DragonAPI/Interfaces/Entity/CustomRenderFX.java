package Reika.DragonAPI.Interfaces.Entity;

import Reika.DragonAPI.Instantiable.Rendering.ParticleEngine.RenderMode;
import Reika.DragonAPI.Instantiable.Rendering.ParticleEngine.TextureMode;

public interface CustomRenderFX {
    RenderMode getRenderMode();

    TextureMode getTexture();

    public boolean rendersOverLimit();

    //public double getRenderRange();
}
