package algo.one;

public class FullySearch_1 {
	static int arr[] = {1,2,3,4};
	static int result[] = new int[4];
	static boolean visited[] = new boolean[4];
	static int combinationCnt = 0;
	
	public static void main( String[] args ) {
		recur( 0 );
	}

	public static void recur( int idx ) {
		if ( idx == 4 ) {
			for ( int i=0; i<4; i++ ) {
				System.out.print( result[i] + " " );
			}
			combinationCnt++;
			System.out.println( "  combi: " + combinationCnt );
			System.out.println();
			return;
		}
		
		for( int i=0; i<4; i++ ) {
			if ( !visited[i] ) {
				result[idx] = arr[i];
				visited[i] = true;
				recur( idx + 1 );
				visited[i] = false;
			}
		}
	}
}
