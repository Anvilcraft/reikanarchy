/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Render.Particle;

import Reika.ChromatiCraft.Registry.ChromaIcons;
import Reika.DragonAPI.Libraries.Rendering.ReikaColorAPI;
import net.minecraft.world.World;

public class EntityFireSmokeFX extends EntityCCBlurFX {
    public final int startColor;

    public EntityFireSmokeFX(World world, double x, double y, double z, int color) {
        super(world, x, y, z);
        startColor = color;
        this.setBasicBlend();
        this.setIcon(ChromaIcons.CLOUDGROUP_TRANS_BLUR);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        int c = ReikaColorAPI.mixColors(
            0x000000,
            startColor,
            Math.max(0, -0.1875F + particleAge / (float) particleMaxAge)
        );
        this.setColor(c);
    }
}
