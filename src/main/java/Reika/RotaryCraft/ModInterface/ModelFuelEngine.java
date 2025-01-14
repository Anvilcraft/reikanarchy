/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
// Date: 02/11/2013 1:38:38 AM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package Reika.RotaryCraft.ModInterface;

import java.util.ArrayList;

import Reika.DragonAPI.Instantiable.Rendering.LODModelPart;
import Reika.RotaryCraft.Base.RotaryModelBase;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class ModelFuelEngine extends RotaryModelBase {
    //fields
    LODModelPart Shape1;
    LODModelPart Shape1a;
    LODModelPart Shape1b;
    LODModelPart Shape1c;
    LODModelPart Shape2;
    LODModelPart Shape3;
    LODModelPart Shape4;
    LODModelPart Shape4a;
    LODModelPart Shape5;
    LODModelPart Shape5b1;
    LODModelPart Shape5c;
    LODModelPart Shape5d2;
    LODModelPart Shape5e1;
    LODModelPart Shape5f3;
    LODModelPart Shape5g2;
    LODModelPart Shape5h3;
    LODModelPart Shape6k;
    LODModelPart Shape6j;
    LODModelPart Shape4b;
    LODModelPart Shape4c;
    LODModelPart Shape7;
    LODModelPart Shape8;
    LODModelPart Shape9;
    LODModelPart Shape9a;
    LODModelPart Shape10;
    LODModelPart Shape10a;
    LODModelPart Shape10b;
    LODModelPart Shape10c;
    LODModelPart Shape11;
    LODModelPart Shape10d;
    LODModelPart Shape10e;
    LODModelPart Shape10f;
    LODModelPart Shape10g;
    LODModelPart Shape5i;
    LODModelPart Shape5j;

    public ModelFuelEngine() {
        textureWidth = 128;
        textureHeight = 128;

        Shape1 = new LODModelPart(this, 9, 26);
        Shape1.addBox(0F, 0F, -2F, 2, 4, 2);
        Shape1.setRotationPoint(6F, 12F, -7F);
        Shape1.setTextureSize(128, 128);
        Shape1.mirror = true;
        this.setRotation(Shape1, 2.356194F, 0F, 0F);
        Shape1a = new LODModelPart(this, 0, 26);
        Shape1a.addBox(0F, 0F, 0F, 2, 11, 2);
        Shape1a.setRotationPoint(6F, 12F, -7F);
        Shape1a.setTextureSize(128, 128);
        Shape1a.mirror = true;
        this.setRotation(Shape1a, 0F, 0F, 0F);
        Shape1b = new LODModelPart(this, 0, 26);
        Shape1b.addBox(0F, 0F, 0F, 2, 11, 2);
        Shape1b.setRotationPoint(-8F, 12F, -7F);
        Shape1b.setTextureSize(128, 128);
        Shape1b.mirror = true;
        this.setRotation(Shape1b, 0F, 0F, 0F);
        Shape1c = new LODModelPart(this, 9, 26);
        Shape1c.addBox(0F, 0F, -2F, 2, 4, 2);
        Shape1c.setRotationPoint(-8F, 12F, -7F);
        Shape1c.setTextureSize(128, 128);
        Shape1c.mirror = true;
        this.setRotation(Shape1c, 2.356194F, 0F, 0F);
        Shape2 = new LODModelPart(this, 0, 41);
        Shape2.addBox(0F, 0F, 0F, 6, 10, 14);
        Shape2.setRotationPoint(-3F, 13F, -6F);
        Shape2.setTextureSize(128, 128);
        Shape2.mirror = true;
        this.setRotation(Shape2, 0F, 0F, 0F);
        Shape3 = new LODModelPart(this, 41, 0);
        Shape3.addBox(0F, 0F, 0F, 16, 1, 16);
        Shape3.setRotationPoint(-8F, 23F, -8F);
        Shape3.setTextureSize(128, 128);
        Shape3.mirror = true;
        this.setRotation(Shape3, 0F, 0F, 0F);
        Shape4 = new LODModelPart(this, 0, 66);
        Shape4.addBox(-3.5F, 0F, 0F, 7, 8, 13);
        Shape4.setRotationPoint(4.9F, 15.4F, -5.5F);
        Shape4.setTextureSize(128, 128);
        Shape4.mirror = true;
        this.setRotation(Shape4, 0F, 0F, 0.3926991F);
        Shape4a = new LODModelPart(this, 0, 88);
        Shape4a.addBox(-3.5F, -8F, 0F, 7, 8, 13);
        Shape4a.setRotationPoint(0F, 20F, -5.5F);
        Shape4a.setTextureSize(128, 128);
        Shape4a.mirror = true;
        this.setRotation(Shape4a, 0F, 0F, 0.7853982F);
        Shape5 = new LODModelPart(this, 0, 0);
        Shape5.addBox(-2F, -2F, 0F, 4, 11, 14);
        Shape5.setRotationPoint(0F, 14F, -6.4F);
        Shape5.setTextureSize(128, 128);
        Shape5.mirror = true;
        this.setRotation(Shape5, 0F, 0F, 0F);
        Shape5b1 = new LODModelPart(this, 9, 37);
        Shape5b1.addBox(-1F, -1F, 0F, 2, 2, 1);
        Shape5b1.setRotationPoint(0F, 14F, -7.5F);
        Shape5b1.setTextureSize(128, 128);
        Shape5b1.mirror = true;
        this.setRotation(Shape5b1, 0F, 0F, 0F);
        Shape5c = new LODModelPart(this, 0, 110);
        Shape5c.addBox(-2F, -2F, 0F, 4, 4, 14);
        Shape5c.setRotationPoint(0F, 14F, -6.5F);
        Shape5c.setTextureSize(128, 128);
        Shape5c.mirror = true;
        this.setRotation(Shape5c, 0F, 0F, 1.178097F);
        Shape5d2 = new LODModelPart(this, 46, 31);
        Shape5d2.addBox(-1.5F, -1.5F, 0F, 3, 3, 2);
        Shape5d2.setRotationPoint(0F, 17.2F, -7.5F);
        Shape5d2.setTextureSize(128, 128);
        Shape5d2.mirror = true;
        this.setRotation(Shape5d2, 0F, 0F, 0.3926991F);
        Shape5e1 = new LODModelPart(this, 9, 37);
        Shape5e1.addBox(-1F, -1F, 0F, 2, 2, 1);
        Shape5e1.setRotationPoint(0F, 14F, -7.5F);
        Shape5e1.setTextureSize(128, 128);
        Shape5e1.mirror = true;
        this.setRotation(Shape5e1, 0F, 0F, 0.7853982F);
        Shape5f3 = new LODModelPart(this, 69, 19);
        Shape5f3.addBox(-1.5F, -1.5F, 0F, 3, 3, 2);
        Shape5f3.setRotationPoint(0F, 20.8F, -7.5F);
        Shape5f3.setTextureSize(128, 128);
        Shape5f3.mirror = true;
        this.setRotation(Shape5f3, 0F, 0F, 0F);
        Shape5g2 = new LODModelPart(this, 46, 31);
        Shape5g2.addBox(-1.5F, -1.5F, 0F, 3, 3, 2);
        Shape5g2.setRotationPoint(0F, 17.2F, -7.5F);
        Shape5g2.setTextureSize(128, 128);
        Shape5g2.mirror = true;
        this.setRotation(Shape5g2, 0F, 0F, 1.178097F);
        Shape5h3 = new LODModelPart(this, 69, 19);
        Shape5h3.addBox(-1.5F, -1.5F, 0F, 3, 3, 2);
        Shape5h3.setRotationPoint(0F, 20.8F, -7.5F);
        Shape5h3.setTextureSize(128, 128);
        Shape5h3.mirror = true;
        this.setRotation(Shape5h3, 0F, 0F, 0.7853982F);
        Shape6k = new LODModelPart(this, 107, 0);
        Shape6k.addBox(-1F, -1F, 0F, 2, 2, 2);
        Shape6k.setRotationPoint(0F, 15F, 6.5F);
        Shape6k.setTextureSize(128, 128);
        Shape6k.mirror = true;
        this.setRotation(Shape6k, 0F, 0F, 0.7853982F);
        Shape6j = new LODModelPart(this, 107, 0);
        Shape6j.addBox(-1F, -1F, 0F, 2, 2, 2);
        Shape6j.setRotationPoint(0F, 15F, 6.5F);
        Shape6j.setTextureSize(128, 128);
        Shape6j.mirror = true;
        this.setRotation(Shape6j, 0F, 0F, 0F);
        Shape4b = new LODModelPart(this, 0, 88);
        Shape4b.addBox(-3.5F, -8F, 0F, 7, 8, 13);
        Shape4b.setRotationPoint(0F, 20F, -5.5F);
        Shape4b.setTextureSize(128, 128);
        Shape4b.mirror = true;
        this.setRotation(Shape4b, 0F, 0F, -0.7853982F);
        Shape4c = new LODModelPart(this, 0, 66);
        Shape4c.addBox(-3.5F, 0F, 0F, 7, 8, 13);
        Shape4c.setRotationPoint(-4.9F, 15.4F, -5.5F);
        Shape4c.setTextureSize(128, 128);
        Shape4c.mirror = true;
        this.setRotation(Shape4c, 0F, 0F, -0.3926991F);
        Shape7 = new LODModelPart(this, 41, 19);
        Shape7.addBox(0F, 0F, 0F, 12, 3, 1);
        Shape7.setRotationPoint(-6F, 16F, -6.25F);
        Shape7.setTextureSize(128, 128);
        Shape7.mirror = true;
        this.setRotation(Shape7, 0F, 0F, 0F);
        Shape8 = new LODModelPart(this, 46, 24);
        Shape8.addBox(0F, 0F, 0F, 10, 4, 2);
        Shape8.setRotationPoint(-5F, 17.5F, -6F);
        Shape8.setTextureSize(128, 128);
        Shape8.mirror = true;
        this.setRotation(Shape8, 0F, 0F, 0F);
        Shape9 = new LODModelPart(this, 19, 26);
        Shape9.addBox(-2F, 0F, 0F, 4, 1, 9);
        Shape9.setRotationPoint(-6F, 14F, -5F);
        Shape9.setTextureSize(128, 128);
        Shape9.mirror = true;
        this.setRotation(Shape9, 0F, 0F, -0.7853982F);
        Shape9a = new LODModelPart(this, 19, 26);
        Shape9a.addBox(-2F, 0F, 0F, 4, 1, 9);
        Shape9a.setRotationPoint(6F, 14F, -5F);
        Shape9a.setTextureSize(128, 128);
        Shape9a.mirror = true;
        this.setRotation(Shape9a, 0F, 0F, 0.7853982F);
        Shape10 = new LODModelPart(this, 9, 33);
        Shape10.addBox(-1F, 0F, 0F, 2, 1, 1);
        Shape10.setRotationPoint(6.5F, 13.5F, 2F);
        Shape10.setTextureSize(128, 128);
        Shape10.mirror = true;
        this.setRotation(Shape10, 0F, 0F, 0.7853982F);
        Shape10a = new LODModelPart(this, 9, 33);
        Shape10a.addBox(-1F, 0F, 0F, 2, 1, 1);
        Shape10a.setRotationPoint(-6.5F, 13.5F, 2F);
        Shape10a.setTextureSize(128, 128);
        Shape10a.mirror = true;
        this.setRotation(Shape10a, 0F, 0F, -0.7853982F);
        Shape10b = new LODModelPart(this, 9, 33);
        Shape10b.addBox(-1F, 0F, 0F, 2, 1, 1);
        Shape10b.setRotationPoint(6.5F, 13.5F, -2F);
        Shape10b.setTextureSize(128, 128);
        Shape10b.mirror = true;
        this.setRotation(Shape10b, 0F, 0F, 0.7853982F);
        Shape10c = new LODModelPart(this, 9, 33);
        Shape10c.addBox(-1F, 0F, 0F, 2, 1, 1);
        Shape10c.setRotationPoint(6.5F, 13.5F, 0F);
        Shape10c.setTextureSize(128, 128);
        Shape10c.mirror = true;
        this.setRotation(Shape10c, 0F, 0F, 0.7853982F);
        Shape11 = new LODModelPart(this, 42, 38);
        Shape11.addBox(0F, 0F, 0F, 14, 2, 2);
        Shape11.setRotationPoint(-7F, 14F, 5F);
        Shape11.setTextureSize(128, 128);
        Shape11.mirror = true;
        this.setRotation(Shape11, 0F, 0F, 0F);
        Shape10d = new LODModelPart(this, 9, 33);
        Shape10d.addBox(-1F, 0F, 0F, 2, 1, 1);
        Shape10d.setRotationPoint(6.5F, 13.5F, -4F);
        Shape10d.setTextureSize(128, 128);
        Shape10d.mirror = true;
        this.setRotation(Shape10d, 0F, 0F, 0.7853982F);
        Shape10e = new LODModelPart(this, 9, 33);
        Shape10e.addBox(-1F, 0F, 0F, 2, 1, 1);
        Shape10e.setRotationPoint(-6.5F, 13.5F, -4F);
        Shape10e.setTextureSize(128, 128);
        Shape10e.mirror = true;
        this.setRotation(Shape10e, 0F, 0F, -0.7853982F);
        Shape10f = new LODModelPart(this, 9, 33);
        Shape10f.addBox(-1F, 0F, 0F, 2, 1, 1);
        Shape10f.setRotationPoint(-6.5F, 13.5F, -2F);
        Shape10f.setTextureSize(128, 128);
        Shape10f.mirror = true;
        this.setRotation(Shape10f, 0F, 0F, -0.7853982F);
        Shape10g = new LODModelPart(this, 9, 33);
        Shape10g.addBox(-1F, 0F, 0F, 2, 1, 1);
        Shape10g.setRotationPoint(-6.5F, 13.5F, 0F);
        Shape10g.setTextureSize(128, 128);
        Shape10g.mirror = true;
        this.setRotation(Shape10g, 0F, 0F, -0.7853982F);
        Shape5i = new LODModelPart(this, 0, 110);
        Shape5i.addBox(-2F, -2F, 0F, 4, 4, 14);
        Shape5i.setRotationPoint(0F, 14F, -6.5F);
        Shape5i.setTextureSize(128, 128);
        Shape5i.mirror = true;
        this.setRotation(Shape5i, 0F, 0F, 0.7853982F);
        Shape5j = new LODModelPart(this, 0, 110);
        Shape5j.addBox(-2F, -2F, 0F, 4, 4, 14);
        Shape5j.setRotationPoint(0F, 14F, -6.5F);
        Shape5j.setTextureSize(128, 128);
        Shape5j.mirror = true;
        this.setRotation(Shape5j, 0F, 0F, 0.3926991F);
    }

    @Override
    public void renderAll(TileEntity te, ArrayList li, float phi, float theta) {
        Shape1.render(te, f5);
        Shape1a.render(te, f5);
        Shape1b.render(te, f5);
        Shape1c.render(te, f5);
        Shape2.render(te, f5);
        Shape3.render(te, f5);
        Shape4.render(te, f5);
        Shape4a.render(te, f5);
        Shape5.render(te, f5);
        Shape5c.render(te, f5);
        Shape4b.render(te, f5);
        Shape4c.render(te, f5);
        Shape7.render(te, f5);
        Shape8.render(te, f5);
        Shape9.render(te, f5);
        Shape9a.render(te, f5);
        Shape10.render(te, f5);
        Shape10a.render(te, f5);
        Shape10b.render(te, f5);
        Shape10c.render(te, f5);
        Shape11.render(te, f5);
        Shape10d.render(te, f5);
        Shape10e.render(te, f5);
        Shape10f.render(te, f5);
        Shape10g.render(te, f5);
        Shape5i.render(te, f5);
        Shape5j.render(te, f5);

        double d = 0.9375;
        GL11.glTranslated(0, d, 0);
        GL11.glRotatef(phi, 0, 0, 1);
        GL11.glTranslated(0, -d, 0);
        Shape6k.render(te, f5);
        Shape6j.render(te, f5);
        GL11.glTranslated(0, d, 0);
        GL11.glRotatef(-phi, 0, 0, 1);
        GL11.glTranslated(0, -d, 0);

        d = 0.875;
        GL11.glTranslated(0, d, 0);
        GL11.glRotatef(phi, 0, 0, 1);
        GL11.glTranslated(0, -d, 0);
        Shape5b1.render(te, f5);
        Shape5e1.render(te, f5);
        GL11.glTranslated(0, d, 0);
        GL11.glRotatef(-phi, 0, 0, 1);
        GL11.glTranslated(0, -d, 0);

        d = 1.075;
        GL11.glTranslated(0, d, 0);
        GL11.glRotatef(-phi, 0, 0, 1);
        GL11.glTranslated(0, -d, 0);
        Shape5d2.render(te, f5);
        Shape5g2.render(te, f5);
        GL11.glTranslated(0, d, 0);
        GL11.glRotatef(phi, 0, 0, 1);
        GL11.glTranslated(0, -d, 0);

        d = 1.3;
        GL11.glTranslated(0, d, 0);
        GL11.glRotatef(phi, 0, 0, 1);
        GL11.glTranslated(0, -d, 0);
        Shape5f3.render(te, f5);
        Shape5h3.render(te, f5);
        GL11.glTranslated(0, d, 0);
        GL11.glRotatef(phi, 0, 0, 1);
        GL11.glTranslated(0, -d, 0);
    }

    @Override
    public void
    setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5);
    }
}
