/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
// Date: 21/05/2013 1:15:04 AM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package Reika.RotaryCraft.Models;

import java.util.ArrayList;

import Reika.DragonAPI.Instantiable.Rendering.LODModelPart;
import Reika.RotaryCraft.Base.RotaryModelBase;
import net.minecraft.tileentity.TileEntity;

public class ModelScreen extends RotaryModelBase {
    //fields
    LODModelPart Shape1;
    LODModelPart Shape2;
    LODModelPart Shape3;
    LODModelPart Shape4;
    LODModelPart Shape5;
    LODModelPart Shape5a;
    LODModelPart Shape6;
    LODModelPart Shape6a;
    LODModelPart Shape7;
    LODModelPart Shape8a;
    LODModelPart Shape8b;
    LODModelPart Shape8c;
    LODModelPart Shape8d;
    LODModelPart Shape8;

    public ModelScreen() {
        textureWidth = 128;
        textureHeight = 128;

        Shape1 = new LODModelPart(this, 0, 0);
        Shape1.addBox(0F, 0F, 0F, 16, 1, 16);
        Shape1.setRotationPoint(-8F, 23F, -8F);
        Shape1.setTextureSize(128, 128);
        Shape1.mirror = true;
        this.setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new LODModelPart(this, 91, 14);
        Shape2.addBox(0F, 0F, 0F, 8, 1, 8);
        Shape2.setRotationPoint(-4F, 20F, -4F);
        Shape2.setTextureSize(128, 128);
        Shape2.mirror = true;
        this.setRotation(Shape2, 0F, 0F, 0F);
        Shape3 = new LODModelPart(this, 65, 14);
        Shape3.addBox(0F, 0F, 0F, 6, 1, 6);
        Shape3.setRotationPoint(-3F, 21F, -3F);
        Shape3.setTextureSize(128, 128);
        Shape3.mirror = true;
        this.setRotation(Shape3, 0F, 0F, 0F);
        Shape4 = new LODModelPart(this, 65, 0);
        Shape4.addBox(0F, 0F, 0F, 12, 1, 12);
        Shape4.setRotationPoint(-6F, 22F, -6F);
        Shape4.setTextureSize(128, 128);
        Shape4.mirror = true;
        this.setRotation(Shape4, 0F, 0F, 0F);
        Shape5 = new LODModelPart(this, 7, 26);
        Shape5.addBox(0F, 0F, 0F, 2, 12, 1);
        Shape5.setRotationPoint(6F, 8F, -8F);
        Shape5.setTextureSize(128, 128);
        Shape5.mirror = true;
        this.setRotation(Shape5, 0F, 0F, 0F);
        Shape5a = new LODModelPart(this, 0, 26);
        Shape5a.addBox(0F, 0F, 0F, 2, 12, 1);
        Shape5a.setRotationPoint(-8F, 8F, -8F);
        Shape5a.setTextureSize(128, 128);
        Shape5a.mirror = true;
        this.setRotation(Shape5a, 0F, 0F, 0F);
        Shape6 = new LODModelPart(this, 0, 22);
        Shape6.addBox(0F, 0F, 0F, 12, 2, 1);
        Shape6.setRotationPoint(-6F, 8F, -8F);
        Shape6.setTextureSize(128, 128);
        Shape6.mirror = true;
        this.setRotation(Shape6, 0F, 0F, 0F);
        Shape6a = new LODModelPart(this, 0, 18);
        Shape6a.addBox(0F, 0F, 0F, 12, 2, 1);
        Shape6a.setRotationPoint(-6F, 18F, -8F);
        Shape6a.setTextureSize(128, 128);
        Shape6a.mirror = true;
        this.setRotation(Shape6a, 0F, 0F, 0F);
        Shape7 = new LODModelPart(this, 0, 40);
        Shape7.addBox(0F, 0F, 0F, 12, 8, 1);
        Shape7.setRotationPoint(-6F, 10F, -7.5F);
        Shape7.setTextureSize(128, 128);
        Shape7.mirror = true;
        this.setRotation(Shape7, 0F, 0F, 0F);
        Shape8a = new LODModelPart(this, 0, 110);
        Shape8a.addBox(0F, 0F, 0F, 12, 7, 2);
        Shape8a.setRotationPoint(-6F, 12F, 6F);
        Shape8a.setTextureSize(128, 128);
        Shape8a.mirror = true;
        this.setRotation(Shape8a, 0F, 0F, 0F);
        Shape8b = new LODModelPart(this, 0, 50);
        Shape8b.addBox(0F, 0F, 0F, 16, 12, 5);
        Shape8b.setRotationPoint(-8F, 8F, -7F);
        Shape8b.setTextureSize(128, 128);
        Shape8b.mirror = true;
        this.setRotation(Shape8b, 0F, 0F, 0F);
        Shape8c = new LODModelPart(this, 0, 68);
        Shape8c.addBox(0F, 0F, 0F, 16, 11, 3);
        Shape8c.setRotationPoint(-8F, 9F, -2F);
        Shape8c.setTextureSize(128, 128);
        Shape8c.mirror = true;
        this.setRotation(Shape8c, 0F, 0F, 0F);
        Shape8d = new LODModelPart(this, 0, 83);
        Shape8d.addBox(0F, 0F, 0F, 15, 10, 3);
        Shape8d.setRotationPoint(-7.5F, 10F, 1F);
        Shape8d.setTextureSize(128, 128);
        Shape8d.mirror = true;
        this.setRotation(Shape8d, 0F, 0F, 0F);
        Shape8 = new LODModelPart(this, 0, 98);
        Shape8.addBox(0F, 0F, 0F, 14, 9, 2);
        Shape8.setRotationPoint(-7F, 11F, 4F);
        Shape8.setTextureSize(128, 128);
        Shape8.mirror = true;
        this.setRotation(Shape8, 0F, 0F, 0F);
    }

    @Override
    public void renderAll(TileEntity te, ArrayList li, float phi, float theta) {
        Shape1.render(te, f5);
        Shape2.render(te, f5);
        Shape3.render(te, f5);
        Shape4.render(te, f5);
        Shape5.render(te, f5);
        Shape5a.render(te, f5);
        Shape6.render(te, f5);
        Shape6a.render(te, f5);
        Shape7.render(te, f5);
        Shape8a.render(te, f5);
        Shape8b.render(te, f5);
        Shape8c.render(te, f5);
        Shape8d.render(te, f5);
        Shape8.render(te, f5);
    }

    @Override
    public void
    setRotationAngles(float f, float f1, float f2, float f3, float f4, float f6) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5);
    }
}
