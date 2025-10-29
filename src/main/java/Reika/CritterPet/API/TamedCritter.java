package Reika.CritterPet.API;

import Reika.DragonAPI.Interfaces.Entity.TameHostile;

public interface TamedCritter extends TameHostile {
    public String getMobOwner();

    public boolean isVanillaCritter();

    public boolean isModCritter();

    public int getCritterMaxHealth();
}
