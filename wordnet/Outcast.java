import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
	private final WordNet wn;
	public Outcast(WordNet wordnet) {
		wn = wordnet;
	}
	public String outcast(String[] nouns) {
		String oc = ""; // outcast
		int maxDist = -1;

		for (String noun : nouns) {
			int dist = 0; 
			for (String other : nouns) {
				dist += wn.distance(noun, other);
			}
			if (dist > maxDist) {
				maxDist = dist;
				oc = noun;
			}
		}
		return oc;
	}
	public static void main(String[] args) {
		WordNet wordnet = new WordNet(args[0], args[1]);
		Outcast outcast = new Outcast(wordnet);
		for (int t = 2; t < args.length; t++) {
			In in = new In(args[t]);
			String[] nouns = in.readAllStrings();
			StdOut.println(args[t] + ": " + outcast.outcast(nouns));
		}
	}
}
