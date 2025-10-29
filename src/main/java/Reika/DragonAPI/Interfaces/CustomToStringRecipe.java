package Reika.DragonAPI.Interfaces;

public interface CustomToStringRecipe {
    /**
     * Only suitable for display, as it may vary unpredictably (eg item list ordering).
     * Do NOT use this for map keys.
     */
    public String toDisplayString();

    /**
     * This needs to be constant across platforms and game instances! This needs to be
     * keysafe!
     */
    public String toDeterministicString();
}
