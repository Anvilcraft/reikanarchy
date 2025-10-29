package Reika.DragonAPI.Interfaces;

import Reika.DragonAPI.Auxiliary.Trackers.PlayerSpecificRenderer.PlayerRotationData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;

@SideOnly(Side.CLIENT)
public interface PlayerRenderObj {
    /** Render starts centered on eye position */
    public void render(EntityPlayer ep, float ptick, PlayerRotationData dat);

    /** Lower numbers render first. Use high numbers (>> 0) for transparency */
    public int getRenderPriority();
}
