import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;
public class PercolationStats {
	private final int trials;
	private final double[] threshold;
	public PercolationStats(int n, int trials) {    // perform trials independent experiments on an n-by-n grid
		if (n <= 0 || trials <= 0)
			throw new java.lang.IllegalArgumentException();
		this.trials = trials;

		threshold = new double[trials];

		double num  = n*n;
		double count;
		for (int t = 0; t < trials; ++t) {
			Percolation p = new Percolation(n);
			count = 0;
			while (!p.percolates()) {
				int col = StdRandom.uniform(1, n+1);
				int row = StdRandom.uniform(1, n+1);
				if (!p.isOpen(row, col)) {
					p.open(row, col);
					count++;
				}
			}
			threshold[t] = count/num;
		}
	}
	public double mean() {                          // sample mean of percolation threshold 
		return StdStats.mean(threshold);

	}
	public double stddev() {                        // sample standard deviation of percolation threshold
		return StdStats.stddev(threshold);
	}
	public double confidenceLo() {                  // low  endpoint of 95% confidence interval
		return this.mean() - 1.96 * this.stddev()/java.lang.Math.sqrt(trials);
	}
	public double confidenceHi() {                  // high endpoint of 95% confidence interval
		return this.mean() + 1.96 * this.stddev()/java.lang.Math.sqrt(trials);
	}
	public static void main(String[] args) {        // test client (described below)
		int n = Integer.parseInt(args[0]);
		int t = Integer.parseInt(args[1]);
		PercolationStats ps = new PercolationStats(n, t);
		System.out.format("mean	= %f\nstddev = %f\n95%% confidence interval = [%f, %f]\n",
			       	ps.mean(), ps.stddev(), ps.confidenceLo(), ps.confidenceHi());
	}
}
