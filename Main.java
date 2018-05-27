import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    private static int nbvar;
    private static int nbclause;
    private static int[] clausesA;
    private static int[] clausesB;

    private static class Solver {
        private int MAX = 1000;
        private List<List<Integer>> adj;
        private List<List<Integer>> adjInv;
        private int visited[] = new int[MAX];
        private int visitedInv[] = new int[MAX];
        private Stack<Integer> s = new Stack<>();
        private int scc[] = new int[MAX];
        private int counter = 1;

        Solver() {
            adj = new ArrayList<>();
            adjInv = new ArrayList<>();

            fillCollections(adj);
            fillCollections(adjInv);

        }

        private void fillCollections(List<List<Integer>> list) {
            for (int i = 0; i < MAX; i++) {
                list.add(new ArrayList<>());
            }
        }


        private void addEdges(int a, int b) {
            adj.get(a).add(b);
        }

        private void addEdgesInverse(int a, int b) {
            adjInv.get(b).add(a);
        }

        private void dfsFirst(int u) {
            if (visited[u] == 1)
                return;
            visited[u] = 1;

            for (int i = 0; i < adj.get(u).size(); i++)
                dfsFirst(adj.get(u).get(i));

            s.push(u);
        }

        private void dfsSecond(int u) {
            if (visitedInv[u] == 1)
                return;

            visitedInv[u] = 1;

            for (int i = 0; i < adjInv.get(u).size(); i++)
                dfsSecond(adjInv.get(u).get(i));

            scc[u] = counter;
        }

        void is2Satisfiable(int n, int c, int a[], int b[]) {
            for (int i = 0; i < c; i++) {
                // x is mapped to x
                // -x is mapped to n+x = n-(-x)
                // for a[i] or b[i], addEdges -a[i] -> b[i]
                // AND -b[i] -> a[i]
                if (a[i] > 0 && b[i] > 0) {
                    addEdges(a[i] + n, b[i]);
                    addEdgesInverse(a[i] + n, b[i]);
                    addEdges(b[i] + n, a[i]);
                    addEdgesInverse(b[i] + n, a[i]);
                } else if (a[i] > 0 && b[i] < 0) {
                    addEdges(a[i] + n, n - b[i]);
                    addEdgesInverse(a[i] + n, n - b[i]);
                    addEdges(-b[i], a[i]);
                    addEdgesInverse(-b[i], a[i]);
                } else if (a[i] < 0 && b[i] > 0) {
                    addEdges(-a[i], b[i]);
                    addEdgesInverse(-a[i], b[i]);
                    addEdges(b[i] + n, n - a[i]);
                    addEdgesInverse(b[i] + n, n - a[i]);
                } else {
                    addEdges(-a[i], n - b[i]);
                    addEdgesInverse(-a[i], n - b[i]);
                    addEdges(-b[i], n - a[i]);
                    addEdgesInverse(-b[i], n - a[i]);
                }
            }

            // STEP 1 of Kosaraju's Algorithm which
            // traverses the original graph
            for (int i = 1; i <= 2 * n; i++) {
                if (visited[i] == 0)
                    dfsFirst(i);
            }
            // STEP 2 pf Kosaraju's Algorithm which
            // traverses the inverse graph. After this,
            // array scc[] stores the corresponding value
            while (!s.empty()) {
                int k = s.peek();
                s.pop();

                if (visitedInv[k] == 0) {
                    dfsSecond(k);
                    counter++;
                }
            }

            System.out.print("Klauzula je ");
            for (int i = 1; i <= n; i++) {
                // for any 2 variable x and -x lie in
                // same SCC
                if (scc[i] == scc[i + n]) {
                    System.out.println("NESPLNITELNA");
                    return;
                }
            }
            System.out.println("SPLNITELNA");

            System.out.println("Premenne:");
            for (int i = 1; i <= n; i++) {
                System.out.println(i + (scc[i] >= scc[i + n] ? " = True" : " = False"));
            }
        }
    }

    static void load(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename + ".txt"))) {
            String sCurrentLine;
            int lnIndex = 0;
            String[] line;
            sCurrentLine = br.readLine();
            line = sCurrentLine.split(" ");
            nbvar = Integer.parseInt(line[0]);
            nbclause = Integer.parseInt(line[1]);

            clausesA = new int[nbclause];
            clausesB = new int[nbclause];

            while ((sCurrentLine = br.readLine()) != null) {
                String[] s = sCurrentLine.split(" ");

                int x = Integer.parseInt(s[0]);
                clausesA[lnIndex] = x;

                int y = Integer.parseInt(s[1]);
                clausesB[lnIndex] = y != 0 ? y : x;

                lnIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        load("dimacs");

        Solver solve2SAT = new Solver();
        solve2SAT.is2Satisfiable(nbvar, nbclause, clausesA, clausesB);
    }
}
