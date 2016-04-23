package common;

/**
 * @author Lukasz Marczak
 * @since 23.04.16.
 */
public class MPair<FIRST, SECOND> {
    public FIRST first;
    public SECOND second;

    public MPair(FIRST first, SECOND second) {
        this.first = first;
        this.second = second;
    }
}

