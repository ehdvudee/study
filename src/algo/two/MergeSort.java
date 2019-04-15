package algo.two;

import java.util.Arrays;

public class MergeSort {
	static int[] tmpArr;
	
	public static void main( String[] args ) {
		int[] arr = {6,5,3,1,8,7,2,4};
		tmpArr = new int[arr.length];
		divide( arr, 0, 7 );
		System.out.println( Arrays.toString( tmpArr) );
	}
	
	public static void divide( int[] arr, int start, int end ) {
		if ( end > start ) {
			System.out.println( "s : " + start + " e : " + end );
			divide( arr, start, (start + end) / 2 );
			divide( arr, (start + end) / 2 + 1, end );
		}
		
		merge( arr, start, end );
	}
	
	public static void merge( int[] arr, int start, int end ) {
		int i= start;
		int j = (start + end) / 2 + 1;
		int cnt = start;
		
		while ( i<= ( start + end ) / 2 && j <= end ) {
			if ( arr[i] > arr[j] )
				tmpArr[cnt++] = arr[j++];
			else 
				tmpArr[cnt++] = arr[i++];
		}
		while ( i<= ( start + end ) / 2 )
			tmpArr[cnt++] = arr[i++];
		while( j <= end ) 
			tmpArr[cnt++] = arr[j++];
		for ( i=start; i<=end; i++ ) 
			arr[i] = tmpArr[i];
		
		System.out.println( Arrays.toString( tmpArr ) );
	}
}
