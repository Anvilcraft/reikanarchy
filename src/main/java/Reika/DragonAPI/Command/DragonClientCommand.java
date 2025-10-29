package Reika.DragonAPI.Command;

//@SideOnly(Side.CLIENT)
public abstract class DragonClientCommand extends DragonCommandBase {
    @Override
    protected final boolean isAdminOnly() {
        return false;
    }
}
