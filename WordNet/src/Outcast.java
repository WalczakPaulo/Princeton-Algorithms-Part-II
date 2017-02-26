/**
 * Created by Paul on 2017-02-26.
 */
public class Outcast {
    private final WordNet wordnet;

    public Outcast(WordNet wordnet){
        this.wordnet = wordnet;
    }         // constructor takes a WordNet object

    public String outcast(String[] nouns) {
        int maxDistance = 0;
        String outcast = "";
        for(String nounA: nouns) {
            int distance = 0;
            for (String nounB : nouns)
                if (nounA != nounB) {
                    distance += wordnet.distance(nounA, nounB);
                }
            if(distance > maxDistance){
                maxDistance = distance;
                outcast = nounA;
            }
        }
        return outcast;
    }  // given an array of WordNet nouns, return an outcast
    public static void main(String[] args){

    }  // see test client below
}
