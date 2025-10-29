package Reika.DragonAPI.Interfaces.Item;

import Reika.DragonAPI.Instantiable.MusicScore;
import net.minecraft.item.ItemStack;

public interface MusicDataItem {
    public MusicScore getMusic(ItemStack is);
    //public int[][][] getNoteblockMusic(ItemStack is);
}
