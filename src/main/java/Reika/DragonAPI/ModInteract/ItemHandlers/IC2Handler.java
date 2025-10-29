package Reika.DragonAPI.ModInteract.ItemHandlers;

import java.lang.reflect.Field;

import Reika.DragonAPI.Base.ModHandlerBase;
import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.Libraries.Registry.ReikaItemHelper;
import Reika.DragonAPI.ModList;
import cpw.mods.fml.common.Loader;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class IC2Handler extends ModHandlerBase {
    private static final IC2Handler instance = new IC2Handler();
    private boolean init = false;

    private IC2Handler() {
        super();
        if (this.hasMod()) {
            Class ic2 = this.getMod().getItemClass();

            boolean exc = false;

            for (int i = 0; i < IC2Stacks.list.length; i++) {
                IC2Stacks s = IC2Stacks.list[i];
                try {
                    Field f = ic2.getField(s.tag);
                    s.stack = (ItemStack) f.get(null);
                } catch (NoSuchFieldException e) {
                    DragonAPICore.logError(
                        this.getMod() + " field not found! " + e.getMessage()
                    );
                    e.printStackTrace();
                    this.logFailure(e);
                    exc = true;
                } catch (SecurityException e) {
                    DragonAPICore.logError(
                        "Cannot read " + this.getMod() + " (Security Exception)! "
                        + e.getMessage()
                    );
                    e.printStackTrace();
                    this.logFailure(e);
                    exc = true;
                } catch (IllegalArgumentException e) {
                    DragonAPICore.logError(
                        "Illegal argument for reading " + this.getMod() + "!"
                    );
                    e.printStackTrace();
                    this.logFailure(e);
                    exc = true;
                } catch (IllegalAccessException e) {
                    DragonAPICore.logError(
                        "Illegal access exception for reading " + this.getMod() + "!"
                    );
                    e.printStackTrace();
                    this.logFailure(e);
                    exc = true;
                } catch (NullPointerException e) {
                    DragonAPICore.logError(
                        "Null pointer exception for reading " + this.getMod()
                        + "! Was the class loaded?"
                    );
                    e.printStackTrace();
                    this.logFailure(e);
                    exc = true;
                }
            }

            for (int i = 0; i < IC2Ores.list.length; i++) {
                IC2Ores s = IC2Ores.list[i];
                try {
                    Field f = ic2.getField(s.tag);
                    s.oreBlock
                        = Block.getBlockFromItem(((ItemStack) f.get(null)).getItem());
                } catch (NoSuchFieldException e) {
                    DragonAPICore.logError(
                        this.getMod() + " field not found! " + e.getMessage()
                    );
                    e.printStackTrace();
                    this.logFailure(e);
                    exc = true;
                } catch (SecurityException e) {
                    DragonAPICore.logError(
                        "Cannot read " + this.getMod() + " (Security Exception)! "
                        + e.getMessage()
                    );
                    e.printStackTrace();
                    this.logFailure(e);
                    exc = true;
                } catch (IllegalArgumentException e) {
                    DragonAPICore.logError(
                        "Illegal argument for reading " + this.getMod() + "!"
                    );
                    e.printStackTrace();
                    this.logFailure(e);
                    exc = true;
                } catch (IllegalAccessException e) {
                    DragonAPICore.logError(
                        "Illegal access exception for reading " + this.getMod() + "!"
                    );
                    e.printStackTrace();
                    this.logFailure(e);
                    exc = true;
                } catch (NullPointerException e) {
                    DragonAPICore.logError(
                        "Null pointer exception for reading " + this.getMod()
                        + "! Was the class loaded?"
                    );
                    e.printStackTrace();
                    this.logFailure(e);
                    exc = true;
                }
            }

            init = !exc;
        } else {
            this.noMod();
        }
    }

    public enum IC2Ores {
        COPPER("copperOre"),
        TIN("tinOre"),
        LEAD("leadOre"),
        URANIUM("uraniumOre");

        private final String tag;

        private Block oreBlock;

        private IC2Ores(String s) {
            tag = s;
        }

        private static final IC2Ores[] list = values();

        public Block getBlock() {
            return oreBlock;
        }
    }

    public enum IC2Materials {
        COPPER,
        TIN,
        BRONZE,
        GOLD,
        IRON,
        REFIRON,
        LEAD,
        OBSIDIAN,
        LAPIS;

        public ItemStack getDensePlate() {
            return ReikaItemHelper.lookupItem("IC2:itemDensePlates:" + this.ordinal());
        }

        public ItemStack getPlate() {
            return ReikaItemHelper.lookupItem("IC2:itemPlates:" + this.ordinal());
        }

        public ItemStack getCasing() {
            return this == OBSIDIAN || this == LAPIS
                ? null
                : ReikaItemHelper.lookupItem("IC2:itemCasing:" + this.ordinal());
        }
    }

    public enum IC2Cables {
        INSCOPPER,
        COPPER,
        GOLD,
        INSGOLD,
        UNUSED,
        HV,
        INSHV,
        UNUSED2,
        UNUSED3,
        FIBER,
        TIN,
        DETECTOR,
        SPLITTER,
        INSTIN;

        public ItemStack getItem() {
            return ReikaItemHelper.lookupItem("IC2:itemCable:" + this.ordinal());
        }
    }

    public enum IC2Stacks {
        PURECRUSHEDU("purifiedCrushedUraniumOre"),
        ENERGIUM("energiumDust"),
        ADVANCEDALLOY("advancedAlloy"),
        LAPOTRON("lapotronCrystal"),
        CARBONFIBER("carbonPlate"),
        IRIDPLATE("iridiumPlate"),
        RUBBER("rubber"),
        RESIN("resin"),
        SCRAP("scrap"),
        SCRAPBOX("scrapBox"),
        IRIDIUM("iridiumOre"),
        BRONZEPICK("bronzePickaxe"),
        BRONZEAXE("bronzeAxe"),
        BRONZESWORD("bronzeSword"),
        BRONZESHOVEL("bronzeShovel"),
        BRONZEHOE("bronzeHoe"),
        BRONZEHELMET("bronzeHelmet"),
        BRONZECHESTPLATE("bronzeChestplate"),
        BRONZELEGGINGS("bronzeLeggings"),
        BRONZEBOOTS("bronzeBoots"),
        BIOCHAFF("biochaff"),
        PLANTBALL("plantBall"),
        U235("Uran235"),
        U238("Uran238"),
        Pu239("Plutonium"),
        U235_TINY("smallUran235"),
        Pu239_TINY("smallPlutonium"),
        FUEL("UranFuel"),
        MOX("MOXFuel"),
        FUELROD("reactorUraniumSimple"),
        FUELROD2("reactorUraniumDual"),
        FUELROD4("reactorUraniumQuad"),
        DEPLETEDROD("reactorDepletedUraniumSimple"),
        DEPLETEDROD2("reactorDepletedUraniumDual"),
        DEPLETEDROD4("reactorDepletedUraniumQuad"),
        MOXROD("reactorMOXSimple"),
        MOXROD2("reactorMOXDual"),
        MOXROD4("reactorMOXQuad"),
        DEPLETEDMOXROD("reactorDepletedMOXSimple"),
        DEPLETEDMOXROD2("reactorDepletedMOXDual"),
        DEPLETEDMOXROD4("reactorDepletedMOXQuad"),
        REFLECTOR("reactorReflector"),
        DENSEREFLECTOR("reactorReflectorThick"),
        REFINEDIRON("advIronIngot"),
        OVERCLOCK("overclockerUpgrade"),
        TRANSFORMER("transformerUpgrade"),
        CAPACITY("energyStorageUpgrade"),
        RSH("reactorCondensator"),
        LZH("reactorCondensatorLap"),
        VENT("reactorVent"),
        REACVENT("reactorVentCore"),
        ADVENT("reactorVentDiamond"),
        OVERVENT("reactorVentGold"),
        CASTING("machine"),
        ADVCASING("advancedMachine"),
        COOLANT6("reactorCoolantSix"),
        ;

        private final String tag;
        private ItemStack stack;

        private static final IC2Stacks[] list = values();

        private IC2Stacks(String s) {
            tag = s;
        }

        public ItemStack getItem() {
            return stack != null ? stack.copy() : null;
        }

        public boolean match(ItemStack is) {
            return ReikaItemHelper.matchStacks(is, stack);
        }
    }

    public static IC2Handler getInstance() {
        return instance;
    }

    @Override
    public boolean initializedProperly() {
        return init;
    }

    @Override
    public ModList getMod() {
        return ModList.IC2;
    }

    public boolean isIC2Classic() {
        return ModList.IC2.getRegisteredName().equals("Industrial Craft Classic")
            || Loader.isModLoaded("IC2-Classic-Spmod");
    }
}
