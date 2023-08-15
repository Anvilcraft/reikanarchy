package Reika.DragonAPI.ASM.Patchers.Hooks.Event.World.Gen;

import Reika.DragonAPI.ASM.Patchers.SpawnerGen;
import Reika.DragonAPI.Instantiable.Event.SpawnerGenerationEvent.SpawnerSource;
import Reika.DragonAPI.Libraries.Java.ReikaASMHelper;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class StrongholdSpawnerGen extends SpawnerGen {
    public StrongholdSpawnerGen() {
        super(
            "net.minecraft.world.gen.structure.StructureNetherBridgePieces$Throne", "atr"
        );
    }

    @Override
    protected MethodNode getMethod(ClassNode cn) {
        return ReikaASMHelper.getMethodByName(
            cn,
            "func_74875_a",
            "addComponentParts",
            "(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/world/gen/structure/StructureBoundingBox;)Z"
        );
    }

    @Override
    protected SpawnerSource getEnumEntry() {
        return SpawnerSource.NETHERFORTRESS;
    }
}
