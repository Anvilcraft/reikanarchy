/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.Extras;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.zip.ZipFile;

import Reika.DragonAPI.Base.DragonAPIMod;
import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.Exception.InstallationException;
import Reika.DragonAPI.Libraries.Java.ReikaObfuscationHelper;
import Reika.DragonAPI.Libraries.Java.ReikaStringParser;
import Reika.DragonAPI.Libraries.Java.SemanticVersionParser;
import Reika.DragonAPI.Libraries.Java.SemanticVersionParser.SemanticVersion;

public class ModVersion implements Comparable<ModVersion> {
    public static final ModVersion source = new ModVersion(0) {
        @Override
        public boolean equals(Object o) {
            return o == this;
        }
        @Override
        public String toString() {
            return "Source Code";
        }
        @Override
        public boolean isCompiled() {
            return false;
        }
        @Override
        public boolean verify() {
            return false;
        }
    };

    public static final class ErroredVersion extends ModVersion {
        public final String errorMessage;

        public ErroredVersion(Throwable t) {
            this(t.getMessage());
        }

        public ErroredVersion(String err) {
            super(0);
            errorMessage = err;
        }
    }

    public final int majorVersion;
    public final String subVersion;

    private ModVersion(int major) {
        this(major, '\0');
    }

    private ModVersion(int major, char minor) {
        majorVersion = major;
        subVersion
            = minor == '\0' ? "" : Character.toString(minor).toLowerCase(Locale.ENGLISH);
    }

    @Override
    public int hashCode() {
        return (majorVersion << 16) | (subVersion.isEmpty() ? 0 : subVersion.charAt(0));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ModVersion) {
            ModVersion m = (ModVersion) o;
            return m.majorVersion == majorVersion && m.subVersion.equals(subVersion);
        }
        return false;
    }

    public boolean isCompiled() {
        return true;
    }

    public boolean verify() {
        return true;
    }

    @Override
    public String toString() {
        return "v" + majorVersion + subVersion;
    }

    public static ModVersion getFromString(String s) {
        if (s.startsWith("$") || s.startsWith("@"))
            return source;
        if (s.contains("URL TIMEOUT"))
            return new ErroredVersion("Connection Timeout");
        if (s.startsWith("v") || s.startsWith("V"))
            s = s.substring(1);
        char c = s.charAt(s.length() - 1);
        if (Character.isDigit(c)) {
            return new ModVersion(Integer.parseInt(s));
        } else {
            String major = s.substring(0, s.length() - 1);
            return new ModVersion(Integer.parseInt(major), c);
        }
    }

    private int getSubVersionIndex() {
        return subVersion == null || subVersion.isEmpty() ? 0
                                                          : subVersion.charAt(0) - 'a';
    }

    @Override
    public int compareTo(ModVersion v) {
        return 32 * (majorVersion - v.majorVersion)
            + (this.getSubVersionIndex() - v.getSubVersionIndex());
    }

    public boolean isNewerMinorVersion(ModVersion v) {
        return v.majorVersion == majorVersion
            && v.getSubVersionIndex() < this.getSubVersionIndex();
    }

    /**
     * Not for setting ModContainer data; only use this during construction to pass to
     * FML @Mod!
     */
    public static ModVersion readFromJar(ZipFile jar, String innerName) {
        if (DragonAPICore.isReikasComputer()
            && ReikaObfuscationHelper.isDeObfEnvironment())
            return source;
        Properties p = new Properties();
        String path = ReikaStringParser.stripSpaces(
            "version_" + ReikaStringParser.stripSpaces(innerName + ".properties")
        );
        try {
            InputStream stream
                = ModVersion.class.getClassLoader().getResourceAsStream(path);
            if (stream == null) {
                return new ErroredVersion("Null properties file");
            }
            p.load(stream);
            String mj = p.getProperty("Major");
            String mn = p.getProperty("Minor");
            if (mj == null || mn == null || mj.equals("null") || mn.equals("null")
                || mj.isEmpty() || mn.isEmpty())
                return new ErroredVersion("Properties missing");
            return getFromString(mj + mn);
        } catch (IOException e) {
            e.printStackTrace();
            return new ErroredVersion(e);
        }
    }

    public static ModVersion readFromFile(DragonAPIMod mod) {
        if (DragonAPICore.isReikasComputer()
            && ReikaObfuscationHelper.isDeObfEnvironment())
            return source;
        Properties p = new Properties();
        String path = ReikaStringParser.stripSpaces(
            "version_"
            + ReikaStringParser.stripSpaces(
                mod.getTechnicalName().toLowerCase(Locale.ENGLISH)
            )
            + ".properties"
        );
        try {
            InputStream stream
                = ModVersion.class.getClassLoader().getResourceAsStream(path);
            if (stream == null) {
                throw new FileNotFoundException(
                    "Version file for " + mod.getDisplayName() + " is missing!"
                );
            }
            p.load(stream);
            String mj = p.getProperty("Major");
            String mn = p.getProperty("Minor");
            if (mj == null || mn == null || mj.equals("null") || mn.equals("null")
                || mj.isEmpty() || mn.isEmpty())
                throw new InstallationException(
                    mod,
                    "The version file was either damaged, overwritten, or is missing!"
                );
            return getFromString(mj + mn);
        } catch (IOException e) {
            throw new InstallationException(
                mod, "The version file was either damaged, overwritten, or is missing!", e
            );
        }
    }

    public String toSemanticVersion() {
        return String.format("%d.%d", majorVersion, 1 + subVersion.charAt(0) - 'a');
    }

    public static ModVersion fromSemanticVersion(String s) {
        SemanticVersion sm = SemanticVersionParser.getVersion(s);
        int[] ver = sm.getVersions();
        int major = ver.length > 0 ? ver[0] : 1;
        int minor = ver.length > 1 ? ver[1] : 1;
        return new ModVersion(major, Character.toChars('a' - 1 + minor)[0]);
    }
}
