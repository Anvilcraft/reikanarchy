/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
// Date: 20/08/2014 5:25:01 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package Reika.ChromatiCraft.Models;

import java.util.ArrayList;

import Reika.ChromatiCraft.Base.ChromaModelBase;
import Reika.DragonAPI.Instantiable.Rendering.LODModelPart;
import Reika.DragonAPI.Libraries.Rendering.ReikaRenderHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class ModelItemStand extends ChromaModelBase {
    //fields
    LODModelPart Shape1;
    LODModelPart Shape1a;
    LODModelPart Shapbe1d;
    LODModelPart Shape1c;
    LODModelPart Shape1e;
    LODModelPart Shape2;
    LODModelPart Shape3;
    LODModelPart Shape3a;
    LODModelPart Shape3b;
    LODModelPart Shape3c;
    LODModelPart Shape4;
    LODModelPart Shape4a;
    LODModelPart Shape4b;
    LODModelPart Shape4c;
    LODModelPart Shape3d;
    LODModelPart Shape3e;
    LODModelPart Shape3f;
    LODModelPart Shape3g;

    public ModelItemStand() {
        textureWidth = 128;
        textureHeight = 128;

        Shape1 = new LODModelPart(this, 93, 20);
        Shape1.addBox(-2F, 0F, -2F, 6, 1, 7);
        Shape1.setRotationPoint(-1F, 17F, 3F);
        Shape1.setTextureSize(128, 128);
        Shape1.mirror = true;
        this.setRotation(Shape1, 0.5235988F, 0F, 0F);
        Shape1a = new LODModelPart(this, 0, 20);
        Shape1a.addBox(-4F, 0F, -5F, 6, 1, 7);
        Shape1a.setRotationPoint(1F, 17F, -3F);
        Shape1a.setTextureSize(128, 128);
        Shape1a.mirror = true;
        this.setRotation(Shape1a, -0.5235988F, 0F, 0F);
        Shapbe1d = new LODModelPart(this, 64, 20);
        Shapbe1d.addBox(-2F, 0F, 0F, 7, 1, 6);
        Shapbe1d.setRotationPoint(3F, 17F, -3F);
        Shapbe1d.setTextureSize(128, 128);
        Shapbe1d.mirror = true;
        this.setRotation(Shapbe1d, 0F, 0F, -0.5235988F);
        Shape1c = new LODModelPart(this, 32, 20);
        Shape1c.addBox(-5F, 0F, 0F, 7, 1, 6);
        Shape1c.setRotationPoint(-3F, 17F, -3F);
        Shape1c.setTextureSize(128, 128);
        Shape1c.mirror = true;
        this.setRotation(Shape1c, 0F, 0F, 0.5235988F);
        Shape1e = new LODModelPart(this, 0, 64);
        Shape1e.addBox(0F, 0F, 0F, 6, 1, 6);
        Shape1e.setRotationPoint(-3F, 18F, -3F);
        Shape1e.setTextureSize(128, 128);
        Shape1e.mirror = true;
        this.setRotation(Shape1e, 0F, 0F, 0F);
        Shape2 = new LODModelPart(this, 32, 0);
        Shape2.addBox(0F, 0F, 0F, 16, 1, 16);
        Shape2.setRotationPoint(-8F, 23F, -8F);
        Shape2.setTextureSize(128, 128);
        Shape2.mirror = true;
        this.setRotation(Shape2, 0F, 0F, 0F);
        Shape3 = new LODModelPart(this, 0, 32);
        Shape3.addBox(-7.8F, 0F, -3.5F, 1, 9, 7);
        Shape3.setRotationPoint(0F, 14F, 0F);
        Shape3.setTextureSize(128, 128);
        Shape3.mirror = true;
        this.setRotation(Shape3, 0F, -0.7853982F, 0F);
        Shape3a = new LODModelPart(this, 0, 50);
        Shape3a.addBox(0F, 0F, 0F, 6, 9, 1);
        Shape3a.setRotationPoint(-3F, 14F, -8F);
        Shape3a.setTextureSize(128, 128);
        Shape3a.mirror = true;
        this.setRotation(Shape3a, 0F, 0F, 0F);
        Shape3b = new LODModelPart(this, 0, 50);
        Shape3b.addBox(0F, 0F, 0F, 6, 9, 1);
        Shape3b.setRotationPoint(-3F, 14F, 7F);
        Shape3b.setTextureSize(128, 128);
        Shape3b.mirror = true;
        this.setRotation(Shape3b, 0F, 0F, 0F);
        Shape3c = new LODModelPart(this, 20, 32);
        Shape3c.addBox(0F, 0F, 1F, 1, 9, 6);
        Shape3c.setRotationPoint(7F, 14F, -4F);
        Shape3c.setTextureSize(128, 128);
        Shape3c.mirror = true;
        this.setRotation(Shape3c, 0F, 0F, 0F);
        Shape4 = new LODModelPart(this, 0, 0);
        Shape4.addBox(-4F, -8.2F, -2.4F, 8, 5, 1);
        Shape4.setRotationPoint(0F, 19F, 0F);
        Shape4.setTextureSize(128, 128);
        Shape4.mirror = true;
        this.setRotation(Shape4, 0.7853982F, -2.356194F, 0F);
        Shape4a = new LODModelPart(this, 0, 7);
        Shape4a.addBox(-4F, -8.2F, -2.4F, 8, 5, 1);
        Shape4a.setRotationPoint(0F, 19F, 0F);
        Shape4a.setTextureSize(128, 128);
        Shape4a.mirror = true;
        this.setRotation(Shape4a, 0.7853982F, -0.7853982F, 0F);
        Shape4b = new LODModelPart(this, 0, 73);
        Shape4b.addBox(-4F, -8.2F, -2.4F, 8, 5, 1);
        Shape4b.setRotationPoint(0F, 19F, 0F);
        Shape4b.setTextureSize(128, 128);
        Shape4b.mirror = true;
        this.setRotation(Shape4b, 0.7853982F, 0.7853982F, 0F);
        Shape4c = new LODModelPart(this, 0, 81);
        Shape4c.addBox(-4F, -8.2F, -2.4F, 8, 5, 1);
        Shape4c.setRotationPoint(0F, 19F, 0F);
        Shape4c.setTextureSize(128, 128);
        Shape4c.mirror = true;
        this.setRotation(Shape4c, 0.7853982F, 2.356194F, 0F);
        Shape3d = new LODModelPart(this, 20, 32);
        Shape3d.addBox(0F, 0F, 0F, 1, 9, 6);
        Shape3d.setRotationPoint(-8F, 14F, -3F);
        Shape3d.setTextureSize(128, 128);
        Shape3d.mirror = true;
        this.setRotation(Shape3d, 0F, 0F, 0F);
        Shape3e = new LODModelPart(this, 0, 32);
        Shape3e.addBox(-7.8F, 0F, -3.5F, 1, 9, 7);
        Shape3e.setRotationPoint(0F, 14F, 0F);
        Shape3e.setTextureSize(128, 128);
        Shape3e.mirror = true;
        this.setRotation(Shape3e, 0F, 0.7853982F, 0F);
        Shape3f = new LODModelPart(this, 0, 32);
        Shape3f.addBox(-7.8F, 0F, -3.5F, 1, 9, 7);
        Shape3f.setRotationPoint(0F, 14F, 0F);
        Shape3f.setTextureSize(128, 128);
        Shape3f.mirror = true;
        this.setRotation(Shape3f, 0F, 2.356194F, 0F);
        Shape3g = new LODModelPart(this, 0, 32);
        Shape3g.addBox(-7.8F, 0F, -3.5F, 1, 9, 7);
        Shape3g.setRotationPoint(0F, 14F, 0F);
        Shape3g.setTextureSize(128, 128);
        Shape3g.mirror = true;
        this.setRotation(Shape3g, 0F, -2.356194F, 0F);
    }

    @Override
    public void renderAll(TileEntity te, ArrayList li) {
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        EntityItem item = (EntityItem) li.get(0);
        if (item != null) {
            ReikaRenderHelper.disableEntityLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
        }
        Shape1.render(te, f5);
        Shape1a.render(te, f5);
        Shapbe1d.render(te, f5);
        Shape1c.render(te, f5);
        Shape1e.render(te, f5);

        Shape4.render(te, f5);
        Shape4a.render(te, f5);
        Shape4b.render(te, f5);
        Shape4c.render(te, f5);
        GL11.glPopAttrib();

        Shape2.render(te, f5);

        Shape3.render(te, f5);
        Shape3a.render(te, f5);
        Shape3b.render(te, f5);
        Shape3c.render(te, f5);
        Shape3d.render(te, f5);
        Shape3e.render(te, f5);
        Shape3f.render(te, f5);
        Shape3g.render(te, f5);
    }

    @Override
    public void
    setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5);
    }
}