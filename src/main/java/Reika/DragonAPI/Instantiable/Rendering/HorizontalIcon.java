package Reika.DragonAPI.Instantiable.Rendering;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public final class HorizontalIcon extends TextureAtlasSprite {
    public final int width;
    public final int height;

    private final Class reference;
    private final String path;

    public HorizontalIcon(String ico, int w, int h, Class c, String p) {
        super(ico);

        width = w;
        height = h;

        reference = c;
        path = p;
    }

    @Override
    public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
        return true;
    }

    @Override
    public boolean load(IResourceManager manager, ResourceLocation location) {
        List<int[][]> data = new ArrayList();
        try (InputStream in = reference.getResourceAsStream(path)) {
            BufferedImage img = ImageIO.read(in);
            int w = img.getWidth();
            int h = img.getHeight();
            int cols = w / width;
            int rows = h / height;
            for (int i = 0; i < rows; i++) {
                for (int k = 0; k < cols; k++) {
                    int[][] argb = new int[width][height];
                    int idx = i * width + k;
                    for (int a = 0; a < width; a++) {
                        for (int b = 0; b < height; b++) {
                            argb[a][b] = img.getRGB(k, i);
                        }
                    }

                    data.add(argb);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
