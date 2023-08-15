/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.Instantiable.IO;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import Reika.DragonAPI.DragonAPIInit;
import Reika.DragonAPI.Libraries.Java.ReikaJavaLibrary;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.data.IMetadataSection;

@SideOnly(Side.CLIENT)
public class DirectResource implements IResource {
    public final String path;

    public boolean cacheData = true;

    private byte[] data;

    public DirectResource(String path) {
        this.path = path;
    }

    @Override
    public final InputStream getInputStream() {
        if (cacheData) {
            //ReikaJavaLibrary.pConsole("Loading "+path+", data="+data);
            if (data == null) {
                try (InputStream st = this.calcStream()) {
                    if (st == null)
                        throw new RuntimeException("Resource not found at " + path);
                    data = ReikaJavaLibrary.streamToBytes(st);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //ReikaJavaLibrary.pConsole("Loaded cache for "+path+", data="+data);
            return new ByteArrayInputStream(data);
        } else {
            //ReikaJavaLibrary.pConsole("Skipped cache for "+path);
            return this.calcStream();
        }
    }

    protected InputStream calcStream() {
        File f = new File(path);
        if (f.exists()) {
            try {
                return new FileInputStream(f);
            } catch (FileNotFoundException e) {
                return null;
            }
        } else
            return DragonAPIInit.class.getClassLoader().getResourceAsStream(path);
    }

    @Override
    public final boolean hasMetadata() {
        return false;
    }

    @Override
    public final IMetadataSection getMetadata(String p_110526_1_) {
        return null;
    }
}
