package ui.agds.time_tests;

import common.Utils;
import topics.agds.engine.AgdsEngine;

/**
 * @author Lukasz
 * @since 12.06.2016.
 */
public class TimerTestSuite {
    AgdsEngine engine;

    public TimerTestSuite withTaskToMeasure(NamedTask task) {
        this.task = task;
        return this;
    }

    private NamedTask task;

    public interface Task {
        void compute();

    }

    public interface NamedTask extends Task {
        String name();
    }

    private TimerTestSuite(AgdsEngine engine) {
        this.engine = engine;
    }

    public static TimerTestSuite with(AgdsEngine engine) {
        return new TimerTestSuite(engine);
    }

    /**
     * @param times how many times task should be executed
     * @return mean time of performing selected task
     */
    public TimerTestSuite measure(int times) {
        if (times == 0) throw new UnsupportedOperationException("This method is not defind for input 0");
        int fixedTime = (times < 0 ? -times : times);
        long sum = 0;
        for (int j = 0; j < fixedTime; j++) {
            long time0 = System.nanoTime();
            task.compute();
            long time1 = System.nanoTime();
            sum += (time1 - time0);
        }
        Utils.log("Function " + task.name() + "elapsed in " + (sum / fixedTime) + " ns");
        return this;
    }

}
