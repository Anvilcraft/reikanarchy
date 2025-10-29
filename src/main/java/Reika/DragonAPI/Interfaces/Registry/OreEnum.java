package Reika.DragonAPI.Interfaces.Registry;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public interface OreEnum {
    public int getHarvestLevel();
    public String getHarvestTool();
    public float getXPDropped(World world, int x, int y, int z);
    public boolean dropsSelf(World world, int x, int y, int z);
    public boolean enforceHarvestLevel();

    public Block getBlock();
    public int getBlockMetadata();
    public TileEntity getTileEntity(World world, int x, int y, int z);

    public boolean canGenAt(World world, int posX, int posY, int posZ);
    public int getRandomGeneratedYCoord(World world, int posX, int posZ, Random random);
}
