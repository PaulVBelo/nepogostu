package aisd.q06_dp;

import java.util.Arrays;
import java.util.Random;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.BiPredicate;

public class Main {
    private static final Random random = new Random(42);

    public static void main(String[] args) {
        // ==========================================================================
        System.out.println("=== Fibonacci Example ===");

        int n = random.nextInt(10, 30);
        System.out.println("n: " + n);

        System.out.println("fibonacci(n): " + Arrays.toString(Demo.fibonacci(n)));

        // ==========================================================================
        System.out.println("=== Grasshopper Example ===");

        int[] cells = grasshopperCells(12);
        System.out.println("a: " + Arrays.toString(cells));

        System.out.println("Max sum: " + Demo.grasshopper(cells));

        // ==========================================================================
        System.out.println("=== LIS Example ===");

        int[] sequence = randomIntArray(15, 0, 100);
        System.out.println("a: " + Arrays.toString(sequence));

        System.out.println("LIS: " + Arrays.toString(Demo.longestIncreasingSubsequence(sequence)));

        // ==========================================================================
        System.out.println("=== LCS Example ===");

        int[] A = randomIntArray(12, 0, 10);
        int[] B = randomIntArray(10, 0, 10);
        System.out.println("A: " + Arrays.toString(A));
        System.out.println("B: " + Arrays.toString(B));

        System.out.println("LCS: " + Arrays.toString(Demo.longestCommonSubsequence(A, B)));

        // ==========================================================================
        System.out.println("=== Knapsack Example ===");

        KnapsackTask task = randomKnapsack(8);
        System.out.println("w: " + Arrays.toString(task.w()));
        System.out.println("c: " + Arrays.toString(task.c()));
        System.out.println("W: " + task.W());

        int[] taken = Demo.knapsack(task.w(), task.c(), task.W());
        System.out.println("b: " + Arrays.toString(taken));
        if (taken != null) {
            System.out.println("(w, b) = " + dot(task.w(), taken) + ", (c, b) = " + dot(task.c(), taken));
        }

        // ==========================================================================
        System.out.println("=== Palindromic Subsequence Example ===");

        String word = randomString(15, "abc");
        System.out.println("S: " + word);

        System.out.println("Max palindrome length: " + Demo.longestPalindromicSubsequence(word));

        // ==========================================================================
        System.out.println("=== Bracket Subsequence Example ===");

        String brackets = noisyBracketString(6, 6);
        System.out.println("S: " + brackets);

        System.out.println("Max regular length: " + Demo.longestRegularBracketSubsequence(brackets));

        // ==========================================================================
        System.out.println("=== Knapsack (Masks) Example ===");

        // Same task as in the O(nW) example above: the answers must match
        int[] takenByMasks = Demo.knapsackMasks(task.w(), task.c(), task.W());
        System.out.println("b: " + Arrays.toString(takenByMasks));
        System.out.println("(w, b) = " + dot(task.w(), takenByMasks) + ", (c, b) = " + dot(task.c(), takenByMasks));

        // Small n, huge W: the O(nW) table would not fit in memory, masks don't care about W
        KnapsackTask bigTask = randomKnapsack(15, 100_000_000);
        System.out.println("w: " + Arrays.toString(bigTask.w()));
        System.out.println("c: " + Arrays.toString(bigTask.c()));
        System.out.println("W: " + bigTask.W());

        int[] bigTaken = Demo.knapsackMasks(bigTask.w(), bigTask.c(), bigTask.W());
        System.out.println("b: " + Arrays.toString(bigTaken));
        System.out.println("(w, b) = " + dot(bigTask.w(), bigTaken) + ", (c, b) = " + dot(bigTask.c(), bigTaken));

        // ==========================================================================
        System.out.println("=== Travelling Salesman Example ===");

        int[][] graph = randomCompleteGraph(8);
        System.out.println("w:");
        for (int[] row : graph) System.out.println("  " + Arrays.toString(row));

        int[] path = Demo.travellingSalesman(graph);
        System.out.println("Path: " + Arrays.toString(path));
        System.out.println("Cost: " + pathCost(graph, path));
    }

    // Values in [from, to)
    private static int[] randomIntArray(int size, int from, int to) {
        int[] result = new int[size];
        for (int i = 0; i < size; i++) result[i] = random.nextInt(from, to);
        return result;
    }

    // a[0] = 0 is the start, a[1..n] are the cells (negative values make the jumps matter)
    private static int[] grasshopperCells(int n) {
        int[] result = new int[n + 1];
        for (int i = 1; i <= n; i++) result[i] = random.nextInt(-50, 51);
        return result;
    }

    // Weights in [1, 20], costs in [1, 100], capacity = half of total weight
    private static KnapsackTask randomKnapsack(int n) {
        return randomKnapsack(n, 20);
    }

    // Weights in [1, maxWeight], costs in [1, 100], capacity = half of total weight
    // n * maxWeight must fit in int
    private static KnapsackTask randomKnapsack(int n, int maxWeight) {
        int[] w = randomIntArray(n, 1, maxWeight + 1);
        int[] c = randomIntArray(n, 1, 101);
        int W = Arrays.stream(w).sum() / 2;
        return new KnapsackTask(w, c, W);
    }

    private static long dot(int[] x, int[] y) {
        long result = 0;
        for (int i = 0; i < x.length; i++) result += (long) x[i] * y[i];
        return result;
    }

    // Random string of given length over the alphabet
    private static String randomString(int length, String alphabet) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) result.append(alphabet.charAt(random.nextInt(alphabet.length())));
        return result.toString();
    }

    // Random regular bracket sequence of `pairs` pairs of types (), [], {}
    // with `noise` random brackets inserted at random positions, so the answer is >= 2 * pairs
    private static String noisyBracketString(int pairs, int noise) {
        String open = "([{", close = ")]}", all = "()[]{}";
        StringBuilder result = new StringBuilder();
        Deque<Character> stack = new ArrayDeque<>();
        int opened = 0;
        while (result.length() < 2 * pairs) {
            if (opened < pairs && (stack.isEmpty() || random.nextBoolean())) {
                int type = random.nextInt(open.length());
                result.append(open.charAt(type));
                stack.push(close.charAt(type));
                opened++;
            } else {
                result.append(stack.pop());
            }
        }
        for (int k = 0; k < noise; k++) {
            result.insert(random.nextInt(result.length() + 1), all.charAt(random.nextInt(all.length())));
        }
        return result.toString();
    }

    // Symmetric weights of a complete graph on n vertices: w[i][j] = w[j][i] in [1, 100], w[i][i] = 0
    private static int[][] randomCompleteGraph(int n) {
        int[][] result = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                result[i][j] = result[j][i] = random.nextInt(1, 101);
            }
        }
        return result;
    }

    private static long pathCost(int[][] w, int[] path) {
        long result = 0;
        for (int i = 1; i < path.length; i++) result += w[path[i - 1]][path[i]];
        return result;
    }
}

record KnapsackTask(int[] w, int[] c, int W) {
}

class Demo {
    // Fibonacci as described in the related note: first n numbers, dp[0] = dp[1] = 1
    public static long[] fibonacci(int n) {
        if (n <= 1) return n == 0 ? new long[0] : new long[]{1};
        ;

        long[] dp = new long[n];

        dp[0] = 1;
        dp[1] = 1;

        for (int i = 2; i < n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }

        return dp;
    }

    // Grasshopper as described in the related note
    // a[0] = 0 is the start, jumps of 1 or 2 forward, must finish at a[n]
    // Returns the max sum of visited cells
    public static long grasshopper(int[] a) {
        if (a.length == 1) return 0;

        long[] dp = new long[a.length];

        dp[1] = a[1];

        for (int i = 2; i < a.length; i++) {
            dp[i] = Math.max(dp[i - 1], dp[i - 2]) + a[i];
        }

        return dp[a.length - 1];
    }

    // LIS as described in the related note
    // Returns the longest strictly increasing subsequence itself (elements, not length)
    public static int[] longestIncreasingSubsequence(int[] a) {
        if (a.length == 0) return new int[0];

        int[] dp = new int[a.length];
        int[] parent = new int[a.length];

        Arrays.fill(dp, 1);
        Arrays.fill(parent, -1);


        int lengthOfLIS = 1;
        int endOfLIS = 0;

        for (int i = 1; i < a.length; i++) {
            for (int j = 0; j < i; j++) {
                if (a[j] < a[i] && dp[i] < dp[j] + 1) {
                    dp[i] = dp[j] + 1;
                    parent[i] = j;
                }
            }
            if (dp[i] > lengthOfLIS) {
                lengthOfLIS = dp[i];
                endOfLIS = i;
            }
        }

        int[] LIS = new int[lengthOfLIS];

        int cur = endOfLIS;
        for (int i = 0; i < lengthOfLIS; i++) {
            LIS[lengthOfLIS - i - 1] = a[cur];
            cur = parent[cur];
        }

        return LIS;
    }

    // LCS as described in the related note
    // Returns the longest common subsequence itself (elements, not length)
    public static int[] longestCommonSubsequence(int[] A, int[] B) {
        if (A.length == 0 || B.length == 0) return new int[0];

        int[][] dp = new int[A.length + 1][B.length + 1];

        for (int i = 1; i < A.length + 1; i++) {
            for (int j = 1; j < B.length + 1; j++) {
                dp[i][j] = A[i - 1] == B[j - 1] ? dp[i - 1][j - 1] + 1 : Math.max(dp[i - 1][j], dp[i][j - 1]);
            }
        }


        int[] LCS = new int[dp[A.length][B.length]];

        int curSlot = dp[A.length][B.length] - 1;
        int curI = A.length;
        int curJ = B.length;

        while (curSlot >= 0) {
            while (A[curI - 1] != B[curJ - 1]) {
                if (dp[curI][curJ] == dp[curI - 1][curJ]) {
                    curI--;
                } else {
                    curJ--;
                }
            }

            LCS[curSlot] = A[curI - 1];
            curI--;
            curJ--;
            curSlot--;
        }

        return LCS;
    }

    // 0/1 Knapsack as described in the related note, O(nW)
    // Returns bit vector b: b[i] = 1 if item i is taken; (w, b) <= W, (c, b) -> max
    public static int[] knapsack(int[] w, int[] c, int W) {
        int n = w.length;

        int[][] dp = new int[n + 1][W + 1];

        for (int i = 1; i < n + 1; i++) {
            for (int v = 1; v < W + 1; v++) {
                if (v >= w[i - 1] && dp[i - 1][v - w[i - 1]] + c[i - 1] > dp[i - 1][v]) {
                    dp[i][v] = dp[i - 1][v - w[i - 1]] + c[i - 1];
                } else {
                    dp[i][v] = dp[i - 1][v];
                }
            }
        }

        int[] b = new int[n];

        int curV = W;
        for (int i = n; i >= 1; i--) {
            if (dp[i][curV] != dp[i - 1][curV]) {
                curV -= w[i - 1];
                b[i - 1] = 1;
            }
        }

        return b;
    }

    // Longest palindromic subsequence as described in the related note, O(|S|^2)
    // Returns the length
    public static int longestPalindromicSubsequence(String s) {
        int[][] dp = new int[s.length()][s.length()];

        for (int i = 0; i < s.length(); i++) dp[i][i] = 1;

        for (int len = 2; len < s.length() + 1; len++) {
            for (int l = 0; l < s.length() - len + 1; l++) {
                int r = l + len - 1;
                dp[l][r] = s.charAt(l) == s.charAt(r) ? dp[l + 1][r - 1] + 2 : Math.max(dp[l][r - 1], dp[l + 1][r]);
            }
        }

        return dp[0][s.length() - 1];
    }

    // Longest regular bracket subsequence as described in the related note, O(|S|^3)
    // Bracket types: (), [], {}. Returns the length (not the number of deletions)
    public static int longestRegularBracketSubsequence(String s) {
        if (s.isEmpty()) return 0;

        int[][] dp = new int[s.length() + 1][s.length()];

        for (int i = 0; i < s.length(); i++) dp[i][i] = 1;

        for (int len = 2; len < s.length() + 1; len++) {
            for (int l = 0; l < s.length() - len + 1; l++) {
                int r = l + len - 1;

                dp[l][r] = dp[l + 1][r] + 1;

                for (int m = l + 1; m < r + 1; m++) {
                    if (isMatchingPair(s.charAt(l), s.charAt(m))) {
                        dp[l][r] = Math.min(dp[l][r], dp[l+1][m-1] + dp[m+1][r]);
                    }
                }
            }
        }

        return s.length() - dp[0][s.length() - 1];
    }

    private static boolean isMatchingPair(char open, char close) {
        return (open == '(' && close == ')') || (open == '[' && close == ']') || (open == '{' && close == '}');
    }

    // 0/1 Knapsack via subset masks as described in the related note, O(2^n * n)
    // Same contract as knapsack: returns bit vector b, which is exactly the best mask
    public static int[] knapsackMasks(int[] w, int[] c, int W) {
        int n = w.length;
        final int NEG_INF = Integer.MIN_VALUE;

        int[] dp = new int[1 << n];         // cost of the subset, or NEG_INF if it doesn't fit
        long[] weight = new long[1 << n];   // weight of the subset (long: sums of big weights)
        Arrays.fill(dp, NEG_INF);
        dp[0] = 0;

        int bestMask = 0;
        for (int mask = 0; mask < (1 << n); mask++) {
            if (dp[mask] == NEG_INF) continue;  // doesn't fit, so supersets don't fit either
            if (dp[mask] > dp[bestMask]) bestMask = mask;

            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) continue;
                int next = mask | (1 << i);
                weight[next] = weight[mask] + w[i];
                dp[next] = weight[next] <= W ? dp[mask] + c[i] : NEG_INF;
            }
        }

        int[] b = new int[n];
        for (int i = 0; i < n; i++) b[i] = (bestMask >> i) & 1;
        return b;
    }

    // Travelling salesman (cheapest Hamiltonian path) via subset masks as described in the related note, O(2^n * n^2)
    // Returns the order of vertices; the path may start and end anywhere
    public static int[] travellingSalesman(int[][] w) {
        int n = w.length;
        if (n == 0) return new int[0];

        final int INF = Integer.MAX_VALUE / 2;  // INF + edge weight doesn't overflow
        int full = (1 << n) - 1;

        int[][] dp = new int[1 << n][n];        // dp[mask][v]: cheapest path over mask ending at v
        int[][] parent = new int[1 << n][n];    // previous vertex on that path
        for (int[] row : dp) Arrays.fill(row, INF);
        for (int v = 0; v < n; v++) dp[1 << v][v] = 0;

        for (int mask = 1; mask <= full; mask++) {
            for (int u = 0; u < n; u++) {
                if ((mask & (1 << u)) != 0) continue;
                int next = mask | (1 << u);

                for (int v = 0; v < n; v++) {
                    if ((mask & (1 << v)) == 0) continue;
                    if (dp[mask][v] + w[v][u] < dp[next][u]) {
                        dp[next][u] = dp[mask][v] + w[v][u];
                        parent[next][u] = v;
                    }
                }
            }
        }

        int last = 0;
        for (int v = 1; v < n; v++) {
            if (dp[full][v] < dp[full][last]) last = v;
        }

        int[] path = new int[n];
        int mask = full;
        for (int k = n - 1; k >= 0; k--) {
            path[k] = last;
            int prev = parent[mask][last];
            mask ^= 1 << last;
            last = prev;
        }

        return path;
    }
}