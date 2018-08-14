public class CircularSuffixArray {
    private final int len;
    // circular suffix array of s
    public CircularSuffixArray(String s)  {
        if (s == null)
            throw new java.lang.IllegalArgumentException();

        len = s.length();

        // using radix sort from LSD to MSD
        for (int i = 0; i < len; ++i) {

        }
    }

    // length of s
    public int length() {
        return len;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
    }

    // unit testing (required)
    public static void main(String[] args) {
    }

