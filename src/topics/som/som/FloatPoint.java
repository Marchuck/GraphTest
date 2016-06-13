package topics.som.som;

/**
 * @author Lukasz
 * @since 12.06.2016.
 */
public class FloatPoint {
    public float x, y, z;

    public FloatPoint() {
        x = 0;
        y = 0;
        z = 0;
    }

    public FloatPoint(float x1, float y1, float z1) {
        x = x1;
        y = y1;
        z = z1;
    }

    public float dist() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public void set(float x1, float y1, float z1) {
        x = x1;
        y = y1;
        z = z1;
    }

    public FloatPoint sub(FloatPoint m) {
        FloatPoint ret = new FloatPoint();
        ret.x = x - m.x;
        ret.y = y - m.y;
        ret.z = z - m.z;

        return ret;
    }

    public FloatPoint sub(float m) {
        FloatPoint ret = new FloatPoint();
        ret.x = x - m;
        ret.y = y - m;
        ret.z = z - m;

        return ret;
    }

    public FloatPoint add(FloatPoint m) {
        FloatPoint ret = new FloatPoint();
        ret.x = x + m.x;
        ret.y = y + m.y;
        ret.z = z + m.z;

        return ret;
    }

    public FloatPoint add(float m) {
        FloatPoint ret = new FloatPoint();
        ret.x = x + m;
        ret.y = y + m;
        ret.z = z + m;

        return ret;
    }

    public FloatPoint mult(float m) {
        FloatPoint ret = new FloatPoint();
        ret.x = x * m;
        ret.y = y * m;
        ret.z = z * m;

        return ret;
    }


    public FloatPoint div(float m) {
        FloatPoint ret = new FloatPoint();
        if (m != 0) {
            ret.x = x / m;
            ret.y = y / m;
            ret.z = z / m;
        } else
            ret.x = ret.y = ret.z = 0;
        return ret;
    }

    public float pipe(FloatPoint m) {
        return (x * m.x + y * m.y + z * m.z);
    }

    IntegerPoint fti() {
        IntegerPoint i = new IntegerPoint(Math.round(x), Math.round(y), Math.round(z));
        return i;
    }

    public void print() {
        System.out.println(" x = " + x);
        System.out.println(" y = " + y);
        System.out.println(" z = " + z);
        System.out.println("\n");
    }
}
