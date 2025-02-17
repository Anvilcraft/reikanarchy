/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.ModRegistry;

import java.lang.reflect.Field;
import java.util.ArrayList;

import Reika.DragonAPI.Auxiliary.Trackers.ReflectiveFailureTracker;
import Reika.DragonAPI.Base.CropHandlerBase;
import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.DragonAPIInit;
import Reika.DragonAPI.Exception.MisuseException;
import Reika.DragonAPI.Instantiable.Data.Maps.BlockMap;
import Reika.DragonAPI.Interfaces.CustomCropHandler;
import Reika.DragonAPI.Interfaces.Registry.CropHandler;
import Reika.DragonAPI.Interfaces.Registry.ModCrop;
import Reika.DragonAPI.Interfaces.Registry.ModEntry;
import Reika.DragonAPI.Libraries.Java.ReikaReflectionHelper;
import Reika.DragonAPI.Libraries.MathSci.ReikaMathLibrary;
import Reika.DragonAPI.ModInteract.ItemHandlers.AgriCraftHandler;
import Reika.DragonAPI.ModInteract.ItemHandlers.BerryBushHandler;
import Reika.DragonAPI.ModInteract.ItemHandlers.FluxedCrystalHandler;
import Reika.DragonAPI.ModInteract.ItemHandlers.ForestryHandler;
import Reika.DragonAPI.ModInteract.ItemHandlers.HarvestCraftHandler;
import Reika.DragonAPI.ModInteract.ItemHandlers.IC2RubberLogHandler;
import Reika.DragonAPI.ModInteract.ItemHandlers.OreBerryBushHandler;
import Reika.DragonAPI.ModInteract.ItemHandlers.PneumaticPlantHandler;
import Reika.DragonAPI.ModList;
import Reika.DragonAPI.ModRegistry.ModWoodList.VarType;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

public enum ModCropList implements ModCrop {
    //seed meta, min meta, fresh meta, ripe meta
    BARLEY(ModList.NATURA, 0xCDB14D, "crops", "seeds", 0, 0, 0, 3, VarType.INSTANCE),
    COTTON(ModList.NATURA, 0xE366F5, "crops", "seeds", 0, 4, 6, 8, VarType.INSTANCE),
    FLAX(
        ModList.BLUEPOWER,
        0xD9C482,
        "flax_crop",
        "flax_seeds",
        0,
        0,
        0,
        8,
        VarType.INSTANCE
    ),
    MAGIC(
        ModList.MAGICCROPS,
        0x6F9165,
        (CropHandlerBase) ModList.MAGICCROPS.getHandler("Handler")
    ),
    MANA(
        ModList.THAUMCRAFT,
        0x55aaff,
        "blockManaPod",
        "itemManaBean",
        0,
        0,
        0,
        7,
        VarType.INSTANCE
    ),
    BERRY(ModList.NATURA, 0x55ff33, BerryBushHandler.getInstance()),
    OREBERRY(ModList.TINKERER, 0xcccccc, OreBerryBushHandler.getInstance()),
    PAM(ModList.HARVESTCRAFT, 0x22aa22, HarvestCraftHandler.getInstance()),
    ALGAE(ModList.EMASHER, 0x29D855, "algae", 0, VarType.INSTANCE),
    ENDER(ModList.EXTRAUTILS, 0x00684A, "enderLily", 7, VarType.INSTANCE),
    PNEUMATIC(ModList.PNEUMATICRAFT, 0x37FF69, PneumaticPlantHandler.getInstance()),
    FLUXED(ModList.FLUXEDCRYSTALS, 0xd00000, FluxedCrystalHandler.getInstance()),
    AGRICRAFT(ModList.AGRICRAFT, 0x7E9612, AgriCraftHandler.getInstance()),
    ASPECT(
        ModList.THAUMICTINKER,
        0xC591D8,
        "infusedGrainBlock",
        "infusedGrain",
        0,
        0,
        0,
        7,
        VarType.REGISTRY
    ),
    RUBBERLOG(
        ModList.IC2,
        0xC66902,
        IC2RubberLogHandler.getInstance(
        ) /*"rubberWood", "resin", 0, 0, 0, 7, VarType.ITEMSTACK*/
    ),
    FORTREE(ModList.FORESTRY, 0x15A53C, ForestryHandler.getInstance()),
    ;

    private final ModEntry mod;
    public final Block blockID;
    public final Item seedID;
    public final int seedMeta;
    public final int ripeMeta;
    /** Not necessarily zero; see cotton */
    private final int harvestedMeta;
    private int minmeta;
    private final CropHandler handler;
    private String blockClass;
    private String itemClass;
    private boolean dropsSelf;

    public final int cropColor;

    private boolean exists;

    public static final ModCropList[] cropList = values();
    private static final BlockMap<ModCropList> cropMappings = new BlockMap();

    private ModCropList(
        ModList api, int color, String blockVar, int metaripe, VarType type
    ) {
        this(api, color, blockVar, 0, metaripe, type);
    }

    private ModCropList(
        ModList api, int color, String blockVar, int metamin, int metaripe, VarType type
    ) {
        dropsSelf = true;
        mod = api;
        ripeMeta = metaripe;
        harvestedMeta = metamin;
        handler = null;
        cropColor = color;

        Block id = null;
        Item seed = null;
        if (mod.isLoaded()) {
            Class blocks = api.getBlockClass();
            Class items = api.getItemClass();
            if (blocks == null) {
                DragonAPICore.logError(
                    "Error loading crop " + this + ": Empty block class"
                );
            } else if (items == null) {
                DragonAPICore.logError(
                    "Error loading crop " + this + ": Empty item class"
                );
            } else if (blockVar == null || blockVar.isEmpty()) {
                DragonAPICore.logError(
                    "Error loading crop " + this + ": Empty variable name"
                );
            } else {
                try {
                    Field b;
                    Field i;
                    switch (type) {
                        case ITEMSTACK:
                            b = blocks.getField(blockVar);
                            ItemStack is = (ItemStack) b.get(null);
                            if (is == null || is.getItem() == null) {
                                DragonAPICore.logError(
                                    "Error loading crop " + this
                                    + ": Block stack not instantiated @ "
                                    + blocks.getName() + "#" + blockVar + "!"
                                );
                            } else {
                                id = Block.getBlockFromItem(is.getItem());
                            }
                            break;
                        case INSTANCE: {
                            b = blocks.getField(blockVar);
                            Block block = (Block) b.get(null);
                            if (block == null) {
                                DragonAPICore.logError(
                                    "Error loading crop " + this
                                    + ": Block not instantiated @ " + blocks.getName()
                                    + "#" + blockVar + "!"
                                );
                            } else {
                                id = block;
                            }
                            break;
                        }
                        case REGISTRY: {
                            Block block
                                = GameRegistry.findBlock(mod.getModLabel(), blockVar);
                            if (block == null) {
                                DragonAPICore.logError(
                                    "Error loading crop " + this
                                    + ": Block not found @ GameRegistry "
                                    + mod.getModLabel() + ":" + blockVar + "!"
                                );
                            } else {
                                id = block;
                            }
                            break;
                        }
                        default:
                            DragonAPICore.logError("Error loading crop " + this);
                            DragonAPICore.logError(
                                "Invalid variable type for field " + blockVar
                            );
                    }
                } catch (NoSuchFieldException e) {
                    DragonAPICore.logError("Error loading crop " + this);
                    e.printStackTrace();
                    ReflectiveFailureTracker.instance.logModReflectiveFailure(mod, e);
                } catch (SecurityException e) {
                    DragonAPICore.logError("Error loading crop " + this);
                    e.printStackTrace();
                    ReflectiveFailureTracker.instance.logModReflectiveFailure(mod, e);
                } catch (IllegalAccessException e) {
                    DragonAPICore.logError("Error loading crop " + this);
                    e.printStackTrace();
                    ReflectiveFailureTracker.instance.logModReflectiveFailure(mod, e);
                } catch (IllegalArgumentException e) {
                    DragonAPICore.logError("Error loading crop " + this);
                    e.printStackTrace();
                    ReflectiveFailureTracker.instance.logModReflectiveFailure(mod, e);
                }
            }
        }
        blockID = id;
        seedID = Item.getItemFromBlock(blockID);
        seedMeta = 0;

        exists = blockID != null;
    }

    private ModCropList(ModEntry api, int color, CropHandler h) {
        handler = h;
        mod = api;
        blockID = null;
        seedID = null;
        seedMeta = -1;
        harvestedMeta = -1;
        ripeMeta = -1;
        cropColor = color;
        exists = h != null && h.initializedProperly();
    }

    private ModCropList(
        ModList api,
        int color,
        String blockVar,
        String itemVar,
        int seedItem,
        int metamin,
        int metafresh,
        int metaripe,
        VarType type
    ) {
        this(
            api,
            color,
            blockVar,
            itemVar,
            seedItem,
            metamin,
            metafresh,
            metaripe,
            type,
            type
        );
    }

    private ModCropList(
        ModList api,
        int color,
        String blockVar,
        String itemVar,
        int seedItem,
        int metamin,
        int metafresh,
        int metaripe,
        VarType blockType,
        VarType itemType
    ) {
        if (!DragonAPIInit.canLoadHandlers())
            throw new MisuseException(
                "Accessed registry enum too early! Wait until postInit!"
            );
        mod = api;
        harvestedMeta = metafresh;
        ripeMeta = metaripe;
        minmeta = metamin;
        cropColor = color;
        seedMeta = seedItem;
        handler = null;
        Block id = null;
        Item seed = null;
        if (mod.isLoaded()) {
            if (blockType == VarType.REGISTRY) {
                Item item = GameRegistry.findItem(mod.getModLabel(), itemVar);
                if (item == null) {
                    DragonAPICore.logError(
                        "Error loading crop " + this + ": Item not instantiated!"
                    );
                } else {
                    seed = item;
                }
            } else {
                Class blocks = api.getBlockClass();
                Class items = api.getItemClass();
                if (blocks == null) {
                    DragonAPICore.logError(
                        "Error loading crop " + this + ": Empty block class"
                    );
                } else if (items == null) {
                    DragonAPICore.logError(
                        "Error loading crop " + this + ": Empty item class"
                    );
                }
				else if (blockVar == null || blockVar.isEmpty() || itemVar == null || itemVar.isEmpty()) {
                    DragonAPICore.logError(
                        "Error loading crop " + this + ": Empty variable name"
                    );
                } else {
                    try {
                        Field b;
                        Field i;
                        switch (blockType) {
                            case ITEMSTACK:
                                b = blocks.getField(blockVar);
                                ItemStack is = (ItemStack) b.get(null);
                                if (is == null || is.getItem() == null) {
                                    DragonAPICore.logError(
                                        "Error loading crop " + this
                                        + ": Block stack not instantiated @ "
                                        + blocks.getName() + "#" + blockVar + "!"
                                    );
                                } else {
                                    id = Block.getBlockFromItem(is.getItem());
                                }
                                break;
                            case INSTANCE: {
                                b = blocks.getField(blockVar);
                                Block block = (Block) b.get(null);
                                if (block == null) {
                                    DragonAPICore.logError(
                                        "Error loading crop " + this
                                        + ": Block not instantiated @ " + blocks.getName()
                                        + "#" + blockVar + "!"
                                    );
                                } else {
                                    id = block;
                                }
                                break;
                            }
                            case REGISTRY: {
                                Block block
                                    = GameRegistry.findBlock(mod.getModLabel(), blockVar);
                                if (block == null) {
                                    DragonAPICore.logError(
                                        "Error loading crop " + this
                                        + ": Block not found @ GameRegistry "
                                        + mod.getModLabel() + ":" + blockVar + "!"
                                    );
                                } else {
                                    id = block;
                                }
                                break;
                            }
                            default:
                                DragonAPICore.logError("Error loading crop " + this);
                                DragonAPICore.logError(
                                    "Invalid variable type for field " + blockVar
                                );
                        }
                        switch (itemType) {
                            case ITEMSTACK:
                                i = items.getField(itemVar);
                                ItemStack is2 = (ItemStack) i.get(null);
                                if (is2 == null) {
                                    DragonAPICore.logError(
                                        "Error loading crop " + this
                                        + ": Seed not instantiated!"
                                    );
                                } else {
                                    seed = is2.getItem();
                                }
                                break;
                            case INSTANCE: {
                                i = items.getField(itemVar);
                                Item item = (Item) i.get(null);
                                if (item == null) {
                                    DragonAPICore.logError(
                                        "Error loading crop " + this
                                        + ": Seed not instantiated!"
                                    );
                                } else {
                                    seed = item;
                                }
                                break;
                            }
                            case REGISTRY: {
                                DragonAPICore.logError("Error loading crop " + this);
                                DragonAPICore.logError(
                                    "Skipped initial registry handling?!"
                                );
                                break;
                            }
                            default:
                                DragonAPICore.logError("Error loading crop " + this);
                                DragonAPICore.logError(
                                    "Invalid variable type for field " + itemVar
                                );
                                exists = false;
                        }
                    } catch (NoSuchFieldException e) {
                        DragonAPICore.logError("Error loading crop " + this);
                        e.printStackTrace();
                        ReflectiveFailureTracker.instance.logModReflectiveFailure(mod, e);
                    } catch (SecurityException e) {
                        DragonAPICore.logError("Error loading crop " + this);
                        e.printStackTrace();
                        ReflectiveFailureTracker.instance.logModReflectiveFailure(mod, e);
                    } catch (IllegalAccessException e) {
                        DragonAPICore.logError("Error loading crop " + this);
                        e.printStackTrace();
                        ReflectiveFailureTracker.instance.logModReflectiveFailure(mod, e);
                    } catch (IllegalArgumentException e) {
                        DragonAPICore.logError("Error loading crop " + this);
                        e.printStackTrace();
                        ReflectiveFailureTracker.instance.logModReflectiveFailure(mod, e);
                    }
                }
            }
        }
        blockID = id;
        seedID = seed;

        exists = blockID != null;
    }

    @Override
    public String toString() {
        if (this.isHandlered())
            return this.name() + " from " + mod + " with handler "
                + handler.getClass().getSimpleName();
        else
            return this.name() + " from " + mod + " with metadatas [" + harvestedMeta
                + "," + ripeMeta + "]";
    }

    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int fortune) {
        Block b = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        ArrayList<ItemStack> li = new ArrayList();
        if (blockID != null)
            li.addAll(blockID.getDrops(world, x, y, z, meta, fortune));
        else {
            if (b == Blocks.air)
                return new ArrayList();
            if (b != null)
                li.addAll(b.getDrops(world, x, y, z, meta, fortune));
        }
        if (this.isHandlered()) {
            ArrayList<ItemStack> li3
                = handler.getDropsOverride(world, x, y, z, b, meta, fortune);
            if (li3 != null) {
                li.clear();
                li.addAll(li3);
            }
            ArrayList<ItemStack> li2
                = handler.getAdditionalDrops(world, x, y, z, b, meta, fortune);
            if (li2 != null && !li2.isEmpty())
                li.addAll(li2);
        }
        return li;
    }

    public boolean isTileEntityUsedForGrowth() {
        return handler != null && handler.isTileEntity();
    }

    public void setHarvested(World world, int x, int y, int z) {
        if (this.isTileEntityUsedForGrowth())
            this.runTEHarvestCode(world, x, y, z);
        else
            world.setBlockMetadataWithNotify(
                x, y, z, this.getHarvestedMetadata(world, x, y, z), 3
            );
    }

    private void runTEHarvestCode(World world, int x, int y, int z) {
        if (!this.isTileEntityUsedForGrowth())
            return;
        handler.editTileDataForHarvest(world, x, y, z);
    }

    public boolean isSeedItem(ItemStack is) {
        if (this.isHandlered()) {
            return handler.isSeedItem(is);
        } else if (itemClass != null && !itemClass.isEmpty()) {
            return itemClass.equals(is.getItem().getClass().getSimpleName());
        } else {
            return seedID == is.getItem() && seedMeta == is.getItemDamage();
        }
    }

    public ModEntry getParentMod() {
        return mod;
    }

    public static ModCropList getModCrop(Block id, int meta) {
        ModCropList mod = cropMappings.get(id, meta);

        if (mod == null) {
            for (int i = 0; i < cropList.length && mod == null; i++) {
                ModCropList crop = cropList[i];
                if (crop.isHandlered()) {
                    if (crop.handler.isCrop(id, meta)) {
                        mod = crop;
                    }
                } else if (crop.blockClass != null && !crop.blockClass.isEmpty()) {
                    if (crop.blockClass.equals(id.getClass().getSimpleName())) {
                        mod = crop;
                    }
                } else {
                    if (crop.blockID == id
                        && ReikaMathLibrary.isValueInsideBoundsIncl(
                            crop.minmeta, crop.ripeMeta, meta
                        )) {
                        mod = crop;
                    }
                }
            }

            cropMappings.put(id, meta, mod);
        }

        return mod;
    }

    public static boolean isModCrop(Block id, int meta) {
        return getModCrop(id, meta) != null;
    }

    public boolean isCrop(Block id, int meta) {
        return this.isHandlered() ? handler.isCrop(id, meta)
                                  : blockID == id && meta >= minmeta && meta <= ripeMeta;
    }

    public boolean destroyOnHarvest() {
        return this == ALGAE || this == ASPECT;
    }

    public boolean isBerryBush() {
        return this == BERRY || this == OREBERRY;
    }

    public boolean isRipe(World world, int x, int y, int z) {
        return this.isHandlered() ? handler.isRipeCrop(world, x, y, z)
                                  : world.getBlockMetadata(x, y, z) >= ripeMeta;
    }

    public void makeRipe(World world, int x, int y, int z) {
        if (this.isHandlered()) {
            handler.makeRipe(world, x, y, z);
        } else {
            int metato = ripeMeta;
            world.setBlockMetadataWithNotify(x, y, z, metato, 3);
        }
    }

    private int getHarvestedMetadata(World world, int x, int y, int z) {
        return this.isHandlered() ? handler.getHarvestedMeta(world, x, y, z)
                                  : harvestedMeta;
    }

    public boolean isHandlered() {
        return handler != null;
    }

    public boolean existsInGame() {
        return exists && mod.isLoaded();
    }

    static {
        for (int i = 0; i < ModCropList.cropList.length; i++) {
            ModCropList c = ModCropList.cropList[i];
            if (c.existsInGame() && !c.isHandlered()) {
                Block b = c.blockID;
                for (int k = c.minmeta; k <= c.ripeMeta; k++)
                    cropMappings.put(b, k, c);
            }
        }
    }

    public static void addCustomCropType(CustomCropHandler ch) {
        String n = ch.getEnumEntryName().toUpperCase();
        try {
            ModCropList.valueOf(n);
            throw new IllegalArgumentException("Crop name " + n + " is already taken!");
        } catch (IllegalArgumentException e) {
            //Field was not used
        }
        try {
            Class[] argTypes
                = new Class[] { ModEntry.class, int.class, CropHandler.class };
            Object[] args = new Object[] { ch.getMod(), ch.getColor(), ch };
            ModCropList crop = EnumHelper.addEnum(ModCropList.class, n, argTypes, args);
            ReikaReflectionHelper.setFinalField(
                ModCropList.class, "cropList", null, values()
            );
        } catch (Exception e) {
            DragonAPICore.logError(
                "Could not add custom crop type '" + ch.getMod() + ": " + n + "'!"
            );
            e.printStackTrace();
        }
    }

    @Override
    public int getGrowthState(World world, int x, int y, int z) {
        return this.isHandlered() ? handler.getGrowthState(world, x, y, z)
                                  : world.getBlockMetadata(x, y, z);
    }

    @Override
    public boolean neverDropsSecondSeed() {
        return this.isHandlered() ? handler.neverDropsSecondSeed() : false;
    }

    /*
    @Override
    public CropFormat getShape() {
        if (this.isHandlered())
            return handler.getShape();
        switch(this) {
            case AGRICRAFT:
            case ASPECT:
            case BARLEY:
            case COTTON:
            case ENDER:
            case FLAX:
            case FLUXED:
            case MAGIC:
            case PAM:
            case ALGAE:
            case PNEUMATIC:
                return CropFormat.PLANT;
            case MANA:
                return CropFormat.POD;
            case BERRY:
            case OREBERRY:
                return CropFormat.BLOCK;
            case RUBBERLOG:
                return CropFormat.BLOCKSIDE;

        }
        return null;
    }*/
}
