import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	private final WeightedQuickUnionUF uf; 
	private boolean [][] sites;
	private final int n;
	private int numOpen = 0;

	public Percolation(int n) {
		if (n <= 0)
			throw new java.lang.IllegalArgumentException();
		uf = new WeightedQuickUnionUF(n*n+2);
		sites = new boolean [n][n];
		this.n = n;
		numOpen = 0;
	}
	private int convert(int row, int col) {
		return (row-1)*n+col;
	}
	public boolean isOpen(int row, int col) {
		if (row < 1 || row > n || col < 1 || col > n)
			throw new java.lang.IllegalArgumentException();
		return sites[row-1][col-1];
	}
	public boolean isFull(int row, int col) {
		if (row < 1 || row > n || col < 1 || col > n)
			throw new java.lang.IllegalArgumentException();

		return uf.connected(convert(row, col), 0);
	}
	
	public void open(int row, int col) {
		if (row < 1 || row > n || col < 1 || col > n)
			throw new java.lang.IllegalArgumentException();
		if (!sites[row-1][col-1]) {
			sites[row-1][col-1] = true;
			numOpen++;
			int c = convert(row, col);
			if (row == 1)
				uf.union(c, 0);

			if (row < n && isOpen(row+1, col)) {
				uf.union(c+n, c);
			}
			if (row > 1 && isOpen(row-1, col)) {
				uf.union(c-n, c);
			}
			if (col < n && isOpen(row, col+1)) {
				uf.union(c+1, c);
			}
			if (col > 1 && isOpen(row, col-1)) {
				uf.union(c-1, c);
			}

			if (row == n) {
				uf.union(c, n * n + 1);
			}
		}
	}
	public int numberOfOpenSites() {
		return numOpen;
	}
	public boolean percolates() {
		return uf.connected(n*n+1, 0);
	}
	public static void main(String[] args) {
		int k = Integer.parseInt(args[0]);
		Percolation p = new Percolation(k);

		while (!p.percolates()) {
			int col = StdRandom.uniform(1, k+1);
			int row = StdRandom.uniform(1, k+1);
			System.out.format("open row %d, col %d,\n", row, col);
			p.open(row, col);
		}
		System.out.print("Percolates !\n");
	}
}
