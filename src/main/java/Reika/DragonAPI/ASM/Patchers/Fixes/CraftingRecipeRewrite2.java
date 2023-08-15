/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.ASM.Patchers.Fixes;

import Reika.DragonAPI.ASM.Patchers.Patcher;
import Reika.DragonAPI.Libraries.Java.ReikaASMHelper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

@Deprecated
public class CraftingRecipeRewrite2 extends Patcher {
    public CraftingRecipeRewrite2() {
        super("cpw.mods.fml.common.registry.GameRegistry#");
    }

    @Override
    protected void apply(ClassNode cn) {
        MethodNode m = ReikaASMHelper.getMethodByName(
            cn, "addRecipe", "(Lnet/minecraft/item/crafting/IRecipe;)V"
        );
        m.instructions.clear();
        m.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        m.instructions.add(new MethodInsnNode(
            Opcodes.INVOKESTATIC,
            "Reika/DragonAPI/Extras/ReplacementCraftingHandler",
            "addRecipe",
            "(Lnet/minecraft/item/crafting/IRecipe;)V",
            false
        ));
        m.instructions.add(new InsnNode(Opcodes.RETURN));
    }
}
