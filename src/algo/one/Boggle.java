package algo.one;

public class Boggle {
	private static int[] dx = {-1, -1, -1, 1, 1, 1, 0, 0 };
	private static int[] dy = {-1, 0, 1, -1, 0, 1, -1, 1};
	private static char[][] boardA ={
			{ 'U', 'R', 'L', 'P', 'M'},
			{ 'X', 'P', 'R', 'E', 'T'},
			{ 'G', 'I', 'A', 'E', 'T'},
			{ 'X', 'T', 'N', 'Z', 'Y'},
			{ 'X', 'O', 'Q', 'R', 'S'}
		};
	private static char[][] word = {
			{'P','R','E','T','T','Y'},
			{'G','I','R','L'},
			{'R','E','P','E','A','T'},
			{'K','A','R','A'},
			{'P','A','N','D','O','R','A'},
			{'G','I','A','Z','A','P','X'}
	};
	
	private static boolean[] ret = new boolean[word.length];
	private static boolean[] visited;
	
	public static void main( String[] args ) {
		for ( int i=0; i<word.length; i++ ) {
			visited = new boolean[word[i].length];
			
			for ( int j=0; j<5; j++ ) {
				for ( int k=0; k<5; k++ ) {
					if ( word[i][0] == boardA[j][k] ) {
						visited[0] = true;
						dfs( j, k, word, i, visited );
					}
				}
			}
			
			int tmpCnt=0;
			for ( int l=0; l<visited.length; l++ ) {
				if ( visited[l] == true ) tmpCnt++; 
				System.out.print( word[i][l] );
				if ( visited.length == ( l + 1 ) && visited.length == tmpCnt ) System.out.println( " YES" );
				else if ( visited.length == ( l + 1 ) )  System.out.println( " NO" );
			}
		}
	}
	
	public static void dfs( int x, int y, char[][]word, int wordSeq, boolean[] visited ) {
		for ( int i=0; i<8; i++ ) {
			int searchDx = dx[i] + x;
			int searchDy = dy[i] + y;
			if ( (searchDx >= 0 && searchDy >= 0 ) && ( searchDx <= 4 && searchDy <= 4 ) ) {
				for ( int j=1; j<word[wordSeq].length; j++ ) {
					if ( visited[j] == false && word[wordSeq][j] == boardA[searchDx][searchDy] ) {
						visited[j] = true;
						dfs( searchDx, searchDy, word, wordSeq, visited );
					}
				}
			}
				
		}
	}
}
