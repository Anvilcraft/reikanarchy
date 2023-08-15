/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.DragonAPI.Command;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;

import Reika.DragonAPI.Command.BytecodeCommand.BytecodeProgram.ByteCodeInstruction;
import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.DragonAPIInit;
import Reika.DragonAPI.DragonOptions;
import Reika.DragonAPI.Exception.InstallationException;
import Reika.DragonAPI.IO.ReikaFileReader;
import Reika.DragonAPI.Libraries.IO.ReikaChatHelper;
import Reika.DragonAPI.Libraries.Java.ReikaJavaLibrary;
import Reika.DragonAPI.Libraries.Java.ReikaReflectionHelper;
import Reika.DragonAPI.Libraries.Registry.ReikaItemHelper;
import Reika.DragonAPI.Libraries.ReikaPlayerAPI;
import com.google.common.base.Charsets;
import cpw.mods.fml.common.registry.RegistryDelegate;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;

/**
   Example use:
    LDC {dimID} [+1 stack]<br>
    INVOKESTATIC net.minecraftforge.common.DimensionManager getWorld [-1 stack, +1
   stack]<br> LDC {x} [+1 stack]<br> LDC {y} [+1 stack]<br> LDC {z} [+1 stack]<br>
    INVOKEVIRTUAL net.minecraft.world.World getTileEntity [-4 stack, +1 stack]<br>
    GETFIELD {tileClass} {fieldName} [-1 stack, +1 stack]<br>
    OUTPUT<br>
    FLUSH [empty stack]<br>
 */
public class BytecodeCommand extends ReflectiveBasedCommand {
    private static final String PROGRAM_PATH = "DragonAPI_CommandPrograms";

    private static final HashMap<UUID, Stack> objectStack = new HashMap();

    private final HashSet<UUID> playerPermissions = new HashSet();

    private BytecodeProgram program;

    public static BytecodeCommand instance;

    public BytecodeCommand() {
        for (String s : DragonOptions.BYTECODELIST.getStringArray()) {
            try {
                playerPermissions.add(UUID.fromString(s));
            } catch (IllegalArgumentException e) {
                throw new InstallationException(
                    DragonAPIInit.instance,
                    "Invalid UUID in whitelist for " + this.getCommandString() + ": " + s
                );
            }
        }
        instance = this;
    }

    @Override
    public void processCommand(ICommandSender ics, String[] args) {
        boolean admin = this.hasPermissionToRun(ics);
        if (args[0].equalsIgnoreCase("define")) {
            if (admin) {
                try {
                    String cl = args[1];
                    if (cl.equalsIgnoreCase("*stack*")) {
                        Object o = this.getStack(ics).pop();
                        if (o instanceof Class) {
                            Class c = (Class) o;
                            if (this.addClassShortcut(c))
                                this.sendChatToSender(
                                    ics,
                                    EnumChatFormatting.GREEN
                                        + "New shortcut defined for class: #"
                                        + c.getSimpleName() + " for " + c.getName()
                                );
                            else
                                this.sendChatToSender(
                                    ics,
                                    EnumChatFormatting.RED
                                        + "Class shortcut already defined for " + args[1]
                                );
                            return;
                        }
                    }
                    Class c = this.findClass(cl);
                    if (this.addClassShortcut(c))
                        this.sendChatToSender(
                            ics,
                            EnumChatFormatting.GREEN + "New shortcut defined for class: #"
                                + c.getSimpleName() + " for " + c.getName()
                        );
                    else
                        this.sendChatToSender(
                            ics,
                            EnumChatFormatting.RED + "Class shortcut already defined for "
                                + args[1]
                        );
                } catch (ClassNotFoundException e) {
                    this.sendChatToSender(
                        ics, EnumChatFormatting.RED + "No such class: " + args[1]
                    );
                }
            } else {
                this.sendChatToSender(
                    ics,
                    EnumChatFormatting.RED
                        + "You do not have permission to use this command in this way."
                );
            }
            return;
        } else if (args[0].equalsIgnoreCase("shortcuts")) {
            EntityPlayer ep = this.getCommandSenderAsPlayer(ics);
            this.sendChatToSender(ics, "Defined class shortcuts: ");
            for (String s : this.getClassShortcuts().keySet()) {
                String sg = "#" + s + " - " + this.getClassShortcuts().get(s).getName();
                this.sendChatToSender(ics, sg);
            }
            return;
        } else if (args[0].equalsIgnoreCase("self")) {
            if (admin) {
                EntityPlayer ep = this.getCommandSenderAsPlayer(ics);
                this.getStack(ics).push(ep);
                this.sendChatToSender(
                    ics, EnumChatFormatting.GREEN + "Loaded self onto the stack."
                );
            } else {
                this.sendChatToSender(
                    ics,
                    EnumChatFormatting.RED
                        + "You do not have permission to use this command in this way."
                );
            }
            return;
        } else if (args[0].equalsIgnoreCase("getplayer")) {
            if (admin) {
                EntityPlayer ep = ReikaPlayerAPI.getPlayerByNameAnyWorld(args[1]);
                if (ep != null) {
                    this.getStack(ics).push(ep);
                    this.sendChatToSender(
                        ics,
                        EnumChatFormatting.GREEN + "Loaded " + ep.getCommandSenderName()
                            + " onto the stack."
                    );
                }
            } else {
                this.sendChatToSender(
                    ics,
                    EnumChatFormatting.RED
                        + "You do not have permission to use this command in this way."
                );
            }
            return;
        } else if (args[0].equalsIgnoreCase("held")) {
            if (admin) {
                EntityPlayer ep = this.getCommandSenderAsPlayer(ics);
                this.getStack(ics).push(ep.getCurrentEquippedItem());
                this.sendChatToSender(
                    ics, EnumChatFormatting.GREEN + "Loaded held item onto the stack."
                );
            } else {
                this.sendChatToSender(
                    ics,
                    EnumChatFormatting.RED
                        + "You do not have permission to use this command in this way."
                );
            }
            return;
        } else if (args[0].equalsIgnoreCase("look")) {
            if (admin) {
                EntityPlayer ep = this.getCommandSenderAsPlayer(ics);
                MovingObjectPosition mov
                    = ReikaPlayerAPI.getLookedAtBlock(ep, 4.5, false);
                if (mov != null) {
                    this.getStack(ics).push(ep.worldObj);
                    this.getStack(ics).push(mov.blockX);
                    this.getStack(ics).push(mov.blockY);
                    this.getStack(ics).push(mov.blockZ);
                    this.sendChatToSender(
                        ics,
                        EnumChatFormatting.GREEN
                            + "Loaded looked position onto the stack."
                    );
                } else {
                    this.sendChatToSender(
                        ics, EnumChatFormatting.RED + "Not looking at a block."
                    );
                }
            } else {
                this.sendChatToSender(
                    ics,
                    EnumChatFormatting.RED
                        + "You do not have permission to use this command in this way."
                );
            }
            return;
        } else if (args[0].equalsIgnoreCase("getclass")) {
            if (admin) {
                EntityPlayer ep = this.getCommandSenderAsPlayer(ics);
                try {
                    this.getStack(ics).push(this.findClass(args[1]));
                    this.sendChatToSender(
                        ics, EnumChatFormatting.GREEN + "Loaded held item onto the stack."
                    );
                } catch (ClassNotFoundException e) {
                    this.sendChatToSender(
                        ics, EnumChatFormatting.RED + "No such class '" + args[1] + '!'
                    );
                }
            } else {
                this.sendChatToSender(
                    ics,
                    EnumChatFormatting.RED
                        + "You do not have permission to use this command in this way."
                );
            }
            return;
        } else if (args[0].equalsIgnoreCase("mapsrg")) {
            if (admin) {
                try {
                    Class cl = findClass(args[1]);
                    addSRGMapping(cl, args[3], args[2]);
                    this.sendChatToSender(
                        ics,
                        EnumChatFormatting.GREEN + "Loaded SRG mapping " + cl.getName()
                            + "::obf=" + args[2] + " >deobf=" + args[3]
                    );
                } catch (ClassNotFoundException e) {
                    this.sendChatToSender(
                        ics,
                        EnumChatFormatting.RED
                            + "Could not parse SRG mapping - class not found."
                    );
                }
            } else {
                this.sendChatToSender(
                    ics,
                    EnumChatFormatting.RED
                        + "You do not have permission to use this command in this way."
                );
            }
            return;
        } else if (args[0].equalsIgnoreCase("loadsrg")) {
            if (admin) {
                int sum = 0;
                for (String s : ReikaFileReader.getFileAsLines(
                         new File(args[1]), true, Charsets.UTF_8
                     )) {
                    s = s.trim();
                    if (s.isEmpty())
                        continue;
                    try {
                        String[] parts = s.split(" ");
                        int idx = parts[1].lastIndexOf('/');
                        Class cl
                            = Class.forName(parts[1].substring(0, idx).replace('/', '.'));
                        if (s.startsWith("FD"
                            )) //eg FD: net/minecraft/world/chunk/Chunk/heightMapMinimum
                               //net/minecraft/world/chunk/Chunk/field_82912_p
                            addSRGMapping(
                                cl,
                                parts[1].substring(idx + 1),
                                parts[2].substring(idx + 1)
                            );
                        else if (s.startsWith("MD")) //eg MD:
                                                     //net/minecraft/block/BlockStainedGlass/canSilkHarvest
                                                     //()Z
                                                     //net/minecraft/block/BlockStainedGlass/func_149700_E
                                                     //()Z
                            addSRGMapping(
                                cl,
                                parts[1].substring(idx + 1),
                                parts[3].substring(idx + 1)
                            );
                        sum++;
                    } catch (Exception e) {
                        DragonAPICore.logError("Failed to parse SRG line '" + s + "'");
                        e.printStackTrace();
                    }
                }
                this.sendChatToSender(
                    ics, EnumChatFormatting.GREEN + "Loaded " + sum + " SRG mappings."
                );
            } else {
                this.sendChatToSender(
                    ics,
                    EnumChatFormatting.RED
                        + "You do not have permission to use this command in this way."
                );
            }
            return;
        } else if (args[0].equalsIgnoreCase("startProgram")) {
            if (admin) {
                EntityPlayerMP ep = this.getCommandSenderAsPlayer(ics);
                if (args.length < 2) {
                    this.sendChatToSender(
                        ics,
                        EnumChatFormatting.RED
                            + "Not enough arguments: You must specify a name!"
                    );
                    return;
                }
                this.startProgram(ep, args);
            } else {
                this.sendChatToSender(
                    ics,
                    EnumChatFormatting.RED
                        + "You do not have permission to use this command in this way."
                );
            }
            return;
        } else if (args[0].equalsIgnoreCase("saveProgram")) {
            if (admin) {
                EntityPlayerMP ep = this.getCommandSenderAsPlayer(ics);
                this.finishProgram(ep);
            } else {
                this.sendChatToSender(
                    ics,
                    EnumChatFormatting.RED
                        + "You do not have permission to use this command in this way."
                );
            }
            return;
        } else if (args[0].equalsIgnoreCase("runProgram")) {
            EntityPlayerMP ep = this.getCommandSenderAsPlayer(ics);
            if (args.length < 2) {
                this.sendChatToSender(
                    ics,
                    EnumChatFormatting.RED
                        + "Not enough arguments: You must specify a name!"
                );
                return;
            }
            this.runProgram(ep, args[1]);
            return;
        }
        if (!admin) {
            this.sendChatToSender(
                ics,
                EnumChatFormatting.RED
                    + "You do not have permission to use this command in this way."
            );
            return;
        }
        boolean removeTop = !args[0].startsWith("&");
        if (!removeTop)
            args[0] = args[0].substring(1);
        Opcodes o = null;
        try {
            o = Opcodes.valueOf(args[0].toUpperCase(Locale.ENGLISH));
        } catch (Exception e) {
            this.error(ics, "No such opcode: " + args[0]);
            this.sendChatToSender(
                ics, "Valid opcodes: " + Arrays.toString(Opcodes.values())
            );
            return;
        }
        args = Arrays.copyOfRange(args, 1, args.length);
        if (program != null) {
            program.instructions.add(new ByteCodeInstruction(o, args));
            this.sendChatToSender(
                ics, EnumChatFormatting.GREEN + "Added instruction to program:"
            );
            this.sendChatToSender(ics, program.toString());
        } else {
            try {
                Stack s = this.getStack(ics);
                o.call(this, ics, args, s, removeTop);
                if (DragonOptions.NOTIFYBYTEEXEC.getState())
                    this.notifyOtherAdmins(ics, o, s, args);
                if (o != Opcodes.OUTPUT)
                    Opcodes.OUTPUT.call(this, ics, null, this.getStack(ics), false);
            } catch (ClassNotFoundException e) {
                this.error(ics, "No such class: " + e);
            } catch (NoSuchFieldException e) {
                this.error(ics, "No such field: " + e);
            } catch (NoSuchMethodException e) {
                this.error(ics, "No such method: " + e);
            } catch (IllegalArgumentException e) {
                this.error(ics, "Invalid specified type: " + e);
            } catch (IllegalAccessException e) {
                //never happens
            } catch (InvocationTargetException e) {
                this.error(ics, "Called method threw exception: " + e);
            } catch (InstantiationException e) {
                this.error(ics, "Could not construct object: " + e);
            } catch (Exception e) {
                this.error(ics, "Could not execute: " + e);
                e.printStackTrace();
            }
        }
    }

    private void
    notifyOtherAdmins(ICommandSender ics, Opcodes o, Stack s, String[] args) {
        for (EntityPlayer ep : ReikaPlayerAPI.getOps()) {
            if (ep != ics) {
                ReikaChatHelper.sendChatToPlayer(
                    ep,
                    "Player " + ics + " ran bytecode command: " + o.name() + " args "
                        + Arrays.toString(args) + " resulting in stack " + s
                );
            }
        }
    }

    private boolean hasPermissionToRun(ICommandSender ics) {
        if (ics instanceof RConConsoleSource) //console
            return true;
        if (DragonAPICore.isSinglePlayer())
            return true;
        EntityPlayer ep = this.getCommandSenderAsPlayer(ics);
        return playerPermissions.contains(ep.getUniqueID());
    }

    @Override
    protected void error(ICommandSender ics, String s) {
        super.error(ics, s);
        try {
            //Opcodes.FLUSH.call(this, ics, null, this.getStack(ics));
        } catch (Exception e) {
            //never happens
        }
    }

    private void startProgram(EntityPlayerMP ep, String[] args) {
        if (program != null) {
            this.sendChatToSender(
                ep,
                EnumChatFormatting.YELLOW
                    + "Cannot start writing a program; another is already being written: "
                    + program
            );
            return;
        }
        HashSet<String> args2 = new HashSet();
        for (int i = 2; i < args.length; i++) {
            args2.add(args[i].toLowerCase(Locale.ENGLISH));
        }
        program = new BytecodeProgram(ep, args[1], args2);
        this.sendChatToSender(
            ep, EnumChatFormatting.GREEN + "Started writing program: " + program
        );
    }

    private void finishProgram(EntityPlayerMP ep) {
        try {
            program.save();
            this.sendChatToSender(
                ep, EnumChatFormatting.GREEN + "Saved program: " + program
            );
            program = null;
        } catch (IOException e) {
            this.sendChatToSender(
                ep,
                EnumChatFormatting.RED + "Could not save program file: " + e.toString()
            );
            e.printStackTrace();
        }
    }

    private void runProgram(EntityPlayerMP ep, String name) {
        BytecodeProgram program = this.findProgram(name);
        if (program == null) {
            this.sendChatToSender(
                ep, EnumChatFormatting.RED + "No such program '" + name + "'!"
            );
            return;
        }
        program.stack.clear();
        program.stack.addAll(this.getStack(ep));
        this.getStack(ep).clear();
        if (program.allowAnyUser || (program.allowAnyAdmin && ReikaPlayerAPI.isAdmin(ep))
            || program.programOwner.equals(ep.getUniqueID())) {
            try {
                program.run(ep, this);
                this.sendChatToSender(
                    ep, EnumChatFormatting.GREEN + "Program ran successfully! Stack: "
                );
                this.sendChatToSender(ep, program.stack.toString());
            } catch (Exception e) {
                this.sendChatToSender(
                    ep, EnumChatFormatting.RED + "Program threw exception!"
                );
                this.sendChatToSender(ep, e.toString());
                e.printStackTrace();
            }
        } else {
            this.sendChatToSender(
                ep, EnumChatFormatting.RED + "You do not have permission to run this!"
            );
        }
    }

    private BytecodeProgram findProgram(String name) {
        File base = DragonAPICore.getMinecraftDirectory();
        File f = new File(base, PROGRAM_PATH + "/" + name + ".byteprog");
        if (!f.exists())
            return null;
        return BytecodeProgram.readFile(f);
    }

    public Stack getStack(ICommandSender ics) {
        UUID id = this.getUID(ics);
        Stack s = objectStack.get(id);
        if (s == null) {
            s = new Stack();
            objectStack.put(id, s);
        }
        return s;
    }

    private void writeObjectToFile(File f, int idx, Object o, boolean fullValues)
        throws IOException {
        String s = "#" + idx + " - " + o.getClass().getName().replaceAll("\\\\.", "/");
        File f2 = new File(f, s + ".log");
        f2.getParentFile().mkdirs();
        f2.delete();
        f2.createNewFile();
        ReikaFileReader.writeLinesToFile(
            f2, this.getObjectData(o, 0, fullValues, new HashSet()), true, Charsets.UTF_8
        );
    }

    private ArrayList<String>
    getObjectData(Object o, int depth, boolean fullValues, HashSet<Object> chain) {
        ArrayList<String> li = new ArrayList();
        if (!chain.isEmpty()) {
            if (o instanceof RegistryDelegate || o instanceof World
                || o instanceof MinecraftServer)
                return li;
        }
        if (chain.contains(o)
            && !(
                (o instanceof String || o instanceof Number || o instanceof Boolean
                 || o instanceof Object[] || o instanceof Iterable || o instanceof Map
                 || o instanceof Enum)
            ))
            return li;
        chain.add(o);
        if (o == null) {
            li.add(this.pad(depth) + "<null>");
            return li;
        }
        if (o instanceof Map) {
            li.add(this.pad(depth) + "Map [" + o.getClass() + "]:");
            Map m = (Map) o;
            Set<Map.Entry> st
                = m.entrySet(); //not sure why this requires independent declaration
            for (Map.Entry e : st) {
                li.add(this.pad(depth + 1) + "Key:");
                for (String s :
                     this.getObjectData(e.getKey(), depth + 2, fullValues, chain)) {
                    li.add(s);
                }
                li.add(this.pad(depth + 1) + "Value:");
                for (String s :
                     this.getObjectData(e.getValue(), depth + 2, fullValues, chain)) {
                    li.add(s);
                }
                li.add("----");
            }
        } else if (o.getClass().isArray()) {
            li.add(this.pad(depth + 1) + "Array:");
            if (o instanceof int[]) {
                for (int o2 : (int[]) o) {
                    for (String s :
                         this.getObjectData(o2, depth + 2, fullValues, chain)) {
                        li.add(s);
                    }
                    li.add("----");
                }
            } else if (o instanceof boolean[]) {
                for (boolean o2 : (boolean[]) o) {
                    for (String s :
                         this.getObjectData(o2, depth + 2, fullValues, chain)) {
                        li.add(s);
                    }
                    li.add("----");
                }
            } else if (o instanceof float[]) {
                for (float o2 : (float[]) o) {
                    for (String s :
                         this.getObjectData(o2, depth + 2, fullValues, chain)) {
                        li.add(s);
                    }
                    li.add("----");
                }
            } else if (o instanceof double[]) {
                for (double o2 : (double[]) o) {
                    for (String s :
                         this.getObjectData(o2, depth + 2, fullValues, chain)) {
                        li.add(s);
                    }
                    li.add("----");
                }
            } else if (o instanceof short[]) {
                for (short o2 : (short[]) o) {
                    for (String s :
                         this.getObjectData(o2, depth + 2, fullValues, chain)) {
                        li.add(s);
                    }
                    li.add("----");
                }
            } else if (o instanceof long[]) {
                for (long o2 : (long[]) o) {
                    for (String s :
                         this.getObjectData(o2, depth + 2, fullValues, chain)) {
                        li.add(s);
                    }
                    li.add("----");
                }
            } else if (o instanceof byte[]) {
                for (byte o2 : (byte[]) o) {
                    for (String s :
                         this.getObjectData(o2, depth + 2, fullValues, chain)) {
                        li.add(s);
                    }
                    li.add("----");
                }
            } else if (o instanceof char[]) {
                for (char o2 : (char[]) o) {
                    for (String s :
                         this.getObjectData(o2, depth + 2, fullValues, chain)) {
                        li.add(s);
                    }
                    li.add("----");
                }
            } else {
                for (Object o2 : ((Object[]) o)) {
                    for (String s :
                         this.getObjectData(o2, depth + 2, fullValues, chain)) {
                        li.add(s);
                    }
                    li.add("----");
                }
            }
        } else if (o instanceof Iterable) {
            li.add(this.pad(depth + 1) + "Iterable [" + o.getClass() + "]:");
            for (Object o2 : ((Iterable) o)) {
                for (String s : this.getObjectData(o2, depth + 2, fullValues, chain)) {
                    li.add(s);
                }
                li.add("----");
            }
        } else if (o instanceof ItemStack) {
            li.add(this.pad(depth) + "ItemStack: " + this.fullID((ItemStack) o));
        } else {
            String ts;
            try {
                ts = "toString() : " + o.toString();
            } catch (Exception e) {
                ts = "threw Exception: " + e.toString();
            }
            li.add(this.pad(depth) + "[" + o.getClass().getName() + "] " + ts);
        }

        if (fullValues && o != null
            && !(
                o instanceof String || o instanceof Number || o instanceof Boolean
                || o instanceof Object[] || o instanceof Iterable || o instanceof Map
                || o instanceof Enum
            )) {
            try {
                li.add(this.pad(depth + 1) + "Fields:");
                for (Field f : ReikaReflectionHelper.getFields(o.getClass(), null)) {
                    if ((f.getModifiers() & Modifier.STATIC) == 0 && !f.isSynthetic()) {
                        f.setAccessible(true);
                        ReikaJavaLibrary.pConsole(f.getName());
                        Object o2 = f.get(o);
                        if (o != o2) {
                            li.add(
                                this.pad(depth + 2) + f.getName() + " [" + f.getType()
                                + "]:"
                            );
                            for (String s :
                                 this.getObjectData(o2, depth + 3, fullValues, chain)) {
                                li.add(s);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return li;
    }

    private final String fullID(ItemStack is) {
        if (is == null)
            return "[null]";
        else if (is.getItem() == null)
            return "[null-item stack]";
        return is.stackSize + "x" + Item.itemRegistry.getNameForObject(is.getItem()) + "@"
            + is.getItemDamage() + "{" + is.stackTagCompound + "}["
            + ReikaItemHelper.getRegistrantMod(is) + "]";
    }

    private String pad(int d) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < d; i++) {
            sb.append("\t\t");
        }
        return sb.toString();
    }

    @Override
    public String getCommandString() {
        return "bytecodeexec";
    }

    @Override
    protected boolean isAdminOnly() {
        return false; //for program admins
    }

    private static enum Opcodes {
        LDC(),
        NEW(),
        DUP(),
        POP(),
        SWAP(),
        INVOKESTATIC(),
        INVOKEVIRTUAL(),
        OBJMETHOD(),
        GETSTATIC(),
        GETFIELD(),
        OBJFIELD(),
        SETFIELD(),
        //THROW(),
        INSTANCEOF(),
        MAKEARRAY(),
        GETARRAY(),
        SETARRAY(),
        //DECOMPOSE(),
        CONCAT(),
        ITERATE(),
        OUTPUT(),
        WRITE(),
        FLUSH();

        private static final Opcodes[] list = values();

        private void call(
            BytecodeCommand cmd,
            ICommandSender ics,
            String[] args,
            Stack s,
            boolean removeTop
        ) throws ClassNotFoundException, NoSuchFieldException, IllegalArgumentException,
                 IllegalAccessException, NoSuchMethodException, InvocationTargetException,
                 InstantiationException {
            switch (this) {
                case FLUSH:
                    s.clear();
                    break;
                case GETFIELD: {
                    Class c = cmd.findClass(args[0]);
                    Field f = c.getDeclaredField(cmd.deSRG(c, args[1]));
                    f.setAccessible(true);
                    if (s.isEmpty())
                        throw new IllegalArgumentException("Operand stack underflow");
                    s.push(f.get(removeTop ? s.pop() : s.peek()));
                    break;
                }

                case OBJFIELD: {
                    Object o = s.peek();
                    Field f
                        = ReikaReflectionHelper.getProtectedInheritedField(o, args[0]);
                    f.setAccessible(true);
                    if (s.isEmpty())
                        throw new IllegalArgumentException("Operand stack underflow");
                    s.push(f.get(removeTop ? s.pop() : s.peek()));
                    break;
                }
                case SETFIELD: {
                    Class c = cmd.findClass(args[0]);
                    Field f = c.getDeclaredField(cmd.deSRG(c, args[1]));
                    f.setAccessible(true);
                    if (s.size() < 2)
                        throw new IllegalArgumentException("Operand stack underflow");
                    Object arg = s.pop();
                    f.set(removeTop ? s.pop() : s.peek(), arg);
                    break;
                }
                case GETSTATIC: {
                    Class c = cmd.findClass(args[0]);
                    Field f = c.getDeclaredField(cmd.deSRG(c, args[1]));
                    f.setAccessible(true);
                    s.push(f.get(null));
                    break;
                }
                case INVOKESTATIC: {
                    Class c = cmd.findClass(args[0]);
                    Class[] types = cmd.parseTypes(args[2]);
                    Method m = c.getDeclaredMethod(cmd.deSRG(c, args[1]), types);
                    m.setAccessible(true);
                    Object[] vals = new Object[types.length];
                    if (vals.length > s.size())
                        throw new IllegalArgumentException("Operand stack underflow");
                    for (int i = vals.length - 1; i >= 0; i--) {
                        vals[i] = s.pop();
                    }
                    s.push(m.invoke(null, vals));
                    break;
                }
                case INVOKEVIRTUAL: {
                    Class c = cmd.findClass(args[0]);
                    Class[] types = cmd.parseTypes(args[2]);
                    Method m = c.getDeclaredMethod(cmd.deSRG(c, args[1]), types);
                    m.setAccessible(true);
                    Object[] vals = new Object[types.length];
                    if (vals.length + 1 > s.size())
                        throw new IllegalArgumentException("Operand stack underflow");
                    for (int i = vals.length - 1; i >= 0; i--) {
                        vals[i] = s.pop();
                    }
                    s.push(m.invoke(removeTop ? s.pop() : s.peek(), vals));
                    break;
                }
                case OBJMETHOD: {
                    Object o = removeTop ? s.pop() : s.peek();
                    Method[] arr = o.getClass().getMethods();
                    Method call = null;
                    for (Method m : arr) {
                        if (m.getName().equals(args[0])) {
                            call = m;
                            break;
                        }
                    }
                    if (call == null) {
                        arr = o.getClass().getDeclaredMethods();
                        for (Method m : arr) {
                            if (m.getName().equals(args[0])) {
                                call = m;
                                break;
                            }
                        }
                    }
                    if (call == null)
                        throw new IllegalArgumentException("No such method");
                    call.setAccessible(true);
                    if (s.isEmpty())
                        throw new IllegalArgumentException("Operand stack underflow");
                    Object[] vals = new Object[call.getParameterCount()];
                    if (vals.length + 1 > s.size())
                        throw new IllegalArgumentException("Operand stack underflow");
                    for (int i = vals.length - 1; i >= 0; i--) {
                        vals[i] = s.pop();
                    }
                    s.push(call.invoke(o, vals));
                    break;
                }
                case LDC:
                    s.push(cmd.parseObject(args[0]));
                    break;
                case CONCAT:
                    String s2 = String.valueOf(s.pop());
                    String s1 = String.valueOf(s.pop());
                    s.push(s1 + s2);
                    break;
                case ITERATE:
                    Collection list = (Collection) s.pop();
                    for (Object o : list) {}
                    break;
                case NEW: {
                    Class c = cmd.findClass(args[0]);
                    Class[] types = cmd.parseTypes(args[1]);
                    Object[] vals = new Object[types.length];
                    if (vals.length > s.size())
                        throw new IllegalArgumentException("Operand stack underflow");
                    for (int i = vals.length - 1; i >= 0; i--) {
                        vals[i] = s.pop();
                    }
                    Constructor cn = c.getDeclaredConstructor(types);
                    cn.setAccessible(true);
                    Object o = cn.newInstance(vals);
                    s.push(o);
                    break;
                }
                case OUTPUT:
                    sendChatToSender(ics, "Current stack: [");
                    for (Object o : s)
                        sendChatToSender(ics, cmd.toReadableString(o));
                    sendChatToSender(ics, "]");
                    break;
                case WRITE:
                    try {
                        File f = new File(
                            DragonAPICore.getMinecraftDirectory(),
                            "/ByteCodeStackOut/" + args[0]
                        );
                        for (Object o : s)
                            cmd.writeObjectToFile(
                                f,
                                s.indexOf(o),
                                o,
                                args.length > 1 && args[1].equalsIgnoreCase("full")
                            );
                        sendChatToSender(
                            ics,
                            s.size() + " objects written to files in "
                                + f.getCanonicalPath()
                        );
                    } catch (IOException e) {
                        sendChatToSender(
                            ics, "Could not write output file: " + e.toString()
                        );
                        e.printStackTrace();
                    }
                    break;
                case DUP:
                    if (s.isEmpty())
                        throw new IllegalArgumentException("Operand stack underflow");
                    s.push(s.peek());
                    break;
                case POP:
                    if (s.isEmpty())
                        throw new IllegalArgumentException("Operand stack underflow");
                    s.pop();
                    break;
                case SWAP: {
                    if (s.size() < 2)
                        throw new IllegalArgumentException("Operand stack underflow");
                    Object top = s.pop();
                    Object o2 = s.pop();
                    s.push(top);
                    s.push(o2);
                    break;
                }
                case INSTANCEOF: {
                    if (s.size() < 2)
                        throw new IllegalArgumentException("Operand stack underflow");
                    Object obj = s.pop();
                    Object type = s.pop();
                    s.push(((Class) type).isAssignableFrom(obj.getClass()));
                    break;
                }
                case MAKEARRAY: {
                    int amt = Integer.parseInt(args[0]);
                    if (s.size() < amt)
                        throw new IllegalArgumentException("Operand stack underflow");
                    Object[] arr = new Object[amt];
                    for (int i = 0; i < arr.length; i++) {
                        arr[i] = s.pop();
                    }
                    ArrayUtils.reverse(arr); //to match stack
                    break;
                }
                case GETARRAY: {
                    if (s.size() < 2)
                        throw new IllegalArgumentException("Operand stack underflow");
                    Object[] arr = (Object[]) s.pop();
                    int idx = (int) s.pop();
                    s.push(arr[idx]);
                    break;
                }
                case SETARRAY: {
                    if (s.size() < 3)
                        throw new IllegalArgumentException("Operand stack underflow");
                    Object[] arr = (Object[]) s.pop();
                    int idx = (int) s.pop();
                    arr[idx] = s.pop();
                    break;
                }
            }
        }
    }

    static class BytecodeProgram {
        private final String programName;
        private final UUID programOwner;

        private final boolean allowAnyAdmin;
        private final boolean allowAnyUser;

        private final ArrayList<ByteCodeInstruction> instructions = new ArrayList();

        private final Stack stack = new Stack();

        private BytecodeProgram(EntityPlayerMP ep, String name, HashSet<String> args) {
            this(ep.getUniqueID(), name, args.contains("admin"), args.contains("public"));
        }

        private BytecodeProgram(UUID id, String name, boolean admin, boolean any) {
            programOwner = id;
            programName = name;

            allowAnyAdmin = admin;
            allowAnyUser = any;
        }

        private static BytecodeProgram readFile(File f) {
            List<String> li = ReikaFileReader.getFileAsLines(f, true, Charsets.UTF_8);
            String name = li.remove(0);
            String uid = li.remove(0);
            String admin = li.remove(0);
            String any = li.remove(0);
            String space = li.remove(0);

            BytecodeProgram p = new BytecodeProgram(
                UUID.fromString(uid),
                name,
                Boolean.parseBoolean(admin),
                Boolean.parseBoolean(any)
            );
            for (String s : li) {
                p.instructions.add(ByteCodeInstruction.parseInstruction(s));
            }
            return p;
        }

        private void run(EntityPlayerMP ep, BytecodeCommand c) throws Exception {
            for (ByteCodeInstruction p : instructions) {
                p.execute(ep, c, stack);
            }
        }

        private void save() throws IOException {
            File base = DragonAPICore.getMinecraftDirectory();
            File f = new File(base, PROGRAM_PATH + "/" + programName + ".byteprog");
            f.getParentFile().mkdirs();
            if (f.exists())
                f.delete();
            f.createNewFile();
            ArrayList<String> li = this.serializeProgram();
            this.prependHeader(li);
            ReikaFileReader.writeLinesToFile(f, li, true, Charsets.UTF_8);
        }

        private void prependHeader(ArrayList<String> li) {
            li.add(0, programName);
            li.add(1, programOwner.toString());
            li.add(2, String.valueOf(allowAnyAdmin));
            li.add(3, String.valueOf(allowAnyUser));
            li.add(4, "===========================");
        }

        private ArrayList<String> serializeProgram() {
            ArrayList<String> li = new ArrayList();
            for (ByteCodeInstruction p : instructions) {
                li.add(p.serialize());
            }
            return li;
        }

        @Override
        public String toString() {
            return instructions.toString();
        }

        static class ByteCodeInstruction {
            private final Opcodes operation;
            private final String[] arguments;

            private ByteCodeInstruction(Opcodes o, String... args) {
                operation = o;
                arguments = args;
            }

            private static ByteCodeInstruction parseInstruction(String s) {
                String[] parts = s.split(":");
                Opcodes o = Opcodes.valueOf(parts[0]);
                String[] args = parts[1].split("\\|");
                return new ByteCodeInstruction(o, args);
            }

            private String serialize() {
                String args = Arrays.toString(arguments).replace(", ", "|");
                args = args.substring(1, args.length() - 1);
                return operation.toString() + ":" + args;
            }

            private void execute(EntityPlayerMP ep, BytecodeCommand c, Stack s)
                throws Exception {
                operation.call(c, ep, arguments, s, true);
            }

            @Override
            public String toString() {
                return operation.toString() + " on " + Arrays.toString(arguments);
            }
        }
    }
}
