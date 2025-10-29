package Reika.DragonAPI.ASM.Patchers.Fixes;

import Reika.DragonAPI.ASM.Patchers.Patcher;
import Reika.DragonAPI.Libraries.Java.ReikaASMHelper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class LavaHandlingFix extends Patcher {
    public LavaHandlingFix() {
        super("net.minecraft.entity.Entity", "sa");
    }

    @Override
    protected void apply(ClassNode cn) {
        MethodNode m = ReikaASMHelper.getMethodByName(
            cn, "func_70058_J", "handleLavaMovement", "()Z"
        );
        m.instructions.clear();
        m.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        m.instructions.add(new MethodInsnNode(
            Opcodes.INVOKESTATIC,
            "Reika/DragonAPI/ASM/ASMCalls",
            "handleLavaMovement",
            "(Lnet/minecraft/entity/Entity;)Z",
            false
        ));
        m.instructions.add(new InsnNode(Opcodes.IRETURN));
    }
}
