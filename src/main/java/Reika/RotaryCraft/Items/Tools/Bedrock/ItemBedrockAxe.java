/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.Items.Tools.Bedrock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import Reika.ChromatiCraft.ChromaticEventManager;
import Reika.ChromatiCraft.Registry.ChromaEnchants;
import Reika.DragonAPI.ASM.DependentMethodStripper.ModDependent;
import Reika.DragonAPI.Auxiliary.ProgressiveRecursiveBreaker;
import Reika.DragonAPI.Auxiliary.ProgressiveRecursiveBreaker.ProgressiveBreaker;
import Reika.DragonAPI.Instantiable.Data.BlockStruct.TreeReader;
import Reika.DragonAPI.Instantiable.Data.Immutable.BlockKey;
import Reika.DragonAPI.Instantiable.Data.Immutable.Coordinate;
import Reika.DragonAPI.Interfaces.Item.IndexedItemSprites;
import Reika.DragonAPI.Interfaces.Registry.TreeType;
import Reika.DragonAPI.Libraries.IO.ReikaSoundHelper;
import Reika.DragonAPI.Libraries.IO.ReikaTextureHelper;
import Reika.DragonAPI.Libraries.Registry.ReikaItemHelper;
import Reika.DragonAPI.Libraries.Registry.ReikaTreeHelper;
import Reika.DragonAPI.Libraries.ReikaEnchantmentHelper;
import Reika.DragonAPI.Libraries.ReikaInventoryHelper;
import Reika.DragonAPI.Libraries.ReikaPlayerAPI;
import Reika.DragonAPI.ModInteract.ItemHandlers.ForestryHandler;
import Reika.DragonAPI.ModInteract.ItemHandlers.TwilightForestHandler;
import Reika.DragonAPI.ModList;
import Reika.DragonAPI.ModRegistry.ModWoodList;
import Reika.RotaryCraft.Registry.ConfigRegistry;
import Reika.RotaryCraft.Registry.ItemRegistry;
import Reika.RotaryCraft.Registry.RotaryAchievements;
import Reika.RotaryCraft.RotaryCraft;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

public class ItemBedrockAxe extends ItemAxe implements IndexedItemSprites {
    private int index;

    public ItemBedrockAxe(int tex) {
        super(ToolMaterial.GOLD);
        this.setIndex(tex);
        maxStackSize = 1;
        this.setMaxDamage(0);
        efficiencyOnProperMaterial = 12F;
        damageVsEntity = 6;
        this.setNoRepair();
        this.setHarvestLevel("axe", Integer.MAX_VALUE);

        this.setCreativeTab(
            RotaryCraft.instance.isLocked() ? null : RotaryCraft.tabRotaryTools
        );
    }

    @Override
    public void onCreated(ItemStack is, World world, EntityPlayer ep) {
        RotaryAchievements.BEDROCKTOOLS.triggerAchievement(ep);
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass) {
        return toolClass == null
                || (toolClass.toLowerCase(Locale.ENGLISH).contains("axe")
                    && !toolClass.toLowerCase(Locale.ENGLISH).contains("pick"))
            ? Integer.MAX_VALUE
            : super.getHarvestLevel(stack, toolClass);
    }

    @Override
    public void
    onUpdate(ItemStack is, World world, Entity entity, int slot, boolean par5) {
        this.forceNoSilkTouch(is, world, entity, slot);
    }

    private void forceNoSilkTouch(ItemStack is, World world, Entity entity, int slot) {
        if (ReikaEnchantmentHelper.hasEnchantment(Enchantment.silkTouch, is)) {
            HashMap<Enchantment, Integer> map
                = ReikaEnchantmentHelper.getEnchantments(is);
            is.stackTagCompound = null;
            map.remove(Enchantment.silkTouch);
            ReikaEnchantmentHelper.applyEnchantments(is, map);
        }
    }

    @Override
    public boolean isItemTool(ItemStack is) {
        return true;
    }

    @Override
    public boolean canHarvestBlock(Block b, ItemStack is) {
        return b.getMaterial() == Material.wood || b.getMaterial() == Material.gourd
            || b.getMaterial().isToolNotRequired();
    }

    @Override
    public int getItemEnchantability() {
        return Items.iron_axe.getItemEnchantability();
    }

    @Override
    public float getDigSpeed(ItemStack is, Block b, int meta) {
        if (b == null)
            return 0;
        if (b == TwilightForestHandler.BlockEntry.TOWERWOOD.getBlock())
            return 30F;
        if (b == TwilightForestHandler.BlockEntry.TOWERMACHINE.getBlock())
            return 24F;
        if (b.getMaterial() == Material.wood)
            return 20F;
        if (field_150914_c.contains(b))
            return 20F;
        return 1F;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack is, int x, int y, int z, EntityPlayer ep) {
        if (ep.isSneaking())
            return false;
        if (!ConfigRegistry.FAKEBEDROCK.getState() && ReikaPlayerAPI.isFake(ep))
            return false;

        World world = ep.worldObj;
        Block id = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        TreeType type = ReikaTreeHelper.getTree(id, meta);
        if (type == null)
            type = ModWoodList.getModWood(id, meta);
        int fortune = ReikaEnchantmentHelper.getEnchantmentLevel(Enchantment.fortune, is);
        TreeReader tree = new TreeReader();
        tree.setWorld(world);
        tree.setTree(type);

        tree.addTree(world, x, y, z);
        if (!tree.isRainbowTree() && !tree.isDyeTree())
            tree.clear();
        if (id == TwilightForestHandler.BlockEntry.ROOT.getBlock()) {
            int r = 2;
            for (int i = -r; i <= r; i++) {
                for (int j = -r; j <= r; j++) {
                    for (int k = -r; k <= r; k++) {
                        Block id2 = world.getBlock(x + i, y + j, z + k);
                        if (id2 == id) {
                            id.dropBlockAsItem(world, x + i, y + j, z + k, meta, fortune);
                            ReikaSoundHelper.playBreakSound(
                                world, x + i, y + j, z + k, id
                            );
                            world.setBlockToAir(x + i, y + j, z + k);
                        }
                    }
                }
            }
            return true;
        } else if (id == Blocks.red_mushroom_block || id == Blocks.brown_mushroom_block) {
            ;
            int r = 3;
            for (int i = -r; i <= r; i++) {
                for (int j = -r; j <= r; j++) {
                    for (int k = -r; k <= r; k++) {
                        Block id2 = world.getBlock(x + i, y + j, z + k);
                        int meta2 = world.getBlockMetadata(x + i, y + j, z + k);
                        if (id2 == Blocks.red_mushroom_block
                            || id2 == Blocks.brown_mushroom_block) {
                            id.dropBlockAsItem(world, x + i, y + j, z + k, meta, fortune);
                            ReikaSoundHelper.playBreakSound(
                                world, x + i, y + j, z + k, id, 0.25F, 1
                            );
                            world.setBlockToAir(x + i, y + j, z + k);
                        }
                    }
                }
            }
            return true;
        } else if (ModList.FORESTRY.isLoaded() && ForestryHandler.getInstance().isLog(id)) {
            this.cutEntireForestryTree(is, world, x, y, z, ep, id, meta);
            return true;
        } else if (!tree.isEmpty() && tree.isValidTree()) {
            this.cutEntireTree(is, world, tree, x, y, z, ep);
            return true;
        } else if (!world.isRemote) {
            if (type != null) {
                ProgressiveBreaker b
                    = ProgressiveRecursiveBreaker.instance.getTreeBreaker(
                        world, x, y, z, type
                    );
                b.player = ep;
                b.fortune = fortune;
                if (ModList.CHROMATICRAFT.isLoaded() && this.hasAutoCollect(is))
                    b.dropInventory = ep.inventory;
                ProgressiveRecursiveBreaker.instance.addCoordinate(world, b);
                return true;
            }
        }
        return false;
    }

    @ModDependent(ModList.CHROMATICRAFT)
    private boolean hasAutoCollect(ItemStack is) {
        return ReikaEnchantmentHelper.hasEnchantment(
            ChromaEnchants.AUTOCOLLECT.getEnchantment(), is
        );
    }

    private void cutEntireTree(
        ItemStack is,
        World world,
        TreeReader tree,
        int dx,
        int dy,
        int dz,
        EntityPlayer ep
    ) {
        int fortune = ReikaEnchantmentHelper.getEnchantmentLevel(Enchantment.fortune, is);
        boolean rainbow = tree.isRainbowTree();
        if (rainbow) {
            ArrayList<ItemStack> items = tree.getAllDroppedItems(world, fortune, ep);
            if (ModList.CHROMATICRAFT.isLoaded() && this.hasAutoCollect(is)) {
                Iterator<ItemStack> it = items.iterator();
                while (it.hasNext()) {
                    ItemStack in = it.next();
                    if (MinecraftForge.EVENT_BUS.post(new EntityItemPickupEvent(
                            ep, new EntityItem(world, dx + 0.5, dy + 0.5, dz + 0.5, is)
                        )))
                        it.remove();
                    else if (ReikaInventoryHelper.addToIInv(in, ep.inventory))
                        it.remove();
                }
            }
            ReikaItemHelper.dropItems(world, dx, dy, dz, items);
        }
        for (int i = 0; i < tree.getSize(); i++) {
            Coordinate c = tree.getNthBlock(i);
            int x = c.xCoord;
            int y = c.yCoord;
            int z = c.zCoord;
            Block b = world.getBlock(x, y, z);
            int meta = world.getBlockMetadata(x, y, z);
            if (b != null) {
                ReikaSoundHelper.playBreakSound(world, x, y, z, b, 0.75F, 1);
                if (!rainbow) {
                    if (ModList.CHROMATICRAFT.isLoaded() && this.hasAutoCollect(is)) {
                        this.setCollectionPlayer(ep);
                    }
                    b.dropBlockAsItem(world, x, y, z, meta, fortune);
                    if (ModList.CHROMATICRAFT.isLoaded()) {
                        this.setCollectionPlayer(null);
                    }
                }
                world.setBlockToAir(x, y, z);
            }
        }
    }

    @ModDependent(ModList.CHROMATICRAFT)
    private void setCollectionPlayer(EntityPlayer ep) {
        ChromaticEventManager.instance.collectItemPlayer = ep;
    }

    private void cutEntireForestryTree(
        ItemStack is,
        World world,
        int x,
        int y,
        int z,
        EntityPlayer ep,
        Block id,
        int meta
    ) {
        if (world.isRemote)
            return;
        int fortune = ReikaEnchantmentHelper.getEnchantmentLevel(Enchantment.fortune, is);
        ProgressiveBreaker b
            = ProgressiveRecursiveBreaker.instance.addCoordinateWithReturn(
                world, x, y, z, 64
            );
        //b.addBlock(new BlockKey(id, meta));
        b.setBlocks(
            false,
            new BlockKey(id),
            new BlockKey(ForestryHandler.BlockEntry.LEAF.getBlock())
        );
        b.player = ep;
        b.fortune = fortune;
        if (ModList.CHROMATICRAFT.isLoaded() && this.hasAutoCollect(is))
            b.dropInventory = ep.inventory;
    }

    public String getTextureFile() {
        return "/Reika/RotaryCraft/Textures/GUI/items.png"; //return the block texture
                                                            //where the block texture is
                                                            //saved in
    }

    public int getItemSpriteIndex(ItemStack item) {
        return index;
    }

    public void setIndex(int a) {
        index = a;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public final void registerIcons(IIconRegister ico) {}

    @Override
    public final IIcon getIconFromDamage(int dmg) {
        return RotaryCraft.instance.isLocked() ? ReikaTextureHelper.getMissingIcon()
                                               : Items.stone_axe.getIconFromDamage(0);
    }

    public Class getTextureReferenceClass() {
        return RotaryCraft.class;
    }

    @Override
    public String getTexture(ItemStack is) {
        return "/Reika/RotaryCraft/Textures/Items/items2.png";
    }

    @Override
    public boolean hitEntity(ItemStack is, EntityLivingBase target, EntityLivingBase ep) {
        if (target.getClass().getSimpleName().equals("EntityEnt")) {
            DamageSource src = ep instanceof EntityPlayer
                ? DamageSource.causePlayerDamage((EntityPlayer) ep)
                : DamageSource.generic;
            target.setHealth(1);
            target.attackEntityFrom(src, Integer.MAX_VALUE);
            return true;
        } else {
            return super.hitEntity(is, target, ep);
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack is) {
        return ItemRegistry.getEntry(is).getBasicName();
    }
}
