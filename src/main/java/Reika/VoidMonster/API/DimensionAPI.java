package Reika.VoidMonster.API;

import Reika.VoidMonster.World.AmbientSoundGenerator;
import Reika.VoidMonster.World.MonsterGenerator;

public class DimensionAPI {
    public static void blacklistDimensionForSounds(int id) {
        AmbientSoundGenerator.instance.blacklistDimension(id);
    }

    public static void blacklistBiomeForSounds(int id) {
        AmbientSoundGenerator.instance.blacklistBiome(id);
    }

    public static void setDimensionRuleForSpawning(int id, boolean allow) {
        MonsterGenerator.instance.setDimensionRuleAPI(id, allow);
    }
}
