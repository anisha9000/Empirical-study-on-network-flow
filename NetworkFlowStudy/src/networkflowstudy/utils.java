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
     * @param G
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
     * @param G
     * @param flow
     * @return Residual Graph
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
            Integer edgeCapacity = (Integer) e.getData();
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
     * Updates s-t path of graph Gf, if there is a simple path and returns true,
     * return false otherwise
     *
     * @param Gf
     * @param path
     * @param source
     * @param sink
     * @return
     */
    public static boolean isSTPath(SimpleGraph Gf,
            Vertex source, Vertex sink) {

        int numOfVertices = Gf.numVertices();

        //To ensure there is no cycle
        boolean[] visited = new boolean[numOfVertices];

        LinkedList<Vertex> vertexList = new LinkedList<>();
        Iterator i = Gf.vertices();
        for (int j = 0; j < numOfVertices; j++) {
            vertexList.add((Vertex) i.next());
        }

        Stack<Vertex> vertexStack = new Stack<>();
        vertexStack.push(source);

        while (!vertexStack.empty()) {

            Vertex currVertex = vertexStack.pop();
            visited[vertexList.indexOf(currVertex)] = true;

            i = Gf.incidentEdges(currVertex);
            while (i.hasNext()) {

                Edge e = (Edge) i.next();
                Vertex secondVertex = e.getSecondEndpoint();
                int indexOfSecondVertex = vertexList.indexOf(secondVertex);

                if (!visited[indexOfSecondVertex]) {

                    // path[indexOfSecondVertex] = currVertex;
                    vertexStack.push(secondVertex);

                }

            }
        }

        int indexOfSink = vertexList.indexOf(sink);
        if (visited[indexOfSink]) {
            return true;
        } else {
            return false;
        }

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
        // TODO: return edges in path and not vertices. 
        Map<Vertex, Boolean> vis = new HashMap<>();
        List<Vertex> directions = new LinkedList<>();
        Queue<Vertex> q = new LinkedList<>();
        Map<Vertex, Vertex> prev = new HashMap<>();
        Vertex current = Source;
        q.add(current);
        vis.put(current, true);
        //System.out.println("Sink: "+ Sink.getName() );
        while (!q.isEmpty()) {
            current = q.poll();
            //System.out.println("Current "+current.getName());
            if (current.getName().equals(Sink.getName())) {
                break;
            } else {
                Iterator i1 = Gf.incidentEdges(current);
                while (i1.hasNext()) {
                    Edge e = (Edge) i1.next();
                    //System.out.println(e_G.getName()+" "+e_G.getFirstEndpoint().getName()+" "+e_G.getSecondEndpoint().getName());

                    //TODO check for forward and backward edges
                    if ((e.getFirstEndpoint().getName().equals(current.getName()))) {

                        Vertex node = e.getSecondEndpoint();
                        if (!vis.containsKey(node)) {
                            //System.out.println("node: "+node.getName()); 
                            q.add(node);
                            vis.put(node, true);
                            prev.put(node, current);
                        }
                    }

                }
            }
        }
        if (!current.getName().equals(Sink.getName())) {
            System.out.println("can't reach destination");
            return null;
        }
        for (Vertex node = Sink; node != null; node = prev.get(node)) {
            //System.out.println("dir: " + node.getName());
            directions.add(node);
        }

        Collections.reverse(directions);

        /*directions.forEach((v) -> {
            System.out.println("dir: " + v.getName());
        }); */
        return directions;

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
        System.out.println("Gf to consider in path:");
        //utils.printGraph(Gf);
        
        while (!queue.isEmpty()) {
            current = queue.poll();
            //System.out.println("Current "+current.getName());
            if (current.getName().equals(sink.getName())) {
                break;
            } else {
                Iterator i1 = Gf.incidentEdges(current);
                while (i1.hasNext()) {
                    Edge e = (Edge) i1.next();
                    //printEdge(e);
                    int edgeCapacity = (int) e.getData();
                    if ((e.getFirstEndpoint().getName().equals(current.getName()))
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
        Iterator i1 = G.edges();
        while (i1.hasNext()) {
            Edge e_G = (Edge) i1.next();

            edgeList_G.put(String.valueOf(e_G.getFirstEndpoint().getName() + "->" + e_G.getSecondEndpoint().getName()), e_G);
            /*System.out.println("edgelist_G" + e_G + " Data:" + e_G.getData() + "first and second node "
                    + e_G.getFirstEndpoint().getName() + " " + e_G.getSecondEndpoint().getName());*/
        }
        ListIterator i_path = path.listIterator();
        int b_neck = get_bottleneck(Gf, path);
        while (i_path.hasNext()) {
            Vertex v1 = (Vertex) (i_path.next());

            if (i_path.hasNext()) {
                Vertex v2 = (Vertex) (i_path.next());

                Iterator i_edge = Gf.incidentEdges(v1);
                while (i_edge.hasNext()) {
                    Edge e = (Edge) (i_edge.next());
                    if (Gf.opposite(v1, e).equals(v2)) {
                        Integer edge_capacity = (Integer) (e.getData());
                        // ***TODO Update residual capacity for residual capacity = 0: i_path.e_G. removing edge 
                        //e.setData(edge_capacity - b_neck);
                        if (e.getName() == "fe") {
                            /*
                            System.out.println("Edge" + e.getName() + " data now:" + e.getData());
                            System.out.println(e.getFirstEndpoint().getName() + "->" + e.getSecondEndpoint().getName());
                            System.out.println((Edge) edgeList_G.get(String.valueOf(e.getFirstEndpoint().getName() + "->" + e.getSecondEndpoint().getName())));
                            */

                            Integer current_flow = flow.get((Edge) edgeList_G.get(String.valueOf(e.getFirstEndpoint().getName() + "->" + e.getSecondEndpoint().getName())));
                            //System.out.println("current flow is : " + current_flow);
                            Integer updated_edge_flow = current_flow + b_neck;
                            flow.put((Edge) edgeList_G.get(String.valueOf(e.getFirstEndpoint().getName() + "->" + e.getSecondEndpoint().getName())), updated_edge_flow);
                            // System.out.println(v1.getName() + " "+ v2.getName()+ " " + edge_capacity);
                        }
                        if (e.getName() == "be") {
                            /*
                            System.out.println("Edge" + e.getName() + " data now:" + e.getData());
                            System.out.println(e.getFirstEndpoint().getName() + "->" + e.getSecondEndpoint().getName());
                            System.out.println((Edge) edgeList_G.get(String.valueOf(e.getFirstEndpoint().getName() + "->" + e.getSecondEndpoint().getName())));
                            */

                            Integer current_flow = flow.get((Edge) edgeList_G.get(String.valueOf(e.getFirstEndpoint().getName() + "->" + e.getSecondEndpoint().getName())));
                            //System.out.println("current flow is : " + current_flow);
                            Integer updated_edge_flow = current_flow - b_neck;
                            flow.put((Edge) edgeList_G.get(String.valueOf(e.getFirstEndpoint().getName() + "->" + e.getSecondEndpoint().getName())), updated_edge_flow);
                            // System.out.println(v1.getName() + " "+ v2.getName()+ " " + edge_capacity);
                        }

                    }

                }
                i_path.previous();      // get back to previous element again so that we don't skip a node in the current s-t path
            }

        }
        /*
            for(Map.Entry<Edge,Integer> value : flow.entrySet()) {
            System.out.println("Edge:"+value.getKey().getName()+" Flow:"+value.getValue());  
            
        } */
    }

    /**
     * Given a graph G, print all the nodes and their incident edges
     *
     * @param G
     */
    public static void printGraph(SimpleGraph G) {
        Iterator i;
        Vertex v;
        Edge e;

        System.out.println("Iterating through adjacency lists...");
        for (i = G.vertices(); i.hasNext();) {
            v = (Vertex) i.next();
            System.out.println("Vertex " + v.getName());
            Iterator j;

            for (j = G.incidentEdges(v); j.hasNext();) {
                e = (Edge) j.next();
                System.out.println("  found edge " + e.getName() + " from "+ 
                        e.getFirstEndpoint().getName()+" to "+ 
                        e.getSecondEndpoint().getName() + " of value "+ 
                        e.getData());
            }
        }
    }

    public static void printEdge(Edge e) {
        System.out.println("Edge " + e.getName() + " from "+ 
                e.getFirstEndpoint().getName()+" to "+ 
                e.getSecondEndpoint().getName() + " of value "+ e.getData());

    }
    
    public static void printFlow(LinkedHashMap<Edge, Integer> flow) {
        Iterator i;

        System.out.println("Iterating through flow");
        for (Entry<Edge, Integer> edge : flow.entrySet()) {
            System.out.println("edge:" + edge.getKey().getName() + ", value:" + edge.getValue());
        }

    }

    public static void printPath(List<Vertex> path) {
        System.out.println("Path:");
        if (path != null) {
            path.forEach((v11) -> {
                System.out.println("dir: " + (v11).getName());
            });
        }

    }


    /*   
    /**
     * Calculates the max flow using augment() , getSTPath() and createResidualGraph()   
     * @param G
     * @return maxFlow
     */
 /*    
public static int fordFulkerson(SimpleGraph G){
    
     //Initialize flow
     LinkedHashMap<Edge, Integer> flow = utils.initFlow(G);
     // Create residual graph Gf
     SimpleGraph Gf = createResidualGraph(G, flow);
     
     // get the Vertex object of the current soure and sink nodes
     Vertex source = get_s(Gf);
     Vertex sink = get_t(Gf);
     
     while(getSTPath(Gf, sink, source)!= null){
         
     }
}
     */
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
}
