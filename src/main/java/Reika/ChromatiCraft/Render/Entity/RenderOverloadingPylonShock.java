/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Render.Entity;

import Reika.ChromatiCraft.Entity.EntityOverloadingPylonShock;
import Reika.ChromatiCraft.Registry.ChromaIcons;
import Reika.DragonAPI.Libraries.IO.ReikaTextureHelper;
import Reika.DragonAPI.Libraries.Java.ReikaGLHelper.BlendMode;
import Reika.DragonAPI.Libraries.MathSci.ReikaPhysicsHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderOverloadingPylonShock extends Render {
    @Override
    public void
    doRender(Entity e, double par2, double par4, double par6, float par8, float ptick) {
        ReikaTextureHelper.bindTerrainTexture();
        EntityOverloadingPylonShock eb = (EntityOverloadingPylonShock) e;
        Tessellator v5 = Tessellator.instance;
        IIcon icon = ChromaIcons.FLARE.getIcon();
        float u = icon.getMinU();
        float v = icon.getMinV();
        float du = icon.getMaxU();
        float dv = icon.getMaxV();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LIGHTING);
        BlendMode.ADDITIVEDARK.apply();
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glDepthMask(false);
        GL11.glTranslated(par2, par4, par6);
        if (!e.isDead) {
            RenderManager rm = RenderManager.instance;
            double dx = e.posX - RenderManager.renderPosX;
            double dy = e.posY - RenderManager.renderPosY;
            double dz = e.posZ - RenderManager.renderPosZ;
            double[] angs = ReikaPhysicsHelper.cartesianToPolar(dx, dy, dz);
            GL11.glRotated(angs[2], 0, 1, 0);
            GL11.glRotated(90 - angs[1], 1, 0, 0);
        }
        //GL11.glRotatef(rm.playerViewX, 1.0F, 0.0F, 0.0F);
        v5.startDrawingQuads();
        v5.setBrightness(240);
        double s1 = 1.5;
        double d = 0.001;
        int c1 = 0xffffff; //eb.getRenderColor();
        v5.setColorOpaque_I(c1);
        v5.addVertexWithUV(-s1, -s1, 0, u, v);
        v5.addVertexWithUV(s1, -s1, 0, du, v);
        v5.addVertexWithUV(s1, s1, 0, du, dv);
        v5.addVertexWithUV(-s1, s1, 0, u, dv);
        v5.draw();
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity e) {
        return null;
    }
}
