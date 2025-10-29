package Reika.DragonAPI.Instantiable.Event;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;

public class GenLayerBeachEvent extends Event {
    public final BiomeGenBase originalBiomeID;
    public final int plannedBeachID;
    public int beachIDToPlace;

    public GenLayerBeachEvent(BiomeGenBase orig, int plan) {
        originalBiomeID = orig;
        plannedBeachID = plan;
        beachIDToPlace = plan;
    }

    public void deleteBeach() {
        beachIDToPlace = originalBiomeID.biomeID;
    }

    public static int fire(BiomeGenBase biome, int place) {
        GenLayerBeachEvent evt = new GenLayerBeachEvent(biome, place);
        MinecraftForge.EVENT_BUS.post(evt);
        //DragonAPICore.log("Wanted to place beach
        //"+BiomeGenBase.biomeList[place].biomeName+" in "+biome.biomeName+", changed to
        //"+BiomeGenBase.biomeList[evt.beachIDToPlace].biomeName);
        return evt.beachIDToPlace;
    }
}
