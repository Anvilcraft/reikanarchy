package Reika.DragonAPI.ModInteract.DeepInteract;

import java.lang.reflect.Method;

import Reika.DragonAPI.Auxiliary.Trackers.ReflectiveFailureTracker;
import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.ModList;
import net.minecraft.tileentity.TileEntity;

public class TransvectorHandler {
    private static Class tileClass;
    private static Method getRelayTile;

    public static TileEntity getRelayedTile(TileEntity tv) {
        try {
            return tileClass != null && tv != null
                    && tileClass.isAssignableFrom(tv.getClass())
                ? (TileEntity) getRelayTile.invoke(tv)
                : tv;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static {
        if (ModList.THAUMICTINKER.isLoaded()) {
            try {
                tileClass = Class.forName(
                    "thaumic.tinkerer.common.block.tile.transvector.TileTransvector"
                );
                getRelayTile = tileClass.getMethod("getTile");
            } catch (Exception e) {
                DragonAPICore.logError("Error loading Transvector Handling!");
                e.printStackTrace();
                ReflectiveFailureTracker.instance.logModReflectiveFailure(
                    ModList.THAUMICTINKER, e
                );
            }
        }
    }
}
