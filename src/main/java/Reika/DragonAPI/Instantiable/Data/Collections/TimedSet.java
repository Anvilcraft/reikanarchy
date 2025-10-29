package Reika.DragonAPI.Instantiable.Data.Collections;

import java.util.HashSet;

public class TimedSet<V> {
    private long lastTime;
    private final HashSet<V> data = new HashSet();

    public boolean add(long time, V val) {
        if (time != this.lastTime) {
            this.clear();
        }
        this.lastTime = time;
        return this.data.add(val);
    }

    public boolean contains(V val) {
        return this.data.contains(val);
    }

    public int size() {
        return this.data.size();
    }

    public void clear() {
        this.data.clear();
    }
}
