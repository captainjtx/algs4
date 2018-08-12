import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
public class BruteCollinearPoints {
	private final LineSegment[] segs;
	private final int numOfSegs;

	public BruteCollinearPoints(Point[] points) {
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
		for (int i = 0; i < p.length-3; ++i) {
			for (int j = i+1; j < p.length-2; ++j) {
				double s1 = p[i].slopeTo(p[j]);
				for (int m = j+1; m < p.length-1; ++m) {
					double s2 = p[j].slopeTo(p[m]);
					if (s1 != s2)
						continue;
					for (int n = m+1; n < p.length; ++n) {
						double s3 = p[m].slopeTo(p[n]);

						if (s2 == s3) {
							num++;
						}
					}
				}
			}
		}
		numOfSegs = num;

		segs = new LineSegment[num];
		int count = 0;
		for (int i = 0; i < p.length-3; ++i) {
			for (int j = i+1; j < p.length-2; ++j) {
				double s1 = p[i].slopeTo(p[j]);
				for (int m = j+1; m < p.length-1; ++m) {
					double s2 = p[j].slopeTo(p[m]);
					if (s1 != s2)
						continue;
					for (int n = m+1; n < p.length; ++n) {
						double s3 = p[m].slopeTo(p[n]);

						if (s2 == s3) {
							segs[count] = new LineSegment(p[i], p[n]);
							++count;
						}

					}
				}
			}
		}
	}
	public int numberOfSegments() {
		return numOfSegs;
	}
	public LineSegment[] segments() {
		LineSegment[] cpy = new LineSegment[numOfSegs];
		for ( int i = 0; i < numOfSegs; ++i) {
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
		BruteCollinearPoints collinear = new BruteCollinearPoints(points);
		StdOut.println("numberOfSegments: "+collinear.numberOfSegments());
		for (LineSegment segment : collinear.segments()) {
			StdOut.println(segment);
			segment.draw();
		}
		StdDraw.show();
	}
}

