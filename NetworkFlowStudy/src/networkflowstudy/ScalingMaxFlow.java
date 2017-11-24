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
 * Calculates and returns flow using Scaling max Flow algorithm
 * @author anisha
 */
public class ScalingMaxFlow {

    SimpleGraph G;
    private SimpleGraph Gf;
    LinkedHashMap<Edge, Integer> flow;
    Vertex sourceGf, sinkGf;

    public ScalingMaxFlow(SimpleGraph G) {
        this.G = G;
        flow = utils.initFlow(G);
        Gf = utils.createResidualGraph(G, flow);
    }

    /**
     * Calculate flow of a network using Scaling max- flow algorithm
     * @param source vertex sourceG
     * @param sink vertex sinkG
     * @return flow
     */
    public LinkedHashMap<Edge, Integer> calculateFlow(Vertex sourceG,
            Vertex sinkG) {

        sourceGf = Gf.getVertex((String) sourceG.getName());
        sinkGf = Gf.getVertex((String) sinkG.getName());

        int delta = getDelta(sourceGf);

        while (delta >= 1) {
            System.out.println("delta:" + delta);
            List<Vertex> path = new LinkedList<>();

            path = utils.getSTPath(Gf, sinkGf, sourceGf, delta);

            while (path != null) {

                logging.printPath(path);
                utils.augment(G, Gf, flow, path);
                logging.printFlow(flow);
                Gf = utils.createResidualGraph(G, flow);
                sourceGf = Gf.getVertex((String) sourceG.getName());
                sinkGf = Gf.getVertex((String) sinkG.getName());
                path = utils.getSTPath(Gf, sinkGf, sourceGf, delta);

                //utils.updateResidualGraph(G, Gf, flow, path);
            }
            delta /= 2;
        }

        return flow;
    }

    /**
     * calculate delta for a residual graph G
     * @param source vertex in residual graph 
     * @return delta
     */
    public int getDelta(Vertex source) {
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
