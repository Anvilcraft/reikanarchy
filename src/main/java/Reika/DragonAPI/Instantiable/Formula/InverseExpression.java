package Reika.DragonAPI.Instantiable.Formula;

public class InverseExpression extends MathExpression {
    public final double baseVal;
    public final double scale;

    public InverseExpression(double init, double scale) {
        baseVal = init;
        this.scale = scale;
    }

    @Override
    public final double evaluate(double arg) throws ArithmeticException {
        return baseVal + scale / arg;
    }

    @Override
    public final double getBaseValue() {
        return baseVal;
    }

    @Override
    public final String toString() {
        return baseVal + (scale > 0 ? "+" : "-") + Math.abs(scale) + "/x";
    }
}
