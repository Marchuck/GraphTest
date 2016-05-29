package topics.som;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Lukasz
 * @since 29.05.2016.
 */
public class CNode {


    //this node's weights
    List<Double> m_dWeights = new ArrayList<>();

    //its position within the lattice
    public double m_dX, m_dY;

    //the edges of this node's cell. Each node, when draw to the client
    //area, is represented as a rectangular cell. The color of the cell
    //is set to the RGB value its weights represent.
    int m_iLeft;
    int m_iTop;
    int m_iRight;
    int m_iBottom;


    public CNode(int lft, int rgt, int top, int bot, int NumWeights) {
        m_iLeft = lft;
        m_iRight = rgt;
        m_iBottom = bot;
        m_iTop = top;
        m_iTop = top;
        Random rand = new Random();
        //initialize the weights to small random variables
        for (int w = 0; w < NumWeights; ++w) {
            m_dWeights.add(rand.nextDouble());
        }

        //calculate the node's center
        m_dX = m_iLeft + (double) (m_iRight - m_iLeft) / 2;
        m_dY = m_iTop + (double) (m_iBottom - m_iTop) / 2;
    }


    double X() {
        return m_dX;
    }

    double Y() {
        return m_dY;
    }


    //------------------------------ Render ----------------------------------
//
//------------------------------------------------------------------------
    void Render(HDC surface) {

//        //create a brush and pen of the correct color
//        int red = (int) (m_dWeights.get(0) * 255);
//        int green = (int) (m_dWeights.get(1) * 255);
//        int blue = (int) (m_dWeights.get(2) * 255);
//
//        HBRUSH brush = CreateSolidBrush(RGB(red, green, blue));
//        HPEN pen = CreatePen(PS_SOLID, 1, RGB(red, green, blue));
//
//        HBRUSH OldBrush = (HBRUSH) SelectObject(surface, brush);
//        HPEN OldPen = (HPEN) SelectObject(surface, pen);
//
//        Rectangle(surface, m_iLeft, m_iTop, m_iRight, m_iBottom);
//
//        SelectObject(surface, OldBrush);
//        SelectObject(surface, OldPen);
//
//        DeleteObject(brush);
//        DeleteObject(pen);

    }

    @Override
    public String toString() {
        return "CNode{" +
                "m_dX=" + m_dX +
                ", m_dY=" + m_dY +
                ", m_iLeft=" + m_iLeft +
                ", m_iTop=" + m_iTop +
                ", m_iRight=" + m_iRight +
                ", m_iBottom=" + m_iBottom +
                '}';
    }

    //-------------------------- CalculateDistance ---------------------------
//
//  returns the euclidean distance (squared) between the node's weights
//  and the input vector
//------------------------------------------------------------------------
    double CalculateDistance(final List<Double> InputVector) {
        double distance = 0;

        for (int i = 0; i < m_dWeights.size(); ++i) {
            distance += (InputVector.get(i) - m_dWeights.get(i)) *
                    (InputVector.get(i) - m_dWeights.get(i));
        }
        return distance;
    }

    //-------------------------- AdjustWeights -------------------------------
//
//  Given a learning rate and a target vector this function adjusts
//  the node's weights accordingly
//------------------------------------------------------------------------
    public void AdjustWeights(final List<Double> target,
                       final double LearningRate,
                       final double Influence) {
        for (int w = 0; w < target.size(); ++w) {
            m_dWeights.set(w, m_dWeights.get(w) + LearningRate * Influence *
                    (target.get(w) - m_dWeights.get(w)));
        }
    }
}