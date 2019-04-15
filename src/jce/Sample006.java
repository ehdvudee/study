package jce;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.SecretKey;

public class Sample006 {
	public static void main( String[] args ) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		Signature signature = Signature.getInstance( "SHA256WithDSA" );
		
		SecureRandom secureRandom = new SecureRandom();
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance( "DSA" );
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		byte[] data = "wfejopifwejpowef".getBytes();
		
		signature.initSign( keyPair.getPrivate(), secureRandom );		
		signature.update( data );
		byte[] digitalSignature = signature.sign();
		
		signature.initVerify( keyPair.getPublic() );
		signature.update( data );
		System.out.println( signature.verify( digitalSignature ) );
		
	}
}
