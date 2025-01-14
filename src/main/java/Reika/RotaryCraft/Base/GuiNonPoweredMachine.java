/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.Base;

import Reika.RotaryCraft.Base.TileEntity.RotaryCraftTileEntity;
import net.minecraft.inventory.Container;

public abstract class GuiNonPoweredMachine extends GuiMachine {
    public GuiNonPoweredMachine(Container par1Container, RotaryCraftTileEntity te) {
        super(par1Container, te);
    }

    @Override
    protected final void drawPowerTab(int j, int k) {}
}
