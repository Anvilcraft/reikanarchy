package Reika.DragonAPI.ModInteract.ItemHandlers;

import java.lang.reflect.Field;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.ModList;
import Reika.DragonAPI.ASM.DependentMethodStripper.ModDependent;
import Reika.DragonAPI.Base.ModHandlerBase;
import Reika.DragonAPI.Libraries.Java.ReikaStringParser;
import Reika.DragonAPI.ModInteract.ItemHandlers.HexcraftHandler.BasicHexColor;
import Reika.DragonAPI.ModInteract.ItemHandlers.HexcraftHandler.HexHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class HexBlockHandler extends ModHandlerBase implements HexHandler {

	private static final HexBlockHandler instance = new HexBlockHandler();

	private Class monolithBaseClass;
	private Class worldGenClass;
	private Class colorizedSimpleInterface;
	private Class variantSimpleInterface;

	public static enum BasicHexColors implements BasicHexColor {
        RED,
        GREEN,
        BLUE,
        WHITE,
        BLACK,
        ;

        protected Item crystal;
        protected Block block;
        protected Block netherBlock;

        public static final BasicHexColors[] list = values();

        protected static final HashMap<Item, BasicHexColors> lookup = new HashMap();

        public Item getCrystal() {
            return crystal;
        }

        public boolean isPrimary(boolean nether) {
            return nether ? this == WHITE || this == BLACK : !this.isPrimary(true);
        }

        public Block getMonolith(boolean nether) {
            return nether ? netherBlock : block;
        }

        public static BasicHexColors getColorForItem(ItemStack is) {
            return lookup.get(is.getItem());
        }
    }

	private HexBlockHandler() {
		super();

		if (this.hasMod()) {
			try {
				/*
				Class blocks = this.getMod().getBlockClass();
				Field ore = blocks.getField("MimichiteOre");
				idstone = ((Block)ore.get(null));
				 */

				monolithBaseClass = Class.forName("com.celestek.hexcraft.block.base.BlockHexoriumMonolithBase");
				worldGenClass = Class.forName("com.celestek.hexcraft.block.BlockHexoriumMonolithWorldGen");

				colorizedSimpleInterface = Class.forName("com.celestek.hexcraft.api.IBlockHexColorSimple");
				variantSimpleInterface = Class.forName("com.celestek.hexcraft.api.IBlockHexVariantSimple");
			}
			catch (ClassNotFoundException e) {
				DragonAPICore.logError(this.getMod()+" class not found! "+e.getMessage());
				e.printStackTrace();
				this.logFailure(e);
			}
			catch (SecurityException e) {
				DragonAPICore.logError("Cannot read "+this.getMod()+" (Security Exception)! "+e.getMessage());
				e.printStackTrace();
				this.logFailure(e);
			}
			catch (IllegalArgumentException e) {
				DragonAPICore.logError("Illegal argument for reading "+this.getMod()+"!");
				e.printStackTrace();
				this.logFailure(e);
			}
			catch (NullPointerException e) {
				DragonAPICore.logError("Null pointer exception for reading "+this.getMod()+"! Was the class loaded?");
				e.printStackTrace();
				this.logFailure(e);
			}

			HexcraftHandler.setHandler(this);
		}
		else {
			this.noMod();
		}
	}

	public static HexBlockHandler getInstance() {
		return instance;
	}

	@Override
	public boolean initializedProperly() {
		return monolithBaseClass != null && colorizedSimpleInterface != null && variantSimpleInterface != null;
	}

	@Override
	public ModList getMod() {
		return ModList.HEXCRAFT;
	}

	public boolean isMonolith(Block b) {
		return monolithBaseClass != null && monolithBaseClass.isAssignableFrom(b.getClass());
	}

	public boolean isWorldGenMonolith(Block b) {
		return worldGenClass != null && worldGenClass.isAssignableFrom(b.getClass());
	}

	public boolean isColorized(Block b) {
		return colorizedSimpleInterface != null && colorizedSimpleInterface.isAssignableFrom(b.getClass());
	}

	public boolean hasVariants(Block b) {
		return variantSimpleInterface != null && variantSimpleInterface.isAssignableFrom(b.getClass());
	}

	@Override
	public BasicHexColor[] getColors() {
		return BasicHexColors.list;
	}

}