/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.GUIs.Machine;

import Reika.RotaryCraft.Auxiliary.RotaryAux;
import Reika.RotaryCraft.Base.GuiNonPoweredMachine;
import Reika.RotaryCraft.Containers.Machine.ContainerGearbox;
import Reika.RotaryCraft.TileEntities.Transmission.TileEntityGearbox;
import net.minecraft.entity.player.EntityPlayer;

public class GuiGearbox extends GuiNonPoweredMachine {
    private TileEntityGearbox gbx;

    public GuiGearbox(EntityPlayer p5ep, TileEntityGearbox Gearbox) {
        super(new ContainerGearbox(p5ep, Gearbox), Gearbox);
        gbx = Gearbox;
        ep = p5ep;
        ySize = 84;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int a, int b) {
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;

        super.drawGuiContainerForegroundLayer(a, b);

        String s = gbx.isLiving() ? "Mana" : "Lubricant";
        fontRendererObj.drawString(s, 5, 12, 4210752);

        fontRendererObj.drawString("Damage:", 68, 60, 0x000000);
        int damage = gbx.getDamagePercent();
        if (damage < 10)
            fontRendererObj.drawString(
                String.format("%5d%s", damage, "%"), 122, 60, 0x00ff00
            );
        if (damage < 25 && damage >= 10)
            fontRendererObj.drawString(
                String.format("%5d%s", damage, "%"), 122, 60, 0x55ff00
            );
        if (damage < 50 && damage >= 25)
            fontRendererObj.drawString(
                String.format("%5d%s", damage, "%"), 122, 60, 0xffff00
            );
        if (damage < 80 && damage >= 50)
            fontRendererObj.drawString(
                String.format("%5d%s", damage, "%"), 122, 60, 0xff5500
            );
        if (damage >= 80)
            fontRendererObj.drawString(
                String.format("%5d%s", damage, "%"), 122, 60, 0xff0000
            );

        fontRendererObj.drawString("Ratio:", 80, 24, 0x000000);
        fontRendererObj.drawString("Mode:", 80, 36, 0x000000);
        fontRendererObj.drawString("Power:", 74, 48, 0x000000);

        fontRendererObj.drawString(
            String.format("%5d ", gbx.getRatio()), 127, 24, 0x000000
        );
        if (gbx.reduction)
            fontRendererObj.drawString("Torque", 115, 36, 0x000000);
        else
            fontRendererObj.drawString(" Speed", 115, 36, 0x000000);

        String pw = RotaryAux.formatPower(gbx.power);
        fontRendererObj.drawString(
            pw, 150 - fontRendererObj.getStringWidth(pw), 48, 0x000000
        );

        if (!gbx.isLiving() && api.isMouseInBox(j + 23, j + 32, k + 20, k + 76)) {
            int mx = api.getMouseRealX();
            int my = api.getMouseRealY();
            api.drawTooltipAt(
                fontRendererObj,
                String.format(
                    "%.1f/%d", gbx.getLubricant() / 1000F, gbx.getMaxLubricant() / 1000
                ),
                mx - j,
                my - k
            );
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);

        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;

        int i2 = gbx.getLubricantScaled(55);
        int i3 = 0;
        if (i2 != 0)
            i3 = 1;
        int u = gbx.isLiving() ? 186 : 176;
        this.drawTexturedModalRect(j + 24, ySize / 2 + k + 34 - i2, u, 126 - i2, 8, i2);
    }

    @Override
    protected String getGuiTexture() {
        return "gearboxgui";
    }
}
