package ui.agds.tabs.correlation;

import common.Utils;
import javafx.util.Pair;
import topics.agds.nodes.RecordNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz
 * @since 27.05.2016.
 */
public class Correlation {

    public static void test() {
        pearsonLinearCorrelation(new double[]{
                1, 2, 3, 4
        }, new double[]{
                0.2, 5, 13, 40
        });
    }

    public static double pearsonLinearCorrelation(double[] xs, double[] ys) {
//        Utils.log("Input parameters: " + printedVector(xs) + ",\n" + printedVector(ys));

        if (vectorCheck(xs, ys)) throw new IllegalStateException("Input vectors invalid");
        double L = calculateL(xs, ys);
        double P = calculateP(xs, ys);
        double result = L / P;
//        Utils.log("Retruning result " + String.format("%.6f", result));

        return normalizeToCorrelationLvl(result);

    }

    private static double normalizeToCorrelationLvl(double result) {
        double dMin = 0.8522665405d;
        double dMax = 1d;
        double fixedResult = result - dMin;
        double fixedMax = dMax - dMin;
        double factor = 2 / fixedMax;

        return factor * fixedResult - 1;
    }

    private static double calculateP(double[] xs, double[] ys) {
        return Math.sqrt(deviation(xs) * deviation(ys));
    }

    private static double deviation(double[] xs) {
        double sum = 0d;
        double mean = mean(xs);
        for (double d : xs) {
            sum += d * d - mean * mean;
        }
        return sum / xs.length;
    }


    private static double calculateL(double[] xs, double[] ys) {
        double sum = 0d;
        double x_ = mean(xs);
        double y_ = mean(ys);
        for (int j = 0; j < xs.length; j++) {
            sum += xs[j] * ys[j] - x_ * y_;
        }
        return sum / xs.length;
    }

    private static double mean(double[] ys) {
        double sum = 0d;
        for (double d : ys) {
            sum += d;
        }
        return sum / ys.length;
    }

    public static double compute(double[] xs, double[] ys) {
        //TODO: check here that arrays are not null, of the same length etc
        if (vectorCheck(xs, ys)) throw new IllegalStateException("Input vectors invalid");
        Utils.log("Input parameters: " + printedVector(xs) + ",\n" + printedVector(ys));
        double sx = 0.0;
        double sy = 0.0;
        double sxx = 0.0;
        double syy = 0.0;
        double sxy = 0.0;

        int n = xs.length;

        for (int i = 0; i < n; ++i) {
            double x = xs[i];
            double y = ys[i];

            sx += x;
            sy += y;
            sxx += x * x;
            syy += y * y;
            sxy += x * y;
        }

        // covariation
        double cov = sxy / n - sx * sy / n / n;
        // standard error of x
        double sigmax = Math.sqrt(sxx / n - sx * sx / n / n);
        // standard error of y
        double sigmay = Math.sqrt(syy / n - sy * sy / n / n);

        // correlation is just a normalized covariation
        return cov / sigmax / sigmay;
    }

    private static boolean vectorCheck(double[] xs, double[] ys) {
        return (xs == null || ys == null || xs.length != ys.length);
    }

    private static String printedVector(double[] xs) {
        StringBuilder x = new StringBuilder();
        x.append("[");
        if (xs.length >= 1) {
            for (int j = 0; j < xs.length - 1; j++)
                x.append(xs[j]).append(",");
            x.append(xs[xs.length - 1]);
        }
        x.append("]");
        return x.toString();

    }

    public static void getMinMaxCorrelation(List<Pair<RecordNode, String>> currentSimilarNodes) {
        List<RecordNode> nodes = new ArrayList<>();
        double min, max;

        for (Pair<RecordNode, String> pair : currentSimilarNodes) nodes.add(pair.getKey());
        min = Correlation.pearsonLinearCorrelation(nodes.get(0), nodes.get(1));
        max = Correlation.pearsonLinearCorrelation(nodes.get(0), nodes.get(1));

        for (RecordNode recordNode : nodes) {
            for (RecordNode recordNode1 : nodes) {
                if (!recordNode.getName().equalsIgnoreCase(recordNode1.getName())) {
                    double tmp = Correlation.pearsonLinearCorrelation(recordNode, recordNode1);
                    if (tmp > max) max = tmp;
                    if (tmp < min) {
                        min = tmp;
                        Utils.log("Lowest correlation for nodes: " + recordNode.getName() + ", " + recordNode1.getName());
                    }
                }
            }
        }
        Utils.log("Min correlation: " + String.format("%.10f", min));
        Utils.log("Max correlation: " + String.format("%.10f", max));
    }

    public static double pearsonLinearCorrelation(RecordNode recordNode, RecordNode recordNode1) {
        double[] xs = common.Utils.asVector(recordNode);
        double[] ys = common.Utils.asVector(recordNode1);
        return pearsonLinearCorrelation(xs, ys);
    }
}
