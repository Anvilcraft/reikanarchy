/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.GUI.Tile;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import Reika.ChromatiCraft.Base.GuiChromaBase;
import Reika.ChromatiCraft.ChromatiCraft;
import Reika.ChromatiCraft.Registry.ChromaIcons;
import Reika.ChromatiCraft.Registry.ChromaPackets;
import Reika.ChromatiCraft.Registry.ChromaSounds;
import Reika.ChromatiCraft.TileEntity.Transport.TileEntityTeleportGate;
import Reika.ChromatiCraft.TileEntity.Transport.TileEntityTeleportGate.GateData;
import Reika.ChromatiCraft.TileEntity.Transport.TileEntityTeleportGate.Statuses;
import Reika.DragonAPI.Base.CoreContainer;
import Reika.DragonAPI.Instantiable.Data.Immutable.WorldLocation;
import Reika.DragonAPI.Instantiable.Data.Maps.RegionMap;
import Reika.DragonAPI.Instantiable.Data.ObjectWeb;
import Reika.DragonAPI.Instantiable.IO.PacketTarget;
import Reika.DragonAPI.Libraries.IO.ReikaPacketHelper;
import Reika.DragonAPI.Libraries.IO.ReikaSoundHelper;
import Reika.DragonAPI.Libraries.IO.ReikaTextureHelper;
import Reika.DragonAPI.Libraries.Java.ReikaGLHelper.BlendMode;
import Reika.DragonAPI.Libraries.MathSci.ReikaVectorHelper;
import Reika.DragonAPI.Libraries.Rendering.ReikaColorAPI;
import com.google.common.base.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;

public class GuiTeleportGate extends GuiChromaBase {
    private final TileEntityTeleportGate gate;
    private final WorldLocation gateLoc;

    private final ConnectionWeb web = new ConnectionWeb();
    private final RegionMap<LinkNode> pointLocs = new RegionMap();

    private static final int SIZE = 16;

    private double offsetX = 0;
    private double offsetZ = 0;
    private double scaleFactor = 1;

    private Point zoomBoxStart;

    public GuiTeleportGate(EntityPlayer ep, TileEntityTeleportGate te) {
        super(new CoreContainer(ep, te), ep, te);
        gate = te;
        gateLoc = new WorldLocation(te);
        xSize = 240;
        ySize = 200;
    }

    @Override
    public void initGui() {
        super.initGui();

        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;

        web.clear();
        for (GateData dat : TileEntityTeleportGate.getCache()) {
            LinkNode l = new LinkNode(dat);
            l.statusFlags |= dat.statusFlags;
            web.addNode(l);
        }
        web.scaleTo(xSize - 20, ySize - 20, SIZE);
    }

    @Override
    protected void actionPerformed(GuiButton b) {
        super.actionPerformed(b);
        this.initGui();
    }

    @Override
    protected void mouseMovedOrUp(int mx, int my, int b) {
        super.mouseMovedOrUp(mx, my, b);

        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;

        if (b == 0 && zoomBoxStart != null) {
            int x1 = zoomBoxStart.x;
            int y1 = zoomBoxStart.y;
            int x2 = mx;
            int y2 = my;
            if (x1 > x2) {
                int a = x2;
                x2 = x1;
                x1 = a;
            }
            if (y1 > y2) {
                int a = y2;
                y2 = y1;
                y1 = a;
            }
            Rectangle box = new Rectangle(x1 - j, y1 - k, x2 - x1, y2 - y1);
            Collection<Entry<Rectangle, LinkNode>> c = new ArrayList();
            Map<Rectangle, LinkNode> map = pointLocs.view();
            for (Entry<Rectangle, LinkNode> e : map.entrySet()) {
                Rectangle r = e.getKey();
                //ReikaJavaLibrary.pConsole(zoomBoxStart+", "+r);
                if (r.intersects(box)) {
                    c.add(e);
                }
            }
            if (!c.isEmpty()) {
                //ReikaJavaLibrary.pConsole(c);
                double xc = 0;
                double zc = 0;
                double minX = Double.POSITIVE_INFINITY;
                double minZ = Double.POSITIVE_INFINITY;
                double maxX = Double.NEGATIVE_INFINITY;
                double maxZ = Double.NEGATIVE_INFINITY;
                for (Entry<Rectangle, LinkNode> e : c) {
                    Rectangle r = e.getKey();
                    xc += r.getX();
                    zc += r.getY();
                    minX = Math.min(minX, r.getX());
                    minZ = Math.min(minZ, r.getY());
                    maxX = Math.max(maxX, r.getX());
                    maxZ = Math.max(maxZ, r.getY());
                }
                xc /= c.size();
                zc /= c.size();
                offsetX -= xc * scaleFactor;
                offsetZ -= zc * scaleFactor;
                offsetX += xSize / 2 - SIZE / 2D;
                offsetZ += ySize / 2 - SIZE / 2D;
                if (c.size() > 1) {
                    /*
                    double wx = web.maxX-web.minX;
                    double wz = web.maxZ-web.minZ;
                    double sx = xSize/wx;
                    double sz = ySize/wz;
                    double fx = (maxX-minX)/wx;
                    double fz = (maxZ-minZ)/wz;
                     */
                    double fx = (maxX - minX) / xSize;
                    double fz = (maxZ - minZ) / ySize;
                    //ReikaJavaLibrary.pConsole(fx+" , "+fz);
                    double sc = Math.min(1D / fx, 1D / fz);
                    scaleFactor = sc * 0.5;
                }
            }
        }
        zoomBoxStart = null;
    }

    @Override
    protected void mouseClicked(int x, int y, int b) {
        super.mouseClicked(x, y, b);

        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;

        LinkNode l = pointLocs.getRegion(-j + x, -k + y);
        if (l != null) {
            if (!l.location.equals(
                    gate.worldObj, gate.xCoord, gate.yCoord, gate.zCoord
                )) {
                ReikaPacketHelper.sendDataPacket(
                    ChromatiCraft.packetChannel,
                    ChromaPackets.TRIGGERTELEPORT.ordinal(),
                    PacketTarget.server,
                    gate.worldObj.provider.dimensionId,
                    gate.xCoord,
                    gate.yCoord,
                    gate.zCoord,
                    l.location.dimensionID,
                    l.location.xCoord,
                    l.location.yCoord,
                    l.location.zCoord
                );
            } else
                ReikaSoundHelper.playClientSound(ChromaSounds.ERROR, player, 1, 1);
        }
		else if (x >= j && y >= k && x <= j+fontRendererObj.getStringWidth("Reload Preview")+3 && y <= k+fontRendererObj.FONT_HEIGHT+3) {
            ReikaSoundHelper.playClientSound(ChromaSounds.GUICLICK, player, 1, 1);
            player.closeScreen();
            gate.takeSnapshot();
        } else if (scaleFactor == 1) {
            zoomBoxStart = new Point(x, y);
        }
    }

    public void handleTriggerConfirm(boolean success) {
        ReikaSoundHelper.playClientSound(
            success ? ChromaSounds.USE : ChromaSounds.ERROR, player, 1, 1
        );
        if (success) {
            player.closeScreen();
            player.rotationPitch = 0;
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        super.drawGuiContainerForegroundLayer(par1, par2);
        //fontRendererObj = Minecraft.getMinecraft().fontRenderer;
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;

        pointLocs.clear();
        web.render(j, k, SIZE);

        String s = "Reload Preview";
        int c = ReikaColorAPI.mixColors(
            0xffffffff,
            0x80808080,
            Math.min(
                1, 0.625F + 0.5F * (float) Math.sin(System.currentTimeMillis() / 400D)
            )
        );
        api.drawRectFrame(
            0,
            0,
            fontRendererObj.getStringWidth(s) + 3,
            fontRendererObj.FONT_HEIGHT + 3,
            c
        );
        fontRendererObj.drawString(s, 2, 2, 0xffffff);

        if (zoomBoxStart != null) {
            api.drawRectFrame(
                zoomBoxStart.x - j,
                zoomBoxStart.y - k,
                par1 - zoomBoxStart.x,
                par2 - zoomBoxStart.y,
                0x8f8f8f
            );
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p, int a, int b) {
        //super.drawGuiContainerBackgroundLayer(p, a, b);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;

        api.drawRectFrame(j, k, xSize, ySize, 0xffffffff);
    }

    @Override
    public void drawScreen(int mx, int my, float ptick) {
        super.drawScreen(mx, my, ptick);

        double d = GuiScreen.isCtrlKeyDown() ? -5 : Math.max(-40, -6 * scaleFactor);
        if (Keyboard.isKeyDown(Keyboard.KEY_PRIOR)) {
            scaleFactor *= 0.95;
            offsetX *= 0.95;
            offsetZ *= 0.95;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_NEXT)) {
            scaleFactor *= 1.05;
            offsetX *= 1.05;
            offsetZ *= 1.05;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            offsetZ -= d;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            offsetX -= d;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            offsetZ += d;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            offsetX += d;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_HOME)) {
            scaleFactor = 1;
            offsetX = 0;
            offsetZ = 0;
        }
    }

    @Override
    protected boolean drawTitle() {
        return false;
    }

    @Override
    public String getGuiTexture() {
        return "teleport";
    }

    private class ConnectionWeb {
        private final ObjectWeb<LinkNode> web = new ObjectWeb();

        private double minX = Integer.MAX_VALUE;
        private double minZ = Integer.MAX_VALUE;
        private double maxX = Integer.MIN_VALUE;
        private double maxZ = Integer.MIN_VALUE;

        private void addNode(LinkNode l) {
            web.addNode(l);
            if (Statuses.OWNED.check(l.statusFlags)
                && Statuses.STRUCTURE.check(l.statusFlags)) {
                for (LinkNode l2 : web.objects()) {
                    if (Statuses.POWERED.check(l.statusFlags)
                        || Statuses.POWERED.check(l2.statusFlags)) {
                        if (Statuses.OWNED.check(l2.statusFlags)
                            && Statuses.STRUCTURE.check(l2.statusFlags)) {
                            web.addBilateralConnection(l, l2);
                        }
                    }
                }
            }
            l.renderX = l.getScaledX();
            l.renderZ = l.getScaledZ();
            minX = Math.min(minX, l.getScaledX());
            minZ = Math.min(minZ, l.getScaledZ());
            maxX = Math.max(maxX, l.getScaledX());
            maxZ = Math.max(maxZ, l.getScaledZ());
        }

        private void clear() {
            web.clear();
            minX = Integer.MAX_VALUE;
            minZ = Integer.MAX_VALUE;
            maxX = Integer.MIN_VALUE;
            maxZ = Integer.MIN_VALUE;
        }

        @Override
        public String toString() {
            return web.toString();
        }

        private void scaleTo(int sizeX, int sizeY, int elementSize) {
            sizeX -= elementSize;
            sizeY -= elementSize;
            for (LinkNode l : web.objects()) {
                l.renderX -= minX;
                l.renderZ -= minZ;
            }
            maxX -= minX;
            maxZ -= minZ;
            double sx = sizeX / maxX;
            double sz = sizeY / maxZ;
            double sm = Math.min(sx, sz);
            for (LinkNode l : web.objects()) {
                l.renderX *= sm;
                l.renderZ *= sm;
            }
            maxX *= sm;
            maxZ *= sm;
            double d = elementSize / 2D;
            for (LinkNode l : web.objects()) {
                l.renderX -= d;
                l.renderZ -= d;
            }
            minX -= d;
            minZ -= d;
            maxX -= d;
            maxZ -= d;
        }

        private void render(int j, int k, int s) {
            GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
            GL11.glEnable(GL11.GL_BLEND);
            int dx = (int) (-maxX / 2D * scaleFactor) + xSize / 2 + (int) offsetX;
            int dz = (int) (-maxZ / 2D * scaleFactor) + ySize / 2 + (int) offsetZ;
            BlendMode.ADDITIVEDARK.apply();
            for (LinkNode l : web.objects()) {
                int lx = (int) Math.round(l.renderX * scaleFactor) + dx;
                int lz = (int) Math.round(l.renderZ * scaleFactor) + dz;
                //if (lx >= 0 && lx <= xSize && lz >= 0 && lz < ySize) {
                boolean mbox = api.isMouseInBox(
                    j + lx + 3, j + lx + s - 4, k + lz + 3, k + lz + s - 4
                );
                for (LinkNode l2 : web.getChildren(l)) {
                    int lx2 = (int) Math.round(l2.renderX * scaleFactor) + dx;
                    int lz2 = (int) Math.round(l2.renderZ * scaleFactor) + dz;
                    ImmutablePair<java.awt.Point, java.awt.Point> ps
                        = ReikaVectorHelper.clipLine(
                            lx + s / 2,
                            lx2 + s / 2,
                            lz + s / 2,
                            lz2 + s / 2,
                            0,
                            0,
                            xSize,
                            ySize
                        );
                    if (ps != null) {
                        int lc = mbox ? 0xffffffff : 0x04040404;
                        api.drawLine(ps.left.x, ps.left.y, ps.right.x, ps.right.y, lc);
                    }
                }
                //}
            }

            BlendMode.DEFAULT.apply();

            ReikaTextureHelper.bindTerrainTexture();
            for (LinkNode l : web.objects()) {
                int c = l.getRenderColor();
                int lx = (int) Math.round(l.renderX * scaleFactor) + dx;
                int lz = (int) Math.round(l.renderZ * scaleFactor) + dz;
                if (lx >= 0 && lx <= xSize - s && lz >= 0 && lz < ySize - s) {
                    boolean mbox = api.isMouseInBox(
                        j + lx + 3, j + lx + s - 4, k + lz + 3, k + lz + s - 4
                    );
                    GL11.glColor3f(
                        ReikaColorAPI.getRed(c) / 255F,
                        ReikaColorAPI.getGreen(c) / 255F,
                        ReikaColorAPI.getBlue(c) / 255F
                    );
                    api.drawTexturedModelRectFromIcon(
                        lx, lz, ChromaIcons.DIAMOND.getIcon(), s, s
                    );
                    if (l.location.equals(gateLoc)) {
                        int clr = ReikaColorAPI.mixColors(
                            0xffffff,
                            0xa0a0a0,
                            (float
                            ) (0.5F + 0.5F * Math.sin(System.currentTimeMillis() / 200D))
                        );
                        api.drawRectFrame(lx + 1, lz + 1, 14, 14, clr);
                    }
                    pointLocs.addRegionByWH(lx, lz, s, s, l);
                    if (mbox) {
                        BufferedImage img = gate.getPreview(l.location);
                        if (img == null) {
                            ReikaTextureHelper.bindFinalTexture(
                                ChromatiCraft.class, "Textures/GateNotFound.png"
                            );
                        } else {
                            ReikaTextureHelper.bindRawTexture(img, l.getTextureID());
                        }
                        double w = img == null ? 854 / 8D : img.getWidth() / 8D;
                        double h = img == null ? 480 / 8D : img.getHeight() / 8D;
                        GL11.glColor3f(1, 1, 1);
                        double x = api.getMouseRealX() - w / 1.25;
                        double y = api.getMouseRealY() - h / 4D;
                        x = Math.min(x, xSize - w);
                        y = Math.min(y, ySize - h);
                        Tessellator.instance.startDrawingQuads();
                        Tessellator.instance.setColorOpaque_I(0xffffff);
                        Tessellator.instance.addVertexWithUV(x, y + h, 0, 0, 1);
                        Tessellator.instance.addVertexWithUV(x + w, y + h, 0, 1, 1);
                        Tessellator.instance.addVertexWithUV(x + w, y, 0, 1, 0);
                        Tessellator.instance.addVertexWithUV(x, y, 0, 0, 0);
                        Tessellator.instance.draw();
                        ReikaTextureHelper.bindTerrainTexture();
                        String sg = l.getName();
                        if (Strings.isNullOrEmpty(sg))
                            sg = l.location.toString();
                        api.drawTooltipAt(
                            fontRendererObj,
                            sg,
                            (int) x + fontRendererObj.getStringWidth(sg) + 19,
                            (int) y - (fontRendererObj.FONT_HEIGHT - 8)
                        );
                    }
                }
            }
            GL11.glPopAttrib();
        }
    }

    private class LinkNode implements Comparable<LinkNode> {
        private int statusFlags;
        private final WorldLocation location;
        private final String name;

        private double renderX;
        private double renderZ;

        private LinkNode(GateData dat) {
            location = dat.location;
            name = dat.getName();
            if (location.dimensionID
                != Minecraft.getMinecraft().theWorld.provider.dimensionId)
                this.setFlag(Statuses.DIMENSION);
            if (dat.isOwnedBy(player))
                this.setFlag(Statuses.OWNED);
        }

        public String getName() {
            return name;
        }

        private String getTextureID() {
            return TileEntityTeleportGate.getTextureID(location);
        }

        private LinkNode setFlag(Statuses s) {
            statusFlags |= s.flag;
            return this;
        }

        public int getRenderColor() {
            for (int i = 0; i < Statuses.list.length; i++) {
                Statuses s = Statuses.list[i];
                if (!s.check(statusFlags))
                    return s.color;
            }
            return 0xffffff;
        }

        @Override
        public String toString() {
            return location.toString() + " [" + renderX + "/" + renderZ + "/"
                + Integer.toBinaryString(statusFlags) + "]";
        }

        @Override
        public int compareTo(LinkNode o) {
            return Integer.compare(location.hashCode(), o.location.hashCode());
        }

        public double getScaledX() {
            return this.scale(location.xCoord);
        }

        public double getScaledZ() {
            return this.scale(location.zCoord);
        }

        private double scale(double val) {
            return Math.signum(val) * Math.pow(Math.abs(val), 0.125);
        }
    }
}