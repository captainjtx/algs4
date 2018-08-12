import edu.princeton.cs.algs4.StdDraw;
import java.util.Stack;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
public class PointSET {
	private SET<Point2D> map;
	public PointSET() {
		map = new SET<Point2D>();
	}
	public boolean isEmpty() {
		return map.isEmpty();
	}
	public int size() {
		return map.size();
	}
	public void insert(Point2D p) {
		if (p == null)
			throw new java.lang.IllegalArgumentException();

		map.add(p);
	}
	public boolean contains(Point2D p) {
		if (p == null)
			throw new java.lang.IllegalArgumentException();
		return map.contains(p);
	}
	public void draw() {
		for (Point2D it : map) {
			StdDraw.point(it.x(), it.y());
		}
	}
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null)
			throw new java.lang.IllegalArgumentException();

		Stack<Point2D> inside = new Stack<Point2D>();
		for (Point2D it : map) {
			if (rect.contains(it))
				inside.add(it);
		}

		return inside;
	}
	public Point2D nearest(Point2D p) {
		if (p == null)
			throw new java.lang.IllegalArgumentException();
		if (isEmpty())
			return null;
		Point2D nearest = map.max();
		double dist = p.distanceSquaredTo(nearest); 
		for (Point2D it : map) {
			double tmp = p.distanceSquaredTo(it);
			if (tmp < dist) {
				dist = tmp;
				nearest = it;
			}
		}
		return nearest;
	}
	public static void main(String[] args) {
	}

}

