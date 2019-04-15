package jce;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Sample004MessageDigest {
	public static void main( String[] args ) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		
		byte[] plain = "wefiwefifehiw".getBytes();
		byte[] digest = messageDigest.digest( plain );
		
		System.out.println( Arrays.toString( digest ) );
	}
}
