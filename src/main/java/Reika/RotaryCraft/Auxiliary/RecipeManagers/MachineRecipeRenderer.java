/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.Auxiliary.RecipeManagers;

import java.util.ArrayList;
import java.util.List;

import Reika.DragonAPI.Libraries.Registry.ReikaItemHelper;
import Reika.DragonAPI.Libraries.Rendering.ReikaGuiAPI;
import Reika.DragonAPI.Libraries.Rendering.ReikaLiquidRenderer;
import Reika.RotaryCraft.Auxiliary.RecipeManagers.RecipesBlastFurnace.BlastCrafting;
import Reika.RotaryCraft.Auxiliary.RecipeManagers.RecipesBlastFurnace.BlastRecipe;
import Reika.RotaryCraft.RotaryCraft;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class MachineRecipeRenderer {
    public static final MachineRecipeRenderer instance = new MachineRecipeRenderer();

    private final FontRenderer font = Minecraft.getMinecraft().fontRenderer;
    private final RenderItem itemRender = new RenderItem();
    private final RenderBlocks rb = new RenderBlocks();
    private final ReikaGuiAPI gui = ReikaGuiAPI.instance;

    private MachineRecipeRenderer() {}

    private int getIndex(List li) {
        long time = System.currentTimeMillis();
        int index = (int) ((time / 500) % li.size());
        return index;
    }

    /**
     * Draw a compactor recipe in the GUI. Args: x in, y in, input itemstack, x out, y
     * out, output itemstack
     */
    public void
    drawCompressor(int x, int y, ItemStack in, int x2, int y2, ItemStack out) {
        int j = gui.getScreenXInset();
        int k = gui.getScreenYInset();

        if (in != null) {
            for (int ii = 0; ii < 4; ii++)
                gui.drawItemStackWithTooltip(
                    itemRender, font, in, x + j, y + k + ii * 18
                );
        }
        if (out != null)
            gui.drawItemStackWithTooltip(itemRender, font, out, x2 + j, y2 + k);
    }

    public void drawBlastFurnaceCrafting(int x, int y, int x2, int y2, ItemStack output) {
        List<BlastCrafting> c
            = RecipesBlastFurnace.getRecipes().getAllCraftingMaking(output);
        if (c.isEmpty()) {
            RotaryCraft.logger.logError(
                "Tried drawing a Blast Furnace Crafting of " + output
                + ", but no recipes for it exist!"
            );
            return;
        }
        int n = (int) (System.nanoTime() / 1000000000) % c.size();
        this.drawBlastFurnaceCrafting(x, y, x2, y2, c.get(n));
    }

    public void drawBlastFurnaceCrafting(int x, int y, int x2, int y2, BlastCrafting r) {
        ItemStack[] items = r.getArrayForDisplay();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                ItemStack is = items[i + j * 3];
                if (is != null) {
                    gui.drawItemStackWithTooltip(
                        itemRender, font, is, x + 18 * i, y + 18 * j - 8
                    );
                }
            }
        }
        gui.drawItemStackWithTooltip(itemRender, font, r.outputItem(), x2 + 4, y2 - 4);
    }

    public void drawBlastFurnace(int x, int y, int x2, int y2, BlastRecipe r) {
        int j = gui.getScreenXInset();
        int k = gui.getScreenYInset();

        ArrayList<Integer> inputs = r.getValidInputNumbers();
        int index = this.getIndex(inputs);
        int num = inputs.get(index);
        for (int i = 0; i < 3; i++) {
            for (int n = 0; n < 3; n++) {
                if (i * 3 + n < num) {
                    ItemStack is = r.mainItemForDisplay();
                    gui.drawItemStackWithTooltip(
                        itemRender, font, is, x + j + 18 * n, y + k + i * 18
                    );
                }
            }
        }

        if (r.primary.exists())
            gui.drawItemStackWithTooltip(
                itemRender,
                font,
                r.primary.getItemForDisplay(true),
                x + j - 36,
                y + k + 18
            );
        if (r.secondary.exists())
            gui.drawItemStackWithTooltip(
                itemRender,
                font,
                r.secondary.getItemForDisplay(true),
                x + j - 36,
                y + k + 37
            );
        if (r.tertiary.exists())
            gui.drawItemStackWithTooltip(
                itemRender,
                font,
                r.tertiary.getItemForDisplay(true),
                x + j - 36,
                y + k - 1
            );

        ItemStack out
            = ReikaItemHelper.getSizedItemStack(r.outputItem(), r.getNumberProduced(num));
        gui.drawItemStackWithTooltip(itemRender, font, out, x2 + j, y2 + k);
    }

    /**
     * Draw an extractor recipe in the GUI. Args: x in, y in; items of top row;
     * x out, y out; items of bottom row.
     * Items MUST be size-4 arrays!
     */
    public void
    drawExtractor(int x, int y, ItemStack[] in, int x2, int y2, ItemStack[] out) {
        int j = gui.getScreenXInset();
        int k = gui.getScreenYInset();

        for (int ij = 0; ij < 4; ij++)
            gui.drawItemStackWithTooltip(
                itemRender, font, in[ij], x + j + 36 * ij, y + k
            );
        for (int ij = 0; ij < 4; ij++)
            gui.drawItemStackWithTooltip(
                itemRender, font, out[ij], x2 + j + 36 * ij, y2 + k
            );
    }

    /**
     * Draw a fermenter recipe in the GUI. Args: x,y of top input slot, items of input
     * slots, x,y out, item output. Item array must be size-2!
     */
    public void
    drawFermenter(int x, int y, ItemStack[] in, int x2, int y2, ItemStack out) {
        int j = gui.getScreenXInset();
        int k = gui.getScreenYInset();

        ReikaLiquidRenderer.bindFluidTexture(FluidRegistry.WATER);
        GL11.glColor4f(1, 1, 1, 1);
        int h = 16 - (int) (System.nanoTime() / 200000000) % 17;
        gui.drawTexturedModelRectFromIcon(
            x, y + 10 + 16 - h, FluidRegistry.WATER.getStillIcon(), 16, h
        );

        gui.drawItemStackWithTooltip(itemRender, font, in[0], x + j, y + k);
        gui.drawItemStackWithTooltip(itemRender, font, in[1], x + j, y + 36 + k);

        if (out != null)
            gui.drawItemStackWithTooltip(itemRender, font, out, x2 + 4 + j, y2 + 4 + k);
    }
}
