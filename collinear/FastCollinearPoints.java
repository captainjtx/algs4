import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
public class FastCollinearPoints {
	private final int numSegs;
	private final LineSegment[] segs;
	public FastCollinearPoints(Point[] points) {
		if (points == null)
			throw new java.lang.IllegalArgumentException();
		for (Point pt : points) {
			if (pt == null)
				throw new java.lang.IllegalArgumentException();
		}

		Point[] p = new Point[points.length];
		for (int i = 0; i < points.length; ++i) {
			p[i] = points[i];
		}

		Arrays.sort(p);
		for (int i = 0; i < p.length-1; ++i) {
			if (p[i].compareTo(p[i+1]) == 0)
				throw new java.lang.IllegalArgumentException();
		}

		int num = 0;
		Point[] pp = new Point[p.length];
		for (int i = 0; i < p.length-3; ++i) {
			for (int k = 0; k < p.length; ++k) {
				pp[k] = p[k];
			}
			Point origin = pp[i];

			Arrays.sort(pp, origin.slopeOrder());
			int count = 1;
			boolean flag = false;

			for (int j = 2; j <= pp.length; ++j) {
				if (j < pp.length && origin.slopeTo(pp[j-1]) == origin.slopeTo(pp[j])) {
					++count;
					if (origin.compareTo(pp[j]) > 0 || origin.compareTo(pp[j-1]) > 0)  
						flag = true;
				}
				else {
					if (count >= 3 && !flag) {
						num++;
					}
					count = 1;
					flag = false;
				}
			}
		}
		numSegs = num;
		
		segs = new LineSegment[numSegs];

		int ind = 0;
		for (int i = 0; i < p.length-3; ++i) {
			for (int k = 0; k < p.length; ++k) {
				pp[k] = p[k];
			}
			Point origin = pp[i];
			Arrays.sort(pp, origin.slopeOrder());
			int count = 1;
			boolean flag = false;

			for (int j = 2; j <= pp.length; ++j) {
				if (j < pp.length && origin.slopeTo(pp[j-1]) == origin.slopeTo(pp[j])) {
					++count;
					if (origin.compareTo(pp[j]) > 0 || origin.compareTo(pp[j-1]) > 0)  
						flag = true;
				}
				else {
					if (count >= 3 && !flag) {
						segs[ind++] = new LineSegment(origin, pp[j-1]);
					}
					count = 1;
					flag = false;
				}
			}
		}
	}
	public int numberOfSegments() {
		return numSegs;
	}
	public LineSegment[] segments() {
		LineSegment[] cpy = new LineSegment[numSegs];
		for ( int i = 0; i < numSegs; ++i) {
			cpy[i] = segs[i];
		}
		return cpy;
	}
	public static void main(String[] args) {
		// read the n points from a file
		In in = new In(args[0]);
		int n = in.readInt();
		Point[] points = new Point[n];
		for (int i = 0; i < n; i++) {
			int x = in.readInt();
			int y = in.readInt();
			points[i] = new Point(x, y);
		}

		// draw the points
		StdDraw.enableDoubleBuffering();
		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);
		for (Point p : points) {
			p.draw();
		}
		StdDraw.show();

		// print and draw the line segments
		FastCollinearPoints collinear = new FastCollinearPoints(points);
		StdOut.println("numberOfSegments: "+collinear.numberOfSegments());
		LineSegment[] lsegs = collinear.segments();
		for (LineSegment segment : lsegs) {
			StdOut.println(segment);
			segment.draw();
		}
		StdDraw.show();
	}
}
