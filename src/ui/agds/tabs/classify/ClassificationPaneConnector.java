package ui.agds.tabs.classify;

import com.sun.javafx.beans.annotations.NonNull;

import javax.swing.*;
import java.awt.*;
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
public class ClassificationPaneConnector {
    public interface Interaction {
        void onItemsSubmit(List<ClassifyItem> items);
    }

    private AddNewItem currentAddItem;

    public ClassificationPaneConnector(JList<ClassifyItem> classifyItemList, final JButton addItemButton,
                                       final JButton submitButton, final @NonNull Interaction interaction) {

        final DefaultListModel<ClassifyItem> listModel = new DefaultListModel<>();

        classifyItemList.setModel(listModel);
        classifyItemList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList) evt.getSource();
                if (evt.getClickCount() == 1) {

                } else if (evt.getClickCount() == 2) {
                    // Double-click detected
                    int index = list.locationToIndex(evt.getPoint());
                    listModel.remove(index);

                }
            }
        });
        addItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentAddItem != null) currentAddItem.dispose();
                currentAddItem = new AddNewItem(addItemButton, new AddNewItem.AddConnector() {
                    @Override
                    public void connect(String... s) {
                        ClassifyItem classifyItem = new ClassifyItem(s);
                        listModel.addElement(classifyItem);
                    }
                });
            }
        });
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                java.util.List<ClassifyItem> items = new ArrayList<ClassifyItem>();
                for (int j = 0; j < listModel.getSize(); j++) {
                    items.add(listModel.getElementAt(j));
                }
                interaction.onItemsSubmit(items);
            }
        });
    }

    private static class AddNewItem extends JFrame {
        private interface AddConnector {
            void connect(String... s);
        }

        private AddNewItem(Component parent, final AddConnector connector) {
            super("Add new item");

            JPanel rootPanel = new JPanel(new GridLayout(3, 1));
            JPanel jpanel = new JPanel(new GridLayout(2, 1));
            JPanel subPanel = new JPanel(new GridLayout(1, 4));
            JLabel jLabel = new JLabel("Insert new Leaf params");

            final TextField tv0 = new TextField();

            final TextField tv1 = new TextField();
            final TextField tv2 = new TextField();
            final TextField tv3 = new TextField();
            JButton classifyMenuItem = new JButton("Add");

            subPanel.add(tv0);
            subPanel.add(tv1);
            subPanel.add(tv2);
            subPanel.add(tv3);
            jpanel.add(subPanel);
            JDialog jd = new JDialog();
            rootPanel.add(jLabel);
            rootPanel.add(jpanel);

            classifyMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String text0 = tv0.getText();
                    String text1 = tv1.getText();
                    String text2 = tv2.getText();
                    String text3 = tv3.getText();
                    connector.connect(text0, text1, text2, text3);
                    dispose();
                }
            });
            rootPanel.add(classifyMenuItem);
            jd.add(rootPanel);

            jd.setMinimumSize(new Dimension(300, 300));
            jd.pack();

            jd.setBounds(parent.getX() + 100, parent.getY() + 100, 200, 100);

            jd.setVisible(true);
        }
    }
}
