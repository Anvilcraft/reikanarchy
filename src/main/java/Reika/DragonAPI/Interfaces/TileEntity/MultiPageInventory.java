package Reika.DragonAPI.Interfaces.TileEntity;

public interface MultiPageInventory extends ConditionBreakDropsInventory {
    public int getNumberPages();

    public int getSlotsOnPage(int page);

    public int getCurrentPage();
}
