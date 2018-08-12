import java.util.LinkedList;
public class Board {
	private int[] puzzle;
	private final int dim;
	private int rowBlank;
	private int colBlank;
	private int manhattanDistance = 0;
	private int hammingDistance = 0;
	public Board(int[][] blocks) {
		dim = blocks.length;
		puzzle = new int[dim*dim];

		for (int i = 0; i < dim; ++i) {
			for (int j = 0; j < dim; ++j) {
				puzzle[i*dim+j] = blocks[i][j];
				if (blocks[i][j] == 0) {
					rowBlank = i;
					colBlank = j;
				}

				int val = puzzle[i*dim+j];
				if (val != 0) {
					int row = (val-1)/dim;
					int col = val-row*dim-1;
					manhattanDistance += (abs(row-i) + abs(col-j));

					if (val != i*dim+j+1) {
						hammingDistance++;
					}
				}
			}
		}
	}
	private int abs(int k) {
		if (k < 0) 
			return -k;
		else
			return k;
	}

	public int dimension() {
		return dim;
	}

	public int manhattan() {
		return manhattanDistance;
	}
	public int hamming() {
		return hammingDistance;
	}
	public boolean isGoal() {
		return manhattan() == 0;
	}
	
	private void swap(int a, int b) {
		int tmp = puzzle[a];
		puzzle[a] = puzzle[b];
		puzzle[b] = tmp;
	}
	public Board twin() {

		int a = 0;
		int b = 0;
		int row = 0;
		int col = 0;
		while (true) {
			if (puzzle[a] != 0) {
				if (row > 0 && puzzle[a-dim] != 0) {
					b = a-dim;
					break;
				}
				else if (row < dim-1 && puzzle[a+dim] != 0) {
					b = a+dim;
					break;
				}
				else if (col > 0 && puzzle[a-1] != 0)  {
					b = a-1;
					break;
				}
				else if (col < dim-1 && puzzle[a+1] != 0)  {
					b = a+1;
					break;
				}
			}
			a++;
			row = a/dim;
			col = a-row*dim;
		}

		swap(a, b);
		int[][] blocks = new int[dim][dim];
		for (int i = 0; i < dim; ++i) {
			for (int j = 0; j < dim; ++j) {
				blocks[i][j] = puzzle[i*dim+j];
			}
		}
		swap(a, b);
		Board tw = new Board(blocks);

		return tw;
	}
	public boolean equals(Object y) {
		if (y == this) return true;
		if (y == null) return false;
		if (y.getClass() != this.getClass()) return false;
		Board that = (Board) y;
		if (that.dimension() == dim) {
			for (int i = 0; i < dim*dim; ++i) {
				if (puzzle[i] != that.puzzle[i])
					return false;
			}
			return true;
		}
		else
			return false;
	}

	private void swap(int[][] array, int a, int b) {
		int ra = a/dim;
		int ca = a-ra*dim;
		int rb = b/dim;
		int cb = b-rb*dim;
		int tmp = array[ra][ca];
		array[ra][ca] = array[rb][cb];
		array[rb][cb] = tmp;
	}
	public Iterable<Board> neighbors() {
		LinkedList<Board> neigh = new LinkedList<Board>();
		int[][] blocks = new int[dim][dim];
		for (int i = 0; i < dim; ++i) {
			for (int j = 0; j < dim; ++j) {
				blocks[i][j] = puzzle[i*dim+j];
			}
		}
		int a = rowBlank*dim+colBlank;
		int b;
		if (rowBlank > 0) {
			b = a-dim;
			swap(blocks, a, b);
			Board node = new Board(blocks);
			neigh.add(node);
			swap(blocks, a, b);
		}

		if (rowBlank < dim-1) {
			b = a+dim;
			swap(blocks, a, b);
			Board node = new Board(blocks);
			neigh.add(node);
			swap(blocks, a, b);
		}

		if (colBlank < dim-1) {
			b = a+1;
			swap(blocks, a, b);
			Board node = new Board(blocks);
			neigh.add(node);
			swap(blocks, a, b);
		}

		if (colBlank > 0) {
			b = a-1;
			swap(blocks, a, b);
			Board node = new Board(blocks);
			neigh.add(node);
			swap(blocks, a, b);
		}
		return neigh;
	}
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(dim + "\n");
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				s.append(String.format("%2d ", puzzle[i*dim+j]));
			}
			s.append("\n");
		}
		return s.toString();
	}
	public static void main(String[] args) {
	}

}
