package ui.agds.tabs.similar;

import common.Utils;
import javafx.util.Pair;
import topics.agds.engine.AgdsEngine;
import topics.agds.nodes.PropertyNode;
import topics.agds.nodes.RecordNode;
import ui.agds.tabs.classify.InformationDialog;
import ui.agds.tabs.classify.SelectPropertyDialog;
import ui.agds.tabs.classify.SingleValueChooser;
import ui.connector.ResultCallback;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Lukasz
 * @since 26.05.2016.
 */
public class SimilarityPaneConnector {


    public SimilarityPaneConnector(final JButton findLeastSimilarButton, final JButton searchSimilarButton,
                                   final JButton maxButton, final JButton minButton,
                                   JList<SimilarItem> similarityList, final JButton loadRecordNodesButton,
                                   final AgdsEngine engine) {


        maxButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SelectPropertyDialog(maxButton, engine.getPropertyNodes(), new SelectPropertyDialog.SendAction() {
                    @Override
                    public void onSend(List<PropertyNode> node) {
                        double max = engine.getMax(node.get(0));
                        if (node.size() > 1)
                            for (int j = 1; j < node.size(); j++) {
                                double d1 = engine.getMax(node.get(j));
                                if (max > d1) max = d1;
                            }
                        String result = "Max: " + max;
                        new InformationDialog(maxButton, result);
                    }
                });
            }
        });
        minButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SelectPropertyDialog(minButton, engine.getPropertyNodes(), new SelectPropertyDialog.SendAction() {
                    @Override
                    public void onSend(List<PropertyNode> node) {
                        double min = engine.getMin(node.get(0));
                        if (node.size() > 1)
                            for (int j = 1; j < node.size(); j++) {
                                double d1 = engine.getMin(node.get(j));
                                if (min < d1) min = d1;
                            }
                        String result = "Min: " + min;
                        new InformationDialog(minButton, result);
                    }
                });
            }
        });

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
        loadRecordNodesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.clear();
                for (Pair<RecordNode, String> pair : engine.currentSimilarNodes)
                    model.addElement(new SimilarItem(pair));
            }
        });

        findLeastSimilarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


            }
        });


        mostSimilarListener = (new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                common.Utils.log("submit items");

                final SingleValueChooser chooser = new SingleValueChooser(searchSimilarButton,
                        "Set threshold", new SingleValueChooser.SendAction() {
                    @Override
                    public void onSend(String value) {
                        final double d1 = Double.parseDouble(value);
                        if (Utils.isBetween(0, 1, d1)) {
                            List<RecordNode> selected = new ArrayList<RecordNode>();
                            for (int j = 0; j < model.getSize(); j++) {
                                SimilarItem item = model.elementAt(j);
                                if (item.isSelected == 1) {
                                    selected.add(item.recordNode);
                                }
                            }
                            engine.calculateSimilarity(d1, selected, new ResultCallback<RecordNode>() {
                                @Override
                                public void onComputed(final List<RecordNode> result) {
                                    Set<String> names = new HashSet<>();
//                                    String ress = "[";
//                                    for (RecordNode aResult : result) {
//                                        names.add(aResult.getName());
//                                    }
//                                    List<String> a = new ArrayList<>();
//                                    a.addAll(names);
//                                    for (String anA : a) {
//                                        ress += anA + ", ";
//                                    }
//                                    ress += "]";
                                    new InformationDialog(searchSimilarButton, "similarity result:\n", result);
                                }
                            });
                        } else {
                            mostSimilarListener.actionPerformed(e);
                        }
                    }
                });
            }
        });
        searchSimilarButton.addActionListener(mostSimilarListener);
    }

    private ActionListener mostSimilarListener;
    private ActionListener leastSimilarListener;
}
