package Reika.DragonAPI.ASM.Patchers.Hooks;

import Reika.DragonAPI.ASM.Patchers.Patcher;
import Reika.DragonAPI.Auxiliary.CoreModDetection;
import Reika.DragonAPI.Libraries.Java.ReikaASMHelper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class CrashNotifications extends Patcher {
    public CrashNotifications() {
        super("net.minecraft.crash.CrashReport", "b");
    }

    @Override
    protected void apply(ClassNode cn) {
        MethodNode m = ReikaASMHelper.getMethodByName(
            cn, "func_71504_g", "populateEnvironment", "()V"
        );

        if (CoreModDetection.OPTIFINE.isInstalled()) {
            InsnList li = new InsnList();
            li.add(new VarInsnNode(Opcodes.ALOAD, 0));
            li.add(new MethodInsnNode(
                Opcodes.INVOKESTATIC,
                "Reika/DragonAPI/Auxiliary/Trackers/CrashNotifications",
                "fireOF",
                "(Lnet/minecraft/crash/CrashReport;)V",
                false
            ));
            m.instructions.insertBefore(
                ReikaASMHelper.getLastOpcode(m.instructions, Opcodes.RETURN), li
            );
        } else {
            MethodInsnNode min
                = ReikaASMHelper.getFirstMethodCallByName(cn, m, "enhanceCrashReport");
            min.owner = "Reika/DragonAPI/Auxiliary/Trackers/CrashNotifications";
            min.name = "fire";
            min.desc
                = "(Lcpw/mods/fml/common/FMLCommonHandler;Lnet/minecraft/crash/CrashReport;Lnet/minecraft/crash/CrashReportCategory;)V";
            min.setOpcode(Opcodes.INVOKESTATIC);
        }
    }
}
