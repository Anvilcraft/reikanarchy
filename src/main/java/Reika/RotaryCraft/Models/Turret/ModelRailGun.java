/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
// Date: 03/05/2013 2:21:26 AM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package Reika.RotaryCraft.Models.Turret;

import java.util.ArrayList;

import Reika.DragonAPI.Instantiable.Rendering.LODModelPart;
import Reika.RotaryCraft.Base.RotaryModelBase;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class ModelRailGun extends RotaryModelBase {
    //fields
    LODModelPart Shape1;
    LODModelPart Shape1a;
    LODModelPart Shape2;
    LODModelPart Shape2a;
    LODModelPart Shape3;
    LODModelPart Shape4;
    LODModelPart Shape4a;
    LODModelPart Shape5;
    LODModelPart Shape6;
    LODModelPart Shape7;
    LODModelPart Shape7a;
    LODModelPart Shape7b;
    LODModelPart Shape7c;
    LODModelPart Shape7d;
    LODModelPart Shape7e;
    LODModelPart Shape7f;
    LODModelPart Shape7g;
    LODModelPart Shape8;
    LODModelPart Shape8a;
    LODModelPart Shape9;
    LODModelPart Shape2c;
    LODModelPart Shape2b;
    LODModelPart Shape2a1;
    LODModelPart Shape2a2;
    LODModelPart Shape3d;

    public ModelRailGun() {
        textureWidth = 128;
        textureHeight = 128;

        Shape1 = new LODModelPart(this, 0, 81);
        Shape1.addBox(-0.5F, 1.5F, -14F, 1, 2, 14);
        Shape1.setRotationPoint(0F, 11.5F, 6F);
        Shape1.setTextureSize(128, 128);
        Shape1.mirror = true;
        this.setRotation(Shape1, 0F, 0F, 0F);
        Shape1a = new LODModelPart(this, 0, 64);
        Shape1a.addBox(-0.5F, -3.5F, -14F, 1, 2, 14);
        Shape1a.setRotationPoint(0F, 11.5F, 6F);
        Shape1a.setTextureSize(128, 128);
        Shape1a.mirror = true;
        this.setRotation(Shape1a, 0F, 0F, 0F);
        Shape2 = new LODModelPart(this, 65, 48);
        Shape2.addBox(-4F, -0.5F, -14F, 2, 1, 14);
        Shape2.setRotationPoint(0F, 11.5F, 6F);
        Shape2.setTextureSize(128, 128);
        Shape2.mirror = true;
        this.setRotation(Shape2, 0F, 0F, -0.7853982F);
        Shape2a = new LODModelPart(this, 65, 64);
        Shape2a.addBox(2F, -0.5F, -14F, 2, 1, 14);
        Shape2a.setRotationPoint(0F, 11.5F, 6F);
        Shape2a.setTextureSize(128, 128);
        Shape2a.mirror = true;
        this.setRotation(Shape2a, 0F, 0F, 0.7853982F);
        Shape3 = new LODModelPart(this, 0, 57);
        Shape3.addBox(-4F, -2.5F, 2F, 8, 5, 1);
        Shape3.setRotationPoint(0F, 11.5F, 6F);
        Shape3.setTextureSize(128, 128);
        Shape3.mirror = true;
        this.setRotation(Shape3, 0F, 0F, 0F);
        Shape4 = new LODModelPart(this, 25, 47);
        Shape4.addBox(-1F, -1F, -1F, 2, 2, 2);
        Shape4.setRotationPoint(0F, 11.5F, 5F);
        Shape4.setTextureSize(128, 128);
        Shape4.mirror = true;
        this.setRotation(Shape4, 0.3490659F, 0.7853982F, 0.3490659F);
        Shape4a = new LODModelPart(this, 25, 52);
        Shape4a.addBox(-1F, -1F, -1F, 2, 2, 2);
        Shape4a.setRotationPoint(0F, 11.5F, 5F);
        Shape4a.setTextureSize(128, 128);
        Shape4a.mirror = true;
        this.setRotation(Shape4a, 0F, 0F, 0F);
        Shape5 = new LODModelPart(this, 0, 16);
        Shape5.addBox(0F, 0F, 0F, 16, 1, 16);
        Shape5.setRotationPoint(-8F, 23F, -8F);
        Shape5.setTextureSize(128, 128);
        Shape5.mirror = true;
        this.setRotation(Shape5, 0F, 0F, 0F);
        Shape6 = new LODModelPart(this, 0, 34);
        Shape6.addBox(-5F, 0F, -5F, 10, 2, 10);
        Shape6.setRotationPoint(0F, 20F, 0F);
        Shape6.setTextureSize(128, 128);
        Shape6.mirror = true;
        this.setRotation(Shape6, 0F, 0F, 0F);
        Shape7 = new LODModelPart(this, 0, 0);
        Shape7.addBox(-6F, 0F, -6F, 12, 1, 12);
        Shape7.setRotationPoint(0F, 22F, 0F);
        Shape7.setTextureSize(128, 128);
        Shape7.mirror = true;
        this.setRotation(Shape7, 0F, 0.7853982F, 0F);
        Shape7a = new LODModelPart(this, 0, 0);
        Shape7a.addBox(-6F, 0F, -6F, 12, 1, 12);
        Shape7a.setRotationPoint(0F, 22F, 0F);
        Shape7a.setTextureSize(128, 128);
        Shape7a.mirror = true;
        this.setRotation(Shape7a, 0F, 0.3926991F, 0F);
        Shape7b = new LODModelPart(this, 0, 0);
        Shape7b.addBox(-6F, 0F, -6F, 12, 1, 12);
        Shape7b.setRotationPoint(0F, 22F, 0F);
        Shape7b.setTextureSize(128, 128);
        Shape7b.mirror = true;
        this.setRotation(Shape7b, 0F, 1.178097F, 0F);
        Shape7c = new LODModelPart(this, 0, 0);
        Shape7c.addBox(-6F, 0F, -6F, 12, 1, 12);
        Shape7c.setRotationPoint(0F, 22F, 0F);
        Shape7c.setTextureSize(128, 128);
        Shape7c.mirror = true;
        this.setRotation(Shape7c, 0F, 0.1963495F, 0F);
        Shape7d = new LODModelPart(this, 0, 0);
        Shape7d.addBox(-6F, 0F, -6F, 12, 1, 12);
        Shape7d.setRotationPoint(0F, 22F, 0F);
        Shape7d.setTextureSize(128, 128);
        Shape7d.mirror = true;
        this.setRotation(Shape7d, 0F, -0.1963495F, 0F);
        Shape7e = new LODModelPart(this, 0, 0);
        Shape7e.addBox(-6F, 0F, -6F, 12, 1, 12);
        Shape7e.setRotationPoint(0F, 22F, 0F);
        Shape7e.setTextureSize(128, 128);
        Shape7e.mirror = true;
        this.setRotation(Shape7e, 0F, 0.9817477F, 0F);
        Shape7f = new LODModelPart(this, 0, 0);
        Shape7f.addBox(-6F, 0F, -6F, 12, 1, 12);
        Shape7f.setRotationPoint(0F, 22F, 0F);
        Shape7f.setTextureSize(128, 128);
        Shape7f.mirror = true;
        this.setRotation(Shape7f, 0F, 0.5890486F, 0F);
        Shape7g = new LODModelPart(this, 0, 0);
        Shape7g.addBox(-6F, 0F, -6F, 12, 1, 12);
        Shape7g.setRotationPoint(0F, 22F, 0F);
        Shape7g.setTextureSize(128, 128);
        Shape7g.mirror = true;
        this.setRotation(Shape7g, 0F, 0F, 0F);
        Shape8 = new LODModelPart(this, 0, 98);
        Shape8.addBox(-4F, 0F, 0F, 2, 1, 9);
        Shape8.setRotationPoint(0F, 20F, 0F);
        Shape8.setTextureSize(128, 128);
        Shape8.mirror = true;
        this.setRotation(Shape8, 0.7853982F, 0F, 0F);
        Shape8a = new LODModelPart(this, 0, 98);
        Shape8a.addBox(2F, 0F, 0F, 2, 1, 9);
        Shape8a.setRotationPoint(0F, 20F, 0F);
        Shape8a.setTextureSize(128, 128);
        Shape8a.mirror = true;
        this.setRotation(Shape8a, 0.7853982F, 0F, 0F);
        Shape9 = new LODModelPart(this, 0, 109);
        Shape9.addBox(-2F, 0F, 0F, 4, 1, 7);
        Shape9.setRotationPoint(0F, 21F, 4F);
        Shape9.setTextureSize(128, 128);
        Shape9.mirror = true;
        this.setRotation(Shape9, 1.186824F, 0F, 0F);
        Shape2c = new LODModelPart(this, 49, 0);
        Shape2c.addBox(-4F, -0.5F, -14F, 2, 1, 14);
        Shape2c.setRotationPoint(0F, 11.5F, 6F);
        Shape2c.setTextureSize(128, 128);
        Shape2c.mirror = true;
        this.setRotation(Shape2c, 0F, 0F, 0F);
        Shape2b = new LODModelPart(this, 65, 16);
        Shape2b.addBox(-4F, -0.5F, -14F, 2, 1, 14);
        Shape2b.setRotationPoint(0F, 11.5F, 6F);
        Shape2b.setTextureSize(128, 128);
        Shape2b.mirror = true;
        this.setRotation(Shape2b, 0F, 0F, 0.7853982F);
        Shape2a1 = new LODModelPart(this, 65, 32);
        Shape2a1.addBox(2F, -0.5F, -14F, 2, 1, 14);
        Shape2a1.setRotationPoint(0F, 11.5F, 6F);
        Shape2a1.setTextureSize(128, 128);
        Shape2a1.mirror = true;
        this.setRotation(Shape2a1, 0F, 0F, -0.7853982F);
        Shape2a2 = new LODModelPart(this, 82, 0);
        Shape2a2.addBox(2F, -0.5F, -14F, 2, 1, 14);
        Shape2a2.setRotationPoint(0F, 11.5F, 6F);
        Shape2a2.setTextureSize(128, 128);
        Shape2a2.mirror = true;
        this.setRotation(Shape2a2, 0F, 0F, 0F);
        Shape3d = new LODModelPart(this, 0, 47);
        Shape3d.addBox(-5F, -3.5F, 0F, 10, 7, 2);
        Shape3d.setRotationPoint(0F, 11.5F, 6F);
        Shape3d.setTextureSize(128, 128);
        Shape3d.mirror = true;
        this.setRotation(Shape3d, 0F, 0F, 0F);
    }

    @Override
    public void renderAll(TileEntity te, ArrayList li, float phi, float theta) {
        Shape5.render(te, f5);

        GL11.glPushMatrix();

        GL11.glTranslated(0, 1, 0);
        GL11.glRotatef(phi, 0, 1, 0);
        GL11.glTranslated(0, -1, 0);
        Shape6.render(te, f5);
        Shape7.render(te, f5);
        Shape7a.render(te, f5);
        Shape7b.render(te, f5);
        Shape7c.render(te, f5);
        Shape7d.render(te, f5);
        Shape7e.render(te, f5);
        Shape7f.render(te, f5);
        Shape7g.render(te, f5);

        GL11.glTranslated(0, 1, 0);
        GL11.glRotatef(theta, 1, 0, 0);
        GL11.glTranslated(0, -1, 0);
        Shape8.render(te, f5);
        Shape8a.render(te, f5);
        Shape9.render(te, f5);
        Shape3d.render(te, f5);
        Shape3.render(te, f5);
        Shape2a2.render(te, f5);
        Shape2a.render(te, f5);
        Shape1.render(te, f5);
        Shape1a.render(te, f5);
        Shape2.render(te, f5);
        Shape2c.render(te, f5);
        Shape2b.render(te, f5);
        Shape2a1.render(te, f5);
        Shape4.render(te, f5);
        Shape4a.render(te, f5);

        GL11.glPopMatrix();
    }

    @Override
    public void
    setRotationAngles(float f, float f1, float f2, float f3, float f4, float f6) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5);
    }
}