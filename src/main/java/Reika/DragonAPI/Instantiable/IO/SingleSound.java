package Reika.DragonAPI.Instantiable.IO;

import Reika.DragonAPI.Interfaces.Registry.SoundEnum;
import Reika.DragonAPI.Libraries.IO.ReikaPacketHelper;
import Reika.DragonAPI.Libraries.IO.ReikaSoundHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public final class SingleSound implements SoundEnum {
    public final String name;
    public final String path;

    @SideOnly(Side.CLIENT)
    private SoundCategory category;

    public SingleSound(String n, String p) {
        name = n;
        path = p;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPath() {
        return path;
    }

    @SideOnly(Side.CLIENT)
    public void setSoundCategory(SoundCategory cat) {
        category = cat;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public SoundCategory getCategory() {
        return category != null ? category : SoundCategory.MASTER;
    }

    @Override
    public int ordinal() {
        return 0;
    }

    @Override
    public boolean canOverlap() {
        return true;
    }

    @Override
    public void
    playSound(World world, double x, double y, double z, float volume, float pitch) {
        ReikaSoundHelper.playSound(this, world, x, y, z, volume, pitch);
    }

    public void playSound(
        World world,
        double x,
        double y,
        double z,
        float vol,
        float pitch,
        boolean attenuate
    ) {
        ReikaSoundHelper.playSound(this, world, x, y, z, vol, pitch, attenuate);
    }

    public void playSoundNoAttenuation(
        World world, double x, double y, double z, float vol, float pitch, int broadcast
    ) {
        ReikaPacketHelper.sendSoundPacket(
            this, world, x, y, z, vol, pitch, false, broadcast
        );
    }

    @Override
    public void playSound(Entity e, float volume, float pitch) {}

    @Override
    public boolean attenuate() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getModulatedVolume() {
        return 1;
    }

    @Override
    public boolean preload() {
        return false;
    }
}
