package Reika.DragonAPI.Instantiable.Event.Client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.WorldRenderer;

@SideOnly(Side.CLIENT)
public class ChunkWorldRenderEvent {
    private static final ArrayList<ChunkWorldRenderWatcher> handlers = new ArrayList();
    private static final Comparator<ChunkWorldRenderWatcher> comparator
        = new Comparator<ChunkWorldRenderWatcher>() {
              @Override
              public int compare(ChunkWorldRenderWatcher o1, ChunkWorldRenderWatcher o2) {
                  return Integer.compare(
                      o1.chunkRenderSortIndex(), o2.chunkRenderSortIndex()
                  );
              }
          };

    public static void addHandler(ChunkWorldRenderWatcher cw) {
        handlers.add(cw);
        Collections.sort(handlers, comparator);
    }

    public static int fire(int ret, WorldRenderer wr, int pass) {
        for (ChunkWorldRenderWatcher cw : handlers) {
            if (cw.interceptChunkRender(wr, pass, ret))
                return -1;
        }
        return ret;
    }

    @SideOnly(Side.CLIENT)
    public static interface ChunkWorldRenderWatcher {
        /**
         * Return true to cancel the chunk render. WR's posX, posY, posZ are block coords
         * of the subchunk origin.
         */
        public boolean
        interceptChunkRender(WorldRenderer wr, int renderPass, int GLListID);

        public int chunkRenderSortIndex();
    }
}
