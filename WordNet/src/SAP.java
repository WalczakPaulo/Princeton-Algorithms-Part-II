import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Paul on 2017-02-26.
 */
public class SAP {
    private Digraph digraph;

    public SAP(Digraph digraph){
        this.digraph = digraph;
    }

    private boolean validIndex(int index) {
        if(index >= 0 && index < digraph.V())
            return true;
        else
            return false;
    }

    private boolean validIndex(Iterable<Integer> indices){
        for(Integer index: indices)
            if(!validIndex(index))
                return false;
        return true;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w){
        if(!validIndex(v) || !validIndex(w))
            throw new IndexOutOfBoundsException();
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);
        int commonAncestor = ancestor(v,w);
        if(commonAncestor == - 1)
            return -1;
        else
            return bfsV.distTo(commonAncestor) + bfsW.distTo(commonAncestor);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w){
        if(!validIndex(v) || !validIndex(w))
            throw new IndexOutOfBoundsException();
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);

        //in case v and w are the same - checklist
        if(v == w)
            return v;
        int minDistance = Integer.MAX_VALUE;
        int commonShortestAncestor = -1;
        for(int i = 0; i < digraph.V(); i++)
            if(bfsV.hasPathTo(i) && bfsW.hasPathTo(w)) {
                int tempDistance = bfsV.distTo(i) + bfsW.distTo(i);
                if (tempDistance < minDistance) {
                    minDistance = tempDistance;
                    commonShortestAncestor = i;
                }

            }
        return commonShortestAncestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w){
        if(!validIndex(v) || !validIndex(w))
            throw new IndexOutOfBoundsException();
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);
        int commonAncestor = ancestor(v,w);
        if(commonAncestor == - 1)
            return -1;
        else
            return bfsV.distTo(commonAncestor) + bfsW.distTo(commonAncestor);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
        if(!validIndex(v) || !validIndex(w))
            throw new IndexOutOfBoundsException();
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);

        //in case v and w are the same - checklist

        int minDistance = Integer.MAX_VALUE;
        int commonShortestAncestor = -1;
        for(int i = 0; i < digraph.V(); i++)
            if(bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                int tempDistance = bfsV.distTo(i) + bfsW.distTo(i);
                if (tempDistance < minDistance) {
                    minDistance = tempDistance;
                    commonShortestAncestor = i;
                }

            }
        return commonShortestAncestor;
    }

    // do unit testing of this class
    public static void main(String[] args){

    }
}
