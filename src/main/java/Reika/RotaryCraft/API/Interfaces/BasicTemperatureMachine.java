package Reika.RotaryCraft.API.Interfaces;

import Reika.DragonAPI.Interfaces.TileEntity.ThermalTile;

public interface BasicTemperatureMachine extends ThermalTile, TemperatureTile {
    /**
     * If you have "cool to ambient" behavior, and it has a timer, this resets it so as
     * to pause such logic.
     */
    public void resetAmbientTemperatureTimer();
}
