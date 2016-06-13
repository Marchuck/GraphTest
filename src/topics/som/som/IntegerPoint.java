package topics.som.som;

/**
 * @author Lukasz
 * @since 12.06.2016.
 */
public class IntegerPoint {
    public int x,y,z;

    public IntegerPoint() {
        x=0; y=0; z=0;
    }

    public IntegerPoint(int x1, int y1, int z1) {
        x=x1; y=y1; z=z1;
    }

    public float dist() {
        return (float)Math.sqrt(x*x+y*y+z*z);
    }

    public void set(int x1, int y1, int z1) {
        x=x1;
        y=y1;
        z=z1;
    }

    public IntegerPoint sub(IntegerPoint m) {
        IntegerPoint ret = new IntegerPoint();
        ret.x=x-m.x;
        ret.y=y-m.y;
        ret.z=z-m.z;

        return ret;
    }

    public IntegerPoint sub(int m) {
        IntegerPoint ret = new IntegerPoint();
        ret.x=x-m;
        ret.y=y-m;
        ret.z=z-m;

        return ret;
    }

    public IntegerPoint add(IntegerPoint m) {
        IntegerPoint ret = new IntegerPoint();
        ret.x=x+m.x;
        ret.y=y+m.y;
        ret.z=z+m.z;

        return ret;
    }

    public IntegerPoint add(int m) {
        IntegerPoint ret = new IntegerPoint();
        ret.x=x+m;
        ret.y=y+m;
        ret.z=z+m;

        return ret;
    }

    public IntegerPoint mult(int m) {
        IntegerPoint ret = new IntegerPoint();
        ret.x=x*m;
        ret.y=y*m;
        ret.z=z*m;

        return ret;
    }

    public IntegerPoint mult(float m) {
        IntegerPoint ret = new IntegerPoint();
        ret.x=Math.round(x*m);
        ret.y=Math.round(y*m);
        ret.z=Math.round(z*m);

        return ret;
    }

    public IntegerPoint div(int m) {
        IntegerPoint ret = new IntegerPoint();
        if (m!=0) {
            ret.x=x/m;
            ret.y=y/m;
            ret.z=z/m;
        }
        else
            ret.x=ret.y=ret.z=0;
        return ret;
    }

    public IntegerPoint div(float m) {
        IntegerPoint ret = new IntegerPoint();
        if (m!=0.0f) {
            ret.x=Math.round(x/m);
            ret.y=Math.round(y/m);
            ret.z=Math.round(z/m);
        }
        else
            ret.x=ret.y=ret.z=0;

        return ret;
    }

    public int pipe(IntegerPoint m) {
        return (x*m.x+y*m.y+z*m.z);
    }

    public void print() {
        System.out.println(" x = " + x);
        System.out.println(" y = " + y);
        System.out.println(" z = " + z);
        System.out.println("\n");
    }
}
