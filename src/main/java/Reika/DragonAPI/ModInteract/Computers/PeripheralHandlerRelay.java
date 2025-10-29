package Reika.DragonAPI.ModInteract.Computers;

import Reika.DragonAPI.ASM.DependentMethodStripper.ModDependent;
import Reika.DragonAPI.ModList;
import dan200.computercraft.api.ComputerCraftAPI;
import li.cil.oc.api.Driver;

public class PeripheralHandlerRelay {
    @ModDependent(ModList.COMPUTERCRAFT)
    public static void registerCCHandler() {
        ComputerCraftAPI.registerPeripheralProvider(new PeripheralHandlerCC());
    }

    @ModDependent(ModList.OPENCOMPUTERS)
    public static void registerOCHandler() {
        Driver.add(new PeripheralHandlerOC());
    }
}
