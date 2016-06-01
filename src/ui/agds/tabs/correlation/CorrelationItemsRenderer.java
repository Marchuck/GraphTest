package ui.agds.tabs.correlation;

import common.Utils;

import javax.swing.*;
import java.awt.*;

/**
 * @author Lukasz
 * @since 26.05.2016.
 */
public class CorrelationItemsRenderer implements ListCellRenderer<CorrelationItem> {

    final JLabel label = new JLabel();

    @Override

    public Component getListCellRendererComponent(JList<? extends CorrelationItem> list, CorrelationItem value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {

        label.setText(value.name);
        if (value.isSelected == 0) label.setForeground(classBackground(value));
        else if (value.isSelected == 1) label.setForeground(Color.RED);
        else if (value.isSelected == 2) label.setForeground(Color.GREEN);
        return label;
    }

    private Color classBackground(CorrelationItem value) {
        Utils.log("classBackground");
        if (value.recordNode.getClassNode().getName().contains("versicolor")) {
            Utils.log("versicolor");
            return Color.GRAY;
        } else if (value.recordNode.getClassNode().getName().contains("setosa")) {
            Utils.log("setosa");
            return Color.BLACK;
        } else {
            Utils.log("virginica");
            return Color.LIGHT_GRAY;
        }
    }
}