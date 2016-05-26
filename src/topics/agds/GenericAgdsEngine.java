package topics.agds;

import agds.GraphDrawer;
import com.sun.istack.internal.Nullable;
import com.sun.javafx.beans.annotations.NonNull;
import common.Item;
import common.Log;
import javafx.util.Pair;
import topics.agds.nodes.*;

import java.util.*;

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
            correspondingClassNode.addNode(rNode);
            safeDrawEdge(rNode, correspondingClassNode);

            for (int k = 0; k < propertyNodes.size(); k++) {
                PropertyNode propertyNode = propertyNodes.get(k);
                //I am certain this node already is drawn

                ValueNode valueWithProperty = new ValueNode(propertyNode.getName() + item.values[k])
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

//        for (AbstractNode node : getClassNode(propertyName).getNodes()) {
//            Log.d("print Setosas: " + node.getName() + ", " + );
//        }
        return this;
    }


    public ClassNode getClassNode(String name) {
        for (ClassNode propertyNode : classNodes) {
            if (name.equalsIgnoreCase(propertyNode.getName())) return propertyNode;
        }
        throw new IllegalStateException("Cannot find matching property: Invalid dataSet");
    }


    public GenericAgdsEngine printMax() {
        long t0 = System.currentTimeMillis();
        double max = getMax();
        long t1 = System.currentTimeMillis();
        Log.d("\nmaximum value of graph: " + max + ", time elapsed: " + (t1 - t0));

        return this;
    }

    private GenericAgdsEngine foo(float offset) {
        if (outOfRange(offset)) throw new IllegalStateException("Given offset is invalid");
        return this;
    }

    private List<RecordNode> getMostSimilarNodes(int givenLimit, @NonNull Item notClassifiedItem) {
        ClassNode closestClassNode = null;

        for (int j = 0; j < propertyNodes.size(); j++) {
            PropertyNode propertyNode = propertyNodes.get(j);

            int foundIndex = GenericAgdsUtils.findClosestPropertyValueIndex(propertyNode, new ValueNode(notClassifiedItem.values[j]));

            propertyNode.calculateWeights(foundIndex);
        }

        for (ClassNode newClassNode : classNodes) {
            newClassNode.sort();

            if (closestClassNode == null) {
                closestClassNode = newClassNode;
            } else if (newClassNode.getNodes().get(0).getTotalWeight() > closestClassNode.getNodes().get(0).getTotalWeight())
                closestClassNode = newClassNode;
        }
        if (closestClassNode == null) throw new NullPointerException("Nullable class node");

        List<RecordNode> allNodes = closestClassNode.getNodes();
        int limit = allNodes.size() < givenLimit ? allNodes.size() : givenLimit;
        if (givenLimit == -1) limit = allNodes.size();
        List<RecordNode> mostClosest = closestClassNode.getNodes().subList(0, limit);
        cleanupChangedValues();
        return mostClosest;
    }

    public GenericAgdsEngine markNodesSimilarTo(int givenLimit, @NonNull Item notClassifiedItem) {

        for (RecordNode node : getMostSimilarNodes(givenLimit, notClassifiedItem)) {
            Log.d("Most similar node: " + node.getName() + ", class Node: " + node.getClassNode().getName());
            StringBuilder sb = new StringBuilder();
            for (AbstractNode n : node.getNodes()) {
                sb.append(" , ").append(n.getName());
            }
            Log.d("Value nodes: " + sb);
        }


        return this;
    }

    public GenericAgdsEngine markNodesSimilarToMany(int givenLimit, @NonNull Item... notClassifiedItems) {
        Map<RecordNode, Integer> mostFimiliarRecords = new HashMap<>();

        for (Item it : notClassifiedItems) {
            List<RecordNode> records = getMostSimilarNodes(-1, it);
            for (RecordNode node : records) {
                if (!mostFimiliarRecords.containsKey(node)) {
                    mostFimiliarRecords.put(node, 1);
                } else {
                    mostFimiliarRecords.put(node, mostFimiliarRecords.get(node) + 1);
                }
            }
        }
        List<Pair<RecordNode, Integer>> list = new ArrayList<>();
        for (RecordNode node : mostFimiliarRecords.keySet()) {
            list.add(new Pair<>(node, mostFimiliarRecords.get(node)));
        }
        Collections.sort(list, new Comparator<Pair<RecordNode, Integer>>() {
            @Override
            public int compare(Pair<RecordNode, Integer> o1, Pair<RecordNode, Integer> o2) {
                return Integer.compare(o1.getValue(), o2.getValue());
            }
        });

        for (Pair<RecordNode, Integer> node : GenericAgdsUtils.subList(list, givenLimit))
            Log.d("{ " + node.getKey().getName() + " : " + node.getValue() + " }");

        return this;
    }


    private void cleanupChangedValues() {
        for (PropertyNode propertyNode : propertyNodes)
            propertyNode.clean();

        for (ClassNode newClassNode : classNodes)
            newClassNode.clean();
    }


    private boolean outOfRange(float offset) {
        return offset < 0f || offset > 1f;
    }

    public double getMin() {
        double min = propertyNodes.get(0).getMinNode().getValue();
        for (PropertyNode propertyNode : propertyNodes) {
            ValueNode minNode = propertyNode.getMinNode();
            if (min > minNode.getValue()) min = minNode.getValue();
        }
        return min;
    }

    public double getMax() {
        double max = propertyNodes.get(0).getMaxNode().getValue();

        for (PropertyNode propertyNode : propertyNodes) {
            ValueNode maxNode = propertyNode.getMaxNode();
            if (max < maxNode.getValue()) max = maxNode.getValue();
        }
        return max;
    }

    public GenericAgdsEngine printMin() {

        long t0 = System.currentTimeMillis();
        //Log.d("current time = " + t0);
        double min = getMin();
        long t1 = System.currentTimeMillis();
        Log.d("\nminimum value of graph: " + min + ", time elapsed: " + (t1 - t0));
        return this;
    }

    public GenericAgdsEngine withGraphDrawer(@Nullable GraphDrawer<AbstractNode> graphDrawer) {
        this.graphDrawer = graphDrawer;
        return this;
    }

    public Item randomLeaf() {
        return GenericAgdsUtils.randomLeaf(new Random(), this.getMin(), this.getMax());
    }

    public interface ResultCallback {
        void onComputed();
    }

    public void classifyNodesSimilarToMany(double threshold, double[][] doubles, ResultCallback callback) {
        System.out.println("Successfully connected");
        callback.onComputed();
    }
}
