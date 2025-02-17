/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.TileEntities.Weaponry;

import Reika.DragonAPI.Libraries.ReikaPlayerAPI;
import Reika.DragonAPI.Libraries.World.ReikaWorldHelper;
import Reika.RotaryCraft.Base.TileEntity.TileEntityPowerReceiver;
import Reika.RotaryCraft.Registry.MachineRegistry;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class TileEntitySelfDestruct extends TileEntityPowerReceiver {
    private boolean lastHasPower;

    @Override
    protected void animateWithTick(World world, int x, int y, int z) {}

    @Override
    public MachineRegistry getTile() {
        return MachineRegistry.SELFDESTRUCT;
    }

    @Override
    public void updateEntity(World world, int x, int y, int z, int meta) {
        super.updateTileEntity();
        this.getSummativeSidedPower();
        boolean hasPower = power > 0;
        if (lastHasPower && !hasPower)
            this.destroy(world, x, y, z);
        else
            lastHasPower = hasPower;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    public void destroy(World world, int x, int y, int z) {
        if (!world.isRemote) {
            tickcount++;
            int n = 6;
            int count = 32;
            double rx = x + 0.5 + rand.nextInt(2 * n + 1) - n;
            double ry = y + 0.5 + rand.nextInt(2 * n + 1) - n;
            double rz = z + 0.5 + rand.nextInt(2 * n + 1) - n;
            int irx = MathHelper.floor_double(rx);
            int iry = MathHelper.floor_double(ry);
            int irz = MathHelper.floor_double(rz);
            if (ReikaPlayerAPI.playerCanBreakAt(
                    (WorldServer) worldObj, irx, iry, irz, this.getServerPlacer()
                ))
                world.createExplosion(null, rx, ry, rz, 3F, true);
            for (int i = 0; i < 32; i++)
                world.spawnParticle(
                    "lava",
                    rx + rand.nextInt(7) - 3,
                    ry + rand.nextInt(7) - 3,
                    rz + rand.nextInt(7) - 3,
                    0,
                    0,
                    0
                );
            if (tickcount > count) {
                world.newExplosion(null, x + 0.5, y + 0.5, z + 0.5, 12F, true, true);
                ReikaWorldHelper.temperatureEnvironment(world, x, y, z, 1000);
            } /*
         EntityCreeper e = new EntityCreeper(world);
         e.posX = rx;
         e.posZ = rz;
         e.posY = world.getTopSolidOrLiquidBlock((int)rx, (int)rz)+1;
         e.addPotionEffect(new PotionEffect(Potion.resistance.id, 10, 10));
         world.spawnEntityInWorld(e);*/
            MachineRegistry m = this.getTile();
            MachineRegistry m2 = MachineRegistry.getMachine(world, x, y, z);
            if (m != m2 && tickcount <= count) {
                world.setBlock(x, y, z, m.getBlock(), m.getBlockMetadata(), 3);
                TileEntitySelfDestruct te
                    = (TileEntitySelfDestruct) world.getTileEntity(x, y, z);
                te.lastHasPower = true;
                te.tickcount = tickcount;
            }
        }
    }
}
