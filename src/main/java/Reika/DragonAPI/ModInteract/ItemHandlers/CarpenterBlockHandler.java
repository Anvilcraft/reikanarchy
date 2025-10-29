package Reika.DragonAPI.ModInteract.ItemHandlers;

import java.lang.reflect.Field;
import java.util.HashMap;

import Reika.DragonAPI.Base.ModHandlerBase;
import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.ModList;
import net.minecraft.block.Block;

public class CarpenterBlockHandler extends ModHandlerBase {
    private boolean init = false;

    private static final CarpenterBlockHandler instance = new CarpenterBlockHandler();

    private final HashMap<Block, Blocks> blockMap = new HashMap();

    public static enum Blocks {
        BLOCK("Block"),
        SLOPE("Slope"),
        FENCE("Barrier"),
        GATE("Gate"),
        STAIRS("Stairs"),
        TORCH("Torch"),
        DOOR("Door"),
        TRAPDOOR("Hatch"),
        LADDER("Ladder"),
        LEVER("Lever"),
        BUTTON("Button"),
        PRESSURE("PressurePlate"),
        BED("Bed"),
        SENSOR("DaylightSensor"),
        FLOWERPOT("FlowerPot");

        private final String tag;
        private Block item;

        private static final Blocks[] list = values();

        private Blocks(String id) {
            tag = "blockCarpenters" + id;
        }

        public Block getBlock() {
            return item;
        }
    }

    private CarpenterBlockHandler() {
        super();
        if (this.hasMod()) {
            Class blocks = this.getMod().getBlockClass();
            for (int i = 0; i < Blocks.list.length; i++) {
                Blocks ie = Blocks.list[i];
                try {
                    Field f = blocks.getDeclaredField(ie.tag);
                    Block b = (Block) f.get(null);
                    ie.item = b;
                    blockMap.put(b, ie);
                } catch (NoSuchFieldException e) {
                    DragonAPICore.logError(
                        this.getMod() + " field not found! " + e.getMessage()
                    );
                    e.printStackTrace();
                    this.logFailure(e);
                } catch (IllegalArgumentException e) {
                    DragonAPICore.logError(
                        "Illegal argument for reading " + this.getMod() + "!"
                    );
                    e.printStackTrace();
                    this.logFailure(e);
                } catch (IllegalAccessException e) {
                    DragonAPICore.logError(
                        "Illegal access exception for reading " + this.getMod() + "!"
                    );
                    e.printStackTrace();
                    this.logFailure(e);
                } catch (NullPointerException e) {
                    DragonAPICore.logError(
                        "Null pointer exception for reading " + this.getMod()
                        + "! Was the class loaded?"
                    );
                    e.printStackTrace();
                    this.logFailure(e);
                }
            }

            init = true;
        } else {
            this.noMod();
        }
    }

    public static CarpenterBlockHandler getInstance() {
        return instance;
    }

    @Override
    public boolean initializedProperly() {
        return init;
    }

    @Override
    public ModList getMod() {
        return ModList.CARPENTER;
    }

    public boolean isCarpenterBlock(Block b) {
        return blockMap.containsKey(b);
    }

    public Blocks getCarpenterBlock(Block b) {
        return blockMap.get(b);
    }
}
