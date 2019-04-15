package jce;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class Sample006Signature {
	public static void main( String[] args ) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance( "DSA" );
		KeyPair keyPair2 = kpg.generateKeyPair();
		byte[] publicKeyBytes = keyPair2.getPublic().getEncoded();
		byte[] privateKeyBytes = keyPair2.getPrivate().getEncoded();
		byte[] data = "awfeihaewfi".getBytes();
		
		PublicKey publicKey = KeyFactory.getInstance("DSA").generatePublic( new X509EncodedKeySpec( publicKeyBytes ) );
		PrivateKey privateKey = KeyFactory.getInstance("DSA").generatePrivate( new PKCS8EncodedKeySpec( privateKeyBytes ) );
		
		KeyPair keyPair = new KeyPair( publicKey, privateKey );
		Signature signature = Signature.getInstance( "SHA256WithDSA" );
		
		signature.initSign( privateKey );
		signature.update( data );
		byte[] digitalSignature = signature.sign();
	
		signature.initVerify( keyPair.getPublic() );
		signature.update( data );
		System.out.println( signature.verify( digitalSignature ) );
		
	}
}
