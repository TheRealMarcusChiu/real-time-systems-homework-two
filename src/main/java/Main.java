import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Main {

    private static double[][] copy2Darray(double[][] old) {
        double[][] current = new double[old.length][old.length];
        for (int i = 0; i < old.length; i++)
            System.arraycopy(old[i], 0, current[i], 0, old[i].length);
        return current;
    }

    private static double[][] removeRow(double[][] g, int rowRemove) {
        int row = g.length;
        int col = g[0].length;

        double[][] gCopy = new double[row - 1][col];

        for (int i = 0; i < rowRemove; i++) {
            System.arraycopy(g[i], 0, gCopy[i], 0, col);
        }
        for (int i = rowRemove + 1; i < g.length; i++) {
            System.arraycopy(g[i], 0, gCopy[i - 1], 0, col);
        }

        return gCopy;
    }

    private static double[][] removeCol(double[][] array, int colRemove) {
        int row = array.length;
        int col = array[0].length;

        double[][] newArray = new double[row][col - 1];

        for (int i = 0; i < row; i++) {
            for (int j = 0, currColumn = 0; j < col; j++) {
                if (j != colRemove) {
                    newArray[i][currColumn++] = array[i][j];
                }
            }
        }

        return newArray;
    }

    private static double[][] merge(double[][] g, int x, int y) {
        double[][] gCopy = copy2Darray(g);

        for (int i = 0; i < g.length; i++) {
            gCopy[i][x] = gCopy[i][x] + gCopy[i][y];
        }
        gCopy = removeCol(gCopy, y);

        for (int i = 0; i < gCopy[0].length; i++) {
            gCopy[x][i] = gCopy[x][i] + gCopy[y][i];
        }
        gCopy = removeRow(gCopy, y);

        for (int i = 0; i < gCopy.length; i++) {
            gCopy[i][i] = 0;
        }

        return gCopy;
    }

    private static double numEdgesFromNextVertex2ChosenVertices(double[][] g, ArrayList<Integer> chosenVertices, int nextVertex) {
        double pathSum = 0;
        for (Integer integer : chosenVertices) {
            pathSum += g[nextVertex][integer];
        }
        return pathSum;
    }

    private static ArrayList<Integer> possible(double[][] g, ArrayList<Integer> chosenVertices) {
        ArrayList<Integer> possibleVertices = new ArrayList<>();
        for (int i = 0; i < g.length; i++) {
            if (!chosenVertices.contains(i)) {
                possibleVertices.add(i);
            }
        }
        return possibleVertices;
    }

    private static ArrayList<Integer> maximumAdjacencyOrder(double[][] g) {
        ArrayList<Integer> chosenVertices = new ArrayList<>();
        while (chosenVertices.size() != g.length) {
            ArrayList<Integer> possibleVertices = possible(g, chosenVertices);
            int nextVertex = possibleVertices.get(0);
            double numEdgesFromNextVertex2ChosenVertices = numEdgesFromNextVertex2ChosenVertices(g, chosenVertices, nextVertex);
            for (int possibleVertex : possibleVertices) {
                double val = numEdgesFromNextVertex2ChosenVertices(g, chosenVertices, possibleVertex);
                if (val > numEdgesFromNextVertex2ChosenVertices) {
                    nextVertex = possibleVertex;
                    numEdgesFromNextVertex2ChosenVertices = val;
                }
            }
            chosenVertices.add(nextVertex);
        }
        return chosenVertices;
    }

    private static int degree(double[][] g, int vertex) {
        int degree = 0;
        for (int i = 0; i < g.length; i++) {
            degree += g[vertex][i];
        }
        return degree;
    }

    private static double lamb(double[][] g) {
        if (g.length == 2) {
            return g[0][1];
        } else {
            ArrayList<Integer> mao = maximumAdjacencyOrder(g);
            Integer x = mao.get(mao.size() - 2);
            Integer y = mao.get(mao.size() - 1);
            double degree = degree(g, y);
            double[][] gXYmerged = merge(g, x, y);
            double lambdaOfgXY = lamb(gXYmerged);
            return Math.min(degree, lambdaOfgXY);
        }
    }

    private static double[][] generateRandomGraph(Integer numNodes, Integer numEdges) {
        double[][] g = new double[numNodes][numNodes];

        Random rand = new Random();
        for (int e = 0; e < numEdges; e++) {
            int i = rand.nextInt(numNodes);
            int j = rand.nextInt(numNodes);
            if (i == j || g[i][j] == 1) {
                e--;
            } else {
                g[i][j] = 1;
            }
        }

        return g;
    }

    private static double averageOfArrayList(ArrayList<Double> marks) {
        Double sum = 0d;
        if (!marks.isEmpty()) {
            for (Double mark : marks) {
                sum += mark;
            }
            return sum / marks.size();
        }
        return sum;
    }

    private static void showLineGraph(double[] k, double[] y, String title, String xTitle, String yTitle) {
        final XYChart chart = new XYChartBuilder().width(1200).height(800)
                .title(title)
                .xAxisTitle(xTitle)
                .yAxisTitle(yTitle)
                .build();
        chart.addSeries(title, k, y);

        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(title);
            frame.setLayout(new BorderLayout());
            JPanel chartPanel = new XChartPanel<>(chart);
            frame.add(chartPanel, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);
        });
    }

    private static ArrayList<Double> getSortedSet(ArrayList<Double> averages) {
        Set<Double> averagesSet = new HashSet<>(averages);
        ArrayList<Double> averagesSetSorted = new ArrayList<>(averagesSet);
        Collections.sort(averagesSetSorted);
        return averagesSetSorted;
    }

    private static ArrayList<Double> something(ArrayList<Double> averages, ArrayList<Double> averagesSetSorted) {
        ArrayList<Double> spread = new ArrayList<>();

        Collections.sort(averages);
        for (Double average : averagesSetSorted) {
            double smallest = findSmall(averages, average);
            double largest = findLarge(averages, average);
            spread.add(largest - smallest);
        }

        return spread;
    }

    private static int findSmall(ArrayList<Double> sortedAverages, double average) {
        for (int i = 0; i < sortedAverages.size(); i++) {
            if (sortedAverages.get(i) == average) {
                return (i * 3) + 19;
            }
        }
        return 0;
    }

    private static int findLarge(ArrayList<Double> sortedAverages, double average) {
        for (int i = sortedAverages.size() - 1; i >= 0; i--) {
            if (sortedAverages.get(i) == average) {
                return (i * 3) + 19;
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        // Calculate Lambda Values of Random Graphs of M Edges
        ArrayList<Double> averages = new ArrayList<>();
        ArrayList<Double> xValues = new ArrayList<>();
        for (int m = 19; m <= 190; m += 3) {
            xValues.add((double) m);
            ArrayList<Double> edgeConnectivities = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                edgeConnectivities.add(lamb(generateRandomGraph(20, m)));
            }
            averages.add(averageOfArrayList(edgeConnectivities));
        }

        // Display (M vs Lambda)
        double[] averagesArray = averages.stream().mapToDouble(i -> i).toArray();
        double[] xValuesArray = xValues.stream().mapToDouble(i -> i).toArray();
        showLineGraph(xValuesArray, averagesArray, "M Value vs Average Lambda", "M Values", "Average Lambdas");

        // Display (Lambda vs Spread)
        ArrayList<Double> averagesSetSorted = getSortedSet(averages);
        ArrayList<Double> spread = something(averages, averagesSetSorted);
        double[] averagesSetSortedArray = averagesSetSorted.stream().mapToDouble(i -> i).toArray();
        double[] spreadArray = spread.stream().mapToDouble(i -> i).toArray();
        showLineGraph(averagesSetSortedArray, spreadArray, "Average Lambda vs Spread", "Average Lambdas", "Spread");
    }
}
