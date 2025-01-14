/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
// Date: 01/09/2013 5:41:06 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package Reika.RotaryCraft.Models;

import java.util.ArrayList;

import Reika.DragonAPI.Instantiable.Rendering.LODModelPart;
import Reika.RotaryCraft.Base.RotaryModelBase;
import net.minecraft.tileentity.TileEntity;

public class ModelEMP extends RotaryModelBase {
    //fields
    LODModelPart Shape1;
    LODModelPart Shape2;
    LODModelPart Shape3;
    LODModelPart Shape3a;
    LODModelPart Shape3b;
    LODModelPart Shape3c;
    LODModelPart Shape4;
    LODModelPart Shape4a;
    LODModelPart Shape4b;
    LODModelPart Shape4c;
    LODModelPart Shape5;
    LODModelPart Shape5a;
    LODModelPart Shape5b;
    LODModelPart Shape6;

    public ModelEMP() {
        textureWidth = 128;
        textureHeight = 128;

        Shape1 = new LODModelPart(this, 0, 0);
        Shape1.addBox(0F, 0F, 0F, 16, 1, 16);
        Shape1.setRotationPoint(-8F, 23F, -8F);
        Shape1.setTextureSize(128, 128);
        Shape1.mirror = true;
        this.setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new LODModelPart(this, 114, 0);
        Shape2.addBox(0F, 0F, 0F, 2, 7, 2);
        Shape2.setRotationPoint(-1F, 16F, -1F);
        Shape2.setTextureSize(128, 128);
        Shape2.mirror = true;
        this.setRotation(Shape2, 0F, 0F, 0F);
        Shape3 = new LODModelPart(this, 0, 40);
        Shape3.addBox(0F, -7F, 0F, 4, 7, 1);
        Shape3.setRotationPoint(2F, 23F, -1F);
        Shape3.setTextureSize(128, 128);
        Shape3.mirror = true;
        this.setRotation(Shape3, -1.047198F, 3.141593F, 0F);
        Shape3a = new LODModelPart(this, 0, 30);
        Shape3a.addBox(0F, -7F, 0F, 4, 7, 1);
        Shape3a.setRotationPoint(-1F, 23F, -2F);
        Shape3a.setTextureSize(128, 128);
        Shape3a.mirror = true;
        this.setRotation(Shape3a, -1.047198F, -1.570796F, 0F);
        Shape3b = new LODModelPart(this, 0, 50);
        Shape3b.addBox(0F, -7F, 0F, 4, 7, 1);
        Shape3b.setRotationPoint(-2F, 23F, 1F);
        Shape3b.setTextureSize(128, 128);
        Shape3b.mirror = true;
        this.setRotation(Shape3b, -1.047198F, 0F, 0F);
        Shape3c = new LODModelPart(this, 0, 20);
        Shape3c.addBox(0F, -7F, 0F, 4, 7, 1);
        Shape3c.setRotationPoint(1F, 23F, 2F);
        Shape3c.setTextureSize(128, 128);
        Shape3c.mirror = true;
        this.setRotation(Shape3c, -1.047198F, 1.570796F, 0F);
        Shape4 = new LODModelPart(this, 12, 50);
        Shape4.addBox(-1F, -7F, -1F, 2, 7, 1);
        Shape4.setRotationPoint(-1F, 23F, 1F);
        Shape4.setTextureSize(128, 128);
        Shape4.mirror = true;
        this.setRotation(Shape4, 1.047198F, 2.356194F, 0F);
        Shape4a = new LODModelPart(this, 12, 20);
        Shape4a.addBox(-1F, -7F, -1F, 2, 7, 1);
        Shape4a.setRotationPoint(1F, 23F, 1F);
        Shape4a.setTextureSize(128, 128);
        Shape4a.mirror = true;
        this.setRotation(Shape4a, 1.047198F, -2.356194F, 0F);
        Shape4b = new LODModelPart(this, 12, 40);
        Shape4b.addBox(-1F, -7F, -1F, 2, 7, 1);
        Shape4b.setRotationPoint(-1F, 23F, -1F);
        Shape4b.setTextureSize(128, 128);
        Shape4b.mirror = true;
        this.setRotation(Shape4b, 1.047198F, 0.7853982F, 0F);
        Shape4c = new LODModelPart(this, 12, 30);
        Shape4c.addBox(-1F, -7F, -1F, 2, 7, 1);
        Shape4c.setRotationPoint(1F, 23F, -1F);
        Shape4c.setTextureSize(128, 128);
        Shape4c.mirror = true;
        this.setRotation(Shape4c, 1.047198F, -0.7853982F, 0F);
        Shape5 = new LODModelPart(this, 114, 20);
        Shape5.addBox(0F, 0F, 0F, 3, 1, 3);
        Shape5.setRotationPoint(-1.5F, 21F, -1.5F);
        Shape5.setTextureSize(128, 128);
        Shape5.mirror = true;
        this.setRotation(Shape5, 0F, 0F, 0F);
        Shape5a = new LODModelPart(this, 114, 10);
        Shape5a.addBox(0F, 0F, 0F, 3, 1, 3);
        Shape5a.setRotationPoint(-1.5F, 17F, -1.5F);
        Shape5a.setTextureSize(128, 128);
        Shape5a.mirror = true;
        this.setRotation(Shape5a, 0F, 0F, 0F);
        Shape5b = new LODModelPart(this, 114, 15);
        Shape5b.addBox(0F, 0F, 0F, 3, 1, 3);
        Shape5b.setRotationPoint(-1.5F, 19F, -1.5F);
        Shape5b.setTextureSize(128, 128);
        Shape5b.mirror = true;
        this.setRotation(Shape5b, 0F, 0F, 0F);
        Shape6 = new LODModelPart(this, 65, 0);
        Shape6.addBox(0F, 0F, 0F, 12, 1, 12);
        Shape6.setRotationPoint(-6F, 22.5F, -6F);
        Shape6.setTextureSize(128, 128);
        Shape6.mirror = true;
        this.setRotation(Shape6, 0F, 0F, 0F);
    }

    @Override
    public void renderAll(TileEntity te, ArrayList li, float phi, float theta) {
        Shape1.render(te, f5);
        Shape2.render(te, f5);
        Shape3.render(te, f5);
        Shape3a.render(te, f5);
        Shape3b.render(te, f5);
        Shape3c.render(te, f5);
        Shape4.render(te, f5);
        Shape4a.render(te, f5);
        Shape4b.render(te, f5);
        Shape4c.render(te, f5);
        Shape5.render(te, f5);
        Shape5a.render(te, f5);
        Shape5b.render(te, f5);
        Shape6.render(te, f5);
    }

    @Override
    public void
    setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5);
    }
}
