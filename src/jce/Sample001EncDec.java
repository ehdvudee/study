package jce;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;

public class Sample001EncDec {
	public static void main( String[] args ) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, ShortBufferException {
		Cipher cipher = Cipher.getInstance( "AES/ECB/PKCS5Padding" );
		byte[] keyBytes = SecureRandom.getInstance( "SHA1PRNG" ).generateSeed( 16 );
		
		byte[] plaintext = "asjrnoxaldkwjjifeahiwefiphaweiphfaweihpfaewihpfaehpifaweihpafwepihrjwwdwieihfhihiveihevjefoefo".getBytes();
		
		int offset = 10;
		int length = 32;
		byte[] dest = new byte[1024];
		
		SecretKey key = new SecretKeySpec( keyBytes, "AES" );
		cipher.init( Cipher.ENCRYPT_MODE, key );
		
		int destOffset = cipher.doFinal( plaintext, offset, length, dest );
		byte[] ciphertext = cipher.doFinal( plaintext );
		
		System.out.println( new String(plaintext) );
		
		cipher.init( Cipher.DECRYPT_MODE, key );
		int destOffset2 = cipher.doFinal( ciphertext, offset, length, dest, destOffset );
		
		System.out.println( destOffset2 );
	}
}	
