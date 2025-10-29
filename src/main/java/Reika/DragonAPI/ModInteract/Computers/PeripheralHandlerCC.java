package Reika.DragonAPI.ModInteract.Computers;

import Reika.DragonAPI.Base.TileEntityBase;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PeripheralHandlerCC implements IPeripheralProvider {
    @Override
    public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {
        TileEntity te = world.getTileEntity(x, y, z);
        return te instanceof TileEntityBase ? (IPeripheral) te : null;
    }
}
