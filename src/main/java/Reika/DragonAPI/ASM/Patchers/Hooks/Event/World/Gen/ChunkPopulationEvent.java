/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2018
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.ASM.Patchers.Hooks.Event.World.Gen;

import Reika.DragonAPI.ASM.Patchers.Patcher;
import Reika.DragonAPI.Libraries.Java.ReikaASMHelper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class ChunkPopulationEvent extends Patcher {
    public ChunkPopulationEvent() {
        super("net.minecraft.world.gen.ChunkProviderServer", "ms");
    }

    @Override
    public void apply(ClassNode cn) {
        MethodNode m = ReikaASMHelper.getMethodByName(
            cn,
            "func_73153_a",
            "populate",
            "(Lnet/minecraft/world/chunk/IChunkProvider;II)V"
        );
        boolean primed = false;
        for (int i = 0; i < m.instructions.size(); i++) {
            AbstractInsnNode ain = m.instructions.get(i);
            if (ain.getOpcode() == Opcodes.INVOKEINTERFACE) {
                primed = true;
            } else if (primed && ain.getOpcode() == Opcodes.INVOKESTATIC) {
                MethodInsnNode min = (MethodInsnNode) ain;
                if (min.owner.contains("GameRegistry")
                    && min.name.equals("generateWorld")) {
                    primed = false;

                    min.owner = "Reika/DragonAPI/Auxiliary/WorldGenInterceptionRegistry";
                    min.name = "interceptChunkPopulation";
                    min.desc
                        = "(IILnet/minecraft/world/World;Lnet/minecraft/world/chunk/IChunkProvider;Lnet/minecraft/world/chunk/IChunkProvider;)V";

                    ReikaASMHelper.log("Successfully applied " + this + " ASM handler!");
                    break;
                }
            }
        }
    }
}
