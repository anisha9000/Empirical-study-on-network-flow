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
    ArrayList<Vertex> vertices;                                                                 // a vertex vector to work with
    ArrayList<Edge>   edges;                                                                    // an edge vector to work with
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
        vertices = new ArrayList<Vertex>();                                                     // java = :*(
        edges    = new ArrayList<Edge>();                                                       // java < c++
                                                                                                //
        for (i = graph.vertices(); i.hasNext(); )                                               // extract all vertices
            vertices.add((Vertex)i.next());                                                     //
        for (i = graph.edges(); i.hasNext(); )                                                  // extract all edges
            edges.add((Edge)i.next());                                                          //
        for (int i = 0; i < vertices.size(); i++)                                               // initialize all nodes height to zero
            heights.put(vertices.get(i), 0);                                                    //
        Vertex s = graph.getVertex("s");                                                        // get the source node
        heights.put(s, vertices.size());                                                        // set its height to n
        for (int k = 0; k < edges.size(); k++) {                                                // initialize flow of all edges from s to some node v, to the capacity
            if (edges.get(k).getFirstEndpoint().getName().equals("s"))                          // edges leaving source are maxed
                flows.put(edges.get(k), ((Double)edges.get(k).getData()).intValue());           //
            else                                                                                //
                flows.put(edges.get(k), 0);                                                     // everything else is zero
        }                                                                                       //
        residualGraph = utils.createResidualGraph(graph, flows);                                // compute the residual graph
        UpdateExcessMap();                                                                      // update the excess map
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
            isPushSuccess = false;                                                              // reset this each iteration
            Vertex v = excess.keySet().iterator().next();                                       // get first element
            ArrayList<Vertex> w = GetWVertexList(v);                                            // get a list of available nodes
                                                                                                //
            for (int i = 0; i < w.size(); i++) {                                                // iterate through each available node
                if (Push(v, w.get(i))) {                                                        // try to push to the first, if success
                    isPushSuccess = true;                                                       // set this true
                    break;                                                                      // and break, we only want to execute once
                }                                                                               //
            }                                                                                   //
                                                                                                //
            if (!isPushSuccess)                                                                 // if Push failed, call
                Relabel(v);                                                                     // relabel on node v
                                                                                                //
            residualGraph = utils.createResidualGraph(graph, flows);                            // re-create residual graph
            UpdateExcessMap();                                                                  // update excess map
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
     * @name    UpdateExcessMap
     */
    private void UpdateExcessMap() {
        int ef = 0;                                                                             // excess flow
        excess.clear();                                                                         // clear the container each time
        for (int i = 0; i < vertices.size(); i++) {                                             // iterate through all vertices
            ef = GetExcess(vertices.get(i));                                                    // calculate the excess
            if ((ef > 0) && !vertices.get(i).getName().equals("t"))                             // and if this isn't "t"
                excess.put(vertices.get(i), ef);                                                // add it to the excess map
        }                                                                                       //
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
        for (int i = 0; i < edges.size(); i++) {                                                // iterate over all edges
            Edge edge = edges.get(i);                                                           // save a copy
            if (edge.getFirstEndpoint().getName().equals(v.getName())                           // compare actual names to determine both endpoints
                    && edge.getSecondEndpoint().getName().equals(w.getName()))                  // are the same as v and w
                return edges.get(i);                                                            // return the edge when it is found
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
        Edge vw_residual;                                                                       // save the residual graph edge
        i = residualGraph.edges();                                                              // get iterator to residual graph
        while (i.hasNext()) {                                                                   // iterate over all edges
            vw_residual = (Edge)i.next();                                                       // save a copy of current edge
            String e1 = (String)vw_residual.getFirstEndpoint().getName();                       // save endpoint 1 name
            String e2 = (String)vw_residual.getSecondEndpoint().getName();                      // save endpoint 2 name
            if (e1.equals(v.getName()) && e2.equals(w.getName()))                               // test for first and second endpoints
                return vw_residual;                                                             // return the edge when found
        }                                                                                       //
        return null;                                                                            // otherwise return null
    } /* end GetResidualEdge function */
} /* end PreflowPush class */

