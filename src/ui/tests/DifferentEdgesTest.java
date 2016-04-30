package ui.tests;

import agds.AGDS;
import common.RunAlgorithm;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.Viewer;

/**
 * @author Lukasz Marczak
 * @since 25.04.16.
 */
public class DifferentEdgesTest implements RunAlgorithm {


    public static void main(String[] args) throws Exception {


        new DifferentEdgesTest().run();

    }

    @Override
    public void run() throws Exception {
        Graph graph = new MultiGraph("Different edges length");
        graph.setStrict(true);
        graph.setAutoCreate(true);

        Viewer viewer = graph.display();
        Node nodeA = graph.addNode("A");
        Node nodeB = graph.addNode("B");
        Node nodeC = graph.addNode("C");
        Node nodeD = graph.addNode("D");

        nodeA.addAttribute("ui.label", nodeA.getId());
        nodeB.addAttribute("ui.label", nodeB.getId());
        nodeC.addAttribute("ui.label", nodeC.getId());
        nodeD.addAttribute("ui.label", nodeD.getId());

        nodeA.addAttribute("ui.style", AGDS.RECORD_NODE_STYLESHEET);
        nodeB.addAttribute("ui.style", AGDS.VALUE_NODE_STYLESHEET);
        nodeC.addAttribute("ui.style", AGDS.CLASS_NODE_STYLESHEET);
        nodeD.addAttribute("ui.style", AGDS.PROPERTY_NODE_STYLESHEET);

        Edge abEdge = graph.addEdge("AB", "A", "B");
        Edge bcEdge = graph.addEdge("BC", "C", "B");
        Edge cdEdge = graph.addEdge("CD", "C", "D");
        Edge adEdge = graph.addEdge("DA", "A", "D");
        adEdge.setAttribute("ui.color", "red");
//        abEdge.setAttribute("ui.style", AGDS.RECORD_NODE_STYLESHEET);
        abEdge.setAttribute("layout.weight", 0.9);
        bcEdge.setAttribute("layout.weight", 1.1);
        cdEdge.setAttribute("layout.weight", 0.7);
        adEdge.setAttribute("layout.weight", 0.1);
//        adEdge.setAttribute("layout.width", 0.1);

        graph.display();
    }
}