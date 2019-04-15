package algo.one;

public class Festival {
	public static void main( String[] args ) {
		int n = 6;
//		int l = 3;
		int l = 2;
		int[] sCash = {1,2,3,1,2,3};
		int sum = 0;
		float min = 1000;
		
		for ( int i=0; i<=sCash.length-l; i++ ) {
			for ( int j=i; j<sCash.length; j++ ) {
				sum = sum + sCash[j];
				
				if ( j >= (l-1) ) {
					float mSum = sum / (j + 1f );
//					System.out.println(mSum);
					if ( min > mSum ) {
						min = mSum;
					}
				}
			}
		}
		
		System.out.println(min);
	}
}
