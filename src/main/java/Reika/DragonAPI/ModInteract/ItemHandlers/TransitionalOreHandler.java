/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.ModInteract.ItemHandlers;

import java.lang.reflect.Field;

import Reika.DragonAPI.Base.ModHandlerBase;
import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.Libraries.Registry.ReikaItemHelper;
import Reika.DragonAPI.ModList;
import Reika.DragonAPI.ModRegistry.ModOreList;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class TransitionalOreHandler extends ModHandlerBase {
    private static final TransitionalOreHandler instance = new TransitionalOreHandler();

    public final Block magmaID;
    public final Block cobaltID;

    private TransitionalOreHandler() {
        super();
        Block idore = null;
        Block idcobalt = null;

        if (this.hasMod()) {
            try {
                Class trans = ModList.TRANSITIONAL.getBlockClass();
                Field magma = trans.getField("MagmaniteOreID");
                idore = (Block) magma.get(null);
                Field cobalt = trans.getField("CobaltOreID");
                idcobalt = (Block) cobalt.get(null);
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

        magmaID = idore;
        cobaltID = idcobalt;
        OreDictionary.registerOre("oreCobalt", new ItemStack(cobaltID, 1, 0));
        ModOreList.COBALT.initialize();
    }

    public static TransitionalOreHandler getInstance() {
        return instance;
    }

    @Override
    public boolean initializedProperly() {
        return magmaID != null && cobaltID != null;
    }

    @Override
    public ModList getMod() {
        return ModList.TRANSITIONAL;
    }

    public boolean isMagmaniteOre(ItemStack block) {
        if (!this.initializedProperly())
            return false;
        return ReikaItemHelper.matchStackWithBlock(block, magmaID);
    }
}
