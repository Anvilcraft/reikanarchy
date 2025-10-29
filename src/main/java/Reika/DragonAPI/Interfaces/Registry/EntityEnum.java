package Reika.DragonAPI.Interfaces.Registry;

public interface EntityEnum extends RegistryEntry {
    public int getTrackingDistance();

    public boolean sendsVelocityUpdates();

    /** This only works if you have a global ID; spawn eggs require that */
    public boolean hasSpawnEgg();

    public int eggColor1();
    public int eggColor2();

    public boolean hasGlobalID();
}
