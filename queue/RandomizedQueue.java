import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
	private Item[] array;
	private int first;
	public RandomizedQueue() {
		array = (Item[]) new Object[2];
		first = 0; 
	}                 
	public boolean isEmpty() {
		return first == 0;
	}                 
	public int size() {
		return first;
	}        
	private boolean isFull() {
		return first == array.length;
	}
	private void resize(int s) {
		Item[] newArray = (Item[]) new Object[s];
		for (int i = 0; i < first; ++i) {
			newArray[i] = array[i];
		}
		array = newArray;
	}
	public void enqueue(Item item) {
		if (item == null)
			throw new java.lang.IllegalArgumentException();
		if (isFull()) {
			resize(2*array.length);
		}
		
		int ind = first;
		if (first > 0)
			ind = StdRandom.uniform(first);
		array[first++] = array[ind];
		array[ind] = item;
	} 
	public Item dequeue() {
		if (isEmpty())
			throw new java.util.NoSuchElementException();

		return array[--first];
	}
	public Item sample() {
		if (isEmpty())
			throw new java.util.NoSuchElementException();
		int ind = StdRandom.uniform(first);
		return array[ind];
	}

	private class RandomIterator implements Iterator<Item> {
			private int current = 0;
			private int[] seq = null;
			public RandomIterator() {
				seq = new int[size()];
				for (int i = 0; i < size(); ++i) {
					seq[i] = i;
				}
			}


			@Override
			public boolean hasNext() {
				return current < first;
			}
			@Override
			public Item next() {
				if (!hasNext())
					throw new java.util.NoSuchElementException();
				int ind = StdRandom.uniform(first-current);

				Item res = array[seq[ind]];
				seq[ind] = seq[first-current-1];
				current++;
				return res;
			}
			@Override
			public void remove() {
				throw new java.lang.UnsupportedOperationException();
			}
	}

	public Iterator<Item> iterator() { 
		return new RandomIterator();
	}
	public static void main(String[] args) {
		RandomizedQueue<Integer> dq = new RandomizedQueue<Integer>();
		for (int i = 0; i < 10; ++i) {
			dq.enqueue(i);
		}
		Iterator<Integer> itr = dq.iterator(); 
		while (itr.hasNext()) {
			System.out.format("%d ", itr.next());
		}

		System.out.print('\n');

		Iterator<Integer> itr1 = dq.iterator(); 
		while (itr1.hasNext()) {
			System.out.format("%d ", itr1.next());
		}

		System.out.print('\n');
		while (!dq.isEmpty()) {
			System.out.format("%d ", dq.dequeue());
		}
		System.out.print('\n');
	}
}
