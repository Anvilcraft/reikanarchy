/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.Satisforestry.Render;

import Reika.DragonAPI.Base.ISBRH;
import Reika.Satisforestry.Blocks.BlockPinkGrass.GrassTypes;
import Reika.Satisforestry.Blocks.BlockPinkSapling;
import Reika.Satisforestry.Registry.SFBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

public class PinkGrassRenderer extends ISBRH {
    public PinkGrassRenderer(int id) {
        super(id);
    }

    @Override
    public boolean renderWorldBlock(
        IBlockAccess world,
        int x,
        int y,
        int z,
        Block block,
        int modelId,
        RenderBlocks renderer
    ) {
        if (block == SFBlocks.SAPLING.getBlockInstance()) {
            BlockPinkSapling.render(world, x, y, z, renderer);
            return true;
        }
        GrassTypes f = GrassTypes.list[world.getBlockMetadata(x, y, z)];
        Tessellator.instance.setColorOpaque_I(0xffffff);
        f.render(world, x, y, z, block, renderer, Tessellator.instance);
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return false;
    }
}
