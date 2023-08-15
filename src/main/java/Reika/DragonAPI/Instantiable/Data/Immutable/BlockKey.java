/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.Instantiable.Data.Immutable;

import Reika.DragonAPI.Exception.MisuseException;
import Reika.DragonAPI.Interfaces.BlockCheck;
import Reika.DragonAPI.Interfaces.Registry.BlockEnum;
import Reika.DragonAPI.Interfaces.Registry.RegistryEntry;
import Reika.DragonAPI.Interfaces.Registry.TileEnum;
import Reika.DragonAPI.Libraries.ReikaRegistryHelper;
import com.google.common.base.Strings;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public final class BlockKey implements BlockCheck, Comparable<BlockKey> {
    public final Block blockID;
    public final int metadata;

    public static final BlockKey AIR = new BlockKey(Blocks.air);

    public BlockKey(Block b) {
        this(b, -1);
    }

    public BlockKey(Block b, int meta) {
        metadata = meta;
        blockID = b;
        if (b == null)
            throw new MisuseException("Cannot create a BlockKey from a null block!");
    }

    public BlockKey(TileEnum m) {
        metadata = m.getBlockMetadata();
        blockID = m.getBlock();
    }

    public static BlockKey fromItem(ItemStack is) {
        if (is == null || is.getItem() == null)
            throw new MisuseException("Cannot create a BlockKey from a null item!");
        Block b = Block.getBlockFromItem(is.getItem());
        if (b == null)
            throw new MisuseException(
                "Cannot create a BlockKey with an item with no block!"
            );
        return new BlockKey(b, is.getItemDamage());
    }

    public static BlockKey getAt(IBlockAccess world, int x, int y, int z) {
        return new BlockKey(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z));
    }

    public static BlockKey getAtNoMeta(IBlockAccess world, int x, int y, int z) {
        return new BlockKey(world.getBlock(x, y, z), -1);
    }

    @Override
    public int hashCode() {
        return blockID.hashCode() /* + metadata << 24*/;
    }

    @Override
    public boolean equals(Object o) {
        //ReikaJavaLibrary.pConsole(this+" & "+o);
        if (o instanceof BlockKey) {
            BlockKey b = (BlockKey) o;
            return b.blockID == blockID
                && (!this.hasMetadata() || !b.hasMetadata() || b.metadata == metadata);
        }
        return false;
    }

    @Override
    public String toString() {
        return blockID.getUnlocalizedName() + ":" + metadata;
    }

    public boolean hasMetadata() {
        return metadata >= 0 && metadata != OreDictionary.WILDCARD_VALUE;
    }

    public ItemStack asItemStack() {
        return new ItemStack(blockID, 1, this.hasMetadata() ? metadata : 0);
    }

    public ItemStack getDisplay() {
        return this.asItemStack();
    }

    public boolean match(Block b, int meta) {
        return b == blockID && (!this.hasMetadata() || meta == metadata);
    }

    public boolean matchInWorld(World world, int x, int y, int z) {
        return this.match(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z));
    }

    public void place(World world, int x, int y, int z) {
        this.place(world, x, y, z, 3);
    }

    @Override
    public void place(World world, int x, int y, int z, int flags) {
        world.setBlock(x, y, z, blockID, this.hasMetadata() ? metadata : 0, flags);
    }

    @Override
    public BlockKey asBlockKey() {
        return this;
    }

    public String getLocalized() {
        if (blockID == Blocks.air)
            return "Air";
        RegistryEntry reg = ReikaRegistryHelper.getRegistryForObject(blockID);
        if (reg instanceof BlockEnum) {
            BlockEnum be = (BlockEnum) reg;
            return this.hasMetadata() && be.hasMultiValuedName()
                ? be.getMultiValuedName(metadata)
                : be.getBasicName();
        }
        return blockID.getLocalizedName() + ":" + metadata;
    }

    public void writeToNBT(String tag, NBTTagCompound NBT) {
        NBTTagCompound dat = new NBTTagCompound();
        dat.setString("id", Block.blockRegistry.getNameForObject(blockID));
        dat.setInteger("meta", metadata);
        NBT.setTag(tag, dat);
    }

    public static BlockKey readFromNBT(String tag, NBTTagCompound NBT) {
        NBTTagCompound dat = NBT.getCompoundTag(tag);
        String id = dat.getString("id");
        Block b = Strings.isNullOrEmpty(id) ? null : Block.getBlockFromName(id);
        int meta = dat.getInteger("meta");
        return b != null ? new BlockKey(b, meta) : null;
    }

    @Override
    public boolean match(BlockCheck bc) {
        return bc instanceof BlockKey && ((BlockKey) bc).equals(this);
    }

    public int getBlockID() {
        return Block.getIdFromBlock(blockID);
    }

    @Override
    public int compareTo(BlockKey o) {
        return 100000 * Integer.compare(this.getBlockID(), o.getBlockID())
            + Integer.compare(this.getMetadataForCompare(), o.getMetadataForCompare());
    }

    private int getMetadataForCompare() {
        if (metadata == -1 || metadata == OreDictionary.WILDCARD_VALUE)
            return 65536;
        return metadata;
    }
}
