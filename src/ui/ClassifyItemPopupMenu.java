package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Lukasz
 * @since 14.05.2016.
 */
public class ClassifyItemPopupMenu {

    private ClassifyItemPopupMenu(final AgdsAlgorithmProxy proxy) {

        JPanel rootPanel = new JPanel(new GridLayout(3, 1));
        JPanel jpanel = new JPanel(new GridLayout(2, 1));
        JPanel subPanel = new JPanel(new GridLayout(1, 4));
        JLabel jLabel = new JLabel("Insert new Leaf params");

        final TextField tv0 = new TextField();
        final TextField tv1 = new TextField();
        final TextField tv2 = new TextField();
        final TextField tv3 = new TextField();
        JButton classifyMenuItem = new JButton("Classify");

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
                proxy.onItemToClassify(text0, text1, text2, text3);
            }
        });
        rootPanel.add(classifyMenuItem);
        jd.add(rootPanel);

        jd.setMinimumSize(new Dimension(300, 300));
        jd.setVisible(true);
    }

    public static void create(AgdsAlgorithmProxy proxy) {
        new ClassifyItemPopupMenu(proxy);
    }
}
