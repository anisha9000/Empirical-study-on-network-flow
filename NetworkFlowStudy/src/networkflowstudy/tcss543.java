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
        Vertex s,a,b,c,d,t;
        Edge e1,e2,e3,e4,e5,e6,e7;
        s = G.insertVertex(null, "s");
        a = G.insertVertex(null, "a");
        b = G.insertVertex(null, "b");
        c = G.insertVertex(null, "c");
        d = G.insertVertex(null, "d");
        t = G.insertVertex(null, "t");
        
        e1 = G.insertEdge(s, a, 10, "e1");
        e2 = G.insertEdge(b, s, 15, "e2");
        e3 = G.insertEdge(b, a, 15, "e3");
        e4 = G.insertEdge(c, s, 20, "e4");
        e5 = G.insertEdge(d, c, 8, "e5");
        e6 = G.insertEdge(a, d, 6, "e6");
        e7 = G.insertEdge(d, t, 4, "e7");
        
        
        Iterator i;
        Vertex v;
        Edge e;
        Vertex[] path = new Vertex[G.numVertices()];
        
        int counter = 0;
        System.out.println("Iterating through vertices...");
        for (i= G.vertices(); i.hasNext(); ) {
            v = (Vertex) i.next();
            path[counter++] = v;
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
        
        
        boolean isSTPath = utils.isSTPath(G, path, s, t);
        
    }
    
}
