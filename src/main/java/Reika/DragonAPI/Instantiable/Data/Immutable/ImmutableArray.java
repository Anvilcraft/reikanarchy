package Reika.DragonAPI.Instantiable.Data.Immutable;

public final class ImmutableArray<V> {
    private final V[] data;
    public final int length;

    public ImmutableArray(V[] arr) {
        data = arr;
        this.length = arr.length;
    }

    public V get(int i) {
        return data[i];
    }
}
