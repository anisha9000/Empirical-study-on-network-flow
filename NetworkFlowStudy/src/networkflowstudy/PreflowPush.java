/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkflowstudy;

import graphCode.SimpleGraph;
import graphCode.Edge;
import graphCode.Vertex;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

/**
 *
 * @author jason
 */
public class PreflowPush {
    private HashMap<Edge, Integer>   flows;                                     // a hashmap to store flow values
    private HashMap<Vertex, Integer> heights;                                   // a hashmap to store heights of vertices
    private ArrayList<Vertex>        vertices;                                  // a vertex vector to work with
    private ArrayList<Edge>          edges;                                     // an edge vector to work with
    private Iterator                 i;                                         // iterator for a graph
    
    private void Push(int flow, int height, Vertex v, Vertex w) {
        
    }
    
    private void Relabel(int flow, int height, Vertex v) {
        
    }

    private void GetMaxFlow() {
        
    }
    
    /**
     * 
     * @param graph 
     */
    public PreflowPush(SimpleGraph graph) {
        for (i = graph.vertices(); i.hasNext(); )                               // extract all vertices
            vertices.add((Vertex)i.next());
        for (i = graph.edges(); i.hasNext(); )                                  // extract all edges
            edges.add((Edge)i.next());
        for (int i = 0; i < vertices.size(); i++)                               // initialize all nodes height to zero
            heights.put(vertices.get(i), 0);
        for (int j = 0; j < vertices.size(); j++) {                             // initialize start node height to n
            if (vertices.get(j).getName() == "s")
                heights.put(vertices.get(j), vertices.size());
        }
        for (int k = 0; k < edges.size(); k++) {                                // initialize flow of all edges from s to some node v, to the capacity
            String e1 = (String)edges.get(k).getFirstEndpoint().getName();
            String e2 = (String)edges.get(k).getSecondEndpoint().getName();
            if (e1.equals("s") || e2.equals("s"))
                flows.put(edges.get(k), (int)edges.get(k).getData());
        }
    }
}

