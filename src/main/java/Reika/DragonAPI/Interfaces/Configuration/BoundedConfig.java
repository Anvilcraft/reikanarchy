package Reika.DragonAPI.Interfaces.Configuration;

import net.minecraftforge.common.config.Property;

public interface BoundedConfig extends ConfigList {
    public boolean isValueValid(Property p);

    public String getBoundsAsString();
}
