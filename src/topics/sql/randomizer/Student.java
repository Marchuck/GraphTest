package topics.sql.randomizer;

/**
 * @author Lukasz Marczak
 * @since 26.04.16.
 */
public class Student {
    public String name, surname, faculty, city, streetName;
    public boolean hasScholarship;
    public int id, year;

    public Student(String line) {
        String[] l = line.split(",");
        id = Integer.parseInt(l[0]);
        name = l[1];
        surname = l[2];
        hasScholarship = Boolean.parseBoolean(l[3]);
        year = Integer.parseInt(l[4]);
        faculty = l[5];
        faculty = l[6];
        city = l[7];
        streetName = l[8];
    }

    public Student(String name, String surname, String faculty, String city,
                   String streetName, boolean hasScholarship, int id, int year) {
        this.name = name;
        this.surname = surname;
        this.faculty = faculty;
        this.city = city;
        this.streetName = streetName;
        this.hasScholarship = hasScholarship;
        this.id = id;
        this.year = year;
    }
}
