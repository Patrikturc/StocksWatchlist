package program;

import javax.swing.JOptionPane;

/**
 * Custom Double Array Collection Library Works with linear Array
 * 
 * @author Patrik Turcani
 */
public class MyArray {

	// Private variables
	private int count = 0;
	private double[] myArray;

	// Public variables
	/**
	 * gets contents of the array
	 * 
	 * @return contents of the array
	 */
	public double[] GetMyArray() {
		return myArray;
	}

	/**
	 * Gets size of the array
	 * 
	 * @return size of the array
	 */
	public int GetCount() {
		return count;
	}

	/**
	 * Constructor initialises the Array and specifies the size
	 * 
	 * @param length
	 */
	public MyArray(int size) {
		myArray = new double[size];
		count = 0;
	}

	/**
	 * Bubble sort for an array of doubles
	 */
	public void BubbleSort() {
		double min;
		double tempValue = 0;
		int tempIndex = 0;
		boolean alreadySorted = false;
		for (int i = 0; i < count; i++) {
			min = myArray[i];
			for (int j = i + 1; j < count; j++) {
				if (myArray[j] < min) {
					min = myArray[j];
					tempValue = myArray[i];
					tempIndex = j;
					alreadySorted = true;
				}
			}
			BubbleSwap(min, tempIndex, tempValue, alreadySorted, i);
		}
	}

	/**
	 * Helper method for BubbleSort
	 * 
	 * @param min
	 * @param tempIndex
	 * @param tempValue
	 * @param alreadySorted
	 * @param i
	 */
	private void BubbleSwap(double min, int tempIndex, double tempValue, boolean alreadySorted, int i) {
		if (min != i && alreadySorted == true) {
			myArray[tempIndex] = tempValue;
			myArray[i] = min;
		}
	}

	/**
	 * Bubble sort for an array of symbols containing double values. Sorts from min
	 * to max
	 * 
	 * @param symArray symbol array to sort
	 * @return sorted array
	 */
	public Symbol[] SymbolBubbleSortLowToHigh(Symbol[] symArray) {
		double min;
		double tempValue = 0;
		int tempIndex = 0;
		Symbol minS;
		Symbol tempValueS = null;
		int tempIndexS = 0;
		boolean alreadySorted = false;
		for (int i = 0; i < count; i++) {
			min = myArray[i];
			minS = symArray[i];
			for (int j = i + 1; j < count; j++) {
				if (myArray[j] < min) {
					min = myArray[j];
					tempValue = myArray[i];
					tempIndex = j;
					minS = symArray[j];
					tempValueS = symArray[i];
					tempIndexS = j;
					alreadySorted = true;
				}
			}
			if (min != i && alreadySorted == true) {
				symArray[tempIndexS] = tempValueS;
				symArray[i] = minS;
				myArray[tempIndex] = tempValue;
				myArray[i] = min;
			}
		}
		return symArray;
	}

	/**
	 * Bubble sort for an array of symbols containing double values. Sorts from max
	 * to min
	 * 
	 * @param symArray symbol array to sort
	 * @return sorted array
	 */
	public Symbol[] SymbolBubbleSortHighToLow(Symbol[] symArray) {
		double max;
		double tempValue = 0;
		int tempIndex = 0;
		Symbol maxS;
		Symbol tempValueS = null;
		int tempIndexS = 0;
		boolean alreadySorted = false;
		for (int i = 0; i < count; i++) {
			max = myArray[i];
			maxS = symArray[i];
			for (int j = i + 1; j < count; j++) {
				if (myArray[j] > max) {
					max = myArray[j];
					tempValue = myArray[i];
					tempIndex = j;
					maxS = symArray[j];
					tempValueS = symArray[i];
					tempIndexS = j;
					alreadySorted = true;
				}
			}
			if (max != i && alreadySorted == true) {
				symArray[tempIndexS] = tempValueS;
				symArray[i] = maxS;
				myArray[tempIndex] = tempValue;
				myArray[i] = max;
			}
		}
		return symArray;
	}

	/**
	 * Main method of quick sort algorithm in recursive form
	 * 
	 * @param array Array to sort
	 * @param left  current of the array
	 * @param right Length of the array
	 */
	public void QuickSort(double[] array, int left, int right) {
		if (left < right) {
			int part = QuickSortPartition(array, left, right);

			QuickSort(array, left, part - 1);
			QuickSort(array, part + 1, right);
		}
	}

	/**
	 * Method that does the sorting by swapping using a pivot number
	 * 
	 * @param array Array to sort
	 * @param left  current of the array
	 * @param right Length of the array
	 * @return
	 */
	private int QuickSortPartition(double[] array, int left, int right) {
		double pivotNumber = array[right];
		int i = (left - 1);

		for (int j = left; j <= right - 1; j++) {
			if (array[j] < pivotNumber) {
				i++;
				Swapper(array, i, j);
			}
		}
		Swapper(array, i + 1, right);
		return (i + 1);
	}

	/**
	 * Method that assists partition method
	 * 
	 * @param array Array being sorted
	 * @param i     value at index i to be swapped with j
	 * @param j     value at index j to be swapped with i
	 */
	private void Swapper(double[] array, int i, int j) {
		double temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}

	/**
	 * Look for a specific value in the array
	 * 
	 * @param value
	 * @return
	 */
	public double Search(double value) {
		for (int i = 0; i < count - 1; i++) {
			if (myArray[i] == value) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Recursive Binary search
	 * 
	 * @param toSearch Insert value to look for
	 * @return
	 */
	public double BinarySearch(double toSearch, int left, int right) {
		if (right >= 1) {
			int middlePoint = left + (right - 1) / 2;

			if (myArray[middlePoint] == toSearch)
				return myArray[middlePoint];

			if (myArray[middlePoint] > toSearch)
				return BinarySearch(toSearch, left, middlePoint - 1);
			else
				return BinarySearch(toSearch, middlePoint + 1, right);
		}
		JOptionPane.showMessageDialog(null, "Sorry could not locate the value");
		return -1;

	}

	/**
	 * Remove value at specific index
	 * 
	 * @param index insert index to remove
	 * @return value that was removed
	 * @throws Exception if the list is empty throws exception
	 */
	public double RemoveIndex(int index) throws Exception {
		if (index > count - 1) {
			return -1;
		}
		if (ListEmpty()) {
			throw new Exception("Collection is empty");
		}
		double val = myArray[index];

		for (int i = index; i < count - 1; i++) {
			if (index == count - 1) {
				i = count;
				return --count;
			}
			myArray[i] = myArray[i + 1];
		}

		count--;
		return val;
	}

	/**
	 * Remove desired value from the array
	 * 
	 * @param value to remove from the array
	 * @return true if found false if not found
	 * @throws Exception throws exception if list was empty
	 */
	public boolean Remove(int val) throws Exception {
		if (ListEmpty()) {
			throw new Exception("Collection is empty");
		}
		BubbleSort();
		int left = 0;
		int right = count - 1;
		while (left <= right) {
			int pos = (left + right) / 2;
			if (myArray[pos] == val) {
				RemoveIndex(pos);
				return true;
			}
			if (myArray[pos] < val) {
				left = pos + 1;
			} else
				right = pos - 1;
		}
		return false;
	}

	/**
	 * Checks if list is full
	 * 
	 * @return true if list is full false if not
	 */
	public boolean CheckIfFull() {
		return (count >= myArray.length);
	}

	/**
	 * Check if list is empty
	 * 
	 * @return true if list is empty false if not
	 */
	public boolean ListEmpty() {
		return (count == 0);
	}

	/**
	 * Empties the list content
	 */
	public void Clear() {
		count = 0;
	}

	/**
	 * Adds value to the start of the collection
	 * 
	 * @param value to insert
	 * @throws Exception if the collection is full
	 */
	public void AddStart(int val) throws Exception {
		if (CheckIfFull()) {
			throw new Exception("Collection is full");
		}
		for (int i = count; i > 0; i--) {
			myArray[i] = myArray[i - 1];
		}
		myArray[0] = val;
		count++;
	}

	/**
	 * Adds value to the end of the collection
	 * 
	 * @param value to insert
	 * @throws Exception if the collection is full
	 */
	public void AddEnd(double val) throws Exception {
		if (CheckIfFull()) {
			throw new Exception("Collection is full");
		}
		myArray[count] = val;
		count++;
	}

	/**
	 * Removes value at the end of the array
	 * 
	 * @return the removed value
	 * @throws Exception if Collection is empty
	 */
	public double removeLast() throws Exception {
		if (ListEmpty()) {
			throw new Exception("Collection is empty");
		}
		double val = myArray[count - 1];
		count--;
		return val;
	}

	/**
	 * Remove the value at the start of the array
	 * 
	 * @return removed value
	 * @throws Exception if the collection was empty
	 */
	public double removeFirst() throws Exception {
		if (ListEmpty()) {
			throw new Exception("Collection is empty");
		}
		double val = myArray[0];

		for (int i = 0; i < count - 1; i++) {
			myArray[i] = myArray[i + 1];
		}

		count--;
		return val;
	}

}
