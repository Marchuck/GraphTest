package topics.agds;

import common.Item;
import common.ListPrinter;
import common.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 23.04.16.
 */
public class AGDSGraphEngine {
    private List<String> classNames;
    private List<Item> dataSet;

    private AGDSNode rootNode;

    private AGDSNode klassNode;

    private AGDSNode leafLength;
    private AGDSNode leafWidth;
    private AGDSNode petalLength;
    private AGDSNode petalWidth;

    private AGDSNode setosaNode;
    private AGDSNode virginicaNode;
    private AGDSNode versicolorNode;
    private AGDSNode firstRNode;

    public AGDSGraphEngine(List<String> classNames, List<Item> dataSet) {
        this.classNames = classNames;
        this.dataSet = dataSet;
    }

    public static AGDSGraphEngine initWith(List<String> classNames, List<Item> dataSet) {
        return new AGDSGraphEngine(classNames, dataSet);
    }

    public AGDSGraphEngine fill() {
        //create rootNode
        rootNode = new AGDSNode('0').withName("Param");
        //create propertyNodes
        leafLength = new AGDSNode('p').withName("Leaf-length");
        leafWidth = new AGDSNode('p').withName("Leaf-width");
        petalLength = new AGDSNode('p').withName("Petal-length");
        petalWidth = new AGDSNode('p').withName("Petal-width");
        //create classNodes
        setosaNode = new AGDSNode('c').withName("Iris-setosa");
        versicolorNode = new AGDSNode('c').withName("Iris-versicolor");
        virginicaNode = new AGDSNode('c').withName("Iris-virginica");

        //create connections
        klassNode = new AGDSNode('k').withName("Klasa").addNodes(setosaNode, versicolorNode, virginicaNode);

        rootNode.addNodes(leafLength, leafWidth, petalLength, petalWidth, klassNode);

//        leafLength.addNode(rootNode);
//        leafWidth.addNode(rootNode);
//        petalLength.addNode(rootNode);
//        petalWidth.addNode(rootNode);

        List<AGDSNode> rNodes = new ArrayList<>();

        for (int j = 0; j < dataSet.size(); j++) {
            Item item = dataSet.get(j);
            AGDSNode rNode = new AGDSNode('r').withName("R_" + (j + 1));
            AGDSNode _leafLength = new AGDSNode('v').withName("Leaf-length").withValue(item.values[0]).addNode(rNode);
            AGDSNode _leafWidth = new AGDSNode('v').withName("Leaf-width").withValue(item.values[1]).addNode(rNode);
            AGDSNode _petalLength = new AGDSNode('v').withName("Petal-length").withValue(item.values[2]).addNode(rNode);
            AGDSNode _petalWidth = new AGDSNode('v').withName("Petal-width").withValue(item.values[3]).addNode(rNode);

            leafLength.addNode(_leafLength);
            leafWidth.addNode(_leafWidth);
            petalLength.addNode(_petalLength);
            petalWidth.addNode(_petalWidth);
            rNode.addNodes(_leafLength, _leafWidth, _leafWidth, _petalLength, _petalWidth);

            AGDSNode klazzNode = getClassNode(item);
            rNode.addNode(klazzNode);
            klazzNode.addNode(rNode).sort();

            //connect each other nodes
            int lastIndex = rNodes.size();
            if (lastIndex > 0) {
                rNodes.get(lastIndex - 1).addNode(rNode);
            } else {
                firstRNode = rNode;
            }
            rNodes.add(rNode);

            for (AbstractNode node : rNodes) node.sort();
        }
        return this;
    }

    public static List<AbstractNode> getSelectedNodes(AbstractNode node, NodeStrategy filter) {
        char type = 'p';
        if (filter == NodeStrategy.VALUE) {
            type = 'v';
        } else if (filter == NodeStrategy.CLASS) {
            type = 'c';

        } else if (filter == NodeStrategy.RECORD) {
            type = 'r';
        }
        List<AbstractNode> nodes = new LinkedList<>();
        for (AbstractNode node1 : node.getNodes()) {
            if (node1.typeOf(type)) nodes.add(node1);
        }
        return nodes;
    }

    public AGDSGraphEngine printSetosas() {
        Log.d("\n\n");

        for (AbstractNode node : setosaNode.getNodes()) {
            Log.d("print Setosas: " + node.getName() + ", " + node.getNodes().size());
        }
        return this;
    }

    public AGDSGraphEngine printVersicolors() {
        Log.d("\n\n");
        Log.d(" printVersicolors " + printNodes(versicolorNode));
        return this;
    }

    public AGDSGraphEngine printVirginicas() {
        Log.d("\n\n");
        Log.d("printVirginicas " + printNodes(virginicaNode));
        return this;
    }

    public AGDSNode getClassNode(Item item) {
        if (item.name.equalsIgnoreCase(setosaNode.getName())) return setosaNode;
        else if (item.name.equalsIgnoreCase(virginicaNode.getName())) return virginicaNode;
        else return versicolorNode;
    }

    public String printNodes(AbstractNode node) {

        return new ListPrinter<>(new ListPrinter.DefaultStrategy<AbstractNode>() {
            @Override
            public String nextItemRow(AbstractNode abstractNode) {
                return abstractNode.getName();
            }
        }).print(node.getNodes());
    }

    public void draw() {
        //// TODO: 23.04.16
        new AGDSGraphDrawerEngine(rootNode).draw();
    }
}
