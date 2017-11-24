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
        
        utils.printGraph(G);
        
        //Initialize flow
        LinkedHashMap<Edge, Integer> flow = utils.initFlow(G);
        
        System.out.println("Residual graph Before Updating FLOW");
        //Compute Gf
        SimpleGraph Gf = utils.createResidualGraph(G, flow);
        
        utils.printGraph(Gf);
        
        // get the current source node 
        Vertex source = Gf.getVertex("s");
        
        // get the current sink node 
        Vertex sink = Gf.getVertex("t");

        // Create a new LinkedList to store the S-T path
        //TODO List<Edges>
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
        
        utils.printGraph(Gf2);
        
        System.out.println("Calling scaling max flow");
        ScalingMaxFlow maxFlow = new ScalingMaxFlow(G);
        maxFlow.calculateFlow(s, t);

        
    }


}
