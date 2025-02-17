/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.MeteorCraft.Blocks;

import Reika.DragonAPI.Libraries.IO.ReikaSoundHelper;
import Reika.DragonAPI.Libraries.MathSci.ReikaMathLibrary;
import Reika.DragonAPI.Libraries.Registry.ReikaParticleHelper;
import Reika.MeteorCraft.API.Event.MeteorCraftEvent;
import Reika.MeteorCraft.API.Event.MeteorCraftEvent.EntryEvent;
import Reika.MeteorCraft.API.Event.MeteorCraftEvent.MeteorDefenceEvent;
import Reika.MeteorCraft.Entity.EntityMeteor;
import Reika.MeteorCraft.Registry.MeteorOptions;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class TileEntityMeteorGun extends TileEntityMeteorBase {
    private int soundTimer = 0;

    public int getProtectionRange() {
        return 16 * (this.getTier() * 2 + 3);
    }

    public int getTier() {
        return worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
    }

    @Override
    public long getMinPower() {
        return calcMinPower(this.getTier());
    }

    public static long calcMinPower(int tier) {
        return (1 + tier) * 524288;
    }

    @Override
    public void onEvent(MeteorCraftEvent e) {
        if (e instanceof EntryEvent) {
            EntityMeteor m = e.meteor;
            double dd = ReikaMathLibrary.py3d(e.x - xCoord, 0, e.z - zCoord);
            if (this.canPerformActions() && dd <= this.getProtectionRange()) {
                this.killMeteor(m);
            }
        }
    }

    private void killMeteor(EntityMeteor m) {
        m.setPosition(xCoord + 0.5, m.posY, zCoord + 0.5);
        if (MeteorOptions.NOGUNBURST.getState())
            m.setDead();
        else
            m.destroy();
        if (worldObj.isRemote) {
            EntityTNTPrimed tnt = new EntityTNTPrimed(
                worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, null
            );
            tnt.motionY = 5;
            tnt.fuse = 40;
            worldObj.spawnEntityInWorld(tnt);
        }
        ReikaSoundHelper.playSoundAtBlock(
            worldObj, xCoord, yCoord, zCoord, "random.explode"
        );
        ReikaParticleHelper.EXPLODE.spawnAroundBlock(worldObj, xCoord, yCoord, zCoord, 1);

        if (this.getSide() == Side.CLIENT) {
            this.playMCPlayerSound("ambient.weather.thunder", 2, 2);
            for (float i = 0; i <= 2; i += 0.5F) {
                this.playMCPlayerSound("meteorcraft:boom", 2, i);
            }
            soundTimer = 10;
        }
        MinecraftForge.EVENT_BUS.post(new MeteorDefenceEvent(this, m));
    }

    @SideOnly(Side.CLIENT)
    private void playMCPlayerSound(String s, float v, float p) {
        Minecraft.getMinecraft().thePlayer.playSound(s, v, p);
    }

    @SideOnly(Side.CLIENT)
    private void playMCPlayerSound(String s) {
        this.playMCPlayerSound(s, 1, 1);
    }

    @Override
    public void updateEntity(World world, int x, int y, int z, int meta) {
        if (soundTimer == 1) {
            if (this.getSide() == Side.CLIENT) {
                this.playMCPlayerSound("meteorcraft:boom", 2, 2);
                for (int k = 0; k < 2; k++) {
                    this.playMCPlayerSound("ambient.weather.thunder", 2, 0.25F);
                    this.playMCPlayerSound("ambient.weather.thunder", 2, 1F);
                    this.playMCPlayerSound("ambient.weather.thunder", 2, 2F);

                    this.playMCPlayerSound("ambient.weather.thunder", 2, 0.25F);
                    this.playMCPlayerSound("ambient.weather.thunder", 2, 1F);
                    this.playMCPlayerSound("ambient.weather.thunder", 2, 2F);
                }
            }
        }
        if (soundTimer > 0)
            soundTimer--;
    }

    @Override
    protected String getTEName() {
        return "Meteor Defence Gun";
    }
}
