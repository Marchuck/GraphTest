package ui.agds.time_tests;

import common.DataReader;
import common.Item;
import common.Log;
import topics.agds.engine.AgdsEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz
 * @since 03.06.2016.
 */
public class Test {

    public Test() {

        DataReader<Item> reader;
        List<Item> dataSet;
        reader = prepareIrisReader();
        dataSet = reader.read("LargeIris.txt");
        if (!DataReader.dataSetOk(dataSet)) throw new NullPointerException("Data set empty");

        List<String> propertyNames = getPropertyNames(reader.getFirstLine());
        List<String> classNames = getClassNames(dataSet);
        for (String s : propertyNames) Log.d("next property: " + s);
        for (String s : classNames) Log.d("next className: " + s);
        Log.d("without graph representation: ");
        final AgdsEngine engine = AgdsEngine.initWith(propertyNames, classNames, dataSet)
                .buildGraph();
        TimerTestSuite suite = TimerTestSuite.with(engine);
        suite.withTaskToMeasure(new TimerTestSuite.NamedTask() {

            @Override
            public String name() {
                return "getMin()";
            }

            @Override
            public void compute() {
                engine.getMin(engine.getPropertyNodes().get(0));
            }
        }).measure(10)
                .withTaskToMeasure(new TimerTestSuite.NamedTask() {
                    @Override
                    public String name() {
                        return "getNodesFromSectionWithExactProperty (TABLE)";
                    }

                    @Override
                    public void compute() {
                        engine.getNodesFromSectionWithExactProperty(0, 2.f, 5f);
                    }
                }).measure(10)
                .withTaskToMeasure(new TimerTestSuite.NamedTask() {
                    @Override
                    public String name() {
                        return "getNodesFromSectionWithExactProperty (AGDSConstants)";
                    }

                    @Override
                    public void compute() {
                        engine.getNodesFromSectionWithExactProperty(engine.getPropertyNodes().get(0), 2.f, 5f);
                    }
                }).measure(10)
                .withTaskToMeasure(new TimerTestSuite.NamedTask() {
                    @Override
                    public String name() {
                        return "getNodesWithValueWithExactProperty(AGDSConstants)";
                    }

                    @Override
                    public void compute() {
                        engine.getNodesWithValueWithExactProperty(engine.getPropertyNodes().get(0), 2.0);
                    }
                }).measure(10)
                .withTaskToMeasure(new TimerTestSuite.NamedTask() {
                    @Override
                    public String name() {
                        return "getNodesWithValueWithExactProperty(TABLE)";
                    }

                    @Override
                    public void compute() {
                        engine.getNodesWithValueWithExactProperty(0, 2.0f);
                    }
                }).measure(10);
    }

    public static void main(String[] args) {
        new Test();

    }

    private DataReader<Item> prepareWineReader() {
        DataReader.ReadStrategy<Item> readStrategy = new DataReader.ReadStrategy<Item>() {
            @Override
            public Item createNewRow(String line) {
                return DataReader.newWineItemRow(line);
            }
        };

        DataReader<Item> dataReader = new DataReader<>(readStrategy);
        return dataReader.skipFirstLine();
    }

    private DataReader<Item> prepareIrisReader() {
        DataReader.ReadStrategy<Item> readStrategy = new DataReader.ReadStrategy<Item>() {
            @Override
            public Item createNewRow(String line) {
                return DataReader.newCommaItemRow(line);
            }
        };

        DataReader<Item> dataReader = new DataReader<>(readStrategy);
        return dataReader.skipFirstLine();
    }

    private List<String> getPropertyNames(String firstLine) {
        String[] _classes = firstLine.split("\t");
        List<String> classes = new ArrayList<>();
        for (String s : _classes) classes.add(s);
        return classes;
    }

    private List<String> getClassNames(List<Item> items) {
        List<String> classNames = new ArrayList<>();
        for (Item it : items) {
            classNames.add(it.name);
        }
        return DataReader.Utils.toDistinctList(classNames);
    }
}
