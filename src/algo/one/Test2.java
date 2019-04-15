package algo.one;

public class Test2 {
	
	private static int total = 0;
	public static void main( String[] args ) {
		int[] numbers = {1,1,1,1,1};
		int target = 3;
		
		solution( numbers, target );
		
		System.out.println( total );
	}
	
	public static void solution( int[] numbers, int target ) {
		dfs( numbers, target, 0, 0 );
	} 
	
	public static void dfs( int[] numbers, int target, int index, int val ) {
		if ( index == numbers.length ) {
			if( val == target ) {
				total++;
			}
		} else {
			dfs( numbers, target, index+1, val + numbers[index] );
			dfs( numbers, target, index+1, val - numbers[index] );
			
		}
	}
}
