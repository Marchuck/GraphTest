package ui.agds;

import common.Utils;
import org.graphstream.ui.view.Viewer;
import some_graphs.GraphVisualiser;
import topics.agds.AGDSAlgorithm;
import topics.agds.GenericAgdsEngine;
import ui.AgdsAlgorithmProxy;
import ui.agds.tabs.SimilarityPaneConnector;
import ui.agds.tabs.classify.ClassificationPaneConnector;
import ui.agds.tabs.classify.ClassifyItem;
import ui.agds.tabs.classify.SingleValueChooser;
import ui.connector.AgdsConnectable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Lukasz
 * @since 26.05.2016.
 */
public class AgdsGUI extends JFrame implements AgdsConnectable {
    public Viewer graphViewer;


    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JList similarityList;
    private JButton searchSimilarButton;
    private JButton showGraphButton;
    private JList<ClassifyItem> classifyItemList;
    private JButton addItemButton;
    private JButton submitItemsButton;

    /**
     * Connectors
     */
    private ClassificationPaneConnector classificationPaneConnector;
    private SimilarityPaneConnector similarityPaneConnector;
    private CorrelationPaneConnector correlationPaneConnector;

    /**
     * Algorithm here
     */
    public GenericAgdsEngine agdsEngine;
    public GraphVisualiser agdsVisualiser;
    AGDSAlgorithm agdsAlgorithm;


    public AgdsGUI(String title) throws HeadlessException {
        super(title);
        LOG("AgdsGUI");
        setContentPane(panel1);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        buildAgds();
        setupPanes();
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
                new SingleValueChooser(submitItemsButton, "Enter threshold", new SingleValueChooser.SendAction() {
                    @Override
                    public void onSend(String value) {
                        Double d = Double.parseDouble(value);
                        if (Utils.isBetween(0, 1, d)) {
                            AgdsAlgorithmProxy.onItemToClassify(agdsEngine, items, d, new GenericAgdsEngine.ResultCallback() {
                                @Override
                                public void onComputed() {
                                    LOG("DONE");
                                }
                            });

                        }

                    }
                });
            }
        });
    }

    private void setupSimilarityPane() {
        similarityPaneConnector = new SimilarityPaneConnector(this);
    }

    private void setupCorrelationPane() {
        correlationPaneConnector = new CorrelationPaneConnector(this);
    }

    private static void LOG(String s) {
        System.out.println(s);
    }
}
