package ui.agds.tabs.similar;

import common.Utils;
import javafx.util.Pair;
import topics.agds.engine.GenericAgdsEngine;
import topics.agds.nodes.RecordNode;
import ui.agds.tabs.classify.InformationDialog;
import ui.agds.tabs.classify.SingleValueChooser;
import ui.connector.ResultCallback;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz
 * @since 26.05.2016.
 */
public class SimilarityPaneConnector {


    public SimilarityPaneConnector(JList<SimilarItem> similarityList, final JButton submitItemsButton,
                                   final GenericAgdsEngine engine) {
        final DefaultListModel<SimilarItem> model = new DefaultListModel<>();
        similarityList.setModel(model);

        List<Pair<RecordNode, String>> nodes = engine.currentSimilarNodes;
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

        actionListener = (new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                common.Utils.log("submit items");

                final SingleValueChooser chooser = new SingleValueChooser(submitItemsButton, "Set threshold between 0 and 1", new SingleValueChooser.SendAction() {
                    @Override
                    public void onSend(String value) {
                        double d1 = Double.parseDouble(value);
                        if (Utils.isBetween(0, 1, d1)) {
                            List<RecordNode> selected = new ArrayList<RecordNode>();
                            for (int j = 0; j < model.getSize(); j++) {
                                SimilarItem item = model.elementAt(j);
                                if (item.isSelected == 1) {
                                    selected.add(item.recordNode);
                                }
                            }
                            engine.calculateSimilarity(selected, new ResultCallback<RecordNode>() {
                                @Override
                                public void onComputed(List<RecordNode> result) {
                                    String ress = "[";
                                    for (int k = 0; k < result.size() - 1; k++) {
                                        ress += result.get(k).getName() + ",";
                                    }
                                    ress += result.get(result.size() - 1).getName() + "]";
                                    new InformationDialog(submitItemsButton, "similarity result:\n" + ress);
                                }
                            });
                        } else {
                            actionListener.actionPerformed(e);
                        }
                    }
                });


            }
        });
        submitItemsButton.addActionListener(actionListener);
    }

    ActionListener actionListener;
}
