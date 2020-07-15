// basic search algorithm implementation.
package sample;
import java.util.*;

public class Main {

	// Write a method to search through a sorted array of integers for a specific value.
	public static int search(int value, int[] array) {
		int index = 0; 
		int maxIndex = array.length-1; // range of indexes
		
		do {
			int implicitRangeLength = (maxIndex - index) + 1; 
			int middleIndex = index + (implicitRangeLength / 2) -1;
			if(value > array[middleIndex])
				index = middleIndex + 1;
			else 
				maxIndex = middleIndex;
		} while(maxIndex != index);
	
		if(array[index] == value)
			return index; 
		else 
			return -1;
	}
	
	public static void main( String [] args) {
		int[] array = new int[] {1,2,2,3,7,8}; // sorted array	
		int value = 7; 
		int index = search(value, array);
		System.out.println("index of value in: " + index);
	}




	
}

