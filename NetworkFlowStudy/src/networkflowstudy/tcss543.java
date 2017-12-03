/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkflowstudy;

import graphCode.SimpleGraph;
import graphCode.Edge;
import graphCode.GraphInput;
import graphCode.SimpleGraph;
import graphCode.Vertex;
import java.io.IOException;
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
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        SimpleGraph G = new SimpleGraph();
        GraphInput graphGenrerator = new GraphInput();
        //graphGenrerator.LoadSimpleGraph(G, args[0]);
        graphGenrerator.LoadSimpleGraph(G, "C:\\Users\\bhagatsanchya\\Desktop\\UWash\\Adv. Algorithms\\projectnf_fresh\\NetworkFlowStudy\\src\\graphGenerationCode\\FixedDegree\\fixed_250.txt");
        
        Vertex source = G.getVertex("s");
        Vertex sink = G.getVertex("t");
        
        long startTime;
        long endTime;
        int maxFlow;
        LinkedHashMap<Edge, Integer> flow;
        
        logging.printGraph(G);
        System.out.println();
        System.out.println("******************************************");
        /****SCALING MAX FLOW CALCULATION*****/          
        
        System.out.println("Calling scaling max flow");
        startTime = System.currentTimeMillis();
        ScalingMaxFlow scalingMaxFlow = new ScalingMaxFlow(G);
        flow = scalingMaxFlow.calculateFlow(source, 
                sink);
        //logging.printFlow(flow);
        maxFlow = utils.getMaxFlow(G, source, flow);
        // TODO add these outputs to console output
        //System.out.println("maxFlow from Scaling Max Flow:"+ maxFlow);
        endTime = System.currentTimeMillis();
        System.out.println("||Algorithm: Scaling Max Flow||" + "Number of vertices: " + G.numVertices() +"||Running time:" + (endTime- startTime)
                +"||maxFlow:" + maxFlow+"||");
        SaveOutput.writeToCSV("Mesh","Scaling Max flow", G.numVertices(), 
                endTime- startTime, maxFlow);

        
        System.out.println("******************************************");
        /****FORD FULKERSON MAX FLOW CALCULATION*****/      
        
        System.out.println("Calling Ford fulkerson max flow");
        
        startTime = System.currentTimeMillis();
        MaxFlow FFmaxFlow = new MaxFlow(G);
        flow = FFmaxFlow.calculateFlow(source, sink);
        //logging.printFlow(flow);
        maxFlow = utils.getMaxFlow(G, source, flow);
      
        endTime = System.currentTimeMillis();
        System.out.println("||Algorithm: Ford fulkerson ||" + "Number of vertices: " + G.numVertices() +"||Running time:" + (endTime- startTime)
                +"||maxFlow:" + maxFlow + "||");
        // TODO add these outputs to console output
        SaveOutput.writeToCSV("Mesh", "FordFulkerson", G.numVertices(), 
                endTime- startTime, maxFlow);
        
        
        
        System.out.println("******************************************");
        /****PREFLOW PUSH MAX FLOW CALCULATION*****/        
        System.out.println("Calling Preflow Push max flow");
        
        startTime = System.currentTimeMillis();
        PreflowPush preflowPush = new PreflowPush(G);
        flow = preflowPush.GetMaxFlow();
        //logging.printFlow(flow);
        maxFlow = utils.getMaxFlow(G, source, flow);
        // TODO add these outputs to console output
        //System.out.println("maxFlow:"+ maxFlow);
        endTime = System.currentTimeMillis();
        System.out.println("||Algorithm: Preflow Push||" + "Number of vertices: " + G.numVertices() +"||Running time:" + (endTime- startTime)
                +"||maxFlow:" + maxFlow +"||");
        SaveOutput.writeToCSV("Mesh", "PreflowPush", G.numVertices(), 
                endTime- startTime, maxFlow);
        
        System.out.println("******************************************");
        
    }


}
