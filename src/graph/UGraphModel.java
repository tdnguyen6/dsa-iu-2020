/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.PriorityQueue;

/**
 *
 * @author LTSACH
 */
public class UGraphModel<T> extends AbstractGraph<T> {

    public UGraphModel() {
        super();
    }

    @Override
    public void connect(T from, T to) throws VertexNotFoundException {
        connect(from, to, 0);
    }

    @Override
    public void connect(T from, T to, float weight) throws VertexNotFoundException {
        VertexNode<T> fromNode = getVertexNode(from);
        if (fromNode == null) {
            throw new VertexNotFoundException(from);
        }

        VertexNode<T> toNode = getVertexNode(to);
        if (toNode == null) {
            throw new VertexNotFoundException(to);
        }

        fromNode.connect(toNode, weight);
        toNode.connect(fromNode, weight);
    }

    @Override
    public void disconnect(T from, T to) throws VertexNotFoundException, EdgeNotFoundException {
        VertexNode<T> fromNode = getVertexNode(from);
        if (fromNode == null) {
            throw new VertexNotFoundException(from);
        }

        VertexNode<T> toNode = getVertexNode(to);
        if (toNode == null) {
            throw new VertexNotFoundException(to);
        }

        fromNode.removeTo(toNode);
        toNode.removeTo(fromNode);
    }

    @Override
    public void remove(T vertex) throws VertexNotFoundException {
        VertexNode<T> nodeF = getVertexNode(vertex);
        if (nodeF == null) {
            throw new VertexNotFoundException(vertex);
        }
        //disconnect all
        for (VertexNode<T> nodeT : this.nodeList) {
            Edge<T> edge = nodeF.getEdge(nodeT);
            if (edge != null) {
                nodeF.removeTo(nodeT);
                nodeT.removeTo(nodeF);
            }
        }
        //remove
        this.nodeList.remove(nodeF);
    }
}

class UGraphAlgorithm<T> {

    UGraphModel<T> graph;

    public UGraphAlgorithm(UGraphModel<T> graph) {
        this.graph = graph;
    }

    UGraphModel<T> minSpanningTree() {
        //(1) Obtain the list ot vertices being processed
        List<T> vertexList = new LinkedList<>();
        Iterator<T> vertexIt = this.graph.iterator();
        while (vertexIt.hasNext()) {
            T vertex = vertexIt.next();
            vertexList.add(vertex);
        }
        //(2) Process each vertex in vertexList
        UGraphModel<T> mst = new UGraphModel<>();
        while (!vertexList.isEmpty()) {
            T vertex = vertexList.remove(0);

            mst.add(vertex);
            boolean hasChildren = true;
            do {
                PriorityQueue<Edge<T>> crossEdges = new PriorityQueue<>(100, new EdgeComparator());
                Iterator<T> mstIt = mst.iterator();
                while (mstIt.hasNext()) {
                    T parent = mstIt.next();
                    List<T> children = this.graph.getOutwardEdges(parent);

                    Iterator<T> childrenIt = children.iterator();
                    while (childrenIt.hasNext()) {
                        T child = childrenIt.next();
                        if (!mst.contains(child)) {
                            float weight = graph.weight(parent, child);
                            Edge<T> edge = new Edge<>(parent, child, weight);
                            crossEdges.add(edge);
                        }
                    }
                }
                hasChildren = crossEdges.size() > 0;
                if (hasChildren) {
                    Edge<T> smallest = crossEdges.poll();
                    mst.add(smallest.to);
                    mst.connect(smallest.from, smallest.to, smallest.weight);
                    vertexList.remove(smallest.to); //remove from verticeList
                }
            } while (hasChildren);
        }
        return mst;
    }

    //
    class Edge<T> {

        T from;
        T to;
        float weight;

        Edge(T from, T to, float weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    class EdgeComparator implements Comparator<Edge<T>> {

        @Override
        public int compare(Edge<T> o1, Edge<T> o2) {
            float diff = o1.weight - o2.weight;
            if (diff < 0) {
                return -1;
            } else if (diff > 0) {
                return +1;
            } else {
                return 0;
            }
        }

    }
}
