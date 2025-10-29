package Reika.DragonAPI.Libraries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import Reika.DragonAPI.DragonAPICore;
import Reika.DragonAPI.Exception.MisuseException;
import Reika.DragonAPI.Instantiable.Data.KeyedItemStack;
import Reika.DragonAPI.Libraries.Java.ReikaStringParser;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public final class ReikaNBTHelper extends DragonAPICore {
    private static final HashMap<Class, EnumIO> enumIOMap = new HashMap();

    /** Saves an inventory to NBT. Args: Inventory, NBT Tag */
    public static void writeInvToNBT(ItemStack[] inv, NBTTagCompound NBT) {
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < inv.length; i++) {
            if (inv[i] != null) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte) i);
                inv[i].writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }

        NBT.setTag("Items", nbttaglist);
    }

    /** Reads an inventory from NBT. Args: NBT Tag */
    public static ItemStack[] getInvFromNBT(NBTTagCompound NBT) {
        NBTTagList nbttaglist = NBT.getTagList("Items", NBTTypes.COMPOUND.ID);
        ItemStack[] inv = new ItemStack[nbttaglist.tagCount()];

        for (int i = 0; i < nbttaglist.tagCount(); i++) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            byte byte0 = nbttagcompound.getByte("Slot");

            if (byte0 >= 0 && byte0 < inv.length) {
                inv[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound);
            }
        }
        return inv;
    }

    public static Fluid getFluidFromNBT(NBTTagCompound NBT) {
        String name = NBT.getString("liquid");
        if (name == null || name.isEmpty() || name.equals("empty"))
            return null;
        String repl = ReikaFluidHelper.getFluidNameSwap(name);
        if (repl != null && FluidRegistry.getFluid(repl) != null)
            name = repl;
        return FluidRegistry.getFluid(name);
    }

    public static void writeFluidToNBT(NBTTagCompound NBT, Fluid f) {
        String name = f != null ? f.getName() : "empty";
        NBT.setString("liquid", name);
    }

    public static Object getValue(NBTBase NBT) {
        return getValue(NBT, null);
    }

    public static Object getValue(NBTBase NBT, NBTIO converter) {
        if (converter != null && converter.acceptsTag(NBT)) {
            return converter.createFromNBT(NBT);
        } else if (NBT instanceof NBTTagInt) {
            return ((NBTTagInt) NBT).func_150287_d();
        } else if (NBT instanceof NBTTagByte) {
            return ((NBTTagByte) NBT).func_150290_f();
        } else if (NBT instanceof NBTTagShort) {
            return ((NBTTagShort) NBT).func_150289_e();
        } else if (NBT instanceof NBTTagLong) {
            return ((NBTTagLong) NBT).func_150291_c();
        } else if (NBT instanceof NBTTagFloat) {
            return ((NBTTagFloat) NBT).func_150288_h();
        } else if (NBT instanceof NBTTagDouble) {
            return ((NBTTagDouble) NBT).func_150286_g();
        } else if (NBT instanceof NBTTagIntArray) {
            return ((NBTTagIntArray) NBT).func_150302_c();
        } else if (NBT instanceof NBTTagString) {
            return ((NBTTagString) NBT).func_150285_a_();
        } else if (NBT instanceof NBTTagByteArray) {
            return ((NBTTagByteArray) NBT).func_150292_c();
        } else if (NBT instanceof NBTTagCompound) {
            if (((NBTTagCompound) NBT).getBoolean("flag_isItemStack")) {
                return ItemStack.loadItemStackFromNBT((NBTTagCompound) NBT);
            } else {
                HashMap<String, Object> map = new HashMap();
                NBTTagCompound tag = (NBTTagCompound) NBT;
                for (Object o : tag.func_150296_c()) {
                    String s = (String) o;
                    map.put(s, getValue(tag.getTag(s), converter));
                }
                return map;
            }
        } else if (NBT instanceof NBTTagList) {
            ArrayList li = new ArrayList();
            for (Object o : ((NBTTagList) NBT).tagList) {
                li.add(getValue((NBTBase) o, converter));
            }
            return li;
        } else {
            return null;
        }
    }

    public static NBTBase getTagForObject(Object o) {
        return getTagForObject(o, null);
    }

    public static NBTBase getTagForObject(Object o, NBTIO converter) {
        if (converter != null && converter.acceptsType(o)) {
            return converter.convertToNBT(o);

        } else if (o instanceof Integer || o.getClass() == int.class) {
            return new NBTTagInt((Integer) o);

        } else if (o instanceof Byte || o.getClass() == byte.class) {
            return new NBTTagByte((Byte) o);

        } else if (o instanceof Short || o.getClass() == Short.class) {
            return new NBTTagShort((Short) o);

        } else if (o instanceof Long || o.getClass() == Long.class) {
            return new NBTTagLong((Long) o);

        } else if (o instanceof Float || o.getClass() == Float.class) {
            return new NBTTagFloat((Float) o);

        } else if (o instanceof Double || o.getClass() == Double.class) {
            return new NBTTagDouble((Double) o);
        } else if (o instanceof int[]) {
            return new NBTTagIntArray((int[]) o);

        } else if (o instanceof String || o.getClass() == String.class) {
            return new NBTTagString((String) o);
        } else if (o instanceof byte[]) {
            return new NBTTagByteArray((byte[]) o);
        } else if (o instanceof Map) {
            NBTTagCompound tag = new NBTTagCompound();
            Map m = (Map) o;
            for (Object k : m.keySet()) {
                if (k instanceof String) {
                    tag.setTag((String) k, getTagForObject(m.get(k), converter));
                }
            }
            return tag;
        } else if (o instanceof List || o instanceof Set) {
            NBTTagList li = new NBTTagList();
            for (Object o2 : ((Collection) o)) {
                li.appendTag(getTagForObject(o2, converter));
            }
            return li;
        } else if (o instanceof NBTBase) {
            return (NBTBase) o;
        } else if (o instanceof ItemStack) {
            NBTTagCompound tag = ((ItemStack) o).writeToNBT(new NBTTagCompound());
            tag.setBoolean("flag_isItemStack", true);
            return tag;
        } else {
            return null;
        }
    }

    public static boolean isIntNumberTag(NBTBase tag) {
        return tag instanceof NBTTagInt || tag instanceof NBTTagByte
            || tag instanceof NBTTagShort || tag instanceof NBTTagLong;
    }

    public static boolean isNumberTag(NBTBase tag) {
        return isIntNumberTag(tag) || tag instanceof NBTTagFloat
            || tag instanceof NBTTagDouble;
    }

    public static NBTBase compressNumber(NBTBase tag) {
        if (!isIntNumberTag(tag))
            throw new MisuseException(
                "Only integer-type numbers (byte, short, int, and long) can be compressed!"
            );
        long value = (Long) getValue(tag);
        if (value > Integer.MAX_VALUE) {
            return new NBTTagLong(value);
        } else if (value > Short.MAX_VALUE) {
            return new NBTTagInt((int) value);
        } else if (value > Byte.MAX_VALUE) {
            return new NBTTagShort((short) value);
        } else {
            return new NBTTagByte((byte) value);
        }
    }

    public static ArrayList<String> parseNBTAsLines(NBTTagCompound nbt) {
        return parseNBTAsLines(nbt, 0);
    }

    private static ArrayList<String> parseNBTAsLines(NBTTagCompound nbt, int indent) {
        ArrayList<String> li = new ArrayList();
        Iterator<NBTBase> it = nbt.func_150296_c().iterator();
        String idt = ReikaStringParser.getNOf("  ", indent);
        for (Object o : nbt.func_150296_c()) {
            String key = (String) o;
            NBTBase b = nbt.getTag(key); /*
             if (b instanceof NBTTagByteArray) {
                 li.add(b.getName()+": "+Arrays.toString(((NBTTagByteArray)b).byteArray));
             }
             else if (b instanceof NBTTagIntArray) {
                 li.add(b.getName()+": "+Arrays.toString(((NBTTagIntArray)b).intArray));
             }
             else if (b instanceof NBTTagCompound) {
                 li.add(EnumChatFormatting.GOLD+b.getName()+": "+b.toString());
             }
             else {
                 li.add(b.getName()+": "+b.toString());
             }*/
            if (b instanceof NBTTagCompound) {
                li.add(idt + key + ": ");
                li.addAll(parseNBTAsLines((NBTTagCompound) b, indent + 1));
            } else {
                li.add(idt + key + ": " + b.toString());
            }
        }
        return li;
    }

    public static void combineNBT(NBTTagCompound tag1, NBTTagCompound tag2) {
        if (tag2 == null || tag2.hasNoTags())
            return;
        for (Object o : tag2.func_150296_c()) {
            String s = (String) o;
            NBTBase key = tag2.getTag(s);
            tag1.setTag(s, combineTags(tag1.getTag(s), key.copy()));
        }
    }

    private static NBTBase combineTags(NBTBase a, NBTBase b) {
        if (a != null && b == null)
            return a;
        if (a == null || a.getClass() != b.getClass())
            return b;
        if (a instanceof NBTTagCompound) {
            combineNBT((NBTTagCompound) a, (NBTTagCompound) b);
            return a;
        }
        if (a instanceof NBTTagList) {
            for (Object o : ((NBTTagList) b).tagList) {
                ((NBTTagList) a).appendTag((NBTBase) o);
            }
        }
        return b;
    }

    public static void clearTagCompound(NBTTagCompound dat) {
        Collection<String> tags = new ArrayList(dat.func_150296_c());
        for (String tag : tags) {
            dat.removeTag(tag);
        }
    }

    public static void copyNBT(NBTTagCompound from, NBTTagCompound to) {
        Collection<String> tags = new ArrayList(from.func_150296_c());
        for (String tag : tags) {
            to.setTag(tag, from.getTag(tag).copy());
        }
    }

    public static int compareNBTTags(NBTTagCompound o1, NBTTagCompound o2) {
        if (o1 == o2 || (o1 != null && o1.equals(o2))) {
            return 0;
        } else if (o1 == null) {
            return -1;
        } else if (o2 == null) {
            return 1;
        } else {
            return Integer.compare(o1.hashCode(), o2.hashCode());
        }
    }

    public static boolean areNBTTagsEqual(NBTTagCompound tag1, NBTTagCompound tag2) {
        if (tag1 == tag2)
            return true;
        if (tag1 == null || tag2 == null)
            return false;
        return tag1.equals(tag2);
    }

    public static boolean tagContains(NBTTagCompound tag, NBTTagCompound inner) {
        Set<String> set = inner.func_150296_c();
        for (String s : set) {
            NBTBase b1 = inner.getTag(s);
            NBTBase b2 = tag.getTag(s);
            if (b1 == b2)
                continue;
            if (b1 == null || b2 == null)
                return false;
            if (!b1.equals(b2))
                return false;
        }
        return true;
    }

    public static NBTTypes getTagType(NBTBase base) {
        return NBTTypes.IDMap.get(base.getId());
    }

    public static enum NBTTypes {
        INT(new NBTTagInt(0).getId()),
        BYTE(new NBTTagByte((byte) 0).getId()),
        SHORT(new NBTTagShort((short) 0).getId()),
        FLOAT(new NBTTagFloat(0).getId()),
        DOUBLE(new NBTTagDouble(0).getId()),
        LONG(new NBTTagLong(0).getId()),
        INTA(new NBTTagIntArray(new int[0]).getId()),
        BYTEA(new NBTTagByteArray(new byte[0]).getId()),
        STRING(new NBTTagString("").getId()),
        LIST(new NBTTagList().getId()),
        COMPOUND(new NBTTagCompound().getId()),
        END(0);

        public final int ID;

        private static final NBTTypes[] list = values();
        private static final HashMap<Integer, NBTTypes> IDMap = new HashMap();

        private NBTTypes(int id) {
            ID = id;
        }

        static {
            for (int i = 0; i < list.length; i++) {
                IDMap.put(list[i].ID, list[i]);
            }
        }
    }

    public static void overwriteNBT(NBTTagCompound tag, NBTTagCompound over) {
        for (Object o : over.func_150296_c()) {
            NBTBase b = over.getTag((String) o);
            tag.setTag((String) o, b);
        }
    }

    public static void addListToTags(NBTTagList tag, List<Object> li) {
        for (Object o : li) {
            NBTBase b = getTagForObject(o);
            if (b != null) {
                tag.appendTag(b);
            }
        }
    }

    public static void addMapToTags(NBTTagCompound tag, HashMap<String, Object> map) {
        for (String s : map.keySet()) {
            Object o = map.get(s);
            NBTBase b = getTagForObject(o);
            if (b != null) {
                tag.setTag(s, b);
            }
        }
    }

    public static HashMap<String, ?> readMapFromNBT(NBTTagCompound tag) {
        return (HashMap<String, ?>) getValue(tag);
    }

    public static void writeMapToNBT(String s, NBTTagCompound tag, Map<String, ?> map) {
        NBTBase dat = getTagForObject(map);
        tag.setTag(s, dat);
    }

    public static <K, V> void writeMapToNBT(
        Map<K, V> map, NBTTagList li, NBTIO<K> converterK, NBTIO<V> converterV
    ) {
        for (Entry<K, V> e : map.entrySet()) {
            NBTTagCompound entry = new NBTTagCompound();
            entry.setTag("key", getTagForObject(e.getKey(), converterK));
            entry.setTag("value", getTagForObject(e.getValue(), converterV));
            li.appendTag(entry);
        }
    }

    public static <K, V> void readMapFromNBT(
        Map<K, V> map, NBTTagList li, NBTIO<K> converterK, NBTIO<V> converterV
    ) {
        map.clear();
        for (Object o : li.tagList) {
            NBTTagCompound entry = (NBTTagCompound) o;
            K key = (K) getValue(entry.getTag("key"), converterK);
            V val = (V) getValue(entry.getTag("value"), converterV);
            map.put(key, val);
        }
    }

    public static <E> void
    writeCollectionToNBT(Collection<E> c, NBTTagCompound NBT, String key) {
        writeCollectionToNBT(c, NBT, key, null);
    }

    public static <E> void writeCollectionToNBT(
        Collection<E> c, NBTTagCompound NBT, String key, NBTIO<E> converter
    ) {
        NBTTagList li = new NBTTagList();
        for (Object o : c) {
            NBTBase b = getTagForObject(o, converter);
            NBTTagCompound tag = new NBTTagCompound();
            tag.setTag("value", b);
            li.appendTag(tag);
        }
        NBT.setTag(key, li);
    }

    public static <E> void
    readCollectionFromNBT(Collection<E> c, NBTTagCompound NBT, String key) {
        readCollectionFromNBT(c, NBT, key, null);
    }

    public static <E> void readCollectionFromNBT(
        Collection<E> c, NBTTagCompound NBT, String key, NBTIO<E> converter
    ) {
        c.clear();
        NBTTagList li = NBT.getTagList(key, NBTTypes.COMPOUND.ID);
        for (Object o : li.tagList) {
            NBTTagCompound tag = (NBTTagCompound) o;
            NBTBase b = tag.getTag("value");
            c.add((E) getValue(b, converter));
        }
    }

    public static NBTBase
    getNestedNBTTag(NBTTagCompound tag, ArrayList<String> li, String name) {
        for (String s : li) {
            tag = tag.getCompoundTag(s);
            if (tag == null || tag.hasNoTags())
                return null;
        }
        return tag.getTag(name);
    }

    public static void replaceTag(NBTTagCompound NBT, String s, NBTBase tag) {
        NBT.setTag(s, tag);
    }

    public static void replaceTag(NBTTagList NBT, int idx, NBTBase tag) {
        NBT.tagList.remove(idx);
        NBT.tagList.add(idx, tag);
    }

    /*
    public static class CompoundNBTIO {

        private final HashMap<Class, NBTIO> data = new HashMap();

        public <V> void addHandler(Class<? extends V> c, NBTIO<V> h) {
            data.put(c, h);
        }

    }
     */
    public interface NBTIO<V> {
        public V createFromNBT(NBTBase nbt);
        public NBTBase convertToNBT(V obj);
        public boolean acceptsType(Object o);
        public boolean acceptsTag(NBTBase tag);
    }

    /*
    public static class EnumNBTConverter implements NBTIO<Enum> {

        private final List<Enum> enumData;

        public EnumNBTConverter(Class<? extends Enum> c) {
            enumData = (List<Enum>)Arrays.asList(c.getEnumConstants());
        }

        @Override
        public Enum createFromNBT(NBTBase nbt) {
            int idx = ((NBTTagInt)nbt).func_150287_d();
            return idx >= 0 && idx < enumData.size() ? enumData.get(idx) : null;
        }

        @Override
        public NBTBase convertToNBT(Enum obj) {
            return new NBTTagInt(enumData.indexOf(obj));
        }

        @Override
        public boolean acceptsType(Object o) {
            return o instanceof Enum;
        }

    }
     */
    public static class BlockConverter implements NBTIO<Block> {
        public static final BlockConverter instance = new BlockConverter();

        private BlockConverter() {}

        @Override
        public Block createFromNBT(NBTBase nbt) {
            return Block.getBlockFromName((((NBTTagString) nbt).func_150285_a_()));
        }

        @Override
        public NBTBase convertToNBT(Block obj) {
            return new NBTTagString(Block.blockRegistry.getNameForObject(obj));
        }

        @Override
        public boolean acceptsType(Object o) {
            return o instanceof Block;
        }

        @Override
        public boolean acceptsTag(NBTBase tag) {
            return tag instanceof NBTTagString;
        }
    }

    public static class ItemConverter implements NBTIO<Item> {
        public static final ItemConverter instance = new ItemConverter();

        private ItemConverter() {}

        @Override
        public Item createFromNBT(NBTBase nbt) {
            return (Item
            ) Item.itemRegistry.getObject((((NBTTagString) nbt).func_150285_a_()));
        }

        @Override
        public NBTBase convertToNBT(Item obj) {
            return new NBTTagString(Item.itemRegistry.getNameForObject(obj));
        }

        @Override
        public boolean acceptsType(Object o) {
            return o instanceof Item;
        }

        @Override
        public boolean acceptsTag(NBTBase tag) {
            return tag instanceof NBTTagString;
        }
    }

    public static class ItemStackConverter implements NBTIO<ItemStack> {
        public static final ItemStackConverter instance = new ItemStackConverter();

        private ItemStackConverter() {}

        @Override
        public ItemStack createFromNBT(NBTBase nbt) {
            return ItemStack.loadItemStackFromNBT((NBTTagCompound) nbt);
        }

        @Override
        public NBTBase convertToNBT(ItemStack obj) {
            NBTTagCompound ret = new NBTTagCompound();
            obj.writeToNBT(ret);
            return ret;
        }

        @Override
        public boolean acceptsType(Object o) {
            return o instanceof ItemStack;
        }

        @Override
        public boolean acceptsTag(NBTBase tag) {
            return tag instanceof NBTTagCompound;
        }
    }

    public static class KeyedItemStackConverter implements NBTIO<KeyedItemStack> {
        public static final KeyedItemStackConverter instance
            = new KeyedItemStackConverter();

        private KeyedItemStackConverter() {}

        @Override
        public KeyedItemStack createFromNBT(NBTBase nbt) {
            return KeyedItemStack.readFromNBT((NBTTagCompound) nbt);
        }

        @Override
        public NBTBase convertToNBT(KeyedItemStack obj) {
            NBTTagCompound ret = new NBTTagCompound();
            obj.writeToNBT(ret);
            return ret;
        }

        @Override
        public boolean acceptsType(Object o) {
            return o instanceof KeyedItemStack;
        }

        @Override
        public boolean acceptsTag(NBTBase tag) {
            return tag instanceof NBTTagCompound;
        }
    }

    public static class UUIDConverter implements NBTIO<UUID> {
        public static final UUIDConverter instance = new UUIDConverter();

        private UUIDConverter() {}

        @Override
        public UUID createFromNBT(NBTBase nbt) {
            return UUID.fromString(((NBTTagString) nbt).func_150285_a_());
        }

        @Override
        public NBTBase convertToNBT(UUID obj) {
            return new NBTTagString(obj.toString());
        }

        @Override
        public boolean acceptsType(Object o) {
            return o instanceof UUID;
        }

        @Override
        public boolean acceptsTag(NBTBase tag) {
            return tag instanceof NBTTagString;
        }
    }

    private static class EnumIO implements NBTIO<Enum> {
        private final Enum[] objects;
        private final Class enumType;

        private EnumIO(Class<? extends Enum> c) {
            objects = c.getEnumConstants();
            enumType = c;
        }

        @Override
        public Enum createFromNBT(NBTBase nbt) {
            return objects[((NBTTagInt) nbt).func_150287_d()];
        }

        @Override
        public NBTBase convertToNBT(Enum obj) {
            return new NBTTagInt(obj.ordinal());
        }

        @Override
        public boolean acceptsType(Object o) {
            return o instanceof Enum;
        }

        @Override
        public boolean acceptsTag(NBTBase tag) {
            return tag instanceof NBTTagInt;
        }
    }

    public static NBTIO<? extends Enum> getEnumConverter(Class<? extends Enum> c) {
        EnumIO handler = enumIOMap.get(c);
        if (handler == null) {
            handler = new EnumIO(c);
            enumIOMap.put(c, handler);
        }
        return handler;
    }
}
