import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

import java.util.*;

/**
 * Created by Paul on 2017-02-26.
 */

/**
 * Ingenious idea of caching , by @nastra
 */
public class SAP {
    private Digraph digraph;
    private Map<String, FastSAP> cache;


    public SAP(Digraph digraph){
        this.digraph = digraph;
        this.cache = new HashMap<>();
    }

    private FastSAP cachedResult(int v, int w) {
        String key = v + "_" + w;
        if (cache.containsKey(key)) {
            FastSAP p = cache.get(key);
            // we need to cache only for 2 consecutive calls, therefore we delete the result after the second call
            cache.remove(key);
            return p;
        }
        FastSAP p = new FastSAP(v, w);
        cache.put(key, p);
        return p;
    }

    private FastSAP cachedResult(Iterable<Integer> v, Iterable<Integer> w) {
        String key = v.toString() + "_" + w.toString();
        if (cache.containsKey(key)) {
            FastSAP p = cache.get(key);
            // we need to cache only for 2 consecutive calls, therefore we delete the result after the second call
            cache.remove(key);
            return p;
        }

        FastSAP p = new FastSAP(v, w);
        cache.put(key, p);
        return p;
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

    public int length(int v, int w){
        if (!validIndex(v) || !validIndex(w)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return cachedResult(v,w).distance;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w){
        if (!validIndex(v) || !validIndex(w)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return cachedResult(v,w).distance;
    }

    public int ancestor(int v, int w) {
        if (!validIndex(v) || !validIndex(w)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return cachedResult(v, w).ancestor;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (!validIndex(v) || !validIndex(w)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return cachedResult(v, w).ancestor;
    }


    private class FastSAP{
        private int distance;
        private int ancestor;

        public FastSAP(int v, int w){
            BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
            BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);
            calculate(bfsV, bfsW);
        }

        public FastSAP(Iterable<Integer> v, Iterable<Integer> w){
            BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
            BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);
            calculate(bfsV, bfsW);
        }

        private void calculate(BreadthFirstDirectedPaths bfsV, BreadthFirstDirectedPaths bfsW){
            //String concatenatedKey = v + "." + w;
            List<Integer> ancestors = new ArrayList<>();
            for (int i = 0; i < digraph.V(); i++) {
                if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                    ancestors.add(i);
                }
            }

            int shortestAncestor = -1;
            int minDistance = Integer.MAX_VALUE;
            for (int ancestor : ancestors) {
                int dist = bfsV.distTo(ancestor) + bfsW.distTo(ancestor);
                if (dist < minDistance) {
                    minDistance = dist;
                    shortestAncestor = ancestor;
                }
            }
            if (Integer.MAX_VALUE == minDistance) {
                distance = -1;
            } else {
                distance = minDistance;

            }
            ancestor = shortestAncestor;
        }

    }
    // do unit testing of this class
    public static void main(String[] args){

    }
}
