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

import Reika.DragonAPI.Base.CoreContainer;
import Reika.DragonAPI.Libraries.IO.ReikaPacketHelper;
import Reika.DragonAPI.Libraries.Java.ReikaJavaLibrary;
import Reika.RotaryCraft.Base.GuiNonPoweredMachine;
import Reika.RotaryCraft.Registry.PacketRegistry;
import Reika.RotaryCraft.RotaryCraft;
import Reika.RotaryCraft.TileEntities.TileEntityPlayerDetector;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;

public class GuiPlayerDetector extends GuiNonPoweredMachine {
    private TileEntityPlayerDetector playerdetector;
    public int range;
    //private World worldObj = ModLoader.getMinecraftInstance().theWorld;

    int x;
    int y;
    private GuiTextField input;

    public GuiPlayerDetector(EntityPlayer p5ep, TileEntityPlayerDetector PlayerDetector) {
        super(new CoreContainer(p5ep, PlayerDetector), PlayerDetector);
        playerdetector = PlayerDetector;
        ySize = 46;
        ep = p5ep;
        range = playerdetector.selectedrange;
        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d",
        //this.range));
    }

    @Override
    public void initGui() {
        super.initGui();
        int j = (width - xSize) / 2 + 8;
        int k = (height - ySize) / 2 - 12;
        input = new GuiTextField(fontRendererObj, j + xSize / 2 - 6, k + 33, 26, 16);
        input.setFocused(false);
        input.setMaxStringLength(3);
    }

    @Override
    protected void keyTyped(char c, int i) {
        super.keyTyped(c, i);
        input.textboxKeyTyped(c, i);
    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        input.mouseClicked(i, j, k);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        x = Mouse.getX();
        y = Mouse.getY();
        if (input.getText().isEmpty()) {
            return;
        }
        if (!(input.getText().matches("^[0-9 ]+$"))) {
            range = 0;
            input.deleteFromCursor(-1);
            ReikaPacketHelper.sendPacketToServer(
                RotaryCraft.packetChannel,
                PacketRegistry.DETECTOR.ordinal(),
                playerdetector,
                range
            );
            return;
        }
        range = ReikaJavaLibrary.safeIntParse(input.getText());
        if (range >= 0)
            ReikaPacketHelper.sendPacketToServer(
                RotaryCraft.packetChannel,
                PacketRegistry.DETECTOR.ordinal(),
                playerdetector,
                range
            );
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int a, int b) {
        super.drawGuiContainerForegroundLayer(a, b);

        fontRendererObj.drawString("Detection Range:", xSize / 2 - 82, 25, 4210752);
        if (!input.isFocused()) {
            fontRendererObj.drawString(
                String.format("%d", playerdetector.selectedrange),
                xSize / 2 + 6,
                25,
                0xffffffff
            );
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);

        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        input.drawTextBox();
        int color = 4210752;
        if (range > playerdetector.getMaxRange())
            color = 0xff0000;
        api.drawCenteredStringNoShadow(
            fontRendererObj,
            String.format("(%d)", playerdetector.getRange()),
            j + xSize / 2 + 58,
            k + 25,
            color
        );
    }

    @Override
    protected String getGuiTexture() {
        return "rangegui";
    }
}
