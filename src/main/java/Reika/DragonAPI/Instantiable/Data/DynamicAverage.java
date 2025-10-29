package Reika.DragonAPI.Instantiable.Data;

import java.util.ArrayList;
import java.util.Collection;

public class DynamicAverage {
    private final Collection<Double> values = new ArrayList();
    private double sum;

    public double getAverage() {
        return sum / values.size();
    }

    public void add(double val) {
        sum += val;
        values.add(val);
    }

    public void clear() {
        values.clear();
        sum = 0;
    }
}
