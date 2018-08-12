import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

public class WordNet {
	private final Digraph graph;
	private final Map<Integer, Set<String>> synsetMap;
	private final Map<String, Set<Integer>> nounMap;
	private final SAP sap;

	// constructor takes the name of the two input files
	public WordNet(String synset, String hypernyms) {
		if (synset == null || hypernyms == null)
			throw new java.lang.IllegalArgumentException();

		String line = "";
		int numV = 0;
		synsetMap = new HashMap<>();
		nounMap = new HashMap<>();

		In in = new In(synset);
		while ((line = in.readLine()) != null) {
			String[] strs = line.split(","); 
			int key = Integer.parseInt(strs[0]);
			numV = key;
			if (!synsetMap.containsKey(key)) 
				synsetMap.put(key, new HashSet<String>());

			String[] nouns = strs[1].split(" ");
			Set<String> entry = synsetMap.get(key);
			for (String noun : nouns) {
				if (!nounMap.containsKey(noun))
					nounMap.put(noun, new HashSet<Integer>());

				Set<Integer> synsetID = nounMap.get(noun);
				synsetID.add(key);

				entry.add(noun);
			}
		}

		graph = new Digraph(numV+1);
		in = new In(hypernyms);
		while ((line = in.readLine()) != null) {
			String[] strs = line.split(",");
			int key = Integer.parseInt(strs[0]);
			for (int i = 1; i < strs.length; ++i) {
				int n = Integer.parseInt(strs[i]);
				graph.addEdge(key, n);
			} 
		}

		if (!isRootedDag())
			throw new java.lang.IllegalArgumentException();
		sap = new SAP(graph);
		
	}
	private boolean isRootedDagDfs(int root, boolean[] visited, boolean[] visiting) {
		visiting[root] = true;
		for (int child : graph.adj(root)) {
			if (visited[child])
				continue;

			if (visiting[child]) // found a cycle
				return false;

			if (!isRootedDagDfs(child, visited, visiting))
				return false;
		} 
		visited[root] = true;
		return true;
	}

	// RootedDag is a Dag with only one sink
	private boolean isRootedDag() {
		int numVertices = graph.V();
		boolean[] visited = new boolean[numVertices]; 
		boolean[] visiting = new boolean[numVertices]; 

		boolean foundRoot = false;
		for (int i = 0; i < numVertices; ++i) {
			if (graph.outdegree(i) == 0) {
				if (foundRoot)
					return false; // find more than one root
				foundRoot = true;
			}

			if (!visited[i] && !isRootedDagDfs(i, visited, visiting))
					return false; // find a cycle
		}
		return true;
	}
	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return nounMap.keySet();
	}
	
	// is the word a WordNet nounsn?
	public boolean isNoun(String word) {
		if (word == null)
			throw new java.lang.IllegalArgumentException();

		return nounMap.containsKey(word);
	}

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB))
			throw new java.lang.IllegalArgumentException();

		return sap.length(nounMap.get(nounA), nounMap.get(nounB));
	}

	// a synset (second field of synsets.txt) that is common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB)) 
			throw new java.lang.IllegalArgumentException();
		Iterator<String> iter = synsetMap.get(sap.ancestor(nounMap.get(nounA), nounMap.get(nounB))).iterator();
		String synsetStr ="";
	        synsetStr += iter.next();
		while (iter.hasNext()) {
			synsetStr += " ";
			synsetStr += iter.next();
		}
		return synsetStr;
	}

	// do unit testing of this class
	public static void main(String[] args) {
		WordNet wn = new WordNet("", "hypernyms15Tree.txt");
	}
}
