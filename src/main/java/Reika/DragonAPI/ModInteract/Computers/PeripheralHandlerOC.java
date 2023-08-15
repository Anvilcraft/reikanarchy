/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.ModInteract.Computers;

import Reika.DragonAPI.Base.TileEntityBase;
import li.cil.oc.api.Network;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.network.ManagedPeripheral;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.DriverSidedTileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class PeripheralHandlerOC extends DriverSidedTileEntity {
    @Override
    public ManagedEnvironment
    createEnvironment(World world, int x, int y, int z, ForgeDirection side) {
        return new OCManagedEnvironment((TileEntityBase) world.getTileEntity(x, y, z));
    }

    @Override
    public Class<?> getTileEntityClass() {
        return TileEntityBase.class;
    }

    private static class OCManagedEnvironment
        extends li.cil.oc.api.prefab.ManagedEnvironment
        implements li.cil.oc.api.driver.NamedBlock, ManagedPeripheral {
        private final TileEntityBase tile;

        private OCManagedEnvironment(TileEntityBase te) {
            tile = te;

            this.setNode(Network.newNode(this, Visibility.Network)
                             .withComponent(te.getType(), te.getOCNetworkVisibility())
                             .create());
        }

        @Override
        public String[] methods() {
            return tile.methods();
        }

        @Override
        public Object[] invoke(String method, Context context, Arguments args)
            throws Exception {
            return tile.invoke(method, context, args);
        }

        @Override
        public int priority() {
            return (int) 10e6;
        }

        @Override
        public String preferredName() {
            return tile.getType();
        }
    }
}
