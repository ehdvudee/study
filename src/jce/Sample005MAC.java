package jce;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Sample005MAC {
	public static void main( String[] args ) throws NoSuchAlgorithmException, InvalidKeyException {
		byte[] keyBytes = SecureRandom.getInstance("SHA1PRNG").generateSeed( 16 );
		String algorithm = "RawBytes";
		byte[] data = "afwohifawehiawefawef".getBytes();
		
		
		Mac mac = Mac.getInstance( "HmacSHA256" );
		
		SecretKey key = new SecretKeySpec( keyBytes, algorithm );
		
		mac.init( key );
		byte[] macBytes = mac.doFinal( data );
		
		System.out.println( data );
		System.out.println( Arrays.toString( macBytes ) );
		System.out.println( Arrays.toString( mac.doFinal( data ) ));
		
	}
}
