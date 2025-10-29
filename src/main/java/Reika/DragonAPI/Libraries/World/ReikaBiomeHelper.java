package Reika.DragonAPI.Libraries.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import Reika.DragonAPI.ASM.DependentMethodStripper.ModDependent;
import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.Instantiable.Data.Immutable.RGB;
import Reika.DragonAPI.Instantiable.Data.Maps.MultiMap;
import Reika.DragonAPI.Instantiable.Data.Maps.MultiMap.CollectionType;
import Reika.DragonAPI.Instantiable.Data.Maps.MultiMap.MapDeterminator;
import Reika.DragonAPI.Interfaces.CustomTemperatureBiome;
import Reika.DragonAPI.Interfaces.Registry.TreeType;
import Reika.DragonAPI.Libraries.Java.ReikaJavaLibrary;
import Reika.DragonAPI.Libraries.Java.ReikaStringParser;
import Reika.DragonAPI.Libraries.Registry.ReikaTreeHelper;
import Reika.DragonAPI.Libraries.Rendering.ReikaColorAPI;
import Reika.DragonAPI.ModInteract.DeepInteract.ModSeasonHandler;
import Reika.DragonAPI.ModList;
import Reika.DragonAPI.ModRegistry.InterfaceCache;
import biomesoplenty.api.biome.BOPBiome;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenMutated;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenForest;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraft.world.gen.feature.WorldGenSwamp;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;

public class ReikaBiomeHelper extends DragonAPICore {
    private static final MultiMap<BiomeGenBase, BiomeGenBase> children = new MultiMap();
    private static final MultiMap<BiomeGenBase, BiomeGenBase> similarity = new MultiMap();
    private static final HashMap<BiomeGenBase, BiomeGenBase> parents = new HashMap();
    private static final int[] biomeColors = new int[40];

    private static final HashMap<String, BiomeGenBase> nameMap = new HashMap();

    private static final HashMap<BiomeGenBase, BiomeTemperatures> temperatures
        = new HashMap();
    private static final HashMap<BiomeGenBase, TreeType> biomeTrees = new HashMap();

    public static final Comparator<BiomeGenBase> biomeIDSorter
        = new Comparator<BiomeGenBase>() {
              @Override
              public int compare(BiomeGenBase o1, BiomeGenBase o2) {
                  return Integer.compare(o1.biomeID, o2.biomeID);
              }
          };

    public static final Comparator<BiomeGenBase> biomeNameSorter
        = new Comparator<BiomeGenBase>() {
              @Override
              public int compare(BiomeGenBase o1, BiomeGenBase o2) {
                  return o1.biomeName.compareToIgnoreCase(o2.biomeName);
              }
          };

    static {
        addChildBiome(BiomeGenBase.desert, BiomeGenBase.desertHills);

        addChildBiome(BiomeGenBase.forest, BiomeGenBase.forestHills);

        addChildBiome(BiomeGenBase.taiga, BiomeGenBase.taigaHills);
        addChildBiome(BiomeGenBase.taiga, BiomeGenBase.coldTaiga, false);
        addChildBiome(BiomeGenBase.taiga, BiomeGenBase.megaTaiga, false);

        addChildBiome(BiomeGenBase.jungle, BiomeGenBase.jungleHills);
        addChildBiome(BiomeGenBase.jungle, BiomeGenBase.jungleEdge);

        addChildBiome(BiomeGenBase.megaTaiga, BiomeGenBase.megaTaigaHills);

        addChildBiome(BiomeGenBase.coldTaiga, BiomeGenBase.coldTaigaHills);

        addChildBiome(BiomeGenBase.icePlains, BiomeGenBase.iceMountains);

        addChildBiome(BiomeGenBase.birchForest, BiomeGenBase.birchForestHills);

        addChildBiome(BiomeGenBase.extremeHills, BiomeGenBase.extremeHillsEdge);
        addChildBiome(BiomeGenBase.extremeHills, BiomeGenBase.extremeHillsPlus);

        addChildBiome(BiomeGenBase.mushroomIsland, BiomeGenBase.mushroomIslandShore);

        addChildBiome(BiomeGenBase.mesa, BiomeGenBase.mesaPlateau);
        addChildBiome(BiomeGenBase.mesa, BiomeGenBase.mesaPlateau_F);

        addChildBiome(BiomeGenBase.beach, BiomeGenBase.coldBeach, false);
        addChildBiome(BiomeGenBase.beach, BiomeGenBase.stoneBeach);

        addChildBiome(BiomeGenBase.ocean, BiomeGenBase.deepOcean, false);
        addChildBiome(BiomeGenBase.ocean, BiomeGenBase.frozenOcean, false);

        addChildBiome(BiomeGenBase.river, BiomeGenBase.frozenRiver, false);

        addChildBiome(BiomeGenBase.savanna, BiomeGenBase.savannaPlateau);

        biomeColors[BiomeGenBase.ocean.biomeID] = 0x0000FF;
        biomeColors[BiomeGenBase.deepOcean.biomeID] = 0x0000B0;
        biomeColors[BiomeGenBase.river.biomeID] = 0x005AFF;
        biomeColors[BiomeGenBase.frozenOcean.biomeID] = 0x0094FF;
        biomeColors[BiomeGenBase.frozenRiver.biomeID] = 0x00C6FF;

        biomeColors[BiomeGenBase.icePlains.biomeID] = 0x608772;
        biomeColors[BiomeGenBase.plains.biomeID] = 0x6B8C42;
        biomeColors[BiomeGenBase.taiga.biomeID] = 0x62886F;
        biomeColors[BiomeGenBase.swampland.biomeID] = 0x444E3A;
        biomeColors[BiomeGenBase.extremeHills.biomeID] = 0x668766;
        biomeColors[BiomeGenBase.jungle.biomeID] = 0x3E9829;
        biomeColors[BiomeGenBase.forest.biomeID] = 0x5B9144;
        biomeColors[BiomeGenBase.savanna.biomeID] = 0xBCB555;
        biomeColors[BiomeGenBase.birchForest.biomeID] = 0x78BC63;
        biomeColors[BiomeGenBase.roofedForest.biomeID] = 0x387F24;

        biomeColors[BiomeGenBase.desert.biomeID] = 0xEEE7B1;
        biomeColors[BiomeGenBase.mushroomIsland.biomeID] = 0x726D81;
        biomeColors[BiomeGenBase.mesa.biomeID] = 0x9C6247;
        biomeColors[BiomeGenBase.beach.biomeID] = 0xEDE28E;
        biomeColors[BiomeGenBase.stoneBeach.biomeID] = 0x949494;
        biomeColors[BiomeGenBase.coldBeach.biomeID] = 0xACB6D3;

        biomeColors[BiomeGenBase.megaTaiga.biomeID] = 0x62886A;
        biomeColors[BiomeGenBase.megaTaigaHills.biomeID] = 0x82B28B;
        biomeColors[BiomeGenBase.coldTaiga.biomeID] = 0x628878;
        biomeColors[BiomeGenBase.coldTaigaHills.biomeID] = 0x82B29D;
        biomeColors[BiomeGenBase.savannaPlateau.biomeID] = 0xD3CA61;
        biomeColors[BiomeGenBase.iceMountains.biomeID] = 0x80B297;
        biomeColors[BiomeGenBase.mushroomIslandShore.biomeID] = 0x726D96;
        biomeColors[BiomeGenBase.desertHills.biomeID] = 0xFFF7BF;
        biomeColors[BiomeGenBase.forestHills.biomeID] = 0x70B253;
        biomeColors[BiomeGenBase.taigaHills.biomeID] = 0x82B292;
        biomeColors[BiomeGenBase.jungleHills.biomeID] = 0x4CB731;
        biomeColors[BiomeGenBase.jungleEdge.biomeID] = 0x4CB784;
        biomeColors[BiomeGenBase.extremeHillsEdge.biomeID] = 0x9BCC9B;
        biomeColors[BiomeGenBase.extremeHillsPlus.biomeID] = 0x87B287;
        biomeColors[BiomeGenBase.birchForestHills.biomeID] = 0x91E076;
        biomeColors[BiomeGenBase.mesaPlateau.biomeID] = 0xCC805D;
        biomeColors[BiomeGenBase.mesaPlateau_F.biomeID] = 0xFF9E75;

        biomeColors[BiomeGenBase.hell.biomeID] = 0x8F5353;
        biomeColors[BiomeGenBase.sky.biomeID] = 0xD6D99B;

        temperatures.put(BiomeGenBase.taiga, BiomeTemperatures.COOL);
        temperatures.put(BiomeGenBase.extremeHills, BiomeTemperatures.COOL);
        temperatures.put(BiomeGenBase.megaTaiga, BiomeTemperatures.COOL);

        temperatures.put(BiomeGenBase.forest, BiomeTemperatures.TEMPERATE);
        temperatures.put(BiomeGenBase.plains, BiomeTemperatures.TEMPERATE);
        temperatures.put(BiomeGenBase.birchForest, BiomeTemperatures.TEMPERATE);
        temperatures.put(BiomeGenBase.ocean, BiomeTemperatures.TEMPERATE);
        temperatures.put(BiomeGenBase.deepOcean, BiomeTemperatures.TEMPERATE);
        temperatures.put(BiomeGenBase.mushroomIsland, BiomeTemperatures.TEMPERATE);
        temperatures.put(BiomeGenBase.swampland, BiomeTemperatures.TEMPERATE);
        temperatures.put(BiomeGenBase.river, BiomeTemperatures.TEMPERATE);

        temperatures.put(BiomeGenBase.roofedForest, BiomeTemperatures.WARM);
        temperatures.put(BiomeGenBase.savanna, BiomeTemperatures.WARM);

        temperatures.put(BiomeGenBase.desert, BiomeTemperatures.HOT);
        temperatures.put(BiomeGenBase.mesa, BiomeTemperatures.HOT);
        temperatures.put(BiomeGenBase.jungle, BiomeTemperatures.HOT);

        temperatures.put(BiomeGenBase.coldTaiga, BiomeTemperatures.ICY);
        temperatures.put(BiomeGenBase.coldBeach, BiomeTemperatures.ICY);
        temperatures.put(BiomeGenBase.icePlains, BiomeTemperatures.ICY);
        temperatures.put(BiomeGenBase.iceMountains, BiomeTemperatures.ICY);
        temperatures.put(BiomeGenBase.frozenOcean, BiomeTemperatures.ICY);
        temperatures.put(BiomeGenBase.frozenRiver, BiomeTemperatures.ICY);

        temperatures.put(BiomeGenBase.hell, BiomeTemperatures.FIERY);

        temperatures.put(BiomeGenBase.sky, BiomeTemperatures.LUNAR);

        biomeTrees.put(BiomeGenBase.coldTaiga, ReikaTreeHelper.SPRUCE);
        biomeTrees.put(BiomeGenBase.coldTaigaHills, ReikaTreeHelper.SPRUCE);
        biomeTrees.put(BiomeGenBase.taiga, ReikaTreeHelper.SPRUCE);
        biomeTrees.put(BiomeGenBase.taigaHills, ReikaTreeHelper.SPRUCE);
        biomeTrees.put(BiomeGenBase.megaTaiga, ReikaTreeHelper.SPRUCE);
        biomeTrees.put(BiomeGenBase.megaTaigaHills, ReikaTreeHelper.SPRUCE);

        biomeTrees.put(BiomeGenBase.ocean, ReikaTreeHelper.OAK);
        biomeTrees.put(BiomeGenBase.forest, ReikaTreeHelper.OAK);
        biomeTrees.put(BiomeGenBase.forestHills, ReikaTreeHelper.OAK);
        biomeTrees.put(BiomeGenBase.swampland, ReikaTreeHelper.OAK);
        biomeTrees.put(BiomeGenBase.river, ReikaTreeHelper.OAK);
        biomeTrees.put(BiomeGenBase.iceMountains, ReikaTreeHelper.OAK);
        biomeTrees.put(BiomeGenBase.icePlains, ReikaTreeHelper.OAK);
        biomeTrees.put(BiomeGenBase.extremeHills, ReikaTreeHelper.OAK);
        biomeTrees.put(BiomeGenBase.extremeHillsEdge, ReikaTreeHelper.OAK);
        biomeTrees.put(BiomeGenBase.extremeHillsPlus, ReikaTreeHelper.OAK);
        biomeTrees.put(BiomeGenBase.mesaPlateau, ReikaTreeHelper.OAK);
        biomeTrees.put(BiomeGenBase.mesaPlateau_F, ReikaTreeHelper.OAK);

        biomeTrees.put(BiomeGenBase.birchForest, ReikaTreeHelper.BIRCH);
        biomeTrees.put(BiomeGenBase.birchForestHills, ReikaTreeHelper.BIRCH);

        biomeTrees.put(BiomeGenBase.jungle, ReikaTreeHelper.JUNGLE);
        biomeTrees.put(BiomeGenBase.jungleEdge, ReikaTreeHelper.JUNGLE);
        biomeTrees.put(BiomeGenBase.jungleHills, ReikaTreeHelper.JUNGLE);

        biomeTrees.put(BiomeGenBase.savanna, ReikaTreeHelper.ACACIA);
        biomeTrees.put(BiomeGenBase.savannaPlateau, ReikaTreeHelper.ACACIA);

        biomeTrees.put(BiomeGenBase.roofedForest, ReikaTreeHelper.DARKOAK);

        for (int i = 0; i < BiomeGenBase.biomeList.length; i++) {
            BiomeGenBase b = BiomeGenBase.biomeList[i];
            if (b != null) {
                nameMap.put(b.biomeName, b);
            }
        }
    }

    public static void addChildBiome(BiomeGenBase parent, BiomeGenBase child) {
        addChildBiome(parent, child, true);
    }

    private static void
    addChildBiome(BiomeGenBase parent, BiomeGenBase child, boolean isChild) {
        similarity.addValue(parent, child);
        if (isChild) {
            children.addValue(parent, child);
            parents.put(child, parent);
        }
    }

    public static enum BiomeTemperatures {
        LUNAR(-100),
        ICY(-20),
        COOL(10),
        TEMPERATE(25),
        WARM(30),
        HOT(40),
        FIERY(300);

        public final int ambientTemperature;

        private BiomeTemperatures(int t) {
            ambientTemperature = t;
        }
    }

    /** Returns the first empty biome index. */
    public static int getFirstEmptyBiomeIndex() {
        for (int i = 0; i < BiomeGenBase.biomeList.length; i++) {
            if (BiomeGenBase.biomeList[i] == null)
                return i;
        }
        throw new RuntimeException("Error: Biome Limit Exceeded!");
    }

    /**
     * Note that this is affected by other mods, so exclusive calls on this will end up
     * including mod biomes
     */
    public static List<BiomeGenBase> getAllBiomes() {
        List<BiomeGenBase> li = new ArrayList<BiomeGenBase>();
        for (int i = 0; i < BiomeGenBase.biomeList.length; i++) {
            if (BiomeGenBase.biomeList[i] != null)
                li.add(BiomeGenBase.biomeList[i]);
        }
        return li;
    }

    /**
     * Returns any associated biomes (eg Desert+DesertHills) to the one supplied. Args:
     * Biome, Whether to match "loosely". Loose matching is defined as similarity between
     * two biomes where one is not a parent of the other but both are similar in nature,
     * eg Taiga+Cold Taiga
     */
    public static Collection<BiomeGenBase>
    getAllAssociatedBiomes(BiomeGenBase biome, boolean loose) {
        return Collections.unmodifiableCollection(
            loose ? similarity.get(biome) : children.get(biome)
        );
    }

    public static Collection<BiomeGenBase> getChildBiomes(BiomeGenBase biome) {
        return getAllAssociatedBiomes(biome, false);
    }

    /** Returns the biome's parent. Args: Biome */
    public static BiomeGenBase
    getParentBiomeType(BiomeGenBase biome, boolean onlyDirect) {
        BiomeGenBase b = onlyDirect ? null : parents.get(biome);
        if (b != null)
            return b;
        if (biome instanceof BiomeGenMutated
            && ((BiomeGenMutated) biome).baseBiome != null) {
            parents.put(biome, ((BiomeGenMutated) biome).baseBiome);
            biome = ((BiomeGenMutated) biome).baseBiome;
        }
        if (biome.biomeID >= 128) {
            BiomeGenBase below = BiomeGenBase.biomeList[biome.biomeID - 128];
            if (below != null) {
                if (below.getClass().isAssignableFrom(biome.getClass())) {
                    biome = below;
                    parents.put(biome, below);
                }
            }
        }
        return biome;
    }

    /** Returns whether the biome is a variant of a parent. Args: Biome */
    public static boolean isChildBiome(BiomeGenBase biome) {
        return parents.containsKey(biome);
    }

    /**
     * Converts the given coordinates to an RGB representation of those coordinates'
     * biome's color, for the given material type. Args: World, x, z, material (String)
     */
    public static int[]
    biomeToRGB(IBlockAccess world, int x, int y, int z, Material material) {
        int color = biomeToHex(world, x, y, z, material);
        return ReikaColorAPI.HexToRGB(color);
    }

    /**
     * Converts the given coordinates to a hex representation of those coordinates'
     * biome's color, for the given material type. Args: World, x, z, material (String)
     */
    public static int
    biomeToHexColor(IBlockAccess world, int x, int y, int z, Material material) {
        int color = biomeToHex(world, x, y, z, material);
        return color;
    }

    private static int biomeToHex(IBlockAccess world, int x, int y, int z, Material mat) {
        BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
        int color = 0;
        if (mat == Material.water)
            color = biome.getBiomeFoliageColor(x, y, z);
        if (mat == Material.grass)
            color = biome.getBiomeGrassColor(x, y, z);
        if (mat == Material.water)
            color = biome.getWaterColorMultiplier();
        if (mat == Material.air)
            color = biome.getSkyColorByTemp(biome.getFloatTemperature(x, y, z));
        return color;
    }

    /** Returns true if the passed biome is a cool but not cold biome.  Args: Biome */
    public static boolean isCoolBiome(BiomeGenBase biome) {
        if (biome == BiomeGenBase.taiga)
            return true;
        if (biome == BiomeGenBase.taigaHills)
            return true;
        if (biome.biomeName.toLowerCase(Locale.ENGLISH).contains("maple woods"))
            return true;
        BiomeDictionary.Type[] types = BiomeDictionary.getTypesForBiome(biome);
        for (int i = 0; i < types.length; i++) {}
        return false;
    }

    /** Returns true if the passed biome is a snow biome.  Args: Biome*/
    public static boolean isSnowBiome(BiomeGenBase biome) {
        if (biome == BiomeGenBase.frozenOcean)
            return true;
        if (biome == BiomeGenBase.frozenRiver)
            return true;
        if (biome == BiomeGenBase.iceMountains)
            return true;
        if (biome == BiomeGenBase.icePlains)
            return true;
        if (biome == BiomeGenBase.coldTaiga)
            return true;
        if (biome == BiomeGenBase.coldTaigaHills)
            return true;
        if (biome == BiomeGenBase.taiga)
            return false;
        if (biome == BiomeGenBase.taigaHills)
            return false;
        if (biome.biomeName.toLowerCase(Locale.ENGLISH)
                .contains("maple woods")) //I do NOT live in the Arctic
            return false;
        if (biome.getEnableSnow())
            return true;
        if (biome.biomeName.toLowerCase(Locale.ENGLISH).contains("arctic"))
            return true;
        if (biome.biomeName.toLowerCase(Locale.ENGLISH).contains("tundra"))
            return true;
        if (biome.biomeName.toLowerCase(Locale.ENGLISH).contains("alpine"))
            return true;
        if (biome.biomeName.toLowerCase(Locale.ENGLISH).contains("frozen"))
            return true;
        BiomeDictionary.Type[] types = BiomeDictionary.getTypesForBiome(biome);
        for (int i = 0; i < types.length; i++) {
            if (types[i] == BiomeDictionary.Type.FROZEN)
                return true;
            if (types[i] == BiomeDictionary.Type.COLD)
                return true;
            if (types[i] == BiomeDictionary.Type.SNOWY)
                return true;
        }
        return false;
    }

    /** Returns true if the passed biome is a hot biome.  Args: Biome*/
    public static boolean isHotBiome(BiomeGenBase biome) {
        if (biome == BiomeGenBase.desert)
            return true;
        if (biome == BiomeGenBase.desertHills)
            return true;
        if (biome == BiomeGenBase.hell)
            return true;
        if (biome == BiomeGenBase.jungle)
            return true;
        if (biome == BiomeGenBase.jungleHills)
            return true;
        if (biome == BiomeGenBase.mesa)
            return true;
        BiomeDictionary.Type[] types = BiomeDictionary.getTypesForBiome(biome);
        for (int i = 0; i < types.length; i++) {
            if (types[i] == BiomeDictionary.Type.WASTELAND)
                return true;
            if (types[i] == BiomeDictionary.Type.DESERT)
                return true;
            if (types[i] == BiomeDictionary.Type.HOT)
                return true;
            if (types[i] == BiomeDictionary.Type.JUNGLE)
                return true;
        }
        return false;
    }

    /**
     * Returns a broad-stroke biome temperature in degrees centigrade.
     * Args: biome
     */
    public static int getBiomeTemp(World world, BiomeGenBase biome) {
        biome = getParentBiomeType(biome, false);
        int Tamb;
        BiomeTemperatures temp = null;
        if (biome instanceof CustomTemperatureBiome) {
            Tamb = ((CustomTemperatureBiome) biome).getBaseAmbientTemperature();
        } else {
            temp = temperatures.get(biome);
            if (temp == null) {
                temp = calcBiomeTemp(biome);
                temperatures.put(biome, temp);
            }
            Tamb = temp.ambientTemperature;
        }

        if (ModSeasonHandler.isLoaded()) { //account for seasons
            Tamb += getBiomeSeasonStrength(biome, temp)
                * ModSeasonHandler.getSeasonTemperatureModifier(world);
        }

        return Tamb;
    }

    private static float
    getBiomeSeasonStrength(BiomeGenBase biome, BiomeTemperatures temp) {
        if (biome instanceof CustomTemperatureBiome)
            return ((CustomTemperatureBiome) biome).getSeasonStrength();
        if (temp == BiomeTemperatures.FIERY || temp == BiomeTemperatures.LUNAR)
            return 0;
        if (BiomeDictionary.isBiomeOfType(biome, Type.SANDY))
            return 1.5F;
        if (BiomeDictionary.isBiomeOfType(biome, Type.SAVANNA))
            return 1.25F;
        if (BiomeDictionary.isBiomeOfType(biome, Type.JUNGLE))
            return 0.4F;
        if (temp == BiomeTemperatures.HOT || temp == BiomeTemperatures.ICY)
            return 0.2F;
        if (temp == BiomeTemperatures.COOL || temp == BiomeTemperatures.WARM)
            return 0.75F;
        if (temp == BiomeTemperatures.TEMPERATE)
            return 1F;
        return 1;
    }

    private static BiomeTemperatures calcBiomeTemp(BiomeGenBase biome) {
        if (biome == BiomeGenBase.hell)
            return BiomeTemperatures.FIERY;
        else if (biome == BiomeGenBase.sky)
            return BiomeTemperatures.LUNAR;

        BiomeDictionary.Type[] types = BiomeDictionary.getTypesForBiome(biome);
        for (int i = 0; i < types.length; i++) {
            if (types[i] == BiomeDictionary.Type.NETHER)
                return BiomeTemperatures.FIERY;
            else if (types[i] == BiomeDictionary.Type.END)
                return BiomeTemperatures.LUNAR;
        }

        if (isSnowBiome(biome))
            return BiomeTemperatures.ICY;
        else if (isHotBiome(biome))
            return BiomeTemperatures.HOT;
        else if (isCoolBiome(biome))
            return BiomeTemperatures.COOL;
        else
            return BiomeTemperatures.TEMPERATE;
    }

    /**
     * Returns a broad-stroke biome temperature in degrees centigrade.
     * Args: World, x, z
     */
    public static int getBiomeTemp(World world, int x, int z) {
        BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
        return getBiomeTemp(world, biome);
    }

    public static float getBiomeHumidity(BiomeGenBase biome) {
        biome = getParentBiomeType(biome, false);
        if (biome == BiomeGenBase.jungle)
            return 0.95F;
        if (biome == BiomeGenBase.ocean)
            return 1F;
        if (biome == BiomeGenBase.deepOcean)
            return 1F;
        if (biome == BiomeGenBase.swampland)
            return 0.85F;
        if (biome == BiomeGenBase.forest)
            return 0.6F;
        if (biome == BiomeGenBase.birchForest)
            return 0.55F;
        if (biome == BiomeGenBase.roofedForest)
            return 0.7F;
        if (biome == BiomeGenBase.plains)
            return 0.4F;
        if (biome == BiomeGenBase.savanna)
            return 0.3F;
        if (biome == BiomeGenBase.desert)
            return 0.2F;
        if (biome == BiomeGenBase.mesa)
            return 0.2F;
        if (biome == BiomeGenBase.hell)
            return 0.1F;
        if (biome == BiomeGenBase.sky)
            return 0.1F;
        if (biome == BiomeGenBase.beach)
            return 0.98F;
        if (biome == BiomeGenBase.icePlains)
            return 0.4F;
        if (biome == BiomeGenBase.mushroomIsland)
            return 0.75F;

        BiomeDictionary.Type[] types = BiomeDictionary.getTypesForBiome(biome);
        float val = 0.5F;
        for (int i = 0; i < types.length; i++) {
            if (types[i] == BiomeDictionary.Type.BEACH)
                val = Math.max(val, 0.95F);
            if (types[i] == BiomeDictionary.Type.OCEAN
                || types[i] == BiomeDictionary.Type.RIVER
                || types[i] == BiomeDictionary.Type.WATER)
                val = Math.max(val, 1F);
            if (types[i] == BiomeDictionary.Type.JUNGLE)
                val = Math.max(val, 0.95F);
            if (types[i] == BiomeDictionary.Type.DESERT
                || types[i] == BiomeDictionary.Type.SANDY)
                val = Math.min(val, 0.2F);
            if (types[i] == BiomeDictionary.Type.NETHER
                || types[i] == BiomeDictionary.Type.END)
                val = Math.min(val, 0.1F);
            if (types[i] == BiomeDictionary.Type.WASTELAND)
                val = Math.min(val, 0.1F);
            if (types[i] == BiomeDictionary.Type.LUSH)
                val = Math.max(val, 0.6F);
            if (types[i] == BiomeDictionary.Type.WET)
                val = Math.max(val, 0.7F);
            if (types[i] == BiomeDictionary.Type.DRY)
                val = Math.min(val, 0.3F);
        }
        return val;
    }

    public static float getBiomeHumidity(World world, int x, int z) {
        return getBiomeHumidity(world.getBiomeGenForCoords(x, z));
    }

    public static boolean isOcean(BiomeGenBase biome) {
        if (biome == BiomeGenBase.ocean || biome == BiomeGenBase.frozenOcean
            || biome == BiomeGenBase.deepOcean)
            return true;
        if (BiomeDictionary.isBiomeOfType(biome, Type.FOREST))
            return false;
        if (BiomeDictionary.isBiomeOfType(biome, Type.DRY))
            return false;
        if (BiomeDictionary.isBiomeOfType(biome, Type.DENSE))
            return false;
        if (BiomeDictionary.isBiomeOfType(biome, Type.OCEAN))
            return true;
        return ReikaStringParser.containsWord(
            biome.biomeName.toLowerCase(Locale.ENGLISH), "ocean"
        );
    }

    public static void removeBiomeWithAssociates(BiomeGenBase biome) {
        BiomeManager.removeSpawnBiome(biome);
        Collection<BiomeGenBase> c = getChildBiomes(biome);
        for (BiomeGenBase b : c)
            BiomeManager.removeSpawnBiome(b);
    }

    public static void removeAllBiomesBut(Collection<BiomeGenBase> biomes) {
        for (int i = 0; i < BiomeGenBase.biomeList.length; i++) {
            BiomeGenBase b = BiomeGenBase.biomeList[i];
            if (!biomes.contains(b))
                BiomeManager.removeSpawnBiome(b);
        }
    }

    public static void removeAllBiomesBut(BiomeGenBase... biomes) {
        removeAllBiomesBut(ReikaJavaLibrary.makeListFromArray(biomes));
    }

    public static void removeAllBiomesBut(BiomeGenBase biome) {
        for (int i = 0; i < BiomeGenBase.biomeList.length; i++) {
            BiomeGenBase b = BiomeGenBase.biomeList[i];
            if (b != biome)
                BiomeManager.removeSpawnBiome(b);
        }
    }

    public static int getBiomeNaturalColor(BiomeGenBase b) {
        Block top = b.topBlock;
        if (BiomeDictionary.isBiomeOfType(b, Type.WATER))
            top = Blocks.water;
        int mat = top.getMaterial().getMaterialMapColor().colorValue;
        if (top == Blocks.grass) {
            mat = b.getBiomeGrassColor(0, 0, 0);
        }
        RGB rgb = new RGB(mat);
        return rgb.getInt();
    }

    public static int getBiomeUniqueColor(BiomeGenBase b) {
        return biomeColors[b.biomeID];
    }

    public static BiomeGenBase getBiomeByName(String s) {
        return nameMap.get(s);
    }

    public static MultiMap<Integer, Integer> getBiomeHierearchy() {
        MultiMap<Integer, Integer> data
            = new MultiMap(CollectionType.LIST, new MapDeterminator() {
                  @Override
                  public Map getMapType() {
                      return new TreeMap();
                  }
              });

        for (BiomeGenBase b : getAllBiomes()) {
            for (BiomeGenBase b2 : getChildBiomes(b)) {
                data.addValue(b.biomeID, b2.biomeID);
            }
        }
        for (BiomeGenBase b : getAllBiomes()) {
            BiomeGenBase parent = getParentBiomeType(b, false);
            if (b != parent) {
                data.addValue(parent.biomeID, b.biomeID);
            }
        }
        for (BiomeGenBase b : getAllBiomes()) {
            if (!data.containsKey(b.biomeID)) {
                data.put(b.biomeID, new ArrayList());
            }
        }
        HashSet<Integer> set = new HashSet(data.allValues(false));
        for (int id : set) {
            data.remove(id);
        }
        return data;
    }

    public static BiomeDecorator getBiomeDecorator(BiomeGenBase b) {
        if (InterfaceCache.BOPBIOME.instanceOf(b)) {
            return getBOPDecorator(b);
        }
        return b.theBiomeDecorator;
    }

    @ModDependent(ModList.BOP)
    private static BiomeDecorator getBOPDecorator(BiomeGenBase b) {
        return ((BOPBiome) b).theBiomeDecorator;
    }

    public static boolean doesBiomeHavePrecipitation(BiomeGenBase b) {
        return b.canSpawnLightningBolt() || b.getEnableSnow();
    }

    public static TreeType getDominantTreeType(BiomeGenBase biome) {
        biome = getParentBiomeType(biome, false);
        TreeType map = biomeTrees.get(biome);
        if (map != null)
            return map;
        WorldGenAbstractTree gen = biome.func_150567_a(rand);
        if (gen == null)
            return null;
        if (gen instanceof WorldGenBigTree || gen instanceof WorldGenSwamp)
            return ReikaTreeHelper.OAK;
        if (gen instanceof WorldGenForest)
            return ReikaTreeHelper.BIRCH;
        if (gen instanceof WorldGenTaiga2 || gen instanceof WorldGenMegaPineTree)
            return ReikaTreeHelper.SPRUCE;
        if (gen instanceof WorldGenMegaJungle)
            return ReikaTreeHelper.JUNGLE;
        if (gen instanceof WorldGenSavannaTree)
            return ReikaTreeHelper.ACACIA;
        if (gen instanceof WorldGenCanopyTree)
            return ReikaTreeHelper.DARKOAK;
        return null;
    }
}
