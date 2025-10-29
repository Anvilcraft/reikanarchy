package Reika.DragonAPI.Exception;

import Reika.DragonAPI.ModList;

public class ModHandlerException extends DragonAPIException {
    public ModHandlerException(ModList mod) {
        message.append(
            "You cannot call a mod handler before its parent mod initializes!\n"
        );
        message.append("Target mod: " + mod);
        this.crash();
    }
}
