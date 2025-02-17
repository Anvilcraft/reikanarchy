package Reika.ChromatiCraft.Auxiliary.Interfaces;

import java.util.Collection;

import Reika.ChromatiCraft.Auxiliary.CastingAutomationSystem;
import Reika.ChromatiCraft.Auxiliary.RecipeManagers.CastingRecipe;
import Reika.ChromatiCraft.TileEntity.Recipe.TileEntityCastingTable;
import Reika.DragonAPI.ASM.APIStripper.Strippable;
import Reika.DragonAPI.Interfaces.TileEntity.BreakAction;
import Reika.DragonAPI.Interfaces.TileEntity.GuiController;
import appeng.api.networking.security.IActionHost;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

@Strippable(value = { "appeng.api.networking.security.IActionHost" })
public interface CastingAutomationBlock<V extends CastingAutomationSystem>
    extends GuiController, OwnedTile, BreakAction, IActionHost {
    public Collection<CastingRecipe> getAvailableRecipes();

    public TileEntityCastingTable getTable();

    public int getInjectionTickRate();
    public boolean isAbleToRun(TileEntityCastingTable te);

    public boolean canTriggerCrafting();
    public boolean canPlaceCentralItemForMultiRecipes();
    public boolean canRecursivelyRequest(CastingRecipe c);

    public V getAutomationHandler();

    public void consumeEnergy(CastingRecipe c, TileEntityCastingTable te, ItemStack is);
    public boolean canCraft(World world, int x, int y, int z, TileEntityCastingTable te);

    public TileEntity getItemPool();
}
