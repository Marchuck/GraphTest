package ui.agds.tabs.correlation;

import javafx.util.Pair;
import topics.agds.nodes.RecordNode;

/**
 * @author Lukasz
 * @since 27.05.2016.
 */
public class CorrelationItem {
    public RecordNode recordNode;
    public String name;
    /**
     * 0 - not selected
     * 1 - selected
     * 2 - similar
     */
    public int isSelected = 0;

    public CorrelationItem(Pair<RecordNode, String> pair) {
        this(pair.getKey(), pair.getKey().getName() + " : " + pair.getValue());
    }

    public CorrelationItem(RecordNode recordNode, String name) {
        this.recordNode = recordNode;
        this.name = name;
    }
}
