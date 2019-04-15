package jce;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import jce.util.KeyStoreUtil;
import sun.security.pkcs12.PKCS12KeyStore;

public class Sample015pkcs12 {
	
	private static String password = "kmspbkdf";
	
	public static void main( String[] args ) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableEntryException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidParameterSpecException, InvalidKeySpecException { 
		KeyStoreUtil keyStoreUtil = KeyStoreUtil.getInstance( "change".toCharArray(), "./jks/keystore.jks" );
		KeyPair kPair = keyStoreUtil.getKeyPair( "kms".toCharArray(), "kms" );
		Certificate[] certs = keyStoreUtil.getCertificateChain( "kms".toCharArray(), "kms" );
		
		byte[] encryptedPrivKey = enc( kPair.getPrivate().getEncoded() );
		
		storePkcs12( encryptedPrivKey, certs );
	}
	
	//https://www.pixelstech.net/article/1420427307-Different-types-of-keystore-in-Java----PKCS12
	public static void storePkcs12( byte[] encryptedPrivKey, Certificate[] certs ) throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException, CertificateException, IOException {
		PKCS12KeyStore pkcs12kStr = new PKCS12KeyStore();
		
		pkcs12kStr.engineSetKeyEntry( "kms", encryptedPrivKey, certs );
		
		FileOutputStream keyStoreOutputStream = null;
		
		try {
			keyStoreOutputStream = new FileOutputStream( "jks/kms.p12" );

			pkcs12kStr.engineStore( keyStoreOutputStream, "kms".toCharArray() );
		} finally {
			keyStoreOutputStream.close();
		}
	}
	
	public static byte[] enc( byte[] encodedprivkey ) throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException, NoSuchPaddingException, CertificateException, IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, InvalidParameterSpecException, InvalidKeySpecException {
		String MYPBEALG = "PBEWithSHA1AndDESede";
		byte[] salt;
		int count;
		
		count = 20;// hash iteration count
		SecureRandom random = new SecureRandom();
		salt = new byte[8];
		random.nextBytes( salt );
		
		// Create PBE parameter set
		PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, count);
		PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
		SecretKeyFactory keyFac = SecretKeyFactory.getInstance(MYPBEALG);
		SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
		
		Cipher pbeCipher = Cipher.getInstance( MYPBEALG );
		
		// Initialize PBE Cipher with key and parameters
		pbeCipher.init( Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec );
		
		// Encrypt the encoded Private Key with the PBE key
		byte[] ciphertext = pbeCipher.doFinal( encodedprivkey );
		
		// Now construct  PKCS #8 EncryptedPrivateKeyInfo object
		AlgorithmParameters algParams = AlgorithmParameters.getInstance( MYPBEALG );
		algParams.init( pbeParamSpec );
		
		EncryptedPrivateKeyInfo encInfo = new EncryptedPrivateKeyInfo( algParams, ciphertext);
		
		// and here we have it! a DER encoded PKCS#8 encrypted key!
		return encInfo.getEncoded();
	}
}
