package ui.agds.tabs.similar;

import javax.swing.*;
import java.awt.*;

/**
 * @author Lukasz
 * @since 26.05.2016.
 */
public class SimilarItemsRenderer implements ListCellRenderer<SimilarItem> {
    @Override
    public Component getListCellRendererComponent(JList<? extends SimilarItem> list, SimilarItem value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        final JLabel label = new JLabel(value.name);

        if (value.isSelected == 1) label.setForeground(Color.RED);
        else if (value.isSelected == 0) {
            label.setForeground(value.className.contains("etosa") ? Color.BLACK :
                    value.className.contains("olor") ? Color.gray : Color.LIGHT_GRAY);
        } else label.setForeground(Color.blue);
        return label;
    }
}