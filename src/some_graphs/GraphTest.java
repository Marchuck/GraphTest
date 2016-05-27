package some_graphs;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;

import java.util.Random;

/**
 * @author Lukasz Marczak
 * @since 27.04.16.
 */
public class GraphTest {

    public static String styleSheet = "graph {\n" +
            "\tfill-color: white;\n" +
            "}\n" +
            "\n" +
            "node {\n" +
            "\tsize: 10px, 15px;\n" +
            "\tshape: box;\n" +
            "\tfill-color: green;\n" +
            "\tstroke-mode: plain;\n" +
            "\tstroke-color: yellow;\n" +
            "\tshadow-color: black;\n" +
            "}\n" +
            "\n" +
            "node#A {\n" +
            "\tfill-color: blue;\n" +
            "}\n" +
            "\n" +
            "node:clicked {\n" +
            "\tfill-color: red;\n" +
            "}\n" +
            "\n" +
            "edge#AB{\n" +
            "    arrow-shape: arrow;\n" +
            "    shape: cubic-curve;\n" +
            "}";

    public static void main(String[] args) {

//        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        Graph graph = new MultiGraph("name");

        graph.addAttribute("ui.stylesheet", styleSheet);
        int j = 0;
//        Random seed = new Random();
        graph.addNode("Node" + j);
        for (j = 1; j < 30; j++) {
            graph.addNode("Node" + j);
            graph.addEdge("Edge" + j + "|" + (j - 1), "Node" + j, "Node" + (j - 1));
            if (j > 10) graph.addEdge("Edge" + j + "|" + (j - 10), "Node" + j, "Node" + (j - 10));
//            if (j > 10) {
//                try {
//                    graph.addEdge("Edge" + j + "|" + (j - seed.nextInt(10)), "Node" + j, "Node" + (j - seed.nextInt(10)));
//                } catch (Exception ignored) {
//                }
//            }
        }
        graph.addEdge("Edge" + j + "|0", "Node" + (j - 1), "Node" + (0));
//        Node nodeA = graph.addNode("A");
//        Node nodeB = graph.addNode("B");
//        Node nodeC = graph.addNode("C");
//        Node nodeD = graph.addNode("D");
//
//        Edge edgeA = graph.addEdge("AB", "A", "B");
//        Edge edgeB = graph.addEdge("CB", "C", "B");
//        Edge edgeC = graph.addEdge("CD", "C", "D");
//        Edge edgeD = graph.addEdge("AD", "A", "D");
        //edgeA.addAttribute("ui.style", "fill-color: rgb(233,33,80);");
        //edgeA.addAttribute("ui.style", "padding: 5px;");
//        edgeA.addAttribute("ui.style", "shape: cubic-curve;");
//        edgeB.addAttribute("ui.style", "shape: cubic-curve; fill-color: rgb(233,33,80);");
//        edgeC.addAttribute("ui.style", "shape: cubic-curve; fill-color: rgb(23,33,180);");
//        edgeD.addAttribute("ui.style", "shape: cubic-curve; fill-color: rgb(63,133,80);");


        graph.display();
    }
}
