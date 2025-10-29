package Reika.DragonAPI.ASM.Patchers.Fixes;

import Reika.DragonAPI.ASM.Patchers.Patcher;
import Reika.DragonAPI.Libraries.Java.ReikaASMHelper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class TamedMobBeds extends Patcher {
    public TamedMobBeds() {
        super("net.minecraft.entity.player.EntityPlayer", "yz");
    }

    @Override
    protected void apply(ClassNode cn) {
        MethodInsnNode min = ReikaASMHelper.getFirstMethodCall(
            cn, this.getMethod(cn), "java/util/List", "isEmpty", "()Z"
        );
        min.setOpcode(Opcodes.INVOKESTATIC);
        min.owner = "Reika/DragonAPI/ASM/ASMCalls";
        min.name = "allowMobSleeping";
        min.desc = "(Ljava/util/List;)Z";
        min.itf = false;
    }

    protected MethodNode getMethod(ClassNode cn) {
        return ReikaASMHelper.getMethodByName(
            cn,
            "func_71018_a",
            "sleepInBedAt",
            "(III)Lnet/minecraft/entity/player/EntityPlayer$EnumStatus;"
        );
    }
}
