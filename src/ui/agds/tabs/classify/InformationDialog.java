package ui.agds.tabs.classify;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Lukasz
 * @since 26.05.2016.
 */
public class InformationDialog extends JFrame {


    JDialog jd;

    public JDialog getRoot() {
        return jd;
    }

    public InformationDialog(JComponent parent, String title) {
        super(title);
        JPanel rootPanel = new JPanel(new GridLayout(3, 1));
          JButton jButton = new JButton("OK");

        jd = new JDialog();
        rootPanel.add(new JLabel(title));

        rootPanel.add(jButton);

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getRoot().setVisible(false);
                InformationDialog.this.dispose();
            }
        });
        rootPanel.add(jButton);
        jd.add(rootPanel);
        jd.pack();
        jd.setBounds(parent.getX() + 100, parent.getY() + 100, 200, 100);
        jd.setVisible(true);

    }
}
