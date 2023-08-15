package Reika.DragonAPI.ModInteract.DeepInteract;

import Reika.DragonAPI.ASM.APIStripper.Strippable;
import Reika.DragonAPI.ASM.DependentMethodStripper.SmartStrip;
import Reika.DragonAPI.Instantiable.Event.TileEntityMoveEvent;
import com.amadornes.framez.api.FramezApi;
import com.amadornes.framez.api.movement.BlockMovementType;
import com.amadornes.framez.api.movement.IMovementHandler;
import com.amadornes.framez.api.movement.IMovingBlock;
import cpw.mods.fml.common.Loader;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class FrameBlacklist {
    public static final FrameBlacklist instance = new FrameBlacklist();

    private FrameBlacklist() {
        if (Loader.isModLoaded("framez"))
            FramezApi.inst().getMovementApi().registerMovementHandler(new FramezHandler()
            ); //FramezApi.instance().movement().registerMovementHandler(new
               //FramezHandler());
    }

    private boolean
    isBlacklisted(World world, int x, int y, int z, Block b, int meta, TileEntity te) {
        return MinecraftForge.EVENT_BUS.post(
            new TileEntityMoveEvent(world, x, y, z, b, meta, te)
        );
    }

    @Strippable("com.amadornes.framez.api.movement.IMovementHandler")
    public class FramezHandler implements IMovementHandler {
        private FramezHandler() {}

        @Override
        @SmartStrip
        public boolean handleStartMoving(IMovingBlock block) {
            return FrameBlacklist.this.isBlacklisted(
                block.getWorld(),
                block.getX(),
                block.getY(),
                block.getZ(),
                block.getBlock(),
                block.getMetadata(),
                block.getTileEntity()
            );
        }

        @Override
        @SmartStrip
        public boolean handleFinishMoving(IMovingBlock block) {
            return FrameBlacklist.this.isBlacklisted(
                block.getWorld(),
                block.getX(),
                block.getY(),
                block.getZ(),
                block.getBlock(),
                block.getMetadata(),
                block.getTileEntity()
            );
        }

        @Override
        public BlockMovementType
        getMovementType(World world, Integer x, Integer y, Integer z) {
            return FrameBlacklist.this.isBlacklisted(
                       world,
                       x,
                       y,
                       z,
                       world.getBlock(x, y, z),
                       world.getBlockMetadata(x, y, z),
                       world.getTileEntity(x, y, z)
                   )
                ? BlockMovementType.UNMOVABLE
                : null;
        }
    }
}
