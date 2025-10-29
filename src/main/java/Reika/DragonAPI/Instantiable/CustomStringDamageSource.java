package Reika.DragonAPI.Instantiable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;

public class CustomStringDamageSource extends DamageSource {
    private final String message;

    /**
     * Takes one arg - the rest of the message after the player's name.
     * For example, supplying "was sucked into a jet engine" turns into
     * "[Player] was sucked into a jet engine".
     */
    public CustomStringDamageSource(String msg) {
        super("custom");
        message = msg;
    }

    @Override
    public final IChatComponent func_151519_b(EntityLivingBase e) {
        IChatComponent ch = new ChatComponentTranslation(
            e.getCommandSenderName() + " " + this.getMessage()
        );
        return ch;
    }

    protected String getMessage() {
        return message;
    }

    @Override
    public DamageSource setDamageBypassesArmor() {
        return super.setDamageBypassesArmor();
    }

    @Override
    public DamageSource setDamageAllowedInCreativeMode() {
        return super.setDamageAllowedInCreativeMode();
    }
}
