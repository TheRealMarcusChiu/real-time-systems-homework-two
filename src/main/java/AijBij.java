import java.util.Arrays;
import java.util.Random;

public class AijBij {

    public static int[][] generateBijGraph(int[] ds) {
        int n = ds.length;
        int[][] bij = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                bij[i][j] = Math.abs(ds[i] - ds[j]);
            }
        }

        return bij;
    }

    public static int[][] generateAijGraph(int n, int k) {
        int[][] aij = new int[n][n];

        for (int i = 0; i < n; i++) {
            int[] randomKIndices = getRandomIndices(n, k, i);
            for (int j = 0; j < n; j++) {
                if (contains(randomKIndices, j)) {
                    aij[i][j] = 1;
                } else {
                    aij[i][j] = 100;
                }
            }
            aij[i][i] = 0;
        }

        return aij;
    }

    public static boolean contains(final int[] arr, final int key) {
        return Arrays.stream(arr).anyMatch(i -> i == key);
    }

    private static int[] getRandomIndices(int n, int k, int excludeIndex) {
        int[] randomIndices = new int[k];

        for (int i = 0; i < k; i++) {
            int index = new Random().nextInt(n);

            if (!contains(randomIndices, index) && excludeIndex != index) {
                randomIndices[i] = index;
            } else {
                i--;
            }
        }

        return randomIndices;
    }
}
