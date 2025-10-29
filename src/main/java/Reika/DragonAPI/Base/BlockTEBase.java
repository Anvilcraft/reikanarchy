package Reika.DragonAPI.Base;

import Reika.DragonAPI.ASM.APIStripper.Strippable;
import Reika.DragonAPI.Instantiable.Event.TileEntityMoveEvent;
import Reika.DragonAPI.Interfaces.TileEntity.BreakAction;
import Reika.DragonAPI.Interfaces.TileEntity.ConditionBreakDropsInventory;
import Reika.DragonAPI.Libraries.Registry.ReikaItemHelper;
import Reika.DragonAPI.Libraries.ReikaDirectionHelper;
import framesapi.IMoveCheck;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

@Strippable(value = { "framesapi.IMoveCheck", "vazkii.botania.api.mana.ILaputaImmobile" })
public abstract class BlockTEBase extends Block implements IMoveCheck {
    public BlockTEBase(Material mat) {
        super(mat);
    }

    @Override
    public abstract boolean hasTileEntity(int meta);

    @Override
    public abstract TileEntity createTileEntity(World world, int meta);

    @Override
    public void
    onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase e, ItemStack is) {
        if (e instanceof EntityPlayer) {
            TileEntityBase te = (TileEntityBase) world.getTileEntity(x, y, z);
            if (te != null)
                te.setPlacer((EntityPlayer) e);
        }
    }

    @Override
    public final float getBlockHardness(World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityBase && ((TileEntityBase) te).isUnMineable())
            return -1;
        return blockHardness;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block b) {
        TileEntityBase te = (TileEntityBase) world.getTileEntity(x, y, z);
        if (te != null)
            te.onBlockUpdate();
    }

    @Override
    public final void onNeighborChange(
        IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ
    ) {
        ForgeDirection dir
            = ReikaDirectionHelper.getDirectionBetween(x, y, z, tileX, tileY, tileZ);
        TileEntityBase te = (TileEntityBase) world.getTileEntity(x, y, z);
        if (te != null)
            te.updateCache(dir);
    }

    public void updateTileCache(World world, int x, int y, int z) {
        TileEntityBase te = (TileEntityBase) world.getTileEntity(x, y, z);
        for (int i = 0; i < 6; i++) {
            ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];
            te.updateCache(dir);
        }
    }

    public final AxisAlignedBB getBlockAABB(int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1);
    }

    protected final void setFullBlockBounds() {
        this.setBlockBounds(0, 0, 0, 1, 1, 1);
    }

    protected final void setBounds(AxisAlignedBB box, int x, int y, int z) {
        this.setBlockBounds(
            (float) box.minX - x,
            (float) box.minY - y,
            (float) box.minZ - z,
            (float) box.maxX - x,
            (float) box.maxY - y,
            (float) box.maxZ - z
        );
    }

    @Override
    public boolean onBlockActivated(
        World world,
        int x,
        int y,
        int z,
        EntityPlayer ep,
        int side,
        float par7,
        float par8,
        float par9
    ) {
        TileEntityBase te = (TileEntityBase) world.getTileEntity(x, y, z);
        te.syncAllData(true);
        return false;
    }

    @Override
    public final boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public final int
    getComparatorInputOverride(World world, int x, int y, int z, int par5) {
        return ((TileEntityBase) world.getTileEntity(x, y, z)).getComparatorOverride();
    }

    @Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity e) {
        return false;
    }

    @Override
    public boolean canMove(World world, int x, int y, int z) {
        return !TileEntityMoveEvent.fireTileMoveEvent(world, x, y, z);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
        TileEntity te = world.getTileEntity(x, y, z);
        boolean drops = te instanceof IInventory;
        if (te instanceof ConditionBreakDropsInventory) {
            drops &= ((ConditionBreakDropsInventory) te).dropsInventoryOnBroken();
        }
        if (drops)
            ReikaItemHelper.dropInventory(world, x, y, z);
        if (te instanceof BreakAction) {
            ((BreakAction) te).breakBlock();
        }
        super.breakBlock(world, x, y, z, par5, par6);
    }
}
