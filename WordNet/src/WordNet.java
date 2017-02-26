import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.*;

/**
 * Created by Paul on 2017-02-26.
 */
public class WordNet {

    /* these will not be modified througout work */
    private final Map<Integer,String> idToSynset = new HashMap<>();
    private final Map<String, Set<Integer>> nounToIds = new HashMap<>();
    //private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        fillSynsets(synsets);
        Digraph digraph = makeGraph(hypernyms);
        if(!isRightGraph(digraph))
            throw new IllegalArgumentException();
        //sap = new SAP(digraph);
    }

    public boolean isRightGraph(Digraph digraph){
        DirectedCycle directedCycle = new DirectedCycle(digraph);
        if(directedCycle.hasCycle() || !isRootedDAG(digraph))
            return false;
        else
            return true;
    }

    public boolean isRootedDAG(Digraph digraph){
        int outDegree = 0;
        for(int i = 0; i < digraph.V(); i++)
            if (digraph.outdegree(i) != 0) {
                outDegree++;
                if (outDegree > 1)
                    return false;
            }
        return outDegree == 1;
    }


    public void fillSynsets(String synsets){
        In inputFile = new In(synsets);
        //iterate through all records
        while (inputFile.hasNextLine()) {
            String[] record = inputFile.readLine().split(",");
            Integer id = Integer.valueOf(record[0]);
            String words = record[1];
            idToSynset.put(id, words);
            String[] nouns = words.split(" ");
            for (String noun : nouns) {
                Set<Integer> ids = nounToIds.get(noun);
                if (ids == null)
                    ids = new HashSet<>();
                ids.add(id);
                nounToIds.put(noun, ids);
            }
        }
    }

    public Digraph makeGraph(String hypernyms){
        Digraph digraph = new Digraph(idToSynset.size());
        In inputFile = new In(hypernyms);
        while(inputFile.hasNextLine()) {
            String[] record = inputFile.readLine().split(",");
            Integer vertex = Integer.valueOf(record[0]);
            for(int i = 1; i < record.length; i++) {
                digraph.addEdge(vertex, Integer.valueOf(record[i]));
            }
        }
        return digraph;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns(){
        return nounToIds.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if(word == null || word.equals(""))
            return false;
        return nounToIds.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if(!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();
        Set<Integer> setOfIdsA = nounToIds.get(nounA);
        Set<Integer> setOfIdsB = nounToIds.get(nounB);
        //return sap.length(setOfIdsA,setOfIdsB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if(!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();
        Set<Integer> setOfIdsA = nounToIds.get(nounA);
        Set<Integer> setOfIdsB = nounToIds.get(nounB);
        //int commonAncestor =  sap.ancestor(setOfIdsA, setOfIdsB);
        //return idToSynset.get(commonAncestor);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        
    }
}