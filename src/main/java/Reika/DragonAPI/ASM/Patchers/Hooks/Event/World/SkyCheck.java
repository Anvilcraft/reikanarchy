/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.ASM.Patchers.Hooks.Event.World;

import Reika.DragonAPI.ASM.Patchers.Patcher;
import Reika.DragonAPI.Libraries.Java.ReikaASMHelper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class SkyCheck extends Patcher {
    public SkyCheck() {
        super("net.minecraft.world.chunk.Chunk", "apx");
    }

    @Override
    protected void apply(ClassNode cn) {
        MethodNode m = ReikaASMHelper.getMethodByName(
            cn, "func_76619_d", "canBlockSeeTheSky", "(III)Z"
        );
        m.instructions.clear();
        m.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        m.instructions.add(new VarInsnNode(Opcodes.ILOAD, 1));
        m.instructions.add(new VarInsnNode(Opcodes.ILOAD, 2));
        m.instructions.add(new VarInsnNode(Opcodes.ILOAD, 3));
        m.instructions.add(new MethodInsnNode(
            Opcodes.INVOKESTATIC,
            "Reika/DragonAPI/Instantiable/Event/CanSeeSkyEvent",
            "fire",
            "(Lnet/minecraft/world/chunk/Chunk;III)Z",
            false
        ));
        m.instructions.add(new InsnNode(Opcodes.IRETURN));
    }
}
