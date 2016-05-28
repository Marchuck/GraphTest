package ui.agds.tabs.correlation;

import common.Utils;
import javafx.util.Pair;
import topics.agds.engine.GenericAgdsEngine;
import topics.agds.nodes.RecordNode;
import ui.agds.tabs.classify.SingleValueChooser;
import ui.connector.ResultCallback;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * @author Lukasz
 * @since 26.05.2016.
 */
public class CorrelationPaneConnector {

    private CorrelationBundle correlationBundle = new CorrelationBundle();

    public CorrelationPaneConnector(final JList<CorrelationItem> correlationList, final JButton computeCorrelationButton, final GenericAgdsEngine agdsEngine) {

        final DefaultListModel<CorrelationItem> model = new DefaultListModel<>();
        correlationList.setModel(model);

        List<Pair<RecordNode, String>> nodes = agdsEngine.currentSimilarNodes;
        for (Pair<RecordNode, String> pair : nodes)
            model.addElement(new CorrelationItem(pair));

        correlationList.setCellRenderer(new CorrelationItemsRenderer());
        correlationList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
//        similarityList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        correlationList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList) evt.getSource();
                if (evt.getClickCount() == 1) {

                    int index = list.locationToIndex(evt.getPoint());
                    correlationBundle.add(index);
                    int firstIndex = correlationBundle.firstIndex;
                    int secondIndex = correlationBundle.secondIndex;

                    for (int j = 0; j < model.getSize(); j++) {
                        if (j == firstIndex) {
                            model.get(j).isSelected = 1;
                        } else if (j == secondIndex) {
                            model.get(j).isSelected = 2;
                        } else {
                            model.get(j).isSelected = 0;
                        }
                    }
                    correlationList.invalidate();

                    Utils.log("clicked " + index);

                } else if (evt.getClickCount() == 2) {
                    int index = list.locationToIndex(evt.getPoint());
                    Utils.log("clicked twice on " + index);
                    // Double-click detected
//                    model.remove(index);

                }
            }
        });

        computeCorrelationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utils.log("submit items");

                agdsEngine.calculateCorrelation(correlationBundle, new ResultCallback<Double>() {
                    @Override
                    public void onComputed(List<Double> results) {

                            Double result = (  results).get(0);
                            new SingleValueChooser(computeCorrelationButton, "Result is " + result, new SingleValueChooser.SendAction() {
                                @Override
                                public void onSend(String value) {
                                    Utils.log("value : " + value);
                                }
                            });

                    }
                });

            }
        });
    }
}
