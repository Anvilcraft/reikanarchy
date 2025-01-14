package Reika.Satisforestry.Biome.Generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import Reika.DragonAPI.Instantiable.Data.Immutable.Coordinate;
import Reika.DragonAPI.Libraries.Java.ReikaRandomHelper;
import Reika.DragonAPI.Libraries.World.ReikaBlockHelper;
import Reika.DragonAPI.Libraries.World.ReikaWorldHelper;
import Reika.Satisforestry.Biome.DecoratorPinkForest;
import Reika.Satisforestry.Blocks.BlockFrackingAux.TileFrackingAux;
import Reika.Satisforestry.Blocks.BlockFrackingNode.TileFrackingNode;
import Reika.Satisforestry.Blocks.BlockTerrain.TerrainType;
import Reika.Satisforestry.Config.BiomeConfig;
import Reika.Satisforestry.Config.NodeResource.Purity;
import Reika.Satisforestry.Config.ResourceFluid;
import Reika.Satisforestry.Miner.FrackerStructure;
import Reika.Satisforestry.Registry.SFBlocks;
import Reika.Satisforestry.Satisforestry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenFrackingNode extends WorldGenerator {
    private final boolean forceGen;

    public WorldGenFrackingNode(boolean force) {
        forceGen = force;
    }

    @Override
    public boolean generate(World world, Random rand, int x, int yUnused, int z) {
        if (BiomeConfig.instance.getFluidDrops().isEmpty())
            return false;
        int size = ReikaWorldHelper.getBiomeSize(world) - 4;
        if (size > 0
            && rand.nextInt(1 + size * 2)
                > 1) //67% rate on size 5 and 40% rate on size 6 (large biomes)
            return false;
        if (!forceGen && !Satisforestry.isPinkForest(world, x, z))
            return false;
        for (int i = 0; i < 6; i++) {
            int dx = ReikaRandomHelper.getRandomPlusMinus(x, 5, rand);
            int dz = ReikaRandomHelper.getRandomPlusMinus(z, 5, rand);
            if (forceGen || Satisforestry.isPinkForest(world, dx, dz)) {
                //int dy = DecoratorPinkForest.getTrueTopAt(world, dx, dz);

                if (this.tryPlace(world, dx, dz, rand))
                    return true;
            }
        }
        return false;
    }

    private boolean tryPlace(World world, int x, int z, Random rand) {
        int a = 11;
        int minY = 999;
        int maxY = 0;
        for (int i = -a; i <= a; i++) {
            for (int k = -a; k <= a; k++) {
                int dx = x + i;
                int dz = z + k;
                int y = DecoratorPinkForest.getTrueTopAt(world, dx, dz);
                minY = Math.min(minY, y);
                maxY = Math.max(maxY, y);
                if (!this.isValidGroundBlock(world, dx, y, dz))
                    return false;
                if (Math.abs(a) <= 4) {
                    for (int h = 1; h <= 9; h++) {
                        int dy = y + h;
                        if (!this.isValidAirBlock(world, dx, dy, dz))
                            return false;
                    }
                }
            }
        }
        if (maxY - minY > 2 || minY < 90)
            return false;
        ResourceFluid rf = TileFrackingNode.selectResource(rand);
        HashSet<Coordinate> nodes = new HashSet();
        for (double ang = 0; ang < 360; ang += 2.5) {
            double cos = Math.cos(Math.toRadians(ang));
            double sin = Math.sin(Math.toRadians(ang));
            for (double r = 9; r <= 16; r += 0.25) {
                int dx = ReikaRandomHelper.getRandomPlusMinus(
                    MathHelper.floor_double(x + r * cos), 2, rand
                );
                int dz = ReikaRandomHelper.getRandomPlusMinus(
                    MathHelper.floor_double(z + r * sin), 2, rand
                );
                int y = DecoratorPinkForest.getTrueTopAt(world, dx, dz);
                if (y >= minY
                    && !FrackerStructure.isUnderFrackingFootprint(
                        world, dx - x, dz - z
                    )) {
                    if (this.isValidAirBlock(world, dx, y + 1, dz)
                        && this.isValidGroundBlock(world, dx, y, dz)
                        && this.isValidGroundBlock(world, dx, y - 1, dz)) {
                        nodes.add(new Coordinate(dx, (maxY + minY) / 2, dz));
                        break;
                    }
                }
            }
        }
        if (nodes.size() < 4)
            return false;
        ArrayList<Coordinate> take = new ArrayList(nodes);
        HashSet<Coordinate> spawned = new HashSet();
        int y = Math.max(minY, (maxY + minY) / 2 - 1);
        this.clearArea(world, x, y, z, 4, null);
        world.setBlock(x, y, z, SFBlocks.FRACKNODE.getBlockInstance(), 0, 2);
        TileFrackingNode te = (TileFrackingNode) world.getTileEntity(x, y, z);
        Coordinate root = new Coordinate(te);
        te.generate(rf, rand);
        Purity p = te.getPurity();
        while (spawned.size() < rf.maxNodes && !take.isEmpty()) {
            Coordinate c = take.remove(rand.nextInt(take.size())).setY(y);
            boolean close = false;
            for (Coordinate c2 : spawned) {
                if (c2.getTaxicabDistanceTo(c) <= 5
                    || Math.abs(Math.toDegrees(
                           Math.atan2(c2.zCoord - c.zCoord, c2.xCoord - c.xCoord)
                       ))
                        <= 5) {
                    close = true;
                    break;
                }
            }
            if (close)
                continue;
            for (double d = 0; d <= 1; d += 0.2) {
                this.clearArea(
                    world,
                    MathHelper.floor_double(x + (c.xCoord - x) * d),
                    y,
                    MathHelper.floor_double(z + (c.zCoord - z) * d),
                    2,
                    root
                );
            }
            Purity p2 = TileFrackingNode.getRelativePurity(p, rand);
            c.setBlock(world, SFBlocks.FRACKNODEAUX.getBlockInstance(), p2.ordinal(), 2);
            TileFrackingAux te2 = (TileFrackingAux) c.getTileEntity(world);
            te2.linkTo(root);
            spawned.add(c);
        }
        return true;
    }

    private void clearArea(World world, int x, int y, int z, double r, Coordinate root) {
        for (int i = -(int) r; i <= r; i++) {
            for (int k = -(int) r; k <= r; k++) {
                if (i * i + k * k <= r * r + 0.5
                    && (root == null || x != root.xCoord || z != root.zCoord)) {
                    for (int dy = y - 2; dy <= y; dy++) {
                        Block b = world.getBlock(x + i, dy, z + k);
                        if (b != SFBlocks.FRACKNODE.getBlockInstance()
                            && b != SFBlocks.FRACKNODEAUX.getBlockInstance()
                            && b != SFBlocks.CAVESHIELD.getBlockInstance())
                            world.setBlock(
                                x + i,
                                dy,
                                z + k,
                                y == dy ? SFBlocks.CAVESHIELD.getBlockInstance()
                                        : Blocks.dirt,
                                0,
                                2
                            );
                    }
                    for (int dy = y + 1; dy <= y + 4; dy++) {
                        world.setBlock(x + i, dy, z + k, Blocks.air, 0, 2);
                    }
                    for (int dy = y + 5; dy <= y + 9; dy++) {
                        Block b = world.getBlock(x + i, dy, z + k);
                        if (b == SFBlocks.BAMBOO.getBlockInstance()
                            || b.isLeaves(world, x + i, dy, z + k)
                            || b.isWood(world, x + i, dy, z + k))
                            world.setBlock(x + i, dy, z + k, Blocks.air, 0, 2);
                    }
                }
            }
        }
    }

    private boolean isValidAirBlock(World world, int x, int y, int z) {
        Block b = world.getBlock(x, y, z);
        if (ReikaBlockHelper.isLiquid(b))
            return false;
        return ReikaWorldHelper.softBlocks(world, x, y, z)
            || b == SFBlocks.BAMBOO.getBlockInstance();
    }

    private boolean isValidGroundBlock(World world, int x, int y, int z) {
        Block b = world.getBlock(x, y, z);
        return b == Blocks.grass || b == Blocks.dirt || b == Blocks.sand
            || b == Blocks.stone
            || (b == SFBlocks.TERRAIN.getBlockInstance()
                && world.getBlockMetadata(x, y, z) == TerrainType.OUTCROP.ordinal());
    }
}