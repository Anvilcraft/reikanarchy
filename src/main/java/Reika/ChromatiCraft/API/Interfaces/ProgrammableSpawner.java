/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.API.Interfaces;

import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.MobSpawnerBaseLogic;

/**
 * Implement this if you want the spawner controller to be able to reprogram your spawner
 * item.
 */
public interface ProgrammableSpawner {
    /** The entity class that the spawner corresponds to. Args: Spawner itemstack */
    public Class<? extends EntityLiving> getSpawnerEntity(ItemStack is);

    /** Actually sets the spawner's entity type. Args: Spawner itemstack, entity class */
    public void setSpawnerType(ItemStack is, Class<? extends EntityLiving> cl);

    /** Actually sets the spawner's data. See {@link MobSpawnerBaseLogic}. */
    public void setSpawnerData(
        ItemStack is,
        int minDelay,
        int maxDelay,
        int maxNear,
        int spawnCount,
        int spawnRange,
        int activeRange
    );
}
