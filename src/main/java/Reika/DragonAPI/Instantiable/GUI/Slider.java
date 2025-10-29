package Reika.DragonAPI.Instantiable.GUI;

public class Slider {
    public final int posX;
    public final int posY;
    public final int width;
    public final int height;

    private int position;

    public Slider(int x, int y, int w, int h) {
        posX = x;
        posY = y;
        width = w;
        height = h;
    }

    public float getFraction() {
        return position / (float) height;
    }

    public Slider setPosition(int pos) {
        position = pos;
        return this;
    }
}
