package topics.sql;

import common.Log;
import common.RunAlgorithm;
import topics.sql.randomizer.Randomizer;

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
public class AANG implements RunAlgorithm {
    public static final String TAG = AANG.class.getSimpleName();

    public static void main(String[] args) throws Exception {
        new AANG().run();
    }

    /**
     * todo: get mdb access and create graph structure
     */
    @Override
    public void run() throws Exception {
        Log.d(TAG, "properties user dir: " + scala.util.Properties.userDir());
        Randomizer randomizer = new Randomizer();
        for (int j = 0; j < 1000; j++) {
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
}
