package topics.som.som;

/**
 * @author Lukasz
 * @since 12.06.2016.
 */
public class SOMThread extends Thread {

    //0 - pause
    //1 - go1
    //2 - play
    //3 - ff
    int state = -1;
    int MAX_ITER = 0;
    float t;
    float T_INC;

    //Random sample retrieved from class Screen
    FloatPoint r_sample = new FloatPoint();

    //Location of the best matching unit of r_sample retrieved from
    //  Screen
    IntegerPoint bmu_loc = new IntegerPoint();


    public void set_MAX_ITER(int M) {
        MAX_ITER = M;
        T_INC = 1.0f / (float) (MAX_ITER);
    }

    public void set_state(int f) {
        state = f;
    }

    public void reset() {

        SomsApplet.screen.set_INIT_STYLE(SomsApplet.combo.getSelectedIndex());
        SomsApplet.screen.init_v_weights();

        //Get rid of this line to keep the same colors when resetting
        SomsApplet.screen.init_v_samples();

        t = 0.0f;
        T_INC = 1.0f / (float) (MAX_ITER);
        SomsApplet.jbar.setValue(0);
        SomsApplet.screen.paint(SomsApplet.screen.getGraphics());
    }


    /*
      Updates the class Screen according the to values retrieved from
        the user interface
    */
    public void run() {
        while (true) {
            if (t < 1.0f && state != 0) {

                //Get a random sample
                r_sample = SomsApplet.screen.get_random();

                //Find its best matching unit
                bmu_loc = SomsApplet.screen.get_bmu(r_sample);

                //Scale the neighbors according to t
                SomsApplet.screen.scale_neighbors(bmu_loc, r_sample, t);

                //Increase t to decrease the number of neighbros and
                //  the amount each weight can learn
                t += T_INC;
            }

            //To prevent it from displaying 101%
            if (t >= 1.0f) {
                t = 1.0f;
                state = 0;
            }

            //Stop the process if it's completed or just advanced one frame
            if (t == 1.0f || state == 1 || state == 2) {
                SomsApplet.screen.paint(SomsApplet.screen.getGraphics());
                if (state == 1)
                    state = 0;
            }

            //Update progress bar
            SomsApplet.jbar.setValue(Math.round(t * 100.0f));
            SomsApplet.jbar_label.setText("Percentage Complete = " + Math.round(t * 100.0f) + "%");
            SomsApplet.japplet.getContentPane().invalidate();

            //Allow other operations
            yield();
        } //while(true)
    }


    /*
      Performs some simple initializations
    */
    public void init() {
        if (MAX_ITER == 0)
            MAX_ITER = 100;
        if (state == -1)
            state = 0;

        reset();
    }
}

