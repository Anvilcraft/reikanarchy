/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI;

import Reika.DragonAPI.Auxiliary.Trackers.KeyWatcher.Key;
import Reika.DragonAPI.Interfaces.Configuration.BooleanConfig;
import Reika.DragonAPI.Interfaces.Configuration.IntegerConfig;
import Reika.DragonAPI.Interfaces.Configuration.StringArrayConfig;
import Reika.DragonAPI.Interfaces.Configuration.StringConfig;
import Reika.DragonAPI.Interfaces.Configuration.UserSpecificConfig;
import net.minecraft.world.ChunkCoordIntPair;

public enum DragonOptions implements IntegerConfig, BooleanConfig, StringArrayConfig,
                                     StringConfig, UserSpecificConfig {
    LOGLOADING("Console Loading Info", true),
    FILELOG("Log Loading Info To Separate File", false),
    DEBUGMODE("Debug Mode", false),
    SYNCPACKET("Sync Packet ID", 182),
    NORENDERS("Disable Renders For Debug", false),
    TABNBT("Show TileEntity NBT when using Debug Key", false),
    SOUNDCHANNELS("Increase sound channel count", true),
    UNNERFOBSIDIAN(
        "Restore Obsidian Blast Resistance", true
    ), //This is to undo an effect from IC2, presumably intended for making obsidian
       //vulnerable to its explosions
    NOHOTBARSWAP(
        "Disable Hotbar Swapping", false
    ), //Whether to disable using number keys while mousing over inventory slots to swap
       //its item into that hotbar slot. Good if you keep doing this accidentally
    CHATERRORS("Log errors to chat", true),
    SORTCREATIVE("Sort Creative Tabs Alphabetically", true),
    CUSTOMRENDER("Custom/Donator Renders", true),
    OPONLYUPDATE("Only show update notice to Ops or SSP", false),
    //PACKONLYUPDATE("Only show update notice to pack creator", false),
    GREGORES(
        "Force Gregtech Ore Compatibility", true
    ), //Whether to add GT ores to the registries of vanilla/modded ores DAPI recognizes
    LOGSYNCCME(
        "Log Sync Packet CME Avoidance", true
    ), //Whether to log an error to the console when a TE sync packet arrives during the
       //tick and would have triggered a CME. Usually only happens when the TPS drops
       //severely, and is not harmful aside from causing clientside desync
    SLOWSYNC(
        "Slow Sync Packets - Only use this as a last resort", false
    ), //Whether to reduce the rate at which sync packets are sent. This is not a good
       //thing to do unnecessarily
    LAGWARNING("Minimum Delay (ms) for 'Can't Keep Up!' Log Warning", 0),
    CHECKSANITY(
        "Check Environment Sanity", false
    ), //Whether to warn about other mods registering invalid content (eg biomes without
       //names or items that crash when rendered). See
       //https://www.reddit.com/r/feedthebeast/comments/48pzdu/headsup_for_v12_enhanced_environment_sanity/
    FIXSANITY(
        "Attempt to Repair Environment Sanity", false
    ), //Whether to attempt to repair the above detections. Imperfect
    ADMINPERMBYPASS(
        "Admins Bypass Permissions", true
    ), //Whether admins bypass block break permissions applied to machines
    SOUNDHASHMAP(
        "Use HashMap for Sound Categories - Only use if necessary", false
    ), //Whether to use a HashMap instead of an EnumMap for sound categories. Necessary on
       //some JVMs but comes with a (hopefully very) minor performance penalty.
    FILEHASH(
        "Compare mod file hashes between client and server", true
    ), //Whether to check and compare mod file hashes at login. Not usually necessary but
       //useful if you have multiple potential builds with the same outwards version (eg
       //playing DragonRealm)
    APRIL("Enable Temporally Dependent Amusement Behavior", true),
    //NOALPHATEST("Disable Alpha Clipping in WorldRenderer", true),
    PARTICLELIMIT("Particle Limit (Vanilla = 4000)", 4000),
    DEBUGKEY("Debug Overlay Key (LWJGL ID)", 0x0F),
    //RECURSE("Recursion Limit Override", -1),
    //COMPOUNDSYNC("Compound Sync Packet System - Use at own risk", false);
    DIRECTOC(
        "Direct OpenComputers Support", false
    ), //Whether OC compat on DAPI TileEntities is done with components as opposed to them
       //being peripherals
    //AUTOREBOOT("Automatic Reboot Interval (Seconds)", -1),
    XPMERGE("Merge XP Orbs Like Items", true),
    RAINTICK(
        "Extra Block Ticks When Raining", true
    ), //Whether rain should trigger extra block ticks, thus making exposed crops grow
       //faster
    PROTECTNEW("Prevent Mobs From Targeting Players Immediately After Logging In", true),
    //SKINCACHE("Cache Skins", true),
    BIOMEFIRE("Biome Humidity Dependent Fire Spread", true),
    ADMINPROFILERS("Restrict profiling abilities to admins", true),
    BYTECODELIST(
        "Bytecodeexec command user UUID whitelist", new String[0]
    ), //Who can use /bytecodeexec. See
       //https://reikakalseki.github.io/minecraft/pages/info/byteexec.html
    CTRLCOLLECT(
        "Automatic Collection of Inventories; set to 'NULL' to disable", Key.LCTRL.name()
    ), //TypeHelper to Website Generator: String; Like Factorio - ctrl-click to collect
       //everything without opening the GUI
    AFK("AFK Timer Threshold (Seconds); Set to 0 to Disable", 120),
    REROUTEEYES(
        "Reroute Ender Eyes to Stronghold Entrances", false
    ), //As opposed to the portal room; this way players are required to actually navigate
       //the structure
    WORLDSIZE(
        "Expected Approximate Maximum World Size (Radius)", 5000
    ), //How large you expect the world to eventually become. Will affect the spread of
       //some fixed-count worldgen objects
    WORLDCENTERX(
        "Expected Approximate World Center Location X", 0
    ), //Where you expect the center of occupied world to be. Will affect the spread of
       //some fixed-count worldgen objects
    WORLDCENTERZ("Expected Approximate World Center Location Z", 0),
    NORAINFX("Disable rain sound and particles", false),
    NOTIFYBYTEEXEC("Bytecodeexec command notifies other admins", false),
    PLAYERMOBCAP(
        "Player-Specific Mob Caps", false
    ), //Whether to make the mob cap work on a per-player basis to avoid the vanilla issue
       //of more players on a server resulting in fewer mobs per player. Disable this in
       //SSP
    SETTINGWARN(
        "Setting Warning Persistence (EVERYLOAD/SETTINGCHANGE/VERSION/ONCE)",
        "SETTINGCHANGE"
    ),
    CHARGEDCERTUS(
        "Allow charged certus to be recognized as normal certus ore", false
    ), //This affects the ore registries used for things like meteors and processing
       //machines
    STOPUNLOADSPREAD(
        "Prevent block spreading near unloaded chunks", true
    ), //For things like grass spread. This helps keep them from inadvertently loading
       //those chunks
    GRASSMEAL("Make bonemeal generate biome-correct foliage instead of tall grass", true),
    VERSIONCHANGEWARN(
        "Version Change Warning Level (0 = None, 1 = ReikaMods only, 2 = All)", 2
    ), //Control what if any mods trigger a "you changed versions" alert
    ;

    private String label;
    private boolean defaultState;
    private int defaultValue;
    private String defaultString;
    private String[] defaultStringArray;
    private Class type;
    private boolean enforcing = false;

    public static final DragonOptions[] optionList = values();

    private DragonOptions(String l, boolean d) {
        label = l;
        defaultState = d;
        type = boolean.class;
    }

    private DragonOptions(String l, boolean d, boolean tag) {
        this(l, d);
        enforcing = true;
    }

    private DragonOptions(String l, int d) {
        label = l;
        defaultValue = d;
        type = int.class;
    }

    private DragonOptions(String l, String s) {
        label = l;
        defaultString = s;
        type = String.class;
    }

    private DragonOptions(String l, String[] s) {
        label = l;
        defaultStringArray = s;
        type = String[].class;
    }

    public boolean isBoolean() {
        return type == boolean.class;
    }

    public boolean isNumeric() {
        return type == int.class;
    }

    public boolean isString() {
        return type == String.class;
    }

    public Class getPropertyType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public boolean getState() {
        return (Boolean) DragonAPIInit.config.getControl(this.ordinal());
    }

    public int getValue() {
        return (Integer) DragonAPIInit.config.getControl(this.ordinal());
    }

    public String getString() {
        return (String) DragonAPIInit.config.getControl(this.ordinal());
    }

    public boolean isDummiedOut() {
        return type == null;
    }

    @Override
    public boolean getDefaultState() {
        return defaultState;
    }

    @Override
    public int getDefaultValue() {
        return defaultValue;
    }

    @Override
    public boolean isEnforcingDefaults() {
        return enforcing;
    }

    @Override
    public boolean shouldLoad() {
        return true;
    }

    @Override
    public boolean isUserSpecific() {
        switch (this) {
            case LOGLOADING:
            case FILELOG:
            case DEBUGMODE:
            case NORENDERS:
            case SOUNDCHANNELS:
            case NOHOTBARSWAP:
            case CHATERRORS:
            case SORTCREATIVE:
            case CUSTOMRENDER:
            case APRIL:
                //case NOALPHATEST:
            case TABNBT:
            case PARTICLELIMIT:
            case DEBUGKEY:
            case NORAINFX:
            case SETTINGWARN:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isStringArray() {
        return type == String[].class;
    }

    @Override
    public String[] getStringArray() {
        return (String[]) DragonAPIInit.config.getControl(this.ordinal());
    }

    @Override
    public String[] getDefaultStringArray() {
        return defaultStringArray;
    }

    @Override
    public String getDefaultString() {
        return defaultString;
    }

    public static Key getCollectKey() {
        if (CTRLCOLLECT.getString().equalsIgnoreCase("null"))
            return null;
        return Key.readFromConfig(DragonAPIInit.instance, CTRLCOLLECT);
    }

    /** In block coords */
    public static ChunkCoordIntPair getWorldCenter() {
        return new ChunkCoordIntPair(WORLDCENTERX.getValue(), WORLDCENTERZ.getValue());
    }
}
