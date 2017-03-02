import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;

/**
 * Created by Paul on 2017-03-02.
 */
public class FFGraph {
    public FordFulkerson ff;
    public FlowNetwork network;
    public int source;
    public int t;

    public FFGraph(FordFulkerson ff, FlowNetwork network, int source, int t) {
        super();
        this.ff = ff;
        this.network = network;
        this.source = source;
        this.t = t;
    }
}

