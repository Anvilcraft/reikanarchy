/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.Renders.DM;

import Reika.DragonAPI.Interfaces.TileEntity.RenderFetcher;
import Reika.DragonAPI.Libraries.Java.ReikaJavaLibrary;
import Reika.RotaryCraft.Auxiliary.IORenderer;
import Reika.RotaryCraft.Base.RotaryTERenderer;
import Reika.RotaryCraft.Base.TileEntity.RotaryCraftTileEntity;
import Reika.RotaryCraft.Models.Animated.ModelPileDriver;
import Reika.RotaryCraft.TileEntities.World.TileEntityPileDriver;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderPileDriver extends RotaryTERenderer {
    private ModelPileDriver PileDriverModel = new ModelPileDriver();

    /**
     * Renders the TileEntity for the position.
     */
    public void renderTileEntityPileDriverAt(
        TileEntityPileDriver tile, double par2, double par4, double par6, float par8
    ) {
        int var9;

        if (!tile.isInWorld())
            var9 = 0;
        else
            var9 = tile.getBlockMetadata();

        ModelPileDriver var14;
        var14 = PileDriverModel;

        this.bindTextureByName("/Reika/RotaryCraft/Textures/TileEntityTex/piletex.png");

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) par2, (float) par4 + 2.0F, (float) par6 + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        int var11 = 0; //used to rotate the model about metadata

        if (tile.isInWorld()) {
            switch (tile.getBlockMetadata()) {
                case 0:
                    var11 = 90;
                    break;
                case 1:
                    var11 = 0;
                    break;
            }
            GL11.glRotatef(var11, 0.0F, 1.0F, 0.0F);
        }

        float var13;

        var14.renderAll(tile, ReikaJavaLibrary.makeListFrom(tile.power > 0), tile.phi, 0);

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
            this.renderTileEntityPileDriverAt(
                (TileEntityPileDriver) tile, par2, par4, par6, par8
            );
        if (((RotaryCraftTileEntity) tile).isInWorld()
            && MinecraftForgeClient.getRenderPass() == 1)
            IORenderer.renderIO(tile, par2, par4, par6);
    }

    @Override
    public String getImageFileName(RenderFetcher te) {
        return "piletex.png";
    }
}
