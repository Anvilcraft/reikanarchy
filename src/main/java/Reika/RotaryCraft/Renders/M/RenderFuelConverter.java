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
import Reika.DragonAPI.Libraries.Java.ReikaGLHelper.BlendMode;
import Reika.DragonAPI.Libraries.Rendering.ReikaLiquidRenderer;
import Reika.RotaryCraft.Auxiliary.IORenderer;
import Reika.RotaryCraft.Base.RotaryTERenderer;
import Reika.RotaryCraft.Base.TileEntity.RotaryCraftTileEntity;
import Reika.RotaryCraft.ModInterface.ModelFuelConverter;
import Reika.RotaryCraft.TileEntities.Processing.TileEntityFuelConverter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderFuelConverter extends RotaryTERenderer {
    private ModelFuelConverter FuelConverterModel = new ModelFuelConverter();

    /**
     * Renders the TileEntity for the position.
     */
    public void renderTileEntityFuelConverterAt(
        TileEntityFuelConverter tile, double par2, double par4, double par6, float par8
    ) {
        ModelFuelConverter var14;
        var14 = FuelConverterModel;

        this.bindTextureByName(
            "/Reika/RotaryCraft/Textures/TileEntityTex/fuelconverttex.png"
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
            this.renderTileEntityFuelConverterAt(
                (TileEntityFuelConverter) tile, par2, par4, par6, par8
            );
        if (((RotaryCraftTileEntity) tile).isInWorld()
            && MinecraftForgeClient.getRenderPass() == 1) {
            IORenderer.renderIO(tile, par2, par4, par6);
            this.renderFuels((TileEntityFuelConverter) tile, par2, par4, par6);
        }
    }

    private void
    renderFuels(TileEntityFuelConverter tile, double par2, double par4, double par6) {
        for (int i = 0; i <= 1; i++) {
            Fluid f = i == 0 ? tile.getInputFluidType() : tile.getOutputFluidType();
            int amount = i == 0 ? tile.getInputLevel() : tile.getOutputLevel();
            if (f == null)
                continue;
            FluidStack liquid = new FluidStack(f, 1);

            int[] displayList
                = ReikaLiquidRenderer.getGLLists(liquid, tile.worldObj, false);

            if (displayList == null) {
                return;
            }

            GL11.glPushMatrix();
            GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            BlendMode.DEFAULT.apply();

            ReikaLiquidRenderer.bindFluidTexture(f);
            ReikaLiquidRenderer.setFluidColor(liquid);

            GL11.glTranslated(par2, par4, par6);

            GL11.glTranslated(0, tile.getLiquidModelOffset(i == 0), 0);

            GL11.glTranslated(0, 0.001, 0);
            GL11.glScaled(1, 1 / 3D, 1);
            GL11.glScaled(0.99, 0.95, 0.99);

            amount = MathHelper.clamp_int(amount, 0, tile.CAPACITY);
            GL11.glCallList(displayList[(int
            ) (amount / ((double) tile.CAPACITY) * (ReikaLiquidRenderer.LEVELS - 1))]);

            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }
    }

    @Override
    public String getImageFileName(RenderFetcher te) {
        return "fuelconverttex.png";
    }
}
