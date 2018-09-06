import CircularSuffixArray;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;

public class BurrowWheeler {
	// apply Burrow-Wheeler transform, reading from standard input and writing to standard output
	public static void transform() {
		In in = new In();
		String s = in.readLine();

		CircularSuffixArray csa = new CircularSuffixArray(s);
	}

	// apply Burrow-Wheeler inverse transform, reading from standard input and writing to standard output
	public static void inverseTransform() {
	}

	// if args[0] is '-', apply Burrows-Wheeler transform
	// if args[1] is '+', apply Burrows-Wheeler inverse transform
	public static void main(String[] args) {
	}
}
