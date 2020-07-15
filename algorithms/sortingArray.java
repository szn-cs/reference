// Simple quick implementation of array sorting.
//Write a method to sort an array of integers from smallest to largest.

import java.util.*;

public class Main {

	public static int[] sort(int[] array) {
		boolean sorted;
		do {
			sorted = true; // assumption
			for(int i=0; i < array.length- 1; i++) {
				if(array[i] > array[i+1]) {
					int temp = array[i+1]; 
					array[i+1] = array[i]; 
					array[i] = temp;
					sorted = false;
				}					
			}
		} while(!sorted);
		return array;
	}
	public static void main( String [] args) {
		int[] a = new int[] {2,7,1,3,2,8};
		
		a = sort(a);
			
		for (int element: a) {
			System.out.print(element + " ");
		}

	}	
}

