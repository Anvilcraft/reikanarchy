package Reika.DragonAPI.ModInteract.ItemHandlers;

import java.lang.reflect.Field;

import Reika.DragonAPI.Base.ModHandlerBase;
import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.Libraries.Java.ReikaArrayHelper;
import Reika.DragonAPI.ModList;
import net.minecraft.block.Block;

public class ThermalDuctHandler extends ModHandlerBase {
    private static final ThermalDuctHandler instance = new ThermalDuctHandler();

    private Block[] ductIDs;

    private ThermalDuctHandler() {
        super();
        Block idender = null;
        if (this.hasMod()) {
            try {
                Class blocks = this.getMod().getBlockClass();
                Field pipe = blocks.getField("blockDuct");
                ductIDs = ((Block[]) pipe.get(null));
            } catch (NoSuchFieldException e) {
                DragonAPICore.logError(
                    this.getMod() + " field not found! " + e.getMessage()
                );
                e.printStackTrace();
                this.logFailure(e);
            } catch (SecurityException e) {
                DragonAPICore.logError(
                    "Cannot read " + this.getMod() + " (Security Exception)! "
                    + e.getMessage()
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
        } else {
            this.noMod();
        }
    }

    public static ThermalDuctHandler getInstance() {
        return instance;
    }

    @Override
    public boolean initializedProperly() {
        return ductIDs != null;
    }

    public boolean isDuct(Block b) {
        return ductIDs != null && ReikaArrayHelper.contains(ductIDs, b);
    }

    @Override
    public ModList getMod() {
        return ModList.THERMALDYNAMICS;
    }
}
