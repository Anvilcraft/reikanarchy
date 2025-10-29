package Reika.ChromatiCraft.API.Interfaces;

import Reika.ChromatiCraft.API.AdjacencyUpgradeAPI;
import net.minecraft.world.World;

/**
 * You can fetch one of these via {@link AdjacencyUpgradeAPI} createCheckHandler() to
 * query the adjacency core tiers next to a given position.
 */
public interface AdjacencyCheckHandler {
    //public Map<CrystalElementProxy, Integer> getAdjacentUpgrades(World world, int x, int
    //y, int z);

    /** Returns 1 though 8 for adjacent upgrades tiers 0-7 and 0 for none. */
    public int getAdjacentUpgradeTier(World world, int x, int y, int z);

    public double getFactorSimple(World world, int x, int y, int z);

    //public double getFactorSimple(World world, int x, int y, int z, String color);
}
