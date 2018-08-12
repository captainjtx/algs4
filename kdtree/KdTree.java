import edu.princeton.cs.algs4.StdDraw;
import java.util.Stack;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
public class KdTree {
	private class Node {
		private Point2D p = null;
		private Node left = null;
		private Node right = null;

		public Node(Point2D pt) {
			p = new Point2D(pt.x(), pt.y());
		}
	}

	private int num = 0;
	private Node root = null;
	public KdTree() {
	}
	public boolean isEmpty() {
		return root == null;
	}
	public int size() {
		return num;
	}
	public void insert(Point2D p) {
		if (p == null)
			throw new java.lang.IllegalArgumentException();

		Node current = root;
		Node parent = root;

		boolean even = true;
		boolean left = true;

		RectHV part = new RectHV(0, 0, 1, 1);
		while (current != null) {
			parent = current;

			if (current.p.equals(p))
				return;

			if (even) {
				if (p.x() < current.p.x()) {
					current = current.left;
					left = true;
				}
				else {
					current = current.right;
					left = false;
				}
			}
			else {
				if (p.y() < current.p.y()) {
					current = current.left;
					left = true;
				}
				else {
					current = current.right;
					left = false;
				}
			}
			even = !even;
		}

		Node newNode = new Node(p);
		if (parent == null)
			root = newNode;
		else if (left)	
			parent.left = newNode;
		else
			parent.right = newNode;

		num++;

	}
	public boolean contains(Point2D p) {
		if (p == null)
			throw new java.lang.IllegalArgumentException();
		Node current = root; 
		boolean even = true;

		while (current != null) {


			if (current.p.equals(p))
				return true;

			if (even) {
				if (p.x() < current.p.x()) {
					current = current.left;
				}
				else {
					current = current.right;
				}
			}
			else {
				if (p.y() < current.p.y()) {
					current = current.left;
				}
				else {
					current = current.right;
				}
			}
			even = !even;
		}
		return false;
	}
	private void drawHelper(Node current) {
		if (current != null) {
			StdDraw.point(current.p.x(), current.p.y());
			drawHelper(current.left);
			drawHelper(current.right);
		}
	}
	public void draw() {
		drawHelper(root);
	}

	private void rangeHelper(RectHV rect, RectHV part, Node current, boolean even, Stack<Point2D> in) {
		if (current == null)
			return;

		if (rect.contains(current.p))
			in.add(current.p);

		RectHV leftRec;
		RectHV rightRec;
		if (even) {
			leftRec = new RectHV(part.xmin(), part.ymin(), current.p.x(), part.ymax());
			rightRec = new RectHV(current.p.x(), part.ymin(), part.xmax(), part.ymax());
		}
		else {
			leftRec = new RectHV(part.xmin(), part.ymin(), part.xmax(), current.p.y());
			rightRec = new RectHV(part.xmin(), current.p.y(), part.xmax(), part.ymax());
		}
		if (rect.intersects(leftRec))
			rangeHelper(rect, leftRec, current.left, !even, in);

		if (rect.intersects(rightRec))
			rangeHelper(rect, rightRec, current.right, !even, in);
	}
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null)
			throw new java.lang.IllegalArgumentException();

		Stack<Point2D> inside = new Stack<Point2D>();

		RectHV initial = new RectHV(0.0, 0.0, 1.0, 1.0);

		rangeHelper(rect, initial, root, true, inside);
		
		return inside;
	}
	private Point2D nearestHelper(Point2D p, Node current, RectHV part, boolean even, Point2D nearest) {
		if (current == null) 
			return nearest;

		double dist = p.distanceSquaredTo(nearest);
		double newDist = p.distanceSquaredTo(current.p);

		if (newDist < dist) {
			nearest = current.p;
			dist = newDist;
		}

		RectHV leftRec;
		RectHV rightRec;
		if (even) {
			leftRec = new RectHV(part.xmin(), part.ymin(), current.p.x(), part.ymax());
			rightRec = new RectHV(current.p.x(), part.ymin(), part.xmax(), part.ymax());
		}
		else {
			leftRec = new RectHV(part.xmin(), part.ymin(), part.xmax(), current.p.y());
			rightRec = new RectHV(part.xmin(), current.p.y(), part.xmax(), part.ymax());
		}

		double leftDist = leftRec.distanceSquaredTo(p);
		double rightDist = rightRec.distanceSquaredTo(p);

		if (leftDist < rightDist) {
			if (leftDist < dist) {
				nearest = nearestHelper(p, current.left, leftRec, !even, nearest);
			}

			dist = p.distanceSquaredTo(nearest);
			if (rightDist < dist) {
				nearest = nearestHelper(p, current.right, rightRec, !even, nearest);
			}
		}
		else {
			if (rightDist < dist) {
				nearest = nearestHelper(p, current.right, rightRec, !even, nearest);
			}

			dist = p.distanceSquaredTo(nearest);
			if (leftDist < dist) {
				nearest = nearestHelper(p, current.left, leftRec, !even, nearest);
			}
		}
		return nearest;
	}
	public Point2D nearest(Point2D p) {
		if (p == null)
			throw new java.lang.IllegalArgumentException();
		if (isEmpty())
			return null;

		RectHV initial = new RectHV(0.0, 0.0, 1.0, 1.0);

		return nearestHelper(p, root, initial, true, root.p);
	}
	public static void main(String[] args) {
	}

}

