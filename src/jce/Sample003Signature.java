package jce;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import jce.util.DataTypeUtil;
import jce.util.KeyStoreUtil;

public class Sample003Signature {
	public static void main( String[] args ) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException, UnrecoverableEntryException, KeyStoreException, CertificateException {
		SecureRandom secureRandom = new SecureRandom();
		SecureRandom.getInstance( "SHA1PRNG" );
//		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance( "DSA" );
//		keyPairGenerator.initialize( 2048 , SecureRandom.getInstance( "SHA1PRNG" ) );
//		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		
		KeyStoreUtil ksu = KeyStoreUtil.getInstance( "123".toCharArray(), "test/user.jks");
		KeyPair keyPair = ksu.getKeyPair( "1234".toCharArray(), "dev1" );
		
		Signature signature = Signature.getInstance( "SHA256WithRSA" );
		
		signature.initSign( keyPair.getPrivate(), secureRandom);
		
		byte[] sha1 = new byte[20];
		SecureRandom sr = SecureRandom.getInstance( "SHA1PRNG" );
		sr.nextBytes( sha1 );
		
		signature.update( sha1 );
		
		System.out.println( DataTypeUtil.convertByteArrayToHexString( sha1 ) );
		
		byte[] digitalSignature = signature.sign();
		
		System.out.println( DataTypeUtil.convertByteArrayToHexString( digitalSignature) );
		
		signature.initVerify( keyPair.getPublic() );
		
		signature.update( sha1 );
		
		boolean verified = signature.verify( digitalSignature );
		
		System.out.println("verified = " + verified );
		
		
	}
}
