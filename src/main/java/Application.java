import java.util.ArrayList;

public class Application {
    public static void main(String[] args) {
        int n = 20;
        int kMin = 3;
        int kMax = 14;
        int[] dValues = {2, 0, 2, 1, 1, 4, 0, 1, 8, 8, 2, 0, 2, 1, 1, 4, 0, 1, 8, 8};

        ArrayList<int[][]> capacityGraphs = new ArrayList<>();
        double[] totalCosts = new double[kMax - kMin + 1];
        double[] densities = new double[kMax - kMin + 1];

        for (int k = kMin; k <= kMax; k++) {
            int[][] bijGraph = AijBij.generateBijGraph(dValues);
            int[][] aijGraph = AijBij.generateAijGraph(n, k);

            int[][] capacity = CapacityGraphDensity.computeMinCapacityGraph(aijGraph, bijGraph);
            int totalCost = CapacityGraphDensity.getTotalCost(aijGraph, capacity);
            double density = CapacityGraphDensity.getDensity(capacity);

            capacityGraphs.add(capacity);
            totalCosts[k - kMin] = totalCost;
            densities[k - kMin] = density;
        }

        double[] k = new double[kMax - kMin + 1];
        for (int i = kMin; i <= kMax; i++) {
            k[i - kMin] = i;
        }
        GraphDisplays.showLineGraph(k, totalCosts, "Total Cost Graph", "Total Cost Value");
        GraphDisplays.showLineGraph(k, densities, "Density Graph", "Density Value");

        GraphDisplays.showNetworkGraph(capacityGraphs.get(0), "K = 3");
        GraphDisplays.showNetworkGraph(capacityGraphs.get(5), "K = 8");
        GraphDisplays.showNetworkGraph(capacityGraphs.get(11), "K = 14");
    }
}
