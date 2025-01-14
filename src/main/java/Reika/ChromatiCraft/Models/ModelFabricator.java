/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
// Date: 31/10/2014 7:19:32 PM
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

public class ModelFabricator extends ChromaModelBase {
    //fields
    LODModelPart Shape1;
    LODModelPart Shape2;
    LODModelPart Shape3;
    LODModelPart Shape4;
    LODModelPart Shape4a;
    LODModelPart Shape3a;
    LODModelPart Shape5;
    LODModelPart Shape5a;
    LODModelPart Shape5b;
    LODModelPart Shape5c;
    LODModelPart Shape6;
    LODModelPart Shape6a;
    LODModelPart Shape6c;
    LODModelPart Shape6b;
    LODModelPart Shape3b;
    LODModelPart Shape3c;
    LODModelPart Shape3d;
    LODModelPart Shape2a;
    LODModelPart Shape2b;
    LODModelPart Shape2c;
    LODModelPart Shape2d;
    LODModelPart Shape2e;
    LODModelPart Shape2f;
    LODModelPart Shape2g;
    LODModelPart Shape2h;
    LODModelPart Shape2i;
    LODModelPart Shape2j;
    LODModelPart Shape2k;
    LODModelPart Shape2l;
    LODModelPart Shape2m;
    LODModelPart Shape2n;
    LODModelPart Shape2o;
    LODModelPart Shape2p;
    LODModelPart Shape2q;
    LODModelPart Shape2r;
    LODModelPart Shape2s;
    LODModelPart Shape2t;
    LODModelPart Shape2u;
    LODModelPart Shape2v;
    LODModelPart Shape2w;
    LODModelPart Shape2x;
    LODModelPart Shape2y;
    LODModelPart Shape2z;
    LODModelPart Shape2aa;
    LODModelPart Shape2ab;
    LODModelPart Shape2ac;
    LODModelPart Shape2ad;
    LODModelPart Shape2ae;

    public ModelFabricator() {
        textureWidth = 128;
        textureHeight = 128;

        Shape1 = new LODModelPart(this, 0, 19);
        Shape1.addBox(0F, 0F, 0F, 16, 1, 16);
        Shape1.setRotationPoint(-8F, 23F, -8F);
        Shape1.setTextureSize(128, 128);
        Shape1.mirror = true;
        this.setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new LODModelPart(this, 0, 0);
        Shape2.addBox(-1F, 0F, 0F, 2, 1, 16);
        Shape2.setRotationPoint(0F, 21F, 0F);
        Shape2.setTextureSize(128, 128);
        Shape2.mirror = true;
        this.setRotation(Shape2, 0.5235988F, -2.748893F, 0F);
        Shape3 = new LODModelPart(this, 0, 51);
        Shape3.addBox(-3F, 0F, -3F, 6, 1, 6);
        Shape3.setRotationPoint(0F, 18.5F, 0F);
        Shape3.setTextureSize(128, 128);
        Shape3.mirror = true;
        this.setRotation(Shape3, 0F, 0.7853982F, 0F);
        Shape4 = new LODModelPart(this, 0, 43);
        Shape4.addBox(0F, 0F, 0F, 10, 1, 6);
        Shape4.setRotationPoint(-5F, 22F, -3F);
        Shape4.setTextureSize(128, 128);
        Shape4.mirror = true;
        this.setRotation(Shape4, 0F, 0F, 0F);
        Shape4a = new LODModelPart(this, 34, 37);
        Shape4a.addBox(0F, 0F, 0F, 6, 1, 10);
        Shape4a.setRotationPoint(-3F, 22F, -5F);
        Shape4a.setTextureSize(128, 128);
        Shape4a.mirror = true;
        this.setRotation(Shape4a, 0F, 0F, 0F);
        Shape3a = new LODModelPart(this, 70, 0);
        Shape3a.addBox(0F, 0F, 0F, 8, 4, 8);
        Shape3a.setRotationPoint(-4F, 19F, -4F);
        Shape3a.setTextureSize(128, 128);
        Shape3a.mirror = true;
        this.setRotation(Shape3a, 0F, 0F, 0F);
        Shape5 = new LODModelPart(this, 112, 0);
        Shape5.addBox(0F, 0F, 0F, 2, 3, 2);
        Shape5.setRotationPoint(5F, 20F, 5F);
        Shape5.setTextureSize(128, 128);
        Shape5.mirror = true;
        this.setRotation(Shape5, 0F, 0F, 0F);
        Shape5a = new LODModelPart(this, 112, 6);
        Shape5a.addBox(0F, 0F, 0F, 2, 3, 2);
        Shape5a.setRotationPoint(-7F, 20F, 5F);
        Shape5a.setTextureSize(128, 128);
        Shape5a.mirror = true;
        this.setRotation(Shape5a, 0F, 0F, 0F);
        Shape5b = new LODModelPart(this, 103, 0);
        Shape5b.addBox(0F, 0F, 0F, 2, 3, 2);
        Shape5b.setRotationPoint(5F, 20F, -7F);
        Shape5b.setTextureSize(128, 128);
        Shape5b.mirror = true;
        this.setRotation(Shape5b, 0F, 0F, 0F);
        Shape5c = new LODModelPart(this, 103, 6);
        Shape5c.addBox(0F, 0F, 0F, 2, 3, 2);
        Shape5c.setRotationPoint(-7F, 20F, -7F);
        Shape5c.setTextureSize(128, 128);
        Shape5c.mirror = true;
        this.setRotation(Shape5c, 0F, 0F, 0F);
        Shape6 = new LODModelPart(this, 0, 38);
        Shape6.addBox(0F, 0F, 0F, 12, 1, 3);
        Shape6.setRotationPoint(-6F, 22.5F, 4.5F);
        Shape6.setTextureSize(128, 128);
        Shape6.mirror = true;
        this.setRotation(Shape6, 0F, 0F, 0F);
        Shape6a = new LODModelPart(this, 38, 0);
        Shape6a.addBox(0F, 0F, 0F, 3, 1, 12);
        Shape6a.setRotationPoint(4.5F, 22.5F, -6F);
        Shape6a.setTextureSize(128, 128);
        Shape6a.mirror = true;
        this.setRotation(Shape6a, 0F, 0F, 0F);
        Shape6c = new LODModelPart(this, 0, 38);
        Shape6c.addBox(0F, 0F, 0F, 12, 1, 3);
        Shape6c.setRotationPoint(-6F, 22.5F, -7.5F);
        Shape6c.setTextureSize(128, 128);
        Shape6c.mirror = true;
        this.setRotation(Shape6c, 0F, 0F, 0F);
        Shape6b = new LODModelPart(this, 38, 0);
        Shape6b.addBox(0F, 0F, 0F, 3, 1, 12);
        Shape6b.setRotationPoint(-7.5F, 22.5F, -6F);
        Shape6b.setTextureSize(128, 128);
        Shape6b.mirror = true;
        this.setRotation(Shape6b, 0F, 0F, 0F);
        Shape2a = new LODModelPart(this, 0, 59);
        Shape2a.addBox(-1.5F, -0.1F, 14F, 3, 1, 4);
        Shape2a.setRotationPoint(0F, 21F, 0F);
        Shape2a.setTextureSize(128, 128);
        Shape2a.mirror = true;
        this.setRotation(Shape2a, 0.5235988F, -2.748893F, 0F);
        Shape2b = new LODModelPart(this, 0, 0);
        Shape2b.addBox(-1F, 0F, 0F, 2, 1, 16);
        Shape2b.setRotationPoint(0F, 21F, 0F);
        Shape2b.setTextureSize(128, 128);
        Shape2b.mirror = true;
        this.setRotation(Shape2b, 0.5235988F, 0.3926991F, 0F);
        Shape2c = new LODModelPart(this, 0, 0);
        Shape2c.addBox(-1F, 0F, 0F, 2, 1, 16);
        Shape2c.setRotationPoint(0F, 21F, 0F);
        Shape2c.setTextureSize(128, 128);
        Shape2c.mirror = true;
        this.setRotation(Shape2c, 0.5235988F, 0.7853982F, 0F);
        Shape2d = new LODModelPart(this, 0, 0);
        Shape2d.addBox(-1F, 0F, 0F, 2, 1, 16);
        Shape2d.setRotationPoint(0F, 21F, 0F);
        Shape2d.setTextureSize(128, 128);
        Shape2d.mirror = true;
        this.setRotation(Shape2d, 0.5235988F, 1.178097F, 0F);
        Shape2e = new LODModelPart(this, 0, 0);
        Shape2e.addBox(-1F, 0F, 0F, 2, 1, 16);
        Shape2e.setRotationPoint(0F, 21F, 0F);
        Shape2e.setTextureSize(128, 128);
        Shape2e.mirror = true;
        this.setRotation(Shape2e, 0.5235988F, 1.570796F, 0F);
        Shape2f = new LODModelPart(this, 0, 0);
        Shape2f.addBox(-1F, 0F, 0F, 2, 1, 16);
        Shape2f.setRotationPoint(0F, 21F, 0F);
        Shape2f.setTextureSize(128, 128);
        Shape2f.mirror = true;
        this.setRotation(Shape2f, 0.5235988F, 2.356194F, 0F);
        Shape2g = new LODModelPart(this, 0, 0);
        Shape2g.addBox(-1F, 0F, 0F, 2, 1, 16);
        Shape2g.setRotationPoint(0F, 21F, 0F);
        Shape2g.setTextureSize(128, 128);
        Shape2g.mirror = true;
        this.setRotation(Shape2g, 0.5235988F, 3.141593F, 0F);
        Shape2h = new LODModelPart(this, 0, 0);
        Shape2h.addBox(-1F, 0F, 0F, 2, 1, 16);
        Shape2h.setRotationPoint(0F, 21F, 0F);
        Shape2h.setTextureSize(128, 128);
        Shape2h.mirror = true;
        this.setRotation(Shape2h, 0.5235988F, -0.3926991F, 0F);
        Shape2i = new LODModelPart(this, 0, 0);
        Shape2i.addBox(-1F, 0F, 0F, 2, 1, 16);
        Shape2i.setRotationPoint(0F, 21F, 0F);
        Shape2i.setTextureSize(128, 128);
        Shape2i.mirror = true;
        this.setRotation(Shape2i, 0.5235988F, -0.7853982F, 0F);
        Shape2j = new LODModelPart(this, 0, 0);
        Shape2j.addBox(-1F, 0F, 0F, 2, 1, 16);
        Shape2j.setRotationPoint(0F, 21F, 0F);
        Shape2j.setTextureSize(128, 128);
        Shape2j.mirror = true;
        this.setRotation(Shape2j, 0.5235988F, -2.356194F, 0F);
        Shape2k = new LODModelPart(this, 0, 0);
        Shape2k.addBox(-1F, 0F, 0F, 2, 1, 16);
        Shape2k.setRotationPoint(0F, 21F, 0F);
        Shape2k.setTextureSize(128, 128);
        Shape2k.mirror = true;
        this.setRotation(Shape2k, 0.5235988F, -1.570796F, 0F);
        Shape2l = new LODModelPart(this, 0, 0);
        Shape2l.addBox(-1F, 0F, 0F, 2, 1, 16);
        Shape2l.setRotationPoint(0F, 21F, 0F);
        Shape2l.setTextureSize(128, 128);
        Shape2l.mirror = true;
        this.setRotation(Shape2l, 0.5235988F, -1.178097F, 0F);
        Shape2m = new LODModelPart(this, 0, 0);
        Shape2m.addBox(-1F, 0F, 0F, 2, 1, 16);
        Shape2m.setRotationPoint(0F, 21F, 0F);
        Shape2m.setTextureSize(128, 128);
        Shape2m.mirror = true;
        this.setRotation(Shape2m, 0.5235988F, 1.963495F, 0F);
        Shape2n = new LODModelPart(this, 0, 0);
        Shape2n.addBox(-1F, 0F, 0F, 2, 1, 16);
        Shape2n.setRotationPoint(0F, 21F, 0F);
        Shape2n.setTextureSize(128, 128);
        Shape2n.mirror = true;
        this.setRotation(Shape2n, 0.5235988F, -1.963495F, 0F);
        Shape2o = new LODModelPart(this, 0, 0);
        Shape2o.addBox(-1F, 0F, 0F, 2, 1, 16);
        Shape2o.setRotationPoint(0F, 21F, 0F);
        Shape2o.setTextureSize(128, 128);
        Shape2o.mirror = true;
        this.setRotation(Shape2o, 0.5235988F, 2.748893F, 0F);
        Shape3b = new LODModelPart(this, 25, 51);
        Shape3b.addBox(-2F, 0F, -2F, 4, 1, 4);
        Shape3b.setRotationPoint(0F, 18F, 0F);
        Shape3b.setTextureSize(128, 128);
        Shape3b.mirror = true;
        this.setRotation(Shape3b, 0F, 0.7853982F, 0F);
        Shape3c = new LODModelPart(this, 0, 51);
        Shape3c.addBox(-3F, 0F, -3F, 6, 1, 6);
        Shape3c.setRotationPoint(0F, 18.5F, 0F);
        Shape3c.setTextureSize(128, 128);
        Shape3c.mirror = true;
        this.setRotation(Shape3c, 0F, 0F, 0F);
        Shape3d = new LODModelPart(this, 25, 51);
        Shape3d.addBox(-2F, 0F, -2F, 4, 1, 4);
        Shape3d.setRotationPoint(0F, 18F, 0F);
        Shape3d.setTextureSize(128, 128);
        Shape3d.mirror = true;
        this.setRotation(Shape3d, 0F, 0F, 0F);
        Shape2p = new LODModelPart(this, 0, 0);
        Shape2p.addBox(-1F, 0F, 0F, 2, 1, 16);
        Shape2p.setRotationPoint(0F, 21F, 0F);
        Shape2p.setTextureSize(128, 128);
        Shape2p.mirror = true;
        this.setRotation(Shape2p, 0.5235988F, 0F, 0F);

        Shape2q = new LODModelPart(this, 0, 59);
        Shape2q.addBox(-1.5F, -0.1F, 14F, 3, 1, 4);
        Shape2q.setRotationPoint(0F, 21F, 0F);
        Shape2q.setTextureSize(128, 128);
        Shape2q.mirror = true;
        this.setRotation(Shape2q, 0.5235988F, 0F, 0F);
        Shape2r = new LODModelPart(this, 0, 59);
        Shape2r.addBox(-1.5F, -0.1F, 14F, 3, 1, 4);
        Shape2r.setRotationPoint(0F, 21F, 0F);
        Shape2r.setTextureSize(128, 128);
        Shape2r.mirror = true;
        this.setRotation(Shape2r, 0.5235988F, 0.7853982F, 0F);
        Shape2s = new LODModelPart(this, 0, 59);
        Shape2s.addBox(-1.5F, -0.1F, 14F, 3, 1, 4);
        Shape2s.setRotationPoint(0F, 21F, 0F);
        Shape2s.setTextureSize(128, 128);
        Shape2s.mirror = true;
        this.setRotation(Shape2s, 0.5235988F, 1.570796F, 0F);
        Shape2t = new LODModelPart(this, 0, 59);
        Shape2t.addBox(-1.5F, -0.1F, 14F, 3, 1, 4);
        Shape2t.setRotationPoint(0F, 21F, 0F);
        Shape2t.setTextureSize(128, 128);
        Shape2t.mirror = true;
        this.setRotation(Shape2t, 0.5235988F, 2.356194F, 0F);
        Shape2u = new LODModelPart(this, 0, 59);
        Shape2u.addBox(-1.5F, -0.1F, 14F, 3, 1, 4);
        Shape2u.setRotationPoint(0F, 21F, 0F);
        Shape2u.setTextureSize(128, 128);
        Shape2u.mirror = true;
        this.setRotation(Shape2u, 0.5235988F, -0.7853982F, 0F);
        Shape2v = new LODModelPart(this, 0, 59);
        Shape2v.addBox(-1.5F, -0.1F, 14F, 3, 1, 4);
        Shape2v.setRotationPoint(0F, 21F, 0F);
        Shape2v.setTextureSize(128, 128);
        Shape2v.mirror = true;
        this.setRotation(Shape2v, 0.5235988F, -1.570796F, 0F);
        Shape2w = new LODModelPart(this, 0, 59);
        Shape2w.addBox(-1.5F, -0.1F, 14F, 3, 1, 4);
        Shape2w.setRotationPoint(0F, 21F, 0F);
        Shape2w.setTextureSize(128, 128);
        Shape2w.mirror = true;
        this.setRotation(Shape2w, 0.5235988F, 3.141593F, 0F);
        Shape2x = new LODModelPart(this, 0, 59);
        Shape2x.addBox(-1.5F, -0.1F, 14F, 3, 1, 4);
        Shape2x.setRotationPoint(0F, 21F, 0F);
        Shape2x.setTextureSize(128, 128);
        Shape2x.mirror = true;
        this.setRotation(Shape2x, 0.5235988F, -2.356194F, 0F);
        Shape2y = new LODModelPart(this, 0, 59);
        Shape2y.addBox(-1.5F, -0.1F, 14F, 3, 1, 4);
        Shape2y.setRotationPoint(0F, 21F, 0F);
        Shape2y.setTextureSize(128, 128);
        Shape2y.mirror = true;
        this.setRotation(Shape2y, 0.5235988F, 0.3926991F, 0F);
        Shape2z = new LODModelPart(this, 0, 59);
        Shape2z.addBox(-1.5F, -0.1F, 14F, 3, 1, 4);
        Shape2z.setRotationPoint(0F, 21F, 0F);
        Shape2z.setTextureSize(128, 128);
        Shape2z.mirror = true;
        this.setRotation(Shape2z, 0.5235988F, 1.178097F, 0F);
        Shape2aa = new LODModelPart(this, 0, 59);
        Shape2aa.addBox(-1.5F, -0.1F, 14F, 3, 1, 4);
        Shape2aa.setRotationPoint(0F, 21F, 0F);
        Shape2aa.setTextureSize(128, 128);
        Shape2aa.mirror = true;
        this.setRotation(Shape2aa, 0.5235988F, -0.3926991F, 0F);
        Shape2ab = new LODModelPart(this, 0, 59);
        Shape2ab.addBox(-1.5F, -0.1F, 14F, 3, 1, 4);
        Shape2ab.setRotationPoint(0F, 21F, 0F);
        Shape2ab.setTextureSize(128, 128);
        Shape2ab.mirror = true;
        this.setRotation(Shape2ab, 0.5235988F, -1.178097F, 0F);
        Shape2ac = new LODModelPart(this, 0, 59);
        Shape2ac.addBox(-1.5F, -0.1F, 14F, 3, 1, 4);
        Shape2ac.setRotationPoint(0F, 21F, 0F);
        Shape2ac.setTextureSize(128, 128);
        Shape2ac.mirror = true;
        this.setRotation(Shape2ac, 0.5235988F, 1.963495F, 0F);
        Shape2ad = new LODModelPart(this, 0, 59);
        Shape2ad.addBox(-1.5F, -0.1F, 14F, 3, 1, 4);
        Shape2ad.setRotationPoint(0F, 21F, 0F);
        Shape2ad.setTextureSize(128, 128);
        Shape2ad.mirror = true;
        this.setRotation(Shape2ad, 0.5235988F, -1.963495F, 0F);
        Shape2ae = new LODModelPart(this, 0, 59);
        Shape2ae.addBox(-1.5F, -0.1F, 14F, 3, 1, 4);
        Shape2ae.setRotationPoint(0F, 21F, 0F);
        Shape2ae.setTextureSize(128, 128);
        Shape2ae.mirror = true;
        this.setRotation(Shape2ae, 0.5235988F, 2.748893F, 0F);
    }

    @Override
    public void renderAll(TileEntity te, ArrayList li) {
        Shape1.render(te, f5);
        Shape3.render(te, f5);
        Shape4.render(te, f5);
        Shape4a.render(te, f5);
        Shape3a.render(te, f5);
        Shape5.render(te, f5);
        Shape5a.render(te, f5);
        Shape5b.render(te, f5);
        Shape5c.render(te, f5);
        Shape6.render(te, f5);
        Shape6a.render(te, f5);
        Shape6c.render(te, f5);
        Shape6b.render(te, f5);
        Shape3b.render(te, f5);
        Shape3c.render(te, f5);
        Shape3d.render(te, f5);

        double s = te.hasWorldObj() ? 1.75 : 0.8;
        GL11.glPushMatrix();
        double x = 0;
        double y = te.hasWorldObj() ? -1 : 0.125;
        double z = 0;
        GL11.glTranslated(x, y, z);
        GL11.glScaled(s, s, s);
        Shape2.render(te, f5);
        Shape2a.render(te, f5);
        Shape2b.render(te, f5);
        Shape2c.render(te, f5);
        Shape2d.render(te, f5);
        Shape2e.render(te, f5);
        Shape2f.render(te, f5);
        Shape2g.render(te, f5);
        Shape2h.render(te, f5);
        Shape2i.render(te, f5);
        Shape2j.render(te, f5);
        Shape2k.render(te, f5);
        Shape2l.render(te, f5);
        Shape2m.render(te, f5);
        Shape2n.render(te, f5);
        Shape2o.render(te, f5);
        Shape2p.render(te, f5);
        Shape2q.render(te, f5);
        Shape2r.render(te, f5);
        Shape2s.render(te, f5);
        Shape2t.render(te, f5);
        Shape2u.render(te, f5);
        Shape2v.render(te, f5);
        Shape2w.render(te, f5);
        Shape2x.render(te, f5);
        Shape2y.render(te, f5);
        Shape2z.render(te, f5);
        Shape2aa.render(te, f5);
        Shape2ab.render(te, f5);
        Shape2ac.render(te, f5);
        Shape2ad.render(te, f5);
        Shape2ae.render(te, f5);
        GL11.glPopMatrix();
    }

    @Override
    public void
    setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5);
    }
}
