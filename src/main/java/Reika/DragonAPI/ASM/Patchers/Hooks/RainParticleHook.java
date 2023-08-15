package Reika.DragonAPI.ASM.Patchers.Hooks;

import Reika.DragonAPI.ASM.Patchers.Patcher;
import Reika.DragonAPI.Libraries.Java.ReikaASMHelper;
import net.minecraftforge.classloading.FMLForgePlugin;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class RainParticleHook extends Patcher {
    public RainParticleHook() {
        super("net.minecraft.client.renderer.EntityRenderer", "blt");
    }

    @Override
    protected void apply(ClassNode cn) {
        MethodNode m
            = ReikaASMHelper.getMethodByName(cn, "func_78464_a", "updateRenderer", "()V");
        MethodInsnNode min = ReikaASMHelper.getFirstMethodCallByName(
            cn, m, FMLForgePlugin.RUNTIME_DEOBF ? "func_78484_h" : "addRainParticles"
        );
        min.owner = "Reika/DragonAPI/ASM/ASMCallsClient";
        min.name = "addRainParticlesAndSound";
        min.setOpcode(Opcodes.INVOKESTATIC);
        ReikaASMHelper.addLeadingArgument(min, ReikaASMHelper.convertClassName(cn, true));
    }
}
