package jce;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Sample001EncDec2 {
	 public static void main( String[] args ) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {

		 System.out.println( Arrays.toString( SecureRandom.getInstance("SHA1PRNG").generateSeed(20) ) );
		 System.out.println( Arrays.toString( SecureRandom.getInstance("SHA1PRNG").generateSeed(16) ) );
		 System.out.println( Arrays.toString( SecureRandom.getInstance("SHA1PRNG").generateSeed(18) ) );
		 byte[] keyBytes = SecureRandom.getInstance("SHA1PRNG").generateSeed(16);
		 byte[] iv = SecureRandom.getInstance("SHA1PRNG").generateSeed(16);
		 AlgorithmParameterSpec parameter = new IvParameterSpec( iv );
		 
		 Cipher cipher = Cipher.getInstance( "AES/CBC/PKCS5Padding" );
		 String algorithm = "AES";
		 SecretKeySpec key = new SecretKeySpec( keyBytes, algorithm );
		  
		 cipher.init( Cipher.ENCRYPT_MODE, key, parameter );
		 
		 byte[] plaintext = "십".getBytes("UTF-8");
		 byte[] ciphertext = cipher.doFinal( plaintext );
		 
		 System.out.println( "plaintext : " + new String( plaintext, "UTF-8" ) );
		 System.out.println( "ciphertext : " + Arrays.toString( ciphertext ) );
		 
		 cipher.init( Cipher.DECRYPT_MODE, key, parameter );
		 
		 byte[] newPlaintext = cipher.doFinal( ciphertext );
		 System.out.println( "plaintext : " + new String( newPlaintext, "UTF-8") );
		 
	 }
}
