package topics.sql.randomizer;

/**
 * @author Lukasz Marczak
 * @since 26.04.16.
 */
public class Student {
    public String name, surname, faculty, city, streetName;
    public boolean hasScholarship;
    public int id, year;

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
