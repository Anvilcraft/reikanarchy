/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.TileEntities.World;

import java.util.ArrayList;

import Reika.DragonAPI.Libraries.Registry.ReikaItemHelper;
import Reika.DragonAPI.Libraries.ReikaEntityHelper;
import Reika.DragonAPI.Libraries.ReikaInventoryHelper;
import Reika.RotaryCraft.API.Event.WeatherControlEvent;
import Reika.RotaryCraft.Auxiliary.Interfaces.ConditionalOperation;
import Reika.RotaryCraft.Auxiliary.ItemStacks;
import Reika.RotaryCraft.Base.TileEntity.InventoriedPowerReceiver;
import Reika.RotaryCraft.Registry.ConfigRegistry;
import Reika.RotaryCraft.Registry.MachineRegistry;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityWeatherController
    extends InventoriedPowerReceiver implements ConditionalOperation {
    private int cooldown = 0;

    private RainMode rainmode = RainMode.NONE;

    private static enum RainMode {
        NONE(),
        SUN(),
        RAIN(),
        THUNDER(),
        SUPERSTORM();

        public boolean isRain() {
            return this.ordinal() > SUN.ordinal();
        }

        public boolean isThunder() {
            return this.ordinal() > RAIN.ordinal();
        }

        public boolean hasAction() {
            return this != NONE;
        }
    }

    @Override
    public void updateEntity(World world, int x, int y, int z, int meta) {
        super.updateTileEntity();
        this.getPowerBelow();
        if (cooldown > 0)
            cooldown--;
        if (power < MINPOWER)
            return;
        if (!world.canBlockSeeTheSky(x, y + 1, z))
            return;

        WorldInfo wi = world.getWorldInfo();
        //ReikaJavaLibrary.pConsole(rainmode, Side.SERVER);
        if (rainmode == RainMode.SUPERSTORM) {
            wi.setRaining(true);
            wi.setThundering(true);
            if (rand.nextInt(20) == 0) {
                int xl = x - 64 + rand.nextInt(129);
                int zl = z - 64 + rand.nextInt(129);
                int yl = world.getTopSolidOrLiquidBlock(xl, zl);
                world.addWeatherEffect(new EntityLightningBolt(world, xl, yl, zl));
            }
        }

        if (cooldown > 0)
            return;

        rainmode = this.getRainMode();
        //ReikaJavaLibrary.pConsole(rainmode);
        if (rainmode.isRain() && ConfigRegistry.BANRAIN.getState())
            rainmode = RainMode.NONE;

        if (this.isAlready(world, rainmode))
            return;

        if (!rainmode.hasAction())
            return;

        boolean isThunder = world.isThundering();
        boolean rain = rainmode.isRain();
        boolean thunder = rainmode.isThunder();
        boolean storm = rainmode == RainMode.SUPERSTORM;
        wi.setRaining(rain);
        wi.setThundering(thunder);
        MinecraftForge.EVENT_BUS.post(new WeatherControlEvent(this, rain, thunder, storm)
        );
    }

    private boolean isAlready(World world, RainMode m) {
        boolean rain = m.isRain();
        boolean thunder = m.isThunder();
        boolean rain2 = world.isRaining();
        boolean thunder2 = world.isThundering();
        return rain == rain2 && thunder == thunder2;
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return false;
    }

    private void fire(ItemStack is, ItemStack is2) {
        worldObj.playSoundEffect(
            xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, "random.explode", 1F, 1F
        );
        if (is != null) {
            EntityItem ei = new EntityItem(
                worldObj,
                xCoord + 0.5,
                yCoord + 1.0625,
                zCoord + 0.5,
                new ItemStack(is.getItem(), 1, is.getItemDamage())
            );
            ReikaEntityHelper.addRandomDirVelocity(ei, 0.2);
            ei.delayBeforeCanPickup = 5000;
            ei.age = 5900;
            ei.motionY = 3;
            if (!worldObj.isRemote)
                worldObj.spawnEntityInWorld(ei);
        }
        if (is2 != null) {
            EntityItem ei = new EntityItem(
                worldObj,
                xCoord + 0.5,
                yCoord + 1.0625,
                zCoord + 0.5,
                ReikaItemHelper.getSizedItemStack(is2, 1)
            );
            ReikaEntityHelper.addRandomDirVelocity(ei, 0.2);
            ei.delayBeforeCanPickup = 5000;
            ei.age = 5900;
            ei.motionY = 3;
            if (!worldObj.isRemote)
                worldObj.spawnEntityInWorld(ei);
        }
    }

    private int hasSawdust() {
        int sawdust
            = ReikaInventoryHelper.locateInInventory(ItemStacks.sawdust, inv, false);
        if (sawdust >= 0)
            return sawdust;
        ArrayList<ItemStack> li = OreDictionary.getOres("dustWood");
        for (int i = 0; i < inv.length; i++) {
            ItemStack is = inv[i];
            if (is != null) {
                if (ReikaItemHelper.collectionContainsItemStack(li, is))
                    return i;
            }
        }
        return -1;
    }

    private RainMode getRainMode() {
        RainMode rainmode;
        ItemStack is = null;
        ItemStack is2 = null;
        int sawdust = this.hasSawdust();
        boolean silverio
            = ReikaInventoryHelper.checkForItemStack(ItemStacks.silveriodide, inv, false);
        boolean redstone = ReikaInventoryHelper.checkForItem(Items.redstone, inv);
        boolean glowdust = ReikaInventoryHelper.checkForItem(Items.glowstone_dust, inv);
        if (sawdust >= 0) {
            rainmode = RainMode.SUN;
            is = ItemStacks.sawdust;
        } else if (silverio) {
            rainmode = RainMode.RAIN;
            is = ItemStacks.silveriodide;
            if (redstone) {
                rainmode = RainMode.THUNDER;
                is2 = new ItemStack(Items.redstone, 1, 0);
            } else if (glowdust) {
                rainmode = RainMode.SUPERSTORM;
                is2 = new ItemStack(Items.glowstone_dust, 1, 0);
            }
        } else
            rainmode = RainMode.NONE;
        //ReikaJavaLibrary.pConsole(rainmode, Side.SERVER);
        if (this.isAlready(worldObj, rainmode))
            return this.rainmode;
        cooldown = 200 + rand.nextInt(400);
        if (rainmode.hasAction())
            this.fire(is, is2);
        int slot = -1;
        switch (rainmode) {
            case NONE:
                break;
            case SUN:
                slot = sawdust;
                ReikaInventoryHelper.decrStack(slot, inv);
                break;
            case RAIN:
                slot = ReikaInventoryHelper.locateInInventory(
                    ItemStacks.silveriodide, inv, false
                );
                ReikaInventoryHelper.decrStack(slot, inv);
                break;
            case THUNDER:
                slot = ReikaInventoryHelper.locateInInventory(
                    ItemStacks.silveriodide, inv, false
                );
                ReikaInventoryHelper.decrStack(slot, inv);
                slot = ReikaInventoryHelper.locateInInventory(Items.redstone, inv);
                ReikaInventoryHelper.decrStack(slot, inv);
                break;
            case SUPERSTORM:
                slot = ReikaInventoryHelper.locateInInventory(
                    ItemStacks.silveriodide, inv, false
                );
                ReikaInventoryHelper.decrStack(slot, inv);
                slot = ReikaInventoryHelper.locateInInventory(Items.glowstone_dust, inv);
                ReikaInventoryHelper.decrStack(slot, inv);
                break;
        }
        return rainmode;
    }

    private boolean isValidWeatherItem(ItemStack is) {
        if (ReikaItemHelper.matchStacks(is, ItemStacks.sawdust))
            return true;
        if (ReikaItemHelper.matchStacks(is, ItemStacks.silveriodide))
            return true;
        if (is.getItem() == Items.redstone)
            return true;
        if (is.getItem() == Items.glowstone_dust)
            return true;
        ArrayList<ItemStack> li = OreDictionary.getOres("dustWood");
        if (ReikaItemHelper.collectionContainsItemStack(li, is))
            return true;
        return false;
    }

    @Override
    public int getSizeInventory() {
        return 18;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    protected void animateWithTick(World world, int x, int y, int z) {}

    @Override
    public MachineRegistry getTile() {
        return MachineRegistry.WEATHERCONTROLLER;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack is) {
        return this.isValidWeatherItem(is);
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    public boolean areConditionsMet() {
        return this.getRainMode().hasAction();
    }

    @Override
    public String getOperationalStatus() {
        return cooldown <= 0 ? this.areConditionsMet() ? "Operational" : "Empty Inventory"
                             : "Idle";
    }
}
