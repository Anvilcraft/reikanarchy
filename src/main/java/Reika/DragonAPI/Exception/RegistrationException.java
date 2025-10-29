package Reika.DragonAPI.Exception;

import Reika.DragonAPI.Base.DragonAPIMod;

public class RegistrationException extends DragonAPIException {
    public RegistrationException(DragonAPIMod mod, String msg) {
        this(mod, msg, null);
    }

    public RegistrationException(DragonAPIMod mod, String msg, Throwable e) {
        message.append(mod.getTechnicalName() + " has a registration error:\n");
        message.append(msg + "\n");
        message.append("Contact " + mod.getModAuthorName() + " immediately!\n");
        message.append("Include the following information:");
        if (e != null)
            this.initCause(e);
        this.crash();
    }
}
