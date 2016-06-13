package ui.agds.time_tests;

import common.DataReader;
import common.Item;
import common.Log;
import common.Utils;
import topics.agds.engine.AgdsEngine;
import topics.agds.nodes.PropertyNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz
 * @since 03.06.2016.
 */
public class Test {

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

    public Test() {

        DataReader<Item> reader;
        List<Item> dataSet;
        reader = prepareIrisReader();
        dataSet = reader.read("LargeIris.txt");
//        dataSet = reader.read(DataReader.IRIS_DATA);

        if (!DataReader.dataSetOk(dataSet)) throw new NullPointerException("Data set empty");

        List<String> propertyNames = getPropertyNames(reader.getFirstLine());
        List<String> classNames = getClassNames(dataSet);
//        Log.d("properties size = " + propertyNames.size());
//        Log.d("class size = " + classNames.size());
        for (String s : propertyNames) Log.d("next property: " + s);
        for (String s : classNames) Log.d("next className: " + s);
//        long timeElapsed = System.currentTimeMillis();
        Log.d("without graph representation: ");
//        double max = dataSet.get(0).values[0];
//        for (Item it : dataSet) {
//            for (double f : it.values)
//                if (max < f) max = f;
//        }
//        Log.d("max = " + max + ", time elapsed: " + (System.currentTimeMillis() - timeElapsed));

//        long minTimeElapsed = System.currentTimeMillis();
//        Log.d("without graph representation: ");
//        double min = dataSet.get(0).values[0];
//        for (Item it : dataSet) {
//            for (double f : it.values)
//                if (min > f) min = f;
//        }
//        Log.d("min = " + min + ", time elapsed: " + (System.currentTimeMillis() - minTimeElapsed));


        final AgdsEngine engine = AgdsEngine.initWith(propertyNames, classNames, dataSet)
                .buildGraph();
//                .printMin()
//                .printMax()
//                .printRecordNodes()
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
                        return "getNodesFromSectionWithExactProperty (AGDS)";
                    }

                    @Override
                    public void compute() {
                        engine.getNodesFromSectionWithExactProperty(engine.getPropertyNodes().get(0), 2.f, 5f);
                    }
                }).measure(10)
                .withTaskToMeasure(new TimerTestSuite.NamedTask() {
                    @Override
                    public String name() {
                        return "getNodesWithValueWithExactProperty(AGDS)";
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

//        List<Long> timeAGDS = new ArrayList<>();
//        List<Long> timeTable = new ArrayList<>();
//        for (int x = 0; x < 10; x++) {
//            long time_1 = System.nanoTime();
//            engine.getMax(engine.getPropertyNodes().get(0));
//            long time_2 = System.nanoTime();
//            long agdsTime = time_2 - time_1;
//            timeAGDS.add(agdsTime);
//            Utils.log("Time elapsed: " + agdsTime);
//
//            double min = dataSet.get(0).values[0];
//            long time1 = System.nanoTime();
//            for (Item d : dataSet) for (double f : d.values) if (f > min) min = f;
//            long time2 = System.nanoTime();
//            long timetableTime = (time2 - time1);
//            timeTable.add(timetableTime);
//            Utils.log("Time elapsed: " + timetableTime);
//        }
//        long sumAgds = 0;
//        long sumTT = 0;
//        for (long l : timeAGDS) sumAgds += l;
//        for (long l : timeTable) sumTT += l;
//        Utils.log("Mean agds time " + sumAgds / timeAGDS.size());
//        Utils.log("Mean table time " + sumTT / timeTable.size());

        for (PropertyNode propertyNode : engine.getPropertyNodes()) {
//            ValueNode node = engine.getNodesWithValueWithExactProperty(propertyNode, 2.0);
//            if (node != null) {
//                Utils.log("node: " + node.getName() + ", " + node.getValue());
//            }
//            engine.getNodesFromSectionWithExactProperty(propertyNode, 1, 2);
//            engine.getNodesWithValueWithExactProperty(propertyNode, 2.0);

        }
        Utils.log("\n_________________________________\n");

        for (int x = 0; x < 4; x++) {
//            Item it = engine.getNodesWithValueWithExactProperty(x, 2.0);
//            if (it != null) {
//                Utils.log("item: " + it);
//            }
//            engine.getNodesFromSectionWithExactProperty(x, 1, 2);
//            engine.getNodesWithValueWithExactProperty(x, 2.0);

//            engine.getMin(engine.getPropertyNodes().get(0));
        }
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

    public static void main(String[] args) {
        new Test();

    }
}
