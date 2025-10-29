package Reika.DragonAPI.Instantiable.Event.Client;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;

public abstract class ChatEvent extends Event {
    public final String chatMessage;

    public ChatEvent(String message) {
        super();
        chatMessage = message;
    }

    public static class ChatEventPre extends ChatEvent {
        public ChatEventPre(String msg) {
            super(msg);
        }
    }

    public static class ChatEventPost extends ChatEvent {
        public ChatEventPost(String msg) {
            super(msg);
        }
    }

    public static void firePre(IChatComponent msg) {
        MinecraftForge.EVENT_BUS.post(new ChatEventPre(msg.getUnformattedText()));
    }

    public static void firePost(IChatComponent msg) {
        MinecraftForge.EVENT_BUS.post(new ChatEventPost(msg.getUnformattedText()));
    }
}
