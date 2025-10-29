package Reika.DragonAPI.ASM.Patchers.Hooks.Event.Entity;

import Reika.DragonAPI.ASM.Patchers.Patcher;
import Reika.DragonAPI.Libraries.Java.ReikaASMHelper;
import net.minecraftforge.classloading.FMLForgePlugin;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class EntityDecreaseAirEvent extends Patcher {
    public EntityDecreaseAirEvent() {
        super("net.minecraft.entity.EntityLivingBase", "sv");
    }

    @Override
    protected void apply(ClassNode cn) {
        MethodNode m
            = ReikaASMHelper.getMethodByName(cn, "func_70030_z", "onEntityUpdate", "()V");
        String name
            = FMLForgePlugin.RUNTIME_DEOBF ? "func_70055_a" : "isInsideOfMaterial";
        MethodInsnNode min = ReikaASMHelper.getFirstMethodCallByName(cn, m, name);
        min.owner = "Reika/DragonAPI/Instantiable/Event/EntityDecreaseAirEvent";
        min.name = "fire";
        min.desc = "(Lnet/minecraft/entity/EntityLivingBase;)Z";
        min.setOpcode(Opcodes.INVOKESTATIC);
        m.instructions.remove(min.getPrevious()); //Material.water GETSTATIC
    }
}
