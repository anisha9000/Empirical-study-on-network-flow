/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkflowstudy;

import graphCode.SimpleGraph;
import graphCode.Edge;
import graphCode.Vertex;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.ArrayList;

/**
 *
 * @author jason
 */
public class PreflowPush {

    LinkedHashMap<Edge, Integer>   flows;                                       // a hashmap to store flow values
    LinkedHashMap<Vertex, Integer> heights;                                     // a hashmap to store heights of vertices
    SimpleGraph       graph;                                                    // represents the input graph
    SimpleGraph       residualGraph;                                            // represents the residual graph
    ArrayList<Vertex> vertices;                                                 // a vertex vector to work with
    ArrayList<Edge>   edges;                                                    // an edge vector to work with
    Iterator          i;                                                        // iterator for a graph
    utils             utilities;
    
    
    /**
     * 
     * @param g     represents a SimpleGraph to find max flow for.
     */
    public PreflowPush(SimpleGraph g) {
        graph = g;
        for (i = graph.vertices(); i.hasNext(); )                               // extract all vertices
            vertices.add((Vertex)i.next());
        for (i = graph.edges(); i.hasNext(); )                                  // extract all edges
            edges.add((Edge)i.next());
        for (int i = 0; i < vertices.size(); i++)                               // initialize all nodes height to zero
            heights.put(vertices.get(i), 0);
        for (int j = 0; j < vertices.size(); j++) {                             // initialize start node height to n
            if (vertices.get(j).getName() == "s")
                heights.put(vertices.get(j), vertices.size());
        }
        for (int k = 0; k < edges.size(); k++) {                                // initialize flow of all edges from s to some node v, to the capacity
            String e1 = (String)edges.get(k).getFirstEndpoint().getName();
            String e2 = (String)edges.get(k).getSecondEndpoint().getName();
            if (e1.equals("s") || e2.equals("s"))
                flows.put(edges.get(k), (int)edges.get(k).getData());
        }
        residualGraph = utils.createResidualGraph(graph, flows);                // compute the residual graph
    } /* end Constructor */
    
    
    /**
     * 
     */
    public void GetMaxFlow() {
        
    } /* end GetMaxFlow function */
    
    
    /**
     * @name  Push
     * @param v     represents the vertex with excess flow.
     * @param w     the vertex we want to push flow to.
     */
    private void Push(Vertex v, Vertex w) {
        Edge     vw_edge       = new Edge(v, w, "", "");                        // test for the edge (v, w)
        int      fintov        = GetExcess(v);                                  // flow into v
        boolean  hasEdgeVW     = false;                                         // indicate whether residual graph has (v, w)
                                                                                //
        i = residualGraph.edges();                                              // get iterator to residual graph
        while (i.hasNext()) {                                                   // iterate over all edges
            vw_edge = (Edge)i.next();                                           // save a copy of current edge
            if (vw_edge.getFirstEndpoint().equals(v)                            // test for first and second endpoints
                    && vw_edge.getSecondEndpoint().equals(w)) {                 // if equal,
                hasEdgeVW = true;                                               // set this to true
                break;                                                          // break out of this loop, we're done
            }                                                                   //
        }                                                                       //
                                                                                //
        if (fintov > 0)                                                         // if there is any excess, we can push
        {                                                                       //
            if (heights.get(w) < heights.get(v))                                // if the height of v is greater than w,
            {                                                                   // we can push
                if (hasEdgeVW)                                                  // if (v, w) exists in the residual graph,
                {                                                               // we can push
                    if (vw_edge.getName() == "fe") {                            // forward edge
                        int delta = Math.min(fintov, (int)vw_edge.getData());   // save the delta
                        for (int i = 0; i < edges.size(); i++) {                // iterate over all edges
                            if (edges.get(i).equals(vw_edge)) {                 // search for our saved edge
                                int f = (int)edges.get(i).getData();            // save the current data
                                edges.get(i).setData(f + delta);                // increase it by delta
                                // update residual graph
                            }                                                   //
                        }                                                       //
                    }                                                           //
                    if (vw_edge.getName() == "be") {                            // backward edge
                        int delta = Math.min(fintov, (int)vw_edge.getData());   // save the delta
                        for (int j = 0; j < edges.size(); j++) {                // iterate over all edges
                            if (edges.get(j).equals(vw_edge)) {                 // search for our saved edge
                                int f = (int)edges.get(j).getData();            // save the current data
                                edges.get(j).setData(f - delta);                // decrease it by delta
                                // update residual graph
                            }                                                   //
                        }                                                       //
                    }                                                           //
                }                                                               //
            }                                                                   //
        }                                                                       //
    } /* end Push function */
    
    
    /**
     * 
     * @param v     represents the vertex we need to re-label.
     */
    private void Relabel(Vertex v) {
        int      newHeight     = 0;
        int      fintov        = GetExcess(v);
        boolean  isSteepness   = true;
        Iterator incidentEdges = residualGraph.incidentEdges(v);
        
        if (fintov > 0) {
            while (incidentEdges.hasNext()) {
                Edge edge = (Edge)incidentEdges.next();
                if (edge.getFirstEndpoint().equals(v)) {
                    if (heights.get(edge.getSecondEndpoint()) >= heights.get(v))
                        isSteepness = true;
                    else
                        isSteepness = false;
                }
                if (!isSteepness)
                    return;
            }
            newHeight = heights.get(v) + 1;
            heights.put(v, newHeight);
        }
    } /* end Relabel function */
    
    
    /**
     * 
     * @param v     represents the vertex to read amount of excess flow from.
     * @return      integer amount of flow into vertex v from all edges
     */
    private int GetExcess(Vertex v) {
        int      e_into_v      = 0;
        Iterator incidentEdges = graph.incidentEdges(v);
        
        while (incidentEdges.hasNext()) {                                       // iterate over edges incident to v
            Edge edge = (Edge)incidentEdges.next();                             // save a copy
            if (edge.getSecondEndpoint().equals(v))                             // if the second endpoint equals v
                e_into_v += flows.get(edge);                                    // this edge is going into v, save the flow
        }                                                                       //
        
        return e_into_v;
    } /* end GetExcess function */
} /* end PreflowPush class */

