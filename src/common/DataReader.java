package common;

import com.sun.javafx.beans.annotations.NonNull;
import topics.data_mining.transaction.Transaction;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DataReader<DESTINATION> {
    public static final String IRIS_DATA = "IrisData.txt";
    public static final String WINE = "Wine.txt";
    public static final String STUDENTS = "BazaStudenciAcces2002.mdb";

    public boolean dataSetOk(List<DESTINATION> list) {
        return list.size() > 0;
    }

    public static void throwExc(String detailedMessage) {
        throw new IllegalStateException(detailedMessage);
    }

    private boolean shouldSkipFirstLine;

    public void skipFirstLine() {
        this.shouldSkipFirstLine = true;
    }

    /**
     * ReadStrategy tells DataReader how to
     *
     * @param <DESTINATION>
     */
    public interface ReadStrategy<DESTINATION> {
        DESTINATION createNewRow(String line);
    }

    @NonNull
    ReadStrategy<DESTINATION> readStrategy;

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
            input.nextLine();

            while (input.hasNextLine()) {
                //read next line

                String nextLine = input.nextLine();
                //save record as four variables
                if (shouldSkipFirstLine) {
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
     *
     * @param nextLine
     * @return
     */
    public static Item newItemRow(String nextLine) {
        String arr[] = nextLine.split("\t");

        Float first = Float.parseFloat(arr[0]);
        Float sec = Float.parseFloat(arr[1]);
        Float third = Float.parseFloat(arr[2]);
        Float fourth = Float.parseFloat(arr[3]);

        float[] values = new float[]{first, sec, third, fourth};
        String name = arr[4];
        return new Item(values, name);
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

        public static <T> List<T> toDistinctList(List<T> list) {
            Set<T> set = new HashSet<>();
            set.addAll(list);
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

}
