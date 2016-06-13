package topics.sql;

import common.DataReader;
import common.Log;
import common.RunAlgorithm;
import common.Utils;
import scala.util.parsing.combinator.testing.Str;
import topics.agds.engine.AgdsEngine;
import topics.agds.engine.GenericAgdsEngine;
import topics.agds.nodes.GenericRecordNode;
import topics.agds.nodes.GenericValueNode;
import topics.sql.randomizer.Randomizer;
import topics.sql.randomizer.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Lukasz Marczak
 * @since 25.04.16.
 * <p/>
 * Zakres wartości z poszczególnych atrybutów
 * filtracja, emulować SELECT na grafowej strukturze
 * <p/>
 * agds szukanie podobieństw
 */
public class RelationalDatabases implements RunAlgorithm {
    public static final String TAG = RelationalDatabases.class.getSimpleName();
    public static final String FILE_NAME = "students.txt";

    public static final int _ID = 0;
    public static final int _NAME = 1;
    public static final int _SURNAME = 2;
    public static final int _HAS_SCHOLARSHIP = 3;
    public static final int _YEAR = 4;
    public static final int _FACULTY = 5;
    public static final int _CITY = 6;
    public static final int _STREET = 7;

    public static void main(String[] args) throws Exception {
        new RelationalDatabases().run();
    }

    private void printData(int howMuch) {
        Randomizer randomizer = new Randomizer();
        for (int j = 0; j < howMuch; j++) {
            Random seed = randomizer.getSeed();
            String imie = randomizer.getName();
            String nazwisko = randomizer.getSurname();
            int rokStudiow = 1 + seed.nextInt(5);
            String kierunek = randomizer.getFaculty();
            String miasto = randomizer.getCity();
            String ulica = randomizer.getStreet();
            boolean stypendium = seed.nextInt(100) > 80;
            Log.d((1 + j) + "," + imie + "," + nazwisko + "," + stypendium + "," + rokStudiow + "," + kierunek
                    + "," + miasto + "," + ulica);
        }
    }

    private Student studentFromLine(String line) {
        String[] data = line.split(",");
        int id = Integer.valueOf(data[0]);
        String name = data[1];
        String surname = data[2];
        boolean hasScholarShip = Boolean.valueOf(data[3]);
        int year = Integer.valueOf(data[4]);
        String faculty = data[5];
        String city = data[6];
        String street = data[7];
        return new Student(name, surname, faculty, city, street, hasScholarShip, id, year);
    }

    private DataReader<Student> prepareReader() {
        return new DataReader<>(new DataReader.ReadStrategy<Student>() {
            @Override
            public Student createNewRow(String line) {
                return studentFromLine(line);
            }
        }).skipFirstLine();
    }

    @Override
    public void run() throws Exception {
        Log.d(TAG, "properties user dir: " + scala.util.Properties.userDir());
        DataReader<Student> studentReader = prepareReader();
        List<Student> allStudents = studentReader.read(FILE_NAME);
        List<String> columnNames = getColumnNames(studentReader.getFirstLine());
        GenericAgdsEngine<String, Student> engine = new GenericAgdsEngine<>(columnNames, columnNames, allStudents)
                .buildGraph()
                .printMin()
                .printMax();
        GenericValueNode<String> s =
                engine.getNodesWithValueWithExactProperty(engine.getPropertyNodes().get(_NAME),
                        "Laura");
        List<Student> students = engine.getNodesFromSectionWithExactProperty(_YEAR, "2", "4");
        for (Student student : students) {
            Utils.log("student: " + student.getValues()[0] + ", " + student.getValues()[2]);
        }
        Utils.log("result: " + s.getName() + ", " + s.getValue());

        for (GenericRecordNode<String> ss : s.getNodes()) {
            Utils.log(ss.getName() + " : ");
        }

//        for (String columnName : columnNames)
//            sqlRoot.addColumns(new SqlColumn(columnName));
    }

    private List<String> getColumnNames(String firstLine) {
        List<String> arr = new ArrayList<>();
        Collections.addAll(arr, firstLine.split(","));
        return arr;
    }
}
