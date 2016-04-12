package select;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 11.04.16.
 */
public class AgdsRecord implements AbstractRecord {
    public String city = "";
    public String streetName = "";
    public String name = "";
    public String surname = "";
    public String faculty = "";
    public String number = "";

    @Override
    public String toString() {
        String children = "";
        for (AgdsRecord record : next) {
            children += record.toString();
        }
        return "AgdsRecord " +
                "city='" + city + '\'' +
                ", streetName='" + streetName + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", faculty='" + faculty + '\'' +
                ", number='" + number + '\'' +
                ", next count = " + next.size() + '}';
    }

    public List<AgdsRecord> next = new ArrayList<>();


    public AgdsRecord() {
    }

    public AgdsRecord(String city,
                      String streetName,
                      String name,
                      String surname,
                      String faculty,
                      String number) {
        this.name = name;
        this.surname = surname;
        this.number = number;
        this.city = city;
        this.streetName = streetName;
        this.faculty = faculty;
    }

    @Override
    public String getColumn(int i) {
        return i == 0 ? city
                : i == 1 ? streetName
                : i == 2 ? name
                : i == 3 ? surname
                : i == 4 ? faculty : number;
    }
}
