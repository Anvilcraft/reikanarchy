package Reika.Satisforestry.Render;

import Reika.Satisforestry.Satisforestry;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.model.ModelFormatException;
import net.minecraftforge.client.model.obj.GroupObject;
import net.minecraftforge.client.model.obj.WavefrontObject;

public class ModelSFMiner extends WavefrontObject {
    private GroupObject chassis;
    private GroupObject drill;
    private GroupObject lightbar;

    public ModelSFMiner(String path) throws ModelFormatException {
        super("SF Miner", Satisforestry.class.getResourceAsStream(path));

        chassis = this.findGroupObject("base");
        drill = this.findGroupObject("drill");
        lightbar = this.findGroupObject("lightbar");
    }

    private GroupObject findGroupObject(String s) {
        for (GroupObject g : groupObjects) {
            if (g.name.equals(s))
                return g;
        }
        return null;
    }

    public void drawChassis() {
        chassis.render();
    }

    public void drawDrill() {
        if (drill != null)
            drill.render();
    }

    public void drawLightbar(int c) {
        Tessellator v5 = Tessellator.instance;
        v5.startDrawing(lightbar.glDrawingMode);
        v5.setBrightness(240);
        v5.setColorOpaque_I(c);
        lightbar.render(v5);
        v5.draw();
    }
}
