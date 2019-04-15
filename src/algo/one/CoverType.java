package algo.one;

public class CoverType {
	static int tot=0;
	public static void main( String[] args ) {
		int h = 8;
		int w = 10;
//		char[][] cover = {
//				{'#','.','.','.','.','.','#'},
//				{'#','.','.','.','.','.','#'},
//				{'#','#','.','.','#','#','#'}
//		};
		
		char[][] cover = {
				{'#','#','#','#','#','#','#','#','#','#'},
				{'#','.','.','.','.','.','.','.','.','#'},
				{'#','.','.','.','.','.','.','.','.','#'},
				{'#','.','.','.','.','.','.','.','.','#'},
				{'#','.','.','.','.','.','.','.','.','#'},
				{'#','.','.','.','.','.','.','.','.','#'},
				{'#','.','.','.','.','.','.','.','.','#'},
				{'#','#','#','#','#','#','#','#','#','#'}
				
		};
		
		int[][] coverType= {
				{-1,-1},
				{1,-1},
				{1,1},
				{-1,1}
		};

		boolean[][] visited = new boolean[h][w];
		dfs( coverType, 0, 0, visited, w, h, cover );
		System.out.println(tot);
	}
	
	public static void dfs( int[][] coverType, int x, int y, boolean[][]visited, int w, int h, char[][] cover ) {
		if ( x == (h-1) && y == (w-1) ) {
			int to = 0;
			for ( int i=0; i<h; i++ ) {
				for ( int j=0; j<w; j++ ) {
//					System.out.print( cover[i][j] + " ");
					if ( cover[i][j] == '#' ) to++;
				}
//				System.out.println();
			}
//			
			if ( to == (h*w) ) {
				tot++;
				System.out.println(tot);
			}
			
//			System.out.println();
			return;
		}
		
		for (/*coverType*/ int k=0; k<4; k++ ) {
			
			int dirX = x+coverType[k][0];
			int dirY = y+coverType[k][1];
			if ( (dirX < 0 || dirY < 0 ) || ( dirX >= h || dirY >= w ) ) continue;
			if ( cover[dirX][y] == '.' && cover[x][dirY] =='.' && cover[x][y] == '.' ) {
				
				char[][] rCover = new char[cover.length][];
				for ( int j=0; j<cover.length; j++ ) {
					rCover[j] = cover[j].clone();
				}
				
				rCover[dirX][y] = '#';
				rCover[x][dirY] = '#';
				rCover[x][y] = '#';
				
				if ( y < (w-1) ) dfs( coverType, x, y+1, visited, w, h, rCover );
				else if ( x < (h-1) ) dfs( coverType, x+1, 0, visited, w, h, rCover );
			} 
		}
		
		if ( y < (w-1) ) dfs( coverType, x, y+1, visited, w, h, cover );
		else if ( x < (h-1) ) dfs( coverType, x+1, 0, visited, w, h, cover );
		
	}
}
