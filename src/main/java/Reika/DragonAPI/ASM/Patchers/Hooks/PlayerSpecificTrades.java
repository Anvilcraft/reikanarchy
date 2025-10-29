package Reika.DragonAPI.ASM.Patchers.Hooks;

import Reika.DragonAPI.ASM.Patchers.Patcher;
import Reika.DragonAPI.Libraries.Java.ReikaASMHelper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class PlayerSpecificTrades extends Patcher {
    public PlayerSpecificTrades() {
        super("net.minecraft.entity.passive.EntityVillager", "yv");
    }

    @Override
    protected void apply(ClassNode cn) {
        MethodNode m = ReikaASMHelper.getMethodByName(
            cn,
            "func_70934_b",
            "getRecipes",
            "(Lnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/village/MerchantRecipeList;"
        );
        AbstractInsnNode ain
            = ReikaASMHelper.getLastOpcode(m.instructions, Opcodes.ARETURN);
        m.instructions.insertBefore(ain, new VarInsnNode(Opcodes.ALOAD, 1));
        m.instructions.insertBefore(
            ain,
            new MethodInsnNode(
                Opcodes.INVOKESTATIC,
                "Reika/DragonAPI/Interfaces/PlayerSpecificTrade$MerchantRecipeHooks",
                "filterRecipeList",
                "(Lnet/minecraft/village/MerchantRecipeList;Lnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/village/MerchantRecipeList;",
                false
            )
        );
    }
}
