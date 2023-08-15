package Reika.ChromatiCraft.Auxiliary.Structure;

import Reika.ChromatiCraft.Base.ChromaStructureBase;
import Reika.ChromatiCraft.Registry.ChromaBlocks;
import Reika.DragonAPI.Instantiable.Data.BlockStruct.FilledBlockArray;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class PortalStructure extends ChromaStructureBase {
    @Override
    public FilledBlockArray getArray(World world, int x, int y, int z) {
        FilledBlockArray array = new FilledBlockArray(world);

        int i = x - 7;
        int j = y + 0;
        int k = z - 7;

        Block ch = ChromaBlocks.LUMA.getBlockInstance();
        Block p = ChromaBlocks.PORTAL.getBlockInstance();
        //Block er =
        //FluidRegistry.getFluid("ender").getBlock();//ChromaBlocks.ENDER.getBlockInstance();
        Fluid er = FluidRegistry.getFluid("ender");

        array.setBlock(i + 0, j + 2, k + 1, shield, 0);
        array.setBlock(i + 0, j + 2, k + 2, shield, 0);
        array.setBlock(i + 0, j + 2, k + 3, shield, 0);
        array.setBlock(i + 0, j + 2, k + 11, shield, 0);
        array.setBlock(i + 0, j + 2, k + 12, shield, 0);
        array.setBlock(i + 0, j + 2, k + 13, shield, 0);
        array.setBlock(i + 0, j + 3, k + 0, crystalstone, 0);
        array.setBlock(i + 0, j + 3, k + 1, crystalstone, 0);
        array.setBlock(i + 0, j + 3, k + 2, crystalstone, 0);
        array.setBlock(i + 0, j + 3, k + 3, crystalstone, 0);
        array.setBlock(i + 0, j + 3, k + 4, crystalstone, 0);
        array.setBlock(i + 0, j + 3, k + 5, crystalstone, 15);
        array.setBlock(i + 0, j + 3, k + 6, crystalstone, 15);
        array.setBlock(i + 0, j + 3, k + 7, crystalstone, 15);
        array.setBlock(i + 0, j + 3, k + 8, crystalstone, 15);
        array.setBlock(i + 0, j + 3, k + 9, crystalstone, 15);
        array.setBlock(i + 0, j + 3, k + 10, crystalstone, 0);
        array.setBlock(i + 0, j + 3, k + 11, crystalstone, 0);
        array.setBlock(i + 0, j + 3, k + 12, crystalstone, 0);
        array.setBlock(i + 0, j + 3, k + 13, crystalstone, 0);
        array.setBlock(i + 0, j + 3, k + 14, crystalstone, 0);
        array.setBlock(i + 0, j + 4, k + 0, crystalstone, 2);
        array.setBlock(i + 0, j + 4, k + 4, crystalstone, 2);
        array.setBlock(i + 0, j + 4, k + 10, crystalstone, 2);
        array.setBlock(i + 0, j + 4, k + 14, crystalstone, 2);
        array.setBlock(i + 0, j + 5, k + 0, crystalstone, 3);
        array.setBlock(i + 0, j + 5, k + 4, crystalstone, 13);
        array.setBlock(i + 0, j + 5, k + 10, crystalstone, 13);
        array.setBlock(i + 0, j + 5, k + 14, crystalstone, 3);
        array.setBlock(i + 0, j + 6, k + 0, crystalstone, 2);
        array.setBlock(i + 0, j + 6, k + 4, crystalstone, 2);
        array.setBlock(i + 0, j + 6, k + 10, crystalstone, 2);
        array.setBlock(i + 0, j + 6, k + 14, crystalstone, 2);
        array.setBlock(i + 0, j + 7, k + 0, crystalstone, 3);
        array.setBlock(i + 0, j + 7, k + 4, crystalstone, 7);
        array.setBlock(i + 0, j + 7, k + 5, crystalstone, 1);
        array.setBlock(i + 0, j + 7, k + 6, crystalstone, 1);
        array.setBlock(i + 0, j + 7, k + 7, crystalstone, 1);
        array.setBlock(i + 0, j + 7, k + 8, crystalstone, 1);
        array.setBlock(i + 0, j + 7, k + 9, crystalstone, 1);
        array.setBlock(i + 0, j + 7, k + 10, crystalstone, 7);
        array.setBlock(i + 0, j + 7, k + 14, crystalstone, 3);
        array.setBlock(i + 0, j + 8, k + 0, crystalstone, 2);
        array.setBlock(i + 0, j + 8, k + 14, crystalstone, 2);
        array.setBlock(i + 0, j + 9, k + 0, crystalstone, 5);
        array.setBlock(i + 0, j + 9, k + 14, crystalstone, 5);
        array.setBlock(i + 1, j + 1, k + 1, shield, 0);
        array.setBlock(i + 1, j + 1, k + 2, shield, 0);
        array.setBlock(i + 1, j + 1, k + 3, shield, 0);
        array.setBlock(i + 1, j + 1, k + 11, shield, 0);
        array.setBlock(i + 1, j + 1, k + 12, shield, 0);
        array.setBlock(i + 1, j + 1, k + 13, shield, 0);
        array.setBlock(i + 1, j + 2, k + 0, shield, 0);
        array.setBlock(i + 1, j + 2, k + 1, ch);
        array.setBlock(i + 1, j + 2, k + 2, ch);
        array.setBlock(i + 1, j + 2, k + 3, ch);
        array.setBlock(i + 1, j + 2, k + 4, shield, 0);
        array.setBlock(i + 1, j + 2, k + 6, shield, 0);
        array.setBlock(i + 1, j + 2, k + 7, shield, 0);
        array.setBlock(i + 1, j + 2, k + 8, shield, 0);
        array.setBlock(i + 1, j + 2, k + 10, shield, 0);
        array.setBlock(i + 1, j + 2, k + 11, ch);
        array.setBlock(i + 1, j + 2, k + 12, ch);
        array.setBlock(i + 1, j + 2, k + 13, ch);
        array.setBlock(i + 1, j + 2, k + 14, shield, 0);
        array.setBlock(i + 1, j + 3, k + 0, crystalstone, 0);
        array.setBlock(i + 1, j + 3, k + 2, ch, 1);
        array.setBlock(i + 1, j + 3, k + 4, crystalstone, 15);
        array.setBlock(i + 1, j + 3, k + 5, crystalstone, 12);
        array.setBlock(i + 1, j + 3, k + 6, crystalstone, 12);
        array.setBlock(i + 1, j + 3, k + 7, crystalstone, 12);
        array.setBlock(i + 1, j + 3, k + 8, crystalstone, 12);
        array.setBlock(i + 1, j + 3, k + 9, crystalstone, 12);
        array.setBlock(i + 1, j + 3, k + 10, crystalstone, 15);
        array.setBlock(i + 1, j + 3, k + 12, ch, 1);
        array.setBlock(i + 1, j + 3, k + 14, crystalstone, 0);
        array.setBlock(i + 1, j + 4, k + 2, ch, 1);
        array.setBlock(i + 1, j + 4, k + 12, ch, 1);
        array.setBlock(i + 1, j + 5, k + 2, ch, 1);
        array.setBlock(i + 1, j + 5, k + 12, ch, 1);
        array.setBlock(i + 1, j + 6, k + 2, ch, 1);
        array.setBlock(i + 1, j + 6, k + 12, ch, 1);
        array.setBlock(i + 2, j + 1, k + 1, shield, 0);
        array.setBlock(i + 2, j + 1, k + 2, shield, 0);
        array.setBlock(i + 2, j + 1, k + 3, shield, 0);
        array.setBlock(i + 2, j + 1, k + 6, shield, 0);
        array.setBlock(i + 2, j + 1, k + 7, shield, 0);
        array.setBlock(i + 2, j + 1, k + 8, shield, 0);
        array.setBlock(i + 2, j + 1, k + 11, shield, 0);
        array.setBlock(i + 2, j + 1, k + 12, shield, 0);
        array.setBlock(i + 2, j + 1, k + 13, shield, 0);
        array.setBlock(i + 2, j + 2, k + 0, shield, 0);
        array.setBlock(i + 2, j + 2, k + 1, ch);
        array.setBlock(i + 2, j + 2, k + 2, crystalstone, 0);
        array.setBlock(i + 2, j + 2, k + 3, ch);
        array.setBlock(i + 2, j + 2, k + 4, shield, 0);
        array.setBlock(i + 2, j + 2, k + 5, shield, 0);
        array.setFluid(i + 2, j + 2, k + 6, er);
        array.setFluid(i + 2, j + 2, k + 7, er);
        array.setFluid(i + 2, j + 2, k + 8, er);
        array.setBlock(i + 2, j + 2, k + 9, shield, 0);
        array.setBlock(i + 2, j + 2, k + 10, shield, 0);
        array.setBlock(i + 2, j + 2, k + 11, ch);
        array.setBlock(i + 2, j + 2, k + 12, crystalstone, 0);
        array.setBlock(i + 2, j + 2, k + 13, ch);
        array.setBlock(i + 2, j + 2, k + 14, shield, 0);
        array.setBlock(i + 2, j + 3, k + 0, crystalstone, 0);
        array.setBlock(i + 2, j + 3, k + 1, ch, 1);
        array.setBlock(i + 2, j + 3, k + 2, crystalstone, 0);
        array.setBlock(i + 2, j + 3, k + 3, ch, 1);
        array.setBlock(i + 2, j + 3, k + 4, crystalstone, 15);
        array.setBlock(i + 2, j + 3, k + 5, crystalstone, 12);
        array.setBlock(i + 2, j + 3, k + 9, crystalstone, 12);
        array.setBlock(i + 2, j + 3, k + 10, crystalstone, 15);
        array.setBlock(i + 2, j + 3, k + 11, ch, 1);
        array.setBlock(i + 2, j + 3, k + 12, crystalstone, 0);
        array.setBlock(i + 2, j + 3, k + 13, ch, 1);
        array.setBlock(i + 2, j + 3, k + 14, crystalstone, 0);
        array.setBlock(i + 2, j + 4, k + 1, ch, 1);
        array.setBlock(i + 2, j + 4, k + 2, crystalstone, 0);
        array.setBlock(i + 2, j + 4, k + 3, ch, 1);
        array.setBlock(i + 2, j + 4, k + 11, ch, 1);
        array.setBlock(i + 2, j + 4, k + 12, crystalstone, 0);
        array.setBlock(i + 2, j + 4, k + 13, ch, 1);
        array.setBlock(i + 2, j + 5, k + 1, ch, 1);
        array.setBlock(i + 2, j + 5, k + 2, crystalstone, 0);
        array.setBlock(i + 2, j + 5, k + 3, ch, 1);
        array.setBlock(i + 2, j + 5, k + 11, ch, 1);
        array.setBlock(i + 2, j + 5, k + 12, crystalstone, 0);
        array.setBlock(i + 2, j + 5, k + 13, ch, 1);
        array.setBlock(i + 2, j + 6, k + 1, ch, 1);
        array.setBlock(i + 2, j + 6, k + 2, ch);
        array.setBlock(i + 2, j + 6, k + 3, ch, 1);
        array.setBlock(i + 2, j + 6, k + 11, ch, 1);
        array.setBlock(i + 2, j + 6, k + 12, ch);
        array.setBlock(i + 2, j + 6, k + 13, ch, 1);
        array.setBlock(i + 3, j + 1, k + 1, shield, 0);
        array.setBlock(i + 3, j + 1, k + 2, shield, 0);
        array.setBlock(i + 3, j + 1, k + 3, shield, 0);
        array.setBlock(i + 3, j + 1, k + 6, shield, 0);
        array.setBlock(i + 3, j + 1, k + 7, shield, 0);
        array.setBlock(i + 3, j + 1, k + 8, shield, 0);
        array.setBlock(i + 3, j + 1, k + 11, shield, 0);
        array.setBlock(i + 3, j + 1, k + 12, shield, 0);
        array.setBlock(i + 3, j + 1, k + 13, shield, 0);
        array.setBlock(i + 3, j + 2, k + 0, shield, 0);
        array.setBlock(i + 3, j + 2, k + 1, ch);
        array.setBlock(i + 3, j + 2, k + 2, ch);
        array.setBlock(i + 3, j + 2, k + 3, ch);
        array.setBlock(i + 3, j + 2, k + 4, shield, 0);
        array.setBlock(i + 3, j + 2, k + 5, shield, 0);
        array.setFluid(i + 3, j + 2, k + 6, er);
        array.setFluid(i + 3, j + 2, k + 7, er);
        array.setFluid(i + 3, j + 2, k + 8, er);
        array.setBlock(i + 3, j + 2, k + 9, shield, 0);
        array.setBlock(i + 3, j + 2, k + 10, shield, 0);
        array.setBlock(i + 3, j + 2, k + 11, ch);
        array.setBlock(i + 3, j + 2, k + 12, ch);
        array.setBlock(i + 3, j + 2, k + 13, ch);
        array.setBlock(i + 3, j + 2, k + 14, shield, 0);
        array.setBlock(i + 3, j + 3, k + 0, crystalstone, 0);
        array.setBlock(i + 3, j + 3, k + 2, ch, 1);
        array.setBlock(i + 3, j + 3, k + 4, crystalstone, 15);
        array.setBlock(i + 3, j + 3, k + 5, crystalstone, 12);
        array.setBlock(i + 3, j + 3, k + 9, crystalstone, 12);
        array.setBlock(i + 3, j + 3, k + 10, crystalstone, 15);
        array.setBlock(i + 3, j + 3, k + 12, ch, 1);
        array.setBlock(i + 3, j + 3, k + 14, crystalstone, 0);
        array.setBlock(i + 3, j + 4, k + 2, ch, 1);
        array.setBlock(i + 3, j + 4, k + 12, ch, 1);
        array.setBlock(i + 3, j + 5, k + 2, ch, 1);
        array.setBlock(i + 3, j + 5, k + 12, ch, 1);
        array.setBlock(i + 3, j + 6, k + 2, ch, 1);
        array.setBlock(i + 3, j + 6, k + 12, ch, 1);
        array.setBlock(i + 4, j + 2, k + 1, shield, 0);
        array.setBlock(i + 4, j + 2, k + 2, shield, 0);
        array.setBlock(i + 4, j + 2, k + 3, shield, 0);
        array.setBlock(i + 4, j + 2, k + 6, shield, 0);
        array.setBlock(i + 4, j + 2, k + 7, shield, 0);
        array.setBlock(i + 4, j + 2, k + 8, shield, 0);
        array.setBlock(i + 4, j + 2, k + 11, shield, 0);
        array.setBlock(i + 4, j + 2, k + 12, shield, 0);
        array.setBlock(i + 4, j + 2, k + 13, shield, 0);
        array.setBlock(i + 4, j + 3, k + 0, crystalstone, 0);
        array.setBlock(i + 4, j + 3, k + 1, crystalstone, 15);
        array.setBlock(i + 4, j + 3, k + 2, crystalstone, 15);
        array.setBlock(i + 4, j + 3, k + 3, crystalstone, 15);
        array.setBlock(i + 4, j + 3, k + 4, crystalstone, 0);
        array.setBlock(i + 4, j + 3, k + 5, crystalstone, 12);
        array.setBlock(i + 4, j + 3, k + 6, crystalstone, 12);
        array.setBlock(i + 4, j + 3, k + 7, crystalstone, 12);
        array.setBlock(i + 4, j + 3, k + 8, crystalstone, 12);
        array.setBlock(i + 4, j + 3, k + 9, crystalstone, 12);
        array.setBlock(i + 4, j + 3, k + 10, crystalstone, 0);
        array.setBlock(i + 4, j + 3, k + 11, crystalstone, 15);
        array.setBlock(i + 4, j + 3, k + 12, crystalstone, 15);
        array.setBlock(i + 4, j + 3, k + 13, crystalstone, 15);
        array.setBlock(i + 4, j + 3, k + 14, crystalstone, 0);
        array.setBlock(i + 4, j + 4, k + 0, crystalstone, 2);
        array.setBlock(i + 4, j + 4, k + 4, crystalstone, 2);
        array.setBlock(i + 4, j + 4, k + 10, crystalstone, 2);
        array.setBlock(i + 4, j + 4, k + 14, crystalstone, 2);
        array.setBlock(i + 4, j + 5, k + 0, crystalstone, 13);
        array.setBlock(i + 4, j + 5, k + 4, crystalstone, 5);
        array.setBlock(i + 4, j + 5, k + 10, crystalstone, 5);
        array.setBlock(i + 4, j + 5, k + 14, crystalstone, 13);
        array.setBlock(i + 4, j + 6, k + 0, crystalstone, 2);
        array.setBlock(i + 4, j + 6, k + 4, crystalstone, 2);
        array.setBlock(i + 4, j + 6, k + 10, crystalstone, 2);
        array.setBlock(i + 4, j + 6, k + 14, crystalstone, 2);
        array.setBlock(i + 4, j + 7, k + 0, crystalstone, 7);
        array.setBlock(i + 4, j + 7, k + 4, crystalstone, 6);
        array.setBlock(i + 4, j + 7, k + 5, crystalstone, 4);
        array.setBlock(i + 4, j + 7, k + 6, crystalstone, 4);
        array.setBlock(i + 4, j + 7, k + 7, crystalstone, 4);
        array.setBlock(i + 4, j + 7, k + 8, crystalstone, 4);
        array.setBlock(i + 4, j + 7, k + 9, crystalstone, 4);
        array.setBlock(i + 4, j + 7, k + 10, crystalstone, 6);
        array.setBlock(i + 4, j + 7, k + 14, crystalstone, 7);
        array.setBlock(i + 5, j + 1, k + 5, crystalstone, 0);
        array.setBlock(i + 5, j + 1, k + 6, crystalstone, 2);
        array.setBlock(i + 5, j + 1, k + 7, crystalstone, 2);
        array.setBlock(i + 5, j + 1, k + 8, crystalstone, 2);
        array.setBlock(i + 5, j + 1, k + 9, crystalstone, 0);
        array.setBlock(i + 5, j + 2, k + 2, shield, 0);
        array.setBlock(i + 5, j + 2, k + 3, shield, 0);
        array.setBlock(i + 5, j + 2, k + 5, crystalstone, 14);
        array.setBlock(i + 5, j + 2, k + 6, crystalstone, 11);
        array.setBlock(i + 5, j + 2, k + 7, crystalstone, 11);
        array.setBlock(i + 5, j + 2, k + 8, crystalstone, 11);
        array.setBlock(i + 5, j + 2, k + 9, crystalstone, 14);
        array.setBlock(i + 5, j + 2, k + 11, shield, 0);
        array.setBlock(i + 5, j + 2, k + 12, shield, 0);
        array.setBlock(i + 5, j + 3, k + 0, crystalstone, 15);
        array.setBlock(i + 5, j + 3, k + 1, crystalstone, 12);
        array.setBlock(i + 5, j + 3, k + 2, crystalstone, 12);
        array.setBlock(i + 5, j + 3, k + 3, crystalstone, 12);
        array.setBlock(i + 5, j + 3, k + 4, crystalstone, 12);
        array.setBlock(i + 5, j + 3, k + 10, crystalstone, 12);
        array.setBlock(i + 5, j + 3, k + 11, crystalstone, 12);
        array.setBlock(i + 5, j + 3, k + 12, crystalstone, 12);
        array.setBlock(i + 5, j + 3, k + 13, crystalstone, 12);
        array.setBlock(i + 5, j + 3, k + 14, crystalstone, 15);
        array.setBlock(i + 5, j + 7, k + 0, crystalstone, 1);
        array.setBlock(i + 5, j + 7, k + 4, crystalstone, 4);
        array.setBlock(i + 5, j + 7, k + 10, crystalstone, 4);
        array.setBlock(i + 5, j + 7, k + 14, crystalstone, 1);
        array.setBlock(i + 6, j + 0, k + 6, p);
        array.setBlock(i + 6, j + 0, k + 7, p);
        array.setBlock(i + 6, j + 0, k + 8, p);
        array.setBlock(i + 6, j + 1, k + 2, shield, 0);
        array.setBlock(i + 6, j + 1, k + 3, shield, 0);
        array.setBlock(i + 6, j + 1, k + 5, crystalstone, 2);
        array.setBlock(i + 6, j + 1, k + 9, crystalstone, 2);
        array.setBlock(i + 6, j + 1, k + 11, shield, 0);
        array.setBlock(i + 6, j + 1, k + 12, shield, 0);
        array.setBlock(i + 6, j + 2, k + 1, shield, 0);
        array.setFluid(i + 6, j + 2, k + 2, er);
        array.setFluid(i + 6, j + 2, k + 3, er);
        array.setBlock(i + 6, j + 2, k + 4, shield, 0);
        array.setBlock(i + 6, j + 2, k + 5, crystalstone, 10);
        array.setBlock(i + 6, j + 2, k + 9, crystalstone, 10);
        array.setBlock(i + 6, j + 2, k + 10, shield, 0);
        array.setFluid(i + 6, j + 2, k + 11, er);
        array.setFluid(i + 6, j + 2, k + 12, er);
        array.setBlock(i + 6, j + 2, k + 13, shield, 0);
        array.setBlock(i + 6, j + 3, k + 0, crystalstone, 15);
        array.setBlock(i + 6, j + 3, k + 1, crystalstone, 12);
        array.setBlock(i + 6, j + 3, k + 4, crystalstone, 12);
        array.setBlock(i + 6, j + 3, k + 10, crystalstone, 12);
        array.setBlock(i + 6, j + 3, k + 13, crystalstone, 12);
        array.setBlock(i + 6, j + 3, k + 14, crystalstone, 15);
        array.setBlock(i + 6, j + 7, k + 0, crystalstone, 1);
        array.setBlock(i + 6, j + 7, k + 4, crystalstone, 4);
        array.setBlock(i + 6, j + 7, k + 10, crystalstone, 4);
        array.setBlock(i + 6, j + 7, k + 14, crystalstone, 1);
        array.setBlock(i + 7, j + 0, k + 6, p);
        array.setBlock(i + 7, j + 0, k + 7, p);
        array.setBlock(i + 7, j + 0, k + 8, p);
        array.setBlock(i + 7, j + 1, k + 2, shield, 0);
        array.setBlock(i + 7, j + 1, k + 3, shield, 0);
        array.setBlock(i + 7, j + 1, k + 5, crystalstone, 2);
        array.setBlock(i + 7, j + 1, k + 9, crystalstone, 2);
        array.setBlock(i + 7, j + 1, k + 11, shield, 0);
        array.setBlock(i + 7, j + 1, k + 12, shield, 0);
        array.setBlock(i + 7, j + 2, k + 1, shield, 0);
        array.setFluid(i + 7, j + 2, k + 2, er);
        array.setFluid(i + 7, j + 2, k + 3, er);
        array.setBlock(i + 7, j + 2, k + 4, shield, 0);
        array.setBlock(i + 7, j + 2, k + 5, crystalstone, 10);
        array.setBlock(i + 7, j + 2, k + 9, crystalstone, 10);
        array.setBlock(i + 7, j + 2, k + 10, shield, 0);
        array.setFluid(i + 7, j + 2, k + 11, er);
        array.setFluid(i + 7, j + 2, k + 12, er);
        array.setBlock(i + 7, j + 2, k + 13, shield, 0);
        array.setBlock(i + 7, j + 3, k + 0, crystalstone, 15);
        array.setBlock(i + 7, j + 3, k + 1, crystalstone, 12);
        array.setBlock(i + 7, j + 3, k + 4, crystalstone, 12);
        array.setBlock(i + 7, j + 3, k + 10, crystalstone, 12);
        array.setBlock(i + 7, j + 3, k + 13, crystalstone, 12);
        array.setBlock(i + 7, j + 3, k + 14, crystalstone, 15);
        array.setBlock(i + 7, j + 7, k + 0, crystalstone, 1);
        array.setBlock(i + 7, j + 7, k + 4, crystalstone, 4);
        array.setBlock(i + 7, j + 7, k + 10, crystalstone, 4);
        array.setBlock(i + 7, j + 7, k + 14, crystalstone, 1);
        array.setBlock(i + 8, j + 0, k + 6, p);
        array.setBlock(i + 8, j + 0, k + 7, p);
        array.setBlock(i + 8, j + 0, k + 8, p);
        array.setBlock(i + 8, j + 1, k + 2, shield, 0);
        array.setBlock(i + 8, j + 1, k + 3, shield, 0);
        array.setBlock(i + 8, j + 1, k + 5, crystalstone, 2);
        array.setBlock(i + 8, j + 1, k + 9, crystalstone, 2);
        array.setBlock(i + 8, j + 1, k + 11, shield, 0);
        array.setBlock(i + 8, j + 1, k + 12, shield, 0);
        array.setBlock(i + 8, j + 2, k + 1, shield, 0);
        array.setFluid(i + 8, j + 2, k + 2, er);
        array.setFluid(i + 8, j + 2, k + 3, er);
        array.setBlock(i + 8, j + 2, k + 4, shield, 0);
        array.setBlock(i + 8, j + 2, k + 5, crystalstone, 10);
        array.setBlock(i + 8, j + 2, k + 9, crystalstone, 10);
        array.setBlock(i + 8, j + 2, k + 10, shield, 0);
        array.setFluid(i + 8, j + 2, k + 11, er);
        array.setFluid(i + 8, j + 2, k + 12, er);
        array.setBlock(i + 8, j + 2, k + 13, shield, 0);
        array.setBlock(i + 8, j + 3, k + 0, crystalstone, 15);
        array.setBlock(i + 8, j + 3, k + 1, crystalstone, 12);
        array.setBlock(i + 8, j + 3, k + 4, crystalstone, 12);
        array.setBlock(i + 8, j + 3, k + 10, crystalstone, 12);
        array.setBlock(i + 8, j + 3, k + 13, crystalstone, 12);
        array.setBlock(i + 8, j + 3, k + 14, crystalstone, 15);
        array.setBlock(i + 8, j + 7, k + 0, crystalstone, 1);
        array.setBlock(i + 8, j + 7, k + 4, crystalstone, 4);
        array.setBlock(i + 8, j + 7, k + 10, crystalstone, 4);
        array.setBlock(i + 8, j + 7, k + 14, crystalstone, 1);
        array.setBlock(i + 9, j + 1, k + 5, crystalstone, 0);
        array.setBlock(i + 9, j + 1, k + 6, crystalstone, 2);
        array.setBlock(i + 9, j + 1, k + 7, crystalstone, 2);
        array.setBlock(i + 9, j + 1, k + 8, crystalstone, 2);
        array.setBlock(i + 9, j + 1, k + 9, crystalstone, 0);
        array.setBlock(i + 9, j + 2, k + 2, shield, 0);
        array.setBlock(i + 9, j + 2, k + 3, shield, 0);
        array.setBlock(i + 9, j + 2, k + 5, crystalstone, 14);
        array.setBlock(i + 9, j + 2, k + 6, crystalstone, 11);
        array.setBlock(i + 9, j + 2, k + 7, crystalstone, 11);
        array.setBlock(i + 9, j + 2, k + 8, crystalstone, 11);
        array.setBlock(i + 9, j + 2, k + 9, crystalstone, 14);
        array.setBlock(i + 9, j + 2, k + 11, shield, 0);
        array.setBlock(i + 9, j + 2, k + 12, shield, 0);
        array.setBlock(i + 9, j + 3, k + 0, crystalstone, 15);
        array.setBlock(i + 9, j + 3, k + 1, crystalstone, 12);
        array.setBlock(i + 9, j + 3, k + 2, crystalstone, 12);
        array.setBlock(i + 9, j + 3, k + 3, crystalstone, 12);
        array.setBlock(i + 9, j + 3, k + 4, crystalstone, 12);
        array.setBlock(i + 9, j + 3, k + 10, crystalstone, 12);
        array.setBlock(i + 9, j + 3, k + 11, crystalstone, 12);
        array.setBlock(i + 9, j + 3, k + 12, crystalstone, 12);
        array.setBlock(i + 9, j + 3, k + 13, crystalstone, 12);
        array.setBlock(i + 9, j + 3, k + 14, crystalstone, 15);
        array.setBlock(i + 9, j + 7, k + 0, crystalstone, 1);
        array.setBlock(i + 9, j + 7, k + 4, crystalstone, 4);
        array.setBlock(i + 9, j + 7, k + 10, crystalstone, 4);
        array.setBlock(i + 9, j + 7, k + 14, crystalstone, 1);
        array.setBlock(i + 10, j + 2, k + 1, shield, 0);
        array.setBlock(i + 10, j + 2, k + 2, shield, 0);
        array.setBlock(i + 10, j + 2, k + 3, shield, 0);
        array.setBlock(i + 10, j + 2, k + 6, shield, 0);
        array.setBlock(i + 10, j + 2, k + 7, shield, 0);
        array.setBlock(i + 10, j + 2, k + 8, shield, 0);
        array.setBlock(i + 10, j + 2, k + 11, shield, 0);
        array.setBlock(i + 10, j + 2, k + 12, shield, 0);
        array.setBlock(i + 10, j + 2, k + 13, shield, 0);
        array.setBlock(i + 10, j + 3, k + 0, crystalstone, 0);
        array.setBlock(i + 10, j + 3, k + 1, crystalstone, 15);
        array.setBlock(i + 10, j + 3, k + 2, crystalstone, 15);
        array.setBlock(i + 10, j + 3, k + 3, crystalstone, 15);
        array.setBlock(i + 10, j + 3, k + 4, crystalstone, 0);
        array.setBlock(i + 10, j + 3, k + 5, crystalstone, 12);
        array.setBlock(i + 10, j + 3, k + 6, crystalstone, 12);
        array.setBlock(i + 10, j + 3, k + 7, crystalstone, 12);
        array.setBlock(i + 10, j + 3, k + 8, crystalstone, 12);
        array.setBlock(i + 10, j + 3, k + 9, crystalstone, 12);
        array.setBlock(i + 10, j + 3, k + 10, crystalstone, 0);
        array.setBlock(i + 10, j + 3, k + 11, crystalstone, 15);
        array.setBlock(i + 10, j + 3, k + 12, crystalstone, 15);
        array.setBlock(i + 10, j + 3, k + 13, crystalstone, 15);
        array.setBlock(i + 10, j + 3, k + 14, crystalstone, 0);
        array.setBlock(i + 10, j + 4, k + 0, crystalstone, 2);
        array.setBlock(i + 10, j + 4, k + 4, crystalstone, 2);
        array.setBlock(i + 10, j + 4, k + 10, crystalstone, 2);
        array.setBlock(i + 10, j + 4, k + 14, crystalstone, 2);
        array.setBlock(i + 10, j + 5, k + 0, crystalstone, 13);
        array.setBlock(i + 10, j + 5, k + 4, crystalstone, 5);
        array.setBlock(i + 10, j + 5, k + 10, crystalstone, 5);
        array.setBlock(i + 10, j + 5, k + 14, crystalstone, 13);
        array.setBlock(i + 10, j + 6, k + 0, crystalstone, 2);
        array.setBlock(i + 10, j + 6, k + 4, crystalstone, 2);
        array.setBlock(i + 10, j + 6, k + 10, crystalstone, 2);
        array.setBlock(i + 10, j + 6, k + 14, crystalstone, 2);
        array.setBlock(i + 10, j + 7, k + 0, crystalstone, 7);
        array.setBlock(i + 10, j + 7, k + 4, crystalstone, 6);
        array.setBlock(i + 10, j + 7, k + 5, crystalstone, 4);
        array.setBlock(i + 10, j + 7, k + 6, crystalstone, 4);
        array.setBlock(i + 10, j + 7, k + 7, crystalstone, 4);
        array.setBlock(i + 10, j + 7, k + 8, crystalstone, 4);
        array.setBlock(i + 10, j + 7, k + 9, crystalstone, 4);
        array.setBlock(i + 10, j + 7, k + 10, crystalstone, 6);
        array.setBlock(i + 10, j + 7, k + 14, crystalstone, 7);
        array.setBlock(i + 11, j + 1, k + 1, shield, 0);
        array.setBlock(i + 11, j + 1, k + 2, shield, 0);
        array.setBlock(i + 11, j + 1, k + 3, shield, 0);
        array.setBlock(i + 11, j + 1, k + 6, shield, 0);
        array.setBlock(i + 11, j + 1, k + 7, shield, 0);
        array.setBlock(i + 11, j + 1, k + 8, shield, 0);
        array.setBlock(i + 11, j + 1, k + 11, shield, 0);
        array.setBlock(i + 11, j + 1, k + 12, shield, 0);
        array.setBlock(i + 11, j + 1, k + 13, shield, 0);
        array.setBlock(i + 11, j + 2, k + 0, shield, 0);
        array.setBlock(i + 11, j + 2, k + 1, ch);
        array.setBlock(i + 11, j + 2, k + 2, ch);
        array.setBlock(i + 11, j + 2, k + 3, ch);
        array.setBlock(i + 11, j + 2, k + 4, shield, 0);
        array.setBlock(i + 11, j + 2, k + 5, shield, 0);
        array.setFluid(i + 11, j + 2, k + 6, er);
        array.setFluid(i + 11, j + 2, k + 7, er);
        array.setFluid(i + 11, j + 2, k + 8, er);
        array.setBlock(i + 11, j + 2, k + 9, shield, 0);
        array.setBlock(i + 11, j + 2, k + 10, shield, 0);
        array.setBlock(i + 11, j + 2, k + 11, ch);
        array.setBlock(i + 11, j + 2, k + 12, ch);
        array.setBlock(i + 11, j + 2, k + 13, ch);
        array.setBlock(i + 11, j + 2, k + 14, shield, 0);
        array.setBlock(i + 11, j + 3, k + 0, crystalstone, 0);
        array.setBlock(i + 11, j + 3, k + 2, ch, 1);
        array.setBlock(i + 11, j + 3, k + 4, crystalstone, 15);
        array.setBlock(i + 11, j + 3, k + 5, crystalstone, 12);
        array.setBlock(i + 11, j + 3, k + 9, crystalstone, 12);
        array.setBlock(i + 11, j + 3, k + 10, crystalstone, 15);
        array.setBlock(i + 11, j + 3, k + 12, ch, 1);
        array.setBlock(i + 11, j + 3, k + 14, crystalstone, 0);
        array.setBlock(i + 11, j + 4, k + 2, ch, 1);
        array.setBlock(i + 11, j + 4, k + 12, ch, 1);
        array.setBlock(i + 11, j + 5, k + 2, ch, 1);
        array.setBlock(i + 11, j + 5, k + 12, ch, 1);
        array.setBlock(i + 11, j + 6, k + 2, ch, 1);
        array.setBlock(i + 11, j + 6, k + 12, ch, 1);
        array.setBlock(i + 12, j + 1, k + 1, shield, 0);
        array.setBlock(i + 12, j + 1, k + 2, shield, 0);
        array.setBlock(i + 12, j + 1, k + 3, shield, 0);
        array.setBlock(i + 12, j + 1, k + 6, shield, 0);
        array.setBlock(i + 12, j + 1, k + 7, shield, 0);
        array.setBlock(i + 12, j + 1, k + 8, shield, 0);
        array.setBlock(i + 12, j + 1, k + 11, shield, 0);
        array.setBlock(i + 12, j + 1, k + 12, shield, 0);
        array.setBlock(i + 12, j + 1, k + 13, shield, 0);
        array.setBlock(i + 12, j + 2, k + 0, shield, 0);
        array.setBlock(i + 12, j + 2, k + 1, ch);
        array.setBlock(i + 12, j + 2, k + 2, crystalstone, 0);
        array.setBlock(i + 12, j + 2, k + 3, ch);
        array.setBlock(i + 12, j + 2, k + 4, shield, 0);
        array.setBlock(i + 12, j + 2, k + 5, shield, 0);
        array.setFluid(i + 12, j + 2, k + 6, er);
        array.setFluid(i + 12, j + 2, k + 7, er);
        array.setFluid(i + 12, j + 2, k + 8, er);
        array.setBlock(i + 12, j + 2, k + 9, shield, 0);
        array.setBlock(i + 12, j + 2, k + 10, shield, 0);
        array.setBlock(i + 12, j + 2, k + 11, ch);
        array.setBlock(i + 12, j + 2, k + 12, crystalstone, 0);
        array.setBlock(i + 12, j + 2, k + 13, ch);
        array.setBlock(i + 12, j + 2, k + 14, shield, 0);
        array.setBlock(i + 12, j + 3, k + 0, crystalstone, 0);
        array.setBlock(i + 12, j + 3, k + 1, ch, 1);
        array.setBlock(i + 12, j + 3, k + 2, crystalstone, 0);
        array.setBlock(i + 12, j + 3, k + 3, ch, 1);
        array.setBlock(i + 12, j + 3, k + 4, crystalstone, 15);
        array.setBlock(i + 12, j + 3, k + 5, crystalstone, 12);
        array.setBlock(i + 12, j + 3, k + 9, crystalstone, 12);
        array.setBlock(i + 12, j + 3, k + 10, crystalstone, 15);
        array.setBlock(i + 12, j + 3, k + 11, ch, 1);
        array.setBlock(i + 12, j + 3, k + 12, crystalstone, 0);
        array.setBlock(i + 12, j + 3, k + 13, ch, 1);
        array.setBlock(i + 12, j + 3, k + 14, crystalstone, 0);
        array.setBlock(i + 12, j + 4, k + 1, ch, 1);
        array.setBlock(i + 12, j + 4, k + 2, crystalstone, 0);
        array.setBlock(i + 12, j + 4, k + 3, ch, 1);
        array.setBlock(i + 12, j + 4, k + 11, ch, 1);
        array.setBlock(i + 12, j + 4, k + 12, crystalstone, 0);
        array.setBlock(i + 12, j + 4, k + 13, ch, 1);
        array.setBlock(i + 12, j + 5, k + 1, ch, 1);
        array.setBlock(i + 12, j + 5, k + 2, crystalstone, 0);
        array.setBlock(i + 12, j + 5, k + 3, ch, 1);
        array.setBlock(i + 12, j + 5, k + 11, ch, 1);
        array.setBlock(i + 12, j + 5, k + 12, crystalstone, 0);
        array.setBlock(i + 12, j + 5, k + 13, ch, 1);
        array.setBlock(i + 12, j + 6, k + 1, ch, 1);
        array.setBlock(i + 12, j + 6, k + 2, ch);
        array.setBlock(i + 12, j + 6, k + 3, ch, 1);
        array.setBlock(i + 12, j + 6, k + 11, ch, 1);
        array.setBlock(i + 12, j + 6, k + 12, ch);
        array.setBlock(i + 12, j + 6, k + 13, ch, 1);
        array.setBlock(i + 13, j + 1, k + 1, shield, 0);
        array.setBlock(i + 13, j + 1, k + 2, shield, 0);
        array.setBlock(i + 13, j + 1, k + 3, shield, 0);
        array.setBlock(i + 13, j + 1, k + 11, shield, 0);
        array.setBlock(i + 13, j + 1, k + 12, shield, 0);
        array.setBlock(i + 13, j + 1, k + 13, shield, 0);
        array.setBlock(i + 13, j + 2, k + 0, shield, 0);
        array.setBlock(i + 13, j + 2, k + 1, ch);
        array.setBlock(i + 13, j + 2, k + 2, ch);
        array.setBlock(i + 13, j + 2, k + 3, ch);
        array.setBlock(i + 13, j + 2, k + 4, shield, 0);
        array.setBlock(i + 13, j + 2, k + 6, shield, 0);
        array.setBlock(i + 13, j + 2, k + 7, shield, 0);
        array.setBlock(i + 13, j + 2, k + 8, shield, 0);
        array.setBlock(i + 13, j + 2, k + 10, shield, 0);
        array.setBlock(i + 13, j + 2, k + 11, ch);
        array.setBlock(i + 13, j + 2, k + 12, ch);
        array.setBlock(i + 13, j + 2, k + 13, ch);
        array.setBlock(i + 13, j + 2, k + 14, shield, 0);
        array.setBlock(i + 13, j + 3, k + 0, crystalstone, 0);
        array.setBlock(i + 13, j + 3, k + 2, ch, 1);
        array.setBlock(i + 13, j + 3, k + 4, crystalstone, 15);
        array.setBlock(i + 13, j + 3, k + 5, crystalstone, 12);
        array.setBlock(i + 13, j + 3, k + 6, crystalstone, 12);
        array.setBlock(i + 13, j + 3, k + 7, crystalstone, 12);
        array.setBlock(i + 13, j + 3, k + 8, crystalstone, 12);
        array.setBlock(i + 13, j + 3, k + 9, crystalstone, 12);
        array.setBlock(i + 13, j + 3, k + 10, crystalstone, 15);
        array.setBlock(i + 13, j + 3, k + 12, ch, 1);
        array.setBlock(i + 13, j + 3, k + 14, crystalstone, 0);
        array.setBlock(i + 13, j + 4, k + 2, ch, 1);
        array.setBlock(i + 13, j + 4, k + 12, ch, 1);
        array.setBlock(i + 13, j + 5, k + 2, ch, 1);
        array.setBlock(i + 13, j + 5, k + 12, ch, 1);
        array.setBlock(i + 13, j + 6, k + 2, ch, 1);
        array.setBlock(i + 13, j + 6, k + 12, ch, 1);
        array.setBlock(i + 14, j + 2, k + 1, shield, 0);
        array.setBlock(i + 14, j + 2, k + 2, shield, 0);
        array.setBlock(i + 14, j + 2, k + 3, shield, 0);
        array.setBlock(i + 14, j + 2, k + 11, shield, 0);
        array.setBlock(i + 14, j + 2, k + 12, shield, 0);
        array.setBlock(i + 14, j + 2, k + 13, shield, 0);
        array.setBlock(i + 14, j + 3, k + 0, crystalstone, 0);
        array.setBlock(i + 14, j + 3, k + 1, crystalstone, 0);
        array.setBlock(i + 14, j + 3, k + 2, crystalstone, 0);
        array.setBlock(i + 14, j + 3, k + 3, crystalstone, 0);
        array.setBlock(i + 14, j + 3, k + 4, crystalstone, 0);
        array.setBlock(i + 14, j + 3, k + 5, crystalstone, 15);
        array.setBlock(i + 14, j + 3, k + 6, crystalstone, 15);
        array.setBlock(i + 14, j + 3, k + 7, crystalstone, 15);
        array.setBlock(i + 14, j + 3, k + 8, crystalstone, 15);
        array.setBlock(i + 14, j + 3, k + 9, crystalstone, 15);
        array.setBlock(i + 14, j + 3, k + 10, crystalstone, 0);
        array.setBlock(i + 14, j + 3, k + 11, crystalstone, 0);
        array.setBlock(i + 14, j + 3, k + 12, crystalstone, 0);
        array.setBlock(i + 14, j + 3, k + 13, crystalstone, 0);
        array.setBlock(i + 14, j + 3, k + 14, crystalstone, 0);
        array.setBlock(i + 14, j + 4, k + 0, crystalstone, 2);
        array.setBlock(i + 14, j + 4, k + 4, crystalstone, 2);
        array.setBlock(i + 14, j + 4, k + 10, crystalstone, 2);
        array.setBlock(i + 14, j + 4, k + 14, crystalstone, 2);
        array.setBlock(i + 14, j + 5, k + 0, crystalstone, 3);
        array.setBlock(i + 14, j + 5, k + 4, crystalstone, 13);
        array.setBlock(i + 14, j + 5, k + 10, crystalstone, 13);
        array.setBlock(i + 14, j + 5, k + 14, crystalstone, 3);
        array.setBlock(i + 14, j + 6, k + 0, crystalstone, 2);
        array.setBlock(i + 14, j + 6, k + 4, crystalstone, 2);
        array.setBlock(i + 14, j + 6, k + 10, crystalstone, 2);
        array.setBlock(i + 14, j + 6, k + 14, crystalstone, 2);
        array.setBlock(i + 14, j + 7, k + 0, crystalstone, 3);
        array.setBlock(i + 14, j + 7, k + 4, crystalstone, 7);
        array.setBlock(i + 14, j + 7, k + 5, crystalstone, 1);
        array.setBlock(i + 14, j + 7, k + 6, crystalstone, 1);
        array.setBlock(i + 14, j + 7, k + 7, crystalstone, 1);
        array.setBlock(i + 14, j + 7, k + 8, crystalstone, 1);
        array.setBlock(i + 14, j + 7, k + 9, crystalstone, 1);
        array.setBlock(i + 14, j + 7, k + 10, crystalstone, 7);
        array.setBlock(i + 14, j + 7, k + 14, crystalstone, 3);
        array.setBlock(i + 14, j + 8, k + 0, crystalstone, 2);
        array.setBlock(i + 14, j + 8, k + 14, crystalstone, 2);
        array.setBlock(i + 14, j + 9, k + 0, crystalstone, 5);
        array.setBlock(i + 14, j + 9, k + 14, crystalstone, 5);

        if (this.isDisplay()) {
            int mx = i + 7;
            int mz = k + 7;

            array.setBlock(mx - 5, j + 4, mz - 9, Blocks.bedrock);
            array.setBlock(mx - 9, j + 4, mz - 5, Blocks.bedrock);

            array.setBlock(mx + 5, j + 4, mz - 9, Blocks.bedrock);
            array.setBlock(mx + 9, j + 4, mz - 5, Blocks.bedrock);

            array.setBlock(mx - 5, j + 4, mz + 9, Blocks.bedrock);
            array.setBlock(mx - 9, j + 4, mz + 5, Blocks.bedrock);

            array.setBlock(mx + 5, j + 4, mz + 9, Blocks.bedrock);
            array.setBlock(mx + 9, j + 4, mz + 5, Blocks.bedrock);
        }

        return array;
    }
}