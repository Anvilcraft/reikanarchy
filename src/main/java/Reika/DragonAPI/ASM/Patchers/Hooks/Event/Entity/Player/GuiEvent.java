package Reika.DragonAPI.ASM.Patchers.Hooks.Event.Entity.Player;

import Reika.DragonAPI.ASM.Patchers.Patcher;
import Reika.DragonAPI.Libraries.Java.ReikaASMHelper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class GuiEvent extends Patcher {
    public GuiEvent() {
        super("net.minecraft.entity.player.EntityPlayer", "yz");
    }

    @Override
    protected void apply(ClassNode cn) {
        MethodNode m = ReikaASMHelper.getMethodByName(
            cn, "openGui", "(Ljava/lang/Object;ILnet/minecraft/world/World;III)V"
        );
        MethodInsnNode min = (MethodInsnNode
        ) ReikaASMHelper.getFirstOpcode(m.instructions, Opcodes.INVOKESTATIC);
        min.owner = "Reika/DragonAPI/Instantiable/Event/PlayerOpenGuiEvent";
        min.name = "fire";
    }
}
