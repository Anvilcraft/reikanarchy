/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
// Date: 13/10/2013 3:08:19 PM
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

public class ModelFertilizer extends RotaryModelBase {
    //fields
    LODModelPart Shape1;
    LODModelPart Shape1a;
    LODModelPart Shape1b;
    LODModelPart Shape1d;
    LODModelPart Shape1e;
    LODModelPart Shape1f;
    LODModelPart Shape1g;
    LODModelPart Shape1j;
    LODModelPart Shape1h;
    LODModelPart Shape1r;
    LODModelPart Shape1k;
    LODModelPart Shape1l;
    LODModelPart Shape1m;
    LODModelPart Shape1n;
    LODModelPart Shape1o;
    LODModelPart Shape1p;
    LODModelPart Shape1q;
    LODModelPart Shape2;
    LODModelPart Shape3;
    LODModelPart Shape3a;
    LODModelPart Shape3b;
    LODModelPart Shape3c;
    LODModelPart Shape4;
    LODModelPart Shape4a;
    LODModelPart Shape4b;
    LODModelPart Shape4c;
    LODModelPart Shape2a;
    LODModelPart Shape5;

    public ModelFertilizer() {
        textureWidth = 128;
        textureHeight = 128;

        Shape1 = new LODModelPart(this, 0, 0);
        Shape1.addBox(-1F, 0F, -7F, 2, 1, 14);
        Shape1.setRotationPoint(0F, 13F, 0F);
        Shape1.setTextureSize(128, 128);
        Shape1.mirror = true;
        this.setRotation(Shape1, 0F, 0.7853982F, 0F);
        Shape1a = new LODModelPart(this, 35, 0);
        Shape1a.addBox(1F, 0F, -7F, 1, 2, 14);
        Shape1a.setRotationPoint(0F, 11F, 0F);
        Shape1a.setTextureSize(128, 128);
        Shape1a.mirror = true;
        this.setRotation(Shape1a, 0F, 0.7853982F, 0F);
        Shape1b = new LODModelPart(this, 0, 17);
        Shape1b.addBox(-1F, 0F, -7F, 2, 1, 14);
        Shape1b.setRotationPoint(0F, 10F, 0F);
        Shape1b.setTextureSize(128, 128);
        Shape1b.mirror = true;
        this.setRotation(Shape1b, 0F, 0.7853982F, 0F);
        Shape1d = new LODModelPart(this, 35, 17);
        Shape1d.addBox(-2F, 0F, -7F, 1, 2, 14);
        Shape1d.setRotationPoint(0F, 11F, 0F);
        Shape1d.setTextureSize(128, 128);
        Shape1d.mirror = true;
        this.setRotation(Shape1d, 0F, 0.7853982F, 0F);
        Shape1e = new LODModelPart(this, 35, 0);
        Shape1e.addBox(1F, 0F, -7F, 1, 2, 14);
        Shape1e.setRotationPoint(0F, 11F, 0F);
        Shape1e.setTextureSize(128, 128);
        Shape1e.mirror = true;
        this.setRotation(Shape1e, 0F, 0.7853982F, 0F);
        Shape1f = new LODModelPart(this, 0, 17);
        Shape1f.addBox(-1F, 0F, -7F, 2, 1, 14);
        Shape1f.setRotationPoint(0F, 10F, 0F);
        Shape1f.setTextureSize(128, 128);
        Shape1f.mirror = true;
        this.setRotation(Shape1f, 0F, 1.570796F, 0F);
        Shape1g = new LODModelPart(this, 35, 0);
        Shape1g.addBox(1F, 0F, -7F, 1, 2, 14);
        Shape1g.setRotationPoint(0F, 11F, 0F);
        Shape1g.setTextureSize(128, 128);
        Shape1g.mirror = true;
        this.setRotation(Shape1g, 0F, 1.570796F, 0F);
        Shape1j = new LODModelPart(this, 35, 17);
        Shape1j.addBox(-2F, 0F, -7F, 1, 2, 14);
        Shape1j.setRotationPoint(0F, 11F, 0F);
        Shape1j.setTextureSize(128, 128);
        Shape1j.mirror = true;
        this.setRotation(Shape1j, 0F, 1.570796F, 0F);
        Shape1h = new LODModelPart(this, 0, 0);
        Shape1h.addBox(-1F, 0F, -7F, 2, 1, 14);
        Shape1h.setRotationPoint(0F, 13F, 0F);
        Shape1h.setTextureSize(128, 128);
        Shape1h.mirror = true;
        this.setRotation(Shape1h, 0F, 1.570796F, 0F);
        Shape1r = new LODModelPart(this, 35, 17);
        Shape1r.addBox(-2F, 0F, -7F, 1, 2, 14);
        Shape1r.setRotationPoint(0F, 11F, 0F);
        Shape1r.setTextureSize(128, 128);
        Shape1r.mirror = true;
        this.setRotation(Shape1r, 0F, -0.7853982F, 0F);
        Shape1k = new LODModelPart(this, 35, 0);
        Shape1k.addBox(1F, 0F, -7F, 1, 2, 14);
        Shape1k.setRotationPoint(0F, 11F, 0F);
        Shape1k.setTextureSize(128, 128);
        Shape1k.mirror = true;
        this.setRotation(Shape1k, 0F, -0.7853982F, 0F);
        Shape1l = new LODModelPart(this, 0, 0);
        Shape1l.addBox(-1F, 0F, -7F, 2, 1, 14);
        Shape1l.setRotationPoint(0F, 13F, 0F);
        Shape1l.setTextureSize(128, 128);
        Shape1l.mirror = true;
        this.setRotation(Shape1l, 0F, -0.7853982F, 0F);
        Shape1m = new LODModelPart(this, 0, 17);
        Shape1m.addBox(-1F, 0F, -7F, 2, 1, 14);
        Shape1m.setRotationPoint(0F, 10F, 0F);
        Shape1m.setTextureSize(128, 128);
        Shape1m.mirror = true;
        this.setRotation(Shape1m, 0F, -0.7853982F, 0F);
        Shape1n = new LODModelPart(this, 0, 17);
        Shape1n.addBox(-1F, 0F, -7F, 2, 1, 14);
        Shape1n.setRotationPoint(0F, 10F, 0F);
        Shape1n.setTextureSize(128, 128);
        Shape1n.mirror = true;
        this.setRotation(Shape1n, 0F, 0F, 0F);
        Shape1o = new LODModelPart(this, 35, 17);
        Shape1o.addBox(-2F, 0F, -7F, 1, 2, 14);
        Shape1o.setRotationPoint(0F, 11F, 0F);
        Shape1o.setTextureSize(128, 128);
        Shape1o.mirror = true;
        this.setRotation(Shape1o, 0F, 0F, 0F);
        Shape1p = new LODModelPart(this, 35, 0);
        Shape1p.addBox(1F, 0F, -7F, 1, 2, 14);
        Shape1p.setRotationPoint(0F, 11F, 0F);
        Shape1p.setTextureSize(128, 128);
        Shape1p.mirror = true;
        this.setRotation(Shape1p, 0F, 0F, 0F);
        Shape1q = new LODModelPart(this, 0, 0);
        Shape1q.addBox(-1F, 0F, -7F, 2, 1, 14);
        Shape1q.setRotationPoint(0F, 13F, 0F);
        Shape1q.setTextureSize(128, 128);
        Shape1q.mirror = true;
        this.setRotation(Shape1q, 0F, 0F, 0F);
        Shape2 = new LODModelPart(this, 0, 35);
        Shape2.addBox(0F, 0F, 0F, 16, 3, 16);
        Shape2.setRotationPoint(-8F, 21F, -8F);
        Shape2.setTextureSize(128, 128);
        Shape2.mirror = true;
        this.setRotation(Shape2, 0F, 0F, 0F);
        Shape3 = new LODModelPart(this, 0, 77);
        Shape3.addBox(-1F, 0F, 3F, 2, 3, 1);
        Shape3.setRotationPoint(0F, 14F, 0F);
        Shape3.setTextureSize(128, 128);
        Shape3.mirror = true;
        this.setRotation(Shape3, 0F, -1.570796F, 0F);
        Shape3a = new LODModelPart(this, 0, 77);
        Shape3a.addBox(-1F, 0F, 3F, 2, 3, 1);
        Shape3a.setRotationPoint(0F, 14F, 0F);
        Shape3a.setTextureSize(128, 128);
        Shape3a.mirror = true;
        this.setRotation(Shape3a, 0F, 0F, 0F);
        Shape3b = new LODModelPart(this, 0, 77);
        Shape3b.addBox(-1F, 0F, 3F, 2, 3, 1);
        Shape3b.setRotationPoint(0F, 14F, 0F);
        Shape3b.setTextureSize(128, 128);
        Shape3b.mirror = true;
        this.setRotation(Shape3b, 0F, 1.570796F, 0F);
        Shape3c = new LODModelPart(this, 0, 77);
        Shape3c.addBox(-1F, 0F, 3F, 2, 3, 1);
        Shape3c.setRotationPoint(0F, 14F, 0F);
        Shape3c.setTextureSize(128, 128);
        Shape3c.mirror = true;
        this.setRotation(Shape3c, 0F, 3.141593F, 0F);
        Shape4 = new LODModelPart(this, 0, 56);
        Shape4.addBox(-5F, 0F, -5F, 10, 1, 10);
        Shape4.setRotationPoint(0F, 17F, 0F);
        Shape4.setTextureSize(128, 128);
        Shape4.mirror = true;
        this.setRotation(Shape4, 0F, 1.178097F, 0F);
        Shape4a = new LODModelPart(this, 0, 56);
        Shape4a.addBox(-5F, 0F, -5F, 10, 1, 10);
        Shape4a.setRotationPoint(0F, 17F, 0F);
        Shape4a.setTextureSize(128, 128);
        Shape4a.mirror = true;
        this.setRotation(Shape4a, 0F, 0F, 0F);
        Shape4b = new LODModelPart(this, 0, 56);
        Shape4b.addBox(-5F, 0F, -5F, 10, 1, 10);
        Shape4b.setRotationPoint(0F, 17F, 0F);
        Shape4b.setTextureSize(128, 128);
        Shape4b.mirror = true;
        this.setRotation(Shape4b, 0F, 0.7853982F, 0F);
        Shape4c = new LODModelPart(this, 0, 56);
        Shape4c.addBox(-5F, 0F, -5F, 10, 1, 10);
        Shape4c.setRotationPoint(0F, 17F, 0F);
        Shape4c.setTextureSize(128, 128);
        Shape4c.mirror = true;
        this.setRotation(Shape4c, 0F, 0.3926991F, 0F);
        Shape2a = new LODModelPart(this, 66, 35);
        Shape2a.addBox(0F, 0F, 0F, 14, 3, 14);
        Shape2a.setRotationPoint(-7F, 18F, -7F);
        Shape2a.setTextureSize(128, 128);
        Shape2a.mirror = true;
        this.setRotation(Shape2a, 0F, 0F, 0F);
        Shape5 = new LODModelPart(this, 0, 68);
        Shape5.addBox(-1F, 0F, -1F, 2, 3, 2);
        Shape5.setRotationPoint(0F, 14F, 0F);
        Shape5.setTextureSize(128, 128);
        Shape5.mirror = true;
        this.setRotation(Shape5, 0F, 0F, 0F);
    }

    @Override
    public void renderAll(TileEntity te, ArrayList li, float phi, float theta) {
        Shape2.render(te, f5);
        Shape2a.render(te, f5);

        GL11.glRotatef(phi, 0, 1, 0);
        Shape1.render(te, f5);
        Shape1a.render(te, f5);
        Shape1b.render(te, f5);
        Shape1d.render(te, f5);
        Shape1e.render(te, f5);
        Shape1f.render(te, f5);
        Shape1g.render(te, f5);
        Shape1j.render(te, f5);
        Shape1h.render(te, f5);
        Shape1r.render(te, f5);
        Shape1k.render(te, f5);
        Shape1l.render(te, f5);
        Shape1m.render(te, f5);
        Shape1n.render(te, f5);
        Shape1o.render(te, f5);
        Shape1p.render(te, f5);
        Shape1q.render(te, f5);
        Shape3.render(te, f5);
        Shape3a.render(te, f5);
        Shape3b.render(te, f5);
        Shape3c.render(te, f5);
        Shape4.render(te, f5);
        Shape4a.render(te, f5);
        Shape4b.render(te, f5);
        Shape4c.render(te, f5);
        Shape5.render(te, f5);
        GL11.glRotatef(-phi, 0, 1, 0);
    }

    @Override
    public void
    setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5);
    }
}
