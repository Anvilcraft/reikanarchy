package Reika.DragonAPI.ASM.Patchers.Hooks.Event.Render;

import Reika.DragonAPI.ASM.Patchers.Patcher;
import Reika.DragonAPI.Libraries.Java.ReikaASMHelper;
import net.minecraftforge.classloading.FMLForgePlugin;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class EntityRender extends Patcher {
    public EntityRender() {
        super("net.minecraft.client.renderer.entity.RenderManager", "bnn");
    }

    @Override
    protected void apply(ClassNode cn) {
        MethodNode m = ReikaASMHelper.getMethodByName(
            cn, "func_147939_a", "func_147939_a", "(Lnet/minecraft/entity/Entity;DDDFFZ)Z"
        );
        MethodInsnNode min = ReikaASMHelper.getFirstMethodCallByName(
            cn, m, FMLForgePlugin.RUNTIME_DEOBF ? "func_76986_a" : "doRender"
        );
        min.setOpcode(Opcodes.INVOKESTATIC);
        min.owner = "Reika/DragonAPI/Instantiable/Event/Client/EntityRenderEvent";
        min.name = "fire";
        min.desc = ReikaASMHelper.addLeadingArgument(
            min.desc, "Lnet/minecraft/client/renderer/entity/Render;"
        );
    }
}
