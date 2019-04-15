package lec;

public class Sample002RightAnser {
	public static void main( String[] args ) {
		tst004Ans1();
		System.out.println();
		tst004Ans2();
		
	}

	private static void tst004Ans1() {
		String plain = "aA bB xX zZ";
		int sht = 35;
		
		for ( char ch : plain.toCharArray() ) {
			char posi = 0;

			// 유효성 검사 및 필요 값 파싱
			if ( (ch >= 65 && ch <= 90 /*upper case */) ) posi = 65;
			else if ( (ch >= 97 && ch <= 122 /*lower case*/) ) posi = 97;
			else if ( ch == 32 ) posi = 32;
			else throw new IllegalArgumentException( "this is not a alphabet or space." );
			
			if ( posi >= 33 ) System.out.print( (char) ((((ch + sht) - posi ) % 26) + posi) );
			else System.out.print(" ");
		}
	}
	
	private static void tst004Ans2() {
		String plain = "aA bB xX zZ";
		int sht = 35;
		
		for ( char ch : plain.toCharArray() ) {
			char originChar = ch;
			ch = (char)(ch + (sht % 26));
			
			if ( (originChar <= 90 && ch > 90) || (originChar <= 122 && ch > 122) ) ch = (char) (ch - 26);
			
			// 유효성 검사 및 출력
			if ( originChar == 32) System.out.print(" ");
			else if ( (ch >= 65 && ch <= 90 /*upper case */) 
					|| (ch >= 97 && ch <= 122 /*lower case*/) ) 
				System.out.print( ch );
			else throw new IllegalArgumentException( "this is not a alphabet or space." );
		}
	}
	
	// responsed 
	private static void test004() {
		// 시저 암호 만들어보세요.
		// 아스키 코드
		// 대문자 소문자, 알파벳 갯수 26개
		int a = 8; //평행이동 수
		String str = "aAbBxXzZ";
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			char chh = (char) (ch + a);
			int chhh = (int) chh;
			//합치기
			//대문자
			if (65 <= chhh && chhh <= 90 + a) {
				if (chhh > 90) {
					int chhhh = chhh - 26;
					System.out.print((char) chhhh);
				} else {
					System.out.print((char) chhh);
				}
				//소문자
			} else if (97 <= chhh && chhh <= 122 + a) {
				if (chhh > 122) {
					int chhhh = chhh - 26;
					System.out.print((char) chhhh);
				} else {
					System.out.print((char) chhh);
				}
			}
		}
	}
}
