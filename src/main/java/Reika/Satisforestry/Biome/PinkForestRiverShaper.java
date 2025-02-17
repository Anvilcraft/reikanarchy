package Reika.Satisforestry.Biome;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import Reika.DragonAPI.Exception.RegistrationException;
import Reika.Satisforestry.Satisforestry;

@Deprecated
public class PinkForestRiverShaper {
    public static final PinkForestRiverShaper instance = new PinkForestRiverShaper();

    private final BufferedImage data;
    private final int width;
    private final int height;

    private PinkForestRiverShaper() {
        try {
            data = ImageIO.read(
                Satisforestry.class.getResourceAsStream("Textures/forestrivers.png")
            );
        } catch (IOException e) {
            throw new RegistrationException(
                Satisforestry.instance, "Could not load pink forest river data", e
            );
        }
        width = data.getWidth();
        height = data.getHeight();
    }

    public double getIntensity(int x, int z) {
        x = (x % width + width) % width;
        z = (z % height + height) % height;
        int rgb = data.getRGB(x, z) & 0xFF; //all channels the same so just use blue
        return 1D - rgb / 255D;
    }
}
