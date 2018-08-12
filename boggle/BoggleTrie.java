import java.util.HashSet;
import java.util.LinkedList;

public class BoggleTrie {
	private static final int R = 26; // capital A-Z
	private Node root; // root of trie
	private int n; // number of keys in trie

	// R-way trie node, no value
	private static class Node {
		private boolean isWord = false;
		private Node[] next  = new Node[R];
	}

	// initialize empty set
	public BoggleTrie() {}

	public BoggleTrie(String[] dictionary) {
		for (String w : dictionary) add(w);
	}

	public boolean contains(String key) {
		if (key == null) throw new IllegalArgumentException("argument to contains() is null");
		return contains(root, key, 0);
	}
	private boolean contains(Node x, String key, int d) {
		if (x == null) return false;
		if (d == key.length()) return x.isWord;
		char c = key.charAt(d);
		return contains(x.next[c-'A'], key, d+1);
	}

	public void add(String key) {
		if (key == null) throw new IllegalArgumentException("first argument to add() is null");
		root = add(root, key, 0);
	}

	private Node add(Node x, String key, int d) {
		if (x == null) x = new Node();
		if (d == key.length()) {
			if (!x.isWord) n++;
			x.isWord = true;
			return x;
		}
		char c = key.charAt(d);
		x.next[c-'A'] = add(x.next[c-'A'], key, d+1);
		return x;
	}

	public void delete(String key) {
		if (key == null) throw new IllegalArgumentException("argument to delete() is null");
		root = delete(root, key, 0);
	}

	private Node delete(Node x, String key, int d) {
		if (x == null) return null;
		if (d == key.length()) {
			if (x.isWord) n--;
			x.isWord = false;
		}
		else {
			char c = key.charAt(d);
			x.next[c-'A'] = delete(x.next[c-'A'], key, d+1);
		}
		// remove subtrie rooted at x if it is completely empty
		if (x.isWord) return x;
		for (int c = 0; c < R; c++)
			if (x.next[c-'A'] != null)
				return x;
		return null;
	}

	// return the number of keys
	public int size() {
		return n;
	}

	public boolean isEmpty() {
		return size() == 0;
	} 

	public Iterable<String> getAllValidWords(BoggleBoard board) {
		HashSet<String> results = new HashSet<>();
		boolean[][] visited = new boolean[board.rows()][board.cols()];
		StringBuilder prefix = new StringBuilder();

		for (int i = 0; i < board.rows(); ++i) {
			for (int j = 0; j < board.cols(); ++j)
				getAllValidWords(root, board, visited, i, j, prefix, results);
		}
		return results;
	}

	private void getAllValidWords(Node x, BoggleBoard board, boolean[][] visited, int i, int j, StringBuilder prefix, HashSet<String> results) {
		int rows = board.rows();
		int cols = board.cols(); 

		if (x == null || i < 0 || j < 0 || i >= rows || j >= cols || visited[i][j]) return;

		char c = board.getLetter(i, j);
		x = x.next[c-'A'];
		prefix.append(c);

		boolean qu = false;
		if (c == 'Q' && x != null) {
			x = x.next['U'-'A'];
			prefix.append('U');
			qu = true;
		}

		visited[i][j] = true;
		if (x != null && x.isWord && prefix.length() > 2) results.add(prefix.toString());

		getAllValidWords(x, board, visited, i-1, j, prefix, results);
		getAllValidWords(x, board, visited, i, j-1, prefix, results);
		getAllValidWords(x, board, visited, i, j+1, prefix, results);
		getAllValidWords(x, board, visited, i+1, j, prefix, results);
		getAllValidWords(x, board, visited, i-1, j-1, prefix, results);
		getAllValidWords(x, board, visited, i-1, j+1, prefix, results);
		getAllValidWords(x, board, visited, i+1, j-1, prefix, results);
		getAllValidWords(x, board, visited, i+1, j+1, prefix, results);

		visited[i][j] = false; //backtrack
		prefix.deleteCharAt(prefix.length()-1);
		if (qu) prefix.deleteCharAt(prefix.length()-1);
	}
}
