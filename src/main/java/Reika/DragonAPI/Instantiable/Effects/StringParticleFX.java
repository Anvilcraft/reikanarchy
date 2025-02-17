/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.Instantiable.Effects;

import Reika.DragonAPI.Libraries.IO.ReikaTextureHelper;
import Reika.DragonAPI.Libraries.Java.ReikaGLHelper.BlendMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class StringParticleFX extends EntityFX {
    private final String string;

    public StringParticleFX(World world, double x, double y, double z, String s) {
        super(world, x, y, z);
        string = s;
        particleGravity = 0.225F;
        motionY = 0.18;
        particleMaxAge = 15;
        particleScale = 0.05F;
        noClip = true;
    }

    public void setScale(float f) {
        particleScale = f;
        motionY *= f / 0.025;
    }

    public void setLife(int f) {
        particleMaxAge = f;
    }

    @Override
    public void renderParticle(
        Tessellator v5,
        float par2,
        float par3,
        float par4,
        float par5,
        float par6,
        float par7
    ) {
        rotationYaw = (-Minecraft.getMinecraft().thePlayer.rotationYaw);
        rotationPitch = Minecraft.getMinecraft().thePlayer.rotationPitch;
        boolean depth = true;
        boolean shadow = false;
        float locX = 0;
        float locY = 0;
        float locZ = 0;
        locX = ((float) (prevPosX + (posX - prevPosX) * par2 - interpPosX));
        locY = ((float) (prevPosY + (posY - prevPosY) * par2 - interpPosY));
        locZ = ((float) (prevPosZ + (posZ - prevPosZ) * par2 - interpPosZ));
        GL11.glPushMatrix();

        motionY = Math.max(motionY, 0);

        if (!depth)
            GL11.glDisable(GL11.GL_DEPTH_TEST);

        GL11.glTranslatef(locX, locY, locZ);
        GL11.glRotatef(rotationYaw, 0, 1, 0);
        GL11.glRotatef(rotationPitch, 1, 0, 0);

        GL11.glScalef(-1, -1, 1);
        GL11.glScaled(particleScale, particleScale, particleScale);
        FontRenderer f = Minecraft.getMinecraft().fontRenderer;
        OpenGlHelper.setLightmapTextureCoords(
            OpenGlHelper.lightmapTexUnit, 240, 0.003662109F
        );
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        BlendMode.DEFAULT.apply();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1, 1, 1, 1);
        int color = 0xffffff;
        int w = -MathHelper.floor_float(f.getStringWidth(string) / 2.0F);
        int h = -MathHelper.floor_float(f.FONT_HEIGHT / 2F);
        if (shadow)
            f.drawStringWithShadow(string, 1 + w, 1 + h, color);
        else
            f.drawString(string, w, h, color);

        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        GL11.glPopMatrix();
        ReikaTextureHelper.bindParticleTexture();
    }
}
