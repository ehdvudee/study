//package algo.two;
//
//public class Test1 {
//	public static void main( String[] args ) {
//		int[][] paper = {
//				{0, 0, 0, 1, 1, 1, -1, -1, -1},
//				{0, 0, 0, 1, 1, 1, -1, -1, -1},
//				{0, 0, 0, 1, 1, 1, -1, -1, -1},
//				{1, 1, 1, 0, 0, 0, 0, 0, 0},
//				{1, 1, 1, 0, 0, 0, 0, 0, 0},
//				{1, 1, 1, 0, 0, 0, 0, 0, 0},
//				{0, 1, -1, 0, 1, -1, 0, 1, -1},
//				{0, -1, 1, 0, 1, -1, 0, 1, -1},
//				{0, 1, -1, 1, 0, -1, 0, 1, -1}
//		};
//		int n = 9;
//		
//		
//	}
//	
//	public static void calc( int x, int y, int forVal, int[][] paper ) {
//		if ( (forVal / 3) != 1 ) calc( forVal / 3, paper ); 
//		
//		int cVal = 0;
//		for ( int i=x; i<forVal; i++ ) {
//			for ( int j=y; j<forVal; j++ ) {
//				if ( i ==0 && j == 0 ) cVal = paper[i][j];
//				if ( (i != 0 && j != 0) || cVal != paper[i][j] ) {
//					
//					
//				}
//			}
//		}
//	}
//}
