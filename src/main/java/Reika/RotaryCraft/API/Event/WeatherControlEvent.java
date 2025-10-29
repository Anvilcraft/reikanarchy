package Reika.RotaryCraft.API.Event;

import Reika.DragonAPI.Instantiable.Event.TileEntityEvent;
import net.minecraft.tileentity.TileEntity;

public class WeatherControlEvent extends TileEntityEvent {
    public final boolean setRain;
    public final boolean setThunder;
    public final boolean setSuperStorm;

    public WeatherControlEvent(
        TileEntity te, boolean rain, boolean thunder, boolean storm
    ) {
        super(te);

        setRain = rain;
        setSuperStorm = storm;
        setThunder = thunder;
    }
}
