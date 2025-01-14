/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
// Date: 10/11/2013 8:19:25 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package Reika.RotaryCraft.Models;

import java.util.ArrayList;

import Reika.DragonAPI.Instantiable.Rendering.LODModelPart;
import Reika.RotaryCraft.Base.RotaryModelBase;
import net.minecraft.tileentity.TileEntity;

public class ModelFillingStation extends RotaryModelBase {
    //fields
    LODModelPart Shape1;
    LODModelPart Shape2a;
    LODModelPart Shape2;
    LODModelPart Shape3;
    LODModelPart Shape3a;
    LODModelPart Shape4;
    LODModelPart Shape5;
    LODModelPart Shape5a;

    public ModelFillingStation() {
        textureWidth = 128;
        textureHeight = 128;

        Shape1 = new LODModelPart(this, 0, 0);
        Shape1.addBox(0F, 0F, 0F, 16, 1, 16);
        Shape1.setRotationPoint(-8F, 23F, -8F);
        Shape1.setTextureSize(128, 128);
        Shape1.mirror = true;
        this.setRotation(Shape1, 0F, 0F, 0F);
        Shape2a = new LODModelPart(this, 65, 17);
        Shape2a.addBox(0F, 0F, 0F, 16, 15, 1);
        Shape2a.setRotationPoint(-8F, 8F, -5F);
        Shape2a.setTextureSize(128, 128);
        Shape2a.mirror = true;
        this.setRotation(Shape2a, 0F, 0F, 0F);
        Shape2 = new LODModelPart(this, 65, 0);
        Shape2.addBox(0F, 0F, 0F, 16, 15, 1);
        Shape2.setRotationPoint(-8F, 8F, 7F);
        Shape2.setTextureSize(128, 128);
        Shape2.mirror = true;
        this.setRotation(Shape2, 0F, 0F, 0F);
        Shape3 = new LODModelPart(this, 0, 31);
        Shape3.addBox(0F, 0F, 0F, 1, 15, 11);
        Shape3.setRotationPoint(7F, 8F, -4F);
        Shape3.setTextureSize(128, 128);
        Shape3.mirror = true;
        this.setRotation(Shape3, 0F, 0F, 0F);
        Shape3a = new LODModelPart(this, 0, 31);
        Shape3a.addBox(0F, 0F, 0F, 1, 15, 11);
        Shape3a.setRotationPoint(-8F, 8F, -4F);
        Shape3a.setTextureSize(128, 128);
        Shape3a.mirror = true;
        this.setRotation(Shape3a, 0F, 0F, 0F);
        Shape4 = new LODModelPart(this, 0, 19);
        Shape4.addBox(0F, 0F, 0F, 16, 1, 2);
        Shape4.setRotationPoint(-8F, 8F, -7F);
        Shape4.setTextureSize(128, 128);
        Shape4.mirror = true;
        this.setRotation(Shape4, 0F, 0F, 0F);
        Shape5 = new LODModelPart(this, 38, 19);
        Shape5.addBox(0F, 0F, 0F, 1, 14, 2);
        Shape5.setRotationPoint(7F, 9F, -7F);
        Shape5.setTextureSize(128, 128);
        Shape5.mirror = true;
        this.setRotation(Shape5, 0F, 0F, 0F);
        Shape5a = new LODModelPart(this, 38, 19);
        Shape5a.addBox(0F, 0F, 0F, 1, 14, 2);
        Shape5a.setRotationPoint(-8F, 9F, -7F);
        Shape5a.setTextureSize(128, 128);
        Shape5a.mirror = true;
        this.setRotation(Shape5a, 0F, 0F, 0F);
    }

    @Override
    public void renderAll(TileEntity te, ArrayList li, float phi, float theta) {
        Shape1.render(te, f5);
        Shape2a.render(te, f5);
        Shape2.render(te, f5);
        Shape3.render(te, f5);
        Shape3a.render(te, f5);
        Shape4.render(te, f5);
        Shape5.render(te, f5);
        Shape5a.render(te, f5);
    }

    @Override
    public void
    setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5);
    }
}
