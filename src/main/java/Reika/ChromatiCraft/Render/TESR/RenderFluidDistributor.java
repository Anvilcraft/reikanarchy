/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Render.TESR;

import Reika.ChromatiCraft.Base.RenderDistributorBase;
import Reika.ChromatiCraft.TileEntity.Transport.TileEntityFluidDistributor;
import Reika.DragonAPI.Interfaces.TileEntity.RenderFetcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;

public class RenderFluidDistributor extends RenderDistributorBase {
    //private final ModelFluidDistributor model = new ModelFluidDistributor();

    @Override
    public String getImageFileName(RenderFetcher te) {
        return "fluiddistrib.png";
    }

    @Override
    public void renderTileEntityAt(
        TileEntity tile, double par2, double par4, double par6, float par8
    ) {
        TileEntityFluidDistributor te = (TileEntityFluidDistributor) tile;

        GL11.glPushMatrix();

        if (MinecraftForgeClient.getRenderPass() == 1 || !te.isInWorld()) {
            this.renderHalo(te, par2, par4, par6, par8);
        }
        GL11.glTranslated(par2, par4, par6);
        //this.renderModel(te, model, te.isInWorld());

        GL11.glPopMatrix();
    }

    @Override
    public int getColor() {
        return 0x2255ff;
    }

    @Override
    protected double pulsationSpeed() {
        return 3D;
    }
}
