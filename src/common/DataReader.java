package common;

import com.sun.istack.internal.Nullable;
import com.sun.javafx.beans.annotations.NonNull;
import com.sun.javafx.geom.FlatteningPathIterator;
import topics.sql.randomizer.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DataReader<DESTINATION> {
    public static final String TAG = DataReader.class.getSimpleName();
    public static final String IRIS_DATA_LARGE = "LargeIris.txt";
    public static final String IRIS_DATA = "IrisData.txt";
    public static final String IRIS_DATA_SIMPLIFIED = "IrisDataSimplified.txt";
    public static final String IRIS_DATA_TWICE_SIMPLIFIED = "IrisDataTwiceSimplified.txt";
    public static final String WINE_DATA = "Wine.txt";
    public static final String STUDENTS = "students.txt";
    private String firstLine;

    /**
     * if first line is skipped, its value is stored here
     *
     * @return first Line of file
     */
    public String getFirstLine() {
        Log.d(TAG, "returning first line: " + firstLine);
        return firstLine;
    }

    public static <DESTINATION> boolean dataSetOk(@Nullable List<DESTINATION> list) {
        return list != null && list.size() > 0;
    }

    public static void throwExc(String detailedMessage) {
        throw new IllegalStateException(detailedMessage);
    }

    private boolean shouldSkipFirstLine;

    public DataReader<DESTINATION> skipFirstLine() {
        this.shouldSkipFirstLine = true;
        return this;
    }

    /**
     * ReadStrategy tells DataReader how to save line as object
     *
     * @param <DESTINATION>
     */
    public interface ReadStrategy<DESTINATION> {
        DESTINATION createNewRow(String line);
    }

    @NonNull
    private ReadStrategy<DESTINATION> readStrategy;

    private DataReader() {
        throw new RuntimeException("Reader strategy should not be null. Call "
                + DataReader.class.getSimpleName()
                + "(" + ReadStrategy.class.getSimpleName() + ") instead.");
    }

    public DataReader(ReadStrategy<DESTINATION> readStrategy) {
        this.readStrategy = readStrategy;
    }

    public List<DESTINATION> read(String name) {

        List<DESTINATION> list = new ArrayList<>();

        File file = new File(name);
        Scanner input = null;

        try {
            input = new Scanner(file);
            //input.nextLine();

            while (input.hasNextLine()) {
                //read next line

                String nextLine = input.nextLine();
                Log.d(TAG, nextLine);
                //save record as four variables
                if (shouldSkipFirstLine) {
                    firstLine = nextLine;
                    shouldSkipFirstLine = false;
                    continue;
                }
                list.add(readStrategy.createNewRow(nextLine));
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error! " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (input != null) input.close();
        }
        return list;
    }

    /**
     * create new row and convert to {@link Item}
     * dotted means that floats have format 1.23
     *
     * @param nextLine
     * @return
     */
    public static Item newDottedItemRow(String nextLine) {
        String arr[] = nextLine.split("\t");

        double first =  Double.parseDouble(arr[0]);
        double sec =    Double.parseDouble(arr[1]);
        double third =  Double.parseDouble(arr[2]);
        double fourth = Double.parseDouble(arr[3]);

        double[] values = new double[]{first, sec, third, fourth};
        String name = arr[4];
        return new Item(values, name);
    }

    public static String toDotted(String s) {
        return s.replaceAll(",", ".");
    }

    public static Item newWineItemRow(String nextLine) {
        nextLine = toDotted(nextLine);
        String arr[] = nextLine.split("\t");
        double[] values = new double[arr.length - 1];
        String name = arr[0];
        for (int j = 0; j < values.length; j++) {
            values[j] = Double.parseDouble(arr[j + 1]);
        }
        return new Item(values, name);
    }

    /**
     * create new row and convert to {@link Item}
     * dotted means that floats have format 1,23
     *
     * @param nextLine
     * @return
     */
    public static Item newCommaItemRow(String nextLine) {
        nextLine = nextLine.replaceAll(",", ".");
        return newDottedItemRow(nextLine);
    }

    /**
     * create new row and convert to list of Strings
     *
     * @param nextLine
     * @return
     */
    public static List<String> newStringListRow(String nextLine) {
        String arr[] = nextLine.split("\t");
//		System.out.println("New row length "+arr.length);
        List<String> list = new ArrayList<>();
        Collections.addAll(list, arr);
        return list;
    }

    public static class Utils {
        public static <E> boolean isNullOrEmpty(Collection<E> collection) {
            return collection == null || collection.size() == 0;
        }

        public static <T> List<T> toDistinctList(Collection<T> collection) {
            Set<T> set;
            if (!(collection instanceof Set)) {
                set = new HashSet<>();
                set.addAll(collection);
            } else {
                set = (Set<T>) collection;
            }
            List<T> distinctList = new ArrayList<>();
            distinctList.addAll(set);
            return distinctList;
        }

        public static <T> List<T> wrapToList(T singleProperty) {
            List<T> list = new ArrayList<>();
            list.add(singleProperty);
            return list;
        }
    }
    public static DataReader<Item> prepareWineReader() {
        DataReader.ReadStrategy<Item> readStrategy = new DataReader.ReadStrategy<Item>() {
            @Override
            public Item createNewRow(String line) {
                return DataReader.newWineItemRow(line);
            }
        };

        DataReader<Item> dataReader = new DataReader<>(readStrategy);
        return dataReader.skipFirstLine();
    }

    public static  DataReader<Item> prepareIrisReader() {
        DataReader.ReadStrategy<Item> readStrategy = new DataReader.ReadStrategy<Item>() {
            @Override
            public Item createNewRow(String line) {
                return DataReader.newCommaItemRow(line);
            }
        };

        DataReader<Item> dataReader = new DataReader<>(readStrategy);
        return dataReader.skipFirstLine();
    }
}
