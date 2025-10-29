package Reika.DragonAPI.Exception;

import Reika.DragonAPI.Base.DragonAPIMod;

public class MissingASMException extends DragonAPIException {
    public MissingASMException(DragonAPIMod mod) {
        message.append(mod.getDisplayName() + " is missing its ASM transformers.\n");
        message.append(
            "This should never happen, and is likely caused by a jar manifest failure.\n"
        );
        message.append(
            "If you got this by editing the mod jar, you may have to redownload the mod. Consult the developer for further questions."
        );
        this.crash();
    }
}
