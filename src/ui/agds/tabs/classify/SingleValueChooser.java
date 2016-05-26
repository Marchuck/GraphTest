package ui.agds.tabs.classify;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Lukasz
 * @since 26.05.2016.
 */
public class SingleValueChooser extends JFrame {
    public interface SendAction {
        void onSend(String value);
    }

    public SingleValueChooser(JComponent c, String title, final SendAction action) {
        super(title);
        JPanel rootPanel = new JPanel(new GridLayout(3, 1));
        final TextField tv0 = new TextField();
        JButton jButton = new JButton("OK");

        JDialog jd = new JDialog();
        rootPanel.add(new JLabel(title));
        rootPanel.add(tv0);
        rootPanel.add(jButton);

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text0 = tv0.getText();
                SingleValueChooser.this.dispose();
                if (!text0.isEmpty()) action.onSend(text0);
            }
        });
        rootPanel.add(jButton);
        jd.add(rootPanel);
        jd.pack();
        jd.setBounds(c.getX() + 100, c.getY() + 100, 200, 100);
        jd.setVisible(true);

    }
}
