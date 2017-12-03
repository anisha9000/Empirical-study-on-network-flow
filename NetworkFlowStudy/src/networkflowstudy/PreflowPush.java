/* --------------------------------------------------------------------------------------------- ||
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

    LinkedHashMap<Edge, Integer>   flows;                                                       // a hashmap to store flow values
    LinkedHashMap<Vertex, Integer> heights;                                                     // a hashmap to store heights of vertices
    LinkedHashMap<Vertex, Integer> excess;                                                      // a hashmap to store excesses
    SimpleGraph       graph;                                                                    // represents the input graph
    SimpleGraph       residualGraph;                                                            // represents the residual graph
    Iterator          i;                                                                        // iterator for a graph
    
    
    /**
     * @name        Constructor
     * @param g     represents a SimpleGraph to find max flow for.
     */
    public PreflowPush(SimpleGraph g) {
        graph = g;                                                                              // save the graph
                                                                                                //
        flows    = new LinkedHashMap<Edge, Integer>();                                          // initialize all the containers
        heights  = new LinkedHashMap<Vertex, Integer>();                                        //
        excess   = new LinkedHashMap<Vertex, Integer>();                                        // what the hell is the diamond operator???
                                                                                                //
        i = graph.vertices();                                                                   // set i to graph vertices
        while (i.hasNext()) {                                                                   // loop through each
            Vertex v = (Vertex)i.next();                                                        // save a copy
            heights.put(v, 0);                                                                  // set all heights to zero
        }                                                                                       //
                                                                                                //
        Vertex s = graph.getVertex("s");                                                        // get the source node
        heights.put(s, graph.numVertices());                                                    // set its height to n
                                                                                                //
        i = graph.edges();                                                                      // set i to graph edges
        while (i.hasNext()) {                                                                   // loop through each
            Edge e = (Edge)i.next();                                                            // save a copy
            if (e.getFirstEndpoint().getName().equals("s"))                                     // if the first endpoint is s
                flows.put(e, ((Double)e.getData()).intValue());                                 // set flow to max capacity
            else                                                                                // if not,
                flows.put(e, 0);                                                                // set to zero
        }                                                                                       //
                                                                                                //
        residualGraph = utils.createResidualGraph(graph, flows);                                // compute the residual graph
        CreateExcessMap();                                                                      // update the excess map
    } /* end Constructor */
    
    
    /**
     * @name        GetMaxFlow
     * @return      the flow of the graph in a linked hash map of Edge to
     *              Integer.
     */
    public LinkedHashMap<Edge, Integer> GetMaxFlow() {
        boolean isPushSuccess = false;                                                          // tell loop to relabel if push fails
                                                                                                //
        while (!excess.isEmpty()) {                                                             // while excess map isn't empty
            int i = 0;                                                                          // declare here so UpdateExcessMap can use index value
            isPushSuccess = false;                                                              // reset this each iteration
            Vertex v = excess.entrySet().iterator().next().getKey();                            // get the first key
            ArrayList<Vertex> w = GetWVertexList(v);                                            // get a list of available nodes
                                                                                                //
            for (i = 0; i < w.size(); i++) {                                                    // iterate through each available node
                if (Push(v, w.get(i))) {                                                        // try to push to the first, if success
                    isPushSuccess = true;                                                       // set this true
                    break;                                                                      // and break, we only want to execute once
                }                                                                               //
            }                                                                                   //
                                                                                                //
            if (!isPushSuccess)                                                                 // if Push failed, call
                Relabel(v);                                                                     // relabel on node v
            else {                                                                              // if Push suceeded,
                UpdateExcessMap(v, w.get(i));                                                   // update excess map
                residualGraph = utils.createResidualGraph(graph, flows);                        // re-create residual graph
            }                                                                                   //
        }                                                                                       //
        return flows;                                                                           // return the flows
    } /* end GetMaxFlow function */
    
    
    /**
     * @name        Push
     * @param v     represents the vertex with excess flow.
     * @param w     the vertex we want to push flow to.
     * @return      true/false
     */
    private boolean Push(Vertex v, Vertex w) {
        Edge     vw_residual = GetResidualEdge(v, w);                                           // test for the residual edge (v, w)
        Edge     vw_edge     = GetEdge(v, w);                                                   // store the actual edge (v, w)
        int      excess_v    = GetExcess(v);                                                    // flow into v
                                                                                                //
        if (excess_v > 0) {                                                                     // if there is any excess, we can push
            if (heights.get(w) < heights.get(v)) {                                              // if the height of v is greater than w, we can push
                int delta = Math.min(excess_v, (int)vw_residual.getData());                     // save the delta
                int f = 0;                                                                      // f is the current flow
                if (vw_residual.getName() == "fe") {                                            // forward edge
                    f = flows.get(vw_edge);                                                     // get current flow from vw_edge
                    flows.put(vw_edge, f + delta);                                              // add the delta
                }                                                                               //
                if (vw_residual.getName() == "be") {                                            // backward edge
                    vw_edge = GetEdge(w, v);                                                    // reverse vw_edge to match what is in flows
                    f = flows.get(vw_edge);                                                     // get current flow from vw_edge
                    flows.put(vw_edge, f - delta);                                              // subtract the delta
                }                                                                               //
                return true;                                                                    // if all goes well, return true
            }                                                                                   //
        }                                                                                       //
        return false;                                                                           // else, return false to relabel
    } /* end Push function */
    
    
    /**
     * @name        Relabel
     * @param v     represents the vertex we need to re-label.
     */
    private void Relabel(Vertex v) {
        int      newHeight      = 0;                                                            // store the new height
        int      fintov         = GetExcess(v);                                                 // how much excess?
        boolean  isSteepness    = true;                                                         // indicate whether the steepness rule is satisfied
        Vertex   residualVertex = residualGraph.getVertex((String)v.getName());                 // locate respective vertex in residual graph
        Vertex   tempw;                                                                         //
        Iterator it = residualGraph.incidentEdges(residualVertex);                              // iterator to v's incident edges
                                                                                                // 
        if (fintov > 0) {                                                                       // if we have excess
            while (it.hasNext()) {                                                              // check each edge
                Edge edge = (Edge)it.next();                                                    // to make sure it is
                if (edge.getFirstEndpoint().getName().equals(v.getName())) {                    // at least as high as v
                    Vertex t = edge.getSecondEndpoint();                                        // temp vertex
                    tempw = graph.getVertex((String)t.getName());                               // get the graph vertex by name to match heights
                    if (heights.get(tempw) >= heights.get(v))                                   // this is the steepness rule
                        isSteepness = true;                                                     // if all check true,
                    else                                                                        //
                        isSteepness = false;                                                    // and none are false,
                }                                                                               //
                if (!isSteepness)                                                               //
                    return;                                                                     //
            }                                                                                   //
            newHeight = heights.get(v) + 1;                                                     // the new height is incremented
            heights.put(v, newHeight);                                                          // and stored in heights
        }                                                                                       //
    } /* end Relabel function */
    
    
    /**
     * @name    CreateExcessMap
     */
    private void CreateExcessMap() {
        int ef = 0;                                                                             // excess flow
        excess.clear();                                                                         // clear the container each time
        i = graph.vertices();                                                                   // set i to graph vertices
        while (i.hasNext()) {                                                                   // loop through each
            Vertex v = (Vertex)i.next();                                                        // save a copy
            ef = GetExcess(v);                                                                  // set the excess, if any
            if ((ef > 0) && !v.getName().equals("t"))                                           // as long as v isn't t,
                excess.put(v, ef);                                                              // set the excess in the map
        }                                                                                       //
    } /* end CreateExcessMap function */

    
    /**
     * @name    UpdateExcessMap
     */
    private void UpdateExcessMap(Vertex v, Vertex w) {
        int ef1 = 0, ef2 = 0;                                                                   // store "excess flow 1" and "2"
        ef1 = GetExcess(v);                                                                     // get excess of "v"
        ef2 = GetExcess(w);                                                                     // get excess of "w"
                                                                                                //
        if ((ef1 > 0) && !v.getName().equals("t"))                                              // if v has excess,
            excess.put(v, ef1);                                                                 // put it on the map
        else                                                                                    // otherwise remove
            excess.remove(v);                                                                   // if it wasn't there nothing happens
                                                                                                //
        if ((ef2 > 0) && !w.getName().equals("t"))                                              // if w has excess,
            excess.put(w, ef2);                                                                 // put it on the map
        else                                                                                    // otherwise remove
            excess.remove(w);                                                                   // if it wasn't there nothing happens
    } /* end FillExcessMap function */
    
    
    /**
     * @name        GetExcess
     * @param v     represents the vertex to read amount of excess flow from.
     * @return      integer amount of flow into vertex v from all edges
     */
    private int GetExcess(Vertex v) {
        int      f_into_v      = 0;                                                             // flow into v
        int      f_outa_v      = 0;                                                             // flow out of v
        Iterator incidentEdges = graph.incidentEdges(v);                                        // v's incident edges
                                                                                                //
        while (incidentEdges.hasNext()) {                                                       // iterate over edges incident to v
            Edge edge = (Edge)incidentEdges.next();                                             // save a copy
            if (edge.getFirstEndpoint().equals(v))                                              // count how much flow leaving v
                f_outa_v += flows.get(edge);                                                    //
            if (edge.getSecondEndpoint().equals(v))                                             // count how much flow entering v
                f_into_v += flows.get(edge);                                                    // 
        }                                                                                       //
                                                                                                //
        return f_into_v - f_outa_v;                                                             // excess = flow in - flow out
    } /* end GetExcess function */


    /**
     * @name    GetWVertexList
     * @return  ArrayList<Vertex> with available w nodes
     */
    private ArrayList<Vertex> GetWVertexList(Vertex v) {
        ArrayList<Vertex> w = new ArrayList<Vertex>();                                          // the vertex list 'w' to return
        Vertex rV = residualGraph.getVertex((String)v.getName());                               // v's corresponding vertex in the residual graph
        Iterator residualIterator = residualGraph.incidentEdges(rV);                            // iterator of rV's incident edges
                                                                                                //
        while (residualIterator.hasNext()) {                                                    // now iterate through the residual graph
            Edge edge = (Edge)residualIterator.next();                                          // to locate a backward edge to use.
            if (edge.getFirstEndpoint().getName().equals(v.getName())) {                        // if 'v' is the first endpoint,
                Vertex tempw = edge.getSecondEndpoint();                                        // save a copy of the second endpoint
                Vertex graphw = graph.getVertex((String)tempw.getName());                       // save a copy from the actual graph
                if (heights.get(graphw) <= heights.get(v))                                      // if the height of w is same or lower, add first
                    w.add(0, graphw);                                                           // to "w"
                else                                                                            // otherwise
                    w.add(graphw);                                                              // add it last
            }                                                                                   //
        }                                                                                       //
                                                                                                //
        return w;                                                                               // return w
    } /* end GetWVertex function */
    
    
    /**
     * @name        GetEdge
     * @param v     Endpoint one of the edge we're looking for
     * @param w     Endpoint two of the edge we're looking for
     * @return      Edge
     */
    private Edge GetEdge(Vertex v, Vertex w) {
        i = graph.edges();                                                                      // set i to graph edges
        while (i.hasNext()) {                                                                   // loop through each
            Edge e = (Edge)i.next();                                                            // save a copy
            if (e.getFirstEndpoint().getName().equals(v.getName())                              // if v is first endpoint
                    && e.getSecondEndpoint().getName().equals(w.getName()))                     // and w is second endpoint
                return e;                                                                       // return e
        }                                                                                       //
        return null;                                                                            // otherwise return null
    } /* end GetEdge function */
    
    
    /**
     * @name        GetResidualEdge
     * @param v     Endpoint one of the edge we're looking for
     * @param w     Endpoint two of the edge we're looking for
     * @return      Edge
     */
    private Edge GetResidualEdge(Vertex v, Vertex w) {
        i = residualGraph.edges();                                                              // get iterator to residual graph
        while (i.hasNext()) {                                                                   // iterate over all edges
            Edge e = (Edge)i.next();                                                            // save a copy of current edge
            if (e.getFirstEndpoint().getName().equals(v.getName())                              // if v is first endpoint
                    && e.getSecondEndpoint().getName().equals(w.getName()))                     // and w is second endpoints
                return e;                                                                       // return e
        }                                                                                       //
        return null;                                                                            // otherwise return null
    } /* end GetResidualEdge function */
} /* end PreflowPush class */

