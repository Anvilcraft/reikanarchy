package Reika.ElectriCraft.API;

import Reika.RotaryCraft.API.Power.ShaftMachine;
import Reika.RotaryCraft.Auxiliary.Interfaces.PowerSourceTracker;
import net.minecraftforge.common.util.ForgeDirection;

public interface WrappableWireSource extends ShaftMachine, PowerSourceTracker {
    public boolean canConnectToSide(ForgeDirection dir);

    public boolean isFunctional();

    public boolean hasPowerStatusChangedSinceLastTick();
}
