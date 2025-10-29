package Reika.DragonAPI.Interfaces.Block;

public interface SidedTextureIndex {
    public int getBlockTextureFromSideAndMetadata(int side, int metadata);
    public int getRenderType();
}
