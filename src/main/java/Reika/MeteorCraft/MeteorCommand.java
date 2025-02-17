/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.MeteorCraft;

import java.util.ArrayList;

import Reika.DragonAPI.Command.DragonCommandBase;
import Reika.DragonAPI.Libraries.Java.ReikaRandomHelper;
import Reika.DragonAPI.Libraries.ReikaPlayerAPI;
import Reika.MeteorCraft.Entity.EntityMeteor;
import Reika.MeteorCraft.MeteorGenerator.MeteorType;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;

public class MeteorCommand extends DragonCommandBase {
    private final String tag = "meteor";

    @Override
    public String getCommandString() {
        return "meteor";
    }

    @Override
    public void processCommand(ICommandSender ics, String[] args) {
        EntityPlayerMP ep = getCommandSenderAsPlayer(ics);
        EntityMeteor e = MeteorSpawnController.instance.createMeteor(ep.worldObj, ep);
        for (int i = 0; i < args.length; i++) {
            Argument a = Argument.getArgument(args[i]);

            if (a == null) {
                StringBuilder sb2 = new StringBuilder();
                String sg = EnumChatFormatting.RED.toString() + "Invalid argument \""
                    + args[i] + "\".";
                sb2.append(sg);
                sb2.append("\n");
                sg = EnumChatFormatting.GOLD.toString() + "Valid arguments:";
                sb2.append(sg);
                sb2.append("\n");
                StringBuilder sb = new StringBuilder();
                ArrayList<String> li = this.getValidArguments();
                for (int k = 0; k < li.size(); k++) {
                    sb.append(li.get(k));
                    if (k < li.size() - 1)
                        sb.append("\n");
                }
                sg = sb.toString();
                sb2.append(sg);

                ChatComponentTranslation chat
                    = new ChatComponentTranslation(sb2.toString());
                ep.addChatMessage(chat);
                return;
            } else {
                a.applyToEntity(e);
            }
        }
        this.spawnMeteorAt(ep, e);
    }

    private void spawnMeteorAt(EntityPlayerMP ep, EntityMeteor e) {
        MovingObjectPosition mov = ReikaPlayerAPI.getLookedAtBlock(ep, 512, false);
        if (mov != null) {
            double dx
                = mov.blockX - 8 + ReikaRandomHelper.getSafeRandomInt(16) + 0.5 - e.posX;
            double dz
                = mov.blockZ - 8 + ReikaRandomHelper.getSafeRandomInt(16) + 0.5 - e.posZ;
            double dy = Math.abs(mov.blockY + 0.5 - e.posY);
            double v = Math.abs(e.motionY);
            e.motionX = dx * v / dy;
            e.motionZ = dz * v / dy;
        }
        if (!ep.worldObj.isRemote)
            ep.worldObj.spawnEntityInWorld(e);
    }

    @Override
    protected boolean isAdminOnly() {
        return true;
    }

    private enum Argument {
        AIRBURST("Make the meteor explode in midair:", "airburst", "explode"),
        STONE("Set the meteor to stone:", "stone", "overworld"),
        NETHERRACK("Set the meteor to netherrack:", "netherrack", "nether"),
        END("Set the meteor to end stone:", "endstone", "end"),
        SKY("Set the meteor to skystone:", "skystone", "sky", "ae2");

        public final String description;
        public final String[] variants;

        public static final Argument[] list = values();

        private Argument(String desc, String... args) {
            variants = args;
            description = desc;
        }

        public static Argument getArgument(String msg) {
            for (int i = 0; i < list.length; i++) {
                Argument a = list[i];
                for (int k = 0; k < a.variants.length; k++) {
                    String v = a.variants[k];
                    if (v.equals(msg))
                        return a;
                }
            }
            return null;
        }

        public void applyToEntity(EntityMeteor e) {
            switch (this) {
                case AIRBURST:
                    e.setExploding();
                    break;
                case STONE:
                    e.setType(MeteorType.STONE);
                    break;
                case NETHERRACK:
                    e.setType(MeteorType.NETHERRACK);
                    break;
                case END:
                    e.setType(MeteorType.END);
                    break;
                case SKY:
                    e.setType(MeteorType.SKYSTONE);
                    break;
            }
        }
    }

    public ArrayList<String> getValidArguments() {
        ArrayList<String> li = new ArrayList();
        for (int i = 0; i < Argument.list.length; i++) {
            Argument a = Argument.list[i];
            StringBuilder sb = new StringBuilder();
            sb.append(EnumChatFormatting.WHITE.toString());
            sb.append(a.description);
            sb.append(" ");
            for (int k = 0; k < a.variants.length; k++) {
                sb.append(EnumChatFormatting.GREEN.toString());
                sb.append(a.variants[k]);
                if (k < a.variants.length - 1) {
                    sb.append(EnumChatFormatting.WHITE.toString());
                    sb.append(", ");
                }
            }
            li.add(sb.toString());
        }
        return li;
    }
}
