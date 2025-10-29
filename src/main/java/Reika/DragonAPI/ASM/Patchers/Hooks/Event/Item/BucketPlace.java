package Reika.DragonAPI.ASM.Patchers.Hooks.Event.Item;

import Reika.DragonAPI.ASM.Patchers.Patcher;
import Reika.DragonAPI.Libraries.Java.ReikaASMHelper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class BucketPlace extends Patcher {
    public BucketPlace() {
        super("net.minecraft.item.ItemBucket", "abo");
    }

    @Override
    protected void apply(ClassNode cn) {
        MethodNode m = ReikaASMHelper.getMethodByName(
            cn,
            "func_77875_a",
            "tryPlaceContainedLiquid",
            "(Lnet/minecraft/world/World;III)Z"
        );

        MethodInsnNode min = (MethodInsnNode
        ) ReikaASMHelper.getLastOpcode(m.instructions, Opcodes.INVOKEVIRTUAL);
        min.setOpcode(Opcodes.INVOKESTATIC);
        min.owner = "Reika/DragonAPI/Instantiable/Event/PlaceBucketEvent";
        min.name = "fire";
        ReikaASMHelper.addLeadingArgument(min, "Lnet/minecraft/world/World;");
    }
}
