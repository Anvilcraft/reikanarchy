package Reika.DragonAPI.Base;

import Reika.DragonAPI.Interfaces.Registry.TileEnum;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public abstract class TileEntityRegistryBase<E extends TileEnum> extends TileEntityBase {
    @Override
    public final Block getTileEntityBlockID() {
        return this.getTile().getBlock();
    }

    public abstract E getTile();

    public final int getIndex() {
        return this.getTile().ordinal();
    }

    @Override
    public String getTEName() {
        return this.getTile().getName();
    }

    @Override
    public abstract void updateEntity(World world, int x, int y, int z, int meta);

    @Override
    protected abstract void animateWithTick(World world, int x, int y, int z);

    public final boolean isThisTE(Block id, int meta) {
        return id == this.getTileEntityBlockID() && meta == this.getIndex();
    }
}
