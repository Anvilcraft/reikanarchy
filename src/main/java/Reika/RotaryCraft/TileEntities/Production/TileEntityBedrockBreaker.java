/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.TileEntities.Production;

import Reika.DragonAPI.Instantiable.Data.Immutable.Coordinate;
import Reika.DragonAPI.Interfaces.TileEntity.InertIInv;
import Reika.DragonAPI.Interfaces.TileEntity.PartialInventory;
import Reika.DragonAPI.Libraries.IO.ReikaSoundHelper;
import Reika.DragonAPI.Libraries.Java.ReikaRandomHelper;
import Reika.DragonAPI.Libraries.MathSci.ReikaMathLibrary;
import Reika.DragonAPI.Libraries.Registry.ReikaItemHelper;
import Reika.DragonAPI.Libraries.Registry.ReikaParticleHelper;
import Reika.DragonAPI.Libraries.ReikaInventoryHelper;
import Reika.DragonAPI.Libraries.ReikaPlayerAPI;
import Reika.DragonAPI.Libraries.Rendering.ReikaRenderHelper;
import Reika.DragonAPI.Libraries.World.ReikaWorldHelper;
import Reika.DragonAPI.ModInteract.ItemHandlers.FactorizationHandler;
import Reika.RotaryCraft.API.Event.BedrockDigEvent;
import Reika.RotaryCraft.API.Interfaces.SurrogateBedrock;
import Reika.RotaryCraft.Auxiliary.Interfaces.DiscreteFunction;
import Reika.RotaryCraft.Auxiliary.ItemStacks;
import Reika.RotaryCraft.Base.TileEntity.InventoriedPowerReceiver;
import Reika.RotaryCraft.Blocks.BlockBedrockSlice.TileEntityBedrockSlice;
import Reika.RotaryCraft.Registry.BlockRegistry;
import Reika.RotaryCraft.Registry.ConfigRegistry;
import Reika.RotaryCraft.Registry.DifficultyEffects;
import Reika.RotaryCraft.Registry.DurationRegistry;
import Reika.RotaryCraft.Registry.MachineRegistry;
import Reika.RotaryCraft.Registry.RotaryAchievements;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityBedrockBreaker
    extends InventoriedPowerReceiver implements InertIInv, DiscreteFunction {
    private ForgeDirection facing;
    private int step = 1;

    private double dropx;
    private double dropy;
    private double dropz;

    public ForgeDirection getFacing() {
        return facing != null ? facing : ForgeDirection.UNKNOWN;
    }

    @Override
    public void updateEntity(World world, int x, int y, int z, int meta) {
        super.updateTileEntity();
        tickcount++;
        this.readPower(false);
        this.getIOSides(world, x, y, z, meta);
        if (power >= MINPOWER && torque >= MINTORQUE) {
            int time = this.getOperationTime();
            if (time <= 1)
                RotaryAchievements.INSTANTBED.triggerAchievement(this.getPlacer());
            if (tickcount >= time) {
                this.process(world, x, y, z, meta);
                tickcount = 0;
            }
            int hx = x + step * facing.offsetX;
            int hy = y + step * facing.offsetY;
            int hz = z + step * facing.offsetZ;
            Block b = world.getBlock(hx, hy, hz);
            if (b != Blocks.air) {
                float f = step + this.getGrindFraction() - 0.5F;
                for (int i = 0; i < 4; i++) {
                    double px = x + 0.5 + facing.offsetX * f;
                    double py = y + 0.5 + facing.offsetY * f;
                    double pz = z + 0.5 + facing.offsetZ * f;
                    if (facing.offsetX == 0) {
                        px = ReikaRandomHelper.getRandomPlusMinus(px, 0.5);
                    }
                    if (facing.offsetY == 0) {
                        py = ReikaRandomHelper.getRandomPlusMinus(py, 0.5);
                    }
                    if (facing.offsetZ == 0) {
                        pz = ReikaRandomHelper.getRandomPlusMinus(pz, 0.5);
                    }
                    ReikaParticleHelper.CRITICAL.spawnAt(world, px, py, pz);
                }
                //ReikaParticleHelper.CRITICAL.spawnAroundBlock(world, hx, hy, hz, 4);
                ReikaSoundHelper.playStepSound(
                    world, hx, hy, hz, b, 0.5F + rand.nextFloat(), 0.5F * rand.nextFloat()
                );
                //ReikaSoundHelper.playSoundAtBlock(world, hx, hy, hz, "dig.stone", );
            }
        }
    }

    public void process(World world, int x, int y, int z, int metadata) {
        if (this.hasInventorySpace()) {
            Coordinate c = this.getHeadLocation();
            if (this.canBreakAt(world, c.xCoord, c.yCoord, c.zCoord)) {
                this.grind(world, x, y, z, c.xCoord, c.yCoord, c.zCoord, metadata);
            }
        }
    }

    public Coordinate getHeadLocation() {
        return new Coordinate(this).offset(this.getFacing(), step);
    }

    private boolean canBreakAt(World world, int x, int y, int z) {
        if (y < 0)
            return false;
        if (y > 255)
            return false;
        if (y == 0 && !ConfigRegistry.VOIDHOLE.getState())
            return false;
        return world.isRemote
            || ReikaPlayerAPI.playerCanBreakAt(
                (WorldServer) world, x, y, z, this.getServerPlacer()
            );
    }

    private boolean processBlock(World world, int x, int y, int z) {
        Block b = world.getBlock(x, y, z);
        if (this.isBedrock(world, x, y, z))
            return true;
        if (b == BlockRegistry.BEDROCKSLICE.getBlockInstance())
            return true;
        return false;
    }

    private boolean hasInventorySpace() {
        if (inv[0] == null)
            return true;
        if (!ReikaItemHelper.matchStacks(inv[0], ItemStacks.bedrockdust))
            return false;
        return inv[0].stackSize + DifficultyEffects.BEDROCKDUST.getInt()
            <= inv[0].getMaxStackSize();
    }

    public void readPower(boolean doublesided) {
        if (!this.getReceptor(worldObj, xCoord, yCoord, zCoord, this.getBlockMetadata()))
            return;
        super.getPower(doublesided);
        power = (long) omega * (long) torque;
    }

    public boolean getReceptor(World world, int x, int y, int z, int metadata) {
        if (y == 0 && !ConfigRegistry.VOIDHOLE.getState())
            return false;
        switch (metadata) {
            case 0:
                read = ForgeDirection.WEST;
                break;
            case 1:
                read = ForgeDirection.EAST;
                break;
            case 2:
                read = ForgeDirection.NORTH;
                break;
            case 3:
                read = ForgeDirection.SOUTH;
                break;
            case 4:
                read = ForgeDirection.DOWN;
                break;
            case 5:
                read = ForgeDirection.UP;
                break;
        }
        return true;
    }

    public void getIOSides(World world, int x, int y, int z, int metadata) {
        switch (metadata) {
            case 0:
                dropx = x + 0.5;
                dropy = y + 1.25;
                dropz = z + 0.5;
                facing = ForgeDirection.EAST;
                break;
            case 1:
                dropx = x + 0.5;
                dropy = y + 1.25;
                dropz = z + 0.5;
                facing = ForgeDirection.WEST;
                break;
            case 2:
                dropx = x + 0.5;
                dropy = y + 1.25;
                dropz = z + 0.5;
                facing = ForgeDirection.SOUTH;
                break;
            case 3:
                dropx = x + 0.5;
                dropy = y + 1.25;
                dropz = z + 0.5;
                facing = ForgeDirection.NORTH;
                break;
            case 4:
                dropx = x + 0.5;
                dropy = y + 1.25;
                dropz = z + 0.5;
                facing = ForgeDirection.UP;
                break;
            case 5:
                dropx = x + 0.5;
                dropy = y - 0.25;
                dropz = z + 0.5;
                facing = ForgeDirection.DOWN;
                break;
        }
    }

    private boolean isBedrock(World world, int x, int y, int z) {
        Block id = world.getBlock(x, y, z);
        if (id == Blocks.bedrock)
            return true;
        if (id == FactorizationHandler.getInstance().bedrockID)
            return true;
        if (id instanceof SurrogateBedrock) {
            return ((SurrogateBedrock) id).isBedrock(world, x, y, z);
        }
        return false;
    }

    private void
    grind(World world, int mx, int my, int mz, int x, int y, int z, int meta) {
        if (this.processBlock(world, x, y, z)) {
            if (this.isBedrock(world, x, y, z)) {
                world.playSoundEffect(
                    x + 0.5D,
                    y + 0.5D,
                    z + 0.5D,
                    "dig.stone",
                    0.5F,
                    rand.nextFloat() * 0.4F + 0.8F
                );
                world.setBlock(
                    x, y, z, BlockRegistry.BEDROCKSLICE.getBlockInstance(), 0, 3
                );
                ((TileEntityBedrockSlice) world.getTileEntity(x, y, z))
                    .setDirection(this.getFacing().getOpposite());
            } else {
                int rockmetadata = world.getBlockMetadata(x, y, z);
                if (rockmetadata < 15) {
                    world.playSoundEffect(
                        x + 0.5D,
                        y + 0.5D,
                        z + 0.5D,
                        "dig.stone",
                        0.5F,
                        rand.nextFloat() * 0.4F + 0.8F
                    );
                    world.setBlockMetadataWithNotify(x, y, z, rockmetadata + 1, 3);
                    step--;
                    this.incrementStep(world, mx, my, mz);
                } else {
                    world.playSoundEffect(
                        x + 0.5D,
                        y + 0.5D,
                        z + 0.5D,
                        "mob.blaze.hit",
                        0.5F,
                        rand.nextFloat() * 0.4F + 0.8F
                    );
                    ItemStack is = this.getDrops(world, x, y, z);
                    world.setBlockToAir(x, y, z);
                    if (!this.chestCheck(world, x, y, z, is)) {
                        if (this.isInventoryFull())
                            ReikaItemHelper.dropItem(world, dropx, dropy, dropz, is);
                        else
                            ReikaInventoryHelper.addOrSetStack(is, inv, 0);
                    }
                    RotaryAchievements.BEDROCKBREAKER.triggerAchievement(this.getPlacer()
                    );
                    MinecraftForge.EVENT_BUS.post(new BedrockDigEvent(this, x, y, z));
                    if (world.isRemote)
                        this.incrementStep(world, mx, my, mz);
                }
            }
        } else {
            Block b = world.getBlock(x, y, z);
            if (b != Blocks.air && b.getBlockHardness(world, x, y, z) >= 0) {
                ReikaSoundHelper.playBreakSound(world, x, y, z, b);
                if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
                    ReikaRenderHelper.spawnDropParticles(
                        world, x, y, z, b, world.getBlockMetadata(x, y, z)
                    );
                world.setBlockToAir(x, y, z);
            }
            if (!world.isRemote)
                this.incrementStep(world, mx, my, mz);
        }
    }

    private boolean chestCheck(World world, int x, int y, int z, ItemStack is) {
        if (is == null)
            return false;
        if (world.isRemote)
            return false;
        for (int i = 0; i < 6; i++) {
            ForgeDirection dir = dirs[i];
            TileEntity te = this.getAdjacentTileEntity(dir);
            if (te instanceof IInventory) {
                boolean flag = true;
                if (te instanceof PartialInventory) {
                    if (!((PartialInventory) te).hasInventory())
                        flag = false;
                }
                if (flag) {
                    if (ReikaInventoryHelper.addToIInv(is, (IInventory) te))
                        return true;
                }
            }
        }
        return false;
    }

    private void incrementStep(World world, int x, int y, int z) {
        int max = step + 1;
        for (int i = 1; i < max; i++) {
            int dx = x + i * facing.offsetX;
            int dy = y + i * facing.offsetY;
            int dz = z + i * facing.offsetZ;
            //ReikaJavaLibrary.pConsole(step+"; "+i+" @ "+dx+","+dy+","+dz+" :
            //"+world.getBlock(dx, dy, dz), Side.SERVER);
            if (!ReikaWorldHelper.softBlocks(world, dx, dy, dz)) {
                step = i;
                return;
            }
        }
        step = max;
    }

    public void dropInventory() {
        int meta = this.getBlockMetadata();
        if (inv[0] == null)
            return;
        EntityItem itementity = new EntityItem(worldObj, dropx, dropy, dropz, inv[0]);
        itementity.delayBeforeCanPickup = 0;
        itementity.motionX = -0.025 + 0.05 * rand.nextFloat(); // 0-0.5 m/s
        itementity.motionZ = -0.025 + 0.05 * rand.nextFloat();
        if (meta != 5)
            itementity.motionY = 0.1 + 0.2 * rand.nextFloat()
                + 0.25 * rand.nextFloat()
                    * rand.nextInt(2); // 2-6m/s up, + a 50/50 chance of 0-5 m/s more up
        itementity.velocityChanged = true;
        if (!worldObj.isRemote)
            worldObj.spawnEntityInWorld(itementity);
        worldObj.playSoundEffect(
            xCoord + 0.5,
            yCoord + 0.5,
            zCoord + 0.5,
            "random.pop",
            0.2F,
            ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F
        );
        inv[0] = null;
    }

    private ItemStack getDrops(World world, int x, int y, int z) {
        return ReikaItemHelper.getSizedItemStack(
            ItemStacks.bedrockdust, this.getNumberDust(world, x, y, z)
        );
    }

    private int getNumberDust(World world, int x, int y, int z) {
        float f = Math.min(
            1, ((TileEntityBedrockSlice) world.getTileEntity(x, y, z)).dustYield
        );
        return Math.max(1, (int) (f * DifficultyEffects.BEDROCKDUST.getInt()));
        //return DifficultyEffects.BEDROCKDUST.getInt();
    }

    public int getContents() {
        return inv[0] != null
                && ReikaItemHelper.matchStacks(inv[0], ItemStacks.bedrockdust)
            ? inv[0].stackSize
            : 0;
    }

    private void setContents(int num) {
        inv[0] = ReikaItemHelper.getSizedItemStack(ItemStacks.bedrockdust, num);
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    protected void animateWithTick(World world, int x, int y, int z) {
        if (!this.isInWorld()) {
            phi = 0;
            return;
        }
        if (power < MINPOWER || torque < MINTORQUE)
            return;
        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
    }

    @Override
    public MachineRegistry getTile() {
        return MachineRegistry.BEDROCKBREAKER;
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack is) {
        return false;
    }

    public boolean isInventoryFull() {
        if (inv[0] == null)
            return false;
        if (!ReikaItemHelper.matchStacks(ItemStacks.bedrockdust, inv[0]))
            return true;
        return inv[0].stackSize >= inv[0].getMaxStackSize();
    }

    @Override
    public void onEMP() {}

    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return false;
    }

    @Override
    public int getOperationTime() {
        return DurationRegistry.BEDROCK.getOperationTime(omega);
    }

    @Override
    protected void writeSyncTag(NBTTagCompound NBT) {
        super.writeSyncTag(NBT);

        NBT.setInteger("step", step);
    }

    @Override
    protected void readSyncTag(NBTTagCompound NBT) {
        super.readSyncTag(NBT);

        step = NBT.getInteger("step");
    }

    public int getStep() {
        return step;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    public float getGrindFraction() {
        TileEntity te = this.getHeadLocation().getTileEntity(worldObj);
        if (te instanceof TileEntityBedrockSlice) {
            return te.getBlockMetadata() / 16F;
        }
        return 0;
    }
}
