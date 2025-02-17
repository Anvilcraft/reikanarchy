/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.GUIs.Machine.Inventory;

import Reika.DragonAPI.Libraries.Rendering.ReikaColorAPI;
import Reika.DragonAPI.Libraries.Rendering.ReikaLiquidRenderer;
import Reika.RotaryCraft.Base.GuiPowerOnlyMachine;
import Reika.RotaryCraft.Containers.Machine.Inventory.ContainerFillingStation;
import Reika.RotaryCraft.TileEntities.Auxiliary.TileEntityFillingStation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;

public class GuiFillingStation extends GuiPowerOnlyMachine {
    private TileEntityFillingStation fillingStation;

    //private World worldObj = ModLoader.getMinecraftInstance().theWorld;

    public GuiFillingStation(EntityPlayer p5ep, TileEntityFillingStation te) {
        super(new ContainerFillingStation(p5ep, te), te);
        fillingStation = te;
        xSize = 176;
        ySize = 187;
        ep = p5ep;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int a, int b) {
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;

        super.drawGuiContainerForegroundLayer(a, b);

        if (api.isMouseInBox(j + 81, j + 94, k + 20, k + 87)) {
            int mx = api.getMouseRealX();
            int my = api.getMouseRealY();
            api.drawTooltipAt(
                fontRendererObj,
                String.format(
                    "%d/%d mB", fillingStation.getLevel(), fillingStation.CAPACITY
                ),
                mx - j,
                my - k
            );
        }

        if (!fillingStation.isEmpty()) {
            int i2 = fillingStation.getLiquidScaled(66);
            int x = 82;
            int y = 87 - i2;
            IIcon ico = ReikaLiquidRenderer.getFluidIconSafe(fillingStation.getFluid());
            ReikaLiquidRenderer.bindFluidTexture(fillingStation.getFluid());
            int clr = 0xffffffff;
            if (fillingStation.getFluid().canBePlacedInWorld()) {
                clr = fillingStation.getFluid().getBlock().colorMultiplier(
                    fillingStation.worldObj,
                    fillingStation.xCoord * 2,
                    fillingStation.yCoord * 2,
                    fillingStation.zCoord * 2
                );
            }
            GL11.glColor4f(
                ReikaColorAPI.HextoColorMultiplier(clr, 0),
                ReikaColorAPI.HextoColorMultiplier(clr, 1),
                ReikaColorAPI.HextoColorMultiplier(clr, 2),
                1
            );
            this.drawTexturedModelRectFromIcon(x, y, ico, 12, i2);
        }
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
    }

    @Override
    protected String getGuiTexture() {
        return "fillingstationgui";
    }
}
