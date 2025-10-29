package Reika.DragonAPI.Interfaces.TileEntity;

public interface SidePlacedTile {
    public void placeOnSide(int s);

    public boolean checkLocationValidity();

    public void drop();
}
