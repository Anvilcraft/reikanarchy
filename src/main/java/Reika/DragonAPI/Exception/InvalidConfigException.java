package Reika.DragonAPI.Exception;

import Reika.DragonAPI.Base.DragonAPIMod;
import Reika.DragonAPI.Interfaces.Configuration.BoundedConfig;
import Reika.DragonAPI.Interfaces.Configuration.ConfigList;
import net.minecraftforge.common.config.Property;

public class InvalidConfigException extends UserErrorException {
    public InvalidConfigException(
        DragonAPIMod mod, ConfigList cfg, String bounds, Property p
    ) {
        this(mod, cfg.getLabel(), bounds, p.getString());
    }

    public InvalidConfigException(DragonAPIMod mod, BoundedConfig cfg, Property p) {
        this(mod, cfg.getLabel(), cfg.getBoundsAsString(), p.getString());
    }

    private InvalidConfigException(
        DragonAPIMod mod, String name, String bounds, String val
    ) {
        message.append(mod.getDisplayName() + " was not configured correctly:\n");
        message.append(
            "Setting '" + name + "' was set to value '" + val
            + "', which is invalid. Value must be in the bounds " + bounds + ".\n"
        );
        message.append(
            "Try consulting " + mod.getDocumentationSite().toString()
            + "for information.\n"
        );
        this.applyDNP(mod);
        this.crash();
    }
}
