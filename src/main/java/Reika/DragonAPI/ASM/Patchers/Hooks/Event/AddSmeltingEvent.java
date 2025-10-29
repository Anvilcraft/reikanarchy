package Reika.DragonAPI.ASM.Patchers.Hooks.Event;

import Reika.DragonAPI.ASM.Patchers.Patcher;
import org.objectweb.asm.tree.ClassNode;

@Deprecated
public class AddSmeltingEvent extends Patcher {
    public AddSmeltingEvent() {
        super("net.minecraft.item.crafting.FurnaceRecipes$", "afa");
    }

    @Override
    protected void apply(ClassNode cn) {
        /* No longer necessary due to override
         *
        MethodNode m = ReikaASMHelper.getMethodByName(cn, "func_151394_a",
        "func_151394_a",
        "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;F)V"); LabelNode L1
        = new LabelNode(); InsnList li = new InsnList(); li.add(new
        VarInsnNode(Opcodes.ALOAD, 1)); li.add(new VarInsnNode(Opcodes.ALOAD, 2));
        li.add(new VarInsnNode(Opcodes.FLOAD, 3));
        li.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
        "Reika/DragonAPI/Instantiable/Event/AddSmeltingEvent", "fire",
        "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;F)Z", false));
        li.add(new JumpInsnNode(Opcodes.IFEQ, L1));
        m.instructions.insert(li);

        AbstractInsnNode ain = ReikaASMHelper.getLastOpcode(m.instructions,
        Opcodes.RETURN); m.instructions.insertBefore(ain, L1);
         */
    }
}
