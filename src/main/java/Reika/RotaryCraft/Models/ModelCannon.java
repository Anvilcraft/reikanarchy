/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
// Date: 30/03/2013 7:51:52 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package Reika.RotaryCraft.Models;

import java.util.ArrayList;

import Reika.DragonAPI.Instantiable.Rendering.LODModelPart;
import Reika.RotaryCraft.Base.RotaryModelBase;
import net.minecraft.tileentity.TileEntity;

public class ModelCannon extends RotaryModelBase {
    //fields
    LODModelPart Shape1;
    LODModelPart Shape2;
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
    LODModelPart Shape3;
    LODModelPart Shape3a;
    LODModelPart Shape3b;
    LODModelPart Shape3c;
    LODModelPart Shape3d;
    LODModelPart Shape3e;
    LODModelPart Shape3f;
    LODModelPart Shape3g;
    LODModelPart Shape3h;
    LODModelPart Shape3i;
    LODModelPart Shape3j;
    LODModelPart Shape3k;
    LODModelPart Shape3l;
    LODModelPart Shape3m;

    public ModelCannon() {
        textureWidth = 128;
        textureHeight = 128;

        Shape1 = new LODModelPart(this, 0, 0);
        Shape1.addBox(0F, 0F, 0F, 16, 8, 16);
        Shape1.setRotationPoint(-8F, 16F, -8F);
        Shape1.setTextureSize(128, 128);
        Shape1.mirror = true;
        this.setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new LODModelPart(this, 34, 24);
        Shape2.addBox(0F, 0F, 0F, 1, 1, 16);
        Shape2.setRotationPoint(-8F, 9F, -8F);
        Shape2.setTextureSize(128, 128);
        Shape2.mirror = true;
        this.setRotation(Shape2, 0F, 0F, 0F);
        Shape2a = new LODModelPart(this, 0, 24);
        Shape2a.addBox(0F, 0F, 0F, 1, 1, 16);
        Shape2a.setRotationPoint(7F, 9F, -8F);
        Shape2a.setTextureSize(128, 128);
        Shape2a.mirror = true;
        this.setRotation(Shape2a, 0F, 0F, 0F);
        Shape2b = new LODModelPart(this, 84, 34);
        Shape2b.addBox(0F, 0F, 0F, 6, 1, 16);
        Shape2b.setRotationPoint(-8F, 14F, -8F);
        Shape2b.setTextureSize(128, 128);
        Shape2b.mirror = true;
        this.setRotation(Shape2b, 0F, 0F, 0F);
        Shape2c = new LODModelPart(this, 86, 51);
        Shape2c.addBox(0F, 0F, 0F, 5, 1, 16);
        Shape2c.setRotationPoint(-8F, 13F, -8F);
        Shape2c.setTextureSize(128, 128);
        Shape2c.mirror = true;
        this.setRotation(Shape2c, 0F, 0F, 0F);
        Shape2d = new LODModelPart(this, 88, 68);
        Shape2d.addBox(0F, 0F, 0F, 4, 1, 16);
        Shape2d.setRotationPoint(-8F, 12F, -8F);
        Shape2d.setTextureSize(128, 128);
        Shape2d.mirror = true;
        this.setRotation(Shape2d, 0F, 0F, 0F);
        Shape2e = new LODModelPart(this, 90, 85);
        Shape2e.addBox(0F, 0F, 0F, 3, 1, 16);
        Shape2e.setRotationPoint(-8F, 11F, -8F);
        Shape2e.setTextureSize(128, 128);
        Shape2e.mirror = true;
        this.setRotation(Shape2e, 0F, 0F, 0F);
        Shape2f = new LODModelPart(this, 92, 102);
        Shape2f.addBox(0F, 0F, 0F, 2, 1, 16);
        Shape2f.setRotationPoint(-8F, 10F, -8F);
        Shape2f.setTextureSize(128, 128);
        Shape2f.mirror = true;
        this.setRotation(Shape2f, 0F, 0F, 0F);
        Shape2g = new LODModelPart(this, 82, 17);
        Shape2g.addBox(0F, 0F, 0F, 7, 1, 16);
        Shape2g.setRotationPoint(-8F, 15F, -8F);
        Shape2g.setTextureSize(128, 128);
        Shape2g.mirror = true;
        this.setRotation(Shape2g, 0F, 0F, 0F);
        Shape2h = new LODModelPart(this, 64, 0);
        Shape2h.addBox(0F, 0F, 0F, 7, 1, 16);
        Shape2h.setRotationPoint(1F, 15F, -8F);
        Shape2h.setTextureSize(128, 128);
        Shape2h.mirror = true;
        this.setRotation(Shape2h, 0F, 0F, 0F);
        Shape2i = new LODModelPart(this, 0, 109);
        Shape2i.addBox(0F, 0F, 0F, 6, 1, 16);
        Shape2i.setRotationPoint(2F, 14F, -8F);
        Shape2i.setTextureSize(128, 128);
        Shape2i.mirror = true;
        this.setRotation(Shape2i, 0F, 0F, 0F);
        Shape2j = new LODModelPart(this, 0, 92);
        Shape2j.addBox(0F, 0F, 0F, 5, 1, 16);
        Shape2j.setRotationPoint(3F, 13F, -8F);
        Shape2j.setTextureSize(128, 128);
        Shape2j.mirror = true;
        this.setRotation(Shape2j, 0F, 0F, 0F);
        Shape2k = new LODModelPart(this, 0, 75);
        Shape2k.addBox(0F, 0F, 0F, 4, 1, 16);
        Shape2k.setRotationPoint(4F, 12F, -8F);
        Shape2k.setTextureSize(128, 128);
        Shape2k.mirror = true;
        this.setRotation(Shape2k, 0F, 0F, 0F);
        Shape2l = new LODModelPart(this, 0, 58);
        Shape2l.addBox(0F, 0F, 0F, 3, 1, 16);
        Shape2l.setRotationPoint(5F, 11F, -8F);
        Shape2l.setTextureSize(128, 128);
        Shape2l.mirror = true;
        this.setRotation(Shape2l, 0F, 0F, 0F);
        Shape2m = new LODModelPart(this, 0, 41);
        Shape2m.addBox(0F, 0F, 0F, 2, 1, 16);
        Shape2m.setRotationPoint(6F, 10F, -8F);
        Shape2m.setTextureSize(128, 128);
        Shape2m.mirror = true;
        this.setRotation(Shape2m, 0F, 0F, 0F);
        Shape3 = new LODModelPart(this, 36, 41);
        Shape3.addBox(0F, 0F, 0F, 14, 1, 1);
        Shape3.setRotationPoint(-7F, 9F, 7F);
        Shape3.setTextureSize(128, 128);
        Shape3.mirror = true;
        this.setRotation(Shape3, 0F, 0F, 0F);
        Shape3a = new LODModelPart(this, 39, 68);
        Shape3a.addBox(0F, 0F, 0F, 2, 1, 7);
        Shape3a.setRotationPoint(-1F, 15F, 1F);
        Shape3a.setTextureSize(128, 128);
        Shape3a.mirror = true;
        this.setRotation(Shape3a, 0F, 0F, 0F);
        Shape3b = new LODModelPart(this, 38, 61);
        Shape3b.addBox(0F, 0F, 0F, 4, 1, 6);
        Shape3b.setRotationPoint(-2F, 14F, 2F);
        Shape3b.setTextureSize(128, 128);
        Shape3b.mirror = true;
        this.setRotation(Shape3b, 0F, 0F, 0F);
        Shape3c = new LODModelPart(this, 38, 55);
        Shape3c.addBox(0F, 0F, 0F, 6, 1, 5);
        Shape3c.setRotationPoint(-3F, 13F, 3F);
        Shape3c.setTextureSize(128, 128);
        Shape3c.mirror = true;
        this.setRotation(Shape3c, 0F, 0F, 0F);
        Shape3d = new LODModelPart(this, 36, 50);
        Shape3d.addBox(0F, 0F, 0F, 8, 1, 4);
        Shape3d.setRotationPoint(-4F, 12F, 4F);
        Shape3d.setTextureSize(128, 128);
        Shape3d.mirror = true;
        this.setRotation(Shape3d, 0F, 0F, 0F);
        Shape3e = new LODModelPart(this, 36, 46);
        Shape3e.addBox(0F, 0F, 0F, 10, 1, 3);
        Shape3e.setRotationPoint(-5F, 11F, 5F);
        Shape3e.setTextureSize(128, 128);
        Shape3e.mirror = true;
        this.setRotation(Shape3e, 0F, 0F, 0F);
        Shape3f = new LODModelPart(this, 36, 43);
        Shape3f.addBox(0F, 0F, 0F, 12, 1, 2);
        Shape3f.setRotationPoint(-6F, 10F, 6F);
        Shape3f.setTextureSize(128, 128);
        Shape3f.mirror = true;
        this.setRotation(Shape3f, 0F, 0F, 0F);
        Shape3g = new LODModelPart(this, 40, 76);
        Shape3g.addBox(0F, 0F, 0F, 2, 1, 7);
        Shape3g.setRotationPoint(-1F, 15F, -8F);
        Shape3g.setTextureSize(128, 128);
        Shape3g.mirror = true;
        this.setRotation(Shape3g, 0F, 0F, 0F);
        Shape3h = new LODModelPart(this, 40, 84);
        Shape3h.addBox(0F, 0F, 0F, 4, 1, 6);
        Shape3h.setRotationPoint(-2F, 14F, -8F);
        Shape3h.setTextureSize(128, 128);
        Shape3h.mirror = true;
        this.setRotation(Shape3h, 0F, 0F, 0F);
        Shape3i = new LODModelPart(this, 42, 91);
        Shape3i.addBox(0F, 0F, 0F, 6, 1, 5);
        Shape3i.setRotationPoint(-3F, 13F, -8F);
        Shape3i.setTextureSize(128, 128);
        Shape3i.mirror = true;
        this.setRotation(Shape3i, 0F, 0F, 0F);
        Shape3j = new LODModelPart(this, 42, 97);
        Shape3j.addBox(0F, 0F, 0F, 8, 1, 4);
        Shape3j.setRotationPoint(-4F, 12F, -8F);
        Shape3j.setTextureSize(128, 128);
        Shape3j.mirror = true;
        this.setRotation(Shape3j, 0F, 0F, 0F);
        Shape3k = new LODModelPart(this, 42, 102);
        Shape3k.addBox(0F, 0F, 0F, 10, 1, 3);
        Shape3k.setRotationPoint(-5F, 11F, -8F);
        Shape3k.setTextureSize(128, 128);
        Shape3k.mirror = true;
        this.setRotation(Shape3k, 0F, 0F, 0F);
        Shape3l = new LODModelPart(this, 44, 106);
        Shape3l.addBox(0F, 0F, 0F, 12, 1, 2);
        Shape3l.setRotationPoint(-6F, 10F, -8F);
        Shape3l.setTextureSize(128, 128);
        Shape3l.mirror = true;
        this.setRotation(Shape3l, 0F, 0F, 0F);
        Shape3m = new LODModelPart(this, 44, 109);
        Shape3m.addBox(0F, 0F, 0F, 14, 1, 1);
        Shape3m.setRotationPoint(-7F, 9F, -8F);
        Shape3m.setTextureSize(128, 128);
        Shape3m.mirror = true;
        this.setRotation(Shape3m, 0F, 0F, 0F);
    }

    @Override
    public void renderAll(TileEntity te, ArrayList li, float phi, float theta) {
        Shape1.render(te, f5);
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
        Shape3.render(te, f5);
        Shape3a.render(te, f5);
        Shape3b.render(te, f5);
        Shape3c.render(te, f5);
        Shape3d.render(te, f5);
        Shape3e.render(te, f5);
        Shape3f.render(te, f5);
        Shape3g.render(te, f5);
        Shape3h.render(te, f5);
        Shape3i.render(te, f5);
        Shape3j.render(te, f5);
        Shape3k.render(te, f5);
        Shape3l.render(te, f5);
        Shape3m.render(te, f5);
    }
}
