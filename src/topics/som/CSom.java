package topics.som;

import common.Utils;
import ui.connector.GenericCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.*;
import static topics.som.Constants.*;

/**
 * @author Lukasz
 * @since 29.05.2016.
 */
public class CSom {


    //the neurons representing the Self Organizing Map
    List<CNode> m_SOM = new ArrayList<>();

    //this holds the address of the winning node from the current iteration
    CNode m_pWinningNode;

    //this is the topological 'radius' of the feature map
    double m_dMapRadius;

    //used in the calculation of the neighbourhood width of influence
    double m_dTimeConstant;

    //the number of training iterations
    int m_iNumIterations;

    //keeps track of what iteration the epoch method has reached
    int m_iIterationCount;

    //the current width of the winning node's area of influence
    double m_dNeighbourhoodRadius;

    //how much the learning rate is adjusted for nodes within
    //the area of influence
    double m_dInfluence;

    double m_dLearningRate;

    //set true when training is finished
    boolean m_bDone;

    //the height and width of the cells that the nodes occupy when
    //rendered into 2D space.
    double m_dCellWidth;
    double m_dCellHeight;


    public CNode FindBestMatchingNode(final List<Double> vec) {
        CNode winner = null;

        double LowestDistance = m_SOM.get(0).CalculateDistance(vec);

        for (int n = 0; n < m_SOM.size(); ++n) {
            double dist = m_SOM.get(n).CalculateDistance(vec);

            if (dist < LowestDistance) {
                LowestDistance = dist;

                winner = m_SOM.get(n);
            }
        }
        return winner;
    }

//    public abstract double Gaussian(final double dist, final double sigma);

    public void Create(int cxClient, int cyClient, int CellsUp,
                       int CellsAcross, int NumIterations) {

        m_dCellWidth = (double) cxClient / (double) CellsAcross;
        Utils.log("cell width: " + m_dCellWidth);
        m_dCellHeight = (double) cyClient / (double) CellsUp;
        Utils.log("cell height: " + m_dCellHeight);
        m_iNumIterations = NumIterations;
        Utils.log("m_iNumIterations: " + m_iNumIterations);

        //create all the nodes
        for (int row = 0; row < CellsUp; ++row) {
            for (int col = 0; col < CellsAcross; ++col) {
                m_SOM.add(new CNode((int) (col * m_dCellWidth),           //left
                        (int) ((col + 1) * m_dCellWidth),       //right
                        (int) (row * m_dCellHeight),          //top
                        (int) ((row + 1) * m_dCellHeight),      //bottom
                        constSizeOfInputVector));   //num weights
            }
        }

        //this is the topological 'radius' of the feature map
        m_dMapRadius = max(constWindowWidth, constWindowHeight) / 2;

        //used in the calculation of the neighbourhood width of m_dInfluence
        m_dTimeConstant = m_iNumIterations / log(m_dMapRadius);
    }

    public boolean iteration(final List<List<Double>> data, GenericCallback<String> callback) {
        //make sure the size of the input vector matches the size of each node's
        //weight vector
        if (data.get(0).size() != constSizeOfInputVector) {
            Utils.log("data.get(0).size() != constSizeOfInputVector ");
            return false;
        }

        //return if the training is complete
        if (m_bDone) {
            callback.call("winning node: " + m_pWinningNode.toString());
            return true;
        }


        //enter the training loop
        if (--m_iNumIterations > 0) {
            //the input vectors are presented to the network at random
            int ThisVector = RandInt(0, data.size() - 1);

            //present the vector to each node and determine the BMU
            m_pWinningNode = FindBestMatchingNode(data.get(ThisVector));

            //calculate the width of the neighbourhood for this timestep
            m_dNeighbourhoodRadius = m_dMapRadius * exp(-(double) m_iIterationCount / m_dTimeConstant);

            //Now to adjust the weight vector of the BMU and its
            //neighbours

            //For each node calculate the m_dInfluence (Theta from equation 6 in
            //the tutorial. If it is greater than zero adjust the node's weights
            //accordingly
            for (CNode somNode : m_SOM) {
                //calculate the Euclidean distance (squared) to this node from the
                //BMU
                if (m_pWinningNode == null) {
                  //  Utils.log("m_pWinningNode is null");
                    return true;
                }
                double DistToNodeSq = (m_pWinningNode.X() - somNode.X()) *
                        (m_pWinningNode.X() - somNode.X()) +
                        (m_pWinningNode.Y() - somNode.Y()) *
                                (m_pWinningNode.Y() - somNode.Y());

                double WidthSq = m_dNeighbourhoodRadius * m_dNeighbourhoodRadius;

                //if within the neighbourhood adjust its weights
                if (DistToNodeSq < (m_dNeighbourhoodRadius * m_dNeighbourhoodRadius)) {

                    //calculate by how much its weights are adjusted
                    m_dInfluence = exp(-(DistToNodeSq) / (2 * WidthSq));

                    somNode.AdjustWeights(data.get(ThisVector),
                            m_dLearningRate,
                            m_dInfluence);
                }

            }//next node

            //reduce the learning rate
            m_dLearningRate = constStartLearningRate * exp(-(double) m_iIterationCount / m_iNumIterations);

            callback.call("winning node: " + m_pWinningNode.toString());
            ++m_iIterationCount;

        } else {
            m_bDone = true;
        }

        return true;
    }

    private int RandInt(int i, int i1) {
        return i + new Random().nextInt(i1);
    }

    public boolean FinishedTraining() {
        return m_bDone;
    }
}
