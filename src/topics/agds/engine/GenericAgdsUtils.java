package topics.agds.engine;

import com.sun.javafx.beans.annotations.NonNull;
import common.Item;
import topics.agds.nodes.PropertyNode;
import topics.agds.nodes.ValueNode;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Lukasz
 * @since 13.05.2016.
 * Calculate methods
 */
public class GenericAgdsUtils {
    /**
     * @param d1
     * @param d2
     * @return Abs value of given difference
     */
    private static double diffAbs(double d1, double d2) {
        double d3 = d1 - d2;
        return d3 < 0 ? -d3 : d3;
    }

    private static int calculatedIndex(double lhsValue, double rhsValue, double tokenValue, int fixedIndex) {
        return diffAbs(lhsValue, tokenValue) < diffAbs(rhsValue, tokenValue) ?
                fixedIndex - 1 : fixedIndex;
    }

    public static int findClosestPropertyValueIndex(PropertyNode propertyNode, ValueNode searchedValue) {
        List<ValueNode> newValueNodes = propertyNode.getNodes();
        int foundIndex = Collections.binarySearch(newValueNodes, searchedValue);

        //if this element does not exists in set
        if (foundIndex < 0) {
            int fixedIndex = -foundIndex - 1;
            if (fixedIndex > 0 && fixedIndex < newValueNodes.size()) {
                double lhsValue = newValueNodes.get(fixedIndex - 1).getValue();
                double rhsValue = newValueNodes.get(fixedIndex).getValue();

                foundIndex = calculatedIndex(lhsValue, rhsValue, searchedValue.getValue(), fixedIndex);

            } else if (fixedIndex == 0)
                foundIndex = fixedIndex;
            else
                foundIndex = fixedIndex - 1;
        }
        return foundIndex;
    }

    public static <T> List<T> subList(@NonNull List<T> list, int limit) {
        int index = limit < list.size() ? limit : list.size();
        return list.subList(0, index);
    }

    public static Item randomLeaf(Random r,double min, double max) {
        return new Item(new double[]{
                produceValueFrom(r, min, max),
                produceValueFrom(r, min, max),
                produceValueFrom(r, min, max),
                produceValueFrom(r, min, max),
        });
    }

    public static double produceValueFrom(Random r, double min, double max) {
        return min + (max - min) * r.nextDouble();
    }
}
