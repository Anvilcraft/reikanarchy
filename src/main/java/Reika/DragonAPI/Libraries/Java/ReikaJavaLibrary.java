package Reika.DragonAPI.Libraries.Java;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;

import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.Exception.MisuseException;
import Reika.DragonAPI.IO.ReikaFileReader;
import Reika.DragonAPI.Libraries.MathSci.ReikaMathLibrary;
import Reika.DragonAPI.ModList;
import com.google.common.reflect.ClassPath;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.commons.codec.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

public final class ReikaJavaLibrary extends DragonAPICore {
    private static int maxRecurse = -1;
    public static boolean dumpStack = false;
    public static boolean silent = false;

    private static final boolean printClasses
        = ReikaJVMParser.isArgumentPresent("-DragonAPI_printClassInit");

    private static final HashMap<String, Object> threadLock = new HashMap();

    private static final char[] IDChars = new char[] {
        '\u03b1', '\u03b2', '\u03b3', '\u03b4', '\u03b5', '\u03b6', '\u03b7', '\u03b8',
        '\u03b9', '\u03ba', '\u03bb', '\u03bc', '\u03bd', '\u03be', '\u03bf', '\u03c0',
        '\u03c1', '\u03c2', '\u03c3', '\u03c4', '\u03c5', '\u03c6', '\u03c7', '\u03c8',
        '\u03c9', '\u0393', '\u0394', '\u0398', '\u039b', '\u03a0', '\u03a3', '\u03a6',
        '\u03a8', '\u03a9', '\u0414', '\u0416', '\u0418', '\u0428', '\u042c', '\u042d',
        '\u042e', '\u042f', '\u05d0', '\u05d1', '\u05d2', '\u05d3', '\u05d4', '\u05d7',
        '\u05d8', '\u05da', '\u05db', '\u05dc', '\u05dd', '\u05de', '\u05e1', '\u05e2',
        '\u05e3', '\u05e4', '\u05e6', '\u05e7', '\u05e8', '\u05e9', '\u05ea'
    };

    public static final Comparator<Comparable> reverseComparator
        = new Comparator<Comparable>() {
              @Override
              public int compare(Comparable o1, Comparable o2) {
                  return o2.compareTo(o1);
              }
          };

    /** Generic write-to-console function. Args: Object */
    public static void pConsole(Object obj) {
        pConsole(Level.INFO, obj);
    }

    /** Generic write-to-console function. Args: Object */
    public static void pConsole(Level level, Object obj) {
        if (silent)
            return;
        if (obj == null) {
            writeLineToConsoleAndLogs(level, "null arg");
        } else {
            Class cl = obj.getClass();
            //if (obj instanceof Object[]) {
            //	writeLineToConsoleAndLogs(level, Arrays.toString((Object[])obj));
            //}
            if (cl != String.class && cl != Integer.class && cl != Boolean.class)
                writeLineToConsoleAndLogs(
                    level, String.valueOf(obj) + " of " + String.valueOf(cl)
                );
            else
                writeLineToConsoleAndLogs(level, String.valueOf(obj));
        }
        if (dumpStack)
            dumpStack();
    }

    public static void dumpStack() {
        writeLineToConsoleAndLogs(Level.WARN, "Stack Trace:");
        StackTraceElement[] s = new Exception("Stack Trace").getStackTrace();
        for (int i = 1; i < s.length; i++)
            writeLineToConsoleAndLogs(Level.WARN, "\t" + s[i].toString());
    }

    private static void writeLineToConsoleAndLogs(Level level, String s) {
        //System.out.println(s);
        s = s.replaceAll("%", "%%"); //because FML logger fails at this
        FMLLog.log(level, s);
    }

    public static void spamConsole(Object obj) {
        String sg = String.valueOf(obj);
        for (int i = 0; i < 16; i++)
            pConsole(sg);
    }

    public static void writeCoord(World world, int x, int y, int z) {
        pConsole(
            world.getBlock(x, y, z) + ":" + world.getBlockMetadata(x, y, z) + " @ " + x
            + ", " + y + ", " + z + " @DIM" + world.provider.dimensionId
        );
    }

    public static void pConsole(Object obj, Side s) {
        if (FMLCommonHandler.instance().getEffectiveSide() == s)
            pConsole(obj);
    }

    public static void pConsole(Object obj, Side s, boolean con) {
        if (con)
            pConsole(obj, s);
    }

    public static void pConsole(Object obj, boolean con) {
        if (con)
            pConsole(obj);
    }

    /**
     * A complement to Java's built-in List-to-Array. Args: Array of any object (ints,
     * strings, etc).
     */
    public static <E> HashSet<E> makeSetFromArray(E[] obj) {
        HashSet li = new HashSet();
        for (int i = 0; i < obj.length; i++) {
            li.add(obj[i]);
        }
        return li;
    }

    /**
     * A complement to Java's built-in List-to-Array. Args: Array of any object (ints,
     * strings, etc).
     */
    public static <E> ArrayList makeListFromArray(E[] obj) {
        ArrayList<E> li = new ArrayList();
        for (int i = 0; i < obj.length; i++) {
            li.add(obj[i]);
        }
        return li;
    }

    public static ArrayList<Integer> makeIntListFromArray(int... obj) {
        ArrayList li = new ArrayList();
        for (int i = 0; i < obj.length; i++) {
            li.add(obj[i]);
        }
        return li;
    }

    public static ArrayList<Byte> makeIntListFromArray(byte... obj) {
        ArrayList li = new ArrayList();
        for (int i = 0; i < obj.length; i++) {
            li.add(obj[i]);
        }
        return li;
    }

    public static <E> ArrayList<E> makeListFrom(E obj) {
        ArrayList<E> li = new ArrayList();
        li.add(obj);
        return li;
    }

    public static <E> ArrayList makeListFrom(E... obj) {
        ArrayList<E> li = new ArrayList();
        for (int i = 0; i < obj.length; i++) {
            li.add(obj[i]);
        }
        return li;
    }

    public static boolean isValidInteger(String s) {
        if (s.contentEquals("-"))
            return true;
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static int safeIntParse(String s) {
        boolean neg = false;
        int num = 0;
        if (s.startsWith("-")) {
            s = s.substring(1);
            neg = true;
        }
        if (s.matches("\\d+")) {
            num = Integer.parseInt(s);
        }
        return neg ? -num : num;
    }

    public static long safeLongParse(String s) {
        boolean neg = false;
        long num = 0;
        if (s.startsWith("-")) {
            s = s.substring(1);
            neg = true;
        }
        if (s.matches("\\d+")) {
            num = Long.parseLong(s);
        }
        return neg ? -num : num;
    }

    public static void printLine(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++)
            sb.append("-");
        pConsole(sb.toString());
    }

    public static <T, E> T getHashMapKeyByValue(HashMap<T, E> map, E value) {
        for (Entry<T, E> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static boolean doesClassExist(String cl) {
        try {
            Class.forName(cl, false, Thread.currentThread().getContextClassLoader());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        } catch (NoClassDefFoundError e) {
            return false;
        }
    }

    public static Class getClassNoException(String cl) {
        try {
            return Class.forName(cl);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static void initClass(String c) {
        try {
            Class.forName(c, true, ReikaJavaLibrary.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            pConsole("DRAGONAPI: Failed to initalize class " + c + "! Class not found!");
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            pConsole("DRAGONAPI: Failed to initalize class " + c + "! Class not found!");
            e.printStackTrace();
        } catch (LinkageError e) {
            pConsole("DRAGONAPI: Failed to initalize class " + c + "! Class not found!");
            e.printStackTrace();
        } catch (RuntimeException e) {
            pConsole("DRAGONAPI: Failed to initalize class " + c + "!");
            String s = e.getMessage();
            if (s.endsWith("for invalid side SERVER")) {
                pConsole(
                    "DRAGONAPI: Attemped to load a clientside class on the server! This is a significant programming error!"
                );
            }
            e.printStackTrace();
        }
    }

    /** Initializes a class. */
    public static void initClass(Class c, boolean errorIfFail) {
        if (printClasses)
            printClassMetadata(c);
        if (c == null) {
            pConsole("DRAGONAPI: Cannot initalize a null class!");
            dumpStack();
            return;
        }
        try {
            pConsole(
                "DRAGONAPI: Initializing class " + c.getName() + " on classloader "
                + Thread.currentThread().getContextClassLoader()
            );
            Class.forName(c.getName());
        } catch (ClassNotFoundException e) {
            if (errorIfFail)
                throw new RuntimeException(e);
            pConsole(
                "DRAGONAPI: Failed to initalize class " + c.getName()
                + "! Class not found!"
            );
            e.printStackTrace();
            printClassMetadata(c);
            //printClassASM(c); //, "ErroredClasses/"
        } catch (NoClassDefFoundError e) {
            pConsole(
                "DRAGONAPI: Failed to initalize class " + c.getName()
                + "! Class not found!"
            );
            e.printStackTrace();
            printClassMetadata(c);
            //printClassASM(c);
        } catch (LinkageError e) {
            pConsole(
                "DRAGONAPI: Failed to initalize class " + c.getName()
                + "! Class not found!"
            );
            e.printStackTrace();
            printClassMetadata(c);
            //printClassASM(c);
        } catch (RuntimeException e) {
            pConsole("DRAGONAPI: Failed to initalize class " + c.getName() + "!");
            String s = e.getMessage();
            if (s.endsWith("for invalid side SERVER")) {
                pConsole(
                    "DRAGONAPI: Attemped to load a clientside class on the server! This is a significant programming error!"
                );
            }
            e.printStackTrace();
        }
    }

    public static void printClassSource(Class c, String path) {
        printClassSource(path + c.getName(), getClassBytes(c));
    }

    public static void printClassMetadata(Class c) {
        printClassMetadata(c.getName(), c);
    }

    public static void printClassASM(Class c) {
        printClassASM(c.getName(), getClassBytes(c));
    }

    public static void printClassSource(Class c) {
        printClassSource(c.getName(), getClassBytes(c));
    }

    public static byte[] getClassBytes(Class c) {
        String className = c.getName();
        String classAsPath = className.replace('.', '/') + ".class";
        InputStream stream = c.getClassLoader().getResourceAsStream(classAsPath);
        try {
            return IOUtils.toByteArray(stream);
        } catch (IOException e) {
            pConsole("DRAGONAPI: Error converting class to byte[]!");
            e.printStackTrace();
            return new byte[0];
        }
    }

    public static void printClassMetadata(String path, Class c) {
        String filename = "FailedClasses/" + path + ".classdata";
        File f = new File(filename);
        f.getParentFile().mkdirs();
        if (!printClassMetadata(f, c)) {
            pConsole("DRAGONAPI: Error printing class data!");
        }
    }

    private static boolean printClassMetadata(File f, Class c) {
        ArrayList<String> li = new ArrayList();
        printClassMetadata(li, c);
        return ReikaFileReader.writeLinesToFile(f, li, true, Charsets.UTF_8);
    }

    private static void printClassMetadata(ArrayList<String> li, Class c) {
        try {
            li.add("General:");
            li.add("\t" + c.getName());
            li.add("\tAnnotations: " + Arrays.toString(c.getAnnotations()));
            li.add("\tModifiers: " + parseModifiers(c.getModifiers()));
            li.add("\tSuperclass: " + c.getSuperclass());
            li.add("\tInterfaces: " + Arrays.toString(c.getInterfaces()));
            li.add("\tSynthetic: " + c.isSynthetic());
            li.add("");
        } catch (Throwable t) {
            li.add("ERRORED ROOT DATA");
            t.printStackTrace();
        }

        try {
            li.add("Internal Classes:");
            for (Class cs : c.getDeclaredClasses()) {
                try {
                    li.add("-------------------------");
                    printClassMetadata(li, cs);
                    li.add("-------------------------");
                } catch (Throwable t) {
                    li.add("ERRORED INTERNAL CLASS");
                    t.printStackTrace();
                }
            }
            li.add("");
        } catch (Throwable t) {
            t.printStackTrace();
        }

        try {
            li.add("Constructors:");
            for (Constructor cs : c.getDeclaredConstructors()) {
                try {
                    li.add("\t\tAnnotations: " + Arrays.toString(cs.getAnnotations()));
                    li.add("\t\tModifiers: " + parseModifiers(cs.getModifiers()));
                    li.add("\t\tSignature: " + Arrays.toString(cs.getParameterTypes()));
                    li.add("\t\tExceptions: " + Arrays.toString(cs.getExceptionTypes()));
                    li.add("\t\tSynthetic: " + cs.isSynthetic() + "");
                } catch (Throwable t) {
                    li.add("ERRORED CONSTRUCTOR");
                    t.printStackTrace();
                }
            }
            li.add("");
        } catch (Throwable t) {
            t.printStackTrace();
        }

        try {
            li.add("Fields:");
            for (Field fd : c.getDeclaredFields()) {
                try {
                    li.add("\t" + fd.getName());
                    li.add("\t\tAnnotations: " + Arrays.toString(fd.getAnnotations()));
                    li.add("\t\tModifiers: " + parseModifiers(fd.getModifiers()));
                    li.add("\t\tType: " + fd.getType());
                    li.add("\t\tSynthetic: " + fd.isSynthetic() + "");
                } catch (Throwable t) {
                    li.add("ERRORED FIELD");
                    t.printStackTrace();
                }
            }
            li.add("");
        } catch (Throwable t) {
            t.printStackTrace();
        }

        try {
            li.add("Methods:");
            for (Method m : c.getDeclaredMethods()) {
                try {
                    li.add("\t" + m.getName());
                    li.add("\t\tAnnotations: " + Arrays.toString(m.getAnnotations()));
                    li.add("\t\tModifiers: " + parseModifiers(m.getModifiers()));
                    li.add("\t\tSignature: " + Arrays.toString(m.getParameterTypes()));
                    li.add("\t\tExceptions: " + Arrays.toString(m.getExceptionTypes()));
                    li.add("\t\tReturn: " + m.getReturnType());
                    li.add("\t\tSynthetic: " + m.isSynthetic() + "");
                } catch (Throwable t) {
                    li.add("ERRORED METHOD");
                    t.printStackTrace();
                }
            }
            li.add("");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static String parseModifiers(int modifiers) {
        ArrayList<String> params = new ArrayList();
        for (ClassModifiers mod : ClassModifiers.values()) {
            if (mod.match(modifiers)) {
                params.add(mod.toString());
            }
        }
        return params.toString();
    }

    public static boolean printClassASM(String path, byte[] data) {
        ClassReader reader = new ClassReader(data);
        ClassNode classNode = new ClassNode();
        reader.accept(classNode, 0);
        final List<MethodNode> methods = classNode.methods;
        String filename = path + ".asm";
        File f = new File(filename);
        ArrayList<String> p = new ArrayList();
        for (MethodNode m : methods) {
            InsnList inList = m.instructions;
            p.add(m.name);
            for (int i = 0; i < inList.size(); i++) {
                p.add(ReikaASMHelper.clearString(inList.get(i)));
            }
        }
        return ReikaFileReader.writeLinesToFile(f, p, true, Charsets.UTF_8);
    }

    public static void printClassSource(String path, byte[] data) {
        //read in, build classNode
        /*
        ClassNode classNode = new ClassNode();
        ClassReader cr = new ClassReader(data);
        cr.accept(classNode, 0);

        //peek at classNode and modifier
        List<MethodNode> methods = classNode.methods;
        for (MethodNode method : methods) {
            System.out.println("name = "+method.name+" desc = "+method.desc);
            InsnList insnList = method.instructions;
            Iterator ite = insnList.iterator();

            while(ite.hasNext()) {
                AbstractInsnNode insn = (AbstractInsnNode)ite.next();
                int opcode = insn.getOpcode();
                //add before return: System.out.println("Returning ... ")
                if (opcode == Opcodes.RETURN) {
                    InsnList tempList = new InsnList();
                    tempList.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System",
        "out", "Ljava/io/PrintStream;")); tempList.add(new LdcInsnNode("Returning ... "));
                    tempList.add(new
        MethodInsnNode(Opcodes.INVOKEVIRTUAL,"java/io/PrintStream","println",
        "(Ljava/lang/String;)V")); insnList.insert(insn.getPrevious(), tempList);
                    method.maxStack += 2;
                }
            }
        }

        //write classNode
        ClassWriter out = new ClassWriter(0);
        classNode.accept(out);
        data = out.toByteArray() */

        String filename = path + ".class";
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(data);
        } catch (IOException e) {
            pConsole("DRAGONAPI: Error printing class!");
            e.printStackTrace();
        }
    }

    public static void initClassWithSubs(Class c, boolean errorIfFail) {
        initClass(c, errorIfFail);
        for (Class c2 : c.getDeclaredClasses()) {
            initClass(c2, errorIfFail);
        }
    }

    public static boolean listContainsArray(List<int[]> li, int[] arr) {
        for (int i = 0; i < li.size(); i++) {
            if (Arrays.equals(li.get(i), arr)) {
                return true;
            }
        }
        return false;
    }

    public static int getEnumLengthWithoutInitializing(Class<? extends Enum> c) {
        Field[] q = c.getFields();
        int count = 0;
        for (int i = 0; i < q.length; i++) {
            Field f = q[i];
            if (f.isEnumConstant())
                count++;
        }
        return count;
    }

    public static ArrayList<String>
    getEnumEntriesWithoutInitializing(Class<? extends Enum> c) {
        ArrayList<String> li = new ArrayList();
        Field[] q = c.getFields();
        for (int i = 0; i < q.length; i++) {
            Field f = q[i];
            if (f.isEnumConstant())
                li.add(f.getName());
        }
        return li;
    }

    /**
     * Returns the maximum allowable depth of recursion on the current system.
     * Keep in mind that this number is the <i>total</i> stack depth and as such contains
     * some vanilla MC and Forge calls as well. Subtract 100 or so to be safe.
     */
    public static int getMaximumRecursiveDepth() {
        if (maxRecurse <= 0) {
            recurse(0);
        }
        return maxRecurse;
    }

    private static int recurse(int i) {
        maxRecurse = Math.max(i, maxRecurse);
        //pConsole(i+":"+maxRecurse);
        try {
            recurse(i + 1);
        } catch (StackOverflowError e) {
            return i;
        }
        return 0;
    }

    public static void toggleStackTrace() {
        dumpStack = !dumpStack;
    }

    public static void toggleSilentMode() {
        silent = !silent;
    }

    public static Class[] getObjectClasses(Object... objs) {
        Class[] c = new Class[objs.length];
        for (int i = 0; i < c.length; i++) {
            c[i] = getClassOf(objs[i]);
        }
        return c;
    }

    public static Class getClassOf(Object o) {
        Class p = getPrimitiveClass(o);
        return p != null ? p : o.getClass();
    }

    private static Class getPrimitiveClass(Object o) {
        String name = o.getClass().getSimpleName().toLowerCase(Locale.ENGLISH);
        if (name.equals("byte"))
            return byte.class;
        if (name.equals("short"))
            return short.class;
        if (name.equals("int"))
            return int.class;
        if (name.equals("integer"))
            return int.class;
        if (name.equals("long"))
            return long.class;
        if (name.equals("char"))
            return char.class;
        if (name.equals("character"))
            return char.class;
        if (name.equals("float"))
            return float.class;
        if (name.equals("double"))
            return double.class;
        if (name.equals("boolean"))
            return boolean.class;
        if (name.equals("void"))
            return void.class;
        return null;
    }

    public static <K, T>
        boolean collectionMapContainsValue(Map<K, Collection<T>> map, T value) {
        for (Collection<T> c : map.values()) {
            if (c != null && c.contains(value))
                return true;
        }
        return false;
    }

    public static byte[] streamToBytes(InputStream in) {
        ArrayList<Byte> li = new ArrayList();
        try {
            int ret = in.read();
            while (ReikaMathLibrary.isValueInsideBoundsIncl(0, 255, ret)) {
                li.add((byte) ret);
                ret = in.read();
            }
            byte[] arr = new byte[li.size()];
            for (int i = 0; i < li.size(); i++) {
                arr[i] = li.get(i);
            }
            return arr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <I, O> Collection<O>
    getConstructedCollection(Collection<I> inputs, Class<I> ci, Class<O> co) {
        try {
            return getConstructedCollection(inputs, co.getConstructor(ci));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <I, O> Collection<O>
    getConstructedCollection(Collection<I> inputs, Constructor<O> c) {
        Collection<O> outputs = new ArrayList();
        try {
            for (I in : inputs) {
                O out = c.newInstance(in);
                outputs.add(out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputs;
    }

    public static HashMap sortMapByValues(HashMap map) {
        List<Map.Entry> list = new LinkedList(map.entrySet());
        Collections.sort(list, new MapValueSorter());
        HashMap sortedHashMap = new LinkedHashMap();
        for (Map.Entry e : list) {
            sortedHashMap.put(e.getKey(), e.getValue());
        }
        return sortedHashMap;
    }

    private static class MapValueSorter<V>
        implements Comparator<Map.Entry<V, Comparable>> {
        public int compare(Map.Entry<V, Comparable> o1, Map.Entry<V, Comparable> o2) {
            return (o1.getValue()).compareTo(o2.getValue());
        }
    }

    public static <E> E[] collectionToArray(Collection<E> li) {
        E[] arr = (E[]) new Object[li.size()];
        int i = 0;
        for (E ps : li) {
            arr[i] = ps;
            i++;
        }
        return arr;
    }

    public static <E> E getRandomListEntry(Random rand, List<E> li) {
        return li.isEmpty() ? null : li.get(rand.nextInt(li.size()));
    }

    public static <E> E getRandomCollectionEntry(Random rand, Collection<E> c) {
        return c.isEmpty()      ? null
            : c instanceof List ? getRandomListEntry(rand, (List<E>) c)
                                : getRandomListEntry(rand, new ArrayList<E>(c));
    }

    public static <E> E getAndRemoveRandomCollectionEntry(Random rand, Collection<E> c) {
        if (c instanceof List) {
            return ((List<E>) c).remove(rand.nextInt(c.size()));
        }
        E val = getRandomCollectionEntry(rand, c);
        c.remove(val);
        return val;
    }

    public static String getTopLevelPackage(Class c) {
        String n = c.getName();
        return n.substring(0, n.indexOf('.'));
    }

    public static <E> Collection<E> getCompoundCollection(Collection<Collection<E>> colls
    ) {
        Collection<E> c = new ArrayList();
        for (Collection<E> c2 : colls) {
            c.addAll(c2);
        }
        return c;
    }

    public static boolean isAnyModLoaded(ModList[] mods) {
        for (int i = 0; i < mods.length; i++) {
            if (mods[i].isLoaded())
                return true;
        }
        return false;
    }

    public static boolean removeValuesFromMap(Map map, Object value) {
        boolean flag = false;
        while (map.values().contains(value)) {
            flag |= map.values().remove(value);
        }
        return flag;
    }

    public static int getNestedMapSize(Map map) {
        int s = 0;
        for (Object k : map.keySet()) {
            Object o = map.get(k);
            s += o instanceof Map ? ((Map) o).size() : 1;
        }
        return s;
    }

    public static <V> Collection<V> getValuesForMapOfMaps(Map<?, Map<?, V>> map) {
        Collection<V> li = new ArrayList();
        for (Object k : map.keySet()) {
            Map<?, V> o = map.get(k);
            li.addAll(o.values());
        }
        return li;
    }

    public static <E> Collection<E> getValuesForMapOfCollections(Map<?, Collection<E>> map
    ) {
        Collection<E> li = new ArrayList();
        for (Object k : map.keySet()) {
            Collection<E> o = map.get(k);
            li.addAll(o);
        }
        return li;
    }

    public static void cycleList(List li, int n) {
        if (li.isEmpty())
            return;
        boolean neg = n < 0;
        n = Math.abs(n);
        for (int i = 0; i < n; i++) {
            if (neg) {
                Object o = li.remove(0);
                li.add(o);
            } else {
                Object o = li.remove(li.size() - 1);
                li.add(0, o);
            }
        }
    }

    public static void cycleLinkedList(LinkedList li, int n) {
        if (li.isEmpty())
            return;
        boolean neg = n < 0;
        n = Math.abs(n);
        for (int i = 0; i < n; i++) {
            if (neg) {
                Object o = li.removeFirst();
                li.addLast(o);
            } else {
                Object o = li.removeLast();
                li.addFirst(o);
            }
        }
    }

    public static <V> Collection<V> combineCollections(Collection<V>... colls) {
        Collection<V> ret = new ArrayList();
        for (int i = 0; i < colls.length; i++) {
            ret.addAll(colls[i]);
        }
        return ret;
    }

    public static <E> Set<E> getSet(E... elements) {
        return new HashSet(Arrays.asList(elements));
    }

    /** NOT PERFORMANT */
    public static Thread getThreadByName(String name) {
        ThreadGroup tg = getTopLevelThreadGroup();
        Thread[] all = new Thread[tg.activeCount()];
        tg.enumerate(all, true);
        for (int i = 0; i < all.length; i++) {
            Thread t = all[i];
            String n = t.getName();
            if (n != null && n.equals(name)) {
                return t;
            }
        }
        return null;
    }

    public static ThreadGroup getTopLevelThreadGroup() {
        ThreadGroup tg = Thread.currentThread().getThreadGroup();
        while (tg.getParent() != null) {
            tg = tg.getParent();
        }
        return tg;
    }

    public static boolean exceptionMentions(Throwable e, Class c) {
        StackTraceElement[] arr = e.getStackTrace();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].getClassName().equals(c.getName()))
                return true;
        }
        return false;
    }

    public static <K> void subtractFromIntMap(Map<K, Integer> map, K key, int num) {
        Integer get = map.get(key);
        int ret = (get != null ? get.intValue() : 0) - num;
        map.put(key, ret);
    }

    public static <K> void addToIntMap(Map<K, Integer> map, K key, int num) {
        Integer get = map.get(key);
        int ret = (get != null ? get.intValue() : 0) + num;
        map.put(key, ret);
    }

    /**
     * Not exactly performant. Do not call this excessively, or ever during the main game
     * loop.
     */
    public static Collection<Class> getAllClassesFromPackage(
        String pack, Class parent, boolean skipAbstract, boolean skipDeprecated
    ) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Collection<Class> li = new ArrayList();
        for (ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClasses()) {
            if (info.getName().startsWith(pack)) {
                Class c = info.load();
                if (parent == null || parent.isAssignableFrom(c)) {
                    if (skipAbstract && (c.getModifiers() & Modifier.ABSTRACT) != 0)
                        continue;
                    if (skipDeprecated && c.isAnnotationPresent(Deprecated.class))
                        continue;
                    li.add(c);
                }
            }
        }
        return li;
    }

    /**
     * Starts at lowercase alpha, goes through entire alphabet, reuses some as caps,
     * moves onto Cryllic, then Hebrew.
     */
    public static char getIDChar(int id) {
        return IDChars[id % IDChars.length];
    }

    public static int[] splitLong(long val) {
        int l1 = (int) (val >>> 32);
        int l2 = (int) (val & 0xFFFFFFFFL);
        return new int[] { l1, l2 };
    }

    public static long buildLong(int l1, int l2) {
        return ((long) l1 << 32) | (l2 & 0xffffffffL);
    }

    public static byte[] splitInt(int val) {
        byte[] ret = new byte[4];
        ret[0] = (byte) ((val) & 255);
        ret[1] = (byte) ((val >>> 8) & 255);
        ret[2] = (byte) ((val >>> 16) & 255);
        ret[3] = (byte) ((val >>> 24) & 255);
        return ret;
    }

    public static int buildInt(byte b1, byte b2, byte b3, byte b4) {
        return (b1 & 255) | ((b2 & 255) << 8) | ((b3 & 255) << 16) | ((b4 & 255) << 24);
    }

    public static String getClassLocation(Class c) {
        String ret = c.getResource(c.getSimpleName() + ".class").toString();
        ret = ret.substring("file:\\".length());
        ret = ret.replaceAll("%20", " ");
        return ret;
    }

    public static double buildDoubleFromInts(int i1, int i2) {
        /*
        byte[] arr = new byte[8];
        ByteBuffer buf = ByteBuffer.wrap(arr);
        buf.putInt(i1);
        buf.putInt(i2);
        return ByteBuffer.wrap(arr).getDouble();
         */
        return Double.longBitsToDouble(buildLong(i1, i2));
    }

    public static int[] splitDoubleToInts(double val) {
        /*
        byte[] arr = new byte[8];
        ByteBuffer.wrap(arr).putDouble(val);
        ByteBuffer buf = ByteBuffer.wrap(arr);
        int i1 = buf.getInt();
        int i2 = buf.getInt();
        return new int[]{i1, i2};
         */
        return splitLong(Double.doubleToRawLongBits(val));
    }

    public static byte[] splitIntToHexChars(int val) {
        byte[] arr = new byte[8];
        for (int i = 0; i < 8; i++) {
            byte hex = (byte) ((val >> (i * 4)) & 0xF);
            arr[i] = hex;
        }
        return arr;
    }

    public static int buildIntFromHexChars(byte[] chars) {
        if (chars.length != 8)
            throw new MisuseException(
                "You cannot build an int from less than or more than 8 nibbles!"
            );
        int val = 0;
        for (int i = 0; i < 8; i++) {
            val |= chars[i] << (i * 4);
        }
        return val;
    }

    public static <V> ArrayList<V>
    makeSortedListFromCollection(Collection<V> c, Class baseElementType) {
        ArrayList<V> li = new ArrayList(c);
        if (!li.isEmpty()) {
            Collections.sort(li, new FallbackComparator(baseElementType));
        }
        return li;
    }

    private static class FallbackComparator implements Comparator {
        private final boolean useDefaultCompare;

        private FallbackComparator(Class test) {
            useDefaultCompare = Comparable.class.isAssignableFrom(test);
        }

        @Override
        public int compare(Object o1, Object o2) {
            if (useDefaultCompare)
                return ((Comparable) o1).compareTo(o2);
            else
                return Integer.compare(o1.hashCode(), o2.hashCode());
        }
    }

    public static byte flipBits(byte get) {
        return (byte) (Integer.reverse(get) >>> 24);
    }

    public static <K, V> HashMap<K, V> makeMapOf(K key, V val) {
        HashMap<K, V> map = new HashMap();
        map.put(key, val);
        return map;
    }

    public static <E> Collection<E>
    cloneCollectionObjects(Collection<E> c, CloneCallback<E> call) {
        Collection<E> ret = new ArrayList();
        for (E o : c) {
            ret.add(call.clone(o));
        }
        return ret;
    }

    public static interface CloneCallback<E> {
        E clone(E o);
    }

    /** Order agnostic */
    public static <E>
        boolean collectionsHaveSameValues(Collection<E> c1, Collection<E> c2) {
        return new HashSet(c1).equals(new HashSet(c2));
    }

    public static Object copyObject(Object o) {
        if (o instanceof Object[])
            return ReikaArrayHelper.deepCopyArray((Object[]) o);
        if (o instanceof ArrayList)
            return new ArrayList((ArrayList) o);
        if (o instanceof LinkedList)
            return new LinkedList((LinkedList) o);
        if (o instanceof HashMap)
            return new HashMap((HashMap) o);
        if (o instanceof TreeMap)
            return new TreeMap((TreeMap) o);
        if (o instanceof HashSet)
            return new HashSet((HashSet) o);
        if (o instanceof Map)
            return new HashMap((Map) o);
        if (o instanceof Collection)
            return new ArrayList((Collection) o);
        if (o instanceof ItemStack)
            return ((ItemStack) o).copy();
        return o;
    }

    public static String getPackageName(Class c) {
        String name = c.getName();
        int idx = name.lastIndexOf('.');
        return name.substring(0, idx);
    }

    public static String getEnumNameList(Class<? extends Enum> e) {
        StringBuilder sb = new StringBuilder();
        Enum[] list = e.getEnumConstants();
        for (int i = 0; i < list.length; i++) {
            Enum loc = list[i];
            sb.append(loc.name());
            if (i < list.length - 1)
                sb.append(", ");
        }
        return sb.toString();
    }

    public static <E extends Enum>
        EnumSet getConditionalEnumSet(Class<E> cl, Predicate<E> filter) {
        EnumSet set = EnumSet.noneOf(cl);
        for (E e : cl.getEnumConstants()) {
            if (filter.test(e))
                set.add(e);
        }
        return set;
    }

    public static <E> void removeDuplicates(Collection<E> li) {
        HashSet<E> encountered = new HashSet();
        Iterator<E> it = li.iterator();
        while (it.hasNext()) {
            E e = it.next();
            if (encountered.contains(e))
                it.remove();
            else
                encountered.add(e);
        }
    }

    public static <K> Function<K, Double> createConstFunction(double val) {
        return (key) -> val;
    }
}
