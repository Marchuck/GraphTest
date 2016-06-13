package topics.agds.engine;

import common.Utils;
import javafx.util.Pair;
import topics.agds.nodes.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Lukasz
 * @since 28.05.2016.
 */
public class Extensions {
    public static Pair<ValueNode, Boolean> getNonRepeatableAndResult(PropertyNode propertyNode, ValueNode valueWithProperty) {
        for (ValueNode valueNode : propertyNode.getNodes()) {
            if (Double.compare(valueNode.getValue(), valueWithProperty.getValue()) == 0)
                return new Pair<>(valueNode, true);
        }
        return new Pair<>(valueWithProperty, false);
    }

    public static <T extends Comparable<T>> Pair<GenericValueNode<T>, Boolean>
    getGenericNonRepeatableAndResult(GenericPropertyNode<T> propertyNode, GenericValueNode<T> valueWithProperty) {
        for (GenericValueNode<T> valueNode : propertyNode.getNodes()) {
            if (valueNode.compareTo(valueWithProperty) == 0) return new Pair<>(valueNode, true);
        }
        return new Pair<>(valueWithProperty, false);
    }

    public static void main(String[] args) {
        List<ValueNode> doubles = new ArrayList<>();
        List<Double> doubles1 = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            doubles.add(new ValueNode(((double) i)));
            doubles1.add((double) i);
        }
        Utils.log("INdex: " +
                findClosestIndex(doubles, 8));
    }

    public static int findClosestIndex(List<ValueNode> valueNodes, double maxValue) {
        int currentIndex = -1, prevIndex;
        int lo = 0, hi = valueNodes.size() - 1;

        while (lo <= hi) {
            prevIndex = currentIndex;
            currentIndex = (lo + hi) / 2;
            if (prevIndex == currentIndex) return currentIndex;
            if (currentIndex >= valueNodes.size()) return (valueNodes.size() - 1);
            if (currentIndex <= 0) return 0;

            if (valueNodes.get(currentIndex).getValue() > maxValue) {
                hi = currentIndex;
            } else {
                lo = currentIndex;
            }
//            Utils.log("current index: " + currentIndex);

        }
        return 0;
    }

    public interface Function {
        void perform(RecordNode node);
    }

    /**
     * @param bannedNodes list of nodes which shouldn't be considered in operation
     * @param task
     */
    public static void iterateWithoutTheseRNodes(List<RecordNode> recordNodes, List<RecordNode> bannedNodes, Function task) {

        for (RecordNode recordNode : bannedNodes) {
            recordNode.banned = true;
        }
        for (RecordNode recordNode : recordNodes) {
            if (!recordNode.banned) {
                task.perform(recordNode);
            } else {
                recordNode.banned = false;
            }
        }
    }

    public static ClassNode getClassNode(List<ClassNode> classNodes, String name) {
        for (ClassNode propertyNode : classNodes) {
            if (name.equalsIgnoreCase(propertyNode.getName())) return propertyNode;
        }
        throw new IllegalStateException("Cannot find matching property: Invalid dataSet");
    }

    public static <T extends Comparable<T>> GenericClassNode<T>
    getGenericClassNode(List<GenericClassNode<T>> classNodes, String name) {
        for (GenericClassNode<T> propertyNode : classNodes) {
            if (name.equalsIgnoreCase(propertyNode.getName())) return propertyNode;
        }
        StringBuilder sb = new StringBuilder();
        for (GenericClassNode<T> e : classNodes) sb.append(e.getName()).append(",");
        throw new IllegalStateException("Cannot find property" + name + ": Invalid dataSet: " + sb.toString());
    }


}
