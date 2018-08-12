import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Picture;
import java.lang.Math;

public class SeamCarver {
	private Picture pic;
	private double[][] energy;
	private int energyH;
	private int energyW;

	public SeamCarver(Picture picture) { // create a seam carver object based on the given picture
		if (picture == null)
			throw new java.lang.IllegalArgumentException();
		pic = new Picture(picture); // make a defensive copy

		//precompute the energy for each pixel 
		int w = width();
		int h = height();
		energy = new double[h][w];
		for (int i = 0; i < h; ++i) {
			for (int j = 0; j < w; ++j) {
				energy[i][j] = computeEnergy(j, i);
			}
		}
		energyH = h;
		energyW = w;
	}
	
	public Picture picture() { // current picture
		return new Picture(pic); // make a defensive copy
	}

	public int width() { // width of current picture
		return pic.width();
	}

	public int height() { // height of current picture
		return pic.height();
	}

	private int squaredDistance(int x0, int y0, int x1, int y1) {
		int rgb0 = pic.getRGB(x0, y0);
		int r0 = (rgb0 >> 16) & 0xFF;
		int g0 = (rgb0 >> 8) & 0xFF;
		int b0 = (rgb0 >> 0) & 0xFF;

		int rgb1 = pic.getRGB(x1, y1);
		int r1 = (rgb1 >> 16) & 0xFF;
		int g1 = (rgb1 >> 8) & 0xFF;
		int b1 = (rgb1 >> 0) & 0xFF;

		return (r0-r1)*(r0-r1)+(g0-g1)*(g0-g1)+(b0-b1)*(b0-b1);
	}

	private double computeEnergy(int x, int y) { // compute the energy of pixel at column x and row y
		if (x == 0 || x == width()-1 || y == 0 || y == height()-1)
			return 1000;
		else
			return Math.sqrt(squaredDistance(x-1, y, x+1, y)+squaredDistance(x, y-1, x, y+1));
	}


	public double energy(int x, int y) { // energy of pixel at column x and row y
		if (x < 0 || x > width()-1 || y < 0 || y > height()-1)
			throw new java.lang.IllegalArgumentException();
		return energy[y][x];
	}

	public int[] findHorizontalSeam() { // sequence of indices for horizontal seam
		int h = energyH;
		int w = energyW;

		double[][] tmp = energy;
		energy = energyTranspose();
		energyH = w;
		energyW = h;

		int[] seam = findVerticalSeam();
		energy = tmp;
		energyH = h;
		energyW = w;

		return seam;
	}

	public int[] findVerticalSeam() { // sequence of indices for vertical seam
		int h = energyH;
		int w = energyW;
		
		int[] seam = new int[h];
		if (w == 1)
			return seam;

		// dynamic programming
		int[][] parent = new int[h][w];
		double[] prevDist = new double[w];
		double[] currDist = new double[w];

		for (int i = 0; i < w; ++i)
			prevDist[i] = energy[0][i];
		for (int depth = 1; depth < h; ++depth) {
			// left edge condition 
			if (prevDist[0] <= prevDist[1]) {
				currDist[0] = prevDist[0]+energy[depth][0];
				parent[depth][0] = 0;
			}
			else {
				currDist[0] = prevDist[1]+energy[depth][0];
				parent[depth][0] = 1;
			}
			for (int i = 1; i < w-1; ++i) {
				if (prevDist[i-1] <= prevDist[i] && prevDist[i-1] <= prevDist[i+1]) {
					currDist[i] = prevDist[i-1]+energy[depth][i];
					parent[depth][i] = -1;
				}
				else if (prevDist[i] <= prevDist[i-1] && prevDist[i] <= prevDist[i+1]) {
					currDist[i] = prevDist[i]+energy[depth][i];
					parent[depth][i] = 0;
				}
				else {
					currDist[i] = prevDist[i+1]+energy[depth][i];
					parent[depth][i] = 1;
				}
			}
			// right edge condition 
			if (prevDist[w-2] <= prevDist[w-1]) {
				currDist[w-1] = prevDist[w-2]+energy[depth][w-1];
				parent[depth][w-1] = -1;
			}
			else {
				currDist[w-1] = prevDist[w-1]+energy[depth][w-1];
				parent[depth][w-1] = 0;
			}
			System.arraycopy(currDist, 0, prevDist, 0, currDist.length);
		}
		double minDist = currDist[0];
		int minEnd = 0;
		for (int i = 1; i < w; ++i) {
			if (currDist[i] < minDist) {
				minDist = currDist[i];
				minEnd = i;
			}
		}

		seam[h-1] = minEnd;
		for (int i = h-2; i >= 0 ; --i) {
			seam[i] = seam[i+1]+parent[i+1][seam[i+1]];
		}

		return seam;
	}

	private boolean isValidSeam(int[] seam, int range) { // check if the element is in valid range and differ by <= 1
		for (int i = 0; i < seam.length; ++i) {
			if (seam[i] < 0 || seam[i] >= range || (i > 0 && (seam[i] - seam[i-1] > 1 || seam[i] - seam[i-1] < -1)))
				return false;
		}
		return true;
	}

	private Picture transpose(Picture p) {
		int h = p.height();
		int w = p.width();
		Picture picTranspose = new Picture(h, w);
		for (int i = 0; i < w; ++i) {
			for (int j = 0; j < h; ++j)
				picTranspose.setRGB(j, i, pic.getRGB(i, j));
		}
		return picTranspose;
	}

	private double[][] energyTranspose() {
		double[][] energyT = new double[energyW][energyH];
		for (int i = 0; i < energyW; ++i)
			for (int j = 0; j < energyH; ++j)
				energyT[i][j] = energy[j][i];
		return energyT;
	}

	public void removeHorizontalSeam(int[] seam) { // remove horizontal seam from current picture
		int h = height();
		int w = width();

		pic = transpose(pic);
		energy = energyTranspose();
		energyW = h;
		energyH = w;

		removeVerticalSeam(seam);

		pic = transpose(pic);
		energy = energyTranspose();
		energyH = h-1;
		energyW = w;
	}

	public void removeVerticalSeam(int[] seam) { // remove vertical seam from current picture
		int h = height();
		int w = width();
		if (seam == null || seam.length != h || w <= 1 || !isValidSeam(seam, w))
			throw new java.lang.IllegalArgumentException();
		Picture newPic = new Picture(w-1, h);
		for (int i = 0; i < h; ++i) {
			for (int j = 0; j < w-1; ++j) {
				if (j < seam[i])
					newPic.setRGB(j, i, pic.getRGB(j, i));
				else
					newPic.setRGB(j, i, pic.getRGB(j+1, i));
			}
		}
		pic = newPic;

		for (int i = 0; i < h; ++i) {
			System.arraycopy(energy[i], seam[i]+1, energy[i], seam[i], w-seam[i]-1);
		}
		for (int i = 1; i < h-1; ++i) {
			if (seam[i] == 0)
				energy[i][0] = 1000;
			else if (seam[i] == w-1)
				energy[i][w-2] = 1000;
			else {
				energy[i][seam[i]-1] = computeEnergy(seam[i]-1, i); 
				energy[i][seam[i]] = computeEnergy(seam[i], i); 
			}
		}
		energyW--;
	}
	public static void main(String[] args) { // main function for testing
	}
}
