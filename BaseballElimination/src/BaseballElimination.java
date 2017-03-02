import edu.princeton.cs.algs4.*;

import java.util.*;

/**
 * Created by Paul on 2017-03-02.
 */

/**
 * I will use ready Ford Dulkerson implementations provided by Princeton.
 */
public class BaseballElimination {

    //first well need a hashset to store name-id info about teams
    private Map<String,Integer> nameToId;
    //and arrays for info about matches
    private int teamsNumber;
    private int maxWinsNumber;
    private String leadingTeam;
    private int winsNumber[];
    private int lossesNumber[];
    private int remainingNumber[];
    private int gamesBeetwen[][];


    public BaseballElimination(String filename){
        In input = new In(filename);
        int teamsNumber = input.readInt();
        maxWinsNumber = -1;
        leadingTeam = "";
        teamsNumber = 0;
        winsNumber = new int[teamsNumber];
        lossesNumber = new int[teamsNumber];
        remainingNumber = new int[teamsNumber];
        gamesBeetwen = new int[teamsNumber][teamsNumber];
        nameToId = new HashMap<String, Integer>();

        for (int id = 0; id < teamsNumber; id++) {

            String teamName = input.readString();
            nameToId.put(teamName, id);
            winsNumber[id] = input.readInt();
            lossesNumber[id] = input.readInt();
            remainingNumber[id] = input.readInt();

            if (winsNumber[id] > maxWinsNumber) {
                maxWinsNumber = winsNumber[id];
                leadingTeam = teamName;
            }

            for (int other = 0; other < teamsNumber; other++) {
                gamesBeetwen[id][other] = input.readInt();
            }
        }
    }                    // create a baseball division from given filename in format specified below

    public int numberOfTeams() {
        return teamsNumber;
    }                       // number of teams

    public Iterable<String> teams() {
        return nameToId.keySet();
    }                               // all teams

    public int wins(String team) {
        return winsNumber[nameToId.get(team)];
    }                     // number of wins for given team

    public int losses(String team) {
        return lossesNumber[nameToId.get(team)];
    }                    // number of losses for given team

    public int remaining(String team) {
        return remainingNumber[nameToId.get(team)];
    }                // number of remaining games for given team

    public int against(String team1, String team2) {
        return gamesBeetwen[nameToId.get(team1)][nameToId.get(team2)];
    }   // number of remaining games between team1 and team2

    public boolean teamExists(String name) {
        if(nameToId.containsKey(name))
            return true;
        else
            return false;
    }

    private boolean trivialElimination(String team) {
        int teamId = nameToId.get(team);
        int potentialMaxWins = winsNumber[teamId] + remainingNumber[teamId];
        if(potentialMaxWins < maxWinsNumber)
            return true;
        else
            return false;
    }

    public boolean isEliminated(String team) {
        if(!teamExists(team))
            throw new IllegalArgumentException("Team does not exist");
        if(trivialElimination(team))
            return true;
        int teamId = nameToId.get(team);
        FFGraph ffGraph = constructGraph(teamId);
        for(FlowEdge sEdge: ffGraph.network.adj(ffGraph.source)){
            if(sEdge.flow() < sEdge.capacity())
                return true;
        }
        return false;
    }             // is given team eliminated?

    private FFGraph constructGraph(int teamId) {
        int n = numberOfTeams();
        int source = n;
        int t = n + 1;
        int gameNode = n + 2;
        int currentMaxWins = winsNumber[teamId] + remainingNumber[teamId];
        List<FlowEdge> edges = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (i == teamId)
                continue;
            // no iteration through whole set to avoid duplicates, thats why condition j < i
            for (int j = 0; j < i; j++) {
                if (j == teamId || gamesBeetwen[i][j] == 0 || winsNumber[j] + remainingNumber[j] < maxWinsNumber)
                    continue;

                edges.add(new FlowEdge(source, gameNode, gamesBeetwen[i][j]));
                edges.add(new FlowEdge(gameNode, i, Double.POSITIVE_INFINITY));
                edges.add(new FlowEdge(gameNode, j, Double.POSITIVE_INFINITY));
                gameNode++;
            }
            edges.add(new FlowEdge(i, t, currentMaxWins - winsNumber[i]));
        }

        FlowNetwork network = new FlowNetwork(gameNode);
        for (FlowEdge edge : edges) {
            network.addEdge(edge);
        }
        FordFulkerson ff = new FordFulkerson(network, source, t); // here magic happens
        return new FFGraph(ff, network, source, t);
    }

    public Iterable<String> certificateOfElimination(String team){
        if(!teamExists(team))
            throw new IllegalArgumentException();
        List <String> winningTeams = new LinkedList<>();
        if(trivialElimination(team)) {
            winningTeams.add(leadingTeam);
            return winningTeams;
        }

        FFGraph ffGraph = constructGraph(nameToId.get(team));

        for(FlowEdge sEdge: ffGraph.network.adj(ffGraph.source)) {
            if (sEdge.flow() < sEdge.capacity()){
                for(String teamInCut: teams()){
                    int tempId = nameToId.get(teamInCut);
                    if(ffGraph.ff.inCut(tempId))
                        winningTeams.add(team);
                }
                break;
            }
        }

        if(winningTeams.size() == 0)
            return null;
        return winningTeams;
    }  // subset R of teams that eliminates given team; null if not eliminated

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

}
