import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class Main {
    private int nbvar;
    private int nbclause;
    private static HashMap<Integer, Edges> G;

    private static class Edges{
        int id;
        private static LinkedList<Integer> edges;

        public Edges(int id, int b){
            this.id = id;
            edges = new LinkedList<>();
            edges.add(b);
        }

        public void addEdge(int id){
            edges.add(id);
        }
    }

    void load(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename + ".txt"))) {
            String sCurrentLine;
            int lnIndex = 0;
            String[] line;
            ///dists = new int[15][15];

            while ((sCurrentLine = br.readLine()) != null) {
                if (lnIndex == 0) {
                    line = sCurrentLine.split(" ");
                    nbvar = Integer.parseInt(line[0]);
                    nbclause = Integer.parseInt(line[1]);
                    G = new HashMap<>();
                    lnIndex++;
                } else {
                    line = sCurrentLine.split(" ");

                }
                lnIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addEdge(int a, int b) {
        // find existing ~a and ~b nodes in graph G
        Edges esA = G.get(-a);
        Edges esB = G.get(-b);

        // add edge ~A -> B
        if(esA != null){
            esA.addEdge(b);
        } else {
            G.put(-a, new Edges(-a, b));
        }

        // add edge ~B -> A
        if(esB != null){
            esB.addEdge(a);
        } else {
            G.put(-b, new Edges(-b, a));
        }
    }


    public static void main(String[] args) {

    }
}
