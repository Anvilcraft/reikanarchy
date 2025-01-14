/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.Items.Placers;

import java.util.List;

import Reika.DragonAPI.Libraries.World.ReikaWorldHelper;
import Reika.RotaryCraft.Auxiliary.RotaryAux;
import Reika.RotaryCraft.Base.ItemBlockPlacer;
import Reika.RotaryCraft.Registry.Flywheels;
import Reika.RotaryCraft.Registry.ItemRegistry;
import Reika.RotaryCraft.Registry.MachineRegistry;
import Reika.RotaryCraft.RotaryCraft;
import Reika.RotaryCraft.TileEntities.Transmission.TileEntityFlywheel;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

public class ItemFlywheelPlacer extends ItemBlockPlacer {
    public ItemFlywheelPlacer() {
        super();
        this.setCreativeTab(RotaryCraft.tabPower);
    }

    @Override
    public boolean onItemUse(
        ItemStack is,
        EntityPlayer ep,
        World world,
        int x,
        int y,
        int z,
        int side,
        float par8,
        float par9,
        float par10
    ) {
        if (!ReikaWorldHelper.softBlocks(world, x, y, z)
            && ReikaWorldHelper.getMaterial(world, x, y, z) != Material.water
            && ReikaWorldHelper.getMaterial(world, x, y, z) != Material.lava) {
            if (side == 0)
                --y;
            if (side == 1)
                ++y;
            if (side == 2)
                --z;
            if (side == 3)
                ++z;
            if (side == 4)
                --x;
            if (side == 5)
                ++x;
            if (!ReikaWorldHelper.softBlocks(world, x, y, z)
                && ReikaWorldHelper.getMaterial(world, x, y, z) != Material.water
                && ReikaWorldHelper.getMaterial(world, x, y, z) != Material.lava)
                return false;
        }
        this.clearBlocks(world, x, y, z);
        if (!this.checkValidBounds(is, ep, world, x, y, z))
            return false;
        AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1);
        List inblock = world.getEntitiesWithinAABB(EntityLivingBase.class, box);
        if (inblock.size() > 0)
            return false;
        if (!ep.canPlayerEdit(x, y, z, 0, is))
            return false;
        else {
            if (!ep.capabilities.isCreativeMode)
                --is.stackSize;
            world.setBlock(x, y, z, MachineRegistry.FLYWHEEL.getBlock());
        }
        world.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, "step.stone", 1F, 1.5F);
        TileEntityFlywheel fly = (TileEntityFlywheel) world.getTileEntity(x, y, z);
        fly.setBlockMetadata(RotaryAux.get4SidedMetadataFromPlayerLook(ep));
        int meta = fly.getBlockMetadata();
        if (meta % 2 == 0)
            fly.setBlockMetadata(meta + 1);
        else
            fly.setBlockMetadata(meta - 1);
        fly.setPlacer(ep);
        fly.setMaterialFromItem(is);
        if (RotaryAux.shouldSetFlipped(world, x, y, z)) {
            fly.isFlipped = true;
        }
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs tab, List list) {
        if (MachineRegistry.FLYWHEEL.isAvailableInCreativeInventory()) {
            for (int i = 0; i < Flywheels.list.length; i++) {
                ItemStack item = Flywheels.list[i].getFlywheelItem();
                list.add(item);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack is, EntityPlayer ep, List li, boolean verbose) {
        int i = is.getItemDamage();
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            Flywheels f = Flywheels.list[i];
            if (f.maxSpeed < Integer.MAX_VALUE)
                li.add(String.format("Max Speed: %d rad/s", f.maxSpeed));
            li.add(String.format("Required Torque: %d Nm", f.getMinTorque()));
            if (f.maxTorque < Integer.MAX_VALUE)
                li.add(String.format("Max Torque: %d Nm", f.maxTorque));
            li.add(String.format("Decay Rate: 1/%d", f.decayTime));
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Hold ");
            sb.append(EnumChatFormatting.GREEN.toString());
            sb.append("Shift");
            sb.append(EnumChatFormatting.GRAY.toString());
            sb.append(" for load data");
            li.add(sb.toString());
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack is) {
        return ItemRegistry.getEntry(is).getMultiValuedName(is.getItemDamage());
    }
}
