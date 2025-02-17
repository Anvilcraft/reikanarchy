/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.Exception;

import Reika.DragonAPI.Base.DragonAPIMod;

public class MissingDependencyException extends UserErrorException {
    public MissingDependencyException(DragonAPIMod mod, String mod2) {
        message.append(mod.getDisplayName() + " was not installed correctly:\n");
        message.append(
            mod.getDisplayName() + " was installed without its dependency " + mod2 + "!\n"
        );
        this.applyDNP(mod);
        this.crash();
    }
}
