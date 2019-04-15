package jce;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.xml.bind.DatatypeConverter;

import jce.util.KeyStoreUtil;

public class Sample010GeneratePriKeyWithPw {
	
	private static byte[] encryptedPkcs8;
	private static String password = "kmspbkdf";
	private static byte[] encodedPrivKey;
	
	public static void main( String[] args ) throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException, CertificateException, IOException, InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidParameterSpecException {
		KeyStoreUtil keyStoreUtil = KeyStoreUtil.getInstance( "change".toCharArray(), "./jks/keystore.jks" );
		
		KeyPair kPair = keyStoreUtil.getKeyPair( "kms".toCharArray(), "kms" );
		encodedPrivKey = kPair.getPrivate().getEncoded();
		
		System.out.println( "원본");
		System.out.println( DatatypeConverter.printBase64Binary( encodedPrivKey ) );
		
		enc( encodedPrivKey );
		dec();
		
		OutputStream os = new FileOutputStream( "./jks/kms_cert_priWithPw.p8key" );
		os.write( encryptedPkcs8 );
		os.close();
		
	}
	
	public static void enc( byte[] encodedprivkey ) throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException, NoSuchPaddingException, CertificateException, IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, InvalidParameterSpecException, InvalidKeySpecException {
		// code's comment referenced by 
		// https://stackoverflow.com/questions/5127379/how-to-generate-a-rsa-keypair-with-a-privatekey-encrypted-with-password
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
		encryptedPkcs8 = encInfo.getEncoded();
		
	}
	
	public static void dec() throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException {
		//https://www.javatips.net/api/javax.crypto.encryptedprivatekeyinfo
		EncryptedPrivateKeyInfo encInfo = new EncryptedPrivateKeyInfo( encryptedPkcs8 );
		
		PBEKeySpec pbeSpec = new PBEKeySpec( password.toCharArray() );
		SecretKeyFactory keyFac = SecretKeyFactory.getInstance( encInfo.getAlgName() );
		SecretKey pbeKey = keyFac.generateSecret( pbeSpec );
		
		Cipher cipher = Cipher.getInstance( encInfo.getAlgName() );
		cipher.init( Cipher.DECRYPT_MODE, pbeKey, encInfo.getAlgParameters() );
		PKCS8EncodedKeySpec keySpec = encInfo.getKeySpec( cipher );

		if ( !MessageDigest.isEqual( encodedPrivKey, keySpec.getEncoded() ) ) {
			System.out.println( "틀렸음 ㅅㄱ");
		} 
		
		System.out.println( "원본 -> ENC -> DEC(원본)");
		System.out.println( DatatypeConverter.printBase64Binary( keySpec.getEncoded() ) );
		
	}
}
