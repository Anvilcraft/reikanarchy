/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ReactorCraft.Renders;

import Reika.DragonAPI.Interfaces.TileEntity.RenderFetcher;
import Reika.ReactorCraft.Base.ReactorRenderBase;
import Reika.ReactorCraft.Base.TileEntityReactorBase;
import Reika.ReactorCraft.Models.ModelSolarExchanger;
import Reika.ReactorCraft.TileEntities.PowerGen.TileEntitySolarExchanger;
import Reika.RotaryCraft.Auxiliary.IORenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderSolarExchanger extends ReactorRenderBase {
    private ModelSolarExchanger ExchangerModel = new ModelSolarExchanger();

    /**
     * Renders the TileEntity for the position.
     */
    public void renderTileEntitySolarExchangerAt(
        TileEntitySolarExchanger tile, double par2, double par4, double par6, float par8
    ) {
        ModelSolarExchanger var14;
        var14 = ExchangerModel;

        this.bindTextureByName("/Reika/ReactorCraft/Textures/TileEntity/solar.png");

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) par2, (float) par4 + 2.0F, (float) par6 + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        int var11 = 0;
        float var13;

        var14.renderAll(tile, null);

        if (tile.isInWorld())
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void renderTileEntityAt(
        TileEntity tile, double par2, double par4, double par6, float par8
    ) {
        if (this.doRenderModel((TileEntityReactorBase) tile))
            this.renderTileEntitySolarExchangerAt(
                (TileEntitySolarExchanger) tile, par2, par4, par6, par8
            );
        if (((TileEntityReactorBase) tile).isInWorld()
            && MinecraftForgeClient.getRenderPass() == 1) {
            IORenderer.renderIO(tile, par2, par4, par6);
            //IOAPI.renderIO((ShaftMachine)tile, par2, par4, par6);
        }
    }

    @Override
    public String getImageFileName(RenderFetcher te) {
        return "solar.png";
    }
}
