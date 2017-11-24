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
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author anisha
 */
public class ScalingMaxFlow {

    SimpleGraph G;
    private SimpleGraph Gf;
    LinkedHashMap<Edge, Integer> flow;

    public ScalingMaxFlow(SimpleGraph G) {
        this.G = G;
        flow = utils.initFlow(G);
        Gf = utils.createResidualGraph(G, flow);
    }

    public LinkedHashMap<Edge, Integer> calculateFlow(Vertex sourceG,
            Vertex sinkG) {

        Vertex sourceGf = Gf.getVertex((String) sourceG.getName());
        Vertex sinkGf = Gf.getVertex((String) sinkG.getName());

        int delta = getDelta(sourceGf);
        
        while (delta >= 1) {
            System.out.println("delta:" + delta);
            List<Vertex> path = new LinkedList<>();

            // returns s-t path if it exists, else returns null
            path = utils.getSTPath(Gf, sinkGf, sourceGf, delta);
            
            while (path != null) {
                path = utils.getSTPath(Gf, sinkGf, sourceGf, delta);
                utils.printPath(path);
                utils.augment(G, Gf, flow, path);
                utils.printFlow(flow);
                Gf = utils.createResidualGraph(G, flow);
                utils.printGraph(Gf);
                
                //utils.updateResidualGraph(G, Gf, flow, path);
            }
            delta /= 2;
        }

        return flow;
    }

    public int getDelta(Vertex source) {
        // TODO
        int delta = 1;
        Iterator i = Gf.incidentEdges(source);
        while (i.hasNext()) {
            Edge e = (Edge) i.next();
            if (e.getFirstEndpoint().equals(source)) {
                int edgeCapacity = (int) e.getData();
                int largestPow = (int) Math.pow(2,
                        Math.floor(Math.log(edgeCapacity) / Math.log(2)));
                if (delta < largestPow) {
                    delta = largestPow;
                }
            }
        }
        return delta;
    }
}
