/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.Interfaces.Configuration;

public interface StringArrayConfig extends ConfigList {
    public boolean isStringArray();

    public String[] getStringArray();

    public String[] getDefaultStringArray();
}
