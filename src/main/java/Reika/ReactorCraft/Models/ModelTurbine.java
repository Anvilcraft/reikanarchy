/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
// Date: 24/09/2013 9:40:19 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package Reika.ReactorCraft.Models;

import java.util.ArrayList;

import Reika.DragonAPI.Instantiable.Rendering.LODModelPart;
import Reika.DragonAPI.Libraries.Rendering.ReikaRenderHelper;
import Reika.RotaryCraft.Auxiliary.OldTextureLoader;
import Reika.RotaryCraft.Base.RotaryModelBase;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class ModelTurbine extends RotaryModelBase {
    private final LODModelPart shaft1;
    private final LODModelPart shaft1a;
    private final LODModelPart blade;

    protected final int stage;

    private int compiledList = -1;

    private EntityVillager villagerRender;

    public ModelTurbine(int stage) {
        textureWidth = 128;
        textureHeight = 128;
        this.stage = stage;

        shaft1 = new LODModelPart(this, 0, 106);
        shaft1a = new LODModelPart(this, 0, 106);
        blade = new LODModelPart(this, 0, 0);
        this.init();

        this.setCompilable(false);
    }

    private void init() {
        shaft1.addBox(-2F, -2F, 0F, 4, 4, 16);
        shaft1.setRotationPoint(0F, 15F, -8F);
        shaft1.setTextureSize(128, 128);
        shaft1.mirror = true;
        this.setRotation(shaft1, 0F, 0F, 0.7853982F);
        shaft1a.addBox(-2F, -2F, 0F, 4, 4, 16);
        shaft1a.setRotationPoint(0F, 15F, -8F);
        shaft1a.setTextureSize(128, 128);
        shaft1a.mirror = true;
        this.setRotation(shaft1a, 0F, 0F, 0F);

        blade.addBox(
            -0.5F,
            -this.getBladeLength(),
            -this.getBladeWidth() / 2F,
            1,
            this.getBladeLength(),
            this.getBladeWidth()
        );
        blade.setRotationPoint(0F, 15F, 0F);
        blade.setTextureSize(128, 128);
        blade.mirror = true;
        this.setRotation(blade, 0F, 0F, 0F);
    }

    private void clear() {
        shaft1.cubeList.clear();
        shaft1a.cubeList.clear();
        blade.cubeList.clear();
    }

    protected final void reset() {
        this.clear();
        this.init();
    }

    @Override
    public void renderAll(TileEntity te, ArrayList li, float phi, float theta) {
        double vo = 0.9375;

        int damage = li != null ? (Integer) li.get(0) : 0;

        GL11.glPushMatrix();
        GL11.glTranslated(0, vo, 0);
        GL11.glRotatef(phi, 0, 0, 1);
        GL11.glTranslated(0, -vo, 0);

        if (false && damage == 0) {
            if (compiledList == -1 || GuiScreen.isCtrlKeyDown()) {
                compiledList = GLAllocation.generateDisplayLists(1);
                GL11.glNewList(compiledList, GL11.GL_COMPILE);
                this.renderModelDirect(te, 0, vo);
                GL11.glEndList();
            }
            GL11.glCallList(compiledList);
        } else {
            this.renderModelDirect(te, damage, vo);
        }

        GL11.glPopMatrix();
    }

    private void renderModelDirect(TileEntity te, int damage, double vo) {
        //double sep = 10;
        double dd = 0.25;
        double sc = this.getScaleFactor();

        shaft1.render(te, f5);
        shaft1a.render(te, f5);

        if (this.renderTwoStages())
            GL11.glTranslated(0, 0, dd);

        this.renderBlades(te, damage, vo, 0);

        if (this.renderTwoStages()) {
            GL11.glTranslated(0, 0, -dd * 2);

            GL11.glTranslated(0, vo, 0);
            GL11.glScaled(sc, sc, 1);
            GL11.glTranslated(0, -vo, 0);

            this.renderBlades(te, damage, vo, 0);

            GL11.glTranslated(0, vo, 0);
            GL11.glScaled(1D / sc, 1D / sc, 1);
            GL11.glTranslated(0, -vo, 0);

            GL11.glTranslated(0, 0, dd);
        }
    }

    protected boolean renderTwoStages() {
        return true;
    }

    private void renderBlades(TileEntity te, int damage, double vo, float phi) {
        double da = this.getAngularSeparation() * (damage + 1);
        for (int i = 0; i < 360; i += da) {
            GL11.glPushMatrix();
            GL11.glTranslated(0, vo, 0);
            GL11.glRotatef(i + phi, 0, 0, 1);
            GL11.glTranslated(0, -vo, 0);
            GL11.glRotatef(-this.getBladeTwist(), 0, 1, 0);
            if (te.hasWorldObj() && OldTextureLoader.instance.loadOldTextures()) {
                if (villagerRender == null)
                    villagerRender = new EntityVillager(te.worldObj);
                //RenderManager.instance.renderEntitySimple(villagerRender,
                //ReikaRenderHelper.getPartialTickTime());
                int n = 2;
                switch (stage) {
                    case 0:
                        n = 5;
                        break;
                    case 1:
                        n = 4;
                        break;
                    case 2:
                        n = 3;
                        break;
                }
                if ((i / da) % n == 0) {
                    double s = this.getBladeLength() / 20F;
                    if (stage <= 1) {
                        s *= -0.75;
                        GL11.glTranslated(0, 1.375, 0);
                    }
                    GL11.glScaled(1, s, 1);
                    RenderManager.instance.renderEntityWithPosYaw(
                        villagerRender, 0, 0, 0, 0, ReikaRenderHelper.getPartialTickTime()
                    );
                }
            } else {
                blade.render(te, f5);
            }
            GL11.glPopMatrix();
        }
    }

    public int getBladeLength() {
        switch (stage) {
            case 0:
                return 16;
            case 1:
                return 24;
            case 2:
                return 28;
            case 3:
                return 33;
            case 4:
                return 40;
        }
        return 4;
    }

    public int getAngularSeparation() {
        switch (stage) {
            case 0:
                return 8;
            case 1:
                return 5;
            case 2:
                return 8;
            case 3:
                return 8;
            case 4:
                return 8;
            case 5:
                return 9;
            case 6:
                return 10;
        }
        return 10;
    }

    public int getBladeTwist() {
        switch (stage) {
            case 0:
                return 10;
            case 1:
                return 15;
            case 2:
                return 20;
            case 3:
                return 30;
            case 4:
                return 45;
            case 5:
                return 45;
            case 6:
                return 50;
        }
        return 10;
    }

    public int getBladeWidth() {
        switch (stage) {
            case 0:
                return 2;
            case 1:
                return 2;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 6;
            case 6:
                return 8;
        }
        return 2;
    }

    public double getScaleFactor() {
        if (stage < 1)
            return 1.3;
        else
            return 1.1;
    }
}
