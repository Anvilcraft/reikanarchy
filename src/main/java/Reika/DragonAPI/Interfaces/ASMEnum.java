/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.Interfaces;

import org.objectweb.asm.tree.ClassNode;

public interface ASMEnum {
    public String name();

    public void apply(ClassNode data);
}
