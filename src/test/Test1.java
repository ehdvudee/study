package test;

public class Test1 {
	
	public static void main( String[] args ) {
		
		int j=1;
		int temp;
		int temp2;
		System.out.print("0\t0");
		System.out.println();
		for ( int i=1; i < 10240; i++ ) {
			System.out.print( "DB 값 : " + i );
			
			System.out.print( "\t 캐리연산 : " + ( temp = testa(i) ) );
			
			if ( j % 256 == 0 ) {
				j = 1;
			}
			if ( temp != j ) {
				System.out.println("망");
				break;
			}
			
			System.out.print( "\t 스태틱 : " + j );
			System.out.print( "\t 언캐리 : " + ( temp2 = testb( i, j ) ) );
			System.out.println();
			
			if ( temp2 != i) {
				System.out.println("망");
				break;
			}
			j++;
		}
	}
	
	// carry
	public static int testa ( int i ) {
		if ( i >= 256 ) 
			return testa( i % 256 + i / 256 );
		return i % 256 + i / 256 ;
	}
	
	// unnestcarry
	public static int testb ( int i, int j ) {
		if ( i == 10202 ) { 
			System.out.print(""); 
			}
		int carryCnt = i / 255;
		if ( carryCnt == 0 ) 
			return i;
		if ( i % 255 == 0 )
			return carryCnt * 255;
		return carryCnt * 255 + j;
	}
}
