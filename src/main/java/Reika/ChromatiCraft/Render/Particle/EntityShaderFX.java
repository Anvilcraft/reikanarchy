package Reika.ChromatiCraft.Render.Particle;

import java.util.HashMap;

import Reika.ChromatiCraft.Registry.ChromaShaders;
import Reika.DragonAPI.Instantiable.RayTracer;
import Reika.DragonAPI.Instantiable.RayTracer.RayTracerWithCache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityShaderFX extends EntityCCBlurFX {
    private static final RayTracerWithCache LOS
        = RayTracer.getVisualLOSForRenderCulling();

    private float lensingIntensity;
    private float lensingFadeRate = 0.05F;
    private float lensingClip = 1;
    private ChromaShaders shaderType;
    private boolean stillRender;

    public EntityShaderFX(
        World world, double x, double y, double z, float f, ChromaShaders s
    ) {
        super(world, x, y, z);
        lensingIntensity = f;
        shaderType = s;
    }

    public EntityShaderFX(
        World world,
        double x,
        double y,
        double z,
        double vx,
        double vy,
        double vz,
        float f,
        ChromaShaders s
    ) {
        super(world, x, y, z, vx, vy, vz);
        lensingIntensity = f;
        shaderType = s;
    }

    public EntityShaderFX setIntensity(float f) {
        lensingIntensity = f;
        return this;
    }

    public EntityShaderFX setFadeRate(float f) {
        lensingFadeRate = f;
        return this;
    }

    public EntityShaderFX setClip(float f) {
        lensingClip = f;
        return this;
    }

    public EntityShaderFX setShader(ChromaShaders s) {
        shaderType = s;
        return this;
    }

    public EntityShaderFX setRendering(boolean render) {
        stillRender = render;
        return this;
    }

    @Override
    public void renderParticle(
        Tessellator v5,
        float par2,
        float par3,
        float par4,
        float par5,
        float par6,
        float par7
    ) {
        if (stillRender)
            super.renderParticle(v5, par2, par3, par4, par5, par6, par7);
        shaderType.clearOnRender = true;

        EntityPlayer ep = Minecraft.getMinecraft().thePlayer;
        //LOS.update(this);
        LOS.setOrigins(
            posX, posY, posZ, ep.posX, ep.posY, ep.posZ
        ); //particle posX in world is valid
        boolean see = LOS.isClearLineOfSight(this);
        if (!see)
            return;

        shaderType.setIntensity(1);
        shaderType.getShader().addFocus(this);
        float f = Math.min(lensingIntensity, particleAge * lensingFadeRate);
        HashMap<String, Object> map = new HashMap();
        float f11 = (float) (prevPosX + (posX - prevPosX) * par2 - interpPosX);
        float f12 = (float) (prevPosY + (posY - prevPosY) * par2 - interpPosY);
        float f13 = (float) (prevPosZ + (posZ - prevPosZ) * par2 - interpPosZ);
        map.put("dx", f11);
        map.put("dy", f12);
        map.put("dz", f13);
        map.put("distance", ep.getDistanceSqToEntity(this));
        map.put("clip", lensingClip);
        map.put("scale", particleScale * 0.2F);
        shaderType.getShader().modifyLastCompoundFocus(f, map);
    }
}
