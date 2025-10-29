package Reika.DragonAPI.ASM.Patchers.Fixes;

import Reika.DragonAPI.ASM.Patchers.Patcher;
import Reika.DragonAPI.Libraries.Java.ReikaASMHelper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class PermutedBiomeReg extends Patcher {
    public PermutedBiomeReg() {
        super("net.minecraftforge.common.BiomeDictionary");
    }

    @Override
    protected void apply(ClassNode cn) {
        MethodNode m = ReikaASMHelper.getMethodByName(cn, "registerVanillaBiomes", "()V");
        m.instructions.insertBefore(
            m.instructions.getLast(),
            new MethodInsnNode(
                Opcodes.INVOKESTATIC,
                "Reika/DragonAPI/ASM/ASMCalls",
                "registerPermutedBiomesToDictionary",
                "()V",
                false
            )
        );
    }
}
