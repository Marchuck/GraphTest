package ui.agds.tabs.classify;

import topics.agds.nodes.PropertyNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author Lukasz
 * @since 26.05.2016.
 */
public class SelectPropertyDialog extends JFrame {

    private JDialog jd;

    public JDialog getRoot() {
        return jd;
    }

    public interface SendAction {
        void onSend(java.util.List<PropertyNode> selectedNodes);
    }

    public SelectPropertyDialog(JComponent parent, final java.util.List<PropertyNode> nodes, final SendAction action) {
        super("Select property ");
        JPanel rootPanel = new JPanel(new GridLayout(nodes.size() + 1, 1));
        JButton jButton = new JButton("OK");

        jd = new JDialog();
        final JCheckBox[] radioButtons = new JCheckBox[nodes.size()];
        for (int j = 0; j < nodes.size(); j++) {
            radioButtons[j] = new JCheckBox(nodes.get(j).getName());
            rootPanel.add(radioButtons[j]);
        }

        rootPanel.add(jButton);

        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                java.util.List<PropertyNode> subList = new ArrayList<>();
                for (int j = 0; j < radioButtons.length; j++) {
                    if (radioButtons[j].isSelected())
                        subList.add(nodes.get(j));
                }
                action.onSend(subList);
                getRoot().setVisible(false);
                SelectPropertyDialog.this.dispose();
            }
        });
        rootPanel.add(jButton);
        jd.add(rootPanel);
        jd.pack();
        jd.setBounds(parent.getX() + 100, parent.getY() + 100, 200, 100);
        jd.setVisible(true);

    }
}
