/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.Blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Reika.RotaryCraft.Auxiliary.ItemStacks;
import Reika.RotaryCraft.Base.BlockBasic;
import Reika.RotaryCraft.Registry.BlockRegistry;
import Reika.RotaryCraft.RotaryCraft;
import Reika.RotaryCraft.RotaryNames;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockDeco extends BlockBasic {
    public BlockDeco() {
        super(Material.iron);
        this.setHardness(4F);
        this.setResistance(10F);
        this.setLightLevel(0F);
        this.setStepSound(soundTypeMetal);

        //this.blockIndexInTexture = 29;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(
        Item par1, CreativeTabs par2CreativeTabs, List par3List
    ) //Adds the metadata blocks to the creative inventory
    {
        for (int var4 = 0; var4 < RotaryNames.blockNames.length; ++var4)
            par3List.add(new ItemStack(par1, 1, var4));
    }

    @Override
    public int damageDropped(int par1) {
        return par1;
    }

    @Override
    public ArrayList<ItemStack>
    getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(BlockRegistry.DECO.getStackOfMetadata(metadata));
        return ret;
    }

    @Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity e) {
        return super.canEntityDestroy(world, x, y, z, e)
            && world.getBlockMetadata(x, y, z)
            != ItemStacks.bedingotblock.getItemDamage();
    }

    @Override
    public float getExplosionResistance(
        Entity e, World world, int x, int y, int z, double eX, double eY, double eZ
    ) {
        if (world.getBlockMetadata(x, y, z) == ItemStacks.bedingotblock.getItemDamage())
            return 6000F;
        return super.getExplosionResistance(e, world, x, y, z, eX, eY, eZ);
    }

    @Override
    public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
        super.onBlockExploded(world, x, y, z, explosion);
        if (world.getBlockMetadata(x, y, z) == ItemStacks.bedingotblock.getItemDamage())
            world.setBlock(x, y, z, this, ItemStacks.bedingotblock.getItemDamage(), 3);
    }

    @Override
    public boolean isOpaqueCube() {
        return true;
    }

    public int getBlockTextureFromSideAndMetadata(int side, int meta) {
        switch (meta) {
            case 0:
                return 29;
            case 1:
                return 75;
            case 2:
                return 76;
            default:
                return 0;
        }
    }

    @Override
    public int
    getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        if (world.getBlockMetadata(x, y, z) == ItemStacks.anthrablock.getItemDamage())
            return 30;
        if (world.getBlockMetadata(x, y, z) == ItemStacks.cokeblock.getItemDamage())
            return 20;
        return 0;
    }

    @Override
    public int
    getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        if (world.getBlockMetadata(x, y, z) == ItemStacks.anthrablock.getItemDamage())
            return 4;
        if (world.getBlockMetadata(x, y, z) == ItemStacks.cokeblock.getItemDamage())
            return 3;
        return 0;
    }

    @Override
    public int quantityDropped(Random par1Random) {
        return 1;
    }

    @Override
    public IIcon getIcon(int s, int meta) {
        return icons[meta][0];
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        if (RotaryCraft.instance.isLocked())
            return;
        icons[0][0] = par1IconRegister.registerIcon("RotaryCraft:steel");
        icons[1][0] = par1IconRegister.registerIcon("RotaryCraft:anthra");
        icons[2][0] = par1IconRegister.registerIcon("RotaryCraft:lons");
        icons[3][0] = par1IconRegister.registerIcon("RotaryCraft:shield");
        icons[4][0] = par1IconRegister.registerIcon("RotaryCraft:bedrock");
        icons[5][0] = par1IconRegister.registerIcon("RotaryCraft:coke");
    }

    @Override
    public boolean isBeaconBase(
        IBlockAccess worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ
    ) {
        return true;
    }
}
