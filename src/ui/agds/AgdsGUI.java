package ui.agds;

import common.Utils;
import org.graphstream.ui.view.Viewer;
import some_graphs.GraphVisualiser;
import topics.agds.AGDSAlgorithm;
import topics.agds.GenericAgdsEngine;
import ui.AgdsAlgorithmProxy;
import ui.agds.tabs.classify.ClassificationPaneConnector;
import ui.agds.tabs.classify.ClassifyItem;
import ui.agds.tabs.classify.SingleValueChooser;
import ui.agds.tabs.correlation.CorrelationItem;
import ui.agds.tabs.correlation.CorrelationPaneConnector;
import ui.agds.tabs.similar.SimilarItem;
import ui.agds.tabs.similar.SimilarityPaneConnector;
import ui.connector.ResultCallback;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;

/**
 * @author Lukasz
 * @since 26.05.2016.
 */
public class AgdsGUI extends JFrame   {
    public Viewer graphViewer;


    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JList<SimilarItem> similarityList;
    private JButton searchSimilarButton;
    private JButton showGraphButton;
    private JList<ClassifyItem> classifyItemList;
    private JButton addItemButton;
    private JButton submitItemsButton;
    private JList<CorrelationItem> correlationList;
    private JButton computeCorrelationButton;

    /**
     * Connectors
     */
    private ClassificationPaneConnector classificationPaneConnector;
    private SimilarityPaneConnector similarityPaneConnector;
    private CorrelationPaneConnector correlationPaneConnector;
    /**
     * temporary windows
     */
    private SingleValueChooser thresholdEnterer;
    /**
     * Algorithm here
     */
    public GenericAgdsEngine agdsEngine;
    public GraphVisualiser agdsVisualiser;
    private boolean graphVisible;
    AGDSAlgorithm agdsAlgorithm;


    public AgdsGUI(String title) throws HeadlessException {
        super(title);
        LOG("AgdsGUI");
        setContentPane(panel1);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        buildAgds();
        //setupPanes();
        setMinimumSize(new Dimension(1600 / 4, 900 / 4));
        pack();
        setVisible(true);
    }

    private void buildAgds() {

    }

    private void setupPanes() {
        LOG("setupPanes");
        /*
        Component graphComponent = tabbedPane1.getTabComponentAt(0);
        Component classificationComponent = tabbedPane1.getTabComponentAt(1);
        Component similarityComponent = tabbedPane1.getTabComponentAt(2);
        Component correlationComponent = tabbedPane1.getTabComponentAt(3);
        */
        setupGraphPane();
        setupClassifyPane();
        setupSimilarityPane();
        setupCorrelationPane();
    }

    private void setupGraphPane() {
        showGraphButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (agdsVisualiser != null) {
                    if (!graphVisible) {
                        agdsVisualiser.showGraph();
                        showGraphButton.setText("Hide");
                    } else {
                        agdsVisualiser.hideGraph();
                        showGraphButton.setText("Show");
                    }
                    graphVisible = !graphVisible;
                }
            }
        });
    }

    private void setupClassifyPane() {
        LOG("setupClassifyPane");
        classificationPaneConnector = new ClassificationPaneConnector(classifyItemList, addItemButton,
                submitItemsButton, new ClassificationPaneConnector.Interaction() {
            @Override
            public void onItemsSubmit(final List<ClassifyItem> items) {
                LOG("classyfying items");
                for (ClassifyItem item : items) {
                    LOG(item.toString());
                }
                //invoke popUp to set threshold
                thresholdEnterer = new SingleValueChooser(submitItemsButton, "Enter threshold", new SingleValueChooser.SendAction() {
                    @Override
                    public void onSend(String value) {
                        Double d = Double.parseDouble(value);
                        if (Utils.isBetween(0, 1, d)) {
                            AgdsAlgorithmProxy.onItemToClassify(agdsEngine, items, d, new ResultCallback<String>() {
                                @Override
                                public void onComputed(Collection<String> result) {

                                }
                            });
                        }
                        thresholdEnterer.setVisible(false);
                        thresholdEnterer.dispose();
                    }
                });
            }
        });
    }

    private void setupSimilarityPane() {
        similarityPaneConnector = new SimilarityPaneConnector(similarityList, submitItemsButton, agdsEngine);
    }

    private void setupCorrelationPane() {
        correlationPaneConnector = new CorrelationPaneConnector(correlationList,computeCorrelationButton,agdsEngine);

    }

    private static void LOG(String s) {
        System.out.println(s);
    }

    public void setAgdsEngine(GenericAgdsEngine agdsEngine) {
        this.agdsEngine = agdsEngine;
        setupPanes();
    }

}
