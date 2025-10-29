package Reika.DragonAPI.Instantiable.GUI;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class CustomSoundGuiButton extends GuiButton {
    public final CustomSoundGui gui;
    private boolean lastHover;
    private int ticks = 0;

    public CustomSoundGuiButton(
        int id, int x, int y, int w, int h, String s, CustomSoundGui gui
    ) {
        super(id, x, y, w, h, s);
        this.gui = gui;
    }

    public CustomSoundGuiButton disable() {
        enabled = false;
        return this;
    }

    @Override
    public void drawButton(Minecraft mc, int x, int y) {
        super.drawButton(mc, x, y);
        if (visible) {
            if (field_146123_n && !lastHover && ticks > 1) {
                gui.playHoverSound(this);
            }
            lastHover = field_146123_n;
            ticks++;
        }
    }

    @Override
    public void func_146113_a(SoundHandler sh) {
        gui.playButtonSound(this);
    }

    public static interface CustomSoundGui {
        void playButtonSound(GuiButton b);

        void playHoverSound(GuiButton b);
    }

    public static class CustomSoundImagedGuiButton extends ImagedGuiButton {
        public final CustomSoundGui gui;

        public CustomSoundImagedGuiButton(
            int par1, int par2, int par3, String par4Str, Class mod, CustomSoundGui gui
        ) {
            super(par1, par2, par3, par4Str, mod);
            this.gui = gui;
        }

        /**
         * Draw a Gui Button with an image background. Args: id, x, y, width, height, u,
         * v, filepath, class root
         */
        public CustomSoundImagedGuiButton(
            int par1,
            int par2,
            int par3,
            int par4,
            int par5,
            int par7,
            int par8,
            String file,
            Class mod,
            CustomSoundGui gui
        ) {
            super(par1, par2, par3, par4, par5, par7, par8, file, mod);
            this.gui = gui;
        }

        /**
         *Draw a Gui Button with an image background and text overlay.
         *Args: id, x, y, width, height, u, v, text overlay, text color, shadow, filepath,
         *class root
        */
        public CustomSoundImagedGuiButton(
            int par1,
            int par2,
            int par3,
            int par4,
            int par5,
            int par7,
            int par8,
            String par6Str,
            int par9,
            boolean par10,
            String file,
            Class mod,
            CustomSoundGui gui
        ) {
            super(
                par1, par2, par3, par4, par5, par7, par8, par6Str, par9, par10, file, mod
            );
            this.gui = gui;
        }

        /**
         * Draw a Gui Button with an image background and text tooltip. Args: id, x, y,
         * width, height, u, v, filepath, text tooltip, text color, shadow
         */
        public CustomSoundImagedGuiButton(
            int par1,
            int par2,
            int par3,
            int par4,
            int par5,
            int par7,
            int par8,
            String file,
            String par6Str,
            int par9,
            boolean par10,
            Class mod,
            CustomSoundGui gui
        ) {
            super(
                par1, par2, par3, par4, par5, par7, par8, file, par6Str, par9, par10, mod
            );
            this.gui = gui;
        }

        @Override
        public final void func_146113_a(SoundHandler sh) {
            gui.playButtonSound(this);
        }

        @Override
        protected final void onHoverTo() {
            gui.playHoverSound(this);
        }
    }

    public static class CustomSoundImagedGuiButtonSneakIcon
        extends CustomSoundImagedGuiButton {
        private final int sneakU;
        private final int sneakV;

        public CustomSoundImagedGuiButtonSneakIcon(
            int par1,
            int par2,
            int par3,
            String par4Str,
            Class mod,
            CustomSoundGui gui,
            int u2,
            int v2
        ) {
            super(par1, par2, par3, par4Str, mod, gui);
            sneakU = u2;
            sneakV = v2;
        }

        /**
         * Draw a Gui Button with an image background. Args: id, x, y, width, height, u,
         * v, filepath, class root
         */
        public CustomSoundImagedGuiButtonSneakIcon(
            int par1,
            int par2,
            int par3,
            int par4,
            int par5,
            int par7,
            int par8,
            String file,
            Class mod,
            CustomSoundGui gui,
            int u2,
            int v2
        ) {
            super(par1, par2, par3, par4, par5, par7, par8, file, mod, gui);
            sneakU = u2;
            sneakV = v2;
        }

        /**
         *Draw a Gui Button with an image background and text overlay.
         *Args: id, x, y, width, height, u, v, text overlay, text color, shadow, filepath,
         *class root
        */
        public CustomSoundImagedGuiButtonSneakIcon(
            int par1,
            int par2,
            int par3,
            int par4,
            int par5,
            int par7,
            int par8,
            String par6Str,
            int par9,
            boolean par10,
            String file,
            Class mod,
            CustomSoundGui gui,
            int u2,
            int v2
        ) {
            super(
                par1,
                par2,
                par3,
                par4,
                par5,
                par7,
                par8,
                par6Str,
                par9,
                par10,
                file,
                mod,
                gui
            );
            sneakU = u2;
            sneakV = v2;
        }

        /**
         * Draw a Gui Button with an image background and text tooltip. Args: id, x, y,
         * width, height, u, v, filepath, text tooltip, text color, shadow
         */
        public CustomSoundImagedGuiButtonSneakIcon(
            int par1,
            int par2,
            int par3,
            int par4,
            int par5,
            int par7,
            int par8,
            String file,
            String par6Str,
            int par9,
            boolean par10,
            Class mod,
            CustomSoundGui gui,
            int u2,
            int v2
        ) {
            super(
                par1,
                par2,
                par3,
                par4,
                par5,
                par7,
                par8,
                file,
                par6Str,
                par9,
                par10,
                mod,
                gui
            );
            sneakU = u2;
            sneakV = v2;
        }

        @Override
        protected final void modifyTextureUV() {
            if (GuiScreen.isShiftKeyDown()) {
                u = sneakU;
                v = sneakV;
            }
        }
    }
}
