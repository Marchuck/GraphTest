package some_graphs;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

/**
 * @author Lukasz Marczak
 * @since 27.04.16.
 */
public class GraphTest {

    public static void main(String[] args) {

        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
         Graph graph = new MultiGraph("name");

        Node nodeA = graph.addNode("A");
        Node nodeB = graph.addNode("B");
        Node nodeC = graph.addNode("C");
        Node nodeD = graph.addNode("D");

        Edge edgeA = graph.addEdge("AB", "A", "B");
        Edge edgeB = graph.addEdge("CB", "C", "B");
        Edge edgeC = graph.addEdge("CD", "C", "D");
        Edge edgeD = graph.addEdge("AD", "A", "D");
        //edgeA.addAttribute("ui.style", "fill-color: rgb(233,33,80);");
        //edgeA.addAttribute("ui.style", "padding: 5px;");
        edgeA.addAttribute("ui.style", "shape: cubic-curve;");
        edgeB.addAttribute("ui.style", "shape: cubic-curve; fill-color: rgb(233,33,80);");
        edgeC.addAttribute("ui.style", "shape: cubic-curve; fill-color: rgb(23,33,180);");
        edgeD.addAttribute("ui.style", "shape: cubic-curve; fill-color: rgb(63,133,80);");


        graph.display();
    }
}
