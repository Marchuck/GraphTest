package ui.agds.tabs.classify;

import topics.agds.nodes.RecordNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Lukasz
 * @since 26.05.2016.
 */
public class InformationDialog extends JFrame {

    private JDialog jd;

    public InformationDialog(JButton parent, String title, List<RecordNode> results) {
        super(title);
        Set<RecordNode> set = new HashSet<>();
        set.addAll(results);
        results.clear();
        results.add(new RecordNode(null));
        results.addAll(set);

        JPanel rootPanel = new JPanel(new GridLayout(3, 1));
        JButton jButton = new JButton("OK");

        jd = new JDialog();
        rootPanel.add(new JLabel(title));

        JScrollPane scrollPane = new JScrollPane();
        JList<RecordNode> list = new JList<>();
        final DefaultListModel<RecordNode> model = new DefaultListModel<>();
        for (RecordNode e : results) model.addElement(e);
        list.setModel(model);
        list.setCellRenderer(new RecordNodeRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        scrollPane.setViewportView(list);

        rootPanel.add(scrollPane);

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
        jd.setBounds(parent.getX() + 100, parent.getY() + 100, 450, 300);
        jd.setVisible(true);

    }

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
