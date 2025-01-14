/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.MeteorCraft;

import java.util.ArrayList;
import java.util.HashSet;

import Reika.DragonAPI.Auxiliary.BiomeTypeList;
import Reika.DragonAPI.Base.DragonAPIMod;
import Reika.DragonAPI.Instantiable.Data.Immutable.BlockKey;
import Reika.DragonAPI.Instantiable.IO.ControlledConfig;
import Reika.DragonAPI.Interfaces.Configuration.ConfigList;
import Reika.DragonAPI.Interfaces.Registry.IDRegistry;
import Reika.DragonAPI.Interfaces.Registry.OreType;
import Reika.DragonAPI.Libraries.Java.ReikaJavaLibrary;
import Reika.DragonAPI.Libraries.Java.ReikaStringParser;
import Reika.DragonAPI.Libraries.Registry.ReikaOreHelper;
import Reika.DragonAPI.ModInteract.ItemHandlers.RailcraftHandler;
import Reika.DragonAPI.ModInteract.ItemHandlers.TinkerBlockHandler;
import Reika.DragonAPI.ModList;
import Reika.DragonAPI.ModRegistry.ModOreList;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.oredict.OreDictionary;

public class MeteorConfig extends ControlledConfig {
    private static final ArrayList<String> modOres = getModOres();
    private static final int oreLength = modOres.size();
    private static final int vanillaOreCount = ReikaOreHelper.oreList.length;
    private final DataElement<Integer>[] ores
        = new DataElement[oreLength + vanillaOreCount];

    private final DataElement<Boolean>[] biomes
        = new DataElement[BiomeTypeList.biomeList.length];

    private final HashSet<BlockKey> allowedOreItems = new HashSet();

    public MeteorConfig(DragonAPIMod mod, ConfigList[] option, IDRegistry[] id) {
        super(mod, option, id);

        for (int i = 0; i < vanillaOreCount; i++) {
            String name = ReikaOreHelper.oreList[i].getName();
            ores[i] = this.registerAdditionalOption("Ore Weight", name + " Ore", 10);
        }
        for (int i = 0; i < oreLength; i++) {
            String name = modOres.get(i);
            ores[i + vanillaOreCount]
                = this.registerAdditionalOption("Ore Weight", name, 10);
        }

        for (int i = 0; i < biomes.length; i++) {
            String name = BiomeTypeList.biomeList[i].displayName;
            biomes[i]
                = this.registerAdditionalOption("Allowable Impact Biomes", name, true);
        }

        this.registerOrphanExclusion("Mod Ore Allowance");
    }

    public void initModExclusions() {
        this.load();

        for (int i = 0; i < ModOreList.oreList.length; i++) {
            ModOreList ore = ModOreList.oreList[i];
            String[] names = ore.getOreDictNames();
            boolean multiMod = false;
            boolean multiItem = false;
            boolean noOres = true;
            for (int k = 0; k < names.length; k++) {
                String tag = names[k];
                ArrayList<ItemStack> li = OreDictionary.getOres(tag);
                if (li.size() > 1) {
                    noOres = false;
                    multiItem = true;
                    ArrayList<ModList> mods = new ArrayList();
                    for (int h = 0; h < li.size(); h++) {
                        ItemStack is = li.get(h);
                        ModList mod = ModOreList.getOreModFromItemStack(is);
                        if (mod != null && !mods.contains(mod))
                            mods.add(mod);
                    }
                    if (mods.size() > 1) {
                        multiMod = true;
                        MeteorCraft.logger.log(
                            "Found the following mods for " + ore.displayName + ":"
                        );
                        MeteorCraft.logger.log("\t" + mods);
                        for (int h = 0; h < li.size(); h++) {
                            ItemStack is = li.get(h);
                            ModList mod = ModOreList.getOreModFromItemStack(is);
                            if (mod != null) {
                                boolean allow
                                    = this.getProperty(
                                              "Mod Ore Allowance - " + ore.displayName,
                                              mod.modLabel,
                                              true
                                    )
                                          .getBoolean(true);
                                if (allow)
                                    this.addModOreAllowance(is);
                            }
                        }
                    } else {
                        for (int h = 0; h < li.size(); h++) {
                            ItemStack is = li.get(h);
                            this.addModOreAllowance(is);
                        }
                    }
                } else if (li.size() > 0) {
                    noOres = false;
                    this.addModOreAllowance(li.get(0));
                }
            }
            if (noOres) {
                MeteorCraft.logger.log(
                    "No items found for " + ore.displayName
                    + ". Not adding mod control configuration."
                );
            } else if (!multiItem) {
                MeteorCraft.logger.log(
                    "Only found one item for " + ore.displayName
                    + ". Not adding mod control configuration."
                );
            } else if (!multiMod) {
                MeteorCraft.logger.log(
                    "Found multiple items but only one mod for " + ore.displayName
                    + ". Not adding mod control configuration."
                );
            }
        }

        this.save();
    }

    private void addModOreAllowance(ItemStack is) {
        allowedOreItems.add(BlockKey.fromItem(is));
    }

    private static ArrayList<String> getModOres() {
        ArrayList<String> base
            = ReikaJavaLibrary.getEnumEntriesWithoutInitializing(ModOreList.class);
        ArrayList<String> li = new ArrayList();
        for (int i = 0; i < base.size(); i++) {
            StringBuilder sb = new StringBuilder();
            String sg = base.get(i);
            if (sg.startsWith("NETHER")) {
                sg = sg.substring(6);
                sb.append("Nether ");
                sb.append(ReikaStringParser.capFirstChar(sg));
                sb.append(" Ore");
            } else if (sg.startsWith("INFUSED")) {
                sg = sg.substring(7);
                sb.append(ReikaStringParser.capFirstChar(sg));
                sb.append(" Infused Stone");
            } else if (sg.startsWith("BLUE")) {
                sg = sg.substring(4);
                sb.append("Blue ");
                sb.append(ReikaStringParser.capFirstChar(sg));
                sb.append(" Ore");
            } else if (sg.startsWith("GREEN")) {
                sg = sg.substring(5);
                sb.append("Green ");
                sb.append(ReikaStringParser.capFirstChar(sg));
                sb.append(" Ore");
            } else {
                sb.append(ReikaStringParser.capFirstChar(sg));
                sb.append(" Ore");
            }
            String s2 = sb.toString();
            s2 = s2.replaceAll("Pigiron", "Pig Iron");
            s2 = s2.replaceAll("Certusquartz", "Certus Quartz");
            li.add(s2);
        }
        return li;
    }

    /*
    @Override
    protected void resetConfigFile() {
        String path = this.getConfigPath()+"_Old_Config_Backup.txt";
        File backup = new File(path);
        if (backup.exists())
            backup.delete();
        try {
            ReikaJavaLibrary.pConsole(configMod.getDisplayName().toUpperCase()+": Writing
    Backup File to "+path);
            ReikaJavaLibrary.pConsole(configMod.getDisplayName().toUpperCase()+": Use this
    to restore custom IDs if necessary."); backup.createNewFile(); if (!backup.exists())
                ReikaJavaLibrary.pConsole(configMod.getDisplayName().toUpperCase()+":
    Could not create backup file at "+path+"!"); else { PrintWriter p = new
    PrintWriter(backup); p.println("#####----------THESE ARE ALL THE OLD CONFIG SETTINGS
    YOU WERE USING----------#####"); p.println("#####---IF THEY DIFFER FROM THE DEFAULTS,
    YOU MUST RE-EDIT THE CONFIG FILE---#####"); for (int i = 0; i <
    MeteorOptions.optionList.length; i++) { String label =
    MeteorOptions.optionList[i].getLabel(); if (MeteorOptions.optionList[i].isBoolean())
                        controls[i] = MeteorOptions.optionList[i].setState(config);
                    if (MeteorOptions.optionList[i].isNumeric())
                        controls[i] = MeteorOptions.optionList[i].setValue(config);
                    if (MeteorOptions.optionList[i].isDecimal())
                        controls[i] = MeteorOptions.optionList[i].setDecimal(config);
                    p.println(label+": "+String.valueOf(controls[i]));
                }

                for (int i = 0; i < vanillaOreCount; i++) {
                    String name = ReikaOreHelper.oreList[i].getName();
                    ores[i] = config.get("Generate Vanilla Ores", name,
    true).getBoolean(true);
                }
                for (int i = 0; i < oreLength; i++) {
                    String name = modOres.get(i);
                    ores[i+vanillaOreCount] = config.get("Generate Mod Ores", name,
    true).getBoolean(true);
                }

                for (int i = 0; i < biomes.length; i++) {
                    String name = BiomeTypeList.biomeList[i].displayName;
                    biomes[i] = config.get("Allowable Impact Biomes", name,
    true).getBoolean(true);
                }

                p.close();
            }
        }
        catch (IOException e) {
            ReikaJavaLibrary.pConsole(configMod.getDisplayName().toUpperCase()+": Could
    not create backup file due to IOException!"); e.printStackTrace();
        }
        configFile.delete();
    }*/

    private int getOreIndex(OreType ore) {
        return ore instanceof ModOreList ? ore.ordinal() + ReikaOreHelper.oreList.length
                                         : ore.ordinal();
    }

    public int getOreWeight(OreType ore) {
        return ores[this.getOreIndex(ore)].getData();
    }

    public boolean shouldGenerateOre(OreType ore) {
        return ores[this.getOreIndex(ore)].getData() > 0;
    }

    public void setOreWeight(OreType ore, int wt) {
        ores[this.getOreIndex(ore)].setData(wt);
    }

    public boolean canImpactInBiome(BiomeGenBase biome) {
        BiomeTypeList b = BiomeTypeList.getEntry(biome);
        return b != null ? biomes[b.ordinal()].getData() : true;
    }

    public boolean isItemStackGenerationPermitted(BlockKey is) {
        if (ModList.TINKERER.isLoaded()
            && is.blockID == TinkerBlockHandler.getInstance().gravelOreID)
            return false;
        if (is.blockID == null)
            return false;
        if (is.blockID.getClass().getName().startsWith("shukaro.artifice"
            )) //artifice ore variants
            return false;
        if (ModList.RAILCRAFT.isLoaded()
            && RailcraftHandler.getInstance().isDarkOre(is.blockID, is.metadata))
            return false;
        return allowedOreItems.contains(is);
    }
}
