/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.Instantiable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import Reika.DragonAPI.Libraries.Registry.ReikaItemHelper;
import Reika.DragonAPI.Libraries.ReikaEnchantmentHelper;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class ItemDrop {
    public final int minDrops;
    public final int maxDrops;

    private final ItemStack item;

    private static final Random rand = new Random();

    public ItemDrop(Block b) {
        this(b, 1, 1);
    }

    public ItemDrop(Block b, int min, int max) {
        this(new ItemStack(b), min, max);
    }

    public ItemDrop(Item i) {
        this(i, 1, 1);
    }

    public ItemDrop(Item i, int min, int max) {
        this(new ItemStack(i), min, max);
    }

    public ItemDrop(ItemStack is, int min, int max) {
        maxDrops = max;
        minDrops = min;
        item = is != null ? is.copy() : null;
    }

    public void enchant(HashMap<Enchantment, Integer> map) {
        ReikaEnchantmentHelper.applyEnchantments(item, map);
    }

    public void enchant(Enchantment ench, int level) {
        item.addEnchantment(ench, level);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ItemDrop) {
            ItemDrop it = (ItemDrop) o;
            if (!ReikaItemHelper.matchStacks(item, it.item))
                return false;
            return ItemStack.areItemStackTagsEqual(item, it.item);
        }
        return false;
    }

    public ItemStack getItemStack() {
        return item.copy();
    }

    public ItemStack getItem() {
        return this.getItem(1);
    }

    public ItemStack getItem(float f) {
        int num = this.getDropCount(f);
        ItemStack is = ReikaItemHelper.getSizedItemStack(this.getItemStack(), num);
        return is;
    }

    public int getDropCount() {
        return this.getDropCount(1);
    }

    public int getDropCount(float multiplier) {
        return minDrops + (int) (multiplier * rand.nextInt(1 + maxDrops - minDrops));
    }

    public void drop(World world, double x, double y, double z) {
        this.drop(world, x, y, z, 1);
    }

    public void drop(World world, double x, double y, double z, float f) {
        ReikaItemHelper.dropItem(world, x, y, z, this.getItem(f));
    }

    public EntityItem drop(Entity e) {
        return this.drop(e, 1);
    }

    public EntityItem drop(Entity e, float f) {
        return ReikaItemHelper.dropItem(
            e.worldObj, e.posX, e.posY + 0.25, e.posZ, this.getItem(f)
        );
    }

    public Item getID() {
        return item.getItem();
    }

    public int getMetadata() {
        return item.getItemDamage();
    }

    public boolean isEnchanted() {
        Map map = EnchantmentHelper.getEnchantments(item);
        return map != null && !map.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(item.getDisplayName());
        sb.append(": ");
        sb.append(item.getItem());
        sb.append(":");
        sb.append(item.getItemDamage());
        sb.append(" (");
        if (minDrops != maxDrops) {
            sb.append(minDrops);
            sb.append("-");
        }
        sb.append(maxDrops);
        sb.append(")");
        if (this.isEnchanted()) {
            sb.append("; ");
            sb.append(EnchantmentHelper.getEnchantments(item));
        }
        return sb.toString();
    }

    public static class OreDrop extends ItemDrop {
        public final String oreName;

        public OreDrop(String key, int min, int max) {
            super((ItemStack) null, min, max);
            oreName = key;
        }

        @Override
        public void enchant(HashMap<Enchantment, Integer> map) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void enchant(Enchantment ench, int level) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Item getID() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getMetadata() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isEnchanted() {
            return false;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof OreDrop) {
                return oreName.equals(((OreDrop) o).oreName);
            }
            return false;
        }

        @Override
        public ItemStack getItemStack() {
            ArrayList<ItemStack> li = OreDictionary.getOres(oreName);
            return li.isEmpty() ? null : li.get(rand.nextInt(li.size()));
        }
    }
}
