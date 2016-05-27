package ui.agds.tabs.similar;

import com.sun.javafx.beans.annotations.NonNull;
import javafx.util.Pair;
import topics.agds.GenericAgdsEngine;
import topics.agds.nodes.RecordNode;
import ui.connector.ResultCallback;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Lukasz
 * @since 26.05.2016.
 */
public class SimilarityPaneConnector {
    public interface DataProvider {
        GenericAgdsEngine provideEngine();
    }

    public SimilarityPaneConnector(JList<SimilarItem> similarityList, JButton submitItemsButton, @NonNull final DataProvider provider) {
        final DefaultListModel<SimilarItem> model = new DefaultListModel<>();
        similarityList.setModel(model);

        List<Pair<RecordNode, String>> nodes = provider.provideEngine().currentSimilarNodes;
        for (Pair<RecordNode, String> pair : nodes)
            model.addElement(new SimilarItem(pair));

        similarityList.setCellRenderer(new SimilarItemsRenderer());
        similarityList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
//        similarityList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        similarityList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList) evt.getSource();
                if (evt.getClickCount() == 1) {
                    int index = list.locationToIndex(evt.getPoint());
                    common.Utils.log("clicked " + index);
                    model.get(index).isSelected = model.get(index).isSelected == 0 ? 1 : 0;

                } else if (evt.getClickCount() == 2) {
                    int index = list.locationToIndex(evt.getPoint());
                    common.Utils.log("clicked twice on " + index);
                    // Double-click detected
//                    model.remove(index);

                }
            }
        });

        submitItemsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                common.Utils.log("submit items");
                List<RecordNode> selected = new ArrayList<RecordNode>();
                for (int j = 0; j < model.getSize(); j++) {
                    SimilarItem item = model.elementAt(j);
                    if (item.isSelected == 1) {
                        selected.add(item.recordNode);
                    }
                }
                provider.provideEngine().calculateSimilarity(selected, new ResultCallback<RecordNode>() {
                    @Override
                    public void onComputed(Collection<RecordNode> result) {

                    }
                });

            }
        });
    }
}
