import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.LinkedList;
public class Solver {
	private class SearchNode implements Comparable<SearchNode> {
		public SearchNode prev = null;
		public int steps = 0;
		public Board bd = null;
		public int compareTo(SearchNode that) {
			return bd.manhattan()+steps-that.bd.manhattan()-that.steps;
		}
	}
	private final boolean solvable;
	private SearchNode pqMin;
	private SearchNode pqTwinMin;
	public Solver(Board initial) {
		if (initial == null)
			throw new java.lang.IllegalArgumentException();
		SearchNode origin = new SearchNode();
		origin.bd = initial;
		
		SearchNode twin = new SearchNode();
		twin.bd = initial.twin();

		MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
		MinPQ<SearchNode> pqTwin = new MinPQ<SearchNode>();

		pq.insert(origin);
		pqTwin.insert(twin);

		while (true) {
			pqMin = pq.delMin();
			pqTwinMin = pqTwin.delMin();

			if (pqMin.bd.isGoal()) {
				solvable = true;
				break;
			}
			else if (pqTwinMin.bd.isGoal()) {
				solvable = false;
				break;
			}
			for (Board it : pqMin.bd.neighbors()) {
				if (pqMin.prev == null || !it.equals(pqMin.prev.bd)) {
					SearchNode tmp = new SearchNode();
					tmp.bd = it;
					tmp.prev = pqMin;
					tmp.steps = pqMin.steps+1;
					pq.insert(tmp);
				}

			}
			for (Board it : pqTwinMin.bd.neighbors()) {
				if (pqTwinMin.prev == null || !it.equals(pqTwinMin.prev.bd)) {
					SearchNode tmp = new SearchNode();
					tmp.bd = it;
					tmp.prev = pqTwinMin;
					tmp.steps = pqTwinMin.steps+1;
					pqTwin.insert(tmp);
				}
			}
		}
	}
	public boolean isSolvable() {
		return solvable;
	}
	public int moves() {
		if (solvable)
			return pqMin.steps;
		else
			return -1;
	}
	public Iterable<Board> solution() {
		if (solvable) {
			LinkedList<Board> sol = new LinkedList<Board>();
			SearchNode it = pqMin;
			while (it != null) {
				sol.addFirst(it.bd);
				it = it.prev;
			}
			return sol;
		}
		else
			return null;

	}
	public static void main(String[] args) {
		// create initial board from file
		In in = new In(args[0]);
		int n = in.readInt();
		int[][] blocks = new int[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				blocks[i][j] = in.readInt();
		Board initial = new Board(blocks);

		// solve the puzzle
		Solver solver = new Solver(initial);

		// print solution to standard output
		if (!solver.isSolvable())
			StdOut.println("No solution possible");
		else {
			StdOut.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution())
				StdOut.println(board);
		}
	}
}
