import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import javax.swing.*;
import java.awt.*;

class GraphDisplays {

    static void showLineGraph(double[] k, double[] y, String title, String yTitle) {
        final XYChart chart = new XYChartBuilder().width(1200).height(800).title(title).xAxisTitle("K Value").yAxisTitle(yTitle).build();
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

    static void showNetworkGraph(int[][] capacityGraph, String title) {
        Graph graph = new SingleGraph(title);

        String styleSheet = "edge {" +
                "	fill-color: red;" +
                "}";
        graph.setAttribute("ui.stylesheet", styleSheet);

        int n = capacityGraph.length;
        for (int i = 0; i < n; i++) {
            Node node = graph.addNode(Integer.toString(i));
            node.addAttribute("ui.label", node.getId());
        }

        for (int i =0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int capacity = capacityGraph[i][j];
                if (capacity > 0) {
                    Edge e = graph.addEdge(i + " " + j, Integer.toString(i), Integer.toString(j), true);
                    e.addAttribute("ui.label", Integer.toString(capacity));
                }
            }
        }

        graph.display();
    }
}
