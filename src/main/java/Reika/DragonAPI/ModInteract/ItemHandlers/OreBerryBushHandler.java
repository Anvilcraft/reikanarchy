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
import java.util.ArrayList;

import Reika.DragonAPI.Base.CropHandlerBase;
import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.Instantiable.Data.Immutable.BlockKey;
import Reika.DragonAPI.ModList;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class OreBerryBushHandler extends CropHandlerBase {
    private static final OreBerryBushHandler instance = new OreBerryBushHandler();

    public final Block bushID;
    public final Block secondbushID;

    public final Item berryID;

    public static enum BerryTypes {
        IRON(),
        GOLD(),
        COPPER(),
        TIN(),
        ALUMINUM(),
        XP();

        public ItemStack getStack() {
            return new ItemStack(instance.berryID, 1, this.ordinal());
        }

        /**
         * Metadatas are the worldgen values, which are the ripe ones. Subtract 4 for
         * harvested.
         */
        public BlockKey getBush() {
            Block b = this == ALUMINUM || this == XP ? instance.secondbushID
                                                     : instance.bushID;
            int meta = 12 + this.ordinal();
            if (b == instance.secondbushID)
                meta -= ALUMINUM.ordinal();
            return new BlockKey(b, meta);
        }
    }

    private OreBerryBushHandler() {
        super();
        Block idbush = null;
        Block idsecondbush = null;

        Item idberry = null;

        if (this.hasMod()) {
            Class blocks = this.getMod().getBlockClass();
            Class items = this.getMod().getItemClass();
            try {
                Field f = blocks.getField("oreBerry");
                Block bush = (Block) f.get(null);
                idbush = bush;

                f = blocks.getField("oreBerrySecond");
                Block secondbush = (Block) f.get(null);
                idsecondbush = secondbush;

                f = items.getField("oreBerries");
                Item berry = (Item) f.get(null);
                idberry = berry;
            } catch (NoSuchFieldException e) {
                DragonAPICore.logError(
                    this.getMod() + " field not found! " + e.getMessage()
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
        berryID = idberry;
        bushID = idbush;

        secondbushID = idsecondbush;
    }

    @Override
    public boolean isCrop(Block id, int meta) {
        return id == bushID || id == secondbushID;
    }

    @Override
    public boolean isSeedItem(ItemStack is) {
        return false;
    }

    @Override
    public boolean isRipeCrop(World world, int x, int y, int z) {
        Block b = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        return this.isCrop(b, meta) && meta >= 12;
    }

    @Override
    public void makeRipe(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        int base = meta % 4;
        int metato = 12 + base;
        world.setBlockMetadataWithNotify(x, y, z, metato, 3);
    }

    public static OreBerryBushHandler getInstance() {
        return instance;
    }

    @Override
    public boolean initializedProperly() {
        return bushID != null && berryID != null || secondbushID != null;
    }

    @Override
    public ModList getMod() {
        return ModList.TINKERER;
    }

    @Override
    public int getHarvestedMeta(World world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z) - 4;
    }

    @Override
    public ArrayList<ItemStack> getAdditionalDrops(
        World world, int x, int y, int z, Block id, int meta, int fortune
    ) {
        ArrayList<ItemStack> li = new ArrayList();
        if (id == bushID) {
            li.add(new ItemStack(berryID, 1 + rand.nextInt(3), meta % 4));
        } else if (id == secondbushID) {
            li.add(new ItemStack(berryID, 1 + rand.nextInt(3), meta % 4 + 4));
        }
        return li;
    }

    @Override
    public ArrayList<ItemStack>
    getDropsOverride(World world, int x, int y, int z, Block id, int meta, int fortune) {
        return new ArrayList();
    }

    @Override
    public int getGrowthState(World world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z) % 4;
    }

    @Override
    public boolean neverDropsSecondSeed() {
        return true;
    }

    @Override
    public boolean isTileEntity() {
        return false;
    }
}
