/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkflowstudy;

import graphCode.SimpleGraph;
import graphCode.Edge;
import graphCode.SimpleGraph;
import graphCode.Vertex;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 *
 * @author anisha
 */
public class tcss543 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        SimpleGraph G = new SimpleGraph();
        Vertex v, w, a, b, c;
        Edge e, x, y;
        v = G.insertVertex(null, "a");
        a = v;
        w = G.insertVertex(null, "b");
        b = w;
        e = G.insertEdge(v, w, 10, "X");
        x = e;
        v = G.insertVertex(null, "c");
        c = v;
        e = G.insertEdge(w, v, 15, "Y");
        y = e;
        
        Iterator i;
        
        System.out.println("Iterating through vertices...");
        for (i= G.vertices(); i.hasNext(); ) {
            v = (Vertex) i.next();
            System.out.println("found vertex " + v.getName());
        }

        System.out.println("Iterating through adjacency lists...");
        for (i= G.vertices(); i.hasNext(); ) {
            v = (Vertex) i.next();
            System.out.println("Vertex "+v.getName());
            Iterator j;
            
            for (j = G.incidentEdges(v); j.hasNext();) {
                e = (Edge) j.next();
                System.out.println("  found edge " + e.getName());
            }
        }
        
        //Initialize flow
        HashMap<Edge, Integer> flow = utils.initFlow(G);
        // TODO cleanup
        for(Entry<Edge,Integer> value : flow.entrySet()) {
            System.out.println("Edge:"+value.getKey()+" Flow:"+value.getValue());        
        }
        
        //Compute Gf
        SimpleGraph Gf = utils.createResidualGraph(G, flow);
        
        System.out.println("Iterating through adjacency lists...");
        for (i= Gf.vertices(); i.hasNext(); ) {
            v = (Vertex) i.next();
            System.out.println("Vertex "+v.getName());
            Iterator j;
            
            for (j = Gf.incidentEdges(v); j.hasNext();) {
                e = (Edge) j.next();
                System.out.println("  found edge " + e.getName());
            }
        }
    }
    
}
