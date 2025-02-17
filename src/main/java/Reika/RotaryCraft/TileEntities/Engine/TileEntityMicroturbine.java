/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.TileEntities.Engine;

import Reika.RotaryCraft.Base.TileEntity.TileEntityEngine;
import Reika.RotaryCraft.Registry.SoundRegistry;
import net.minecraft.world.World;

public class TileEntityMicroturbine extends TileEntityEngine {
    @Override
    protected void consumeFuel(float scale) {
        fuel.removeLiquid(scale * this.getConsumedFuel());
    }

    @Override
    protected void internalizeFuel() {}

    @Override
    protected boolean getRequirements(World world, int x, int y, int z, int meta) {
        return this.getFuelLevel() > 0;
    }

    @Override
    public int getFuelLevel() {
        return fuel.getLevel();
    }

    @Override
    protected void
    playSounds(World world, int x, int y, int z, float pitchMultiplier, float volume) {
        soundtick++;
        if (this.isMuffled(world, x, y, z)) {
            volume *= 0.3125F;
        }

        if (soundtick < this.getSoundLength(1F / pitchMultiplier) && soundtick < 2000)
            return;
        soundtick = 0;

        float pitch = 1F;
        volume *= 0.125F;
        SoundRegistry.MICRO.playSoundAtBlock(
            world, x, y, z, volume, pitch * pitchMultiplier
        );
    }

    @Override
    protected void affectSurroundings(World world, int x, int y, int z, int meta) {}
}
