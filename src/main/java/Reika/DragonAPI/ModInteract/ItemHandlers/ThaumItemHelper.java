package Reika.DragonAPI.ModInteract.ItemHandlers;

import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.Interfaces.Registry.OreType;
import Reika.DragonAPI.Libraries.Registry.ReikaItemHelper;
import Reika.DragonAPI.Libraries.Registry.ReikaOreHelper;
import Reika.DragonAPI.ModInteract.DeepInteract.ReikaThaumHelper;
import Reika.DragonAPI.ModList;
import Reika.DragonAPI.ModRegistry.ModOreList;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.api.IWarpingGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;

public class ThaumItemHelper {
    public static enum ItemEntry {
        ALUMENTUM("ItemResource", 0),
        NITOR("ItemResource", 1),
        THAUMIUM("ItemResource", 2),
        TALLOW("ItemResource", 4),
        FABRIC("ItemResource", 7),
        VISFITLER("ItemResource", 8),
        FRAGMENT("ItemResource", 9),
        MIRROR("ItemResource", 10),
        GOO("ItemResource", 11),
        TENDRIL("ItemResource", 12),
        LABEL("ItemResource", 13),
        SALIS("ItemResource", 14),
        CHARM("ItemResource", 15),
        VOIDMETAL("ItemResource", 16),
        VOIDSEED("ItemResource", 17),
        COIN("ItemResource", 18),
        PRIMALFOCUS("FocusPrimal"),
        SALTS("ItemBathSalts"),
        ELDRITCHEYE("ItemEldritchObject"),
        BALANCED("ItemShard", 6),
        PHIAL("ItemEssence", 0),
        FILLEDPHIAL("ItemEssence", 1),
        CRYSTALESSENCE("ItemCrystalEssence", 0),
        NUGGETCLUSTER("ItemNugget", 0),
        LOOTBAG1("ItemLootBag", 0),
        LOOTBAG2("ItemLootBag", 1),
        LOOTBAG3("ItemLootBag", 2),
        MANABEAN("ItemManaBean"),
        DEFUNCTBRAIN("ItemResource", 5),
        ZOMBIEBRAIN("ItemZombieBrain"),
        WISP("ItemWispEssence"),
        RESEARCH("ItemResearchNotes"),
        WAND("WandCasting"),
        THAUMOMETER("ItemThaumometer"),
        ;

        public final int metadata;
        private final String item;

        private ItemEntry(String s) {
            this(s, 0);
        }

        private ItemEntry(String s, int meta) {
            item = s;
            metadata = meta;
        }

        public ItemStack getItem() {
            Item is = GameRegistry.findItem(ModList.THAUMCRAFT.modLabel, item);
            return new ItemStack(is, 1, metadata);
        }
    }

    public static enum BlockEntry {
        TOTEM("blockCosmeticSolid", 0),
        TILE("blockCosmeticSolid", 1),
        THAUMIUM("blockCosmeticSolid", 4),
        TALLOW("blockCosmeticSolid", 5),
        ARCANESTONE("blockCosmeticSolid", 6),
        ARCANEBRICKS("blockCosmeticSolid", 7),
        ANCIENTSTONE("blockCosmeticSolid", 11),
        ANCIENTROCK("blockCosmeticSolid", 12),
        CRUSTEDSTONE("blockCosmeticSolid", 14),
        ANCIENTPEDESTAL("blockCosmeticSolid", 15),
        SHIMMER("blockCustomPlant", 2),
        CINDER("blockCustomPlant", 3),
        ETHEREAL("blockCustomPlant", 4),
        CRYSTAL("blockCrystal"),
        JAR("blockJar"),
        NODE("blockAiry", 0),
        TOTEMNODE("blockAiry", 4),
        GREATWOODPLANKS("blockWoodenDevice", 6),
        SILVERWOODPLANKS("blockWoodenDevice", 7),
        TAINT("blockTaint"),
        TENDRILS("blockTaintFibres"),
        HOLE("blockHole"),
        ;

        public final int metadata;
        private final String item;

        private BlockEntry(String s) {
            this(s, 0);
        }

        private BlockEntry(String s, int meta) {
            item = s;
            metadata = meta;
        }

        public Block getBlock() {
            return GameRegistry.findBlock(ModList.THAUMCRAFT.modLabel, item);
        }

        public ItemStack getItem() {
            return new ItemStack(this.getBlock(), 1, metadata);
        }

        public boolean match(Block b, int meta) {
            return b == this.getBlock() && meta == metadata;
        }
    }

    public static boolean isTotemBlock(Block b, int meta) {
        return (b == BlockEntry.TOTEM.getBlock() && meta < 2)
            || (b == BlockEntry.TOTEMNODE.getBlock()
                && meta == BlockEntry.TOTEMNODE.metadata);
    }

    public static boolean isCrystalCluster(Block b) {
        return b == BlockEntry.CRYSTAL.getBlock();
    }

    public static boolean isWarpingToolOrArmor(ItemStack is) {
        return is != null && is.getItem() instanceof IWarpingGear;
    }

    public static boolean isVoidMetalTool(ItemStack is) {
        return is != null
            && is.getItem().getClass().getName().startsWith(
                "thaumcraft.common.items.equipment.ItemVoid"
            );
    }

    public static boolean isVoidMetalArmor(ItemStack is) {
        return is != null
            && is.getItem().getClass().getName().startsWith(
                "thaumcraft.common.items.armor.ItemVoid"
            );
    }

    public static ItemStack getPhialEssentia(Aspect a) {
        ItemStack is = ItemEntry.FILLEDPHIAL.getItem();
        ((IEssentiaContainerItem) is.getItem())
            .setAspects(is, new AspectList().add(a, 8));
        return is;
    }

    public static ItemStack getCrystallizedEssentia(Aspect a) {
        ItemStack is = ItemEntry.CRYSTALESSENCE.getItem();
        ((IEssentiaContainerItem) is.getItem())
            .setAspects(is, new AspectList().add(a, 1));
        return is;
    }

    public static ItemStack getManaBean(Aspect a) {
        ItemStack is = ItemEntry.MANABEAN.getItem();
        ((IEssentiaContainerItem) is.getItem())
            .setAspects(is, new AspectList().add(a, 1));
        return is;
    }

    public static ItemStack getWispEssence(Aspect a) {
        ItemStack is = ItemEntry.WISP.getItem();
        ((IEssentiaContainerItem) is.getItem())
            .setAspects(is, new AspectList().add(a, 2)); //2, not 1
        return is;
    }

    public static ItemStack getNativeCluster(OreType ore) {
        int meta = -1;
        if (ore == ReikaOreHelper.IRON) {
            meta = 16;
        } else if (ore == ReikaOreHelper.GOLD) {
            meta = 31;
        } else if (ore instanceof ModOreList) {
            switch ((ModOreList) ore) {
                case TIN:
                    meta = 18;
                    break;
                case COPPER:
                    meta = 17;
                    break;
                case SILVER:
                    meta = 19;
                    break;
                case LEAD:
                    meta = 20;
                    break;
                case CINNABAR:
                    meta = 21;
                    break;
                default:
                    break;
            }
        }
        if (Loader.isModLoaded("WitchingGadgets")) {
            if (ore == ModOreList.ALUMINUM) {
                return ReikaItemHelper.lookupItem("WitchingGadgets:item.WG_Cluster");
            } else if (ore == ModOreList.NICKEL) {
                return ReikaItemHelper.lookupItem("WitchingGadgets:item.WG_Cluster:3");
            } else if (ore == ModOreList.PLATINUM) {
                return ReikaItemHelper.lookupItem("WitchingGadgets:item.WG_Cluster:7");
            }
        }
        return meta < 0
            ? null
            : new ItemStack(ItemEntry.NUGGETCLUSTER.getItem().getItem(), 1, meta);
    }

    public static ItemStack getResearchNote(String key, World world, boolean complete) {
        ItemStack is = ItemEntry.RESEARCH.getItem();
        ReikaThaumHelper.programResearchNote(is, key, world);
        if (is.stackTagCompound == null) {
            DragonAPICore.logError("Research '" + key + "' does not exist!");
            return null;
        }
        is.stackTagCompound.setBoolean("complete", complete);
        if (complete) {
            is.setItemDamage(64);
        }
        return is;
    }
}
