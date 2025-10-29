package Reika.RotaryCraft.API.Event;

import Reika.DragonAPI.Instantiable.Event.TileEntityEvent;
import net.minecraft.tileentity.TileEntity;

public class VDGAttackEvent extends TileEntityEvent {
    public final int attackDamage;
    public final int tileCharge;

    public VDGAttackEvent(TileEntity te, int chg, int dmg) {
        super(te);

        attackDamage = dmg;
        tileCharge = chg;
    }
}
