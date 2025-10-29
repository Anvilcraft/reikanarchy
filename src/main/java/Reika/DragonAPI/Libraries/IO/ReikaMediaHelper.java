package Reika.DragonAPI.Libraries.IO;

import Reika.DragonAPI.DragonAPICore;
import net.minecraft.world.World;

public final class ReikaMediaHelper extends DragonAPICore {
    public static void playSoundVarying(World world, int x, int y, int z, String name) {
        world.playSoundEffect(
            x + 0.5, y + 0.5, z + 0.5, name, rand.nextFloat(), rand.nextFloat()
        );
    }
}
