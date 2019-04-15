package jce.util;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class SignatureUtil {
	
	private static final String DEFAULT_SIGN_ALGORITHM = "SHA256WithDSA";
	private static final String DEFAULT_KEY_ALGORITHM = "DSA";
	
	private String algorithm;
	private KeyPair keyPair;
	private SecureRandom sRandom;
	private Provider provider;
	
	// SIGNATURE 생성자
	private SignatureUtil( String algorithm, KeyPair keyPair, SecureRandom sRandom, String provider ) {
		this.algorithm = algorithm;
		this.keyPair = keyPair;
		this.sRandom = sRandom;
		
		if ( provider != null ) {
			this.provider = Security.getProvider( provider );
		}
	}
	
	public static SignatureUtil getSignatureInstance( byte[] privatKey, byte[] publicKey ) throws InvalidKeySpecException, NoSuchAlgorithmException {
		return getSignatureInstance( privatKey, publicKey, DEFAULT_KEY_ALGORITHM, DEFAULT_SIGN_ALGORITHM, null );
	}
	
	public static SignatureUtil getSignatureInstance( byte[] privatKey, byte[] publicKey, String provider ) throws InvalidKeySpecException, NoSuchAlgorithmException {
		return getSignatureInstance( privatKey, publicKey, DEFAULT_KEY_ALGORITHM, DEFAULT_SIGN_ALGORITHM, provider );
	}
	
	public static SignatureUtil getSignatureInstance( byte[] privateKey, byte[] publicKey, String keyAlgorithm, String algorithm, String provider ) throws InvalidKeySpecException, NoSuchAlgorithmException {
		PublicKey pubKey = KeyFactory.getInstance( keyAlgorithm ).generatePublic( new X509EncodedKeySpec( publicKey ) );
		PrivateKey priKey = KeyFactory.getInstance( keyAlgorithm ).generatePrivate( new PKCS8EncodedKeySpec( privateKey ) );
		
		return getSignatureInstance( algorithm, new KeyPair( pubKey, priKey ), new SecureRandom(), provider );
	}
	
	public static SignatureUtil getSignatureInstance( KeyPair keyPair ) {
		return getSignatureInstance( DEFAULT_SIGN_ALGORITHM, keyPair, null );
	}
	
	public static SignatureUtil getSignatureInstance( KeyPair keyPair, String provider ) {
		return getSignatureInstance( DEFAULT_SIGN_ALGORITHM, keyPair, provider );
	}
	
	public static SignatureUtil getSignatureInstance( String algorithm, KeyPair keyPair, String provider ) {
		return getSignatureInstance( algorithm, keyPair, new SecureRandom(), provider );
	}
	
	public static SignatureUtil getSignatureInstance( String algorithm, KeyPair keyPair, SecureRandom sRandom, String provider ) {
		return new SignatureUtil( algorithm, keyPair, sRandom, provider );
	}
	
	public byte[] sign( byte[] data ) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
		Signature signature = this.getSignature();
		
		signature.initSign( keyPair.getPrivate(), sRandom );
		signature.update( data );

		return signature.sign();
	}
	
	public boolean verify( byte[] data, byte[] digitalSignature ) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
		Signature signature = this.getSignature();
		
		signature.initVerify( keyPair.getPublic() );
		signature.update( data );
		
		return signature.verify( digitalSignature );
	}
	
	private Signature getSignature() throws NoSuchAlgorithmException {
		Signature signature;
		
		if ( provider != null ) {
			signature = Signature.getInstance( this.algorithm, provider );
		} else {
			signature = Signature.getInstance( this.algorithm );
		}
		
		return signature;
	}
}
