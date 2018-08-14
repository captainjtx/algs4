import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import java.util.LinkedList;
import edu.princeton.cs.algs4.StdOut;

public class MoveToFront {
    public MoveToFront() {
    }

    private static LinkedList<Character> initializeTable() {
        LinkedList<Character> table = new LinkedList<Character>();
        for (char i = 0; i < 256; ++i) {
            table.add(i);
        }

        return table;
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Character> table = initializeTable();
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            char pos = (char) table.indexOf(c);
            table.remove(pos);
            table.addFirst(c);

            BinaryStdOut.write(pos);
        }
        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        LinkedList<Character> table = initializeTable();
        while (!BinaryStdIn.isEmpty()) {
            char pos = BinaryStdIn.readChar();
            char c = table.get(pos);
            table.remove(pos);
            table.addFirst(c);

            BinaryStdOut.write(c);
        }
        BinaryStdOut.flush();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            MoveToFront.encode();
        }
        else if (args[0].equals("+")) {
            MoveToFront.decode();
        }
    }
}
