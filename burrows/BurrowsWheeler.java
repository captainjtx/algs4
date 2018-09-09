import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import java.util.ArrayList;

public class BurrowsWheeler {
	// apply Burrow-Wheeler transform, reading from standard input and writing to standard output
	public static void transform() {
		String s = BinaryStdIn.readString();

		CircularSuffixArray csa = new CircularSuffixArray(s);

        for (int i = 0; i < csa.length(); ++i)
            if (csa.index(i) == 0)
                BinaryStdOut.write(i);

        for (int i = 0; i < csa.length(); ++i) {
            int p = (csa.index(i) + csa.length()-1) % csa.length();
            BinaryStdOut.write(s.charAt(p));
        }

        BinaryStdOut.flush();
	}

	// apply Burrow-Wheeler inverse transform, reading from standard input and writing to standard output
	public static void inverseTransform() {
        int start = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();

        // cumulative sum of each char
        int[] csum = new int[257];
        for (int i = 0; i < s.length(); ++i)
            csum[s.charAt(i)+1] += 1;
        
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < 256; ++i)
            for (int j = 0; j < csum[i+1]; ++j)
                sb.append((char) i);

        for (int i = 1; i < 257; ++i)
            csum[i] += csum[i-1];

        // preprocess the string to capture the all positions of each char in advance
        ArrayList<ArrayList<Integer>> pos = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < 256; ++i) 
            pos.add(new ArrayList<Integer>());

        for (int i = 0; i < s.length(); ++i)
            pos.get(s.charAt(i)).add(i);

        // decode
        for (int i = 0; i < s.length(); ++i) {
            char c = sb.charAt(start);
            BinaryStdOut.write(c);

            int offset = start-csum[c];
            start = pos.get(c).get(offset);
        }

        BinaryStdOut.flush();
	}

	// if args[0] is '-', apply Burrows-Wheeler transform
	// if args[0] is '+', apply Burrows-Wheeler inverse transform
	public static void main(String[] args) {
        if (args[0].equals("-"))
            BurrowsWheeler.transform();
        else if (args[0].equals("+"))
            BurrowsWheeler.inverseTransform();
	}
}
