/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkflowstudy;

import graphCode.Edge;
import graphCode.SimpleGraph;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author anisha
 */
public class utils {
    public static HashMap<Edge, Double> initFlow(SimpleGraph G) {
        HashMap<Edge,Double> flow = new HashMap<>();
        Iterator i = G.edges();
        while(i.hasNext()) {
            Edge e = (Edge) i.next();
            flow.put(e, Double.valueOf(0));
        }
        return flow;
    }
}
