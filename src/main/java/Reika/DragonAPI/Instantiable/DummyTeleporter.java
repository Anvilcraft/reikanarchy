package Reika.DragonAPI.Instantiable;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class DummyTeleporter extends Teleporter {
    private final WorldServer world;

    public DummyTeleporter(int dim) {
        this(MinecraftServer.getServer().worldServerForDimension(dim));
    }

    public DummyTeleporter(WorldServer world) {
        super(world);
        this.world = world;
    }

    @Override
    public void placeInPortal(Entity e, double x, double y, double z, float facing) {
        this.placeInExistingPortal(e, x, y, z, facing);
    }

    @Override
    public boolean
    placeInExistingPortal(Entity entity, double x, double y, double z, float facing) {
        return true;
    }

    private void makeReturnPortal(World world, int x, int y, int z) {}

    @Override
    public boolean makePortal(Entity e) {
        return false; //super.makePortal(e);
    }
}
