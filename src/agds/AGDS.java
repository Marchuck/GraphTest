package agds;

import com.sun.istack.internal.Nullable;
import common.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.NumberFormat;
import java.util.*;

public class AGDS {
    public static final String TAG = AGDS.class.getSimpleName();
    public static final String CLASS_NODE_STYLESHEET = "fill-color: rgb(76,175,80);";
    public static final String PROPERTY_NODE_STYLESHEET = "fill-color: rgb(255,152,0);";
    public static final String RECORD_NODE_STYLESHEET = "fill-color: rgb(255,87,34);";
    public static final String VALUE_NODE_STYLESHEET = "fill-color: rgb(63,81,181);";

    public static final int CLASS_NODE_WEIGHT = 16;
    public static final int PROPERTY_NODE_WEIGHT = 4;
    public static final int RECORD_NODE_WEIGHT = 3;
    public static final int VALUE_NODE_WEIGHT = 2;


    @Nullable
    private GraphDrawer<DrawableNode> graphDrawer;

    public GraphDrawer getGraphDrawer() {
        return graphDrawer;
    }

    public void connectGraphDrawer(GraphDrawer<DrawableNode> graphDrawer) {
        this.graphDrawer = graphDrawer;
    }

    /**
     * todo:
     * wyszukiwanie najbliższych obiektów do podanego
     * znaleźć najbardziej podobne obiekty do grupy obiektów
     * grupowanie względem przedziałów
     */
    private static final String TAB_WHITESPACE = "\t";
    public static final String IRIS_DATA_PATH = "IrisDataTwiceSimplified.txt"; //or without -Simplified- suffix

    public List<PropertyDrawableNode> param;
    public Map<String, ClassDrawableNode> newClassValues;

    public AGDS() {
        this.param = new ArrayList<>();
        this.newClassValues = new HashMap<>();
    }

    private void readFromFile(File file) {
        NumberFormat commaDelimiterFormat = NumberFormat.getInstance(Locale.GERMAN);

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String nextObjectLine;

            int itemCounter = 0;
            while ((nextObjectLine = bufferedReader.readLine()) != null) {
                //Reading atribute values from the first line.
                if (itemCounter == 0) {
                    String[] objectRawValues = nextObjectLine.split(TAB_WHITESPACE);
                    for (int i = 0; i < objectRawValues.length - 1; i++) {
                        param.add(new PropertyDrawableNode(objectRawValues[i]));
                    }
                    itemCounter++;
                }
                //Reading saved records to value, record and class node.
                else {
                    String[] objectRawValues = nextObjectLine.split(TAB_WHITESPACE);
                    double[] objectDoubleValues = new double[objectRawValues.length - 1];
                    String className = objectRawValues[objectRawValues.length - 1];

                    //If read class doesn't exist in Map, put it there.
                    if (!newClassValues.containsKey(className)) {
                        ClassDrawableNode classValueNode = new ClassDrawableNode(className);
                        newClassValues.put(className, classValueNode);
                    }

                    //Matching record node to class node.
                    ClassDrawableNode classValueNode = newClassValues.get(className);
                    RDrawableNode rNode = new RDrawableNode("Record " + String.valueOf(itemCounter));
                    rNode.addClassNode(classValueNode);
                    classValueNode.addRecordNode(rNode);

                    List<ValueDrawableNode> valueNodeList = new ArrayList<>();

                    //Adding value nodes.
                    for (int i = 0; i < objectRawValues.length - 1; i++) {
                        objectDoubleValues[i] = commaDelimiterFormat.parse(objectRawValues[i]).doubleValue();

                        ValueDrawableNode newValueNode = new ValueDrawableNode(objectDoubleValues[i]);
                        newValueNode.addRecordNode(rNode);
                        newValueNode.setPropertyNode(param.get(i));

                        int indexValue = param.get(i).getValueNodeList().indexOf(newValueNode);
                        param.get(i).addNode(newValueNode);
                        valueNodeList.add(newValueNode);
                    }
                    rNode.setValueNodeList(valueNodeList);
                    itemCounter++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void launchAGDSStructureFromFile(File file) {
        readFromFile(file);
        prepareValueNodes();
        resetNodesWages();
    }


    public RDrawableNode findMostSimilarElement(double[] scannedValues) {
        ClassDrawableNode mostSimilarClass = null;
//        ClassDrawableNode mostSimilarClass =newClassValues.values().iterator().next();
        resetNodesWages();

        for (int j = 0; j < param.size(); j++) {
            PropertyDrawableNode propertyNode = param.get(j);

            int foundIndex = findNearestAttributeValueIndex(propertyNode,
                    new ValueDrawableNode(scannedValues[j]));
            propertyNode.calculateWages(foundIndex);
        }

        for (ClassDrawableNode newClassNode : newClassValues.values()) {
            newClassNode.sortNodes();

            if (mostSimilarClass == null) {
                mostSimilarClass = newClassNode;
            } else if (newClassNode.getrNodeList().get(0).getTotalWeight()
                    > mostSimilarClass.getrNodeList().get(0).getTotalWeight())
                mostSimilarClass = newClassNode;
        }
        return mostSimilarClass.getrNodeList().get(0);
    }

    private double[] loadNextDoubleRecord() {
        Scanner scanner = new Scanner(System.in);
        int loadTimes = param.size();
        double[] selectedValues = new double[loadTimes];

        for (int i = 0; i < loadTimes; i++) {
            System.out.println("Next value: (" + i + "):");
            selectedValues[i] = scanner.nextDouble();
        }
        return selectedValues;
    }

    private int findNearestAttributeValueIndex(PropertyDrawableNode propertyNode, ValueDrawableNode searchedValue) {
        List<ValueDrawableNode> newValueNodes = propertyNode.getValueNodeList();
        int foundIndex = Collections.binarySearch(newValueNodes, searchedValue);

        if (foundIndex < 0) {
            int fixedIndex = -foundIndex - 1;
            if (fixedIndex > 0 && fixedIndex < newValueNodes.size()) {
                double lhsValue = newValueNodes.get(fixedIndex - 1).getDoubleValue();
                double rhsValue = newValueNodes.get(fixedIndex).getDoubleValue();

                foundIndex = Math.abs(lhsValue - searchedValue.getDoubleValue()) < Math.abs(rhsValue - searchedValue.getDoubleValue()) ? fixedIndex - 1 : fixedIndex;
            } else if (fixedIndex == 0)
                foundIndex = fixedIndex;
            else
                foundIndex = fixedIndex - 1;
        }
        return foundIndex;
    }

    private void prepareValueNodes() {
        for (PropertyDrawableNode propertyNode : param) {
            propertyNode.sortValueNodes();
        }
    }

    private void resetNodesWages() {
        for (PropertyDrawableNode propertyNode : param)
            propertyNode.resetValueNodes();

        for (ClassDrawableNode newClassNode : newClassValues.values())
            newClassNode.resetRecordNodes();
    }

    public static void main(String[] args) {
        AGDS irisAgds = new AGDS();
        irisAgds.launchAGDSStructureFromFile(new File(IRIS_DATA_PATH));
        RDrawableNode mostSimilarClassNode = irisAgds.findMostSimilarElement(irisAgds.loadNextDoubleRecord());
        System.out.println("Most similar class: "
                + mostSimilarClassNode.getClassNode().getClassName()
                + "(" + mostSimilarClassNode.getTotalWeight() + ")");
    }

    public void drawData() {
        Log.d(TAG, "drawData ");
        readFromFileWithDrawing(new File(currentData));
    }

    private String currentData = IRIS_DATA_PATH;

    private void readFromFileWithDrawing(File file) {
        Log.d(TAG, "readFromFileWithDrawing ");
        NumberFormat commaDelimiterFormat = NumberFormat.getInstance(Locale.GERMAN);

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String nextObjectLine;

            int itemCounter = 0;
            while ((nextObjectLine = bufferedReader.readLine()) != null) {
                //Reading atribute values from the first line.
                if (itemCounter == 0) {
                    String[] objectRawValues = nextObjectLine.split(TAB_WHITESPACE);
                    for (int i = 0; i < objectRawValues.length - 1; i++) {
                        PropertyDrawableNode node = new PropertyDrawableNode(objectRawValues[i]);
                        param.add(node);
                        safeDrawNode(node);
                    }
                    itemCounter++;
                }
                //Reading saved records to value, record and class node.
                else {
                    String[] objectRawValues = nextObjectLine.split(TAB_WHITESPACE);
                    double[] objectDoubleValues = new double[objectRawValues.length - 1];
                    String className = objectRawValues[objectRawValues.length - 1];

                    //If read class doesn't exist in Map, put it there.
                    if (!newClassValues.containsKey(className)) {
                        ClassDrawableNode classValueNode = new ClassDrawableNode(className);
                        newClassValues.put(className, classValueNode);
                        safeDrawNode(classValueNode);
                    }

                    //Matching record node to class node.
                    ClassDrawableNode classValueNode = newClassValues.get(className);
                    RDrawableNode rNode = new RDrawableNode("Record " + String.valueOf(itemCounter));
                    rNode.addClassNode(classValueNode);
                    classValueNode.addRecordNode(rNode);

                    safeDrawNode(rNode);
                    safeDrawNode(classValueNode);
                    safeDrawEdge(classValueNode, rNode);

                    List<ValueDrawableNode> valueNodeList = new ArrayList<>();

                    //Adding value nodes.
                    for (int i = 0; i < objectRawValues.length - 1; i++) {
                        objectDoubleValues[i] = commaDelimiterFormat.parse(objectRawValues[i]).doubleValue();

                        ValueDrawableNode newValueNode = new ValueDrawableNode(objectDoubleValues[i]);
                        newValueNode.addRecordNode(rNode);
                        newValueNode.setPropertyNode(param.get(i));

                        int indexValue = param.get(i).getValueNodeList().indexOf(newValueNode);
                        param.get(i).addNode(newValueNode);

                        safeDrawNode(newValueNode);
                        safeDrawNode(rNode);
                        safeDrawNode(param.get(i));
                        safeDrawEdge(newValueNode, rNode);
                        safeDrawEdge(param.get(i), newValueNode);

                        valueNodeList.add(newValueNode);
                    }
                    rNode.setValueNodeList(valueNodeList);
                    itemCounter++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void safeDrawNode(DrawableNode drawableNode) {
        if (graphDrawer != null) graphDrawer.drawNode(drawableNode);
    }

    private void safeDrawEdge(DrawableNode drawableNodeA, DrawableNode drawableNodeB) {
        if (graphDrawer != null) graphDrawer.drawEdge(drawableNodeA, drawableNodeB);
    }

    public AGDS withData(String txtFileNameData) {
        this.currentData = txtFileNameData;
        return this;
    }
}
