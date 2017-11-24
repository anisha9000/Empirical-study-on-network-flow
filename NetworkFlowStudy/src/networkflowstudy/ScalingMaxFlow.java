/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkflowstudy;

import graphCode.Edge;
import graphCode.SimpleGraph;
import graphCode.Vertex;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author anisha
 */
public class ScalingMaxFlow {
    SimpleGraph G;
    SimpleGraph Gf;
    
    public ScalingMaxFlow(SimpleGraph G, LinkedHashMap<Edge, Integer> flow) {
        this.G= G;
        Gf = utils.createResidualGraph(G, flow);
    }
    
    public int calculateFlow(Vertex source, Vertex sink) {
        //Initialize flow
        LinkedHashMap<Edge, Integer> flow = utils.initFlow(G);
        
        int delta = getDelta(source);
        while(delta >= 1) {
            List<Vertex> path = new LinkedList<>();
        
            // returns s-t path if it exists, else returns null
            path = utils.getSTPath(Gf, sink, source, delta);
            while(path != null) {
                
            }
            delta /= 2;
        }
        
        return -1;
    }
    
    public int getDelta(Vertex source) {
        // TODO
        return 4;
    }
}
