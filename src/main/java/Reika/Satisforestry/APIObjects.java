package Reika.Satisforestry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import Reika.DragonAPI.ASM.DependentMethodStripper.ModDependent;
import Reika.DragonAPI.ModList;
import Reika.Satisforestry.API.NodeEffectCallback;
import Reika.Satisforestry.API.PinkTreeType;
import Reika.Satisforestry.API.SFAPI;
import Reika.Satisforestry.API.SFAPI.PinkForestBiomeHandler;
import Reika.Satisforestry.API.SFAPI.PinkForestCaveHandler;
import Reika.Satisforestry.API.SFAPI.PinkForestResourceNodeHandler;
import Reika.Satisforestry.API.SFAPI.PinkTreeHandler;
import Reika.Satisforestry.API.SFAPI.PowerSlugHandler;
import Reika.Satisforestry.AlternateRecipes.AlternateRecipeManager;
import Reika.Satisforestry.Biome.Biomewide.BiomewideFeatureGenerator;
import Reika.Satisforestry.Biome.Biomewide.UraniumCave;
import Reika.Satisforestry.Biome.DecoratorPinkForest;
import Reika.Satisforestry.Biome.Generator.PinkTreeGeneratorBase.PinkTreeTypes;
import Reika.Satisforestry.Blocks.BlockPowerSlug.TilePowerSlug;
import Reika.Satisforestry.Blocks.BlockResourceNode.TileResourceNode;
import Reika.Satisforestry.Config.ResourceItem;
import Reika.Satisforestry.Entity.EntityEliteStinger;
import Reika.Satisforestry.Entity.EntityFlyingManta;
import Reika.Satisforestry.Entity.EntityLizardDoggo;
import Reika.Satisforestry.Entity.EntitySpitter;
import Reika.Satisforestry.Registry.SFBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fluids.Fluid;
import thaumcraft.api.aspects.Aspect;

public class APIObjects {
    public static void load() {
        SFAPI.biomeHandler = new SFBiomeHandler();
        SFAPI.treeHandler = new SFTreeHandler();
        SFAPI.caveHandler = new SFCaveHandler();
        SFAPI.resourceNodeHandler = new SFNodeHandler();
        SFAPI.genericLookups = new SFLookups();
        SFAPI.slugHandler = new SFSlugs();
        SFAPI.altRecipeHandler = AlternateRecipeManager.instance;
    }

    private static class SFSlugs implements PowerSlugHandler {
        @Override
        public ItemStack getSlug(int tier) {
            return SFBlocks.SLUG.getStackOfMetadata((tier - 1) % 3);
        }

        @Override
        public int getSlugTier(ItemStack is) {
            return SFBlocks.SLUG.matchWith(is) ? is.getItemDamage() % 3 + 1 : 0;
        }

        @Override
        public int getSlugTier(TileEntity te) {
            return te instanceof TilePowerSlug ? ((TilePowerSlug) te).getTier() + 1 : 0;
        }

        @Override
        public int getSlugHelmetTier(EntityLivingBase e) {
            return SFAux.getSlugHelmetTier(e);
        }
    }

    private static class SFLookups implements Reika.Satisforestry.API.SFAPI.SFLookups {
        @Override
        public Object getAspect() {
            return ModList.THAUMCRAFT.isLoaded() ? this.loadTCHandler() : null;
        }

        @ModDependent(ModList.THAUMCRAFT)
        private Aspect loadTCHandler() {
            return SFThaumHandler.FICSIT;
        }

        @Override
        public Item getPaleberries() {
            return Satisforestry.paleberry;
        }

        @Override
        public Class<? extends EntityLiving> getSpitterClass() {
            return EntitySpitter.class;
        }

        @Override
        public Class<? extends EntityLiving> getStingerClass() {
            return EntityEliteStinger.class;
        }

        @Override
        public Class<? extends EntityLiving> getDoggoClass() {
            return EntityLizardDoggo.class;
        }

        @Override
        public Class<? extends EntityLiving> getMantaClass() {
            return EntityFlyingManta.class;
        }

        @Override
        public Item getCompactedCoal() {
            return Satisforestry.compactedCoal;
        }

        @Override
        public Fluid getTurbofuel() {
            return Satisforestry.turbofuel;
        }
    }

    private static class SFBiomeHandler implements PinkForestBiomeHandler {
        @Override
        public boolean isPinkForest(BiomeGenBase b) {
            return Satisforestry.isPinkForest(b);
        }

        @Override
        public int getBaseTerrainHeight(BiomeGenBase b) {
            return 112;
        }

        @Override
        public int getTrueTopAt(World world, int x, int z) {
            return DecoratorPinkForest.getTrueTopAt(world, x, z);
        }

        @Override
        public BiomeGenBase getBiome() {
            return Satisforestry.pinkforest;
        }
    }

    private static class SFTreeHandler implements PinkTreeHandler {
        @Override
        public boolean
        placeTree(World world, Random rand, int x, int y, int z, PinkTreeType type) {
            return ((PinkTreeTypes) type)
                .constructTreeGenerator()
                .generate(world, rand, x, y, z);
        }

        @Override
        public PinkTreeType getTypeFromLog(IBlockAccess world, int x, int y, int z) {
            return PinkTreeTypes.getLogType(world, x, y, z);
        }

        @Override
        public PinkTreeType getTypeFromLeaves(IBlockAccess world, int x, int y, int z) {
            return PinkTreeTypes.getLeafType(world, x, y, z);
        }

        @Override
        public PinkTreeType[] getTypes() {
            return Arrays.copyOf(PinkTreeTypes.list, PinkTreeTypes.list.length);
        }
    }

    private static class SFCaveHandler implements PinkForestCaveHandler {
        public boolean isInCave(World world, double x, double y, double z) {
            return BiomewideFeatureGenerator.instance.isInCave(world, x, y, z);
        }

        public double getDistanceToCaveCenter(World world, double x, double y, double z) {
            return BiomewideFeatureGenerator.instance.getDistanceToCaveCenter(
                world, x, y, z
            );
        }

        @Override
        public boolean isSpecialCaveBlock(Block b) {
            return UraniumCave.instance.isSpecialCaveBlock(b);
        }
    }

    private static class SFNodeHandler implements PinkForestResourceNodeHandler {
        @Override
        public ItemStack generateRandomResourceFromNode(
            TileEntity node, Random rand, boolean manualMining
        ) {
            return ((TileResourceNode) node).getRandomNodeItem(manualMining);
        }

        @Override
        public HashMap<ItemStack, Double> getPotentialItemsHere(TileEntity node) {
            ResourceItem item = ((TileResourceNode) node).getResource();
            return item.getItemSet(((TileResourceNode) node).getPurity());
        }

        public void registerCustomNodeEffect(String name, NodeEffectCallback eff) {
            ResourceItem.EffectTypes.addCustomCallback(name, eff);
        }
    }
}
