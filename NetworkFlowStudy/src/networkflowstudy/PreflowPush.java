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
    LinkedHashMap<Vertex, Integer> excess;                                      // a hashmap to store excesses
    SimpleGraph       graph;                                                    // represents the input graph
    SimpleGraph       residualGraph;                                            // represents the residual graph
    ArrayList<Vertex> vertices;                                                 // a vertex vector to work with
    ArrayList<Edge>   edges;                                                    // an edge vector to work with
    Iterator          i;                                                        // iterator for a graph
    
    
    /**
     * 
     * @param g     represents a SimpleGraph to find max flow for.
     */
    public PreflowPush(SimpleGraph g) {
        graph = g;                                                              // save the graph
        
        flows    = new LinkedHashMap<Edge, Integer>();
        heights  = new LinkedHashMap<Vertex, Integer>();
        excess   = new LinkedHashMap<Vertex, Integer>();
        vertices = new ArrayList<Vertex>();
        edges    = new ArrayList<Edge>();
        
        for (i = graph.vertices(); i.hasNext(); )                               // extract all vertices
            vertices.add((Vertex)i.next());                                     //
        for (i = graph.edges(); i.hasNext(); )                                  // extract all edges
            edges.add((Edge)i.next());                                          //
        for (int i = 0; i < vertices.size(); i++)                               // initialize all nodes height to zero
            heights.put(vertices.get(i), 0);                                    //
        for (int j = 0; j < vertices.size(); j++) {                             // initialize start node height to n
            if (vertices.get(j).getName() == "s") {                             //
                heights.put(vertices.get(j), vertices.size());                  //
                break;
            }
        }                                                                       //
        for (int k = 0; k < edges.size(); k++) {                                // initialize flow of all edges from s to some node v, to the capacity
            if (edges.get(k).getFirstEndpoint().getName() == "s")               // edges leaving source are maxed
                flows.put(edges.get(k), (int)edges.get(k).getData());           //
            else                                                                //
                flows.put(edges.get(k), 0);                                     // everything else is zero
        }                                                                       //
        residualGraph = utils.createResidualGraph(graph, flows);                // compute the residual graph
        UpdateExcessMap();                                                      // update the excess map
    } /* end Constructor */
    
    
    /**
     * 
     * @return 
     */
    public LinkedHashMap<Edge, Integer> GetMaxFlow() {
        while (!excess.isEmpty()) {                                             // while excess map isn't empty
            Vertex v = excess.keySet().iterator().next();                       // get first element
            Vertex w = GetWVertex(v);                                           // check for a w vertex
            if (w != null) {                                                    // if not null,
                if (!Push(v, w))                                                // we can push to w
                    Relabel(v);                                                 // relabel v
            }
            residualGraph = utils.createResidualGraph(graph, flows);            // update residual graph
            UpdateExcessMap();                                                  // update excess map
        }                                                                       //
        return flows;
    } /* end GetMaxFlow function */
    
    
    /**
     * @name  Push
     * @param v     represents the vertex with excess flow.
     * @param w     the vertex we want to push flow to.
     */
    private boolean Push(Vertex v, Vertex w) {
        Edge     vw_residual = new Edge(v, w, "", "");                          // test for the residual edge (v, w)
        Edge     vw_edge     = GetEdge(v, w);                                   // store the actual edge (v, w)
        int      excess_v    = GetExcess(v);                                    // flow into v
        boolean  hasEdgeVW   = false;                                           // indicate whether residual graph has (v, w)
                                                                                //
        i = residualGraph.edges();                                              // get iterator to residual graph
        while (i.hasNext()) {                                                   // iterate over all edges
            vw_residual = (Edge)i.next();                                       // save a copy of current edge
            String e1 = (String)vw_residual.getFirstEndpoint().getName();       // save endpoint 1 name
            String e2 = (String)vw_residual.getSecondEndpoint().getName();      // save endpoint 2 name
            if (e1.equals(v.getName()) && e2.equals(w.getName())) {             // test for first and second endpoints
                hasEdgeVW = true;                                               // set this to true
                break;                                                          // break out of this loop, we're done
            }                                                                   //
        }                                                                       //
                                                                                //
        if (excess_v > 0) {                                                     // if there is any excess, we can push
            if (heights.get(w) < heights.get(v)) {                              // if the height of v is greater than w, we can push
                if (hasEdgeVW) {                                                // if (v, w) exists in the residual graph, we can push
                    int delta = Math.min(excess_v, (int)vw_residual.getData()); // save the delta
                    int f     = flows.get(vw_edge);                             // get current flow
                    if (vw_residual.getName() == "fe")                          // forward edge
                        flows.put(vw_edge, f + delta);                          // add the delta
                    if (vw_residual.getName() == "be")                          // backward edge
                        flows.put(vw_edge, f - delta);                          // subtract the delta
                    return true;                                                // if all goes well, return true
                }                                                               //
            }                                                                   //
        }                                                                       //
        return false;                                                           // else, return false to relabel
    } /* end Push function */
    
    
    /**
     * 
     * @param v     represents the vertex we need to re-label.
     */
    private void Relabel(Vertex v) {
        int      newHeight     = 0;                                             // store the new height
        int      fintov        = GetExcess(v);                                  // how much excess?
        //boolean  isSteepness   = true;                                          // are the "downhill" nodes at least
        Iterator incidentEdges = residualGraph.incidentEdges(v);                // as high as v
                                                                                // iterator to v's incident edges
        if (fintov > 0) {                                                       // if we have excess
            while (incidentEdges.hasNext()) {                                   // check each edge
                Edge edge = (Edge)incidentEdges.next();                         // to make sure it is
                if (edge.getFirstEndpoint().getName().equals(v.getName())) {
                    if (heights.get(edge.getSecondEndpoint()) >= heights.get(v)) {// this is the steepness rule
                        newHeight = heights.get(v) + 1;                         // the new height is incremented
                        heights.put(v, newHeight);                              // by 1 and executed
                        break;
                    }
                }
            }                                                                   //
        }                                                                       //
    } /* end Relabel function */

    
    /**
     * 
     */
    private void UpdateExcessMap() {
        int ef = 0;                                                             // excess flow
        for (int i = 0; i < vertices.size(); i++) {                             // iterate through all vertices
            ef = GetExcess(vertices.get(i));
            if ((ef > 0) && vertices.get(i).getName() != "t")                   // and this isn't "t"
                excess.put(vertices.get(i), ef);                                // add to the excess map
        }                                                                       //
    } /* end FillExcessMap function */
    
    
    /**
     * 
     * @param v     represents the vertex to read amount of excess flow from.
     * @return      integer amount of flow into vertex v from all edges
     */
    private int GetExcess(Vertex v) {
        int      f_into_v      = 0;                                             // flow into v
        int      f_outa_v      = 0;                                             // flow out of v
        Iterator incidentEdges = graph.incidentEdges(v);                        // v's incident edges
                                                                                //
        while (incidentEdges.hasNext()) {                                       // iterate over edges incident to v
            Edge edge = (Edge)incidentEdges.next();                             // save a copy
            if (edge.getFirstEndpoint().equals(v))                              // count how much flow leaving v
                f_outa_v += flows.get(edge);                                    //
            if (edge.getSecondEndpoint().equals(v))                             // count how much flow entering v
                f_into_v += flows.get(edge);                                    // 
        }                                                                       //
                                                                                //
        return f_into_v - f_outa_v;                                             // excess = flow in - flow out
    } /* end GetExcess function */


    /**
     * 
     */
    private Vertex GetWVertex(Vertex v) {
        Iterator incidentEdges = residualGraph.incidentEdges(v);                // iterator of v's incident edges
        while (incidentEdges.hasNext()) {                                       // cycle through them all
            Edge edge = (Edge)incidentEdges.next();                             // save a copy
            if (edge.getFirstEndpoint().equals(v))                              // to check for v being the first
                return edge.getSecondEndpoint();                                // node, then return the second
        }                                                                       //
        return null;                                                            // else return null
    }                                                                           // for no w node
    
    
    /**
     * 
     * @param v
     * @param w
     * @return 
     */
    private Edge GetEdge(Vertex v, Vertex w) {
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).getFirstEndpoint().getName().equals(v.getName()) 
                    && edges.get(i).getSecondEndpoint().getName().equals(w.getName()))
                return edges.get(i);
        }
        return null;
    }
} /* end PreflowPush class */

