/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.ASM.Patchers.Hooks.Event.Entity;

import Reika.DragonAPI.ASM.Patchers.Patcher;
import Reika.DragonAPI.Libraries.Java.ReikaASMHelper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class EnderAttackTPEvent extends Patcher {
    public EnderAttackTPEvent() {
        super("net.minecraft.entity.monster.EntityEnderman", "ya");
    }

    @Override
    protected void apply(ClassNode cn) {
        MethodNode m = ReikaASMHelper.getMethodByName(
            cn,
            "func_70097_a",
            "attackEntityFrom",
            "(Lnet/minecraft/util/DamageSource;F)Z"
        );

        AbstractInsnNode loc
            = ReikaASMHelper.getNthOpcode(m.instructions, Opcodes.INSTANCEOF, 3);

        InsnList li = new InsnList();
        li.add(new VarInsnNode(Opcodes.ALOAD, 0));
        li.add(new VarInsnNode(Opcodes.ALOAD, 1));
        li.add(new VarInsnNode(Opcodes.FLOAD, 2));
        li.add(new MethodInsnNode(
            Opcodes.INVOKESTATIC,
            "Reika/DragonAPI/Instantiable/Event/EnderAttackTPEvent",
            "fire",
            "(Lnet/minecraft/entity/monster/EntityEnderman;Lnet/minecraft/util/DamageSource;F)Z",
            false
        ));

        //ReikaASMHelper.changeOpcode(loc.getNext(), Opcodes.IFNE);
        m.instructions.insertBefore(loc.getPrevious(), li);
        m.instructions.remove(loc.getPrevious());
        m.instructions.remove(loc);

        //ReikaJavaLibrary.pConsole(ReikaASMHelper.clearString(m.instructions));

        //m.instructions.clear();
        //m.instructions.add(new InsnNode(Opcodes.ICONST_0));
        //m.instructions.add(new InsnNode(Opcodes.IRETURN));
    }
}
