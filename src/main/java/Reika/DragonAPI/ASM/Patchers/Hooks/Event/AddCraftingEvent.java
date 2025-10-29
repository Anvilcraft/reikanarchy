package Reika.DragonAPI.ASM.Patchers.Hooks.Event;

import Reika.DragonAPI.ASM.Patchers.Patcher;
import Reika.DragonAPI.Libraries.Java.ReikaASMHelper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

//@Deprecated
public class AddCraftingEvent extends Patcher {
    public AddCraftingEvent() { //replace list with one that fires events
        super("net.minecraft.item.crafting.CraftingManager", "afe");
    }

    @Override
    protected void apply(ClassNode cn) {
        MethodNode m = ReikaASMHelper.getMethodByName(cn, "<init>", "()V");
        TypeInsnNode type
            = (TypeInsnNode) ReikaASMHelper.getFirstOpcode(m.instructions, Opcodes.NEW);
        MethodInsnNode cons = (MethodInsnNode) type.getNext().getNext();

        String s = "Reika/DragonAPI/Instantiable/Data/Collections/EventRecipeList";

        type.desc = s;
        cons.owner = s;

        AbstractInsnNode ain = cons.getNext();
        AbstractInsnNode end
            = ReikaASMHelper.getLastOpcode(m.instructions, Opcodes.RETURN);
        InsnList enable = new InsnList();
        InsnList disable = new InsnList();
        enable.add(new InsnNode(Opcodes.ICONST_1));
        enable.add(new FieldInsnNode(
            Opcodes.PUTSTATIC,
            "Reika/DragonAPI/Instantiable/Event/AddRecipeEvent",
            "isVanillaPass",
            "Z"
        ));
        disable.add(new InsnNode(Opcodes.ICONST_0));
        disable.add(new FieldInsnNode(
            Opcodes.PUTSTATIC,
            "Reika/DragonAPI/Instantiable/Event/AddRecipeEvent",
            "isVanillaPass",
            "Z"
        ));
        m.instructions.insert(ain, enable);
        m.instructions.insertBefore(end, disable);
    }
}
