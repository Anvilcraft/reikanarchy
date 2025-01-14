/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.Auxiliary.RecipeManagers;

import java.util.Collection;

import Reika.ChromatiCraft.API.ChromatiAPI;
import Reika.ChromatiCraft.API.CrystalElementAccessor;
import Reika.ChromatiCraft.API.CrystalElementAccessor.CrystalElementProxy;
import Reika.ChromatiCraft.API.DyeTreeAPI;
import Reika.DragonAPI.Instantiable.Data.Maps.ItemHashMap;
import Reika.DragonAPI.Libraries.Registry.ReikaItemHelper;
import Reika.DragonAPI.ModInteract.ItemHandlers.ForestryHandler;
import Reika.DragonAPI.ModInteract.ItemHandlers.IC2Handler;
import Reika.DragonAPI.ModList;
import Reika.DragonAPI.ModRegistry.ModCropList;
import Reika.DragonAPI.ModRegistry.ModWoodList;
import Reika.RotaryCraft.Auxiliary.ItemStacks;
import Reika.RotaryCraft.Registry.PlantMaterials;
import Reika.RotaryCraft.RotaryCraft;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MulchMaterials {
    public static final MulchMaterials instance = new MulchMaterials();

    private final ItemHashMap<Integer> values = new ItemHashMap().setOneWay();

    private MulchMaterials() {
        if (ModList.CHROMATICRAFT.isLoaded()) {
            DyeTreeAPI api = ChromatiAPI.getAPI().trees();
            for (int i = 0; i < 16; i++) {
                CrystalElementProxy e = CrystalElementAccessor.getByIndex(i);
                this.addValue(
                    api.getDyeSapling(e), PlantMaterials.SAPLING.getPlantValue()
                );
                this.addValue(
                    api.getDyeLeaf(e, false), PlantMaterials.LEAVES.getPlantValue()
                );
                this.addValue(api.getDyeFlower(e), PlantMaterials.FLOWER.getPlantValue());
            }

            this.addValue(api.getRainbowLeaf(), 16);
            this.addValue(api.getRainbowSapling(), 8);
        }

        if (ModList.FORESTRY.isLoaded()) {
            this.addValue(ForestryHandler.ItemEntry.SAPLING.getItem(), 2);
            this.addValue(ForestryHandler.ItemEntry.HONEY.getItem(), 1);
            this.addValue(ForestryHandler.ItemEntry.HONEYDEW.getItem(), 1);
            this.addValue(ForestryHandler.BlockEntry.LEAF.getBlock(), 4);
        }

        if (ModList.EMASHER.isLoaded()) {
            this.addValue(ModCropList.ALGAE.blockID, 3);
        }

        if (ModList.IC2.isLoaded()) {
            ItemStack plantball = ReikaItemHelper.lookupItem("IC2:itemFuelPlantBall");
            if (plantball != null)
                this.addValue(plantball, 2);
            ItemStack chaff = IC2Handler.IC2Stacks.BIOCHAFF.getItem();
            if (chaff != null)
                this.addValue(chaff, 1);
        }

        if (ModList.BOTANIA.isLoaded()) {
            Item petal = GameRegistry.findItem(ModList.BOTANIA.modLabel, "petal");
            Block flower = GameRegistry.findBlock(ModList.BOTANIA.modLabel, "flower");
            Block tallflower1
                = GameRegistry.findBlock(ModList.BOTANIA.modLabel, "doubleFlower1");
            Block tallflower2
                = GameRegistry.findBlock(ModList.BOTANIA.modLabel, "doubleFlower2");
            for (int i = 0; i < 16; i++) {
                Block tall = i >= 8 ? tallflower2 : tallflower1;
                int tallm = i % 8;
                this.addValue(new ItemStack(flower, 1, i), 4);
                this.addValue(new ItemStack(tall, 1, tallm), 8);
                this.addValue(new ItemStack(petal, 1, i), 2);
            }
        }

        if (Loader.isModLoaded("Growthcraft|Apples")) {
            ItemStack core
                = ReikaItemHelper.lookupItem("Growthcraft|Apples:grc.appleSeeds");
            if (core != null) {
                this.addValue(core, 1);
            }
        }

        for (int i = 0; i < ModWoodList.woodList.length; i++) {
            ModWoodList wood = ModWoodList.woodList[i];
            if (wood.exists()) {
                this.addValue(
                    wood.getCorrespondingSapling(),
                    PlantMaterials.SAPLING.getPlantValue() * this.getModWoodValue(wood)
                );
                for (ItemStack leaf : wood.getAllLeaves()) {
                    if (leaf == null || leaf.getItem() == null) {
                        RotaryCraft.logger.logError(
                            "Tried to add mulch recipe for a stack (" + wood
                            + " leaves) which does not exist!"
                        );
                    } else if (!values.containsKey(leaf))
                        this.addValue(
                            leaf,
                            PlantMaterials.LEAVES.getPlantValue()
                                * this.getModWoodValue(wood)
                        );
                }
            }
        }
        for (int i = 0; i < PlantMaterials.plantList.length; i++) {
            PlantMaterials plant = PlantMaterials.plantList[i];
            int[] metas = plant.getMetadatas();
            for (int k = 0; k < metas.length; k++) {
                ItemStack is = plant.getPlantItem();
                is.setItemDamage(metas[k]);
                this.addValue(is, plant.getPlantValue());
            }
        }
        this.addValue(ItemStacks.sawdust.copy(), 1);
    }

    private void addValue(Item i, int amt) {
        this.addValue(new ItemStack(i), amt);
    }

    private void addValue(Block i, int amt) {
        this.addValue(new ItemStack(i), amt);
    }

    private void addValue(ItemStack is, int amt) {
        if (is == null || is.getItem() == null) {
            RotaryCraft.logger.logError(
                "Tried to add mulch recipe for a stack which does not exist!"
            );
            Thread.dumpStack();
            return;
        }
        if (amt <= 0) {
            RotaryCraft.logger.logError(
                "Tried to add mulch recipe for " + is + " (" + is.getDisplayName()
                + ") that produces zero sludge?!"
            );
            return;
        }
        values.put(is, amt);
    }

    public Collection<ItemStack> getAllValidPlants() {
        return values.keySet();
    }

    public boolean isMulchable(ItemStack is) {
        return values.containsKey(is);
    }

    public int getPlantValue(ItemStack is) {
        Integer ret = values.get(is);
        return ret != null ? ret.intValue() : 0;
    }

    /*
    public static List<ItemStack> getAllValidPlants() {
        List<ItemStack> in = new ArrayList();
        for (int i = 0; i < PlantMaterials.plantList.length; i++) {
            PlantMaterials p = PlantMaterials.plantList[i];
            Item item = p.getPlantItem().getItem();
            item.getSubItems(item, item.getCreativeTab(), in);
        }
        for (int i = 0; i < ModWoodList.woodList.length; i++) {
            if (ModWoodList.woodList[i].exists()) {
                in.add(ModWoodList.woodList[i].getCorrespondingSapling());
                in.add(ModWoodList.woodList[i].getCorrespondingLeaf());
            }
        }
        if (ModList.CHROMATICRAFT.isLoaded()) {
            for (int j = 0; j < 16; j++) {
                in.add(TreeGetter.getDyeSapling(j));
                in.add(TreeGetter.getHeldDyeLeaf(j));
                in.add(TreeGetter.getDyeFlower(j));
            }
            in.add(TreeGetter.getRainbowLeaf());
            in.add(TreeGetter.getRainbowSapling());
        }
        if (ModList.EMASHER.isLoaded()) {
            in.add(new ItemStack(ModCropList.ALGAE.blockID, 1, 0));
        }
        if (ModList.FORESTRY.isLoaded()) {
            in.add(new ItemStack(ForestryHandler.ItemEntry.SAPLING.getItem()));
            in.add(new ItemStack(ForestryHandler.BlockEntry.LEAF.getBlock()));
            in.add(new ItemStack(ForestryHandler.ItemEntry.HONEY.getItem()));
            in.add(new ItemStack(ForestryHandler.ItemEntry.HONEYDEW.getItem()));
        }
        return in;
    }

    public static int getPlantValue(ItemStack is) {
        if (is == null)
            return 0;
        if (ModList.CHROMATICRAFT.isLoaded()) {
            if (TreeGetter.isDyeSapling(is))
                return PlantMaterials.SAPLING.getPlantValue();
            if (TreeGetter.isDyeLeaf(is))
                return PlantMaterials.LEAVES.getPlantValue();
            if (TreeGetter.isDyeFlower(is))
                return PlantMaterials.FLOWER.getPlantValue();
            if (TreeGetter.isRainbowLeaf(is))
                return 16;
            if (TreeGetter.isRainbowSapling(is))
                return 8;
        }
        if (ModList.FORESTRY.isLoaded() && is.getItem() ==
    ForestryHandler.ItemEntry.SAPLING.getItem()) { return 2;
        }
        if (ModList.FORESTRY.isLoaded() && is.getItem() ==
    ForestryHandler.ItemEntry.HONEY.getItem()) { return 1;
        }
        if (ModList.FORESTRY.isLoaded() && is.getItem() ==
    ForestryHandler.ItemEntry.HONEYDEW.getItem()) { return 1;
        }
        if (ModList.FORESTRY.isLoaded() && ReikaItemHelper.matchStackWithBlock(is,
    ForestryHandler.BlockEntry.LEAF.getBlock())) { return 4;
        }
        if (ModList.EMASHER.isLoaded() && ReikaItemHelper.matchStackWithBlock(is,
    ModCropList.ALGAE.blockID)) { return 3;
        }
        ModWoodList sap = ModWoodList.getModWoodFromSapling(is);
        if (sap != null) {
            return PlantMaterials.SAPLING.getPlantValue()*getModWoodValue(sap);
        }
        ModWoodList leaf = ModWoodList.getModWoodFromLeaf(is);
        if (leaf != null) {
            return PlantMaterials.LEAVES.getPlantValue()*getModWoodValue(leaf);
        }
        PlantMaterials plant = PlantMaterials.getPlantEntry(is);
        if (plant == null)
            return 0;
        return plant.getPlantValue();
    }
     */
    private int getModWoodValue(ModWoodList wood) {
        if (wood == null)
            return 0;
        if (wood.isRareTree())
            return 32;
        if (wood == ModWoodList.LIGHTED)
            return 12;
        ModList mod = wood.getParentMod();
        if (mod == ModList.THAUMCRAFT)
            return 4;
        if (mod == ModList.TWILIGHT)
            return 3;
        return 1;
    }
}
