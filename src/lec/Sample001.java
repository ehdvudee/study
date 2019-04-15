package lec;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class Sample001 {
	public static void main( String[] args ) throws NoSuchAlgorithmException {


		

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
		
		
	}
	
	private static void test002() {
		// 1. A와 B의 공통점을 출력하시오. 
		// ex1) A: 11011101 B: 11110111 => 답 11010101
		// ex2) A: 01001001 C: 10110111 => 답 00000001
		
		int a = 109;
		int b = 175;
	}
	
	private static void test001() {
		// 2. XOR를 하여 "!문자!"를 출력하고 저에게 읽어주세요.
		byte[] bytesA = convertHexStringToByteArray( "e633455ff760896f55b2ae04d1c9e302" );
		byte[] bytesB = convertHexStringToByteArray( "9156653c960ea90b3a92c770f0e8c223" );
		
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
