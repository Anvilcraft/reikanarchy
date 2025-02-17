/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
// Date: 12/02/2014 7:24:38 PM
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

public class ModelLawnSprinkler extends RotaryModelBase {
    //fields
    LODModelPart Shape1;
    LODModelPart Shape2;
    LODModelPart Shape2a;
    LODModelPart Shape3;
    LODModelPart Shape3a;
    LODModelPart Shape3b;
    LODModelPart Shape3c;
    LODModelPart Shape3d;
    LODModelPart Shape3e;

    public ModelLawnSprinkler() {
        textureWidth = 128;
        textureHeight = 128;

        Shape1 = new LODModelPart(this, 0, 0);
        Shape1.addBox(0F, 0F, 0F, 16, 1, 16);
        Shape1.setRotationPoint(-8F, 23F, -8F);
        Shape1.setTextureSize(128, 128);
        Shape1.mirror = true;
        this.setRotation(Shape1, 0F, 0F, 0F);
        Shape2 = new LODModelPart(this, 0, 19);
        Shape2.addBox(-2F, 0F, -2F, 4, 9, 4);
        Shape2.setRotationPoint(0F, 14F, 0F);
        Shape2.setTextureSize(128, 128);
        Shape2.mirror = true;
        this.setRotation(Shape2, 0F, 0.7853982F, 0F);
        Shape2a = new LODModelPart(this, 0, 19);
        Shape2a.addBox(-2F, 0F, -2F, 4, 9, 4);
        Shape2a.setRotationPoint(0F, 14F, 0F);
        Shape2a.setTextureSize(128, 128);
        Shape2a.mirror = true;
        this.setRotation(Shape2a, 0F, 0F, 0F);
        Shape3 = new LODModelPart(this, 0, 45);
        Shape3.addBox(-1F, 0.5F, 5.4F, 2, 2, 4);
        Shape3.setRotationPoint(0F, 19F, 0F);
        Shape3.setTextureSize(128, 128);
        Shape3.mirror = true;
        this.setRotation(Shape3, 0.7853982F, 2.094395F, 0F);
        Shape3a = new LODModelPart(this, 0, 34);
        Shape3a.addBox(-1F, -1F, -0.5F, 2, 2, 6);
        Shape3a.setRotationPoint(0F, 16F, 0F);
        Shape3a.setTextureSize(128, 128);
        Shape3a.mirror = true;
        this.setRotation(Shape3a, 0F, -2.094395F, 0F);
        Shape3b = new LODModelPart(this, 0, 34);
        Shape3b.addBox(-1F, -1F, -0.5F, 2, 2, 6);
        Shape3b.setRotationPoint(0F, 16F, 0F);
        Shape3b.setTextureSize(128, 128);
        Shape3b.mirror = true;
        this.setRotation(Shape3b, 0F, 0F, 0F);
        Shape3c = new LODModelPart(this, 0, 34);
        Shape3c.addBox(-1F, -1F, -0.5F, 2, 2, 6);
        Shape3c.setRotationPoint(0F, 16F, 0F);
        Shape3c.setTextureSize(128, 128);
        Shape3c.mirror = true;
        this.setRotation(Shape3c, 0F, 2.094395F, 0F);
        Shape3d = new LODModelPart(this, 0, 45);
        Shape3d.addBox(-1F, 0.5F, 5.4F, 2, 2, 4);
        Shape3d.setRotationPoint(0F, 19F, 0F);
        Shape3d.setTextureSize(128, 128);
        Shape3d.mirror = true;
        this.setRotation(Shape3d, 0.7853982F, 0F, 0F);
        Shape3e = new LODModelPart(this, 0, 45);
        Shape3e.addBox(-1F, 0.5F, 5.4F, 2, 2, 4);
        Shape3e.setRotationPoint(0F, 19F, 0F);
        Shape3e.setTextureSize(128, 128);
        Shape3e.mirror = true;
        this.setRotation(Shape3e, 0.7853982F, -2.094395F, 0F);
    }

    @Override
    public void renderAll(TileEntity te, ArrayList li, float phi, float theta) {
        Shape1.render(te, f5);
        GL11.glRotatef(phi, 0, 1, 0);
        Shape2.render(te, f5);
        Shape2a.render(te, f5);
        Shape3.render(te, f5);
        Shape3a.render(te, f5);
        Shape3b.render(te, f5);
        Shape3c.render(te, f5);
        Shape3d.render(te, f5);
        Shape3e.render(te, f5);
        GL11.glRotatef(-phi, 0, 1, 0);
    }

    @Override
    public void
    setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5);
    }
}
