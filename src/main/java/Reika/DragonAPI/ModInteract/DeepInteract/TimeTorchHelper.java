package Reika.DragonAPI.ModInteract.DeepInteract;

import java.lang.reflect.Field;
import java.util.HashSet;

import Reika.DragonAPI.Auxiliary.Trackers.ReflectiveFailureTracker;
import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.Instantiable.BasicModEntry;
import cpw.mods.fml.common.Loader;
import net.minecraft.block.Block;

public class TimeTorchHelper {
    private static HashSet<Class> tileBlacklist;
    private static HashSet<Block> blockBlacklist;

    public static void blacklistTileEntity(Class c) {
        if (tileBlacklist != null) {
            tileBlacklist.add(c);
        }
    }

    public static void blacklistBlock(Block b) {
        if (blockBlacklist != null) {
            blockBlacklist.add(b);
        }
    }

    static {
        if (Loader.isModLoaded("Torcherino")) {
            try {
                Class c = null;
                try {
                    c = Class.forName("com.sci.torcherino.TorcherinoRegistry");
                } catch (ClassNotFoundException e) {
                    c = Class.forName("com.sci.torcherino.tile.TileTorcherino");
                }
                Field f = c.getDeclaredField("blacklistedTiles");
                f.setAccessible(true);
                tileBlacklist = (HashSet<Class>) f.get(null);

                f = c.getDeclaredField("blacklistedBlocks");
                f.setAccessible(true);
                blockBlacklist = (HashSet<Block>) f.get(null);
            } catch (Exception e) {
                DragonAPICore.logError("Could not load Time Torch blacklisting!");
                e.printStackTrace();
                ReflectiveFailureTracker.instance.logModReflectiveFailure(
                    new BasicModEntry("Torcherino"), e
                );
            }
        }
    }
}
