package topics.agds;

import agds.GraphDrawer;
import com.sun.istack.internal.Nullable;
import common.Item;
import common.ListPrinter;
import common.Log;
import topics.agds.nodes.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 23.04.16.
 */
public class GenericAgdsEngine {
    /**
     * interface which is responsible for drawing selected nodes and edges
     */
    @Nullable
    private GraphDrawer<AbstractNode> graphDrawer;

    private List<String> propertyNames;//for Iris: leaf length, petal width, etc.
    private List<String> classNames; //for Iris: Versicolor, Virginica, etc.
    /**
     * DrawableNode representation of properties (attributes)
     */
    private List<PropertyNode> propertyNodes = new ArrayList<>();
    /**
     * DrawableNode representation of classes
     */
    private List<ClassNode> classNodes = new ArrayList<>();
    /**
     * DrawableNode representation of next row
     */
    private List<RecordNode> recordNodes = new ArrayList<>();
    /**
     * source dataSet; based on this list graph will be created
     */
    private List<Item> dataSet;

    public GenericAgdsEngine(List<String> propertyNames, List<String> classNames, List<Item> dataSet) {

        this.propertyNames = propertyNames;
        this.classNames = classNames;
        this.dataSet = dataSet;
    }

    public static GenericAgdsEngine initWith(List<String> propertyNames, List<String> classNames, List<Item> dataSet) {
        return new GenericAgdsEngine(propertyNames, classNames, dataSet);
    }

    public GenericAgdsEngine buildGraph() {

        //create propertyNodes
        for (String s : propertyNames) {
            PropertyNode propertyNode = new PropertyNode(s);
            propertyNodes.add(propertyNode);
            safeDrawNode(propertyNode);
        }
        //create classNodes
        for (String s : classNames) {
            ClassNode classNode = new ClassNode(s);
            classNodes.add(classNode);
            safeDrawNode(classNode);
        }

        //create list of RecordNodes
//        List<AbstractNode> rNodes = new ArrayList<>();

        for (int j = 0; j < dataSet.size(); j++) {
            Item item = dataSet.get(j);
            RecordNode rNode = new RecordNode("R_" + (j + 1));
            safeDrawNode(rNode);
            //connection between class and record node
            ClassNode correspondingClassNode = getClassNode(item.name);
            rNode.setClassNode(correspondingClassNode);
            safeDrawEdge(rNode, correspondingClassNode);

            for (int k = 0; k < propertyNodes.size(); k++) {
                PropertyNode propertyNode = propertyNodes.get(k);
                //I am certain this node already is drawn

                AbstractNode valueWithProperty = new ValueNode(propertyNode.getName() + item.values[k])
                        .withValue(item.values[k]).addNode(rNode);
                //new value node to draw
                safeDrawNode(valueWithProperty);
                propertyNode.addNode(valueWithProperty);
                //connection exists, also draw corresponding edge
                safeDrawEdge(propertyNode, valueWithProperty);

                rNode.addNode(valueWithProperty);
                //also draw connection between record node and value node
                safeDrawEdge(rNode, valueWithProperty);
            }

            recordNodes.add(rNode);
//            rNodes.get(lastIndex - 1).addNode(rNode);
//            rNodes.add(rNode);
//            for (AbstractNode node : rNodes) node.sort();
        }
        return this;
    }

    /**
     * if graphDrawer is nullable, method call is ignored
     *
     * @param node
     */
    private void safeDrawNode(AbstractNode node) {
        if (graphDrawer != null) graphDrawer.drawNode(node);
    }

    /**
     * if graphDrawer is nullable, method call is ignored
     *
     * @param nodeA
     * @param nodeB
     */
    private void safeDrawEdge(AbstractNode nodeA, AbstractNode nodeB) {
        if (graphDrawer != null) graphDrawer.drawEdge(nodeA, nodeB);
    }

    public GenericAgdsEngine printNodesOfProperty(String propertyName) {
        Log.d("\n\n");

        for (AbstractNode node : getClassNode(propertyName).getNodes()) {
            Log.d("print Setosas: " + node.getName() + ", " + node.getNodes().size());
        }
        return this;
    }


    public ClassNode getClassNode(String name) {
        for (ClassNode propertyNode : classNodes) {
            if (name.equalsIgnoreCase(propertyNode.getName())) return propertyNode;
        }
        throw new IllegalStateException("Cannot find matching property: Invalid dataSet");
    }

    public String printNodes(AbstractNode node) {

        return new ListPrinter<>(new ListPrinter.DefaultStrategy<AbstractNode>() {
            @Override
            public String nextItemRow(AbstractNode abstractNode) {
                return abstractNode.getName();
            }
        }).print(node.getNodes());
    }


    public GenericAgdsEngine printMax() {


        long t0 = System.currentTimeMillis();

        double max = propertyNodes.get(0).getMaxNode().getValue();

        for (PropertyNode propertyNode : propertyNodes) {
            ValueNode maxNode = propertyNode.getMaxNode();
            if (max < maxNode.getValue()) max = maxNode.getValue();
        }
        long t1 = System.currentTimeMillis();
        Log.d("\nmaximum value of graph: " + max + ", time elapsed: " + (t1 - t0));

        return this;
    }

    public GenericAgdsEngine printMin() {

        long t0 = System.currentTimeMillis();
        //Log.d("current time = " + t0);
        double min = propertyNodes.get(0).getMinNode().getValue();

        for (PropertyNode propertyNode : propertyNodes) {
            ValueNode minNode = propertyNode.getMinNode();
            if (min > minNode.getValue()) min = minNode.getValue();
        }
        long t1 = System.currentTimeMillis();
        Log.d("\nminimum value of graph: " + min + ", time elapsed: " + (t1 - t0));
        return this;
    }

    public GenericAgdsEngine withGraphDrawer(GraphDrawer<AbstractNode> graphDrawer) {
        this.graphDrawer = graphDrawer;
        return this;
    }
}
