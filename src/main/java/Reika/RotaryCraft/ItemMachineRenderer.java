/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft;

import java.util.Map;

import Reika.DragonAPI.Auxiliary.ReikaSpriteSheets;
import Reika.DragonAPI.Libraries.IO.ReikaTextureHelper;
import Reika.DragonAPI.Libraries.Java.ReikaGLHelper.BlendMode;
import Reika.DragonAPI.Libraries.MathSci.ReikaMathLibrary;
import Reika.RotaryCraft.Auxiliary.Interfaces.EnchantableMachine;
import Reika.RotaryCraft.Auxiliary.Interfaces.NBTMachine;
import Reika.RotaryCraft.Auxiliary.OldTextureLoader;
import Reika.RotaryCraft.Auxiliary.RotaryAux;
import Reika.RotaryCraft.Base.TileEntity.TileEntityEngine;
import Reika.RotaryCraft.Registry.BlockRegistry;
import Reika.RotaryCraft.Registry.GearboxTypes;
import Reika.RotaryCraft.Registry.ItemRegistry;
import Reika.RotaryCraft.Registry.MachineRegistry;
import Reika.RotaryCraft.Registry.MaterialRegistry;
import Reika.RotaryCraft.TileEntities.Transmission.TileEntityAdvancedGear;
import Reika.RotaryCraft.TileEntities.Transmission.TileEntityFlywheel;
import Reika.RotaryCraft.TileEntities.Transmission.TileEntityGearbox;
import Reika.RotaryCraft.TileEntities.Transmission.TileEntityShaft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class ItemMachineRenderer implements IItemRenderer {
    private int Renderid;
    private int metadata;

    private final RenderBlocks rb = new RenderBlocks();

    public TileEntity getRenderingInstance(MachineRegistry m, int offset) {
        return m.createTEInstanceForRender(offset);
    }

    public ItemMachineRenderer() {}

    public ItemMachineRenderer(int id) {
        Renderid = id;
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(
        ItemRenderType type, ItemStack item, ItemRendererHelper helper
    ) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        Map map = EnchantmentHelper.getEnchantments(item);
        boolean enchant = map != null && !map.isEmpty();
        if (Renderid == -1) {
            ReikaTextureHelper.bindTerrainTexture();
            rb.renderBlockAsItem(Blocks.stone, 0, 1);
            return;
        } else if (OldTextureLoader.instance.loadOldTextures()) {
            MachineRegistry m = MachineRegistry.getMachineByPlacerItem(item);
            if (m == null) {
                ReikaTextureHelper.bindTerrainTexture();
                rb.renderBlockAsItem(Blocks.brick_block, 0, 1F);
                return;
            }
            ReikaTextureHelper.bindTerrainTexture();
            rb.renderBlockAsItem(m.getBlock(), m.getBlockMetadata(), 1F);
            return;
        }
        float a = 0;
        float b = 0;
        if (ItemRegistry.ENGINE.matchItem(item)) {
            TileEntity te
                = this.getRenderingInstance(MachineRegistry.ENGINE, item.getItemDamage());
            TileEntityEngine eng = (TileEntityEngine) te;
            if (type == type.ENTITY) {
                a = -0.5F;
                b = -0.5F;
                GL11.glScalef(0.5F, 0.5F, 0.5F);
            }
            eng.setType(item);
            TileEntityRendererDispatcher.instance.renderTileEntityAt(eng, a, 0.0D, b, 0);
        } else if (ItemRegistry.GEARBOX.matchItem(item)) {
            TileEntity te = this.getRenderingInstance(
                MachineRegistry.GEARBOX, item.getItemDamage()
            );
            TileEntityGearbox gbx = (TileEntityGearbox) te;
            if (type == type.ENTITY) {
                a = -0.5F;
                b = -0.5F;
                GL11.glScalef(0.5F, 0.5F, 0.5F);
            }
            gbx.setData(
                GearboxTypes.getMaterialFromGearboxItem(item),
                ReikaMathLibrary.intpow2(2, item.getItemDamage() + 1)
            );
            int amt = item.stackTagCompound != null
                ? item.stackTagCompound.getInteger("lube")
                : 0;
            gbx.setLubricant(amt);
            if (item.stackTagCompound != null)
                gbx.setDataFromItemStackTag(item.stackTagCompound);
            TileEntityRendererDispatcher.instance.renderTileEntityAt(gbx, a, 0.0D, b, 0);
        } else if (ItemRegistry.ADVGEAR.matchItem(item)) {
            TileEntity te = this.getRenderingInstance(
                MachineRegistry.ADVANCEDGEARS, item.getItemDamage()
            );
            TileEntityAdvancedGear adv = (TileEntityAdvancedGear) te;
            if (type == type.ENTITY) {
                a = -0.5F;
                b = -0.5F;
                GL11.glScalef(0.5F, 0.5F, 0.5F);
            }
            if (item.stackTagCompound != null
                && item.stackTagCompound.getBoolean("bedrock"))
                adv.setBedrock(true);
            else
                adv.setBedrock(false);
            TileEntityRendererDispatcher.instance.renderTileEntityAt(
                adv, a, -0.1D, b, -1000F * (item.getItemDamage() + 1)
            );
        } else if (ItemRegistry.FLYWHEEL.matchItem(item)) {
            TileEntity te = this.getRenderingInstance(
                MachineRegistry.FLYWHEEL, item.getItemDamage()
            );
            TileEntityFlywheel fly = (TileEntityFlywheel) te;
            fly.setMaterialFromItem(item);
            if (type == type.ENTITY) {
                a = -0.5F;
                b = -0.5F;
                GL11.glScalef(0.5F, 0.5F, 0.5F);
            }
            TileEntityRendererDispatcher.instance.renderTileEntityAt(fly, a, 0.0D, b, 0);
        } /*
         else if (item.itemID == RotaryCraft.hydraulicitems.itemID) {
             TileEntity te = this.getRenderingInstance(MachineRegistry.HYDRAULIC);
             TileEntityHydraulicPump hyd = (TileEntityHydraulicPump)te;
             if (type == type.ENTITY) {
                 a = -0.5F; b = -0.5F;
                 GL11.glScalef(0.5F, 0.5F, 0.5F);
             }
             TileEntityRendererDispatcher.instance.renderTileEntityAt(hyd, a, 0.0D, b,
         -1000F*(item.getItemDamage()+1));
         }*/
        else if (ItemRegistry.SHAFT.matchItem(item)) {
            TileEntity te
                = this.getRenderingInstance(MachineRegistry.SHAFT, item.getItemDamage());
            TileEntityShaft sha = (TileEntityShaft) te;
            sha.setData(
                MaterialRegistry.getMaterialFromShaftItem(item),
                RotaryAux.isShaftCross(item)
            );
            if (type == type.ENTITY) {
                GL11.glScalef(0.5F, 0.5F, 0.5F);
                a = -0.5F;
                b = -0.5F;
            }
            TileEntityRendererDispatcher.instance.renderTileEntityAt(sha, a, 0.0D, b, 0);
        } else if (ItemRegistry.MACHINE.matchItem(item)) {
            GL11.glEnable(GL11.GL_BLEND);
            BlendMode.DEFAULT.apply();
            if (type == type.ENTITY) {
                a = -0.5F;
                b = -0.5F;
                GL11.glScalef(0.5F, 0.5F, 0.5F);
            }
            if (item.getItemDamage() >= MachineRegistry.machineList.length)
                throw new IllegalStateException("Invalid machine item for render!");
            MachineRegistry machine
                = MachineRegistry.machineList.get(item.getItemDamage());
            if (machine.isPipe()) {
                if (type == type.EQUIPPED || type == type.EQUIPPED_FIRST_PERSON) {
                    double d = 0.5;
                    GL11.glTranslated(d, d, d);
                }
                rb.renderBlockAsItem(
                    BlockRegistry.PIPING.getBlockInstance(), machine.getBlockMetadata(), 1
                );
            } else if (machine.hasModel()) {
                TileEntity te = this.getRenderingInstance(machine, 0);
                if (machine.isEnchantable()) {
                    EnchantableMachine em = (EnchantableMachine) te;
                    em.getEnchantmentHandler().clear();
                    em.getEnchantmentHandler().applyEnchants(item);
                }
                if (machine.hasNBTVariants()) {
                    ((NBTMachine) te).setDataFromItemStackTag(item.stackTagCompound);
                }
                if (RenderItem.renderInFrame && type == type.ENTITY) {
                    //GL11.glRotated(0, 0, 0, 0);
                }
                TileEntityRendererDispatcher.instance.renderTileEntityAt(
                    te, a, -0.1D, b, 0.0F
                );
            } else {
                ReikaTextureHelper.bindTerrainTexture();
                if (type == type.EQUIPPED || type == type.EQUIPPED_FIRST_PERSON) {
                    double d = 0.5;
                    GL11.glTranslated(d, d, d);
                } else if (RenderItem.renderInFrame && type == type.ENTITY) {
                    GL11.glRotated(90, 0, 1, 0);
                }
                rb.renderBlockAsItem(
                    MachineRegistry.machineList.get(item.getItemDamage()).getBlock(),
                    MachineRegistry.machineList.get(item.getItemDamage())
                        .getBlockMetadata(),
                    1
                );
                if (enchant) {
                    GL11.glRotated(90, 0, 0, 1);
                    ReikaSpriteSheets.renderEffect(type, item);
                    GL11.glRotated(-90, 0, 0, 1);
                }
            }
            GL11.glDisable(GL11.GL_BLEND);
        }
    }
}
