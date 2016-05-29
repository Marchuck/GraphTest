package topics.som;

import ui.connector.GenericCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz
 * @since 29.05.2016.
 */
public class CController {


    //reference to a Self Organising Map
    CSom m_pSOM;

    //the data for the training
    List<List<Double>> m_TrainingSet = new ArrayList<>();


    public CController(int cxClient, int cyClient, int CellsUp, int CellsAcross, int NumIterations) {
        //create the SOM
        m_pSOM = new CSom();

        m_pSOM.Create(cxClient, cyClient, CellsUp, CellsAcross, NumIterations);

        //create the training set
        CreateDataSet();
    }

    public void CreateDataSet() {
        CreateDataSet(null);
    }

    //this method creates a small data set of color values
    //that are used to train the network with
    public void CreateDataSet(List<List<Double>> dataSetter) {
        if (dataSetter == null) {
            List<Double> red = new ArrayList<>(),
                    green = new ArrayList<>(), blue = new ArrayList<>(),
                    yellow = new ArrayList<>(), orange = new ArrayList<>(),
                    purple = new ArrayList<>(), dk_green = new ArrayList<>(),
                    dk_blue = new ArrayList<>();

            red.add(1d);
            red.add(0d);
            red.add(0d);

            green.add(0d);
            green.add(1d);
            green.add(0d);

            dk_green.add(0d);
            dk_green.add(0.5);
            dk_green.add(0.25);

            blue.add(0d);
            blue.add(0d);
            blue.add(1d);

            dk_blue.add(0d);
            dk_blue.add(0d);
            dk_blue.add(0.5);

            yellow.add(1d);
            yellow.add(1d);
            yellow.add(0.2);

            orange.add(1d);
            orange.add(0.4);
            orange.add(0.25);

            purple.add(1d);
            purple.add(0d);
            purple.add(1d);

            m_TrainingSet.add(red);
            m_TrainingSet.add(green);
            m_TrainingSet.add(blue);
            m_TrainingSet.add(yellow);
            m_TrainingSet.add(orange);
            m_TrainingSet.add(purple);
            m_TrainingSet.add(dk_green);
            m_TrainingSet.add(dk_blue);
        } else {
            m_TrainingSet = dataSetter;
        }

    }

    //    void Render(HDC surface);
    public boolean Train(GenericCallback<String> resultCallback) {
        if (!m_pSOM.FinishedTraining()) {
            if (!m_pSOM.Epoch(m_TrainingSet, resultCallback)) {
                return false;
            }
        }

        return true;
    }

    public Trainable Train(int n) {
        return new Trainable(n);
    }

    class Trainable {
        final int n;

        public Trainable(int j) {
            n = j;
        }

        public void make(GenericCallback<String> callback) {
            for (int k = 0; k < n; k++) {
                CController.this.Train(callback);
            }
        }
    }

    boolean Finished() {
        return m_pSOM.FinishedTraining();
    }


};
