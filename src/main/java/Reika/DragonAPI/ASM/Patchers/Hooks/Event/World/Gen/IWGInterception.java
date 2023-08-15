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
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class IWGInterception extends Patcher {
    public IWGInterception() {
        super("cpw.mods.fml.common.registry.GameRegistry");
    }

    @Override
    public void apply(ClassNode cn) {
        MethodNode m = ReikaASMHelper.getMethodByName(
            cn,
            "generateWorld",
            "(IILnet/minecraft/world/World;Lnet/minecraft/world/chunk/IChunkProvider;Lnet/minecraft/world/chunk/IChunkProvider;)V"
        );
        MethodInsnNode min = ReikaASMHelper.getFirstMethodCallByName(cn, m, "generate");
        min.name = "interceptIWG";
        min.owner = "Reika/DragonAPI/Auxiliary/WorldGenInterceptionRegistry";
        ReikaASMHelper.addLeadingArgument(min, "Lcpw/mods/fml/common/IWorldGenerator;");
        min.setOpcode(Opcodes.INVOKESTATIC);
        min.itf = false;
    }
}
