package topics.agds.engine;

import agds.GraphDrawer;
import com.sun.istack.internal.Nullable;
import com.sun.javafx.beans.annotations.NonNull;
import common.Item;
import common.Log;
import common.Utils;
import javafx.util.Pair;
import topics.agds.nodes.*;
import ui.agds.tabs.correlation.Correlation;
import ui.agds.tabs.correlation.CorrelationBundle;
import ui.connector.ResultCallback;

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

    public GenericAgdsEngine withGraphDrawer(@Nullable GraphDrawer<AbstractNode> graphDrawer) {
        this.graphDrawer = graphDrawer;
        return this;
    }

    public GenericAgdsEngine buildGraph() {
        long startTime = System.currentTimeMillis();
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
            ClassNode correspondingClassNode = Extensions.getClassNode(classNodes, item.name);
            rNode.setClassNode(correspondingClassNode);
            correspondingClassNode.addNode(rNode);

            safeDrawEdge(rNode, correspondingClassNode);//no-op when graphDrawer is null

            for (int k = 0; k < propertyNodes.size(); k++) {
                PropertyNode propertyNode = propertyNodes.get(k);
                //I am certain this node already is drawn

                ValueNode valueWithProperty = new ValueNode(propertyNode.getName() + item.values[k])
                        .withValue(item.values[k]).addNode(rNode);
                Pair<ValueNode, Boolean> thisNodeAndResult = Extensions.getNonRepeatableAndResult(propertyNode, valueWithProperty);

                ValueNode nonRepeatingNode = thisNodeAndResult.getKey();
                boolean alreadyExists = thisNodeAndResult.getValue();

                if (alreadyExists) {
                    rNode.addNode(nonRepeatingNode);
                    //also draw connection between record node and value node
                    safeDrawEdge(rNode, nonRepeatingNode);
                } else {
                    //new node
                    safeDrawNode(nonRepeatingNode);//no-op when graphDrawer is null
                    propertyNode.addNode(nonRepeatingNode);
                    //connection exists, also draw corresponding edge
                    safeDrawEdge(propertyNode, nonRepeatingNode);
                    rNode.addNode(nonRepeatingNode);
                    //also draw connection between record node and value node
                    safeDrawEdge(rNode, nonRepeatingNode);
                }
            }
            recordNodes.add(rNode);
        }
        long endTime = System.currentTimeMillis();
        Utils.log("Building graph in " + (endTime - startTime) + " ms");
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
    public void safeDrawEdge(AbstractNode nodeA, AbstractNode nodeB) {
        if (graphDrawer != null) graphDrawer.drawEdge(nodeA, nodeB);
    }


    public GenericAgdsEngine printMax() {
        long t0 = System.currentTimeMillis();
        double max = getMax();
        long t1 = System.currentTimeMillis();
        Log.d("\nmaximum value of graph: " + max + ", time elapsed: " + (t1 - t0));

        return this;
    }

    private List<RecordNode> getMostSimilarNodes(@NonNull Item notClassifiedItem) {
        return getMostSimilarNodes(-1, notClassifiedItem);
    }

    private List<RecordNode> getSimilarRecordNodes(List<RecordNode> nodes, double threshold) {
        double[][] d1 = new double[nodes.size()][nodes.get(0).getNodes().size()];
        for (int j = 0; j < nodes.size(); j++) {
            List<ValueNode> valueNodes = nodes.get(j).getNodes();
            double[] doubles = new double[valueNodes.size()];
            for (int k = 0; k < valueNodes.size(); k++) {
                doubles[k] = valueNodes.get(k).getValue();
            }
            d1[j] = doubles;
        }
        return getSimilarRecordNodes(d1, threshold);
    }

    private List<RecordNode> getSimilarRecordNodes(double[][] nodes, double threshold) {
        ClassNode closestClassNode = null;
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < propertyNodes.size(); j++) {
                PropertyNode propertyNode = propertyNodes.get(j);

                int foundIndex = GenericAgdsUtils.findClosestPropertyValueIndex(propertyNode,
                        new ValueNode(nodes[i][j]));

                propertyNode.calculateWeights(foundIndex);
            }
        }
        for (ClassNode newClassNode : classNodes) {

            if (closestClassNode == null) {
                closestClassNode = newClassNode;
            } else if (newClassNode.getNodes().get(0).getTotalWeight() > closestClassNode.getNodes().get(0).getTotalWeight())
                closestClassNode = newClassNode;
        }
        if (closestClassNode == null) throw new NullPointerException("Nullable class node");

        List<RecordNode> mostClosest = new ArrayList<>();
        double fixedThreshold = RecordNode.minWeight + (RecordNode.maxWeight - RecordNode.minWeight) * threshold;
        for (RecordNode n : closestClassNode.getNodes()) {
            for (int k = 0; k < n.getNodes().size(); k++)
                if (n.getTotalWeight() >= fixedThreshold) mostClosest.add(n);
        }

        cleanupChangedValues();
        return mostClosest;
    }

    private List<RecordNode> getMostSimilarNodes(int givenLimit, @NonNull Item notClassifiedItem) {
        ClassNode closestClassNode = null;
        Utils.log("getMostSimilarNodes: propertyNodes size: " + propertyNodes.size());
        for (int j = 0; j < propertyNodes.size(); j++) {
            PropertyNode propertyNode = propertyNodes.get(j);


            ValueNode valueNode = new ValueNode(notClassifiedItem.values[j]);


            int foundIndex = GenericAgdsUtils.findClosestPropertyValueIndex(propertyNode,
                    valueNode);

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

    public GenericAgdsEngine markNodesSimilarToMany(int givenLimit, @NonNull final Item... notClassifiedItems) {
        return markNodesSimilarToMany(givenLimit, new ArrayList<Item>() {
            {
                addAll(Arrays.asList(notClassifiedItems));
            }
        });
    }

    public String pollMostSignificantClass(List<RecordNode> recordNodes) {
        if (recordNodes.size() == 0) throw new NullPointerException("Polling failed, no such elements");
        if (recordNodes.size() == 1) return recordNodes.get(0).getName();
        Map<RecordNode, Integer> mostFamiliarRecords = new HashMap<>();
        for (RecordNode node : recordNodes) {
            if (!mostFamiliarRecords.containsKey(node)) {
                mostFamiliarRecords.put(node, 1);
            } else {
                mostFamiliarRecords.put(node, mostFamiliarRecords.get(node) + 1);
            }

        }
        List<Pair<RecordNode, Integer>> list = new ArrayList<>();
        for (RecordNode node : mostFamiliarRecords.keySet()) {
            list.add(new Pair<>(node, mostFamiliarRecords.get(node)));
        }

        Collections.sort(list, new Comparator<Pair<RecordNode, Integer>>() {
            @Override
            public int compare(Pair<RecordNode, Integer> o1, Pair<RecordNode, Integer> o2) {
                return Integer.compare(o1.getValue(), o2.getValue());
            }
        });

        return list.get(0).getKey().getName();
    }

    public GenericAgdsEngine markNodesSimilarToMany(int givenLimit, @NonNull List<Item> notClassifiedItems) {

        Map<RecordNode, Integer> mostFamiliarRecords = new HashMap<>();

        for (Item it : notClassifiedItems) {
            List<RecordNode> records = getMostSimilarNodes(it);
            for (RecordNode node : records) {
                if (!mostFamiliarRecords.containsKey(node)) {
                    mostFamiliarRecords.put(node, 1);
                } else {
                    mostFamiliarRecords.put(node, mostFamiliarRecords.get(node) + 1);
                }
            }
        }
        List<Pair<RecordNode, Integer>> list = new ArrayList<>();
        for (RecordNode node : mostFamiliarRecords.keySet()) {
            list.add(new Pair<>(node, mostFamiliarRecords.get(node)));
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

    public double getMin() {
        double min = getMin(propertyNodes.get(0));
        for (PropertyNode propertyNode : propertyNodes) {
            double min2 = getMin(propertyNode);
            if (min2 > min) min = min2;
        }
        return min;
    }

    public double getMin(PropertyNode propertyNode) {
        return propertyNode.getMinNode().getValue();
    }

    public double getMax(PropertyNode propertyNode) {
        return propertyNode.getMaxNode().getValue();
    }

    public double getMax() {
        double max = getMax(propertyNodes.get(0));
        for (PropertyNode propertyNode : propertyNodes) {
            double max2 = getMax(propertyNode);
            if (max2 > max) max = max2;
        }
        return max;
    }

    public GenericAgdsEngine printRecordNodes() {
        currentSimilarNodes.clear();
        for (RecordNode recordNode : recordNodes) {
            List<ValueNode> nodes = recordNode.getNodes();
            StringBuilder stringBuilder = getRecordNodesPrinter(nodes);
            currentSimilarNodes.add(new Pair<>(recordNode, stringBuilder.toString()));
            common.Utils.log("next record node: " + recordNode.getName() + " : " + stringBuilder.toString());
        }
        return this;
    }

    public GenericAgdsEngine printMin() {
        long t0 = System.currentTimeMillis();
        //Log.d("current time = " + t0);
        double min = getMin();
        long t1 = System.currentTimeMillis();
        Log.d("\nminimum value of graph: " + min + ", time elapsed: " + (t1 - t0));
        return this;
    }

    public Item randomLeaf() {
        return GenericAgdsUtils.randomLeaf(new Random(), this.getMin(), this.getMax());
    }


    public StringBuilder getRecordNodesPrinter(List<ValueNode> nodes) {
        return Utils.listPrinter(nodes, recordNodesStrategy);
    }

    public List<Pair<RecordNode, String>> currentSimilarNodes = new ArrayList<>();
    private Utils.PrintStrategy recordNodesStrategy = new Utils.PrintStrategy<AbstractNode>() {
        @Override
        public String print(AbstractNode node) {
            return node.getName().substring(2);
        }
    };

    public void calculateSimilarity(List<RecordNode> selected, ResultCallback<RecordNode> resultCallback) {
        Utils.log("calculateSimilarity");
        resultCallback.onComputed(similar(selected));
    }

    public void classifyNodesSimilarToMany(double threshold, double[][] doubles, ResultCallback<String> callback) {
        final String result = classify(threshold, doubles);
        callback.onComputed(new ArrayList<String>() {{
            add(result);
        }});
    }

    public List<RecordNode> similar(final List<RecordNode> selectedNodes) {
        common.Utils.log("similar fired");
        List<RecordNode> out = getSimilarRecordNodes(selectedNodes, 1);
        out.removeAll(selectedNodes);
        Set<RecordNode> set = new HashSet<>();
        set.addAll(out);
        out.clear();
        out.addAll(set);
        return out;
    }

    /**
     * todo: implement classifying method
     *
     * @param threshold
     * @param doubles
     * @return
     */
    public String classify(double threshold, double[][] doubles) {
        Utils.log("classify");
        List<RecordNode> nodes = getSimilarRecordNodes(doubles, threshold);
        return pollMostSignificantClass(nodes);
    }

    public void calculateCorrelation(CorrelationBundle correlationBundle, ResultCallback<Double> resultCallback) {
        if (correlationBundle.isInvalid()) return;
        int i1 = correlationBundle.firstIndex, i2 = correlationBundle.secondIndex;
        Pair<RecordNode, String> first = currentSimilarNodes.get(i1);
        Pair<RecordNode, String> second = currentSimilarNodes.get(i2);
        RecordNode firstNode = first.getKey();
        RecordNode secondNode = second.getKey();
//        Correlation.test();
        final double result = Correlation.pearsonLinearCorrelation(firstNode, secondNode);
        resultCallback.onComputed(new ArrayList<Double>() {
            {
                add(result);
            }
        });
    }
}
