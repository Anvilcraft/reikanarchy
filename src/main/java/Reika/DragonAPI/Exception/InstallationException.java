package Reika.DragonAPI.Exception;

import Reika.DragonAPI.Base.DragonAPIMod;
import Reika.DragonAPI.ModList;

public class InstallationException extends UserErrorException {
    public InstallationException(DragonAPIMod mod, String msg) {
        this(mod, msg, null);
    }

    public InstallationException(String modname, String msg) {
        message.append(modname + " was not installed correctly:\n");
        message.append(msg + "\n");
        message.append("Try consulting the mod website for information.\n");
        message.append("This is not a bug. Do not post it unless you are really stuck.");
        this.crash();
    }

    public InstallationException(ModList mod, String msg) {
        message.append(mod.name() + " was not installed correctly:\n");
        message.append(msg + "\n");
        message.append(
            "This is not a mod bug. Do not post it to the forums/websites unless you are really stuck."
        );
        this.crash();
    }

    public InstallationException(DragonAPIMod mod, Exception e) {
        super(e);
        message.append(mod.getDisplayName() + " was not installed correctly:\n");
    }

    public InstallationException(DragonAPIMod mod, String msg, Exception e) {
        super(e);
        message.append(mod.getDisplayName() + " was not installed correctly:\n");
        message.append(msg + "\n");
        message.append(
            "Try consulting " + mod.getDocumentationSite().toString()
            + "for information.\n"
        );
        this.applyDNP(mod);
        this.crash();
    }
}
