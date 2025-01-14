/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Auxiliary.Ability;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import Reika.ChromatiCraft.API.AbilityAPI.Ability;
import Reika.ChromatiCraft.Magic.ElementBufferCapacityBoost;
import Reika.ChromatiCraft.Magic.ElementTagCompound;
import Reika.ChromatiCraft.Magic.PlayerElementBuffer;
import Reika.ChromatiCraft.Registry.Chromabilities;
import Reika.ChromatiCraft.Registry.CrystalElement;
import Reika.DragonAPI.Libraries.Java.ReikaRandomHelper;
import Reika.DragonAPI.Libraries.ReikaPlayerAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

class LossCache {
    private static final Random rand = new Random();

    private final Collection<Ability> savedAbilities = new ArrayList();
    private final Collection<ElementBufferCapacityBoost> savedBoosts = new ArrayList();
    private final ElementTagCompound savedEnergy = new ElementTagCompound();
    private int cap;
    //private long time;

    private static final double INIT_CHANCE = 10;
    private static final double INC_CHANCE = 5;
    private static final double MAX_CHANCE = 80;
    private static final int DEATH_COOLDOWN = 20 * 60 * 5;

    static final String NBT_TAG = "bufferDeathChance";

    LossCache(EntityPlayer ep) {
        NBTTagCompound nbt = ReikaPlayerAPI.getDeathPersistentNBT(ep);
        NBTTagCompound tag = nbt.getCompoundTag(NBT_TAG);
        double chance = tag.getDouble("chance");
        long time = ep.worldObj.getTotalWorldTime();
        if (!nbt.hasKey(NBT_TAG) || !tag.hasKey("chance")) {
            chance = INIT_CHANCE;
        } else {
            long prev = tag.getLong("time");
            if (time > prev + DEATH_COOLDOWN) {
                chance += INC_CHANCE;
                if (chance > MAX_CHANCE)
                    chance = MAX_CHANCE;
            }
        }
        tag.setDouble("chance", chance);
        tag.setLong("time", time);
        nbt.setTag(NBT_TAG, tag);
        for (Ability a : Chromabilities.getAvailableFrom(ep)) {
            if (a == Chromabilities.DEATHPROOF
                || ReikaRandomHelper.doWithChance(chance)) {
                savedAbilities.add(a);
            }
        }
        for (ElementBufferCapacityBoost b : ElementBufferCapacityBoost.list) {
            if (b.playerHas(ep)) {
                savedBoosts.add(b);
            }
        }
        for (int i = 0; i < CrystalElement.elements.length; i++) {
            CrystalElement e = CrystalElement.elements[i];
            int max = PlayerElementBuffer.instance.getPlayerContent(ep, e);
            if (max > 0) {
                double ratio = 1D / (2 + rand.nextInt(3));
                double nrat = 1D - ratio;
                int random = (int) (max * nrat);
                int save = (int) (max * ratio) + rand.nextInt(random);
                save *= chance / 100D;
                savedEnergy.addTag(e, save);
            }
        }

        //int pcap = PlayerElementBuffer.instance.getElementCap(ep);
        //cap = Math.max(savedEnergy.getMaximumValue(), Math.max(24,
        //ReikaRandomHelper.doWithChance(chance) ? pcap : pcap/4));
        cap = 24;
        int max = savedEnergy.getMaximumValue();
        while (cap < max)
            cap *= 2;
    }

    void applyToPlayer(EntityPlayer player) {
        PlayerElementBuffer.instance.setElementCap(player, cap, false);
        for (CrystalElement e : savedEnergy.elementSet()) {
            PlayerElementBuffer.instance.addToPlayer(
                player, e, savedEnergy.getValue(e), false
            );
        }
        for (Ability a : savedAbilities) {
            Chromabilities.give(player, a);
        }
        for (ElementBufferCapacityBoost b : savedBoosts) {
            b.give(player);
        }
    }
}
