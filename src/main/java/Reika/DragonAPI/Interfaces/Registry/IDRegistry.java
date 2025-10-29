package Reika.DragonAPI.Interfaces.Registry;

import Reika.DragonAPI.Interfaces.Configuration.MatchingConfig;

public interface IDRegistry extends MatchingConfig {
    public int getDefaultID();

    public String getCategory();
}
