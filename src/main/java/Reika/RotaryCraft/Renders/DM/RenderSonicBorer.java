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
import Reika.RotaryCraft.Models.ModelSonicBorer;
import Reika.RotaryCraft.TileEntities.World.TileEntitySonicBorer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderSonicBorer extends RotaryTERenderer {
    private ModelSonicBorer SonicBorerModel = new ModelSonicBorer();

    /**
     * Renders the TileEntity for the position.
     */
    public void renderTileEntitySonicBorerAt(
        TileEntitySonicBorer tile, double par2, double par4, double par6, float par8
    ) {
        int var9;

        if (!tile.isInWorld())
            var9 = 0;
        else
            var9 = tile.getBlockMetadata();

        ModelSonicBorer var14;
        var14 = SonicBorerModel;
        //ModelSonicBorerV var15;
        //var14 = this.SonicBorerModelV;
        this.bindTextureByName(
            "/Reika/RotaryCraft/Textures/TileEntityTex/sonicborertex.png"
        );

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
                    var11 = 0;
                    break;
                case 1:
                    var11 = 180;
                    break;
                case 3:
                    var11 = 270;
                    break;
                case 2:
                    var11 = 90;
                    break;
                case 4:
                    var11 = 270;
                    break;
                case 5:
                    var11 = 90;
                    break;
            }

            if (tile.getBlockMetadata() <= 3)
                GL11.glRotatef((float) var11 + 90, 0.0F, 1.0F, 0.0F);
            else {
                GL11.glRotatef(var11, 1F, 0F, 0.0F);
                GL11.glTranslatef(0F, -1F, 1F);
                if (tile.getBlockMetadata() == 5)
                    GL11.glTranslatef(0F, 0F, -2F);
            }
        }
        //float var12 = tile.prevLidAngle + (tile.lidAngle - tile.prevLidAngle) * par8;
        float var13; /*

             var12 = 1.0F - var12;
             var12 = 1.0F - var12 * var12 * var12;*/
        // if (tile.getBlockMetadata() < 4)

        var14.renderAll(
            tile, ReikaJavaLibrary.makeListFrom(tile.getBlockMetadata() < 4), -tile.phi, 0
        );
        // else
        //var15.renderAll(tile, );
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
            this.renderTileEntitySonicBorerAt(
                (TileEntitySonicBorer) tile, par2, par4, par6, par8
            );
        if (((RotaryCraftTileEntity) tile).isInWorld()
            && MinecraftForgeClient.getRenderPass() == 1)
            IORenderer.renderIO(tile, par2, par4, par6);
    }

    @Override
    public String getImageFileName(RenderFetcher te) {
        return "sonicborertex.png";
    }
}
