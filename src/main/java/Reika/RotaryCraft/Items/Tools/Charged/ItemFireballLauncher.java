/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.Items.Tools.Charged;

import Reika.ChromatiCraft.API.Interfaces.ProjectileFiringTool;
import Reika.DragonAPI.Instantiable.Data.Immutable.DecimalPosition;
import Reika.DragonAPI.Libraries.Java.ReikaRandomHelper;
import Reika.DragonAPI.Libraries.MathSci.ReikaVectorHelper;
import Reika.DragonAPI.Libraries.ReikaInventoryHelper;
import Reika.DragonAPI.Libraries.ReikaPlayerAPI;
import Reika.RotaryCraft.Base.ItemChargedTool;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemFireballLauncher
    extends ItemChargedTool implements ProjectileFiringTool {
    /** Sprite index */
    private int texture;
    private int defaulttex;

    public ItemFireballLauncher(int tex) {
        super(tex);
        texture = tex;
        defaulttex = texture;
    }

    public void fire(ItemStack is, World world, EntityPlayer ep, boolean randomVec) {
        this.fire(is, world, ep, 0, randomVec);
    }

    public void
    fire(ItemStack is, World world, EntityPlayer ep, int charge, boolean randomVec) {
        DecimalPosition look = ReikaVectorHelper.getPlayerLookCoords(ep, 2);
        EntityLargeFireball ef = new EntityLargeFireball(
            world, ep, look.xCoord, look.yCoord + 1, look.zCoord
        );
        Vec3 lookv = ep.getLookVec();
        if (randomVec) {
            lookv.xCoord = ReikaRandomHelper.getRandomPlusMinus(0, 1D);
            lookv.yCoord = ReikaRandomHelper.getRandomPlusMinus(0, 1D);
            lookv.zCoord = ReikaRandomHelper.getRandomPlusMinus(0, 1D);
            lookv = lookv.normalize();
        }
        ef.motionX = lookv.xCoord / 5;
        ef.motionY = lookv.yCoord / 5;
        ef.motionZ = lookv.zCoord / 5;
        ef.accelerationX = ef.motionX;
        ef.accelerationY = ef.motionY;
        ef.accelerationZ = ef.motionZ;
        ef.field_92057_e = (charge);
        ef.posY = ep.posY + 1;
        if (!world.isRemote) {
            world.playSoundAtEntity(ep, "mob.ghast.fireball", 1, 1);
            world.spawnEntityInWorld(ef);
        }
        if (!ep.capabilities.isCreativeMode && par5Random.nextInt(3) == 0)
            ReikaInventoryHelper.findAndDecrStack(
                Items.fire_charge, -1, ep.inventory.mainInventory
            );
        int decr = (int) (charge / 2F);
        if (decr <= 0)
            decr = 1;
        ep.setCurrentItemOrArmor(
            0, new ItemStack(is.getItem(), is.stackSize, is.getItemDamage() - decr)
        );
    }

    @Override
    public void
    onPlayerStoppedUsing(ItemStack is, World world, EntityPlayer ep, int ticksLeft) {
        texture = defaulttex;
        float power = (is.getMaxItemUseDuration() - ticksLeft) / 20F;
        int charge = 0;
        if (ep.capabilities.isCreativeMode) {
            power *= 2;
            if (ep.isSneaking())
                power *= 2;
        }
        if (power < 0.1F) {
            charge = 0;
        } else if (power < 0.25F) {
            charge = 1;
        } else if (power < 0.5F) {
            charge = 2;
        } else if (power < 1F) {
            charge = 3;
        } else if (power < 2F) {
            charge = 4;
        } else if (power < 3F) {
            charge = 5;
        } else if (power < 5F) {
            charge = 6;
        } else if (power < 8F) {
            charge = 7;
        } else {
            charge = 8;
        }
        //ReikaChatHelper.write(power+"  ->  "+charge);
        this.fire(is, world, ep, charge, false);
    }

    @Override
    public ItemStack onEaten(ItemStack is, World world, EntityPlayer ep) {
        return is;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack is) {
        return 72000;
    }

    /**
     * returns the action that specifies what animation to play when the items is being
     * used
     */
    @Override
    public EnumAction getItemUseAction(ItemStack is) {
        return EnumAction.bow;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args:
     * itemStack, world, entityPlayer
     */
    @Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer ep) {
        if (is.getItemDamage() <= 0) {
            this.noCharge();
            return is;
        }
        this.warnCharge(is);
        if (!ReikaPlayerAPI.playerHasOrIsCreative(ep, Items.fire_charge))
            return is;
        ep.setItemInUse(is, this.getMaxItemUseDuration(is));
        return is;
    }

    @Override
    public int getItemSpriteIndex(ItemStack item) {
        //ReikaJavaLibrary.pConsole(this.texture-this.defaulttex);
        return texture;
    }

    @Override
    public void onUsingTick(ItemStack is, EntityPlayer ep, int count) {
        float power = (is.getMaxItemUseDuration() - count) / 20F;
        if (ep.capabilities.isCreativeMode) {
            power *= 2;
            if (ep.isSneaking())
                power *= 2;
        }
        if (power < 0.1F) {
            texture = defaulttex;
        } else if (power < 0.25F) {
            texture = defaulttex + 1;
        } else if (power < 0.5F) {
            texture = defaulttex + 2;
        } else if (power < 1F) {
            texture = defaulttex + 3;
        } else if (power < 2F) {
            texture = defaulttex + 4;
        } else if (power < 3F) {
            texture = defaulttex + 5;
        } else if (power < 5F) {
            texture = defaulttex + 6;
        } else if (power < 8F) {
            texture = defaulttex + 7;
        } else {
            texture = defaulttex + 8;
        }
    }

    @Override
    public int getAutofireRate() {
        return 50;
    }
}
