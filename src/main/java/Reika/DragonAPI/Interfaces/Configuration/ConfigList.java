package Reika.DragonAPI.Interfaces.Configuration;

/** This is an interface for ENUMS! */
public interface ConfigList {
    public Class getPropertyType();

    public String getLabel();

    //public boolean isDummiedOut();

    public boolean isEnforcingDefaults();

    public boolean shouldLoad();

    /** To avoid casting */
    public int ordinal();
}
