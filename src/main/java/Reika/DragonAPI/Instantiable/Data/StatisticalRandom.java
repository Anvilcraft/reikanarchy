package Reika.DragonAPI.Instantiable.Data;

import java.util.Collection;
import java.util.HashSet;

import Reika.DragonAPI.Exception.MisuseException;
import Reika.DragonAPI.Instantiable.Data.Maps.CountMap;
import Reika.DragonAPI.Libraries.Java.ReikaJavaLibrary;
import Reika.DragonAPI.Libraries.ReikaNBTHelper;
import Reika.DragonAPI.Libraries.ReikaNBTHelper.NBTIO;
import net.minecraft.nbt.NBTTagCompound;

public class StatisticalRandom<K> {
    private final CountMap<K> data = new CountMap();

    private final HashSet<K> options = new HashSet();

    private NBTIO<K> converter;

    public StatisticalRandom() {}

    public StatisticalRandom(Collection<K> set) {
        options.addAll(set);
    }

    public StatisticalRandom(K... set) {
        this(ReikaJavaLibrary.makeListFromArray(set));
    }

    public StatisticalRandom(Class<? extends K> set) {
        if (!set.isEnum())
            throw new MisuseException(
                "You can only specify enum types via a class reference!"
            );
        K[] data = set.getEnumConstants();
        options.addAll(ReikaJavaLibrary.makeListFromArray(data));
        this.setNBTConverter(
            (NBTIO<K>) ReikaNBTHelper.getEnumConverter((Class<? extends Enum>) set)
        );
    }

    public void setNBTConverter(NBTIO<K> c) {
        this.converter = c;
    }

    public K roll() {
        return this.roll(null);
    }

    public K roll(WeightedRandom<K> base) {
        K result = this.genRandom(base).getRandomEntry();
        data.increment(result);
        return result;
    }

    private WeightedRandom<K> genRandom(WeightedRandom<K> base) {
        WeightedRandom<K> w = new WeightedRandom();
        for (K k : options) {
            double wt = this.getWeightOf(base, k);
            if (wt > 0) { //very early on, ones already obtained have negative weights, so
                          //are out of selection, to ensure some of all
                w.addEntry(k, wt);
            }
        }
        return w;
    }

    private double getWeightOf(WeightedRandom<K> src, K k) {
        double base = 1D / options.size();
        double frac = data.getFraction(k);
        if (src != null) {
            base *= src.getWeight(k) / src.getTotalWeight();
        }
        double chance = base - (frac - base);
        return chance;
    }

    public void readFromNBT(NBTTagCompound tag) {
        data.clear();
        HashSet<K> set = new HashSet();
        data.readFromNBT(tag.getCompoundTag("data"), converter);
        ReikaNBTHelper.readCollectionFromNBT(set, tag, "set", converter);
        options.addAll(set);
    }

    public void writeToNBT(NBTTagCompound tag) {
        NBTTagCompound nbt = new NBTTagCompound();
        data.writeToNBT(nbt, converter);
        tag.setTag("data", nbt);
        ReikaNBTHelper.writeCollectionToNBT(options, tag, "set", converter);
    }

    @Override
    public String toString() {
        return data.toString() + " > " + this.genRandom(null).toString();
    }
}
