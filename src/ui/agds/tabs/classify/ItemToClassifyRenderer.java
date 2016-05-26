package ui.agds.tabs.classify;

import javax.swing.*;
import java.awt.*;

/**
 * @author Lukasz
 * @since 26.05.2016.
 */
public class ItemToClassifyRenderer implements ListCellRenderer<ClassifyItem> {
    @Override
    public Component getListCellRendererComponent(JList<? extends ClassifyItem> list, ClassifyItem value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        JPanel subPanel = new JPanel(new GridLayout(1, 7));

        final JLabel tv0 = new JLabel(value.property0);
        final JLabel tv1 = new JLabel(value.property1);
        final JLabel tv2 = new JLabel(value.property2);
        final JLabel tv3 = new JLabel(value.property3);
        final JLabel stretch = new JLabel("|");

        subPanel.add(tv0);
        subPanel.add(stretch);
        subPanel.add(tv1);
        subPanel.add(stretch);
        subPanel.add(tv2);
        subPanel.add(stretch);
        subPanel.add(tv3);
        return subPanel;
    }

}