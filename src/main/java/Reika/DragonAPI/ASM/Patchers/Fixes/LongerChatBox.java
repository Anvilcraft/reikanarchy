package Reika.DragonAPI.ASM.Patchers.Fixes;

import Reika.DragonAPI.ASM.Patchers.Patcher;
import Reika.DragonAPI.Libraries.Java.ReikaASMHelper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodNode;

@Deprecated
public class LongerChatBox extends Patcher {
    public LongerChatBox() {
        super("*net.minecraft.client.gui.GuiChat", "*bct");
    }

    @Override
    protected void apply(ClassNode cn) {
        MethodNode m
            = ReikaASMHelper.getMethodByName(cn, "func_73866_w_", "initGui", "()V");
        IntInsnNode iin
            = (IntInsnNode) ReikaASMHelper.getFirstOpcode(m.instructions, Opcodes.BIPUSH);
        iin.operand = 400;
        iin.setOpcode(Opcodes.SIPUSH);
    }
}
