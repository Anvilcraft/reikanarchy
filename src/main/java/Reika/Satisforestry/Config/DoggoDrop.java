package Reika.Satisforestry.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import Reika.DragonAPI.Instantiable.IO.LuaBlock;
import Reika.DragonAPI.Instantiable.IO.LuaBlock.LuaBlockDatabase;
import Reika.DragonAPI.Libraries.Java.ReikaJavaLibrary;
import Reika.DragonAPI.Libraries.Java.ReikaRandomHelper;
import Reika.DragonAPI.Libraries.Registry.ReikaItemHelper;
import Reika.DragonAPI.Libraries.ReikaNBTHelper;
import Reika.Satisforestry.Config.BiomeConfig.DoggoLuaBlock;
import Reika.Satisforestry.Entity.EntityLizardDoggo;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.biome.BiomeGenBase;

public class DoggoDrop {
    public final int minCount;
    public final int maxCount;
    public final int baseWeight;

    private final ItemStack item;

    private final ArrayList<Condition> requirements = new ArrayList();
    private final HashMap<Condition, Double> weightFactors = new HashMap();

    public DoggoDrop(Item i, int min, int max, int wt) {
        this(new ItemStack(i), min, max, wt);
    }

    public DoggoDrop(ItemStack is, int min, int max, int wt) {
        if (max > is.getMaxStackSize())
            throw new IllegalArgumentException(
                "Invalid max-amount (" + max
                + ") for doggo item - more than max stack size (" + is.getMaxStackSize()
                + ")"
            );
        if (max < min)
            throw new IllegalArgumentException(
                "Invalid min/max max-amounts for doggo item - max is less than min"
            );
        item = is;
        minCount = min;
        maxCount = max;
        baseWeight = wt;
    }

    public ItemStack generateItem(Random rand) {
        return ReikaItemHelper.getSizedItemStack(
            item, ReikaRandomHelper.getRandomBetween(minCount, maxCount, rand)
        );
    }

    public ItemStack getItem() {
        return item.copy();
    }

    public LuaBlock createLuaBlock(LuaBlock parent, LuaBlockDatabase tree) {
        LuaBlock lb = new DoggoLuaBlock("{", parent, tree);
        lb.putData("key", this.getItemKey());
        LuaBlock nbt = this.createNBTKey(lb, tree);
        lb.putData("minCount", minCount);
        lb.putData("maxCount", maxCount);
        lb.putData("weight", baseWeight);
        if (!requirements.isEmpty()) {
            DoggoLuaBlock reqs = new DoggoLuaBlock("limits", lb, tree);
            for (Condition c : requirements) {
                LuaBlock item = new DoggoLuaBlock("{", reqs, tree);
                item.putData("check", c.check.key);
                item.putData("value", String.valueOf(c.value));
                if (c.check.comment != null)
                    item.setComment("check", c.check.comment);
            }
            reqs.setComment(
                null,
                "optional, requirements to allow this item to be found; valid check types: "
                    + Checks.getNameList()
            );
        }
        if (!weightFactors.isEmpty()) {
            DoggoLuaBlock reqs = new DoggoLuaBlock("weightFactors", lb, tree);
            for (Entry<Condition, Double> en : weightFactors.entrySet()) {
                Condition c = en.getKey();
                LuaBlock item = new DoggoLuaBlock("{", reqs, tree);
                item.putData("check", c.check.key);
                item.putData("value", String.valueOf(c.value));
                item.putData("factor", en.getValue());
                if (c.check.comment != null)
                    item.setComment("check", c.check.comment);
            }
            reqs.setComment(
                null,
                "optional, conditionally-applied multipliers to weight; valid check types: "
                    + Checks.getNameList()
            );
        }
        return lb;
    }

    private String getItemKey() {
        Item i = item.getItem();
        int dmg = item.getItemDamage();
        String base = Item.itemRegistry.getNameForObject(i); //already has namespace
        if (dmg == 0 && !i.getHasSubtypes()) {
            return base;
        }
        return base + ":" + dmg;
    }

    private LuaBlock createNBTKey(LuaBlock parent, LuaBlockDatabase db) {
        if (item.stackTagCompound == null)
            return null;
        LuaBlock ret = new DoggoLuaBlock("nbt", parent, db);
        ret.writeData(ReikaNBTHelper.readMapFromNBT(item.stackTagCompound));
        return ret;
    }

    public float getNetWeight(EntityLizardDoggo e) {
        for (Condition c : requirements) {
            if (!c.check.evaluate(e, c.value))
                return 0;
        }
        float val = baseWeight;
        for (Entry<Condition, Double> en : weightFactors.entrySet()) {
            Condition c = en.getKey();
            if (c.check.evaluate(e, c.value)) {
                val *= en.getValue();
            }
        }
        return val;
    }

    public void addWeightFactor(LuaBlock b) {
        String key = b.getString("check");
        Checks c = Checks.getByKey(key);
        if (c == null)
            throw new IllegalArgumentException("Invalid check type '" + key + "'");
        this.addWeightFactor(c, c.parseReq(b.getString("value")), b.getDouble("factor"));
    }

    public void addWeightFactor(Checks c, Object req, double f) {
        if (c == null)
            throw new IllegalArgumentException("Null check type!");
        weightFactors.put(new Condition(c, req), f);
    }

    public void addCondition(LuaBlock b) {
        String key = b.getString("check");
        Checks c = Checks.getByKey(key);
        if (c == null)
            throw new IllegalArgumentException("Invalid check type '" + key + "'");
        this.addCondition(c, c.parseReq(b.getString("value")));
    }

    public void addCondition(Checks c, Object req) {
        if (c == null)
            throw new IllegalArgumentException("Null check type!");
        requirements.add(new Condition(c, req));
    }

    public Collection<Condition> getRequirements() {
        return Collections.unmodifiableCollection(requirements);
    }

    public Map<Condition, Double> getModifiers() {
        return Collections.unmodifiableMap(weightFactors);
    }

    @Override
    public String toString() {
        return item + " @ " + baseWeight;
    }

    public static class Condition {
        public final Checks check;
        public final Object value;

        private Condition(Checks c, Object req) {
            check = c;
            value = req;
        }

        public String getDisplayString() {
            return check.getDisplayString(value);
        }
    }

    public static enum Checks {
        NIGHT("is_night"),
        BIOME("biome_id", "ID of the biome the doggo is in"),
        HEALTH("health", "minimum doggo health fraction"),
        MINY("min_y", "minimum y level of the doggo"),
        MAXY("max_y", "maximum y level of the doggo"),
        PEACEFUL("is_peaceful"),
        SKY("has_sky", "whether sky is visible from its location"),
        ;

        public final String key;
        public final String comment;

        private static final HashMap<String, Checks> keyMap = new HashMap();

        private Checks(String s) {
            this(s, null);
        }

        private Checks(String s, String c) {
            key = s;
            comment = c;
        }

        public boolean evaluate(EntityLizardDoggo e, Object val) {
            switch (this) {
                case BIOME:
                    return e.worldObj.getBiomeGenForCoords(
                               MathHelper.floor_double(e.posX),
                               MathHelper.floor_double(e.posZ)
                           )
                        == val;
                case HEALTH:
                    return e.getHealth() >= e.getMaxHealth() * (double) val;
                case MAXY:
                    return e.posY <= (double) val;
                case MINY:
                    return e.posY >= (double) val;
                case NIGHT:
                    return e.worldObj.isDaytime() != (boolean) val;
                case PEACEFUL:
                    return (e.worldObj.difficultySetting == EnumDifficulty.PEACEFUL)
                        == ((boolean) val == true);
                case SKY:
                    return e.worldObj.canBlockSeeTheSky(
                        MathHelper.floor_double(e.posX),
                        (int) e.posY + 1,
                        MathHelper.floor_double(e.posZ)
                    );
            }
            return false;
        }

        public String getDisplayString(Object value) {
            switch (this) {
                case BIOME:
                    return "Is in biome: "
                        + BiomeGenBase.biomeList[(int) value].biomeName;
                case HEALTH:
                    return "Doggo Health at least " + ((double) value * 100) + "%";
                case MAXY:
                    return "Doggo below Y=" + value;
                case MINY:
                    return "Doggo above Y=" + value;
                case NIGHT:
                    return (boolean) value ? "Is night time" : "Is day time";
                case PEACEFUL:
                    return (boolean) value ? "Is peaceful mode" : "Is not peaceful mode";
                case SKY:
                    return "Doggo can" + ((boolean) value ? "" : "not") + " see the sky";
            }
            return comment + " = " + value;
        }

        public Object parseReq(String input) {
            switch (this) {
                case NIGHT:
                case PEACEFUL:
                case SKY:
                    return Boolean.parseBoolean(input);
                case BIOME:
                    return Integer.parseInt(input);
                case HEALTH:
                case MAXY:
                case MINY:
                    return Double.parseDouble(input);
            }
            return null;
        }

        static {
            for (Checks c : values()) {
                keyMap.put(c.key, c);
            }
        }

        public static Checks getByKey(String s) {
            return keyMap.get(s);
        }

        public static String getNameList() {
            return ReikaJavaLibrary.getEnumNameList(Checks.class);
        }
    }
}
