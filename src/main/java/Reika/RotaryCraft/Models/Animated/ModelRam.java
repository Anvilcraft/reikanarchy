/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
// Date: 25/08/2013 6:56:29 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package Reika.RotaryCraft.Models.Animated;

import java.util.ArrayList;

import Reika.DragonAPI.Instantiable.Rendering.LODModelPart;
import Reika.RotaryCraft.Base.RotaryModelBase;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class ModelRam extends RotaryModelBase {
    //fields
    LODModelPart Shape1;
    LODModelPart a;
    LODModelPart b;
    LODModelPart c;
    LODModelPart d;
    LODModelPart Shape4;
    LODModelPart Shape4a;
    LODModelPart Shape4b;
    LODModelPart Shape4c;
    LODModelPart Shape4d;
    LODModelPart Shape4e;
    LODModelPart Shape4f;
    LODModelPart Shape4g;
    LODModelPart Shape2;

    public ModelRam() {
        textureWidth = 128;
        textureHeight = 128;

        Shape1 = new LODModelPart(this, 0, 0);
        Shape1.addBox(0F, 0F, 0F, 16, 16, 6);
        Shape1.setRotationPoint(-8F, 8F, -8F);
        Shape1.setTextureSize(128, 128);
        Shape1.mirror = true;
        this.setRotation(Shape1, 0F, 0F, 0F);
        a = new LODModelPart(this, 0, 50);
        a.addBox(0F, 0F, 0F, 14, 3, 3);
        a.setRotationPoint(-7F, 20F, 0F);
        a.setTextureSize(128, 128);
        a.mirror = true;
        this.setRotation(a, 0F, 0F, 0F);
        b = new LODModelPart(this, 0, 42);
        b.addBox(0F, 0F, 0F, 14, 3, 3);
        b.setRotationPoint(-7F, 9F, 0F);
        b.setTextureSize(128, 128);
        b.mirror = true;
        this.setRotation(b, 0F, 0F, 0F);
        c = new LODModelPart(this, 0, 58);
        c.addBox(0F, 0F, 0F, 3, 8, 3);
        c.setRotationPoint(4F, 12F, 0F);
        c.setTextureSize(128, 128);
        c.mirror = true;
        this.setRotation(c, 0F, 0F, 0F);
        d = new LODModelPart(this, 14, 58);
        d.addBox(0F, 0F, 0F, 3, 8, 3);
        d.setRotationPoint(-7F, 12F, 0F);
        d.setTextureSize(128, 128);
        d.mirror = true;
        this.setRotation(d, 0F, 0F, 0F);
        Shape4 = new LODModelPart(this, 95, 26);
        Shape4.addBox(0F, 0F, 0F, 2, 2, 10);
        Shape4.setRotationPoint(-8F, 22F, -2F);
        Shape4.setTextureSize(128, 128);
        Shape4.mirror = true;
        this.setRotation(Shape4, 0F, 0F, 0F);
        Shape4a = new LODModelPart(this, 45, 26);
        Shape4a.addBox(0F, 0F, 0F, 2, 2, 10);
        Shape4a.setRotationPoint(6F, 22F, -2F);
        Shape4a.setTextureSize(128, 128);
        Shape4a.mirror = true;
        this.setRotation(Shape4a, 0F, 0F, 0F);
        Shape4b = new LODModelPart(this, 95, 0);
        Shape4b.addBox(0F, 0F, 0F, 2, 2, 10);
        Shape4b.setRotationPoint(-8F, 8F, -2F);
        Shape4b.setTextureSize(128, 128);
        Shape4b.mirror = true;
        this.setRotation(Shape4b, 0F, 0F, 0F);
        Shape4c = new LODModelPart(this, 70, 26);
        Shape4c.addBox(0F, 0F, 0F, 2, 2, 10);
        Shape4c.setRotationPoint(-1F, 22F, -2F);
        Shape4c.setTextureSize(128, 128);
        Shape4c.mirror = true;
        this.setRotation(Shape4c, 0F, 0F, 0F);
        Shape4d = new LODModelPart(this, 95, 13);
        Shape4d.addBox(0F, 0F, 0F, 2, 2, 10);
        Shape4d.setRotationPoint(-8F, 15F, -2F);
        Shape4d.setTextureSize(128, 128);
        Shape4d.mirror = true;
        this.setRotation(Shape4d, 0F, 0F, 0F);
        Shape4e = new LODModelPart(this, 70, 0);
        Shape4e.addBox(0F, 0F, 0F, 2, 2, 10);
        Shape4e.setRotationPoint(-1F, 8F, -2F);
        Shape4e.setTextureSize(128, 128);
        Shape4e.mirror = true;
        this.setRotation(Shape4e, 0F, 0F, 0F);
        Shape4f = new LODModelPart(this, 45, 0);
        Shape4f.addBox(0F, 0F, 0F, 2, 2, 10);
        Shape4f.setRotationPoint(6F, 8F, -2F);
        Shape4f.setTextureSize(128, 128);
        Shape4f.mirror = true;
        this.setRotation(Shape4f, 0F, 0F, 0F);
        Shape4g = new LODModelPart(this, 45, 13);
        Shape4g.addBox(0F, 0F, 0F, 2, 2, 10);
        Shape4g.setRotationPoint(6F, 15F, -2F);
        Shape4g.setTextureSize(128, 128);
        Shape4g.mirror = true;
        this.setRotation(Shape4g, 0F, 0F, 0F);
        Shape2 = new LODModelPart(this, 0, 23);
        Shape2.addBox(0F, 0F, 0F, 14, 14, 2);
        Shape2.setRotationPoint(-7F, 9F, -2F);
        Shape2.setTextureSize(128, 128);
        Shape2.mirror = true;
        this.setRotation(Shape2, 0F, 0F, 0F);
    }

    @Override
    public void renderAll(TileEntity te, ArrayList li, float phi, float theta) {
        Shape1.render(te, f5);

        Shape4.render(te, f5);
        Shape4a.render(te, f5);
        Shape4b.render(te, f5);
        Shape4c.render(te, f5);
        Shape4d.render(te, f5);
        Shape4e.render(te, f5);
        Shape4f.render(te, f5);
        Shape4g.render(te, f5);
        Shape2.render(te, f5);

        double x = 0;
        double y = 0;
        double z = phi * 5 / 16D;
        GL11.glTranslated(x, y, z);
        a.render(te, f5);
        b.render(te, f5);
        c.render(te, f5);
        d.render(te, f5);
        GL11.glTranslated(-x, -y, -z);
    }

    @Override
    public void
    setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5);
    }
}
