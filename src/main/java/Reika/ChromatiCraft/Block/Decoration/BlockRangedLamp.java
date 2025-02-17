/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Block.Decoration;

import Reika.ChromatiCraft.ChromatiCraft;
import Reika.ChromatiCraft.Registry.ChromaGuis;
import Reika.ChromatiCraft.Registry.ChromaISBRH;
import Reika.ChromatiCraft.Registry.ChromaItems;
import Reika.ChromatiCraft.Registry.ChromaOptions;
import Reika.ChromatiCraft.Registry.CrystalElement;
import Reika.ChromatiCraft.TileEntity.AOE.TileEntityLampController;
import Reika.DragonAPI.Interfaces.TileEntity.GuiController;
import Reika.DragonAPI.Libraries.Registry.ReikaDyeHelper;
import Reika.DragonAPI.Libraries.Rendering.ReikaColorAPI;
import Reika.DragonAPI.ModList;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockRangedLamp extends Block {
    private IIcon dark;
    private IIcon light;

    public BlockRangedLamp(Material mat) {
        super(mat);
        blockHardness = 5;
        blockResistance = 10;
        stepSound = new SoundType("stone", 1.0F, 0.5F);
        this.setCreativeTab(ChromatiCraft.tabChromaDeco);
        this.setLightOpacity(0);
    }

    @Override
    public final boolean isOpaqueCube() {
        return false;
    }

    @Override
    public final boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getLightValue(IBlockAccess iba, int x, int y, int z) {
        this.setLightOpacity(0);
        boolean lit = ((TileEntityRangedLamp) iba.getTileEntity(x, y, z)).isLit();
        if (lit) {
            if (ModList.COLORLIGHT.isLoaded()) {
                int color = this.colorMultiplier(iba, x, y, z);
                return ReikaColorAPI.getPackedIntForColoredLight(color, 15) | 15;
            } else {
                return 15;
            }
        }
        return 0;
    }

    @Override
    public final AxisAlignedBB
    getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return ChromaOptions.PANELLAMPCOLLISION.getState()
            ? null
            : super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public final void setBlockBoundsForItemRender() {
        this.setBounds(null);
    }

    @Override
    public final void setBlockBoundsBasedOnState(IBlockAccess iba, int x, int y, int z) {
        TileEntity te = iba.getTileEntity(x, y, z);
        if (te instanceof TileEntityRangedLamp)
            this.setBounds(((TileEntityRangedLamp) te).panel);
        else
            this.setBounds(null);
    }

    private void setBounds(ForgeDirection dir) {
        if (dir == null) {
            this.setBlockBounds(0, 0, 0, 1, 1, 1);
            return;
        }
        float t = 0.125F;
        float o = 0.0625F;
        float x1 = 0;
        float y1 = 0;
        float z1 = 0;
        float x2 = 1;
        float y2 = 1;
        float z2 = 1;
        switch (dir) {
            case DOWN:
                y2 = t;
                break;
            case UP:
                y1 = 1 - t;
                break;
            case WEST:
                x2 = t;
                break;
            case EAST:
                x1 = 1 - t;
                break;
            case NORTH:
                z2 = t;
                break;
            case SOUTH:
                z1 = 1 - t;
                break;
            default:
                break;
        }
        if (dir.offsetX == 0) {
            x1 += o;
            x2 -= o;
        }
        if (dir.offsetY == 0) {
            y1 += o;
            y2 -= o;
        }
        if (dir.offsetZ == 0) {
            z1 += o;
            z2 -= o;
        }
        this.setBlockBounds(x1, y1, z1, x2, y2, z2);
    }

    @Override
    public boolean hasTileEntity(int meta) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileEntityRangedLamp();
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public boolean canRenderInPass(int pass) {
        ChromaISBRH.lamp.setRenderPass(pass);
        return pass <= 1;
    }

    @Override
    public int getRenderType() {
        return ChromaISBRH.lamp.getRenderID();
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        TileEntityRangedLamp te = new TileEntityRangedLamp();
        world.setTileEntity(x, y, z, te);
        TileEntityLampController.addLight(te);
        te.setLit(TileEntityLampController.activeSourceInRange(te));
    }

    @Override
    public boolean onBlockActivated(
        World world,
        int x,
        int y,
        int z,
        EntityPlayer ep,
        int s,
        float a,
        float b,
        float c
    ) {
        TileEntityRangedLamp te = (TileEntityRangedLamp) world.getTileEntity(x, y, z);
        ItemStack is = ep.getCurrentEquippedItem();
        if (is != null) {
            int dmg = -1;
            if (ReikaDyeHelper.isDyeItem(is)) {
                dmg = ReikaDyeHelper.getColorFromItem(is).ordinal();
            } else if (ChromaItems.SHARD.matchWith(is)) {
                dmg = is.getItemDamage() % 16;
            }
            if (dmg >= 0 && dmg != world.getBlockMetadata(x, y, z)) {
                world.setBlockMetadataWithNotify(x, y, z, dmg, 3);
                return true;
            }
        }
        ep.openGui(ChromatiCraft.instance, ChromaGuis.TILE.ordinal(), world, x, y, z);
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block old, int oldm) {
        TileEntityRangedLamp te = (TileEntityRangedLamp) world.getTileEntity(x, y, z);
        TileEntityLampController.removeLight(te);

        super.breakBlock(world, x, y, z, old, oldm);
    }

    @Override
    public int getRenderColor(int meta) {
        return ReikaColorAPI.getModifiedSat(
            CrystalElement.elements[meta % 16].getColor(), 0.7F
        );
    }

    @Override
    public int colorMultiplier(IBlockAccess iba, int x, int y, int z) {
        return this.getRenderColor(iba.getBlockMetadata(x, y, z));
    }

    @Override
    public void registerBlockIcons(IIconRegister ico) {
        blockIcon = ico.registerIcon("chromaticraft:lamp");
        dark = ico.registerIcon("chromaticraft:lamp_off");
        light = blockIcon;
    }

    @Override
    public IIcon getIcon(IBlockAccess iba, int x, int y, int z, int s) {
        return this.getLightValue(iba, x, y, z) > 0 ? light : dark;
    }

    public static class TileEntityRangedLamp extends TileEntity implements GuiController {
        private boolean lit;
        private boolean inverted;
        private int channel;

        private ForgeDirection panel;

        public boolean isPanel() {
            return panel != null;
        }

        public ForgeDirection getPanelSide() {
            return panel;
        }

        public boolean isLit() {
            return lit;
        }

        public int getChannel() {
            return channel;
        }

        public void setChannel(int ch) {
            TileEntityLampController.removeLight(this);
            channel = ch;
            TileEntityLampController.addLight(this);
            this.setLit(TileEntityLampController.activeSourceInRange(this));
        }

        public void setPanel(ForgeDirection side) {
            panel = side;
        }

        public void invert() {
            inverted = !inverted;
            this.setLit(lit);
        }

        public void setLit(boolean on) {
            lit = on != inverted;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }

        @Override
        public boolean canUpdate() {
            return false;
        }

        @Override
        public void writeToNBT(NBTTagCompound NBT) {
            super.writeToNBT(NBT);

            NBT.setBoolean("on", lit);
            NBT.setInteger("ch", channel);
            if (this.isPanel())
                NBT.setInteger("side", panel.ordinal());
        }

        @Override
        public void readFromNBT(NBTTagCompound NBT) {
            super.readFromNBT(NBT);

            lit = NBT.getBoolean("on");
            channel = NBT.getInteger("ch");
            if (NBT.hasKey("side"))
                panel = ForgeDirection.VALID_DIRECTIONS[NBT.getInteger("side")];
        }

        @Override
        public Packet getDescriptionPacket() {
            NBTTagCompound NBT = new NBTTagCompound();
            this.writeToNBT(NBT);
            S35PacketUpdateTileEntity pack
                = new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, NBT);
            return pack;
        }

        @Override
        public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity p) {
            this.readFromNBT(p.field_148860_e);
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            worldObj.func_147451_t(xCoord, yCoord, zCoord);
        }
    }
}
