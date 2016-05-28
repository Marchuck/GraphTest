package topics.agds.engine;

import javafx.util.Pair;
import topics.agds.nodes.ClassNode;
import topics.agds.nodes.PropertyNode;
import topics.agds.nodes.RecordNode;
import topics.agds.nodes.ValueNode;

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

}
