import java.util.ArrayList;

class ShortestPathAlgorithm {

    private static final int NO_PARENT = -1;
    private static ArrayList<Integer> path = new ArrayList<>();

    private static ArrayList[] getShortestPaths_Src2All(int[][] adjacencyMatrix, int startVertex) {
        int nVertices = adjacencyMatrix[0].length;

        int[] shortestDistances = new int[nVertices];
        boolean[] added = new boolean[nVertices];

        for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++) {
            shortestDistances[vertexIndex] = Integer.MAX_VALUE;
            added[vertexIndex] = false;
        }

        shortestDistances[startVertex] = 0;
        int[] parents = new int[nVertices];

        parents[startVertex] = NO_PARENT;

        for (int i = 1; i < nVertices; i++) {
            int nearestVertex = -1;
            int shortestDistance = Integer.MAX_VALUE;
            for (int vertexIndex = 0;
                 vertexIndex < nVertices;
                 vertexIndex++) {
                if (!added[vertexIndex] && shortestDistances[vertexIndex] < shortestDistance) {
                    nearestVertex = vertexIndex;
                    shortestDistance = shortestDistances[vertexIndex];
                }
            }

            added[nearestVertex] = true;

            for (int vertexIndex = 0;
                 vertexIndex < nVertices;
                 vertexIndex++) {
                int edgeDistance = adjacencyMatrix[nearestVertex][vertexIndex];

                if (edgeDistance > 0
                        && ((shortestDistance + edgeDistance) <
                        shortestDistances[vertexIndex])) {
                    parents[vertexIndex] = nearestVertex;
                    shortestDistances[vertexIndex] = shortestDistance +
                            edgeDistance;
                }
            }
        }

        return getPaths(startVertex, shortestDistances, parents);
    }

    private static ArrayList[] getPaths(int startVertex, int[] distances, int[] parents) {
        ArrayList[] paths = new ArrayList[distances.length];

        for (int vertexIndex = 0; vertexIndex < distances.length; vertexIndex++) {
            if (vertexIndex != startVertex) {
                ShortestPathAlgorithm.path = new ArrayList<>();
                getPath(vertexIndex, parents);
                paths[vertexIndex] = ShortestPathAlgorithm.path;
            } else {
                paths[vertexIndex] = new ArrayList<>();
                paths[vertexIndex].add(vertexIndex);
            }
        }

        return paths;
    }

    private static void getPath(int currentVertex, int[] parents) {
        if (currentVertex == NO_PARENT) {
            return;
        }
        getPath(parents[currentVertex], parents);
        ShortestPathAlgorithm.path.add(currentVertex);
    }

    static ArrayList[][] getShortestPaths_All2All(int[][] adjacencyMatrix) {
        int n = adjacencyMatrix[0].length;

        ArrayList[][] shortestPaths = new ArrayList[n][n];

        for (int i = 0; i < n; i++) {
            shortestPaths[i] = getShortestPaths_Src2All(adjacencyMatrix, i);
        }

        return shortestPaths;
    }
}
