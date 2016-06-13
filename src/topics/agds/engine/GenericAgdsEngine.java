package topics.agds.engine;

import agds.GraphDrawer;
import com.sun.istack.internal.Nullable;
import com.sun.javafx.beans.annotations.NonNull;
import common.Item;
import common.Log;
import common.Utils;
import javafx.util.Pair;
import sun.net.www.content.text.Generic;
import topics.agds.nodes.*;
import topics.sql.randomizer.Data;
import ui.agds.tabs.correlation.Correlation;
import ui.agds.tabs.correlation.CorrelationBundle;
import ui.connector.ResultCallback;

import java.util.*;

/**
 * @author Lukasz Marczak
 * @since 23.04.16.
 */
public class GenericAgdsEngine<DataType extends Comparable<DataType>, POJO extends Cellable<DataType>> {
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
    private List<GenericPropertyNode<DataType>> propertyNodes = new ArrayList<>();

    public List<GenericPropertyNode<DataType>> getPropertyNodes() {
        return propertyNodes;
    }

    /**
     * DrawableNode representation of classes
     */
//    private List<GenericClassNode<DataType>> classNodes = new ArrayList<>();
    /**
     * DrawableNode representation of next row
     */
    private List<GenericRecordNode<DataType>> recordNodes = new ArrayList<>();
    /**
     * source dataSet; based on this list graph will be created
     */
    private List<POJO> dataSet;


    public GenericAgdsEngine(List<String> propertyNames, List<String> classNames, List<POJO> dataSet) {

        this.propertyNames = propertyNames;
        this.classNames = classNames;
        this.dataSet = dataSet;
    }


    public GenericAgdsEngine withGraphDrawer(@Nullable GraphDrawer<AbstractNode> graphDrawer) {
        this.graphDrawer = graphDrawer;
        return this;
    }

    public GenericAgdsEngine<DataType,POJO> buildGraph() {
        long startTime = System.currentTimeMillis();
        //create propertyNodes
        for (String s : propertyNames) {
            GenericPropertyNode<DataType> propertyNode = new GenericPropertyNode<>(s);
            propertyNodes.add(propertyNode);
            safeDrawNode(propertyNode);
        }
        //create classNodes
//        for (String s : classNames) {
//            GenericClassNode<DataType> classNode = new GenericClassNode<>(s);
//            classNodes.add(classNode);
//            safeDrawNode(classNode);
//        }

        //create list of RecordNodes
//        List<AbstractNode> rNodes = new ArrayList<>();

        for (int j = 0; j < dataSet.size(); j++) {

            POJO item = dataSet.get(j);
            GenericRecordNode<DataType> rNode = new GenericRecordNode<>("R_" + (j + 1));
            safeDrawNode(rNode);
            //connection between class and record node
//            GenericClassNode<DataType> correspondingClassNode = Extensions.getGenericClassNode(classNodes, item.getName());
//            rNode.setClassNode(correspondingClassNode);
//            correspondingClassNode.addNode(rNode);

//            safeDrawEdge(rNode, correspondingClassNode);//no-op when graphDrawer is null

            for (int k = 0; k < propertyNodes.size(); k++) {
                GenericPropertyNode<DataType> propertyNode = propertyNodes.get(k);
                //I am certain this node already is drawn

                DataType value = item.getValues()[k];
                GenericValueNode<DataType> valueWithProperty = new GenericValueNode<>(propertyNode.getName() + value);
                valueWithProperty = valueWithProperty.withValue(value).addNode(rNode);
                Pair<GenericValueNode<DataType>, Boolean> thisNodeAndResult = Extensions
                        .getGenericNonRepeatableAndResult(propertyNode, valueWithProperty);

                GenericValueNode<DataType> nonRepeatingNode = thisNodeAndResult.getKey();
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

    @Nullable
    public GenericValueNode<DataType> getNodesWithValueWithExactProperty(GenericPropertyNode<DataType>
                                                                                 propertyNode,
                                                                         DataType selectedValue
    )

    {
        Utils.log("getNodesWithValueWithExactProperty\n");
        long time0 = System.nanoTime();
        for (GenericPropertyNode<DataType> propertyNode1 : propertyNodes) {
            if (propertyNode1.getName().equals(propertyNode.getName())) {
                List<GenericValueNode<DataType>> valueNodes = propertyNode.getNodes();
//                for (ValueNode vn : valueNodes) Utils.log("next: " + vn.getValue());
                int index = Collections.binarySearch(valueNodes, new GenericValueNode<>(selectedValue));
                Utils.log("index = " + index);
                long time1 = System.nanoTime();
                Utils.log("Time elapsed: " + (time1 - time0));
                if (index > 0 && index < valueNodes.size()) return valueNodes.get(index);
                return null;
            }
        }

        return null;
    }

    public List<ValueNode> getNodesFromSectionWithExactProperty(PropertyNode propertyNode,
                                                                double minValue, double maxValue) {
        long time0 = System.nanoTime();
        long time01 = System.currentTimeMillis();
        for (GenericPropertyNode<DataType> propertyNode1 : propertyNodes) {
            if (propertyNode1.getName().equals(propertyNode.getName())) {
                final List<ValueNode> valueNodes = propertyNode.getNodes();
//                for (ValueNode vn : valueNodes) Utils.log("next: " + vn.getValue());
                final int indexStart = Extensions.findClosestIndex(valueNodes, minValue);
                final int indexEnd = Extensions.findClosestIndex(valueNodes, maxValue);
                if (indexEnd == indexStart) return new ArrayList<ValueNode>() {
                    {
                        add(valueNodes.get(indexStart));
                    }
                };
//                Utils.log("indexes = " + indexStart + ", " + indexEnd);
                long time1 = System.nanoTime();
                long time11 = System.currentTimeMillis();
                Utils.log("nanos Time elapsed: " + (time1 - time0));
                Utils.log("millis Time elapsed: " + (time11 - time01));

                return valueNodes.subList(indexStart, indexEnd);
            }
        }
        return null;
    }

    @Nullable
    public List<POJO> getNodesFromSectionWithExactProperty(int column, DataType minValue, DataType maxValue) {
        List<POJO> items = new ArrayList<>();
        long time1 = System.nanoTime();
        long time11 = System.currentTimeMillis();
        int index = -1;
        for (int j = 0; j < dataSet.size(); j++) {
            POJO i = dataSet.get(j);
            if (minValue.compareTo(i.getValues()[column]) < 0 && i.getValues()[column].compareTo(maxValue) > 0) {
                items.add(dataSet.get(j));
            }
        }
        long time2 = System.nanoTime();
        long time12 = System.currentTimeMillis();
        Utils.log("nanos Time elapsed: " + (time2 - time1));
        Utils.log("millis Time elapsed: " + (time12 - time11));
        return items;
    }

    @Nullable
    public POJO getNodesWithValueWithExactProperty(int column, DataType selectedValue) {
        long time1 = System.nanoTime();
        int index = -1;
        for (int j = 0; j < dataSet.size(); j++) {
            POJO i = dataSet.get(j);
            if (i.getValues()[column].compareTo(selectedValue) == 0) {
//                Utils.log("Value found : " + i);
                index = j;
            }
        }
        long time2 = System.nanoTime();
        Utils.log("Time elapsed: " + (time2 - time1));
        return -1 == index ? null : dataSet.get(index);
    }
//    public List<RecordNode> getNodesFromSectionWithExactProperty(PropertyNode propertyNode, double selectedValue) {
//        for (PropertyNode propertyNode1 : propertyNodes) {
//            if (propertyNode1.getName().equals(propertyNode.getName())) {
//
//                return
//            }
//        }
//    }

    public GenericAgdsEngine<DataType, POJO> printMax() {
        long t0 = System.nanoTime();
        DataType max = getMax();
        long t1 = System.nanoTime();
        long diff1 = t1 - t0;
        Log.d("\nmaximum value of graph: " + max + ", time elapsed: " + diff1);
        long t2 = System.nanoTime();
        DataType max1 = dataSet.get(0).getValues()[0];
        for (POJO item : dataSet) {
            for (DataType s : item.getValues()) {
                max1 = s.compareTo(max1) > 0 ? s : max1;
            }
        }
        long t3 = System.nanoTime();
        long diff2 = (t3 - t2);
        Log.d("\nmaximum value of graph: " + max1 + ", time elapsed: " + diff2);

        return this;
    }

//    private List<GenericRecordNode<DataType>> getMostSimilarNodes(@NonNull POJO notClassifiedItem) {
//        return getMostSimilarNodes(-1, notClassifiedItem);
//    }

    public interface ArrayCreator<X> {
        X[][] create(int x, int y);
    }

    public interface VectorCreator<X> {
        X[] create(int capacity);
    }

//    private List<GenericRecordNode<DataType>> getSimilarRecordNodes(List<GenericRecordNode<DataType>> nodes,
//                                                                    double threshold,
//                                                                    @NonNull ArrayCreator<DataType> arrayCreator,
//                                                                    @NonNull VectorCreator<DataType> vectorCreator) {
//        DataType[][] d1 = arrayCreator.create(nodes.size(), nodes.get(0).getNodes().size());
//        for (int j = 0; j < nodes.size(); j++) {
//            List<GenericValueNode<DataType>> valueNodes = nodes.get(j).getNodes();
//            DataType[] dd = vectorCreator.create(valueNodes.size());
//            for (int k = 0; k < valueNodes.size(); k++) {
//                dd[k] = valueNodes.get(k).getValue();
//            }
//            d1[j] = dd;
//        }
//        return getSimilarRecordNodes(nodes, threshold,arrayCreator,vectorCreator);
//    }

//    private List<GenericRecordNode<DataType>> getSimilarRecordNodes(DataType[][] nodes, double threshold) {
//        GenericClassNode<DataType> closestClassNode = null;
//        for (int i = 0; i < nodes.length; i++) {
//            for (int j = 0; j < propertyNodes.size(); j++) {
//                GenericPropertyNode<DataType> propertyNode = propertyNodes.get(j);
//                propertyNode.clean();
//
//                int foundIndex = GenericAgdsUtils.findClosestGenericPropertyValueIndex(propertyNode,
//                        new GenericValueNode<>(nodes[i][j]));
//
//                propertyNode.calculateWeights(foundIndex);
//            }
//        }
//        for (GenericClassNode<DataType> newClassNode : classNodes) {
//
//            if (closestClassNode == null) {
//                closestClassNode = newClassNode;
//            } else if (newClassNode.getNodes().get(0).getTotalWeight() >
//                    closestClassNode.getNodes().get(0).getTotalWeight())
//                closestClassNode = newClassNode;
//        }
//        if (closestClassNode == null) throw new NullPointerException("Nullable class node");
//
//        List<GenericRecordNode<DataType>> mostClosest = new ArrayList<>();
//        Utils.log("min weight: " + RecordNode.minWeight);
//        Utils.log("max weight: " + RecordNode.maxWeight);
//        double fixedThreshold = RecordNode.minWeight + (RecordNode.maxWeight - RecordNode.minWeight)
//                * threshold;
//        Utils.log("fixed threshold: " + fixedThreshold);
//
//        for (GenericRecordNode<DataType> n : closestClassNode.getNodes()) {
//            for (int k = 0; k < n.getNodes().size(); k++)
//                if (n.getTotalWeight() >= fixedThreshold) mostClosest.add(n);
//        }
//
//        cleanupChangedValues();
//        Utils.log("returning: " + mostClosest.size() + " elements");
//        return mostClosest;
//    }


//    private List<GenericRecordNode<DataType>> getMostSimilarNodes(int givenLimit, @NonNull POJO notClassifiedItem) {
//        GenericClassNode<DataType> closestClassNode = null;
//
//        for (int j = 0; j < propertyNodes.size(); j++) {
//            GenericPropertyNode<DataType> propertyNode = propertyNodes.get(j);
//
//            int foundIndex = GenericAgdsUtils.findClosestGenericPropertyValueIndex(propertyNode,
//                    new GenericValueNode<DataType>(notClassifiedItem.getValues()[j]));
//
//            propertyNode.calculateWeights(foundIndex);
//        }
//
//        for (GenericClassNode<DataType> newClassNode : classNodes) {
//            newClassNode.sort();
//
//            if (closestClassNode == null) {
//                closestClassNode = newClassNode;
//            } else if (newClassNode.getNodes().get(0).getTotalWeight() > closestClassNode.getNodes().get(0).getTotalWeight())
//                closestClassNode = newClassNode;
//        }
//        if (closestClassNode == null) throw new NullPointerException("Nullable class node");
//
//        List<GenericRecordNode<DataType>> allNodes = closestClassNode.getNodes();
//        int limit = allNodes.size() < givenLimit ? allNodes.size() : givenLimit;
//        if (givenLimit == -1) limit = allNodes.size();
//        List<GenericRecordNode<DataType>> mostClosest = closestClassNode.getNodes().subList(0, limit);
//        cleanupChangedValues();
//        return mostClosest;
//    }

//    public GenericAgdsEngine markNodesSimilarTo(int givenLimit, @NonNull POJO notClassifiedItem) {
//
//        for (GenericRecordNode<DataType> node : getMostSimilarNodes(givenLimit, notClassifiedItem)) {
//            Log.d("Most similar node: " + node.getName() + ", class Node: " + node.getClassNode().getName());
//            StringBuilder sb = new StringBuilder();
//            for (AbstractNode n : node.getNodes()) {
//                sb.append(" , ").append(n.getName());
//            }
//            Log.d("Value nodes: " + sb);
//        }
//        return this;
//    }
//
//    public GenericAgdsEngine<DataType, POJO> markNodesSimilarToMany(int givenLimit, @NonNull final POJO... notClassifiedItems) {
//        return markNodesSimilarToMany(givenLimit, new ArrayList<POJO>() {
//            {
//                addAll(Arrays.asList(notClassifiedItems));
//            }
//        });
//    }

    public String pollMostSignificantClass(List<GenericRecordNode<DataType>> recordNodes) {
        if (recordNodes.size() == 0) throw new NullPointerException("Polling failed, no such elements");
        if (recordNodes.size() == 1) return recordNodes.get(0).getName();
        Map<GenericRecordNode<DataType>, Integer> mostFamiliarRecords = new HashMap<>();
        for (GenericRecordNode<DataType> node : recordNodes) {
            if (!mostFamiliarRecords.containsKey(node)) {
                mostFamiliarRecords.put(node, 1);
            } else {
                mostFamiliarRecords.put(node, mostFamiliarRecords.get(node) + 1);
            }

        }
        List<Pair<GenericRecordNode<DataType>, Integer>> list = new ArrayList<>();
        for (GenericRecordNode<DataType> node : mostFamiliarRecords.keySet()) {
            list.add(new Pair<>(node, mostFamiliarRecords.get(node)));
        }

        Collections.sort(list, new Comparator<Pair<GenericRecordNode<DataType>, Integer>>() {
            @Override
            public int compare(Pair<GenericRecordNode<DataType>, Integer> o1, Pair<GenericRecordNode<DataType>, Integer> o2) {
                return Integer.compare(o1.getValue(), o2.getValue());
            }
        });

        return list.get(0).getKey().getName();
    }

//    public GenericAgdsEngine<DataType, POJO> markNodesSimilarToMany(int givenLimit,
//                                                                    @NonNull List<POJO> notClassifiedItems) {
//
//        Map<GenericRecordNode<DataType>, Integer> mostFamiliarRecords = new HashMap<>();
//
//        for (POJO it : notClassifiedItems) {
//            List<GenericRecordNode<DataType>> records = getMostSimilarNodes(it);
//            for (GenericRecordNode<DataType> node : records) {
//                if (!mostFamiliarRecords.containsKey(node)) {
//                    mostFamiliarRecords.put(node, 1);
//                } else {
//                    mostFamiliarRecords.put(node, mostFamiliarRecords.get(node) + 1);
//                }
//            }
//        }
//        List<Pair<GenericRecordNode<DataType>, Integer>> list = new ArrayList<>();
//        for (GenericRecordNode<DataType> node : mostFamiliarRecords.keySet()) {
//            list.add(new Pair<>(node, mostFamiliarRecords.get(node)));
//        }
//
//        Collections.sort(list, new Comparator<Pair<GenericRecordNode<DataType>, Integer>>() {
//            @Override
//            public int compare(Pair<GenericRecordNode<DataType>, Integer> o1, Pair<GenericRecordNode<DataType>, Integer> o2) {
//                return Integer.compare(o1.getValue(), o2.getValue());
//            }
//        });
//
//        for (Pair<GenericRecordNode<DataType>, Integer> node : GenericAgdsUtils.subList(list, givenLimit))
//            Log.d("{ " + node.getKey().getName() + " : " + node.getValue() + " }");
//
//        return this;
//    }


//    private void cleanupChangedValues() {
//        for (GenericPropertyNode<DataType> propertyNode : propertyNodes)
//            propertyNode.clean();
//
//        for (GenericClassNode<DataType> newClassNode : classNodes)
//            newClassNode.clean();
//    }

    public DataType getMin() {
        DataType min = getMin(propertyNodes.get(0));
        for (GenericPropertyNode<DataType> propertyNode : propertyNodes) {
            DataType min2 = getMin(propertyNode);
            if (min2.compareTo(min) < 0) min = min2;
        }
        return min;
    }

    public DataType getMin(GenericPropertyNode<DataType> propertyNode) {
        return propertyNode.getMinNode().getValue();
    }

    public DataType getMax(GenericPropertyNode<DataType> propertyNode) {
        return propertyNode.getMaxNode().getValue();
    }

    public DataType getMax() {
        DataType max = getMax(propertyNodes.get(0));
        for (GenericPropertyNode<DataType> propertyNode : propertyNodes) {
            DataType max2 = getMax(propertyNode);
            if (max2.compareTo(max) > 0) max = max2;
        }
        return max;
    }

    public GenericAgdsEngine<DataType, POJO> printRecordNodes() {
        currentSimilarNodes.clear();
        for (GenericRecordNode<DataType> recordNode : recordNodes) {
            List<GenericValueNode<DataType>> nodes = recordNode.getNodes();
            StringBuilder stringBuilder = getRecordNodesPrinter(nodes);
            currentSimilarNodes.add(new Pair<>(recordNode, stringBuilder.toString()));
            Utils.log("next record node: " + recordNode.getName() + " : " + stringBuilder.toString());
        }
        return this;
    }

    public GenericAgdsEngine<DataType, POJO> printMin() {
        long t0 = System.nanoTime();
        long t01 = System.currentTimeMillis();
        //Log.d("current time = " + t0);
        DataType min = getMin();
        long t1 = System.nanoTime();
        long t11 = System.currentTimeMillis();
        Log.d("\nminimum value of graph: " + min + ", time elapsed: " + (t1 - t0));
        Log.d("\nminimum value of graph: " + min + ", time elapsed: " + (t11 - t01));
        long t2 = System.nanoTime();
        long t21 = System.currentTimeMillis();
        DataType min1 = dataSet.get(0).getValues()[0];
        for (POJO item : dataSet) {
            for (DataType s : item.getValues()) {
                min1 = s.compareTo(min1) < 0 ? s : min1;
            }
        }
        long t3 = System.nanoTime();
        long t31 = System.currentTimeMillis();
        Log.d("\nminimum value of graph: " + min1 + ", time elapsed: " + (t3 - t2));
        Log.d("\nminimum value of graph: " + min1 + ", time elapsed: " + (t31 - t21));

        return this;
    }

//    public Item randomLeaf() {
//        return GenericAgdsUtils.randomLeaf(new Random(), this.getMin(), this.getMax());
//    }


    public StringBuilder getRecordNodesPrinter(List<GenericValueNode<DataType>> nodes) {
        return Utils.listPrinter(nodes, recordNodesStrategy);
    }

    public List<Pair<GenericRecordNode<DataType>, String>> currentSimilarNodes = new ArrayList<>();
    private Utils.PrintStrategy recordNodesStrategy = new Utils.PrintStrategy<AbstractNode>() {
        @Override
        public String print(AbstractNode node) {
            return node.getName().substring(2);
        }
    };

//    public void calculateSimilarity(double threshold, List<GenericRecordNode<DataType>> selected,
//                                    ArrayCreator<DataType> arrayCreator,
//                                    ResultCallback<GenericRecordNode<DataType>> resultCallback) {
//        Utils.log("calculateSimilarity");
//        resultCallback.onComputed(similar(threshold, selected, arrayCreator));
//    }

//    public void classifyNodesSimilarToMany(double threshold, DataType[][] doubles, ResultCallback<String> callback) {
//        final String result = classify(threshold, doubles);
//        callback.onComputed(new ArrayList<String>() {{
//            add(result);
//        }});
//    }

//    public List<GenericRecordNode<DataType>> similar(double threshold, final List<GenericRecordNode<DataType>> selectedNodes,
//                                                     ArrayCreator<DataType> arrayCreator) {
//        Utils.log("similar fired");
//        List<GenericRecordNode<DataType>> out = getSimilarRecordNodes(arrayCreator.create(0, 0), threshold);
//        out.removeAll(selectedNodes);
//        return out;
//    }


//    public String classify(double threshold, DataType[][] doubles) {
//        Utils.log("classify");
//        List<GenericRecordNode<DataType>> nodes = getSimilarRecordNodes(doubles, threshold);
//        return pollMostSignificantClass(nodes);
//    }

    public void calculateCorrelation(CorrelationBundle correlationBundle, ResultCallback<Double> resultCallback) {
        if (correlationBundle.isInvalid()) return;
        int i1 = correlationBundle.firstIndex, i2 = correlationBundle.secondIndex;
        Pair<GenericRecordNode<DataType>, String> first = currentSimilarNodes.get(i1);
        Pair<GenericRecordNode<DataType>, String> second = currentSimilarNodes.get(i2);
        GenericRecordNode<DataType> firstNode = first.getKey();
        GenericRecordNode<DataType> secondNode = second.getKey();
//        Correlation.test();
//        final double result = Correlation.pearsonLinearCorrelation(firstNode, secondNode);
        throw new UnsupportedOperationException("Not yet implemented");
//        resultCallback.onComputed(new ArrayList<Double>() {
//            {
//                add(result);
//            }
//        });
    }

    public void getMinFromTable() {

    }
}
