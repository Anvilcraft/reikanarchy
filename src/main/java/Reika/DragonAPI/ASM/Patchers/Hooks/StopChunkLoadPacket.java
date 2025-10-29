package Reika.DragonAPI.ASM.Patchers.Hooks;

import Reika.DragonAPI.ASM.Patchers.Patcher;
import Reika.DragonAPI.Libraries.Java.ReikaASMHelper;
import net.minecraftforge.classloading.FMLForgePlugin;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class StopChunkLoadPacket extends Patcher {
    public StopChunkLoadPacket() {
        super("net.minecraft.server.management.ServerConfigurationManager", "oi");
    }

    @Override
    protected void apply(ClassNode cn) {
        MethodNode m = ReikaASMHelper.getMethodByName(
            cn,
            "func_72358_d",
            "updatePlayerPertinentChunks",
            "(Lnet/minecraft/entity/player/EntityPlayerMP;)V"
        );

        MethodInsnNode min = ReikaASMHelper.getFirstMethodCallByName(
            cn,
            m,
            FMLForgePlugin.RUNTIME_DEOBF ? "func_72685_d" : "updatePlayerPertinentChunks"
        );
        min.setOpcode(Opcodes.INVOKESTATIC);
        min.owner = "Reika/DragonAPI/Auxiliary/Trackers/PlayerChunkTracker";
        min.name = "tryUpdatePlayerPertinentChunks";
        min.desc
            = "(Lnet/minecraft/server/management/PlayerManager;Lnet/minecraft/entity/player/EntityPlayerMP;)V";
    }
}
