package common;

import com.sun.javafx.beans.annotations.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataReader<DESTINATION> {
    public static final String IRIS_DATA = "IrisData.txt";
    public static final String WINE = "Wine.txt";
    public static final String STUDENTS = "BazaStudenciAcces2002.mdb";

    public boolean dataSetOk(List<DESTINATION> list) {
        return list.size() > 0;
    }

    public static void throwX(String detailedMessage) {
        throw new IllegalStateException(detailedMessage);
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
     * creates
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
}
