package lec;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class Sample001RightAnswer {
	public static void main( String[] args ) throws NoSuchAlgorithmException {
		test003();
	}
	
	private static void test003() {
		// 1. 1111 2진수가 있다.
		// 2. 왼쪽으로 부터 '콜라', '사이다', '센드위치', '주먹밥' 이다.
		// 3. ex1) A 가게(1101)는 콜라와 사이다 주먹밥만 판매한다.
		// 4. ex2) B 가게(1001)는 콜라와 주먹밥만 판매한다.
		// 5. AA라는 손님은 콜라와 센드위치만 먹는다(1010).
		// 6. BB라는 손님은 콜라 사이다 샌드위치만 먹는다(1110).
		// 7. AA라는 손님이 B 가게에 가면 아래와 같이 출력되어야 한다.
		// 8. 콜라 감사, 사이다 안먹어, 샌드위치 없어, 주먹밥 안먹어 
		// 9. 총 감사, 안먹어, 없어 3가지를 출력한다.
		
		int storeA = 13;
		int storeB = 9;
		int customerA = 10;
		int customerB = 14;
		
		Map<Integer, String> foodMap = new HashMap<>();
		foodMap.put( 8, "cola" );
		foodMap.put( 4, "cida" );
		foodMap.put( 2, "send" );
		foodMap.put( 1, "bob" );
				
		print( storeA, customerA, foodMap );
		
	}
	
	private static void print( int store, int person, Map<Integer, String> foodMap ) {
		for ( int food=8; food!=0; food=food/2  ) {
			if ( ( person & food ) == food && (store & food) == food ) System.out.print( foodMap.get( food )  + " 감사 " ); 
			else if ( (person & food ) == food && (store & food) != food ) System.out.print( foodMap.get( food ) + "없어 " );
			else if ( (person & food ) != food ) System.out.print( foodMap.get( food ) + "안먹어 " );
		}
		
		System.out.println();
	}
	
	private static void test002() throws NoSuchAlgorithmException {
		byte[] plain = "we can do it!!!!".getBytes();
		byte[] key = new byte[16];
		byte[] cipher;
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		sr.setSeed( 16 );
		sr.nextBytes( key );	
	
		System.out.println( "키 : " + convertByteArrayToHexString( key ) );
		System.out.println( "암호문 : " + convertByteArrayToHexString( (cipher=xorBytes( key, plain )) ) );
		System.out.println( "평문(암호문 ^ 키 ) : " + new String(  xorBytes( key, cipher ) ) );
	}
	
	private static byte[] xorBytes( byte[] bytesA, byte[] bytesB ) {
		if ( bytesA.length != bytesB.length ) throw new IllegalArgumentException( "a, b 길이 다름" );
		byte[] retBytes = new byte[bytesA.length];
		
		for ( int i=0; i<bytesA.length; i++ ) {
			retBytes[i] = (byte) (bytesA[i] ^ bytesB[i]);
		}
		
		return retBytes;
	}
	
	//UTILS
	private static byte[] convertHexStringToByteArray( String hexString ) {
		int len = hexString.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ( (Character.digit( hexString.charAt(i), 16 ) << 4 )
					+ Character.digit( hexString.charAt(i+1), 16 ) );
		}
		
		return data;
	}
	
	//UTILS
	private static String convertByteArrayToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for(byte b : bytes) {
			sb.append(String.format("%02x", b&0xff));
		}
		return sb.toString();
	}
}
