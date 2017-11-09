/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkflowstudy;

import graphCode.Edge;
import graphCode.SimpleGraph;
import graphCode.Vertex;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author anisha
 */
public class utils {
    /**
     * Initialize the flow Hashmap for the graph G
     * @param G
     * @return Hashmap containing <Edge, flow value>  pairs
     */
    public static HashMap<Edge, Integer> initFlow(SimpleGraph G) {
        HashMap<Edge,Integer> flow = new HashMap<>();
        Iterator i = G.edges();
        while(i.hasNext()) {
            Edge e = (Edge) i.next();
            flow.put(e, 0);
        }
        return flow;
    }
    
    public static SimpleGraph createResidualGraph(SimpleGraph G, 
            HashMap<Edge, Integer>  flow) {
        
        SimpleGraph Gf = new SimpleGraph();
        HashMap<String, Vertex> vertexList = new HashMap<>();
        
        // Insert all vertex in Gf
        Iterator i = G.vertices();
        while(i.hasNext()) {
            Vertex v = (Vertex) i.next();
            Gf.insertVertex(null, v.getName());
            vertexList.put(String.valueOf(v.getName()), v);
        }
        
        //Insert edges based on flow
        i = G.edges();
        while(i.hasNext()) {
            Edge e = (Edge) i.next();
            Integer flowE = flow.get(e);
            Integer edgeCapacity = (Integer) e.getData();
            String startVertex = String.valueOf(e.getFirstEndpoint().getName());
            String endVertex = String.valueOf(e.getSecondEndpoint().getName());
            
            
            if(flowE.compareTo(edgeCapacity) < 0) {
                //Forward edge
                Edge fe = Gf.insertEdge(vertexList.get(startVertex), 
                        vertexList.get(endVertex), edgeCapacity - flowE, "fe");
            }
            
            if(flowE.compareTo(0) > 0) {
                // Backward edge
                Edge be = Gf.insertEdge(vertexList.get(startVertex), 
                        vertexList.get(endVertex), flowE, "be");
            }
        }
        
        return Gf;
    }
}
