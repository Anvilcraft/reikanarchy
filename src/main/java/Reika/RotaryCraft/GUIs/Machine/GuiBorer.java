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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import Reika.DragonAPI.Base.CoreContainer;
import Reika.DragonAPI.Instantiable.GUI.ImagedGuiButton;
import Reika.DragonAPI.Libraries.IO.ReikaPacketHelper;
import Reika.DragonAPI.Libraries.IO.ReikaTextureHelper;
import Reika.RotaryCraft.Base.GuiMachine;
import Reika.RotaryCraft.Registry.PacketRegistry;
import Reika.RotaryCraft.RotaryCraft;
import Reika.RotaryCraft.TileEntities.Production.TileEntityBorer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class GuiBorer extends GuiMachine {
    public String dropstatus;
    public boolean drops;

    private TileEntityBorer borer;

    private boolean[][] dig = new boolean[7][5];
    private int packetID;

    public GuiBorer(EntityPlayer p5ep, TileEntityBorer borer) {
        super(new CoreContainer(p5ep, borer), borer);
        this.borer = borer;
        ySize = 169;
        xSize = 176;
        dropstatus = "Drops On";
        ep = p5ep;
        drops = borer.drops;
        for (int i = 0; i < 7; i++)
            for (int l = 0; l < 5; l++)
                dig[i][l] = borer.cutShape[i][l];
    }

    @Override
    public void initGui() {
        super.initGui();
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;

        String file = "/Reika/RotaryCraft/Textures/GUI/buttons.png";
        for (int i = 0; i < 7; i++)
            for (int l = 0; l < 5; l++) {
                int u = 0;
                if (i == 3 && l == 4)
                    u = 36;
                if (dig[i][l])
                    buttonList.add(new ImagedGuiButton(
                        50 + i + 7 * l,
                        j + 25 + 18 * i,
                        k + 16 + 18 * l,
                        18,
                        18,
                        u,
                        0,
                        file,
                        RotaryCraft.class
                    ));
                else
                    buttonList.add(new ImagedGuiButton(
                        10 + i + 7 * l,
                        j + 25 + 18 * i,
                        k + 16 + 18 * l,
                        18,
                        18,
                        u + 18,
                        0,
                        file,
                        RotaryCraft.class
                    ));
            }

        buttonList.add(new GuiButton(8, j + 14, -1 + k + 116, 72, 20, "Reset Pos'n"));
        buttonList.add(new GuiButton(6, j + 14, k + 140, 148, 20, "Toggle All"));

        if (drops)
            buttonList.add(new GuiButton(7, j + 90, -1 + k + 116, 72, 20, "Drops On"));
        else
            buttonList.add(new GuiButton(7, j + 90, -1 + k + 116, 72, 20, "Drops Off"));
    }

    public void toggleDrops() {
        if (drops) {
            dropstatus = "Drops Off";
            drops = false;
        } else {
            dropstatus = "Drops On";
            drops = true;
        }

        this.sendPacket(PacketRegistry.BORERDROPS.ordinal());
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        super.actionPerformed(button);
        if (button.id == 7) {
            this.toggleDrops();
        }
        if (button.id == 8)
            this.sendPacket(PacketRegistry.BORERRESET.ordinal());
        if (button.id < 7) { //toggle
            this.sendPacket(PacketRegistry.BORERTOGGLEALL.ordinal());
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 7; j++) {
                    dig[j][i] = !dig[j][i];
                }
            }
        }
        if (button.id >= 10 && button.id < 50) {
            int rows = (button.id - 10) / 7;
            int cols = (button.id - 10) - rows * 7;
            //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d
            //-> row %d col %d", button.id, rows, cols));
            dig[cols][rows] = true;
            packetID = (button.id - 10);
            if (button.id == 10)
                packetID = 100;
            this.sendPacket(PacketRegistry.BORER.ordinal());
        }
        if (button.id >= 50 && button.id < 24000) {
            int rows = (button.id - 50) / 7;
            int cols = (button.id - 50) - rows * 7;
            //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d
            //-> row %d col %d", button.id, rows, cols));
            dig[cols][rows] = false;
            packetID = (button.id - 50);
            if (button.id == 50)
                packetID = 100;
            this.sendPacket(PacketRegistry.BORER.ordinal());
        }
        this.initGui();
    }

    public void sendPacket(int a) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(20); // 5 ints
        DataOutputStream outputStream = new DataOutputStream(bos);
        try {
            //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.valueOf(drops));
            outputStream.writeInt(a);
            if (a == PacketRegistry.BORERDROPS.ordinal()) { /*
                 if (drops)
                     outputStream.writeInt(1); //set drops to 0 (false)
                 else
                     outputStream.writeInt(0);*/
                //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.valueOf(drops));
            }
            if (a == PacketRegistry.BORERTOGGLEALL.ordinal())
                ; //outputStream.writeInt(-1);
            if (a > PacketRegistry.BORERTOGGLEALL.ordinal())
                ; //outputStream.writeInt(-1);
            if (a == PacketRegistry.BORER.ordinal()) {
                //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.valueOf(3434));
                int rows = packetID / 7;
                int cols = packetID - rows * 7;
                if (packetID == 100) {
                    rows = cols = 0;
                }
                //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d
                //-> row %d col %d", this.packetID, rows, cols));
                if (dig[cols][rows])
                    outputStream.writeInt(-1 * packetID);
                else
                    outputStream.writeInt(packetID);
            }
            outputStream.writeInt(borer.xCoord);
            outputStream.writeInt(borer.yCoord);
            outputStream.writeInt(borer.zCoord);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ReikaPacketHelper.sendDataPacket(RotaryCraft.packetChannel, bos);
    }

    /*
    @Override
    public void updateScreen() {
        super.updateScreen();
        this.initGui();
    }
     */
    @Override
    protected void drawPowerTab(int var5, int var6) {
        String var4 = "/Reika/RotaryCraft/Textures/GUI/powertab.png";
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        ReikaTextureHelper.bindTexture(RotaryCraft.class, var4);
        this.drawTexturedModalRect(xSize + var5, var6 + 5, 0, 4, 42, 159);

        long frac = ((borer.power * 29L) / borer.MINPOWER);
        if (frac > 29)
            frac = 29;
        this.drawTexturedModalRect(
            xSize + var5 + 5, ySize + var6 - 146, 0, 0, (int) frac, 4
        );

        frac = borer.omega * 29L / borer.MINSPEED;
        if (frac > 29)
            frac = 29;
        this.drawTexturedModalRect(
            xSize + var5 + 5, ySize + var6 - 86, 0, 0, (int) frac, 4
        );

        frac = borer.torque * 29L / borer.MINTORQUE;
        if (frac > 29)
            frac = 29;
        this.drawTexturedModalRect(
            xSize + var5 + 5, ySize + var6 - 26, 0, 0, (int) frac, 4
        );

        api.drawCenteredStringNoShadow(
            fontRendererObj, "Power:", xSize + var5 + 20, var6 + 12, 0xff000000
        );
        api.drawCenteredStringNoShadow(
            fontRendererObj, "Speed:", xSize + var5 + 20, var6 + 71, 0xff000000
        );
        api.drawCenteredStringNoShadow(
            fontRendererObj, "Torque:", xSize + var5 + 20, var6 + 130, 0xff000000
        );
        //this.drawCenteredStringNoShadow(fontRendererObj, String.format("%d/%d",
        //borer.power, borer.MINPOWER), xSize+var5+16, var6+16, 0xff000000);
    }

    @Override
    protected String getGuiTexture() {
        return "borergui2";
    }
}
