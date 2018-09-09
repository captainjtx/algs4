import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class CircularSuffixArray {
	private final int len;
	private final int[] index;
	// circular suffix array of s
	public CircularSuffixArray(String s)  {
		if (s == null)
			throw new java.lang.IllegalArgumentException();

		len = s.length();
		// initialize index array 
		index = new int [len];
		for (int i = 0; i < len; ++i)
			index[i] = i;

		// ternary MSD sort 
		ternaryMSD(s);
	}

	private char getChar(String s, int circular, int pos) {
		return s.charAt((circular+pos) % len);
	}
	private void swap(int[] array, int i, int j) {
		int tmp = array[i];
		array[i] = array[j];
		array[j] = tmp;
	}

	private void ternaryMSD(String s) {
		threeWayPartition(s, 0, 0, length()-1);
	}

	private void threeWayPartition(String s, int pos, int left, int right) {
		if (left >= right || pos >= length())
			return;

		char pivot = getChar(s, index[left], pos);
		// ternary quicksort
		int p = left;
		int q = right; 
		int curr = left+1;

		while (curr <= q) {
			char c = getChar(s, index[curr], pos);
			if (c < pivot) {
				swap(index, curr, p++);
				++curr;
			}
			else if (c > pivot) {
				swap(index, curr, q--);
			}
			else
				++curr;
		}

		// sort the smaller part at the same char position
		threeWayPartition(s, pos, left, p-1);
		// sort the middle (equal) part at the next position
		threeWayPartition(s, pos+1, p, q);
		// sort the lower part at the same position
		threeWayPartition(s, pos, q+1, right);
	}

	// length of s
	public int length() {
		return len;
	}

	// returns index of ith sorted suffix
	public int index(int i) {
		if (i < 0 || i > len-1)
			throw new java.lang.IllegalArgumentException();

		return index[i];
	}

	// unit testing (required)
	public static void main(String[] args) {
		In in = new In(args[0]);
		String s = in.readLine();
		CircularSuffixArray csa = new CircularSuffixArray(s);
		StdOut.println("Length of string: "+ csa.length());
		for (int i = 0; i < csa.length(); ++i) {
			StdOut.print(csa.index(i)+ " ");
		}
		StdOut.println();
		for (int i = 0; i < csa.length(); ++i) {
			for (int j = 0; j < csa.length(); ++j) {
				StdOut.print(s.charAt((csa.index(i)+j) % s.length()));
				StdOut.print(" ");
			}
			StdOut.println();
		}
	}
}

