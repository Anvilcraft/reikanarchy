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

import java.util.List;

import Reika.DragonAPI.Interfaces.TileEntity.RenderFetcher;
import Reika.DragonAPI.Libraries.ReikaEntityHelper;
import Reika.DragonAPI.Libraries.ReikaInventoryHelper;
import Reika.DragonAPI.Libraries.Rendering.ReikaColorAPI;
import Reika.DragonAPI.Libraries.Rendering.ReikaRenderHelper;
import Reika.RotaryCraft.Auxiliary.IORenderer;
import Reika.RotaryCraft.Base.RotaryTERenderer;
import Reika.RotaryCraft.Base.TileEntity.RotaryCraftTileEntity;
import Reika.RotaryCraft.Models.Animated.ModelRadar;
import Reika.RotaryCraft.Registry.ItemRegistry;
import Reika.RotaryCraft.TileEntities.Surveying.TileEntityMobRadar;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class RenderMobRadar extends RotaryTERenderer {
    private ModelRadar RadarModel = new ModelRadar();

    //private ModelMobRadarV MobRadarModelV = new ModelMobRadarV();

    /**
     * Renders the TileEntity for the position.
     */
    public void renderTileEntityMobRadarAt(
        TileEntityMobRadar tile, double par2, double par4, double par6, float par8
    ) {
        int var9;

        if (!tile.isInWorld())
            var9 = 0;
        else
            var9 = tile.getBlockMetadata();
        ModelRadar var14;
        var14 = RadarModel;
        this.bindTextureByName("/Reika/RotaryCraft/Textures/TileEntityTex/radartex.png");

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) par2, (float) par4 + 2.0F, (float) par6 + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        int var11 = 0; //used to rotate the model about metadata

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
            this.renderTileEntityMobRadarAt(
                (TileEntityMobRadar) tile, par2, par4, par6, par8
            );
        if (((RotaryCraftTileEntity) tile).isInWorld()
            && MinecraftForgeClient.getRenderPass() == 1) {
            IORenderer.renderIO(tile, par2, par4, par6);
            this.renderHUD((TileEntityMobRadar) tile, par2, par4, par6);
        }
    }

    private void renderHUD(TileEntityMobRadar te, double par2, double par4, double par6) {
        if (te == null)
            return;
        if (!te.isInWorld())
            return;
        List<EntityLivingBase> li = te.getEntities();
        if (li == null)
            return;
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer ep = te.getPlacer();
        if (ep == null)
            return;
        if (!ReikaInventoryHelper.checkForItem(
                ItemRegistry.MOTION.getItemInstance(), ep.inventory.mainInventory
            ))
            return;
        if (mc.thePlayer.getCommandSenderName() != ep.getCommandSenderName())
            return;
        ReikaRenderHelper.disableLighting();
        GL11.glPushMatrix();
        //GL11.glTranslatef((float)par2, (float)par4 + 2.0F, (float)par6 + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        Tessellator v5 = Tessellator.instance;
        for (EntityLivingBase ent : li) {
            if (ent != null && ent != mc.thePlayer) {
                float v = ent.height;
                v = 2;
                v5.startDrawing(GL11.GL_LINE_LOOP);
                int color = ReikaEntityHelper.mobToColor(ent);
                int[] rgb = ReikaColorAPI.HexToRGB(color);
                v5.setColorOpaque(rgb[0], rgb[1], rgb[2]);
                v5.addVertex(
                    par2 + ent.posX - te.xCoord,
                    par4 + ent.posY - te.yCoord,
                    par6 + ent.posZ - te.zCoord
                );
                v5.addVertex(
                    par2 + ent.posX - te.xCoord,
                    par4 + ent.posY + v - te.yCoord,
                    par6 + ent.posZ - te.zCoord
                );
                //v5.addVertex(ent.posX, ent.posY+ent.height, ent.posZ);
                v5.draw();
            }
        }
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        ReikaRenderHelper.enableLighting();
    }

    @Override
    public String getImageFileName(RenderFetcher te) {
        return "radartex.png";
    }
}
