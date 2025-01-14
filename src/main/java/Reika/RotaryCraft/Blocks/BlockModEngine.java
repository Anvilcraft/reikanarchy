/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.Blocks;

import Reika.RotaryCraft.Base.BlockModelledMultiTE;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class BlockModEngine extends BlockModelledMultiTE {
    public BlockModEngine(Material mat) {
        super(mat);
    }

    public static int getDirectionMetadataFromPlayerLook(EntityLivingBase ep) {
        int i = MathHelper.floor_double((ep.rotationYaw * 4F) / 360F + 0.5D);
        while (i > 3)
            i -= 4;
        while (i < 0)
            i += 4;
        switch (i) {
            case 0:
                return 2;
            case 1:
                return 1;
            case 2:
                return 0;
            case 3:
                return 3;
        }
        return -1;
    }
}
