package jce;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Sample007KeyGen {
	public static void main( String[] args ) throws NoSuchAlgorithmException {
	
		//gen symmetric key
		KeyGenerator keyGenerator = KeyGenerator.getInstance( "AES" );
		SecureRandom secureRandom = new SecureRandom();
		int keyBitSize = 256;
		
		keyGenerator.init( keyBitSize, secureRandom );
		
		SecretKey secretKey = keyGenerator.generateKey();
		
		System.out.println( Arrays.toString( secretKey.getEncoded() ) );
		
		//gen asymmetric key
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance( "RSA" );
		keyPairGenerator.initialize( 2048 );
		
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		
		
	}
}
