import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Digraph;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;

public class SAP {
	private final Digraph dg;
	private int latestAncestor;
	private int latestPathLength;
	private int latestV;
	private int latestW;
	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G) {
		dg = new Digraph(G);
		latestAncestor = -1;
		latestPathLength = -1;
		latestV = -1;
		latestW = -1;
	}
	private boolean isValidVertice(int v) {
		return v >= 0 && v < dg.V();
	}
	private boolean isValidVertices(Iterable<Integer> v) {
		if (v == null)
			return false;
		for (int vi : v )
			if (!isValidVertice(vi))
				return false;
		return true;
	}
	private void bfs(int v, int w) {
		if ((latestV == v && latestW == w) || (latestW == v && latestV == w))// cache the only latest bfs
			return;
		latestV = v;
		latestW = w;

		int numV = dg.V();
		int[] pathV = new int[numV];
		int[] pathW = new int[numV];
		Arrays.fill(pathV, -1);
		Arrays.fill(pathW, -1);
		pathV[v] = 0;
		pathW[w] = 0;

		Queue<Integer> qV = new LinkedList<>();
		Queue<Integer> qW = new LinkedList<>();

		qV.add(v);
		qW.add(w);

		int lenV = 0;
		int lenW = 0;

		int countV = 1;
		int countW = 1;

		int minPathLength = -1;
		int minAncestor = -1;
		while (!qV.isEmpty() || !qW.isEmpty()) {
			int newCountV = 0;
			int newCountW = 0;

			//step-locked bfs
			while (--countV >= 0) {
				int pV = qV.remove();
				//StdOut.println("visit V :"+pV);
				if (pathW[pV] >= 0) {
					if (minPathLength == -1 || (lenV+pathW[pV]) <= minPathLength) {
						minPathLength = lenV+pathW[pV];
						minAncestor = pV;
						//StdOut.println("LenV: "+lenV+" W to "+pV+" path: "+pathW[pV]+" minAncestor: "+minAncestor);
					}
				}
				for (int cV : dg.adj(pV)) {
					if (pathV[cV] < 0) {
						//StdOut.println("Enqueue V :"+cV);
						pathV[cV] = lenV+1;
						qV.add(cV);
						newCountV++;
					}
				}
			}
			lenV++;
			if (minPathLength >= 0 && lenV >= minPathLength) { // early termination
				qV.clear();
				newCountV = 0;
			}
			countV = newCountV;

			while (--countW >= 0) {
				int pW = qW.remove();
				//StdOut.println("visit W :"+pW);
				if (pathV[pW] >= 0) {
					if (minPathLength == -1 || (lenW+pathV[pW]) <= minPathLength) {
						minPathLength = lenW+pathV[pW];
						minAncestor = pW;
						//StdOut.println("LenW: "+lenW+" V to "+pW+" path: "+pathV[pW]+" minAncestor: "+minAncestor);
					}
				}
				for (int cW : dg.adj(pW)) {
					if (pathW[cW] < 0) {
						//StdOut.println("Enqueue W :"+cW);
						pathW[cW] = lenW+1;
						qW.add(cW);
						newCountW++;
					}
				}
			}
			lenW++;
			if (minPathLength >= 0 && lenW >= minPathLength) { // early termination
				qW.clear();
				newCountW = 0;
			}
			countW = newCountW;
		}
		if (minPathLength >= 0) {
			latestPathLength = minPathLength;
			latestAncestor = minAncestor; 
		}
		else {
			latestAncestor = -1;
			latestPathLength = -1;
		}
	}
	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) {
		if (!isValidVertice(v) || !isValidVertice(w))
			throw new java.lang.IllegalArgumentException();
		bfs(v, w);
		return latestPathLength;
	}

	// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	public int ancestor(int v, int w) {
		if (!isValidVertice(v) || !isValidVertice(w))
			throw new java.lang.IllegalArgumentException();
		bfs(v, w);
		return latestAncestor;
	}

	// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		int minPath = -1;
		if (!isValidVertices(v) || !isValidVertices(w))
			throw new java.lang.IllegalArgumentException();

		for (int vi : v) {
			for (int wi : w) {
				int len = length(vi, wi);
				if (minPath == -1 || len < minPath)
					minPath = len;
			}
		}
		return minPath;
	}

	// a common ancestor that that participates in shortest ancestral path; -1 if no such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		int minPath = -1;
		int minAncestor = -1;

		if (!isValidVertices(v) || !isValidVertices(w))
			throw new java.lang.IllegalArgumentException();

		for (int vi : v) {
			for (int wi : w) {
				int len = length(vi, wi);
				if (minPath == -1 || len < minPath) {
					minPath = len;
					minAncestor = ancestor(vi, wi);
				}
			}
		}
		return minAncestor;
	}

	// do unit testing of this class
	public static void main(String[] args) {
		In in = new In(args[0]);
		Digraph G = new Digraph(in);
		SAP sap = new SAP(G);
		while (!StdIn.isEmpty()) {
			int v = StdIn.readInt();
			int w = StdIn.readInt();
			int length   = sap.length(v, w);
			int ancestor = sap.ancestor(v, w);
			StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
		}
	}
}
