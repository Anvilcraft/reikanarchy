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

import net.minecraftforge.common.util.ForgeDirection;

public interface PipeRenderConnector {
    public boolean canConnectToPipeOnSide(ForgeDirection dir);
}
