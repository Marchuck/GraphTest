package topics.sql;

import common.DataReader;
import topics.sql.randomizer.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz
 * @since 08.05.2016.
 */
public class StudentsDatabaseSQL {

    public static void main(String[] args) {

        DataReader.ReadStrategy<Student> readStrategy = new DataReader.ReadStrategy<Student>() {
            @Override
            public Student createNewRow(String line) {
                return new Student(line);
            }
        };
        DataReader<Student> studentsReader = new DataReader<>(readStrategy).skipFirstLine();
        List<Student> thousandOfStudents = studentsReader.read(DataReader.STUDENTS);
        SqlRoot root = new SqlRoot();
        root.addColumns(asSqlColumns(studentsReader.getFirstLine()));
        for (Student student : thousandOfStudents) {

        }
    }

    private static List<SqlColumn> asSqlColumns(String firstLine) {
        String[] properties = firstLine.split(",");
        List<SqlColumn> list = new ArrayList<>();
        for (String s : properties) list.add(new SqlColumn(s));
        return list;
    }
}
