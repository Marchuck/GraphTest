package ui.agds.tabs.similar;

import javafx.util.Pair;
import topics.agds.nodes.RecordNode;

/**
 * @author Lukasz
 * @since 27.05.2016.
 */
public class SimilarItem {
    public RecordNode recordNode;
    public String name;
    /**
     * 0 - not selected
     * 1 - selected
     * 2 - similar
     */
    public int isSelected = 0;

    public SimilarItem(Pair<RecordNode, String> pair) {
        this(pair.getKey(), pair.getKey().getName() + " : " + pair.getValue());
    }

    public SimilarItem(RecordNode recordNode, String name) {
        this.recordNode = recordNode;
        this.name = name;
    }
}
