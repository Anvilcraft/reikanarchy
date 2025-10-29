package Reika.DragonAPI.Exception;

import Reika.DragonAPI.Base.DragonAPIMod;

public class ModIntegrityException extends DragonAPIException {
    public ModIntegrityException(DragonAPIMod mod, String tampering) {
        message.append(
            "Something has tampered with and broken " + mod.getDisplayName() + "!\n"
        );
        message.append(tampering + " has occurred!\n");
        message.append(
            "Contact " + mod.getModAuthorName()
            + " immediately, with your full mod list!\n"
        );
        this.crash();
    }
}
