package ui.agds.tabs.classify;

import common.Utils;
import topics.agds.engine.AgdsEngine;
import topics.agds.nodes.PropertyNode;
import topics.agds.nodes.RecordNode;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz
 * @since 13.06.2016.
 */
public class RecordNodeRenderer implements javax.swing.ListCellRenderer<RecordNode> {
    @Override
    public Component getListCellRendererComponent(JList<? extends RecordNode> list, RecordNode recordNode, int index, boolean isSelected, boolean cellHasFocus) {
        int numberOfProperties = recordNode.getNodes().size();
        JPanel subPanel = new JPanel(new GridLayout(1, numberOfProperties + 1));
        if (isSelected) subPanel.setForeground(Color.YELLOW);
        else subPanel.setForeground(Color.white);

        if (index == 0) {
            java.util.List<String> propertyNames = getPropertyNodesAsStrings();
            for (String propertyName : propertyNames) {
                JLabel label = new JLabel(propertyName);
                subPanel.add(label);
            }
            JLabel label = new JLabel("Class name");
            subPanel.add(label);
            subPanel.setForeground(Color.ORANGE);
            return subPanel;
        }

        for (int j = 0; j < numberOfProperties; j++) {
            JLabel label = new JLabel(String.valueOf(recordNode.getNodes().get(j).getValue()));
            subPanel.add(label);
        }
        subPanel.add(new JLabel(recordNode.getClassNode().getName()));
        return subPanel;
    }

    private static List<String> getPropertyNodesAsStrings() {
        AgdsEngine engine = AgdsEngine.getInstance();
        List<String> asStrings = new ArrayList<>();
        for (PropertyNode propertyNode : engine.getPropertyNodes()) asStrings.add(propertyNode.getName());
        return asStrings;
    }
}
