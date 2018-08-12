import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
	private Item[] array;
	private int first;
	private int last;
	// construct an empty deque
	public Deque() {
		array = (Item[]) new Object[2];
		first = 0; 
		last = 0;
	}                           
	private int circularIndex(int ind) {
		if (ind >= 0)
			return ind;
		else
			return ind+array.length;
	}

	// return the number of items on the deque
	public int size() {
		return circularIndex(first-last);
	}
	private void resize(int s) {
		Item[] newArray = (Item[]) new Object[s];
		int length = size();
		for (int i = 0; i < length; ++i) {
			int ind = circularIndex(first-1-i); 
			newArray[length-1-i] = array[ind];
		}
		first = length;
		last = 0;
		array = newArray;
	}
	// is the deque empty?
	public boolean isEmpty() {
		return first == last;
	}

	private boolean isFull() {
		return ((first+1) % array.length) == last; 
	}
	// add the item to the front
	public void addFirst(Item item) {
		if (item == null)
			throw new java.lang.IllegalArgumentException();
		if (isFull()) {
			resize(2*array.length);
		}
		array[first] = item; 
		first = (first+1) % array.length;
	}
	// add the item to the end
	public void addLast(Item item) {
		if (item == null)
			throw new java.lang.IllegalArgumentException();
		if (isFull()) {
			resize(2*array.length);
		}

		last = circularIndex(last-1); 
		array[last] = item;
	}
	// remove and return the item from the front
	public Item removeFirst() {
		if (isEmpty())
			throw new java.util.NoSuchElementException();

		int front = circularIndex(first-1); 
		Item res = array[front];
		array[front] = null;
		first = front; 

		if (size() < array.length/4)
			resize(array.length/2);

		return res;
	}               
	// remove and return the item from the end
	public Item removeLast() {
		if (isEmpty())
			throw new java.util.NoSuchElementException();

		Item res = array[last];
		array[last] = null;
		last = (last+1) % array.length;

		if (size() < array.length/4)
			resize(array.length/2);

		return res;
	}                
	// return an iterator over items in order from front to end
	@Override
	public Iterator<Item> iterator() {
		Iterator<Item> it = new Iterator<Item>() {
			private int current = 0;
			@Override
			public boolean hasNext() {
				return current < size();
			}
			@Override
			public Item next() {
				if (!hasNext())
					throw new java.util.NoSuchElementException();
				int ind = circularIndex(first-current-1);
				current++;
				return array[ind];
			}
			@Override
			public void remove() {
				throw new java.lang.UnsupportedOperationException();
			}
		};
		return it;
	}
	// unit testing (optional)
	public static void main(String[] args) {
		Deque<Integer> dq = new Deque<Integer>();
		for (int i = 0; i < 10; ++i) {
			dq.addFirst(i);
		}

		Iterator<Integer> itr = dq.iterator();
		while (itr.hasNext()) {
			System.out.format("%d ", itr.next());
		}
	}   
}
