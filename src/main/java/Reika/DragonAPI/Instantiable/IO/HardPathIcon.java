package Reika.DragonAPI.Instantiable.IO;

import Reika.DragonAPI.IO.DirectResourceManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

public class HardPathIcon extends TextureAtlasSprite {
    public HardPathIcon(String s) {
        super(s);
    }

    public ResourceLocation getResource() {
        return DirectResourceManager.getResource(this.getIconName());
    }

    public HardPathIcon register(TextureMap m) {
        ResourceLocation loc = this.getResource();
        m.mapRegisteredSprites.put(loc.toString(), this);
        return this;
    }
}
