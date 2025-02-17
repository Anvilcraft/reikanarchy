/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.Renders.M;

import Reika.DragonAPI.Interfaces.TileEntity.RenderFetcher;
import Reika.RotaryCraft.Base.RotaryTERenderer;
import Reika.RotaryCraft.Base.TileEntity.RotaryCraftTileEntity;
import Reika.RotaryCraft.Models.Animated.ModelLawnSprinkler;
import Reika.RotaryCraft.TileEntities.Farming.TileEntityLawnSprinkler;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderLawnSprinkler extends RotaryTERenderer {
    private ModelLawnSprinkler LawnSprinklerModel = new ModelLawnSprinkler();

    /**
     * Renders the TileEntity for the position.
     */
    public void renderTileEntityLawnSprinklerAt(
        TileEntityLawnSprinkler tile, double par2, double par4, double par6, float par8
    ) {
        ModelLawnSprinkler var14;
        var14 = LawnSprinklerModel;

        this.bindTextureByName(
            "/Reika/RotaryCraft/Textures/TileEntityTex/lawnsprinklertex.png"
        );

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) par2, (float) par4 + 2.0F, (float) par6 + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        int var11 = 0;
        float var13;

        var14.renderAll(tile, null, -tile.phi);

        if (tile.isInWorld())
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void renderTileEntityAt(
        TileEntity tile, double par2, double par4, double par6, float par8
    ) {
        if (this.doRenderModel((RotaryCraftTileEntity) tile))
            this.renderTileEntityLawnSprinklerAt(
                (TileEntityLawnSprinkler) tile, par2, par4, par6, par8
            );
    }

    @Override
    public String getImageFileName(RenderFetcher te) {
        return "lawnsprinklertex.png";
    }
}
