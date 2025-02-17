/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
// Date: 06/11/2014 9:46:45 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package Reika.ChromatiCraft.Models;

import java.util.ArrayList;

import Reika.ChromatiCraft.Base.ChromaModelBase;
import Reika.DragonAPI.Instantiable.Rendering.LODModelPart;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class ModelRelaySource extends ChromaModelBase {
    //fields
    LODModelPart Shape1;
    LODModelPart Shape2;
    LODModelPart Shape2a;
    LODModelPart Shape2b;
    LODModelPart Shape2c;
    LODModelPart Shape1a;
    LODModelPart Shape1b;
    LODModelPart Shape1d;
    LODModelPart Shape1e;
    LODModelPart Shape1f;
    LODModelPart Shape1c;
    LODModelPart Shape1e1;
    LODModelPart Shape1e2;
    LODModelPart Shape1e3;

    public ModelRelaySource() {
        textureWidth = 128;
        textureHeight = 128;

        Shape1 = new LODModelPart(this, 23, 87);
        Shape1.addBox(0F, 0F, 0F, 3, 3, 8);
        Shape1.setRotationPoint(4F, 14.5F, -4F);
        Shape1.setTextureSize(128, 128);
        Shape1.mirror = true;
        this.setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new LODModelPart(this, 69, 31);
        Shape2.addBox(-3F, 0F, 0F, 3, 2, 16);
        Shape2.setRotationPoint(8F, 14F, -8F);
        Shape2.setTextureSize(128, 128);
        Shape2.mirror = true;
        this.setRotation(Shape2, 0F, 0F, -0.2617994F);
        Shape2a = new LODModelPart(this, 65, 6);
        Shape2a.addBox(0F, 0F, -3F, 16, 2, 3);
        Shape2a.setRotationPoint(-8F, 14F, 8F);
        Shape2a.setTextureSize(128, 128);
        Shape2a.mirror = true;
        this.setRotation(Shape2a, 0.2617994F, 0F, 0F);
        Shape2b = new LODModelPart(this, 69, 12);
        Shape2b.addBox(0F, 0F, 0F, 3, 2, 16);
        Shape2b.setRotationPoint(-8F, 14F, -8F);
        Shape2b.setTextureSize(128, 128);
        Shape2b.mirror = true;
        this.setRotation(Shape2b, 0F, 0F, 0.2617994F);
        Shape2c = new LODModelPart(this, 65, 0);
        Shape2c.addBox(0F, 0F, 0F, 16, 2, 3);
        Shape2c.setRotationPoint(-8F, 14F, -8F);
        Shape2c.setTextureSize(128, 128);
        Shape2c.mirror = true;
        this.setRotation(Shape2c, -0.2617994F, 0F, 0F);
        Shape1a = new LODModelPart(this, 0, 104);
        Shape1a.addBox(0F, 0F, 0F, 14, 1, 3);
        Shape1a.setRotationPoint(-7F, 14.5F, -4F);
        Shape1a.setTextureSize(128, 128);
        Shape1a.mirror = true;
        this.setRotation(Shape1a, -0.5235988F, 0F, 0F);
        Shape1b = new LODModelPart(this, 0, 99);
        Shape1b.addBox(0F, 0F, -3F, 14, 1, 3);
        Shape1b.setRotationPoint(-7F, 14.5F, 4F);
        Shape1b.setTextureSize(128, 128);
        Shape1b.mirror = true;
        this.setRotation(Shape1b, 0.5235988F, 0F, 0F);
        Shape1d = new LODModelPart(this, 0, 87);
        Shape1d.addBox(0F, 0F, 0F, 3, 3, 8);
        Shape1d.setRotationPoint(-7F, 14.5F, -4F);
        Shape1d.setTextureSize(128, 128);
        Shape1d.mirror = true;
        this.setRotation(Shape1d, 0F, 0F, 0F);
        Shape1e = new LODModelPart(this, 0, 25);
        Shape1e.addBox(0F, 0F, 0F, 17, 1, 17);
        Shape1e.setRotationPoint(-8.5F, 22F, -8.5F);
        Shape1e.setTextureSize(128, 128);
        Shape1e.mirror = true;
        this.setRotation(Shape1e, 0F, 0F, 0F);
        Shape1f = new LODModelPart(this, 0, 82);
        Shape1f.addBox(0F, 0F, 0F, 14, 1, 3);
        Shape1f.setRotationPoint(-7F, 14.5F, -7F);
        Shape1f.setTextureSize(128, 128);
        Shape1f.mirror = true;
        this.setRotation(Shape1f, 0F, 0F, 0F);
        Shape1c = new LODModelPart(this, 35, 82);
        Shape1c.addBox(0F, 0F, 0F, 14, 1, 3);
        Shape1c.setRotationPoint(-7F, 14.5F, 4F);
        Shape1c.setTextureSize(128, 128);
        Shape1c.mirror = true;
        this.setRotation(Shape1c, 0F, 0F, 0F);
        Shape1e1 = new LODModelPart(this, 0, 0);
        Shape1e1.addBox(0F, 0F, 0F, 16, 8, 16);
        Shape1e1.setRotationPoint(-8F, 16F, -8F);
        Shape1e1.setTextureSize(128, 128);
        Shape1e1.mirror = true;
        this.setRotation(Shape1e1, 0F, 0F, 0F);
        Shape1e2 = new LODModelPart(this, 0, 63);
        Shape1e2.addBox(0F, 0F, 0F, 17, 1, 17);
        Shape1e2.setRotationPoint(-8.5F, 17F, -8.5F);
        Shape1e2.setTextureSize(128, 128);
        Shape1e2.mirror = true;
        this.setRotation(Shape1e2, 0F, 0F, 0F);
        Shape1e3 = new LODModelPart(this, 0, 44);
        Shape1e3.addBox(0F, 0F, 0F, 17, 1, 17);
        Shape1e3.setRotationPoint(-8.5F, 19F, -8.5F);
        Shape1e3.setTextureSize(128, 128);
        Shape1e3.mirror = true;
        this.setRotation(Shape1e3, 0F, 0F, 0F);
    }

    @Override
    public void renderAll(TileEntity te, ArrayList li) {
        GL11.glColor4f(1, 1, 1, 1);
        Shape1.render(te, f5);
        Shape2.render(te, f5);
        Shape2a.render(te, f5);
        Shape2b.render(te, f5);
        Shape2c.render(te, f5);
        Shape1a.render(te, f5);
        Shape1b.render(te, f5);
        Shape1d.render(te, f5);
        Shape1f.render(te, f5);
        Shape1c.render(te, f5);
        Shape1e1.render(te, f5);
    }

    public void renderEdges(TileEntity te) {
        Shape1e.render(te, f5);
        Shape1e2.render(te, f5);
        Shape1e3.render(te, f5);
    }

    @Override
    public void
    setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5);
    }
}
