/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
// Date: 09/03/2013 5:03:30 PM
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

public class ModelWoodcutter extends RotaryModelBase {
    //fields
    LODModelPart Shape1;
    LODModelPart Shape2;
    LODModelPart Shape3;
    LODModelPart Shape4;
    LODModelPart Shape5a;
    LODModelPart Shape5b;
    LODModelPart Shape5d;
    LODModelPart Shape5e;
    LODModelPart Shape5f;
    LODModelPart Shape5;
    LODModelPart Shape5c;
    LODModelPart Shape5g;
    LODModelPart Shape6;
    LODModelPart Shape7;
    LODModelPart Shape8;
    LODModelPart Shape8a;
    LODModelPart Shape8b;
    LODModelPart Shape8c;
    LODModelPart Shape8d;
    LODModelPart Shape8e;
    LODModelPart Shape8f;
    LODModelPart Shape8g;
    LODModelPart Shape8h;
    LODModelPart Shape8j;
    LODModelPart Shape8i;
    LODModelPart Shape8k;
    LODModelPart Shape8l;
    LODModelPart Shape8m;
    LODModelPart Shape8n;
    LODModelPart Shape8o;
    LODModelPart Shape9;
    LODModelPart Shape9a;
    LODModelPart Shape5fa;
    LODModelPart Shape5aa;
    LODModelPart Shape5ca;
    LODModelPart Shape51;
    LODModelPart Shape5ga;
    LODModelPart Shape5ba;
    LODModelPart Shape5ea;
    LODModelPart Shape5da;
    LODModelPart Shape5fab;
    LODModelPart Shape5fa1;
    LODModelPart Shape8p;
    LODModelPart Shape8q;
    LODModelPart Shape8r;
    LODModelPart Shape8s;
    LODModelPart Shape8t;
    LODModelPart Shape8u;
    LODModelPart Shape8v;
    LODModelPart Shape8w;
    LODModelPart Shape8x;
    LODModelPart Shape8y;
    LODModelPart Shape8z;
    LODModelPart Shape8aa;
    LODModelPart Shape8ab;
    LODModelPart Shape8ac;
    LODModelPart Shape8ad;
    LODModelPart Shape8ae;
    LODModelPart Shape8af;
    LODModelPart Shape8ag;
    LODModelPart Shape8ah;
    LODModelPart Shape8ai;
    LODModelPart Shape8aj;
    LODModelPart Shape8ak;
    LODModelPart Shape8al;
    LODModelPart Shape8am;
    LODModelPart Shape8an;
    LODModelPart Shape8ao;
    LODModelPart Shape8ap;
    LODModelPart Shape8aq;
    LODModelPart Shape8ar;
    LODModelPart Shape8as;
    LODModelPart Shape8at;
    LODModelPart Shape8au;
    LODModelPart Shape8av;
    LODModelPart Shape8aw;
    LODModelPart Shape8ax;
    LODModelPart Shape8ay;
    LODModelPart Shape8az;
    LODModelPart Shape8ba;
    LODModelPart Shape8bb;
    LODModelPart Shape8bc;
    LODModelPart Shape8bd;
    LODModelPart Shape8be;
    LODModelPart Shape8bf;
    LODModelPart Shape8bg;
    LODModelPart Shape8bh;
    LODModelPart Shape8bi;
    LODModelPart Shape8bj;
    LODModelPart Shape8bk;
    LODModelPart Shape9u;
    LODModelPart Shape9v;
    LODModelPart Shape9w;
    LODModelPart Shape9y;
    LODModelPart Shape9x;
    LODModelPart Shape9z;
    LODModelPart Shape10;
    LODModelPart Shape10a;
    LODModelPart Shape10b;
    LODModelPart Shape10c;
    LODModelPart Shape11;
    LODModelPart Shape11a;

    public ModelWoodcutter() {
        textureWidth = 128;
        textureHeight = 128;

        Shape1 = new LODModelPart(this, 42, 0);
        Shape1.addBox(0F, 0F, 0F, 5, 1, 16);
        Shape1.setRotationPoint(3F, 23F, -8F);
        Shape1.setTextureSize(128, 128);
        Shape1.mirror = true;
        this.setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new LODModelPart(this, 84, 0);
        Shape2.addBox(0F, 0F, 0F, 6, 1, 5);
        Shape2.setRotationPoint(-3F, 23F, -8F);
        Shape2.setTextureSize(128, 128);
        Shape2.mirror = true;
        this.setRotation(Shape2, 0F, 0F, 0F);
        Shape3 = new LODModelPart(this, 42, 0);
        Shape3.addBox(0F, 0F, 0F, 5, 1, 16);
        Shape3.setRotationPoint(-8F, 23F, -8F);
        Shape3.setTextureSize(128, 128);
        Shape3.mirror = true;
        this.setRotation(Shape3, 0F, 0F, 0F);
        Shape4 = new LODModelPart(this, 84, 0);
        Shape4.addBox(0F, 0F, 0F, 6, 1, 5);
        Shape4.setRotationPoint(-3F, 23F, 3F);
        Shape4.setTextureSize(128, 128);
        Shape4.mirror = true;
        this.setRotation(Shape4, 0F, 0F, 0F);
        Shape5a = new LODModelPart(this, 84, 6);
        Shape5a.addBox(0F, 0F, 0F, 1, 2, 16);
        Shape5a.setRotationPoint(7F, 21F, -8F);
        Shape5a.setTextureSize(128, 128);
        Shape5a.mirror = true;
        this.setRotation(Shape5a, 0F, 0F, 0F);
        Shape5b = new LODModelPart(this, 101, 38);
        Shape5b.addBox(0F, 0F, 0F, 1, 1, 12);
        Shape5b.setRotationPoint(7F, 16F, -4F);
        Shape5b.setTextureSize(128, 128);
        Shape5b.mirror = true;
        this.setRotation(Shape5b, 0F, 0F, 0F);
        Shape5d = new LODModelPart(this, 57, 34);
        Shape5d.addBox(0F, 0F, 0F, 1, 1, 9);
        Shape5d.setRotationPoint(7F, 14F, -1F);
        Shape5d.setTextureSize(128, 128);
        Shape5d.mirror = true;
        this.setRotation(Shape5d, 0F, 0F, 0F);
        Shape5e = new LODModelPart(this, 77, 38);
        Shape5e.addBox(0F, 0F, 0F, 1, 1, 11);
        Shape5e.setRotationPoint(7F, 15F, -3F);
        Shape5e.setTextureSize(128, 128);
        Shape5e.mirror = true;
        this.setRotation(Shape5e, 0F, 0F, 0F);
        Shape5f = new LODModelPart(this, 112, 24);
        Shape5f.addBox(0F, 0F, 0F, 1, 1, 4);
        Shape5f.setRotationPoint(7F, 12F, 4F);
        Shape5f.setTextureSize(128, 128);
        Shape5f.mirror = true;
        this.setRotation(Shape5f, 0F, 0F, 0F);
        Shape5 = new LODModelPart(this, 22, 17);
        Shape5.addBox(0F, 0F, 0F, 1, 1, 14);
        Shape5.setRotationPoint(7F, 18F, -6F);
        Shape5.setTextureSize(128, 128);
        Shape5.mirror = true;
        this.setRotation(Shape5, 0F, 0F, 0F);
        Shape5c = new LODModelPart(this, 52, 17);
        Shape5c.addBox(0F, 0F, 0F, 1, 2, 15);
        Shape5c.setRotationPoint(7F, 19F, -7F);
        Shape5c.setTextureSize(128, 128);
        Shape5c.mirror = true;
        this.setRotation(Shape5c, 0F, 0F, 0F);
        Shape5g = new LODModelPart(this, 84, 24);
        Shape5g.addBox(0F, 0F, 0F, 1, 1, 13);
        Shape5g.setRotationPoint(7F, 17F, -5F);
        Shape5g.setTextureSize(128, 128);
        Shape5g.mirror = true;
        this.setRotation(Shape5g, 0F, 0F, 0F);
        Shape6 = new LODModelPart(this, 0, 32);
        Shape6.addBox(0F, 0F, 0F, 14, 12, 1);
        Shape6.setRotationPoint(-7F, 11F, 7F);
        Shape6.setTextureSize(128, 128);
        Shape6.mirror = true;
        this.setRotation(Shape6, 0F, 0F, 0F);
        Shape7 = new LODModelPart(this, 0, 28);
        Shape7.addBox(0F, 0F, 0F, 8, 1, 1);
        Shape7.setRotationPoint(-4F, 10F, 7F);
        Shape7.setTextureSize(128, 128);
        Shape7.mirror = true;
        this.setRotation(Shape7, 0F, 0F, 0F);
        Shape8 = new LODModelPart(this, 0, 0);
        Shape8.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8.setRotationPoint(1F, 16F, -1F);
        Shape8.setTextureSize(128, 128);
        Shape8.mirror = true;
        this.setRotation(Shape8, 0.3926991F, 0F, 0F);
        Shape8a = new LODModelPart(this, 0, 0);
        Shape8a.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8a.setRotationPoint(1F, 16F, -1F);
        Shape8a.setTextureSize(128, 128);
        Shape8a.mirror = true;
        this.setRotation(Shape8a, 1.178097F, 0F, 0F);
        Shape8b = new LODModelPart(this, 0, 0);
        Shape8b.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8b.setRotationPoint(1F, 16F, -1F);
        Shape8b.setTextureSize(128, 128);
        Shape8b.mirror = true;
        this.setRotation(Shape8b, 1.963495F, 0F, 0F);
        Shape8c = new LODModelPart(this, 0, 0);
        Shape8c.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8c.setRotationPoint(-2F, 16F, -1F);
        Shape8c.setTextureSize(128, 128);
        Shape8c.mirror = true;
        this.setRotation(Shape8c, 0.7853982F, 0F, 0F);
        Shape8d = new LODModelPart(this, 0, 0);
        Shape8d.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8d.setRotationPoint(1F, 16F, -1F);
        Shape8d.setTextureSize(128, 128);
        Shape8d.mirror = true;
        this.setRotation(Shape8d, 2.356194F, 0F, 0F);
        Shape8e = new LODModelPart(this, 0, 0);
        Shape8e.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8e.setRotationPoint(1F, 16F, -1F);
        Shape8e.setTextureSize(128, 128);
        Shape8e.mirror = true;
        this.setRotation(Shape8e, 2.356194F, 0F, 0F);
        Shape8f = new LODModelPart(this, 0, 0);
        Shape8f.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8f.setRotationPoint(1F, 16F, -1F);
        Shape8f.setTextureSize(128, 128);
        Shape8f.mirror = true;
        this.setRotation(Shape8f, 2.748893F, 0F, 0F);
        Shape8g = new LODModelPart(this, 0, 0);
        Shape8g.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8g.setRotationPoint(1F, 16F, -1F);
        Shape8g.setTextureSize(128, 128);
        Shape8g.mirror = true;
        this.setRotation(Shape8g, 0F, 0F, 0F);
        Shape8h = new LODModelPart(this, 0, 0);
        Shape8h.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8h.setRotationPoint(-2F, 16F, -1F);
        Shape8h.setTextureSize(128, 128);
        Shape8h.mirror = true;
        this.setRotation(Shape8h, 1.963495F, 0F, 0F);
        Shape8j = new LODModelPart(this, 0, 0);
        Shape8j.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8j.setRotationPoint(1F, 16F, -1F);
        Shape8j.setTextureSize(128, 128);
        Shape8j.mirror = true;
        this.setRotation(Shape8j, 0.7853982F, 0F, 0F);
        Shape8i = new LODModelPart(this, 0, 0);
        Shape8i.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8i.setRotationPoint(1F, 16F, -1F);
        Shape8i.setTextureSize(128, 128);
        Shape8i.mirror = true;
        this.setRotation(Shape8i, 0.3926991F, 0F, 0F);
        Shape8k = new LODModelPart(this, 0, 0);
        Shape8k.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8k.setRotationPoint(1F, 16F, -1F);
        Shape8k.setTextureSize(128, 128);
        Shape8k.mirror = true;
        this.setRotation(Shape8k, 2.748893F, 0F, 0F);
        Shape8l = new LODModelPart(this, 0, 0);
        Shape8l.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8l.setRotationPoint(1F, 16F, -1F);
        Shape8l.setTextureSize(128, 128);
        Shape8l.mirror = true;
        this.setRotation(Shape8l, 0F, 0F, 0F);
        Shape8m = new LODModelPart(this, 0, 0);
        Shape8m.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8m.setRotationPoint(1F, 16F, -1F);
        Shape8m.setTextureSize(128, 128);
        Shape8m.mirror = true;
        this.setRotation(Shape8m, 1.178097F, 0F, 0F);
        Shape8n = new LODModelPart(this, 0, 0);
        Shape8n.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8n.setRotationPoint(1F, 16F, -1F);
        Shape8n.setTextureSize(128, 128);
        Shape8n.mirror = true;
        this.setRotation(Shape8n, 1.963495F, 0F, 0F);
        Shape8o = new LODModelPart(this, 0, 0);
        Shape8o.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8o.setRotationPoint(4F, 16F, -1F);
        Shape8o.setTextureSize(128, 128);
        Shape8o.mirror = true;
        this.setRotation(Shape8o, 0F, 0F, 0F);
        Shape9 = new LODModelPart(this, 32, 32);
        Shape9.addBox(0F, -3F, -3F, 1, 6, 6);
        Shape9.setRotationPoint(-7F, 16F, -1F);
        Shape9.setTextureSize(128, 128);
        Shape9.mirror = true;
        this.setRotation(Shape9, 0.3926991F, 0F, 0F);
        Shape9a = new LODModelPart(this, 0, 46);
        Shape9a.addBox(0F, -1F, -1F, 15, 2, 2);
        Shape9a.setRotationPoint(-7.5F, 16F, -1F);
        Shape9a.setTextureSize(128, 128);
        Shape9a.mirror = true;
        this.setRotation(Shape9a, 0.0174533F, 0F, 0F);
        Shape5fa = new LODModelPart(this, 61, 44);
        Shape5fa.addBox(0F, 0F, 0F, 1, 1, 7);
        Shape5fa.setRotationPoint(7F, 13F, 1F);
        Shape5fa.setTextureSize(128, 128);
        Shape5fa.mirror = true;
        this.setRotation(Shape5fa, 0F, 0F, 0F);
        Shape5aa = new LODModelPart(this, 84, 6);
        Shape5aa.addBox(0F, 0F, 0F, 1, 2, 16);
        Shape5aa.setRotationPoint(-8F, 21F, -8F);
        Shape5aa.setTextureSize(128, 128);
        Shape5aa.mirror = true;
        this.setRotation(Shape5aa, 0F, 0F, 0F);
        Shape5ca = new LODModelPart(this, 52, 17);
        Shape5ca.addBox(0F, 0F, 0F, 1, 2, 15);
        Shape5ca.setRotationPoint(-8F, 19F, -7F);
        Shape5ca.setTextureSize(128, 128);
        Shape5ca.mirror = true;
        this.setRotation(Shape5ca, 0F, 0F, 0F);
        Shape51 = new LODModelPart(this, 22, 17);
        Shape51.addBox(0F, 0F, 0F, 1, 1, 14);
        Shape51.setRotationPoint(-8F, 18F, -6F);
        Shape51.setTextureSize(128, 128);
        Shape51.mirror = true;
        this.setRotation(Shape51, 0F, 0F, 0F);
        Shape5ga = new LODModelPart(this, 84, 24);
        Shape5ga.addBox(0F, 0F, 0F, 1, 1, 13);
        Shape5ga.setRotationPoint(-8F, 17F, -5F);
        Shape5ga.setTextureSize(128, 128);
        Shape5ga.mirror = true;
        this.setRotation(Shape5ga, 0F, 0F, 0F);
        Shape5ba = new LODModelPart(this, 101, 38);
        Shape5ba.addBox(0F, 0F, 0F, 1, 1, 12);
        Shape5ba.setRotationPoint(-8F, 16F, -4F);
        Shape5ba.setTextureSize(128, 128);
        Shape5ba.mirror = true;
        this.setRotation(Shape5ba, 0F, 0F, 0F);
        Shape5ea = new LODModelPart(this, 77, 38);
        Shape5ea.addBox(0F, 0F, 0F, 1, 1, 11);
        Shape5ea.setRotationPoint(-8F, 15F, -3F);
        Shape5ea.setTextureSize(128, 128);
        Shape5ea.mirror = true;
        this.setRotation(Shape5ea, 0F, 0F, 0F);
        Shape5da = new LODModelPart(this, 57, 34);
        Shape5da.addBox(0F, 0F, 0F, 1, 1, 9);
        Shape5da.setRotationPoint(-8F, 14F, -1F);
        Shape5da.setTextureSize(128, 128);
        Shape5da.mirror = true;
        this.setRotation(Shape5da, 0F, 0F, 0F);
        Shape5fab = new LODModelPart(this, 61, 44);
        Shape5fab.addBox(0F, 0F, 0F, 1, 1, 7);
        Shape5fab.setRotationPoint(-8F, 13F, 1F);
        Shape5fab.setTextureSize(128, 128);
        Shape5fab.mirror = true;
        this.setRotation(Shape5fa, 0F, 0F, 0F);
        Shape5fa1 = new LODModelPart(this, 112, 24);
        Shape5fa1.addBox(0F, 0F, 0F, 1, 1, 4);
        Shape5fa1.setRotationPoint(-8F, 12F, 4F);
        Shape5fa1.setTextureSize(128, 128);
        Shape5fa1.mirror = true;
        this.setRotation(Shape5fa1, 0F, 0F, 0F);
        Shape8p = new LODModelPart(this, 0, 0);
        Shape8p.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8p.setRotationPoint(4F, 16F, -1F);
        Shape8p.setTextureSize(128, 128);
        Shape8p.mirror = true;
        this.setRotation(Shape8p, 0F, 0F, 0F);
        Shape8q = new LODModelPart(this, 0, 0);
        Shape8q.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8q.setRotationPoint(-2F, 16F, -1F);
        Shape8q.setTextureSize(128, 128);
        Shape8q.mirror = true;
        this.setRotation(Shape8q, 1.178097F, 0F, 0F);
        Shape8r = new LODModelPart(this, 0, 0);
        Shape8r.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8r.setRotationPoint(1F, 16F, -1F);
        Shape8r.setTextureSize(128, 128);
        Shape8r.mirror = true;
        this.setRotation(Shape8r, 0.7853982F, 0F, 0F);
        Shape8s = new LODModelPart(this, 0, 0);
        Shape8s.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8s.setRotationPoint(-2F, 16F, -1F);
        Shape8s.setTextureSize(128, 128);
        Shape8s.mirror = true;
        this.setRotation(Shape8s, 2.356194F, 0F, 0F);
        Shape8t = new LODModelPart(this, 0, 0);
        Shape8t.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8t.setRotationPoint(-2F, 16F, -1F);
        Shape8t.setTextureSize(128, 128);
        Shape8t.mirror = true;
        this.setRotation(Shape8t, 0.3926991F, 0F, 0F);
        Shape8u = new LODModelPart(this, 0, 0);
        Shape8u.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8u.setRotationPoint(-2F, 16F, -1F);
        Shape8u.setTextureSize(128, 128);
        Shape8u.mirror = true;
        this.setRotation(Shape8u, 2.748893F, 0F, 0F);
        Shape8v = new LODModelPart(this, 0, 0);
        Shape8v.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8v.setRotationPoint(-2F, 16F, -1F);
        Shape8v.setTextureSize(128, 128);
        Shape8v.mirror = true;
        this.setRotation(Shape8v, 0F, 0F, 0F);
        Shape8w = new LODModelPart(this, 0, 0);
        Shape8w.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8w.setRotationPoint(-5F, 16F, -1F);
        Shape8w.setTextureSize(128, 128);
        Shape8w.mirror = true;
        this.setRotation(Shape8w, 0F, 0F, 0F);
        Shape8x = new LODModelPart(this, 0, 0);
        Shape8x.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8x.setRotationPoint(-2F, 16F, -1F);
        Shape8x.setTextureSize(128, 128);
        Shape8x.mirror = true;
        this.setRotation(Shape8x, 1.570796F, 0F, 0F);
        Shape8y = new LODModelPart(this, 0, 0);
        Shape8y.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8y.setRotationPoint(-5F, 16F, -1F);
        Shape8y.setTextureSize(128, 128);
        Shape8y.mirror = true;
        this.setRotation(Shape8y, 1.570796F, 0F, 0F);
        Shape8z = new LODModelPart(this, 0, 0);
        Shape8z.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8z.setRotationPoint(-5F, 16F, -1F);
        Shape8z.setTextureSize(128, 128);
        Shape8z.mirror = true;
        this.setRotation(Shape8z, 1.963495F, 0F, 0F);
        Shape8aa = new LODModelPart(this, 0, 0);
        Shape8aa.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8aa.setRotationPoint(-5F, 16F, -1F);
        Shape8aa.setTextureSize(128, 128);
        Shape8aa.mirror = true;
        this.setRotation(Shape8aa, 1.178097F, 0F, 0F);
        Shape8ab = new LODModelPart(this, 0, 0);
        Shape8ab.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8ab.setRotationPoint(-5F, 16F, -1F);
        Shape8ab.setTextureSize(128, 128);
        Shape8ab.mirror = true;
        this.setRotation(Shape8ab, 0.7853982F, 0F, 0F);
        Shape8ac = new LODModelPart(this, 0, 0);
        Shape8ac.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8ac.setRotationPoint(-5F, 16F, -1F);
        Shape8ac.setTextureSize(128, 128);
        Shape8ac.mirror = true;
        this.setRotation(Shape8ac, 0.3926991F, 0F, 0F);
        Shape8ad = new LODModelPart(this, 0, 0);
        Shape8ad.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8ad.setRotationPoint(-5F, 16F, -1F);
        Shape8ad.setTextureSize(128, 128);
        Shape8ad.mirror = true;
        this.setRotation(Shape8ad, 2.356194F, 0F, 0F);
        Shape8ae = new LODModelPart(this, 0, 0);
        Shape8ae.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8ae.setRotationPoint(-5F, 16F, -1F);
        Shape8ae.setTextureSize(128, 128);
        Shape8ae.mirror = true;
        this.setRotation(Shape8ae, 2.748893F, 0F, 0F);
        Shape8af = new LODModelPart(this, 0, 0);
        Shape8af.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8af.setRotationPoint(1F, 16F, -1F);
        Shape8af.setTextureSize(128, 128);
        Shape8af.mirror = true;
        this.setRotation(Shape8af, 1.570796F, 0F, 0F);
        Shape8ag = new LODModelPart(this, 0, 0);
        Shape8ag.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8ag.setRotationPoint(4F, 16F, -1F);
        Shape8ag.setTextureSize(128, 128);
        Shape8ag.mirror = true;
        this.setRotation(Shape8ag, 1.963495F, 0F, 0F);
        Shape8ah = new LODModelPart(this, 0, 0);
        Shape8ah.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8ah.setRotationPoint(4F, 16F, -1F);
        Shape8ah.setTextureSize(128, 128);
        Shape8ah.mirror = true;
        this.setRotation(Shape8ah, 0.7853982F, 0F, 0F);
        Shape8ai = new LODModelPart(this, 0, 0);
        Shape8ai.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8ai.setRotationPoint(4F, 16F, -1F);
        Shape8ai.setTextureSize(128, 128);
        Shape8ai.mirror = true;
        this.setRotation(Shape8ai, 1.178097F, 0F, 0F);
        Shape8aj = new LODModelPart(this, 0, 0);
        Shape8aj.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8aj.setRotationPoint(4F, 16F, -1F);
        Shape8aj.setTextureSize(128, 128);
        Shape8aj.mirror = true;
        this.setRotation(Shape8aj, 0.3926991F, 0F, 0F);
        Shape8ak = new LODModelPart(this, 0, 0);
        Shape8ak.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8ak.setRotationPoint(4F, 16F, -1F);
        Shape8ak.setTextureSize(128, 128);
        Shape8ak.mirror = true;
        this.setRotation(Shape8ak, 2.356194F, 0F, 0F);
        Shape8al = new LODModelPart(this, 0, 0);
        Shape8al.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8al.setRotationPoint(4F, 16F, -1F);
        Shape8al.setTextureSize(128, 128);
        Shape8al.mirror = true;
        this.setRotation(Shape8al, 2.748893F, 0F, 0F);
        Shape8am = new LODModelPart(this, 0, 0);
        Shape8am.addBox(0F, -1F, -6F, 1, 2, 12);
        Shape8am.setRotationPoint(4F, 16F, -1F);
        Shape8am.setTextureSize(128, 128);
        Shape8am.mirror = true;
        this.setRotation(Shape8am, 1.570796F, 0F, 0F);
        Shape8an = new LODModelPart(this, 0, 0);
        Shape8an.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8an.setRotationPoint(-5F, 16F, -1F);
        Shape8an.setTextureSize(128, 128);
        Shape8an.mirror = true;
        this.setRotation(Shape8an, 0F, 0F, 0F);
        Shape8ao = new LODModelPart(this, 0, 0);
        Shape8ao.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8ao.setRotationPoint(-2F, 16F, -1F);
        Shape8ao.setTextureSize(128, 128);
        Shape8ao.mirror = true;
        this.setRotation(Shape8ao, 0F, 0F, 0F);
        Shape8ap = new LODModelPart(this, 0, 0);
        Shape8ap.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8ap.setRotationPoint(1F, 16F, -1F);
        Shape8ap.setTextureSize(128, 128);
        Shape8ap.mirror = true;
        this.setRotation(Shape8ap, 1.570796F, 0F, 0F);
        Shape8aq = new LODModelPart(this, 0, 0);
        Shape8aq.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8aq.setRotationPoint(-5F, 16F, -1F);
        Shape8aq.setTextureSize(128, 128);
        Shape8aq.mirror = true;
        this.setRotation(Shape8aq, 1.963495F, 0F, 0F);
        Shape8ar = new LODModelPart(this, 0, 0);
        Shape8ar.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8ar.setRotationPoint(-5F, 16F, -1F);
        Shape8ar.setTextureSize(128, 128);
        Shape8ar.mirror = true;
        this.setRotation(Shape8ar, 2.356194F, 0F, 0F);
        Shape8as = new LODModelPart(this, 0, 0);
        Shape8as.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8as.setRotationPoint(-5F, 16F, -1F);
        Shape8as.setTextureSize(128, 128);
        Shape8as.mirror = true;
        this.setRotation(Shape8as, 2.748893F, 0F, 0F);
        Shape8at = new LODModelPart(this, 0, 0);
        Shape8at.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8at.setRotationPoint(-5F, 16F, -1F);
        Shape8at.setTextureSize(128, 128);
        Shape8at.mirror = true;
        this.setRotation(Shape8at, 0.7853982F, 0F, 0F);
        Shape8au = new LODModelPart(this, 0, 0);
        Shape8au.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8au.setRotationPoint(-5F, 16F, -1F);
        Shape8au.setTextureSize(128, 128);
        Shape8au.mirror = true;
        this.setRotation(Shape8au, 0.3926991F, 0F, 0F);
        Shape8av = new LODModelPart(this, 0, 0);
        Shape8av.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8av.setRotationPoint(-5F, 16F, -1F);
        Shape8av.setTextureSize(128, 128);
        Shape8av.mirror = true;
        this.setRotation(Shape8av, 1.178097F, 0F, 0F);
        Shape8aw = new LODModelPart(this, 0, 0);
        Shape8aw.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8aw.setRotationPoint(-5F, 16F, -1F);
        Shape8aw.setTextureSize(128, 128);
        Shape8aw.mirror = true;
        this.setRotation(Shape8aw, 1.570796F, 0F, 0F);
        Shape8ax = new LODModelPart(this, 0, 0);
        Shape8ax.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8ax.setRotationPoint(-2F, 16F, -1F);
        Shape8ax.setTextureSize(128, 128);
        Shape8ax.mirror = true;
        this.setRotation(Shape8ax, 2.356194F, 0F, 0F);
        Shape8ay = new LODModelPart(this, 0, 0);
        Shape8ay.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8ay.setRotationPoint(-2F, 16F, -1F);
        Shape8ay.setTextureSize(128, 128);
        Shape8ay.mirror = true;
        this.setRotation(Shape8ay, 0.7853982F, 0F, 0F);
        Shape8az = new LODModelPart(this, 0, 0);
        Shape8az.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8az.setRotationPoint(-2F, 16F, -1F);
        Shape8az.setTextureSize(128, 128);
        Shape8az.mirror = true;
        this.setRotation(Shape8az, 1.178097F, 0F, 0F);
        Shape8ba = new LODModelPart(this, 0, 0);
        Shape8ba.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8ba.setRotationPoint(-2F, 16F, -1F);
        Shape8ba.setTextureSize(128, 128);
        Shape8ba.mirror = true;
        this.setRotation(Shape8ba, 0.3926991F, 0F, 0F);
        Shape8bb = new LODModelPart(this, 0, 0);
        Shape8bb.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8bb.setRotationPoint(-2F, 16F, -1F);
        Shape8bb.setTextureSize(128, 128);
        Shape8bb.mirror = true;
        this.setRotation(Shape8bb, 1.963495F, 0F, 0F);
        Shape8bc = new LODModelPart(this, 0, 0);
        Shape8bc.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8bc.setRotationPoint(-2F, 16F, -1F);
        Shape8bc.setTextureSize(128, 128);
        Shape8bc.mirror = true;
        this.setRotation(Shape8bc, 2.748893F, 0F, 0F);
        Shape8bd = new LODModelPart(this, 0, 0);
        Shape8bd.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8bd.setRotationPoint(-2F, 16F, -1F);
        Shape8bd.setTextureSize(128, 128);
        Shape8bd.mirror = true;
        this.setRotation(Shape8bd, 1.570796F, 0F, 0F);
        Shape8be = new LODModelPart(this, 0, 0);
        Shape8be.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8be.setRotationPoint(4F, 16F, -1F);
        Shape8be.setTextureSize(128, 128);
        Shape8be.mirror = true;
        this.setRotation(Shape8be, 1.178097F, 0F, 0F);
        Shape8bf = new LODModelPart(this, 0, 0);
        Shape8bf.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8bf.setRotationPoint(4F, 16F, -1F);
        Shape8bf.setTextureSize(128, 128);
        Shape8bf.mirror = true;
        this.setRotation(Shape8bf, 0.3926991F, 0F, 0F);
        Shape8bg = new LODModelPart(this, 0, 0);
        Shape8bg.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8bg.setRotationPoint(4F, 16F, -1F);
        Shape8bg.setTextureSize(128, 128);
        Shape8bg.mirror = true;
        this.setRotation(Shape8bg, 0.7853982F, 0F, 0F);
        Shape8bh = new LODModelPart(this, 0, 0);
        Shape8bh.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8bh.setRotationPoint(4F, 16F, -1F);
        Shape8bh.setTextureSize(128, 128);
        Shape8bh.mirror = true;
        this.setRotation(Shape8bh, 1.963495F, 0F, 0F);
        Shape8bi = new LODModelPart(this, 0, 0);
        Shape8bi.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8bi.setRotationPoint(4F, 16F, -1F);
        Shape8bi.setTextureSize(128, 128);
        Shape8bi.mirror = true;
        this.setRotation(Shape8bi, 1.570796F, 0F, 0F);
        Shape8bj = new LODModelPart(this, 0, 0);
        Shape8bj.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8bj.setRotationPoint(4F, 16F, -1F);
        Shape8bj.setTextureSize(128, 128);
        Shape8bj.mirror = true;
        this.setRotation(Shape8bj, 2.356194F, 0F, 0F);
        Shape8bk = new LODModelPart(this, 0, 0);
        Shape8bk.addBox(0F, -0.5F, -7F, 1, 1, 14);
        Shape8bk.setRotationPoint(4F, 16F, -1F);
        Shape8bk.setTextureSize(128, 128);
        Shape8bk.mirror = true;
        this.setRotation(Shape8bk, 2.748893F, 0F, 0F);
        Shape9u = new LODModelPart(this, 0, 46);
        Shape9u.addBox(0F, -1F, -1F, 15, 2, 2);
        Shape9u.setRotationPoint(-7.5F, 16F, -1F);
        Shape9u.setTextureSize(128, 128);
        Shape9u.mirror = true;
        this.setRotation(Shape9u, 0.8028515F, 0F, 0F);
        Shape9v = new LODModelPart(this, 32, 32);
        Shape9v.addBox(0F, -3F, -3F, 1, 6, 6);
        Shape9v.setRotationPoint(-7F, 16F, -1F);
        Shape9v.setTextureSize(128, 128);
        Shape9v.mirror = true;
        this.setRotation(Shape9v, 0.7853982F, 0F, 0F);
        Shape9w = new LODModelPart(this, 32, 32);
        Shape9w.addBox(0F, -3F, -3F, 1, 6, 6);
        Shape9w.setRotationPoint(-7F, 16F, -1F);
        Shape9w.setTextureSize(128, 128);
        Shape9w.mirror = true;
        this.setRotation(Shape9w, 1.178097F, 0F, 0F);
        Shape9y = new LODModelPart(this, 32, 32);
        Shape9y.addBox(0F, -3F, -3F, 1, 6, 6);
        Shape9y.setRotationPoint(-7F, 16F, -1F);
        Shape9y.setTextureSize(128, 128);
        Shape9y.mirror = true;
        this.setRotation(Shape9y, 0F, 0F, 0F);
        Shape9x = new LODModelPart(this, 119, 0);
        Shape9x.addBox(0F, -1.5F, -1.5F, 1, 3, 3);
        Shape9x.setRotationPoint(-7F, 16F, 5F);
        Shape9x.setTextureSize(128, 128);
        Shape9x.mirror = true;
        this.setRotation(Shape9x, 0.7853982F, 0F, 0F);
        Shape9z = new LODModelPart(this, 119, 0);
        Shape9z.addBox(0F, -1.5F, -1.5F, 1, 3, 3);
        Shape9z.setRotationPoint(-7F, 16F, 5F);
        Shape9z.setTextureSize(128, 128);
        Shape9z.mirror = true;
        this.setRotation(Shape9z, 0F, 0F, 0F);
        Shape10 = new LODModelPart(this, 0, 23);
        Shape10.addBox(-2F, -2F, 0F, 4, 4, 1);
        Shape10.setRotationPoint(0F, 16F, 6F);
        Shape10.setTextureSize(128, 128);
        Shape10.mirror = true;
        this.setRotation(Shape10, 0F, 0F, 0.7853982F);
        Shape10a = new LODModelPart(this, 0, 17);
        Shape10a.addBox(-1.5F, -1.5F, 0F, 3, 3, 1);
        Shape10a.setRotationPoint(-4.5F, 16F, 6F);
        Shape10a.setTextureSize(128, 128);
        Shape10a.mirror = true;
        this.setRotation(Shape10a, 0F, 0F, 1.178097F);
        Shape10b = new LODModelPart(this, 0, 23);
        Shape10b.addBox(-2F, -2F, 0F, 4, 4, 1);
        Shape10b.setRotationPoint(0F, 16F, 6F);
        Shape10b.setTextureSize(128, 128);
        Shape10b.mirror = true;
        this.setRotation(Shape10b, 0F, 0F, 0F);
        Shape10c = new LODModelPart(this, 0, 17);
        Shape10c.addBox(-1.5F, -1.5F, 0F, 3, 3, 1);
        Shape10c.setRotationPoint(-4.5F, 16F, 6F);
        Shape10c.setTextureSize(128, 128);
        Shape10c.mirror = true;
        this.setRotation(Shape10c, 0F, 0F, 0.3926991F);
        Shape11 = new LODModelPart(this, 0, 51);
        Shape11.addBox(-1F, -1F, 0F, 2, 2, 2);
        Shape11.setRotationPoint(0F, 16F, 6.5F);
        Shape11.setTextureSize(128, 128);
        Shape11.mirror = true;
        this.setRotation(Shape11, 0F, 0F, 0.7853982F);
        Shape11a = new LODModelPart(this, 0, 51);
        Shape11a.addBox(-1F, -1F, 0F, 2, 2, 2);
        Shape11a.setRotationPoint(0F, 16F, 6.5F);
        Shape11a.setTextureSize(128, 128);
        Shape11a.mirror = true;
        this.setRotation(Shape11a, 0F, 0F, 0F);
    }

    @Override
    public void renderAll(TileEntity tile, ArrayList li, float phi, float theta) {
        Shape1.render(tile, f5);
        Shape2.render(tile, f5);
        Shape3.render(tile, f5);
        Shape4.render(tile, f5);
        Shape5a.render(tile, f5);
        Shape5b.render(tile, f5);
        Shape5d.render(tile, f5);
        Shape5e.render(tile, f5);
        Shape5f.render(tile, f5);
        Shape5.render(tile, f5);
        Shape5c.render(tile, f5);
        Shape5g.render(tile, f5);
        Shape6.render(tile, f5);
        Shape7.render(tile, f5);
        Shape5fa.render(tile, f5);
        Shape5aa.render(tile, f5);
        Shape5ca.render(tile, f5);
        Shape51.render(tile, f5);
        Shape5ga.render(tile, f5);
        Shape5ba.render(tile, f5);
        Shape5ea.render(tile, f5);
        Shape5da.render(tile, f5);
        Shape5fab.render(tile, f5);
        Shape5fa1.render(tile, f5);

        double d = 1;
        double d1 = -0.0625;
        GL11.glTranslated(0, d, d1);
        GL11.glRotatef(-phi, 1, 0, 0);
        GL11.glTranslated(0, -d, -d1);
        Shape8.render(tile, f5);
        Shape8a.render(tile, f5);
        Shape8b.render(tile, f5);
        Shape8c.render(tile, f5);
        Shape8d.render(tile, f5);
        Shape8e.render(tile, f5);
        Shape8f.render(tile, f5);
        Shape8g.render(tile, f5);
        Shape8h.render(tile, f5);
        Shape8j.render(tile, f5);
        Shape8i.render(tile, f5);
        Shape8k.render(tile, f5);
        Shape8l.render(tile, f5);
        Shape8m.render(tile, f5);
        Shape8n.render(tile, f5);
        Shape8o.render(tile, f5);
        Shape8p.render(tile, f5);
        Shape8q.render(tile, f5);
        Shape8r.render(tile, f5);
        Shape8s.render(tile, f5);
        Shape8t.render(tile, f5);
        Shape8u.render(tile, f5);
        Shape8v.render(tile, f5);
        Shape8w.render(tile, f5);
        Shape8x.render(tile, f5);
        Shape8y.render(tile, f5);
        Shape8z.render(tile, f5);
        Shape8aa.render(tile, f5);
        Shape8ab.render(tile, f5);
        Shape8ac.render(tile, f5);
        Shape8ad.render(tile, f5);
        Shape8ae.render(tile, f5);
        Shape8af.render(tile, f5);
        Shape8ag.render(tile, f5);
        Shape8ah.render(tile, f5);
        Shape8ai.render(tile, f5);
        Shape8aj.render(tile, f5);
        Shape8ak.render(tile, f5);
        Shape8al.render(tile, f5);
        Shape8am.render(tile, f5);
        Shape8an.render(tile, f5);
        Shape8ao.render(tile, f5);
        Shape8ap.render(tile, f5);
        Shape8aq.render(tile, f5);
        Shape8ar.render(tile, f5);
        Shape8as.render(tile, f5);
        Shape8at.render(tile, f5);
        Shape8au.render(tile, f5);
        Shape8av.render(tile, f5);
        Shape8aw.render(tile, f5);
        Shape8ax.render(tile, f5);
        Shape8ay.render(tile, f5);
        Shape8az.render(tile, f5);
        Shape8ba.render(tile, f5);
        Shape8bb.render(tile, f5);
        Shape8bc.render(tile, f5);
        Shape8bd.render(tile, f5);
        Shape8be.render(tile, f5);
        Shape8bf.render(tile, f5);
        Shape8bg.render(tile, f5);
        Shape8bh.render(tile, f5);
        Shape8bi.render(tile, f5);
        Shape8bj.render(tile, f5);
        Shape8bk.render(tile, f5);
        GL11.glTranslated(0, d, d1);
        GL11.glRotatef(phi, 1, 0, 0);
        GL11.glTranslated(0, -d, -d1);

        GL11.glTranslated(0, d, d1);
        GL11.glRotatef(-phi, 1, 0, 0);
        GL11.glTranslated(0, -d, -d1);
        Shape9.render(tile, f5);
        Shape9a.render(tile, f5);
        Shape9u.render(tile, f5);
        Shape9v.render(tile, f5);
        Shape9w.render(tile, f5);
        Shape9y.render(tile, f5);
        GL11.glTranslated(0, d, d1);
        GL11.glRotatef(phi, 1, 0, 0);
        GL11.glTranslated(0, -d, -d1);

        double d2 = -0.4375;
        d1 = 0.3125;
        GL11.glTranslated(d2, d, d1);
        GL11.glRotatef(phi, 1, 0, 0);
        Shape9x.setRotationPoint(0, 0, 0);
        Shape9z.setRotationPoint(0, 0, 0);
        Shape9x.render(tile, f5);
        Shape9z.render(tile, f5);
        GL11.glRotatef(-phi, 1, 0, 0);
        GL11.glTranslated(-d2, -d, -d1);

        d = 1;
        d1 = 0.375;
        d2 = -0.25;
        GL11.glTranslated(d2, d, d1);
        GL11.glRotatef(phi, 0, 0, 1);
        Shape10a.setRotationPoint(0, 0, 0);
        Shape10c.setRotationPoint(0, 0, 0);
        Shape10a.render(tile, f5);
        Shape10c.render(tile, f5);
        GL11.glRotatef(-phi, 0, 0, 1);
        GL11.glTranslated(-d2, -d, -d1);

        d = 1;
        d1 = -0.0625;
        GL11.glTranslated(0, d, d1);
        GL11.glRotatef(-phi, 0, 0, 1);
        GL11.glTranslated(0, -d, -d1);
        Shape10.render(tile, f5);
        Shape10b.render(tile, f5);
        Shape11.render(tile, f5);
        Shape11a.render(tile, f5);
        GL11.glTranslated(0, d, d1);
        GL11.glRotatef(phi, 0, 0, 1);
        GL11.glTranslated(0, -d, -d1);
    }
}
