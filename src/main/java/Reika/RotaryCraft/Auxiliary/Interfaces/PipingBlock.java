/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.Auxiliary.Interfaces;

import net.minecraft.util.IIcon;

public interface PipingBlock {
    public IIcon getBodyIcon(int meta);

    public IIcon getGlassIcon(int meta);
}
