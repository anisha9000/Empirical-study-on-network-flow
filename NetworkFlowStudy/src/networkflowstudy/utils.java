/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkflowstudy;

import graphCode.Edge;
import graphCode.SimpleGraph;
import graphCode.Vertex;
import java.util.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Stack;

/**
 *
 * @author anisha
 */
public class utils {

    /**
     * Initialize the flow LinkedHashmap for the graph G
     *
     * @param G simple graph G
     * @return LinkedHashmap containing <Edge, flow value> pairs
     */
    public static LinkedHashMap<Edge, Integer> initFlow(SimpleGraph G) {
        LinkedHashMap<Edge, Integer> flow = new LinkedHashMap<>();
        Iterator i = G.edges();
        while (i.hasNext()) {
            Edge e = (Edge) i.next();
            flow.put(e, 0);
        }
        return flow;
    }

    /**
     * Create a residual graph based on a network flow G=(V,E)
     *
     * @param G a simple graph G
     * @param flow flow across the edges
     * @return Residual Graph Gf
     */
    public static SimpleGraph createResidualGraph(SimpleGraph G,
            LinkedHashMap<Edge, Integer> flow) {
        // Initialize an instance of simple graph to be used as Residual Graph
        SimpleGraph Gf = new SimpleGraph();

        // Insert all vertex in Gf
        Iterator i = G.vertices();
        while (i.hasNext()) {
            Vertex v = (Vertex) i.next();
            v = Gf.insertVertex(null, v.getName());
        }

        //Insert edges based on flow
        HashMap<String, Vertex> gfVertexList = Gf.getVertexMap();
        i = G.edges();
        while (i.hasNext()) {
            Edge e = (Edge) i.next();
            Integer flowE = flow.get(e);
            Integer edgeCapacity = ((Double)e.getData()).intValue();
            String startVertex = String.valueOf(e.getFirstEndpoint().getName());
            String endVertex = String.valueOf(e.getSecondEndpoint().getName());

            if (flowE.compareTo(edgeCapacity) < 0) {
                //Forward edge
                Edge fe = Gf.insertEdge(gfVertexList.get(startVertex),
                        gfVertexList.get(endVertex), edgeCapacity - flowE, "fe");
            }

            if (flowE.compareTo(0) > 0) {
                // Backward edge
                Edge be = Gf.insertEdge(gfVertexList.get(endVertex),
                        gfVertexList.get(startVertex), flowE, "be");
            }

        }

        return Gf;
    }

    /**
     * Returns s-t path of graph Gf, if there is no simple path , returns 0
     *
     * @param Gf
     * @param Source
     * @param Sink
     * @return null if no s-t path else valid s-t path
     */
    public static List<Vertex> getSTPath(SimpleGraph Gf, Vertex Sink, Vertex Source) {
        return getSTPath(Gf, Sink, Source, 1);

    }

    /**
     * Return s-t path for a graph Gf based on the limiting capacity delta
     *
     * @param Gf
     * @param sink
     * @param source
     * @param delta
     * @return
     */
    public static List<Vertex> getSTPath(SimpleGraph Gf, Vertex sink,
            Vertex source, int delta) {

        //Initializations
        Map<Vertex, Boolean> visited = new HashMap<>();
        List<Vertex> directions = new LinkedList<>();
        Queue<Vertex> queue = new LinkedList<>();
        Map<Vertex, Vertex> prev = new HashMap<>();

        Vertex current = source;
        queue.add(current);
        visited.put(current, true);
        
        System.out.println("finding S-t path with delta:"+ delta);
        
        while (!queue.isEmpty()) {
            current = queue.poll();
            //System.out.println("Current "+current.getName());
            if (current.equals(sink)) {
                break;
            } else {
                Iterator i1 = Gf.incidentEdges(current);
                while (i1.hasNext()) {
                    Edge e = (Edge) i1.next();
                    //printEdge(e_Gf);
                    int edgeCapacity = (int) e.getData();
                    if (current.equals(e.getFirstEndpoint()) 
                            && edgeCapacity >= delta) {

                        Vertex node = e.getSecondEndpoint();
                        //System.out.println("node considered:"+ node.getName());
                        if (!visited.containsKey(node)) {
                            queue.add(node);
                            visited.put(node, true);
                            prev.put(node, current);
                        }
                    }

                }
            }
        }
        if (!current.getName().equals(sink.getName())) {
            System.out.println("can't reach destination");
            return null;
        }
        for (Vertex node = sink; node != null; node = prev.get(node)) {
            directions.add(node);
        }

        Collections.reverse(directions);
        return directions;

    }

    /**
     * Returns bottleneck for the given s-t path of graph Gf
     *
     * @param Gf
     * @param st_path
     * @return b_neck
     */
    public static int get_bottleneck(SimpleGraph Gf, List<Vertex> st_path) {
        ListIterator i = st_path.listIterator();
        int b_neck = Integer.MAX_VALUE;
        while (i.hasNext()) {
            Vertex v1 = (Vertex) (i.next());

            if (i.hasNext()) {
                Vertex v2 = (Vertex) (i.next());

                Iterator i_edge = Gf.incidentEdges(v1);
                while (i_edge.hasNext()) {
                    Edge e = (Edge) (i_edge.next());
                    if (Gf.opposite(v1, e).equals(v2)) {
                        Integer edge_capacity = (Integer) (e.getData());
                        // System.out.println(v1.getName() + " "+ v2.getName()+ " " + edge_capacity);
                        if (edge_capacity < b_neck) {
                            b_neck = edge_capacity;
                        }
                    }

                }
                i.previous();
            }

        }
        return b_neck;
    }

    /**
     * Calculates the increase in flow using get_bottleneck() and updates the
     * flow LinkedHashmap
     *
     * @param G
     * @param Gf
     * @param flow
     * @param path
     *
     */
    public static void augment(SimpleGraph G, SimpleGraph Gf, LinkedHashMap<Edge, Integer> flow, List<Vertex> path) {
        Map<String, Edge> edgeList_G = new HashMap<>();
        Map<Vertex, Boolean> vis = new HashMap<>();
       
        Iterator i1 = G.edges();
        while (i1.hasNext()) {
            Edge e_G = (Edge) i1.next();

            edgeList_G.put(String.valueOf(e_G.getFirstEndpoint().getName() + "->" + e_G.getSecondEndpoint().getName()), e_G);
            //System.out.println("edgelist_G" + e_G + " Data:" + e_G.getData() + "first and second node "
            //        + e_G.getFirstEndpoint().getName() + " " + e_G.getSecondEndpoint().getName());
        }
        ListIterator i_path = path.listIterator();
        
        int b_neck = get_bottleneck(Gf, path);
        //System.out.println("bneck = "+ b_neck);
        while (i_path.hasNext()) {
            
            Vertex v1 = (Vertex) (i_path.next());
            

            if (i_path.hasNext()) {
                Vertex v2 = (Vertex) (i_path.next());
                
               // visited.put(v2, true);

                Iterator i_edge = Gf.incidentEdges(v1);
                while (i_edge.hasNext()) {
                    Edge e_Gf = (Edge) (i_edge.next());
                    //System.out.println("visited: "+visited.get(v2));
                    if (Gf.opposite(v1, e_Gf).equals(v2)&&(!vis.containsKey(v2))) {
                        // once the correct edge is found, we make sure not to go backwards again.
                        vis.put(v2, true);
                        Integer edge_capacity = (Integer) (e_Gf.getData());
                        // ***TODO Update residual capacity for residual capacity = 0: i_path.e_G. removing edge 
                        if (e_Gf.getName().equals("fe")) {
                            
                            /*
                            System.out.println("Edge " + e_Gf.getName() +" first and second endpoints:  "+e_Gf.getFirstEndpoint().getName() +" "+ 
                                    e_Gf.getSecondEndpoint().getName()+ " data now:" + e_Gf.getData());
                            
                            System.out.println(e_Gf.getFirstEndpoint().getName() + "->" + e_Gf.getSecondEndpoint().getName());
                            System.out.println((Edge) edgeList_G.get(String.valueOf(e_Gf.getFirstEndpoint().getName() + "->" + e_Gf.getSecondEndpoint().getName())));
                            
                            
                            System.out.println("current flow");
                            System.out.println(flow.get((Edge) edgeList_G.get(String.valueOf(e_Gf.getFirstEndpoint().getName() + "->" + e_Gf.getSecondEndpoint().getName()))));
                            */
                            Integer current_flow = flow.get((Edge) edgeList_G.get(String.valueOf(e_Gf.getFirstEndpoint().getName() + "->" + e_Gf.getSecondEndpoint().getName())));
                            //System.out.println("current flow is : " + current_flow);
                            Integer updated_edge_flow = current_flow + b_neck;
                            flow.put((Edge) edgeList_G.get(String.valueOf(e_Gf.getFirstEndpoint().getName() + "->" + e_Gf.getSecondEndpoint().getName())), updated_edge_flow);
                            // System.out.println(v1.getName() + " "+ v2.getName()+ " " + edge_capacity);
                        }
                        else if (e_Gf.getName().equals("be"))
                        {
                            /*
                            System.out.println("Edge" + e_Gf.getName() + " data now:" + e_Gf.getData());
                            System.out.println(e_Gf.getFirstEndpoint().getName() + "->" + e_Gf.getSecondEndpoint().getName());
                            System.out.println((Edge) edgeList_G.get(String.valueOf(e_Gf.getFirstEndpoint().getName() + "->" + e_Gf.getSecondEndpoint().getName())));
                            
                            System.out.println("Edge " + e_Gf.getName() +" first and second endpoints:  "+e_Gf.getFirstEndpoint().getName() +" "+ 
                                    e_Gf.getSecondEndpoint().getName()+ " data now:" + e_Gf.getData());
                            */
                            Integer current_flow = flow.get((Edge) edgeList_G.get(String.valueOf(e_Gf.getFirstEndpoint().getName() + "->" + e_Gf.getSecondEndpoint().getName())));
                            //System.out.println("current flow is : " + current_flow);
                            //System.out.println(flow.get((Edge) edgeList_G.get(String.valueOf(e_Gf.getFirstEndpoint().getName() + "->" + e_Gf.getSecondEndpoint().getName()))));
                            Integer updated_edge_flow = current_flow - b_neck;
                            flow.put((Edge) edgeList_G.get(String.valueOf(e_Gf.getFirstEndpoint().getName() + "->" + e_Gf.getSecondEndpoint().getName())), updated_edge_flow);
                            // System.out.println(v1.getName() + " "+ v2.getName()+ " " + edge_capacity);
                        }

                    }

                }
                
                i_path.previous();      // get back to previous element again so that we don't skip a node in the current s-t path
               
            }

        }
        
    }

    
    // TODO 
    static void updateResidualGraph(SimpleGraph G, SimpleGraph Gf,
            LinkedHashMap<Edge, Integer> flow, List<Vertex> path) {

        HashMap<String,Edge> edgeListOfG = G.getEdgeMap();
        ListIterator iPath = path.listIterator();
        
        while (iPath.hasNext()) {
            Vertex v1 = (Vertex) (iPath.next());
            if (iPath.hasNext()) {
                Vertex v2 = (Vertex) (iPath.next());

                Iterator iEdge = Gf.incidentEdges(v1);
                while (iEdge.hasNext()) {
                    Edge eGf = (Edge) (iEdge.next());
                    if (Gf.opposite(v1, eGf).equals(v2)) {
                        String edgeNameG = "";

                        if (eGf.getName().equals("fe")) {
                            edgeNameG = v1.getName() + "->" + v2.getName();
                        } else {
                            edgeNameG = v2.getName() + "->" + v1.getName();
                        }
                        
                        Edge eG = edgeListOfG.get(edgeNameG);
                        
                        int flowInEdge = flow.get(eG);
                        System.out.println("Current Flow:"+ flowInEdge);
                        
                        if(eGf.getData().equals(flowInEdge)) {
                            // This is the bottleneck edge
                            
                        }
                        

                    }

                }

            }
        }
    }
    
    /**
     *  Given a graph G, and flow, calculate the sum of flow going out of source
     * @param G graph
     * @param source source vertex
     * @param flow flow value along each edge
     * @return 
     */
    static int getMaxFlow(SimpleGraph G, Vertex source, 
            LinkedHashMap<Edge, Integer> flow) {
        Iterator incidentEdges = G.incidentEdges(source);
        int maxFlow = 0;
        
        while(incidentEdges.hasNext()) {
            Edge currEdge = (Edge) incidentEdges.next();
            maxFlow += flow.get(currEdge);
        }
        
        return maxFlow;
    }
}
