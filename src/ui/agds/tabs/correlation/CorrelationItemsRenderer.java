package ui.agds.tabs.correlation;

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
        if (value.isSelected == 0) label.setForeground(Color.gray);
        else if (value.isSelected == 1) label.setForeground(Color.RED);
        else if (value.isSelected == 2) label.setForeground(Color.ORANGE);
        return label;
    }
}