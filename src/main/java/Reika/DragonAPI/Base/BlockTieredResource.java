package Reika.DragonAPI.Base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import Reika.DragonAPI.Libraries.IO.ReikaSoundHelper;
import Reika.DragonAPI.Libraries.Java.ReikaRandomHelper;
import Reika.DragonAPI.Libraries.Registry.ReikaItemHelper;
import Reika.DragonAPI.Libraries.Rendering.ReikaRenderHelper;
import Reika.DragonAPI.Libraries.World.ReikaBlockHelper;
import Reika.DragonAPI.Libraries.World.ReikaWorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

public abstract class BlockTieredResource extends Block {
    protected static final Random rand = new Random();

    public BlockTieredResource(Material mat) {
        super(mat);
    }

    @Override
    public final Item getItemDropped(int meta, Random r, int fortune) {
        return null;
    }

    @Override
    public final ArrayList<ItemStack>
    getDrops(World world, int x, int y, int z, int meta, int fortune) {
        return new ArrayList();
    }

    @Override
    protected final boolean canSilkHarvest() {
        return false;
    }

    @Override
    public final int quantityDropped(int meta, int fortune, Random random) {
        return 0;
    }

    @Override
    protected final void dropBlockAsItem(World world, int x, int y, int z, ItemStack is) {

    }

    @Override
    public final ItemStack createStackedBlock(int meta) {
        return null;
    }

    @Override
    public ItemStack
    getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return ReikaBlockHelper.getWorldBlockAsItemStack(world, x, y, z);
    }

    @Override
    public final boolean canHarvestBlock(EntityPlayer player, int meta) {
        return false;
    }

    @Override
    public final boolean removedByPlayer(
        World world, EntityPlayer player, int x, int y, int z, boolean willHarvest
    ) {
        Collection<ItemStack> li = null;
        boolean tier = this.isPlayerSufficientTier(world, x, y, z, player);
        int fortune = EnchantmentHelper.getFortuneModifier(player);
        if (player.capabilities.isCreativeMode) {
        } else {
            if (tier) {
                li = this.getHarvestResources(world, x, y, z, fortune, player);
            } else {
                li = this.getNoHarvestResources(world, x, y, z, fortune, player);
            }
        }
        boolean flag = super.removedByPlayer(world, player, x, y, z, willHarvest);
        if (!player.capabilities.isCreativeMode && flag && li != null && tier) {
            li = new ArrayList(li);
            MinecraftForge.EVENT_BUS.post(new HarvestDropsEvent(
                x,
                y,
                z,
                world,
                this,
                world.getBlockMetadata(x, y, z),
                fortune,
                1,
                (ArrayList<ItemStack>) li,
                player,
                false
            ));
            for (ItemStack is : li) {
                double rx = ReikaRandomHelper.getRandomPlusMinus(x + 0.5, 0.25);
                double ry = ReikaRandomHelper.getRandomPlusMinus(y + 0.5, 0.25);
                double rz = ReikaRandomHelper.getRandomPlusMinus(z + 0.5, 0.25);
                ReikaItemHelper.dropItem(world, rx, ry, rz, is, 0);
            }
            ReikaWorldHelper.splitAndSpawnXP(
                world, x + 0.5, y + 0.5, z + 0.5, 2 + rand.nextInt(5)
            );
        }
        return flag;
    }

    public abstract Collection<ItemStack> getHarvestResources(
        World world, int x, int y, int z, int fortune, EntityPlayer player
    );

    public Collection<ItemStack> getNoHarvestResources(
        World world, int x, int y, int z, int fortune, EntityPlayer player
    ) {
        return new ArrayList();
    }

    public abstract boolean
    isPlayerSufficientTier(IBlockAccess world, int x, int y, int z, EntityPlayer ep);

    @Override
    public final void onBlockPlacedBy(
        World world, int x, int y, int z, EntityLivingBase elb, ItemStack is
    ) {
        if (elb instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer) elb;
            if (!this.isPlayerSufficientTier(world, x, y, z, ep)) {
                if (world.isRemote)
                    ReikaRenderHelper.spawnDropParticles(
                        world, x, y, z, this, world.getBlockMetadata(x, y, z)
                    );
                ReikaSoundHelper.playBreakSound(world, x, y, z, this);
                world.setBlockToAir(x, y, z);
            }
        }
    }
}
