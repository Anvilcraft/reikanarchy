package Reika.DragonAPI.ModInteract;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchItem;

public class CustomThaumResearch extends ResearchItem {
    private String name = "";
    private String desc = "";

    public CustomThaumResearch(
        String key,
        String category,
        AspectList tags,
        int col,
        int row,
        int complexity,
        ItemStack icon
    ) {
        super(key, category, tags, col, row, complexity, icon);
    }

    public CustomThaumResearch(
        String key,
        String category,
        AspectList tags,
        int col,
        int row,
        int complexity,
        ResourceLocation ico
    ) {
        super(key, category, tags, col, row, complexity, ico);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getText() {
        return desc;
    }

    public CustomThaumResearch setName(String str) {
        name = str;
        return this;
    }

    public CustomThaumResearch setDescription(String str) {
        desc = str;
        return this;
    }
}
