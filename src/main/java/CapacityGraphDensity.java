import java.util.ArrayList;

public class CapacityGraphDensity {

    public static int[][] computeMinCapacityGraph(int[][] aijGraph, int[][] bijGraph) {
        int n = aijGraph[0].length;
        int[][] capacityGraph = new int[n][n];

        ArrayList[][] shortestPaths = ShortestPathAlgorithm.getShortestPaths_All2All(aijGraph);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int i2j_demand = bijGraph[i][j];
                ArrayList<Integer> i2j_shortestPath = shortestPaths[i][j];
                if (i2j_shortestPath.size() > 1) {
                    for (int s = 1; s < i2j_shortestPath.size(); s++) {
                        int iFrom = i2j_shortestPath.get(s - 1);
                        int iTo   = i2j_shortestPath.get(s);

                        capacityGraph[iFrom][iTo] += i2j_demand;
                    }
                }
            }
        }

        return capacityGraph;
    }

    public static int getTotalCost(int[][] aijGraph, int[][] capacityGraph) {
        int totalCost = 0;
        int n = aijGraph.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                totalCost += capacityGraph[i][j] * aijGraph[i][j];
            }
        }
        return totalCost;
    }

    public static double getDensity(int[][] capacityGraph) {
        int n = capacityGraph.length;
        int numNonZeroEdges = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (capacityGraph[i][j] != 0) {
                    numNonZeroEdges++;
                }
            }
        }

        return ((double) numNonZeroEdges) / ((double) (n * (n - 1)));
    }
}
