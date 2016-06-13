package topics.sql.randomizer;

import topics.agds.engine.Cellable;

/**
 * @author Lukasz Marczak
 * @since 26.04.16.
 */
public class Student implements Cellable<String> {
    public String name, surname, faculty, city, streetName;
    public boolean hasScholarship;
    public int id, year;
    private String[] values;

    public Student(String line) {
        values = line.split(",");
        id = Integer.parseInt(values[0]);
        name = values[1];
        surname = values[2];
        hasScholarship = Boolean.parseBoolean(values[3]);
        year = Integer.parseInt(values[4]);
        faculty = values[5];
        city = values[6];
        streetName = values[7];
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
        values = new String[]{
          String.valueOf(id),name,surname,String.valueOf(hasScholarship),
                String.valueOf(year),faculty,city,streetName
        };
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String[] getValues() {
        return values;
    }

    @Override
    public int compareTo(String o) {
        StringBuilder sb = new StringBuilder();
        for (String v : values) sb.append(v);
        return o.compareTo(sb.toString());
    }

}
