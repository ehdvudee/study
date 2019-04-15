package jce.jceks;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Sample001 {
	public static void main( String[] args ) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
		storeSecretKey();
		loadSecretKey();
	}
	
	public static void loadSecretKey() throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {
		KeyStore keyStore = KeyStore.getInstance( "JCEKS" );
		keyStore.load( new FileInputStream( "jceks/java.jceks" ), "password".toCharArray() );

		SecretKey key = (SecretKey) keyStore.getKey( "secret", "password".toCharArray() );
		
		System.out.println( Arrays.toString( key.getEncoded() ) );
	}
	
	public static void storeSecretKey() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore keyStore = KeyStore.getInstance( "JCEKS" );
		// change defaultType jks -> pkcs12 in java 9
		System.out.println( KeyStore.getDefaultType() );
		
		keyStore.load( null, null );
		
		KeyGenerator keyGen = KeyGenerator.getInstance( "AES" );
		keyGen.init( 256 );
		SecretKey key = keyGen.generateKey();
		
		keyStore.setKeyEntry( "secret", key, "password".toCharArray(), null );
		keyStore.store( new FileOutputStream( "jceks/java.jceks" ), "password".toCharArray() );
	}
}
