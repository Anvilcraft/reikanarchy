package Reika.ChromatiCraft.API;

import java.util.HashSet;

import Reika.ChromatiCraft.API.CrystalElementAccessor.CrystalElementProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ProgressionAPI {
    public static final ProgressionAPI instance = new ProgressionAPI();

    public ResearchRegistry researchManager;
    public ProgressRegistry progressManager;

    private ProgressionAPI() {}

    public static interface ResearchRegistry extends ProgressManager {
        public boolean lexiconHasFragment(ItemStack book, String key);

        public String getResearchLevelForPlayer(EntityPlayer ep);
    }

    public static interface ProgressRegistry extends ProgressManager {
        public boolean canPlayerStepTo(EntityPlayer ep, String key);

        public boolean playerDiscoveredElement(EntityPlayer ep, CrystalElementProxy e);
    }

    public static interface ProgressManager {
        public boolean playerHasResearch(EntityPlayer ep, String key);

        public HashSet<String> getPrerequisites(String key);

        public HashSet<String> getAllResearches();
    }
}
