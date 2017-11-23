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
import java.util.*;

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
        /*
        e1 = G.insertEdge(s, a, 10, "e1");
        e2 = G.insertEdge(b, s, 15, "e2");
        e3 = G.insertEdge(b, a, 15, "e3");
        e4 = G.insertEdge(c, s, 20, "e4");
        e5 = G.insertEdge(d, c, 8, "e5");
        e6 = G.insertEdge(a, d, 6, "e6");
        e7 = G.insertEdge(d, t, 4, "e7");
        */
        e1 = G.insertEdge(s, a, 10, "s->a");
        e2 = G.insertEdge(s, b, 15, "s->b");
        e3 = G.insertEdge(b, a, 15, "b->a");
        e4 = G.insertEdge(b, c, 8, "b->c");
        e5 = G.insertEdge(c, t, 10, "c->t");
        e6 = G.insertEdge(a, t, 7, "a->t");
        //e7 = G.insertEdge(d, t, 4, "e7");

        Iterator i;
        Vertex v;
        Edge e;
        Vertex[] path = new Vertex[G.numVertices()];

        //int counter = 0;
        System.out.println("Iterating through vertices...");
        for (i= G.vertices(); i.hasNext(); ) {
            v = (Vertex) i.next();
            //path[counter++] = v;
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
        LinkedHashMap<Edge, Integer> flow = utils.initFlow(G);
        // TODO cleanup
        //for(Entry<Edge,Integer> value : flow.entrySet()) {
          //  System.out.println("Edge:"+value.getKey().getName()+" Flow:"+value.getValue());
        //}
        System.out.println();
        System.out.println("Residual graph Before Updating FLOW");
        System.out.println();
        //Compute Gf
        SimpleGraph Gf = utils.createResidualGraph(G, flow);
        //int counter = 0;
        System.out.println("Iterating through adjacency lists...");
        for (i= Gf.vertices(); i.hasNext(); ) {
            v = (Vertex) i.next();
            //path[counter++] = v;
            System.out.println("Vertex "+ v +" "+ v.getName() );
            Iterator j;
         
            for (j = Gf.incidentEdges(v); j.hasNext();) {
                e = (Edge) j.next();
               System.out.println("  found edge " + e.getName() + " first= "+ e.getFirstEndpoint().getName()+ "->" + e.getSecondEndpoint().getName() + " Data = " + e.getData());
            }
        }
        
        // get the current source node 
        Vertex source = utils.get_s(Gf);
        
        // get the current sink node 
        Vertex sink = utils.get_t(Gf);
        //System.out.println("Source: " + source.getName());
        //System.out.println("Sink: " + sink.getName() );

        // boolean isSTPath = utils.isSTPath(Gf, path, source, sink);
       // boolean isSTPath = utils.isSTPath(Gf,source, sink);
        /* print Residual graph Gf
        for (Iterator j = Gf.edges(); j.hasNext();) {
                e = (Edge) j.next();
               System.out.println("  found edge " + e.getName()+" First end = " + e.getFirstEndpoint().getName()
               + " Second end point:"+e.getSecondEndpoint().getName());
            }
           */

        // Create a new LinkedList to store the S-T path
        List<Vertex> path_new = new LinkedList<>();
        
        // returns s-t path if it exists, else returns null
        path_new = utils.getSTPath(Gf, sink, source);
        if(path_new != null)
        {
            path_new.forEach((v11) -> {
                System.out.println("dir: " + (v11).getName());
            });
        }
        // testing the bottleneck function
        int bottle_neck = utils.get_bottleneck(Gf, path_new);
        System.out.println("The bottleneck: " + bottle_neck);

        // test augment() for correctness
        utils.augment(G, Gf, flow, path_new);
        
        //Compute Gf again after updating flow once
        SimpleGraph Gf2 = utils.createResidualGraph(G, flow);
        
        System.out.println("Updated flow now:");
        for(Map.Entry<Edge,Integer> value : flow.entrySet()) {
            System.out.println("Edge:"+value.getKey().getName()+" Flow:"+value.getValue());  
            
        } 
        System.out.println();
        System.out.println("Residual graph After updating the flow ONCE");
        System.out.println();
        
        System.out.println("Iterating through adjacency lists...");
        for (i= Gf2.vertices(); i.hasNext(); ) {
            v = (Vertex) i.next();
            //path[counter++] = v;
            System.out.println("Vertex "+ v +" "+ v.getName() );
            Iterator j;
         
            for (j = Gf2.incidentEdges(v); j.hasNext();) {
                e = (Edge) j.next();
                System.out.println("  found edge " + e.getName()+  " first= "+ e.getFirstEndpoint().getName()+ "->" + e.getSecondEndpoint().getName() + " data =" + e.getData());
            }
        }
    }


}
