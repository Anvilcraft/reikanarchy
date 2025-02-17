package Reika.Satisforestry.API;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import Reika.Satisforestry.API.AltRecipe.UncraftableAltRecipe;
import Reika.Satisforestry.API.AltRecipe.UncraftableAltRecipeWithNEI;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fluids.Fluid;

public class SFAPI {
    public static PinkForestBiomeHandler biomeHandler = new DummyPlaceholder();
    public static PinkTreeHandler treeHandler = new DummyPlaceholder();
    public static PinkForestCaveHandler caveHandler = new DummyPlaceholder();
    public static PinkForestResourceNodeHandler resourceNodeHandler
        = new DummyPlaceholder();
    //public static PinkForestEntityHandler entityHandler = new DummyPlaceholder();
    public static PinkForestSpawningHandler spawningHandler = new DummyPlaceholder();
    public static PowerSlugHandler slugHandler = new DummyPlaceholder();
    public static AltRecipeHandler altRecipeHandler = new DummyPlaceholder();
    public static SFLookups genericLookups = new DummyPlaceholder();

    public static interface PinkForestBiomeHandler {
        public BiomeGenBase getBiome();

        public boolean isPinkForest(BiomeGenBase b);

        /** Returns the base (offset from base MC) height of the terrain in the biome. */
        public int getBaseTerrainHeight(BiomeGenBase b);

        /**
         * Like getTopSolidOrLiquid, but passes through the pink tree canopy. Returns the
         * coord of the ground, not the lowest air.
         */
        public int getTrueTopAt(World world, int x, int z);
    }

    public static interface PinkTreeHandler {
        /** Get the values() of the PinkTreeTypes enum. */
        public PinkTreeType[] getTypes();

        public boolean
        placeTree(World world, Random rand, int x, int y, int z, PinkTreeType type);

        public PinkTreeType getTypeFromLog(IBlockAccess world, int x, int y, int z);

        public PinkTreeType getTypeFromLeaves(IBlockAccess world, int x, int y, int z);
    }
    /*
    public static interface PinkForestEntityHandler {

        public Collection<Class<? extends EntityLiving>> getEntityTypes();

    }*/

    public static interface PinkForestSpawningHandler {
        /**
         Returns the spawnpoint of this entity, if one exists. Entities from "spawn
         points" are spawned to guard an area and are tied to that region. They are also
         always hostile, and will infinitely respawn until the player has killed all of
         them. See {@link PointSpawnLocation} for details.
       */
        public PointSpawnLocation getEntitySpawnPoint(EntityLiving e);

        public PointSpawnLocation getNearestSpawnPoint(EntityPlayer ep, double r);

        public PointSpawnLocation getNearestSpawnPointOfType(
            EntityPlayer ep, double r, Class<? extends EntityLiving> c
        );
    }

    public static interface PinkForestCaveHandler {
        public boolean isInCave(World world, double x, double y, double z);

        /**
         * Whether the given block is a special block used to form the structure of the
         * cave or its functionality.
         */
        public boolean isSpecialCaveBlock(Block b);

        public double getDistanceToCaveCenter(World world, double x, double y, double z);
    }

    public static interface PinkForestResourceNodeHandler {
        /** Generate a random harvest result from a resource node TE. */
        public ItemStack generateRandomResourceFromNode(
            TileEntity node, Random rand, boolean manualMining
        );

        /**
         * Get the overall WeightedRandom harvest table, before things like speed or
         * manual modifiers.
         */
        public HashMap<ItemStack, Double> getPotentialItemsHere(TileEntity node);

        public void registerCustomNodeEffect(String name, NodeEffectCallback eff);
    }

    public static interface SFLookups {
        /** Returns the custom Thaumcraft aspect added by SF, if TC is installed. */
        public Object getAspect();

        public Item getPaleberries();

        public Item getCompactedCoal();

        /** Will be null if a "fuel" fluid does not exist */
        public Fluid getTurbofuel();

        public Class<? extends EntityLiving> getSpitterClass();

        public Class<? extends EntityLiving> getStingerClass();

        public Class<? extends EntityLiving> getDoggoClass();

        public Class<? extends EntityLiving> getMantaClass();
    }

    public static interface PowerSlugHandler {
        /**
         * Returns a stack (size 1) of power slugs of a specific tier (green/blue,
         * yellow, purple = 1,2,3)
         */
        public ItemStack getSlug(int tier);

        /** Returns 1-3 of the given slug's tier, or 0 if it is not a slug. */
        public int getSlugTier(ItemStack is);

        /** Returns 1-3 of the given placed slug's tier, or 0 if it is not a slug. */
        public int getSlugTier(TileEntity te);

        /**
         * Returns the slug tier currently worn by this entity. 0 if none. <br><b>Use
         * this instead of getSlugTier(ItemStack is), as it handles "delegate" items</b>
         */
        public int getSlugHelmetTier(EntityLivingBase e);
    }

    public static interface AltRecipeHandler {
        public AltRecipe getRecipeByID(String id);

        /**
         * The full set of recipe IDs.<br><b>Note that these are populated very late</b>,
         * in {@link FMLServerAboutToStart}!
         */
        public Set<String> getRecipeIDs();

        public String getCompactedCoalID();
        public String getTurbofuelID();

        /**
        Creates a new alternate recipe with the given ID, spawn weight, crafting
        recipe, and (optionally) either or both the items and/or power input to the crash
        site required to be able to activate it and unlock the recipe for that player. For
        those familiar, this is analogous to the mechanic on Satisfactory drop pods.
        <br><br>Display name may be null to delegate to the item output display name,
        <b>if and only if that is non-null</b>. <br><br><b>The recipe must be
        non-null</b>, but if you want to make the alternate recipe not a "vanilla-style"
        crafting recipe (such that the alternate recipe acts primarily as a progress flag
        for your own code to check, eg in a custom machine's recipe system), then supply
        an {@link IRecipe} of a class implementing the {@link UncraftableAltRecipe}
        interface. Do note that unless you use the
        {@link UncraftableAltRecipeWithNEI} subclass, this will disable the native NEI
        handling, and you will need to handle it yourself in your own NEI handlers.
        Remember to display the unlock requirements; see the native NEI handling for
        reference.
      */
        public AltRecipe addAltRecipe(
            String id,
            String displayName,
            int spawnWeight,
            IRecipe recipe,
            ItemStack requiredUnlock,
            String unlockPowerType,
            long powerAmount,
            long ticksFor
        );
    }

    private static class DummyPlaceholder
        implements PinkForestBiomeHandler, PinkForestResourceNodeHandler,
                   PinkForestCaveHandler, PinkTreeHandler, SFLookups,
                   PinkForestSpawningHandler, PowerSlugHandler, AltRecipeHandler {
        @Override
        public PinkTreeType[] getTypes() {
            return new PinkTreeType[0];
        }

        @Override
        public boolean
        placeTree(World world, Random rand, int x, int y, int z, PinkTreeType type) {
            return false;
        }

        @Override
        public PinkTreeType getTypeFromLog(IBlockAccess world, int x, int y, int z) {
            return null;
        }

        @Override
        public PinkTreeType getTypeFromLeaves(IBlockAccess world, int x, int y, int z) {
            return null;
        }

        @Override
        public boolean isInCave(World world, double x, double y, double z) {
            return false;
        }

        @Override
        public boolean isSpecialCaveBlock(Block b) {
            return false;
        }

        @Override
        public ItemStack generateRandomResourceFromNode(
            TileEntity node, Random rand, boolean manualMining
        ) {
            return null;
        }

        @Override
        public HashMap<ItemStack, Double> getPotentialItemsHere(TileEntity node) {
            return new HashMap();
        }

        @Override
        public void registerCustomNodeEffect(String name, NodeEffectCallback eff) {}

        @Override
        public boolean isPinkForest(BiomeGenBase b) {
            return false;
        }

        @Override
        public int getBaseTerrainHeight(BiomeGenBase b) {
            return 0;
        }

        @Override
        public int getTrueTopAt(World world, int x, int z) {
            return 0;
        }

        @Override
        public Object getAspect() {
            return null;
        }

        @Override
        public Item getPaleberries() {
            return null;
        }

        @Override
        public BiomeGenBase getBiome() {
            return null;
        }

        @Override
        public PointSpawnLocation getEntitySpawnPoint(EntityLiving e) {
            return null;
        }

        @Override
        public PointSpawnLocation getNearestSpawnPoint(EntityPlayer ep, double r) {
            return null;
        }

        @Override
        public double getDistanceToCaveCenter(World world, double x, double y, double z) {
            return 0;
        }

        @Override
        public PointSpawnLocation getNearestSpawnPointOfType(
            EntityPlayer ep, double r, Class<? extends EntityLiving> c
        ) {
            return null;
        }

        @Override
        public Class<? extends EntityLiving> getSpitterClass() {
            return null;
        }

        @Override
        public Class<? extends EntityLiving> getStingerClass() {
            return null;
        }

        @Override
        public Class<? extends EntityLiving> getDoggoClass() {
            return null;
        }

        @Override
        public Class<? extends EntityLiving> getMantaClass() {
            return null;
        }

        @Override
        public ItemStack getSlug(int tier) {
            return null;
        }

        @Override
        public int getSlugTier(ItemStack is) {
            return 0;
        }

        @Override
        public int getSlugTier(TileEntity te) {
            return 0;
        }

        @Override
        public int getSlugHelmetTier(EntityLivingBase e) {
            return 0;
        }

        @Override
        public AltRecipe getRecipeByID(String id) {
            return null;
        }

        @Override
        public Set<String> getRecipeIDs() {
            return new HashSet();
        }

        @Override
        public AltRecipe addAltRecipe(
            String id,
            String displayName,
            int spawnWeight,
            IRecipe recipe,
            ItemStack requiredUnlock,
            String unlockPowerType,
            long powerAmount,
            long ticksFor
        ) {
            return null;
        }

        @Override
        public Item getCompactedCoal() {
            return null;
        }

        @Override
        public Fluid getTurbofuel() {
            return null;
        }

        @Override
        public String getCompactedCoalID() {
            return null;
        }

        @Override
        public String getTurbofuelID() {
            return null;
        }
    }
}
