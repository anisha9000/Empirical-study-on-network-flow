package graphGenerationCode.Random;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class BuildGraph {

    public static void main(String[] args) {
        if (args.length != 5) {
            System.out.println("\nInvalid parameters!");
            System.out.println("Usage:");
            System.out.println("java BuildGraph v, d, min, max, f");
            System.out.println("v - the number of vertices in the graph");
            System.out.println("d - density of graph");
            System.out.println("min - the lower bound on edge capacities");
            System.out.println("max - the upper bound on edge capacities");
            System.out.println("f - path and file name for saving this graph");
            System.out.println("Example: java BuildGraph 999 50 75 101 graph1.txt");
        } else if (Integer.parseInt(args[3]) >= Integer.parseInt(args[2])) {
                BuildGraph(args[4], "",
                        Integer.parseInt(args[0]),
                        Integer.parseInt(args[1]),
                        Integer.parseInt(args[3]),
                        Integer.parseInt(args[2]));
                System.out.println("\nDONE!");
            } else {
                System.out.println("\nFAIL!");
                System.out.println("Max must be greater than or equal to min.");
            }
    }

    private static void BuildGraph(String fileName, String directory, 
            int vertices, int dense, int maxCapacity, int minCapacity) {
        Random random = new Random();
        try {
            String dirName = directory;//           
            if (dirName.equals("")) {
                dirName = ".";
            }

            File outputfile = new File(dirName, fileName);
            int[][] Graph = new int[vertices][vertices];
            int n, m;

            for (n = 0; n < vertices; n++) {
                for (m = n + 1; m < vertices; m++) {
                    int randomInt = (random.nextInt((maxCapacity - minCapacity + 1)) + minCapacity);

                    int k = (int) (1000.0 * Math.random() / 10.0);
                    int b = (k < dense) ? 1 : 0;
                    if (b == 0) {
                        Graph[n][m] = Graph[m][n] = b;
                    } else {
                        Graph[n][m] = Graph[m][n] = randomInt;
                    }
                }
            }

            PrintWriter output = new PrintWriter(new FileWriter(outputfile));

            for (int x = 0; x < Graph.length; x++) {
                if (x == 0) {
                    for (int y = 0; y < Graph[x].length; y++) {
                        String value = String.valueOf(Graph[x][y]);
                        if (y != 0) {
                            if (value.equals("0") == false) {
                                output.print("s " + String.valueOf(y) + " " + value + "\n");
                            }
                        }
                    }
                } else {
                    if (x == Graph.length - 1) {
                        for (int y = 0; y < Graph[x].length; y++) {
                            String value = String.valueOf(Graph[x][y]);
                            if (y != 0) {
                                if (value.equals("0") == false) {
                                    output.print(String.valueOf(y) + " t " + value + "\n");
                                }
                            }
                        }
                    } else {
                        for (int y = 0; y < Graph[x].length; y++) {
                            String value = String.valueOf(Graph[x][y]);
                            if (y != 0) {
                                if (value.equals("0") == false) {
                                    output.print(x + " " + String.valueOf(y) + " " + value + "\n");
                                }
                            }
                        }
                    }
                }

            }

            output.close();
        } catch (IOException e) {
            System.err.println("Error opening file" + e);
            return;
        }
        System.out.print("\nDone");
    }

}
