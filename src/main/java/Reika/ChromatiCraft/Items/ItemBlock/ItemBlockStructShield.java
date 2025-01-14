/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Items.ItemBlock;

import java.util.List;

import Reika.ChromatiCraft.Block.Worldgen.BlockStructureShield;
import Reika.ChromatiCraft.Registry.ChromaBlocks;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockStructShield extends ItemBlock {
    public ItemBlockStructShield(Block b) {
        super(b);
        hasSubtypes = true;
    }

    @Override
    public void getSubItems(Item id, CreativeTabs par2CreativeTabs, List par3List) {
        for (int i = 0; i < BlockStructureShield.BlockType.list.length; i++)
            par3List.add(new ItemStack(id, 1, i));
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

    @Override
    public String getItemStackDisplayName(ItemStack is) {
        return ChromaBlocks.getEntryByID(field_150939_a)
            .getMultiValuedName(is.getItemDamage());
    }
}
