/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
// Date: 27/07/2013 5:22:52 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package Reika.RotaryCraft.ModInterface.Conversion;

import java.util.ArrayList;

import Reika.DragonAPI.Instantiable.Rendering.LODModelPart;
import Reika.RotaryCraft.Base.RotaryModelBase;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class ModelPneumatic extends RotaryModelBase {
    //fields
    LODModelPart Shape1;
    LODModelPart Shape1a;
    LODModelPart Shape2;
    LODModelPart Shape3;
    LODModelPart Shape4;
    LODModelPart Shape4a;
    LODModelPart Shape4b;
    LODModelPart Shape5;
    LODModelPart Shape4c;
    LODModelPart Shape4d;
    LODModelPart Shape6;
    LODModelPart Shape6a;
    LODModelPart Shape6b;
    LODModelPart Shape6c;
    LODModelPart Shape6d;
    LODModelPart Shape7;
    LODModelPart Shape7a;
    LODModelPart Shape8;
    LODModelPart Shape8a;
    LODModelPart Shape9;
    LODModelPart Shape8b;
    LODModelPart Shape8c;
    LODModelPart Shape9a;
    LODModelPart Shape8d;
    LODModelPart Shape6e;
    LODModelPart Shape8e;

    public ModelPneumatic() {
        textureWidth = 128;
        textureHeight = 128;

        Shape1 = new LODModelPart(this, 87, 8);
        Shape1.addBox(0F, -1F, -1F, 14, 2, 2);
        Shape1.setRotationPoint(-8.5F, 16F, 0F);
        Shape1.setTextureSize(64, 32);
        Shape1.mirror = true;
        this.setRotation(Shape1, 0.7853982F, 0F, 0F);
        Shape1a = new LODModelPart(this, 87, 8);
        Shape1a.addBox(0F, -1F, -1F, 14, 2, 2);
        Shape1a.setRotationPoint(-8.5F, 16F, 0F);
        Shape1a.setTextureSize(64, 32);
        Shape1a.mirror = true;
        this.setRotation(Shape1a, 0F, 0F, 0F);
        Shape2 = new LODModelPart(this, 0, 63);
        Shape2.addBox(0F, 0F, 0F, 16, 1, 16);
        Shape2.setRotationPoint(-8F, 23F, -8F);
        Shape2.setTextureSize(128, 128);
        Shape2.mirror = true;
        this.setRotation(Shape2, 0F, 0F, 0F);
        Shape3 = new LODModelPart(this, 0, 46);
        Shape3.addBox(0F, 0F, 0F, 15, 1, 14);
        Shape3.setRotationPoint(-7F, 22F, -7F);
        Shape3.setTextureSize(128, 128);
        Shape3.mirror = true;
        this.setRotation(Shape3, 0F, 0F, 0F);
        Shape4 = new LODModelPart(this, 0, 0);
        Shape4.addBox(0F, 0F, 0F, 1, 9, 1);
        Shape4.setRotationPoint(-5F, 13F, -6F);
        Shape4.setTextureSize(128, 128);
        Shape4.mirror = true;
        this.setRotation(Shape4, 0F, 0F, 0F);
        Shape4a = new LODModelPart(this, 0, 0);
        Shape4a.addBox(0F, 0F, 0F, 1, 9, 1);
        Shape4a.setRotationPoint(-3F, 13F, -6F);
        Shape4a.setTextureSize(128, 128);
        Shape4a.mirror = true;
        this.setRotation(Shape4a, 0F, 0F, 0F);
        Shape4b = new LODModelPart(this, 0, 0);
        Shape4b.addBox(0F, 0F, 0F, 1, 9, 1);
        Shape4b.setRotationPoint(-1F, 13F, -6F);
        Shape4b.setTextureSize(128, 128);
        Shape4b.mirror = true;
        this.setRotation(Shape4b, 0F, 0F, 0F);
        Shape5 = new LODModelPart(this, 0, 25);
        Shape5.addBox(0F, 0F, 0F, 11, 9, 10);
        Shape5.setRotationPoint(-6F, 13F, -5F);
        Shape5.setTextureSize(128, 128);
        Shape5.mirror = true;
        this.setRotation(Shape5, 0F, 0F, 0F);
        Shape4c = new LODModelPart(this, 0, 0);
        Shape4c.addBox(0F, 0F, 0F, 1, 9, 1);
        Shape4c.setRotationPoint(1F, 13F, -6F);
        Shape4c.setTextureSize(128, 128);
        Shape4c.mirror = true;
        this.setRotation(Shape4c, 0F, 0F, 0F);
        Shape4d = new LODModelPart(this, 0, 0);
        Shape4d.addBox(0F, 0F, 0F, 1, 9, 1);
        Shape4d.setRotationPoint(3F, 13F, -6F);
        Shape4d.setTextureSize(128, 128);
        Shape4d.mirror = true;
        this.setRotation(Shape4d, 0F, 0F, 0F);
        Shape6 = new LODModelPart(this, 8, 0);
        Shape6.addBox(0F, 0F, 0F, 1, 1, 4);
        Shape6.setRotationPoint(3F, 12F, -6F);
        Shape6.setTextureSize(128, 128);
        Shape6.mirror = true;
        this.setRotation(Shape6, 0F, 0F, 0F);
        Shape6a = new LODModelPart(this, 10, 12);
        Shape6a.addBox(0F, 0F, 0F, 1, 1, 7);
        Shape6a.setRotationPoint(-5F, 12F, -6F);
        Shape6a.setTextureSize(128, 128);
        Shape6a.mirror = true;
        this.setRotation(Shape6a, 0F, 0F, 0F);
        Shape6b = new LODModelPart(this, 6, 82);
        Shape6b.addBox(0F, 0F, 0F, 1, 1, 9);
        Shape6b.setRotationPoint(-3F, 12F, -6F);
        Shape6b.setTextureSize(128, 128);
        Shape6b.mirror = true;
        this.setRotation(Shape6b, 0F, 0F, 0F);
        Shape6c = new LODModelPart(this, 0, 12);
        Shape6c.addBox(0F, 0F, 0F, 1, 1, 3);
        Shape6c.setRotationPoint(0F, 12F, 3F);
        Shape6c.setTextureSize(128, 128);
        Shape6c.mirror = true;
        this.setRotation(Shape6c, 0F, 0F, 0F);
        Shape6d = new LODModelPart(this, 6, 106);
        Shape6d.addBox(0F, 0F, 0F, 1, 1, 7);
        Shape6d.setRotationPoint(1F, 12F, -6F);
        Shape6d.setTextureSize(128, 128);
        Shape6d.mirror = true;
        this.setRotation(Shape6d, 0F, 0F, 0F);
        Shape7 = new LODModelPart(this, 30, 0);
        Shape7.addBox(0F, 0F, 0F, 1, 11, 12);
        Shape7.setRotationPoint(7F, 11F, -6F);
        Shape7.setTextureSize(128, 128);
        Shape7.mirror = true;
        this.setRotation(Shape7, 0F, 0F, 0F);
        Shape7a = new LODModelPart(this, 58, 5);
        Shape7a.addBox(0F, 0F, 0F, 2, 10, 11);
        Shape7a.setRotationPoint(5F, 12F, -5.5F);
        Shape7a.setTextureSize(128, 128);
        Shape7a.mirror = true;
        this.setRotation(Shape7a, 0F, 0F, 0F);
        Shape8 = new LODModelPart(this, 46, 25);
        Shape8.addBox(0F, 0F, 0F, 1, 6, 1);
        Shape8.setRotationPoint(-3F, 16F, 5F);
        Shape8.setTextureSize(128, 128);
        Shape8.mirror = true;
        this.setRotation(Shape8, 0F, 0F, 0F);
        Shape8a = new LODModelPart(this, 19, 0);
        Shape8a.addBox(0F, 0F, 0F, 1, 2, 1);
        Shape8a.setRotationPoint(3F, 20F, 5F);
        Shape8a.setTextureSize(128, 128);
        Shape8a.mirror = true;
        this.setRotation(Shape8a, 0F, 0F, 0F);
        Shape9 = new LODModelPart(this, 0, 19);
        Shape9.addBox(0F, 0F, 0F, 2, 1, 1);
        Shape9.setRotationPoint(-2F, 16F, 5F);
        Shape9.setTextureSize(128, 128);
        Shape9.mirror = true;
        this.setRotation(Shape9, 0F, 0F, 0F);
        Shape8b = new LODModelPart(this, 0, 82);
        Shape8b.addBox(0F, 0F, 0F, 1, 8, 1);
        Shape8b.setRotationPoint(-5F, 14F, 5F);
        Shape8b.setTextureSize(128, 128);
        Shape8b.mirror = true;
        this.setRotation(Shape8b, 0F, 0F, 0F);
        Shape8c = new LODModelPart(this, 24, 0);
        Shape8c.addBox(0F, 0F, 0F, 1, 4, 1);
        Shape8c.setRotationPoint(2F, 15F, 5F);
        Shape8c.setTextureSize(128, 128);
        Shape8c.mirror = true;
        this.setRotation(Shape8c, 0F, 0F, 0F);
        Shape9a = new LODModelPart(this, 5, 6);
        Shape9a.addBox(0F, 0F, 0F, 2, 1, 1);
        Shape9a.setRotationPoint(0F, 18F, 5F);
        Shape9a.setTextureSize(128, 128);
        Shape9a.mirror = true;
        this.setRotation(Shape9a, 0F, 0F, 0F);
        Shape8d = new LODModelPart(this, 17, 6);
        Shape8d.addBox(0F, 0F, 0F, 1, 4, 1);
        Shape8d.setRotationPoint(-1F, 18F, 5F);
        Shape8d.setTextureSize(128, 128);
        Shape8d.mirror = true;
        this.setRotation(Shape8d, 0F, 0F, 0F);
        Shape6e = new LODModelPart(this, 6, 94);
        Shape6e.addBox(0F, 0F, 0F, 1, 1, 10);
        Shape6e.setRotationPoint(-1F, 12F, -6F);
        Shape6e.setTextureSize(128, 128);
        Shape6e.mirror = true;
        this.setRotation(Shape6e, 0F, 0F, 0F);
        Shape8e = new LODModelPart(this, 12, 6);
        Shape8e.addBox(0F, 0F, 0F, 1, 4, 1);
        Shape8e.setRotationPoint(0F, 13F, 5F);
        Shape8e.setTextureSize(128, 128);
        Shape8e.mirror = true;
        this.setRotation(Shape8e, 0F, 0F, 0F);
    }

    @Override
    public void renderAll(TileEntity te, ArrayList li, float phi, float theta) {
        GL11.glTranslated(0, 1, 0);
        GL11.glRotatef(phi, 1, 0, 0);
        GL11.glTranslated(0, -1, 0);
        Shape1.render(te, f5);
        Shape1a.render(te, f5);
        GL11.glTranslated(0, 1, 0);
        GL11.glRotatef(-phi, 1, 0, 0);
        GL11.glTranslated(0, -1, 0);

        Shape2.render(te, f5);
        Shape3.render(te, f5);
        Shape4.render(te, f5);
        Shape4a.render(te, f5);
        Shape4b.render(te, f5);
        Shape5.render(te, f5);
        Shape4c.render(te, f5);
        Shape4d.render(te, f5);
        Shape6.render(te, f5);
        Shape6a.render(te, f5);
        Shape6b.render(te, f5);
        Shape6c.render(te, f5);
        Shape6d.render(te, f5);
        Shape7.render(te, f5);
        Shape7a.render(te, f5);
        Shape8.render(te, f5);
        Shape8a.render(te, f5);
        Shape9.render(te, f5);
        Shape8b.render(te, f5);
        Shape8c.render(te, f5);
        Shape9a.render(te, f5);
        Shape8d.render(te, f5);
        Shape6e.render(te, f5);
        Shape8e.render(te, f5);
    }

    @Override
    public void
    setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5);
    }
}
