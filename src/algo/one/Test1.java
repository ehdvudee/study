package algo.one;

import java.util.ArrayList;
import java.util.List;

public class Test1 {
	public static void main( String[] args ) {
		int arr1[] = {1,2,3,4,5};
		int arr2[] = {2,1,2,3,2,4,2,5};
		int arr3[] = {3,3,1,1,2,2,4,4,5,5};
		
		int ret[] = new int[3];
		
		int sol[] = {1,3,2,4,2};
		List<Integer> ret2 = new ArrayList<Integer>();

		for ( int i=0; i<sol.length; i++ ) {
			
			if ( arr1[i % arr1.length] == sol[i] ) ret[0]++;
			if ( arr2[i % arr2.length] == sol[i] ) ret[1]++;
			if ( arr3[i % arr3.length] == sol[i] ) ret[2]++;
		}
		
		int maxVal = 0;
		for ( int i=0; i<3; i++ ) {
			if ( ret[i] > maxVal ) { 
				maxVal = ret[i];
			}
		}
		
		for ( int i=0; i<3; i++ ) {
			if ( maxVal == ret[i] ) ret2.add(i); 
		}
		int[] a = ret2.stream().mapToInt( i->i+1).toArray();

		for ( int i=0; i<3; i++ ) {
			System.out.println( a[i]);
		}
	}
}
