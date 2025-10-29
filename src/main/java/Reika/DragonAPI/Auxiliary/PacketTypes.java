package Reika.DragonAPI.Auxiliary;

public enum PacketTypes {
    DATA(),
    SOUND(),
    STRING(),
    UPDATE(),
    FLOAT(),
    SYNC(),
    TANK(),
    RAW(),
    NBT(),
    STRINGINT(),
    STRINGINTLOC(),
    UUID(),
    PREFIXED(),
    POS(),
    FULLSOUND();

    private PacketTypes() {}

    public static PacketTypes getPacketType(int type) {
        return PacketTypes.values()[type];
    }

    public boolean hasCoordinates() {
        return this != RAW && this != NBT && this != STRINGINT && this != PREFIXED
            && this != POS && this != FULLSOUND;
    }
}
