package Reika.DragonAPI.ASM.Patchers.Hooks.Event.Render;

import Reika.DragonAPI.ASM.Patchers.Patcher;
import Reika.DragonAPI.Libraries.Java.ReikaASMHelper;
import net.minecraftforge.classloading.FMLForgePlugin;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class CloudRenderEvent2 extends Patcher {
    public CloudRenderEvent2() {
        super("net.minecraft.client.renderer.EntityRenderer", "blt");
    }

    @Override
    protected void apply(ClassNode cn) {
        MethodNode m = ReikaASMHelper.getMethodByName(
            cn,
            "func_82829_a",
            "renderCloudsCheck",
            "(Lnet/minecraft/client/renderer/RenderGlobal;F)V"
        );

        MethodInsnNode min = ReikaASMHelper.getFirstMethodCallByName(
            cn, m, FMLForgePlugin.RUNTIME_DEOBF ? "func_74309_c" : "shouldRenderClouds"
        );
        min.owner = "Reika/DragonAPI/Instantiable/Event/Client/CloudRenderEvent";
        min.name = "fire";
        min.desc = "(Lnet/minecraft/client/settings/GameSettings;)Z";
        min.setOpcode(Opcodes.INVOKESTATIC);
    }
}
