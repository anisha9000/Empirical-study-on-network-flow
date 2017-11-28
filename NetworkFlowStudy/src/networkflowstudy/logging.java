/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkflowstudy;

import graphCode.Edge;
import graphCode.SimpleGraph;
import graphCode.Vertex;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author anisha
 */
public class logging {
    /**
     * Given a graph G, print all the nodes and their incident edges
     *
     * @param G
     */
    public static void printGraph(SimpleGraph G) {
        Iterator i;
        Vertex v;
        Edge e;

        System.out.println("Iterating through graph");
        for (i = G.vertices(); i.hasNext();) {
            v = (Vertex) i.next();
            System.out.println("Vertex " + v.getName());
            Iterator j;

            for (j = G.incidentEdges(v); j.hasNext();) {
                e = (Edge) j.next();
                System.out.println("  found edge " + e.getName() + " from "
                        + e.getFirstEndpoint().getName() + " to "
                        + e.getSecondEndpoint().getName() + " of value "
                        + e.getData());
            }
        }
    }

    /**
     * Given an edge e, print it's values
     *
     * @param e
     */
    public static void printEdge(Edge e) {
        System.out.println("Edge " + e.getName() + " from "
                + e.getFirstEndpoint().getName() + " to "
                + e.getSecondEndpoint().getName() + " of value " + e.getData());

    }

    /**
     * Given an flow, print the value across all the edges
     *
     * @param flow
     */
    public static void printFlow(LinkedHashMap<Edge, Integer> flow) {
        Iterator i;

        System.out.println("Iterating through flow");
        for (Map.Entry<Edge, Integer> edge : flow.entrySet()) {
            System.out.println("edge:" + 
                    edge.getKey().getFirstEndpoint().getName() + " to " + 
                    edge.getKey().getSecondEndpoint().getName() + ", value: " +
                    edge.getValue());
        }
    }

    /**
     * Print the vertices in a given path
     * @param path 
     */
    public static void printPath(List<Vertex> path) {
        System.out.println("Path:");
        if (path != null) {
            path.forEach((v11) -> {
                System.out.println("dir: " + (v11).getName());
            });
        }

    }
}
