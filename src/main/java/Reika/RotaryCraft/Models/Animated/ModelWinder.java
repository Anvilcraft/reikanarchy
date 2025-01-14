/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
// Date: 01/04/2013 12:17:15 AM
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

public class ModelWinder extends RotaryModelBase {
    //fields
    LODModelPart Shape1;
    LODModelPart Shape2;
    LODModelPart Shape2a;
    LODModelPart Shape3;
    LODModelPart Shape3a;
    LODModelPart Shape4;
    LODModelPart Shape5;
    LODModelPart Shape5a;
    LODModelPart Shape6;
    LODModelPart Shape6a;
    LODModelPart Shape6b;
    LODModelPart Shape6c;
    LODModelPart Shape6d;
    LODModelPart Shape6e;
    LODModelPart Shape6f;
    LODModelPart Shape6g;
    LODModelPart Shape6h;
    LODModelPart Shape6i;
    LODModelPart Shape6j;
    LODModelPart Shape6k;

    public ModelWinder() {
        textureWidth = 128;
        textureHeight = 128;

        Shape1 = new LODModelPart(this, 48, 0);
        Shape1.addBox(0F, 0F, 0F, 16, 1, 16);
        Shape1.setRotationPoint(-8F, 23F, -8F);
        Shape1.setTextureSize(64, 32);
        Shape1.mirror = true;
        this.setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new LODModelPart(this, 0, 37);
        Shape2.addBox(0F, 0F, 0F, 12, 1, 7);
        Shape2.setRotationPoint(-6F, 11F, 0F);
        Shape2.setTextureSize(64, 32);
        Shape2.mirror = true;
        this.setRotation(Shape2, 0F, 0F, 0F);
        Shape2a = new LODModelPart(this, 48, 18);
        Shape2a.addBox(0F, 0F, 0F, 12, 1, 12);
        Shape2a.setRotationPoint(-6F, 22F, -5F);
        Shape2a.setTextureSize(64, 32);
        Shape2a.mirror = true;
        this.setRotation(Shape2a, 0F, 0F, 0F);
        Shape3 = new LODModelPart(this, 0, 20);
        Shape3.addBox(0F, 0F, 0F, 1, 10, 7);
        Shape3.setRotationPoint(5F, 12F, 0F);
        Shape3.setTextureSize(64, 32);
        Shape3.mirror = true;
        this.setRotation(Shape3, 0F, 0F, 0F);
        Shape3a = new LODModelPart(this, 0, 20);
        Shape3a.addBox(0F, 0F, 0F, 1, 10, 7);
        Shape3a.setRotationPoint(-6F, 12F, 0F);
        Shape3a.setTextureSize(64, 32);
        Shape3a.mirror = true;
        this.setRotation(Shape3a, 0F, 0F, 0F);
        Shape4 = new LODModelPart(this, 0, 46);
        Shape4.addBox(0F, 0F, 0F, 10, 10, 5);
        Shape4.setRotationPoint(-5F, 12F, 2.5F);
        Shape4.setTextureSize(64, 32);
        Shape4.mirror = true;
        this.setRotation(Shape4, 0F, 0F, 0F);
        Shape5 = new LODModelPart(this, 0, 64);
        Shape5.addBox(-1F, -1F, 0F, 2, 2, 8);
        Shape5.setRotationPoint(0F, 17F, 0.5F);
        Shape5.setTextureSize(64, 32);
        Shape5.mirror = true;
        this.setRotation(Shape5, 0F, 0F, 0.7853982F);
        Shape5a = new LODModelPart(this, 0, 64);
        Shape5a.addBox(-1F, -1F, 0F, 2, 2, 8);
        Shape5a.setRotationPoint(0F, 17F, 0.5F);
        Shape5a.setTextureSize(64, 32);
        Shape5a.mirror = true;
        this.setRotation(Shape5a, 0F, 0F, 0F);
        Shape6 = new LODModelPart(this, 0, 0);
        Shape6.addBox(0F, -2F, 0F, 1, 2, 1);
        Shape6.setRotationPoint(-2.9F, 18.2F, 1F);
        Shape6.setTextureSize(64, 32);
        Shape6.mirror = true;
        this.setRotation(Shape6, 0F, 0F, 2.303835F);
        Shape6a = new LODModelPart(this, 0, 0);
        Shape6a.addBox(0F, -3F, 0F, 1, 3, 1);
        Shape6a.setRotationPoint(1.2F, 20F, 1F);
        Shape6a.setTextureSize(64, 32);
        Shape6a.mirror = true;
        this.setRotation(Shape6a, 0F, 0F, 0.8726646F);
        Shape6b = new LODModelPart(this, 0, 0);
        Shape6b.addBox(0F, -3F, 0F, 1, 3, 1);
        Shape6b.setRotationPoint(2.5F, 16.5F, 1F);
        Shape6b.setTextureSize(64, 32);
        Shape6b.mirror = true;
        this.setRotation(Shape6b, 0F, 0F, -0.6806784F);
        Shape6c = new LODModelPart(this, 0, 0);
        Shape6c.addBox(0F, -4F, 0F, 1, 4, 1);
        Shape6c.setRotationPoint(-2.2F, 14.4F, 1F);
        Shape6c.setTextureSize(64, 32);
        Shape6c.mirror = true;
        this.setRotation(Shape6c, 0F, 0F, -3.01942F);
        Shape6d = new LODModelPart(this, 0, 0);
        Shape6d.addBox(0F, -1F, 0F, 1, 1, 1);
        Shape6d.setRotationPoint(1F, 17F, 1F);
        Shape6d.setTextureSize(64, 32);
        Shape6d.mirror = true;
        this.setRotation(Shape6d, 0F, 0F, 0F);
        Shape6e = new LODModelPart(this, 0, 0);
        Shape6e.addBox(0F, -3F, 0F, 1, 3, 1);
        Shape6e.setRotationPoint(0.7F, 14.2F, 1F);
        Shape6e.setTextureSize(64, 32);
        Shape6e.mirror = true;
        this.setRotation(Shape6e, 0F, 0F, -1.692969F);
        Shape6f = new LODModelPart(this, 0, 0);
        Shape6f.addBox(0F, -2F, 0F, 1, 2, 1);
        Shape6f.setRotationPoint(1F, 16F, 1F);
        Shape6f.setTextureSize(64, 32);
        Shape6f.mirror = true;
        this.setRotation(Shape6f, 0F, 0F, -1.22173F);
        Shape6g = new LODModelPart(this, 0, 0);
        Shape6g.addBox(0F, -3F, 0F, 1, 3, 1);
        Shape6g.setRotationPoint(-0.8F, 15.2F, 1F);
        Shape6g.setTextureSize(64, 32);
        Shape6g.mirror = true;
        this.setRotation(Shape6g, 0F, 0F, -2.86234F);
        Shape6h = new LODModelPart(this, 0, 0);
        Shape6h.addBox(0F, -3F, 0F, 1, 3, 1);
        Shape6h.setRotationPoint(-1.6F, 19.4F, 1F);
        Shape6h.setTextureSize(64, 32);
        Shape6h.mirror = true;
        this.setRotation(Shape6h, 0F, 0F, 1.745329F);
        Shape6i = new LODModelPart(this, 0, 0);
        Shape6i.addBox(0F, -3F, 0F, 1, 3, 1);
        Shape6i.setRotationPoint(-1.7F, 18F, 1F);
        Shape6i.setTextureSize(64, 32);
        Shape6i.mirror = true;
        this.setRotation(Shape6i, 0F, 0F, 1.867502F);
        Shape6j = new LODModelPart(this, 0, 0);
        Shape6j.addBox(0F, -3F, 0F, 1, 3, 1);
        Shape6j.setRotationPoint(3.4F, 18.2F, 1F);
        Shape6j.setTextureSize(64, 32);
        Shape6j.mirror = true;
        this.setRotation(Shape6j, 0F, 0F, -0.0698132F);
        Shape6k = new LODModelPart(this, 0, 0);
        Shape6k.addBox(0F, -3F, 0F, 1, 3, 1);
        Shape6k.setRotationPoint(1F, 19F, 1F);
        Shape6k.setTextureSize(64, 32);
        Shape6k.mirror = true;
        this.setRotation(Shape6k, 0F, 0F, 0.5061455F);
    }

    @Override
    public void renderAll(TileEntity te, ArrayList li, float phi, float theta) {
        boolean has = (Boolean) li.get(0);
        Shape1.render(te, f5);
        Shape2.render(te, f5);
        Shape2a.render(te, f5);
        Shape3.render(te, f5);
        Shape3a.render(te, f5);
        Shape4.render(te, f5);
        GL11.glTranslated(0, 1.0625, 0);
        GL11.glRotatef(phi, 0, 0, 1);
        GL11.glTranslated(0, -1.0625, 0);
        Shape5.render(te, f5);
        Shape5a.render(te, f5);
        GL11.glTranslated(0, 1.0625, 0);
        GL11.glRotatef(-phi, 0, 0, 1);
        GL11.glTranslated(0, -1.0625, 0);
        if (!has)
            return;
        GL11.glScaled(-1, 1, 1);
        GL11.glFrontFace(GL11.GL_CW);
        Shape6.render(te, f5);
        Shape6a.render(te, f5);
        Shape6b.render(te, f5);
        Shape6c.render(te, f5);
        Shape6d.render(te, f5);
        Shape6e.render(te, f5);
        Shape6f.render(te, f5);
        Shape6g.render(te, f5);
        Shape6h.render(te, f5);
        Shape6i.render(te, f5);
        Shape6j.render(te, f5);
        Shape6k.render(te, f5);
        GL11.glScaled(-1, 1, 1);
        GL11.glFrontFace(GL11.GL_CCW);
    }
}
