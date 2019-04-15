package pbkdf.sample;

import pbkdf.api.Pbkdf2Util;

public class Sample001 {
	public static void main( String[] args ) {
		
		byte[] pbkdf;
		
		pbkdf = new Pbkdf2Util.Builder().password( "password".getBytes() )
				.salt( "salt".getBytes() )
				.iteration( 1 )
				.derivedKeyLength( 20 )
				.deriveAlgoritm( "HmacSHA1")
				.build()
				.derivKey();
		
		System.out.println( byteaToHex( pbkdf ) );
	}
	
	public static String byteaToHex( byte[] bytea ) {
		StringBuilder sb = new StringBuilder();
		for( byte b : bytea ) {
			sb.append(String.format("%02x", b&0xff));
		}
		
		return sb.toString();
	}
}
