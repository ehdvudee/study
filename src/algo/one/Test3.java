package algo.one;

public class Test3 {
	static int total=0;
	public static void main( String[] args ) {
		int[][] computers = {{1, 1, 0}, {1, 1, 0}, {0, 0, 1}};
		int n = 3;
		
		solution( n, computers );
	}
	
	public static void solution( int n, int[][]computers ) {
		boolean visited[] = new boolean[n];
		for ( int i=0; i<n; i++ ) {
			if(!visited[i]) {
				dfs( n, computers, i, visited );
				total++;
			}
		}
	}
	
	public static void dfs( int n, int[][] computers, int idx, boolean[] visited ) {
		visited[idx] = true;
		for ( int i=0; i<n; i++ ) {
			if ( idx == i ) continue;
			if ( computers[idx][i] == 1 && !visited[i] ) {
				dfs(n, computers, i, visited);
			}
		}
	}
}
