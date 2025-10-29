package Reika.DragonAPI.ASM.Patchers.Hooks.Event.Render;

import Reika.DragonAPI.ASM.Patchers.Patcher;
import Reika.DragonAPI.Libraries.Java.ReikaASMHelper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class CloudRenderEvent1 extends Patcher {
    public CloudRenderEvent1() {
        super("net.minecraft.client.settings.GameSettings", "bbj");
    }

    @Override
    protected void apply(ClassNode cn) {
        MethodNode m = ReikaASMHelper.getMethodByName(
            cn, "func_74309_c", "shouldRenderClouds", "()Z"
        );
        m.instructions.clear();
        m.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        m.instructions.add(new MethodInsnNode(
            Opcodes.INVOKESTATIC,
            "Reika/DragonAPI/Instantiable/Event/Client/CloudRenderEvent",
            "fireNoRedirect",
            "(Lnet/minecraft/client/settings/GameSettings;)Z",
            false
        ));
        m.instructions.add(new InsnNode(Opcodes.IRETURN));
    }
}
