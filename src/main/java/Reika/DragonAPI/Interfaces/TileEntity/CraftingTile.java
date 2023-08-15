package Reika.DragonAPI.Interfaces.TileEntity;

import Reika.DragonAPI.Interfaces.CraftingContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public interface CraftingTile<V> extends IInventory {
    public CraftingContainer constructContainer();

    public boolean handleCrafting(V wr, EntityPlayer ep, boolean keyDown);

    public boolean isReadyToCraft();

    public int getOutputSlot();

    public void setToCraft(V recipe);

    public V getToCraft();
}
