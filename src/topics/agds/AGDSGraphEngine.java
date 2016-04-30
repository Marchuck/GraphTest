//package topics.agds;
//
//import common.Item;
//import common.ListPrinter;
//import common.Log;
//import topics.agds.nodes.*;
//
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//
///**
// * @author Lukasz Marczak
// * @since 23.04.16.
// */
//public class AGDSGraphEngine {
//
//    @Deprecated
//    private List<String> classNames;
//    private List<PropertyDrawableNode> propertyNodes = new ArrayList<>();
//    private List<Item> dataSet;
//
//    private PropertyDrawableNode leafLength;
//    private PropertyDrawableNode leafWidth;
//    private PropertyDrawableNode petalLength;
//    private PropertyDrawableNode petalWidth;
//
//    private ClassDrawableNode setosaNode;
//    private ClassDrawableNode virginicaNode;
//    private ClassDrawableNode versicolorNode;
//
//    public AGDSGraphEngine(List<String> classNames, List<Item> dataSet) {
//        this.classNames = classNames;
//        this.dataSet = dataSet;
//    }
//
//    public static AGDSGraphEngine initWith(List<String> classNames, List<Item> dataSet) {
//        return new AGDSGraphEngine(classNames, dataSet);
//    }
//
//    public AGDSGraphEngine fill() {
//        //create rootNode
//        rootNode = new AGDSNode('0').withName("Param");
//        //create propertyNodes
//        leafLength = new PropertyDrawableNode("Leaf-length");
//        leafWidth = new PropertyDrawableNode("Leaf-width");
//        petalLength = new PropertyDrawableNode("Petal-length");
//        petalWidth = new PropertyDrawableNode("Petal-width");
//        fillPropertyNodesList();
//
//        //create classNodes
//        setosaNode = new ClassDrawableNode("Iris-setosa");
//        versicolorNode = new ClassDrawableNode("Iris-versicolor");
//        virginicaNode = new ClassDrawableNode("Iris-virginica");
//
//        //create connections
////        klassNode = new ClassDrawableNode("Klasa").addNodes(setosaNode, versicolorNode, virginicaNode);
//
//        rootNode.addNodes(leafLength, leafWidth, petalLength, petalWidth, klassNode);
//
////        leafLength.addNode(rootNode);
////        leafWidth.addNode(rootNode);
////        petalLength.addNode(rootNode);
////        petalWidth.addNode(rootNode);
//
//        List<AGDSNode> rNodes = new ArrayList<>();
//
//        for (int j = 0; j < dataSet.size(); j++) {
//            Item item = dataSet.get(j);
//            AbstractNode rNode = new RecordNode("R_" + (j + 1));
//            AbstractNode _leafLength = new ValueDrawableNode("Leaf-length_" + item.values[0]).withValue(item.values[0]).addNode(rNode);
//            AbstractNode _leafWidth = new ValueDrawableNode("Leaf-width_" + item.values[1]).withValue(item.values[1]).addNode(rNode);
//            AbstractNode _petalLength = new ValueDrawableNode("Petal-length_" + item.values[2]).withValue(item.values[2]).addNode(rNode);
//            AbstractNode _petalWidth = new ValueDrawableNode("Petal-width_" + item.values[3]).withValue(item.values[3]).addNode(rNode);
//
//            leafLength.addNode(_leafLength);
//            leafWidth.addNode(_leafWidth);
//            petalLength.addNode(_petalLength);
//            petalWidth.addNode(_petalWidth);
//            rNode.addNodes(_leafLength, _leafWidth, _leafWidth, _petalLength, _petalWidth);
//
//            AGDSNode klazzNode = getClassNode(item);
//            rNode.addNode(klazzNode);
//            klazzNode.addNode(rNode).sort();
//
//            //connect each other nodes
//            int lastIndex = rNodes.size();
//            if (lastIndex > 0) {
//                rNodes.get(lastIndex - 1).addNode(rNode);
//            } else {
//                firstRNode = rNode;
//            }
//            rNodes.add(rNode);
//
//            for (AbstractNode node : rNodes) node.sort();
//        }
//        return this;
//    }
//
//    private void fillPropertyNodesList() {
//        propertyNodes.add(leafLength);
//        propertyNodes.add(leafWidth);
//        propertyNodes.add(petalLength);
//        propertyNodes.add(petalWidth);
//    }
//
//    @Deprecated
//    public static List<AbstractNode> getSelectedNodes(AbstractNode node, NodeStrategy filter) {
//        char type = 'p';
//        if (filter == NodeStrategy.VALUE) {
//            type = 'v';
//        } else if (filter == NodeStrategy.CLASS) {
//            type = 'c';
//
//        } else if (filter == NodeStrategy.RECORD) {
//            type = 'r';
//        }
//        List<AbstractNode> nodes = new LinkedList<>();
//        for (AbstractNode node1 : node.getNodes()) {
//            if (node1.typeOf(type)) nodes.add(node1);
//        }
//        return nodes;
//    }
//
//
//    public AGDSGraphEngine printSetosas() {
//        Log.d("\n\n");
//
//        for (AbstractNode node : setosaNode.getNodes()) {
//            Log.d("print Setosas: " + node.getName() + ", " + node.getNodes().size());
//        }
//        return this;
//    }
//
//    public AGDSGraphEngine printVersicolors() {
//        Log.d("\n\n");
//        Log.d(" printVersicolors " + printNodes(versicolorNode));
//        return this;
//    }
//
//    public AGDSGraphEngine printVirginicas() {
//        Log.d("\n\n");
//        Log.d("printVirginicas " + printNodes(virginicaNode));
//        return this;
//    }
//
//    public AGDSNode getClassNode(Item item) {
//        if (item.name.equalsIgnoreCase(setosaNode.getName())) return setosaNode;
//        else if (item.name.equalsIgnoreCase(virginicaNode.getName())) return virginicaNode;
//        else return versicolorNode;
//    }
//
//    public String printNodes(AbstractNode node) {
//
//        return new ListPrinter<>(new ListPrinter.DefaultStrategy<AbstractNode>() {
//            @Override
//            public String nextItemRow(AbstractNode abstractNode) {
//                return abstractNode.getName();
//            }
//        }).print(node.getNodes());
//    }
//
//    public void draw() {
//        //// TODO: 23.04.16
//        new AGDSGraphDrawerEngine(rootNode).draw();
//    }
//}
