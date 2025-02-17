/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Render.Item;

import Reika.ChromatiCraft.ChromatiCraft;
import Reika.ChromatiCraft.Magic.Artefact.ArtefactWithDataCrystalAlloyingEffect;
import Reika.ChromatiCraft.Registry.ChromaShaders;
import Reika.ChromatiCraft.Render.TESR.RenderDataNode;
import Reika.DragonAPI.Instantiable.RayTracer;
import Reika.DragonAPI.Instantiable.RayTracer.MultipointChecker;
import Reika.DragonAPI.Instantiable.RayTracer.RayTracerWithCache;
import Reika.DragonAPI.Instantiable.Rendering.MultiSheetItemRenderer;
import Reika.DragonAPI.Libraries.Java.ReikaGLHelper.BlendMode;
import Reika.DragonAPI.Libraries.Rendering.ReikaRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class DataCrystalRenderer
    extends MultiSheetItemRenderer implements MultipointChecker<EntityItem> {
    private final RayTracerWithCache trace
        = RayTracer.getMultipointVisualLOSForRenderCulling(this);

    public DataCrystalRenderer() {
        super(ChromatiCraft.instance, ChromatiCraft.class);
    }

    @Override
    public boolean shouldUseRenderHelper(
        ItemRenderType type, ItemStack item, ItemRendererHelper helper
    ) {
        return helper != helper.ENTITY_ROTATION && helper != helper.ENTITY_BOBBING;
    }

    public boolean isClearLineOfSight(EntityItem ei, RayTracer trace, World world) {
        EntityPlayer ep = Minecraft.getMinecraft().thePlayer;
        double r = 0.5;
        for (double i = -r; i <= r; i += r) {
            for (double k = -r; k <= r; k += r) {
                trace.setOrigins(
                    ei.posX + i, ei.posY, ei.posZ + k, ep.posX, ep.posY, ep.posZ
                );
                if (trace.isClearLineOfSight(world))
                    return true;
            }
        }
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if (type == ItemRenderType.ENTITY) {
            GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
            GL11.glPushMatrix();

            GL11.glEnable(GL11.GL_BLEND);
            BlendMode.ADDITIVEDARK.apply();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            ReikaRenderHelper.disableEntityLighting();
            GL11.glDepthMask(false);

            EntityItem ei = (EntityItem) data[1];
            ei.age = 0;

            double s = 3;
            GL11.glScaled(s, s, s);

            GL11.glTranslated(0, 0.125, 0);
            GL11.glPushMatrix();
            GL11.glRotated(27.5, 0, 0, 1);

            double a = 90;
            if (item.stackTagCompound != null && item.stackTagCompound.hasKey("owner")
                && !ei.onGround) {
                double n = 20D + System.identityHashCode(ei) % 10D;
                a = ei.ticksExisted * 90 / n;
            }

            GL11.glRotated(a, 1, 0, 0);
            double h = 1.25;
            GL11.glTranslated(0, -h / 2, 0);

            boolean artealloy = ei.getEntityData().getBoolean("artealloy");

            RenderDataNode.renderPrism(0, Tessellator.instance, 1, h, 0, artealloy);
            GL11.glPopMatrix();

            if (artealloy || trace.isClearLineOfSight(ei)) {
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glTranslated(0, -0.75, 0);
                if (artealloy) {
                    ChromaShaders.ARTEALLOY$GLOW.setIntensity(
                        Math.min(1, ChromaShaders.ARTEALLOY$GLOW.getIntensity() + 0.2F)
                    );
                    //ChromaShaders.ARTEALLOY$GLOW.lingerTime = 4;
                    ChromaShaders.ARTEALLOY$GLOW.rampDownAmount = 0.008F;
                    ChromaShaders.ARTEALLOY$GLOW.rampDownFactor = 0.99F;
                    ChromaShaders.ARTEALLOY$GLOW.getShader().setFocus(ei);
                    ChromaShaders.ARTEALLOY$GLOW.getShader().setMatricesToCurrent();
                    ChromaShaders.ARTEALLOY$GLOW.getShader().setField(
                        "distance",
                        ei.getDistanceSqToEntity(Minecraft.getMinecraft().thePlayer)
                    );

                    int fire
                        = ArtefactWithDataCrystalAlloyingEffect.instance.getFireTick();
                    ChromaShaders.ARTEALLOY$SHOCK.clearOnRender = true;
                    float f
                        = fire > 0 && fire <= 35 ? (float) Math.pow(fire / 35F, 2) : 0;
                    ChromaShaders.ARTEALLOY$SHOCK.setIntensity(f);
                    ChromaShaders.ARTEALLOY$SHOCK.getShader().setFocus(ei);
                    ChromaShaders.ARTEALLOY$SHOCK.getShader().setMatricesToCurrent();
                    double size = 1 - fire / 35F;
                    double thickness = 0.125;
                    ChromaShaders.ARTEALLOY$SHOCK.getShader().setField("size", size);
                    ChromaShaders.ARTEALLOY$SHOCK.getShader().setField(
                        "thickness", thickness
                    );
                } else {
                    RenderDataNode.renderFlare(Tessellator.instance, 1, true);
                }
            }

            GL11.glPopAttrib();
            GL11.glPopMatrix();
        } else {
            super.renderItem(type, item, data);
        }
    }
}
