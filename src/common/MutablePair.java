package common;

/**
 * @author Lukasz Marczak
 * @since 23.04.16.
 * Mutable pair
 */
public final class MutablePair<FIRST, SECOND> {
    public FIRST first;
    public SECOND second;

    public MutablePair(FIRST first, SECOND second) {
        this.first = first;
        this.second = second;
    }
}

