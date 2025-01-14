/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
// Date: 07/03/2016 9:29:20 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package Reika.ChromatiCraft.Models;

import java.util.ArrayList;

import Reika.ChromatiCraft.Base.ChromaModelBase;
import Reika.DragonAPI.Instantiable.Rendering.LODModelPart;
import net.minecraft.tileentity.TileEntity;

public class ModelEnchantDecomp extends ChromaModelBase {
    //fields
    LODModelPart Shape1;
    LODModelPart Shape1a;
    LODModelPart Shape4;
    LODModelPart Shape4a;
    LODModelPart Shape4b;
    LODModelPart Shape4c;
    LODModelPart Shape4d;
    LODModelPart Shape4e;
    LODModelPart Shape1b;
    LODModelPart Shape2;
    LODModelPart Shape2a;
    LODModelPart Shape2b;
    LODModelPart Shape2c;

    public ModelEnchantDecomp() {
        textureWidth = 128;
        textureHeight = 128;

        Shape1 = new LODModelPart(this, 0, 29);
        Shape1.addBox(0F, 0F, 0F, 16, 1, 5);
        Shape1.setRotationPoint(-8F, 16F, 3F);
        Shape1.setTextureSize(128, 128);
        Shape1.mirror = true;
        this.setRotation(Shape1, 0F, 0F, 0F);
        Shape1a = new LODModelPart(this, 0, 0);
        Shape1a.addBox(0F, 0F, 0F, 16, 1, 16);
        Shape1a.setRotationPoint(-8F, 23F, -8F);
        Shape1a.setTextureSize(128, 128);
        Shape1a.mirror = true;
        this.setRotation(Shape1a, 0F, 0F, 0F);
        Shape4 = new LODModelPart(this, 25, 86);
        Shape4.addBox(0F, 0F, -5F, 6, 1, 5);
        Shape4.setRotationPoint(-3F, 16F, 8F);
        Shape4.setTextureSize(128, 128);
        Shape4.mirror = true;
        this.setRotation(Shape4, -0.5235988F, 0F, 0F);
        Shape4a = new LODModelPart(this, 0, 79);
        Shape4a.addBox(-5F, 0F, 0F, 5, 1, 6);
        Shape4a.setRotationPoint(8F, 16F, -3F);
        Shape4a.setTextureSize(128, 128);
        Shape4a.mirror = true;
        this.setRotation(Shape4a, 0F, 0F, 0.5235988F);
        Shape4b = new LODModelPart(this, 25, 70);
        Shape4b.addBox(0F, 0F, 0F, 5, 1, 6);
        Shape4b.setRotationPoint(-8F, 16F, -3F);
        Shape4b.setTextureSize(128, 128);
        Shape4b.mirror = true;
        this.setRotation(Shape4b, 0F, 0F, 0F);
        Shape4c = new LODModelPart(this, 0, 70);
        Shape4c.addBox(0F, 0F, 0F, 5, 1, 6);
        Shape4c.setRotationPoint(3F, 16F, -3F);
        Shape4c.setTextureSize(128, 128);
        Shape4c.mirror = true;
        this.setRotation(Shape4c, 0F, 0F, 0F);
        Shape4d = new LODModelPart(this, 25, 94);
        Shape4d.addBox(0F, 0F, 0F, 5, 1, 6);
        Shape4d.setRotationPoint(-8F, 16F, -3F);
        Shape4d.setTextureSize(128, 128);
        Shape4d.mirror = true;
        this.setRotation(Shape4d, 0F, 0F, -0.5235988F);
        Shape4e = new LODModelPart(this, 25, 79);
        Shape4e.addBox(0F, 0F, 0F, 6, 1, 5);
        Shape4e.setRotationPoint(-3F, 16F, -8F);
        Shape4e.setTextureSize(128, 128);
        Shape4e.mirror = true;
        this.setRotation(Shape4e, 0.5235988F, 0F, 0F);
        Shape1b = new LODModelPart(this, 0, 20);
        Shape1b.addBox(0F, 0F, 0F, 16, 1, 5);
        Shape1b.setRotationPoint(-8F, 16F, -8F);
        Shape1b.setTextureSize(128, 128);
        Shape1b.mirror = true;
        this.setRotation(Shape1b, 0F, 0F, 0F);
        Shape2 = new LODModelPart(this, 36, 37);
        Shape2.addBox(0F, 0F, 0F, 16, 6, 1);
        Shape2.setRotationPoint(-8F, 17F, 7F);
        Shape2.setTextureSize(128, 128);
        Shape2.mirror = true;
        this.setRotation(Shape2, 0F, 0F, 0F);
        Shape2a = new LODModelPart(this, 33, 46);
        Shape2a.addBox(0F, 0F, 0F, 1, 6, 14);
        Shape2a.setRotationPoint(7F, 17F, -7F);
        Shape2a.setTextureSize(128, 128);
        Shape2a.mirror = true;
        this.setRotation(Shape2a, 0F, 0F, 0F);
        Shape2b = new LODModelPart(this, 0, 37);
        Shape2b.addBox(0F, 0F, 0F, 16, 6, 1);
        Shape2b.setRotationPoint(-8F, 17F, -8F);
        Shape2b.setTextureSize(128, 128);
        Shape2b.mirror = true;
        this.setRotation(Shape2b, 0F, 0F, 0F);
        Shape2c = new LODModelPart(this, 0, 46);
        Shape2c.addBox(0F, 0F, 0F, 1, 6, 14);
        Shape2c.setRotationPoint(-8F, 17F, -7F);
        Shape2c.setTextureSize(128, 128);
        Shape2c.mirror = true;
        this.setRotation(Shape2c, 0F, 0F, 0F);
    }

    @Override
    public void renderAll(TileEntity te, ArrayList li) {
        Shape1.render(te, f5);
        Shape1a.render(te, f5);
        Shape4.render(te, f5);
        Shape4a.render(te, f5);
        Shape4b.render(te, f5);
        Shape4c.render(te, f5);
        Shape4d.render(te, f5);
        Shape4e.render(te, f5);
        Shape1b.render(te, f5);
        Shape2.render(te, f5);
        Shape2a.render(te, f5);
        Shape2b.render(te, f5);
        Shape2c.render(te, f5);
    }
}
