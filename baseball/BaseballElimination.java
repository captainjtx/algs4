import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import java.lang.Math;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class BaseballElimination {
	private final int nT;
	private final HashMap<String, Integer> map;
	private final String[] key2string;
	private final int[] w;
	private final int[] l;
	private final int[] r; 
	private final int[][] g; 
	private FlowNetwork fnet;
	private FordFulkerson ff;
	private String trivialEliminate;

	public BaseballElimination(String filename) {                    // create a baseball division from given filename in format specified below
		In input = new In(filename);
		nT = input.readInt();
		key2string = new String[nT];

		w = new int[nT];
		l = new int[nT];
		r = new int[nT];
		g = new int[nT][nT];

		map = new HashMap<>();
		for (int i = 0; i < nT; ++i) {
			String name = input.readString();
			map.put(name, i);
			key2string[i] = name;
			w[i] = input.readInt();
			l[i] = input.readInt();
			r[i] = input.readInt();

			for (int j = 0; j < nT; ++j)
				g[i][j] = input.readInt();
		}
	}

	public int numberOfTeams() {                        // number of teams
		return nT;
	}
	public Iterable<String> teams() {                   // all teams
		return map.keySet();
	}
	public int wins(String team) {                      // number of wins for given team
		if (!map.containsKey(team)) throw new java.lang.IllegalArgumentException();
		return w[map.get(team)];
	}
	public int losses(String team) {                    // number of losses for given team
		if (!map.containsKey(team)) throw new java.lang.IllegalArgumentException();
		return l[map.get(team)];
	}
	public int remaining(String team) {                 // number of remaining games for given team
		if (!map.containsKey(team)) throw new java.lang.IllegalArgumentException();
		return r[map.get(team)];
	}
	public int against(String team1, String team2) {    // number of remaining games between team1 and team2
		if (!map.containsKey(team1) || !map.containsKey(team2)) throw new java.lang.IllegalArgumentException();
		return g[map.get(team1)][map.get(team2)];
	}
	public boolean isEliminated(String team) {          // is given team eliminated?
		if (!map.containsKey(team)) throw new java.lang.IllegalArgumentException();
		int x = map.get(team);
		ff = null;

		for (int i = 0; i < nT; ++i) {
			if (i != x && w[x]+r[x] < w[i]) { // trivially eliminated
				trivialEliminate = key2string[i];
				return true;
			}
		}
		int pairs = 0;
		for (int i = 0; i < nT; ++i) {
			for (int j = 0; j < nT; ++j) {
				if (j > i && g[i][j] > 0 && i != x && j != x)
					pairs++;
			}
		}
		fnet = new FlowNetwork(nT+1+pairs);
		int pairIndex = nT+1;
		int maxFlow = 0;
		for (int i = 0; i < nT; ++i) {
			if (i != x) {
				for (int j = 0; j < nT; ++j) {
					if (j > i && g[i][j] > 0 && j != x) {
						fnet.addEdge(new FlowEdge(0, pairIndex, g[i][j]));
						fnet.addEdge(new FlowEdge(pairIndex, i+1, Double.POSITIVE_INFINITY));
						fnet.addEdge(new FlowEdge(pairIndex, j+1, Double.POSITIVE_INFINITY));
						pairIndex++;
						maxFlow += g[i][j];
					}
				}
				fnet.addEdge(new FlowEdge(i+1, x+1, w[x]+r[x]-w[i]));
			}
		}
		ff = new FordFulkerson(fnet, 0, x+1);
		return ff.value() < maxFlow;
	}
	public Iterable<String> certificateOfElimination(String team) { // subset R of teams that eliminates given team; null if not eliminated
		if (!isEliminated(team))
			return null;

		Queue<String> q = new LinkedList<>();
		if (ff == null) { // trivially eliminated
			q.add(trivialEliminate);
			return q;
		}

		for (int i = 0; i < nT; ++i) {
			if (ff.inCut(i+1))
				q.add(key2string[i]);
		}
		return q;
	}
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
