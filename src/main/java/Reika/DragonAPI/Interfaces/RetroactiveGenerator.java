package Reika.DragonAPI.Interfaces;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.world.World;

public interface RetroactiveGenerator extends IWorldGenerator {
    public boolean canGenerateAt(World world, int chunkX, int chunkZ);

    /**
     * It would be a good idea to prefix this with your mod's name; eg
     * ReactorCraft_PitchblendeGen
     */
    public String getIDString();
}
