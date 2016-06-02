package ui.agds.tabs.classify;

import com.sun.javafx.beans.annotations.NonNull;
import ui.agds.AgdsApplication;

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
                    public void connect(List<String> s) {
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
            void connect(List<String> s);
        }

        private AddNewItem(Component parent, final AddConnector connector) {
            super("Add new item");

            JPanel rootPanel = new JPanel(new GridLayout(3, 1));
            JPanel jpanel = new JPanel(new GridLayout(2, 1));
            int propertiesCount = AgdsApplication.getInstance().numberOfAttributes;
            JPanel subPanel = new JPanel(new GridLayout(1, propertiesCount));
            JLabel jLabel = new JLabel("Insert new Leaf params");


            final TextField[] textFields = new TextField[propertiesCount];
            JButton classifyMenuItem = new JButton("Add");
            for (int j = 0; j < propertiesCount; j++) {
                textFields[j] = new TextField();
                subPanel.add(textFields[j]);
            }
            jpanel.add(subPanel);
            final JDialog jd = new JDialog();
            rootPanel.add(jLabel);
            rootPanel.add(jpanel);


            rootPanel.add(classifyMenuItem);
            jd.add(rootPanel);

            jd.setMinimumSize(new Dimension(260, 200));
            jd.pack();

            jd.setBounds(parent.getX() + 100, parent.getY() + 100, 200, 100);

            jd.setVisible(true);
            classifyMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    List<String> all = new ArrayList<String>();
                    for (TextField tv : textFields) {
                        String txt = tv.getText();
                        all.add(txt.isEmpty() ? "-1" : txt);
                    }
                    connector.connect(all);
                    jd.setVisible(false);
                    dispose();
                }
            });
        }
    }
}
