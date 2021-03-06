package ui;

import some_graphs.GraphMerger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * @author Lukasz Marczak
 * @since 24.03.16.
 */
public class AgdsAlgorithmGUI extends JPanel {

    /**
     * Prezentacja:
     * (1) prosta animacja AGDSConstants, jak dodawane są elementy
     * (2) klasyfikacja węzła/węzłów z poza grafu do węzłów z grafu(zadany próg np 50%)
     * (3) podobieństwo węzła/węzłów z grafu do
     * (3) korelacja pomiędzy dwoma węzłami z grafu
     * <p>
     * co osiągnięto, jakie wady, po co to, użycie
     */
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;

    public AgdsAlgorithmGUI(SingleTab... tabs) {
//        super();
        super(new GridLayout(1, 1));
        JTabbedPane tabbedPane = new JTabbedPane();
        for (SingleTab tab : tabs) {
            tabbedPane.addTab(tab.tabTitle, tab.jComponent);
        }
        tabbedPane.setPreferredSize(new Dimension(3 * WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2));

        add(tabbedPane);
        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }

    public AgdsAlgorithmGUI() {
//        super();
        super(new GridLayout(1, 1));

        final JTabbedPane tabbedPane = new JTabbedPane();
        ImageIcon icon = createImageIcon("images/share.png");

        JComponent panel1 = makeTextPanel("Panel #1");

//        tabbedPane.addTab("Graph",new GraphView());

        final GraphMerger graphMerger = new GraphMerger();
        final AgdsClassificationProxy proxy = new AgdsClassificationProxy(graphMerger);
        //first tab: graph
        tabbedPane.addTab("Graph", graphMerger);
        //second tab: options
        JButton btnLegend = new JButton("show legend");

        btnLegend.addActionListener(new ActionListener() {
            @Override
            public   void actionPerformed(ActionEvent e) {
                graphMerger.getGraphVisualiser().switchLegend();
            }
        });
        final JButton btnClassify = new JButton("classifyMenuItem item");
        btnClassify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.print("event: " + e.getWhen());
                ClassifyItemPopupMenu.create(proxy);
            }
        });

        JPanel layout = new JPanel(new GridLayout(3, 3));
        layout.add(btnLegend);
        layout.add(btnClassify);
        tabbedPane.add(layout);

        tabbedPane.addTab("Tab 1", icon, panel1,
                "Does nothing");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        JComponent panel2 = makeTextPanel("Panel #2");
        tabbedPane.addTab("Tab 2", icon, panel2,
                "Does twice as much nothing");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        JComponent panel3 = makeTextPanel("Panel #3");
        tabbedPane.addTab("Tab 3", icon, panel3,
                "Still does nothing");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

        JComponent panel4 = makeTextPanel(
                "Panel #4 (has createTitle preferred size of " + WINDOW_WIDTH + " x " + WINDOW_HEIGHT + ").");
        panel4.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        tabbedPane.addTab("Tab 4", icon, panel4,
                "Does nothing at all");
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);

        //Add the tabbed pane to this panel.
        add(tabbedPane);

        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }

    public static JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);

        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = AgdsAlgorithmGUI.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from
     * the event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("METHODS OF KNOWLEDGE ENGINEERING");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new AgdsAlgorithmGUI(), BorderLayout.CENTER);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    private static void createAndShowGUI(SingleTab... tabs) {
        //Create and set up the window.
        JFrame frame = new JFrame("METHODS OF KNOWLEDGE ENGINEERING");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new AgdsAlgorithmGUI(tabs), BorderLayout.CENTER);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void showTabs(final SingleTab... tabs) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI(tabs);
                setupLookAndFeel();
            }
        });
    }

    public static void main(String[] args) {
        //Schedule createTitle job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
                setupLookAndFeel();
            }
        });
    }

    public static void setupLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}