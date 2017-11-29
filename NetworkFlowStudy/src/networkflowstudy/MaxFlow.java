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
 * Calculates the max Flow using Ford Fulkerson
 * @author bhagatsanchya
 */
public class MaxFlow {
    SimpleGraph G;
    private SimpleGraph Gf;
    LinkedHashMap<Edge, Integer> flow;
    Vertex sourceGf, sinkGf;

    public MaxFlow(SimpleGraph G) {
        this.G = G;
        flow = utils.initFlow(G);
        Gf = utils.createResidualGraph(G, flow);
    }
    
    /**
     * Calculate flow of a network using Ford fulkerson max- flow algorithm
     * @param sourceG vertex sourceG
     * @param sinkG vertex sinkG
     * @return flow
     */
    public LinkedHashMap<Edge, Integer> calculateFlow(Vertex sourceG,
            Vertex sinkG) {
        
        sourceGf = Gf.getVertex((String) sourceG.getName());
        sinkGf = Gf.getVertex((String) sinkG.getName());

        List<Vertex> path = new LinkedList<>();
        path = utils.getSTPath(Gf, sinkGf, sourceGf);

        while (path != null) {

            //logging.printPath(path);
            utils.augment(G, Gf, flow, path);
            //logging.printFlow(flow);
            Gf = utils.createResidualGraph(G, flow);
            sourceGf = Gf.getVertex((String) sourceG.getName());
            sinkGf = Gf.getVertex((String) sinkG.getName());
            path = utils.getSTPath(Gf, sinkGf, sourceGf);
            //utils.updateResidualGraph(G, Gf, flow, path);
        }
        return flow;
    }
    
}
