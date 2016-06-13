package ui;

import ui.agds.AgdsApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


/**
 * @author Lukasz
 * @since 14.05.2016.
 */
public class ClassifyItemPopupMenu {

    private ClassifyItemPopupMenu(final AgdsClassificationProxy proxy) {

        int propertyCount = AgdsApplication.getInstance().numberOfAttributes;

        JPanel rootPanel = new JPanel(new GridLayout(3, 1));
        JPanel jpanel = new JPanel(new GridLayout(2, 1));
        JPanel subPanel = new JPanel(new GridLayout(1, propertyCount));
        JLabel jLabel = new JLabel("Insert new Leaf params");
        final TextField[] textFields = new TextField[propertyCount];
        JButton classifyMenuItem = new JButton("Classify");
        for (int j = 0; j < propertyCount; j++) {
            textFields[j] = new TextField();
            subPanel.add(textFields[j]);
        }

        jpanel.add(subPanel);
        JDialog jd = new JDialog();
        rootPanel.add(jLabel);
        rootPanel.add(jpanel);

        classifyMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                java.util.List<String> inputValues = new ArrayList<>();
                for (TextField tv : textFields) inputValues.add(tv.getText().isEmpty() ? "-1" : tv.getText());
                proxy.onItemToClassify(inputValues);
            }
        });
        rootPanel.add(classifyMenuItem);
        jd.add(rootPanel);

        jd.setMinimumSize(new Dimension(300, 300));
        jd.setVisible(true);
    }

    public static void create(AgdsClassificationProxy proxy) {
        new ClassifyItemPopupMenu(proxy);
    }
}
