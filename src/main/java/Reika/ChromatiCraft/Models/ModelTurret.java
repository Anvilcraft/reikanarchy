/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
// Date: 27/08/2015 7:44:05 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package Reika.ChromatiCraft.Models;

import java.util.ArrayList;

import Reika.ChromatiCraft.Base.ChromaModelBase;
import Reika.DragonAPI.Instantiable.Rendering.LODModelPart;
import net.minecraft.tileentity.TileEntity;

public class ModelTurret extends ChromaModelBase {
    //fields
    LODModelPart Shape1;
    LODModelPart Shape2;
    LODModelPart Shape2a;
    LODModelPart Shape3;
    LODModelPart Shape3a;
    LODModelPart Shape3b;
    LODModelPart Shape3c;

    public ModelTurret() {
        textureWidth = 64;
        textureHeight = 32;

        Shape1 = new LODModelPart(this, 0, 0);
        Shape1.addBox(0F, 0F, 0F, 8, 1, 8);
        Shape1.setRotationPoint(-4F, 23F, -4F);
        Shape1.setTextureSize(64, 32);
        Shape1.mirror = true;
        this.setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new LODModelPart(this, 42, 0);
        Shape2.addBox(0F, 0F, 0F, 1, 1, 1);
        Shape2.setRotationPoint(-0.5F, 14F, -0.5F);
        Shape2.setTextureSize(64, 32);
        Shape2.mirror = true;
        this.setRotation(Shape2, 0F, 0F, 0F);
        Shape2a = new LODModelPart(this, 33, 0);
        Shape2a.addBox(0F, 0F, 0F, 2, 8, 2);
        Shape2a.setRotationPoint(-1F, 15F, -1F);
        Shape2a.setTextureSize(64, 32);
        Shape2a.mirror = true;
        this.setRotation(Shape2a, 0F, 0F, 0F);
        Shape3 = new LODModelPart(this, 15, 10);
        Shape3.addBox(-0.5F, 1F, -0.5F, 1, 8, 1);
        Shape3.setRotationPoint(0F, 15F, 0F);
        Shape3.setTextureSize(64, 32);
        Shape3.mirror = true;
        this.setRotation(Shape3, 0F, 0F, -0.3926991F);
        Shape3a = new LODModelPart(this, 0, 10);
        Shape3a.addBox(-0.5F, 1F, -0.5F, 1, 8, 1);
        Shape3a.setRotationPoint(0F, 15F, 0F);
        Shape3a.setTextureSize(64, 32);
        Shape3a.mirror = true;
        this.setRotation(Shape3a, 0.3926991F, 0F, 0F);
        Shape3b = new LODModelPart(this, 5, 10);
        Shape3b.addBox(-0.5F, 1F, -0.5F, 1, 8, 1);
        Shape3b.setRotationPoint(0F, 15F, 0F);
        Shape3b.setTextureSize(64, 32);
        Shape3b.mirror = true;
        this.setRotation(Shape3b, -0.3926991F, 0F, 0F);
        Shape3c = new LODModelPart(this, 10, 10);
        Shape3c.addBox(-0.5F, 1F, -0.5F, 1, 8, 1);
        Shape3c.setRotationPoint(0F, 15F, 0F);
        Shape3c.setTextureSize(64, 32);
        Shape3c.mirror = true;
        this.setRotation(Shape3c, 0F, 0F, 0.3926991F);
    }

    @Override
    public void renderAll(TileEntity te, ArrayList li) {
        Shape1.render(te, f5);
        Shape2.render(te, f5);
        Shape2a.render(te, f5);
        Shape3.render(te, f5);
        Shape3a.render(te, f5);
        Shape3b.render(te, f5);
        Shape3c.render(te, f5);
    }

    @Override
    public void
    setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5);
    }
}
