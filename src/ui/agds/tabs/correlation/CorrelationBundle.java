package ui.agds.tabs.correlation;

/**
 * @author Lukasz
 * @since 27.05.2016.
 */
public class CorrelationBundle {
    public int firstIndex = -1, secondIndex = -1;
    private boolean switcher;

    public void add(int index) {
        if (!switcher) {
            if (index != firstIndex)
                firstIndex = index;
        } else {
            if (index != secondIndex)
                secondIndex = index;
        }
        switcher = !switcher;
        common.Utils.log("Current indexes = " + firstIndex + ", " + secondIndex);
    }

    public boolean isInvalid() {
        return firstIndex == -1 || secondIndex == -1;
    }
}