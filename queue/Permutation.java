import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdIn;

public class Permutation {
	public static void main(String[] args) {
		int k = Integer.parseInt(args[0]);
		RandomizedQueue<String> rq = new RandomizedQueue<String>();
		int count = 0;

		while (!StdIn.isEmpty()) {
			String str = StdIn.readString();
			
			int num = StdRandom.uniform(count+1);
			if (count < k) {
				rq.enqueue(str);
			}
			else {
				if (num < k) {
					rq.dequeue();
					rq.enqueue(str);
				}
			}
			count++;
		}

		for (int i = 0; i < k; ++i) {
			System.out.println(rq.dequeue());
		}
	}
}
