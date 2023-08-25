package program;

import java.util.Iterator;

/**
 * Generic Singly Linked List Collection Library
 * 
 * @author Patrik Turcani
 * @param <T> Data type of the Singly Linked List
 */
public class MySinglyLinkedList<T> implements Iterable<T>, Iterator<T> {
	// Private Variables
	private int count;
	private Node<T> current;

	/**
	 * Outputs the amount of values in the list
	 * 
	 * @return number of values in the list as "int"
	 */
	public int Count() {
		return count;
	}

	/**
	 * Constructor - initializes the list
	 */
	public MySinglyLinkedList() {
		count = 0;
		current = null;
	}

	/**
	 * adds a value to the current of the list
	 * 
	 * @param val value to be added
	 */
	public void AddHead(T val) {
		Node<T> n = new Node(val);
		n.next = current;
		current = n;
		count++;
	}

	/**
	 * Adds a value to the end of the list
	 * 
	 * @param value Insert value to add to the end of the list
	 */
	public void AddTail(T value) {
		Node<T> newNode = new Node(value);
		if (current == null) {
			current = newNode;
		} else {
			current.AddAtEnd(newNode);
		}
		count++;
	}

	/**
	 * Remove a specific value from the list
	 * 
	 * @param toDel Insert value to remove from the list
	 */
	public void DeleteVal(T toDel) {
		Node<T> currentV = null;
		if (current.value == toDel) {
			current = current.next;
			count--;
			return;
		}
		currentV = current;
		while (currentV.next != null) {
			if (currentV.next.value == toDel) {
				currentV.next = currentV.next.next;
				count--;
				return;
			}
			currentV = currentV.next;
		}
	}

	/**
	 * Remove value at the start of the list
	 * 
	 * @return The removed value
	 * @throws Exception if list is empty
	 */
	public T RemoveHead() throws Exception {
		if (ListEmpty()) {
			throw new Exception("Error: List is empty");
		}
		Node<T> n = current;
		current = n.next;
		count--;
		return n.value;
	}

	/**
	 * Removes the last value in the list
	 * 
	 * @return the removed value
	 * @throws Exception if list is empty
	 */
	public T RemoveTail() throws Exception {
		Node<T> n = current;
		T val = n.value;
		if (ListEmpty()) {
			throw new Exception("List is empty");
		}
		while (n.next != null) {
			if (n.next.next == null) {
				val = n.next.value;
				n.next = null;
				count--;
				return val;
			}
			n = n.next;
		}
		if (n.next == null) {
			current = null;
		}
		count--;
		return val;
	}

	/**
	 * Method that checks if the list is empty
	 * 
	 * @return True if empty false if there are values present in the list
	 */
	public boolean ListEmpty() {
		if (count == 0 && current == null) {
			return true;
		}
		return false;
	}

	/**
	 * Remove all values that are currently inside the list and resets the count of
	 * this list to 0 accordingly
	 */
	public void Clear() {
		current = null;
		count = 0;
	}

	/**
	 * Generic type Node class. Stores a pointer value to the next node and a data
	 * value of the current node
	 * 
	 * @author Patrik Turcani
	 * @param <T> Data type of Node value
	 */
	private class Node<T> {
		// Public variables
		private T value;
		private Node<T> next;

		/**
		 * Node Constructor initializes the node
		 * 
		 * @param v value to be stored in this node
		 */
		public Node(T v) {
			value = v;
			next = null;
		}

		/**
		 * Helps AddTail method to add values at the end of the list
		 * 
		 * @param n
		 */
		public void AddAtEnd(Node<T> n) {
			if (next == null) {
				next = n;
			} else {
				next.AddAtEnd(n);
			}
		}
	}

	// Variable for iterating purposes, temporarily stores current node
	private Node<T> temp = current;

	/**
	 * Sets temp node to the start of the list
	 */
	private void Restart() {
		temp = current;
	}

	/**
	 * returns current value
	 */
	@Override
	public Iterator<T> iterator() {
		return this;
	}

	/**
	 * Checks if there are more values to iterate through
	 */
	@Override
	public boolean hasNext() {
		if (temp == null) {
			Restart();
			return false;
		} else {
			return true;
		}
	}

	/**
	 * sets value at current node, points to the next node and returns the current
	 * value
	 */
	@Override
	public T next() {
		T val = temp.value;
		temp = temp.next;
		return val;
	}

}
