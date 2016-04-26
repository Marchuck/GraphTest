package topics.sql.randomizer;

import java.util.Random;

final public class Randomizer {

    private Random seed = new Random();

    private String randomStringFrom(String[] array) {
        return array[seed.nextInt(array.length)];
    }

    public String getName() {
        return seed.nextBoolean() ? randomMaleName() : randomFemaleName();
    }

    public String randomMaleName() {
        return randomStringFrom(Data.MALE_NAMES);
    }

    public String randomFemaleName() {
        return randomStringFrom(Data.FEMALE_NAMES);
    }

    public String getSurname() {
        return randomStringFrom(Data.SURNAMES);
    }

    public String getStreet() {
        return randomStringFrom(Data.STREET_NAMES);
    }
    public Random getSeed() {
        return seed;
    }
    public String getFaculty() {
        return randomStringFrom(Data.faculties);
    }

    public String getCity() {
        return randomStringFrom(Data.cities);
    }
}
