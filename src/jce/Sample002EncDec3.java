package jce;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Sample002EncDec3 {
	public static void main( String[] args ) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
		
		System.out.println(Cipher.getMaxAllowedKeyLength("AES"));
		
		KeyGenerator keyGenerator = KeyGenerator.getInstance( "AES" );
		
		SecureRandom secureRandom = new SecureRandom();
		int keyBitSize = 128;
		keyGenerator.init( keyBitSize, secureRandom );
		
		SecretKey secretKey1 = keyGenerator.generateKey();
		
		Cipher cipher = Cipher.getInstance( "AES/ECB/PKCS5Padding" );
		 
		cipher.init( Cipher.ENCRYPT_MODE, secretKey1 );
		
		byte[] ciphertext = cipher.doFinal( "2222".getBytes() );
		
		cipher.init( Cipher.DECRYPT_MODE, secretKey1 );
		System.out.println( new String( cipher.doFinal( ciphertext ) ) );
		
	}
}
