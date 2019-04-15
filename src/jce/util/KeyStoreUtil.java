package jce.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStore.SecretKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class KeyStoreUtil {

	private final KeyStore keyStore;
	private final String DEFAULT_SECRET_KEY_ALGORITHM = "AES";

	private KeyStoreUtil( KeyStore keyStore ) {
		this.keyStore = keyStore;
	}

	public static KeyStoreUtil getInstance( char[] password, String keyStorePath ) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		return getInstance( password, keyStorePath, KeyStore.getDefaultType() );
	}

	public static KeyStoreUtil getInstance( char[] password, String keyStorePath, String type ) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		return loadKeyStore( password, keyStorePath, type, null );	
	}

	public static KeyStoreUtil getInstance( char[] password, String keyStorePath, String type, String provider ) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		if ( provider == null ) {
			return loadKeyStore( password, keyStorePath, type, null );	
		} else {
			return getInstance( password, keyStorePath, type,  Security.getProvider( provider ) );
		}
	}

	public static KeyStoreUtil getInstance( char[] password, String keyStorePath, Provider provider ) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		return getInstance( password, keyStorePath, KeyStore.getDefaultType(), provider );
	}

	public static KeyStoreUtil getInstance( char[] password, String keyStorePath, String type, Provider provider ) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		return loadKeyStore( password, keyStorePath, type, provider );
	}
	
	public static KeyStore createNewKeyStore( char[] password ) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore ks = KeyStore.getInstance( KeyStore.getDefaultType() );
		
		ks.load( null, password );
		
		return ks;
	}
	
	public static void createNewKeyStore( char[] password, String createPath ) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore ks = createNewKeyStore( password );
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream( createPath );
			
			ks.store(fos, password );
		} finally {
			if ( fos != null ) fos.close();
		}
	}
	
	private static KeyStoreUtil loadKeyStore( char[] password, String keyStorePath, String type, Provider provider ) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore keyStore;

		if ( provider != null ) {
			keyStore = KeyStore.getInstance( type, provider );			
		} else {
			keyStore = KeyStore.getInstance( type );
		}

		InputStream keyStoreData = null;

		try {
			keyStoreData = new FileInputStream( keyStorePath );

			keyStore.load( keyStoreData, password );
		} finally {
			keyStoreData.close();
		}

		return new KeyStoreUtil( keyStore );
	}

	private KeyStore.PrivateKeyEntry getPrivateKeyEntry( char[] password, String alias ) throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException {
		KeyStore.ProtectionParameter enrtyPassword = new KeyStore.PasswordProtection( password );

		return (PrivateKeyEntry) keyStore.getEntry( alias, enrtyPassword );
	}

	private KeyStore.SecretKeyEntry getSecretKeyEntry( char[] password, String alias ) throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException {
		KeyStore.ProtectionParameter enrtyPassword = new KeyStore.PasswordProtection( password );

		return (SecretKeyEntry) keyStore.getEntry( alias, enrtyPassword );
	}

	public SecretKey getSecretKey( char[] password, String alias ) throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException {
		SecretKeyEntry secretKeyEntry = this.getSecretKeyEntry( password, alias );

		return secretKeyEntry.getSecretKey();
	}

	public Certificate getCertificate( char[] password, String alias ) throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException {
		PrivateKeyEntry privateKeyEntry = this.getPrivateKeyEntry( password, alias );

		return privateKeyEntry.getCertificate();
	}

	public Certificate[] getCertificateChain( char[] password, String alias ) throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException {
		PrivateKeyEntry privateKeyEntry = this.getPrivateKeyEntry( password, alias );

		return privateKeyEntry.getCertificateChain();
	}
	
	public PublicKey getPublicKey( char[] password, String alias ) throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException {
		PrivateKeyEntry privateKeyEntry = this.getPrivateKeyEntry( password, alias );

		return privateKeyEntry.getCertificate().getPublicKey();
	}

	public PrivateKey getPrivateKey( char[] password, String alias ) throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException {
		PrivateKeyEntry privateKeyEntry = this.getPrivateKeyEntry( password, alias );

		return privateKeyEntry.getPrivateKey();
	}

	public KeyPair getKeyPair( char[] password, String alias ) throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException {
		PrivateKeyEntry privateKey = this.getPrivateKeyEntry( password, alias );

		return new KeyPair( privateKey.getCertificate().getPublicKey(), privateKey.getPrivateKey() );
	}

	public void setSecretKey( byte[] bytes, String alias, char[] password ) throws KeyStoreException {
		this.setSecretKey( new SecretKeySpec( bytes, DEFAULT_SECRET_KEY_ALGORITHM ), alias, password );
	}

	public void setSecretKey( byte[] bytes, String algorithm, String alias, char[] password ) throws KeyStoreException {
		this.setSecretKey( new SecretKeySpec( bytes, algorithm ), alias, password );
	}

	public void setSecretKey( SecretKey key, String alias, char[] password ) throws KeyStoreException {
		KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry( key );

		KeyStore.ProtectionParameter entryPassword = new KeyStore.PasswordProtection( password );

		keyStore.setEntry( alias, secretKeyEntry, entryPassword );
	}

	public void setKeyPair( byte[] privateKey, byte[] publicKey, Certificate[] certChain, String algorithm, String alias, char[] password ) throws InvalidKeySpecException, NoSuchAlgorithmException, KeyStoreException, CertificateException {
		PublicKey pubKey = KeyFactory.getInstance( algorithm ).generatePublic( new X509EncodedKeySpec( publicKey ) );
		PrivateKey priKey = KeyFactory.getInstance( algorithm ).generatePrivate( new PKCS8EncodedKeySpec( privateKey ) );

		this.setKeyPair( new KeyPair( pubKey, priKey ), certChain, alias, password );
	}

	public void setKeyPair( PrivateKey privateKey, PublicKey publicKey, Certificate[] certChain, String alias, char[] password ) throws KeyStoreException, CertificateException {
		this.setKeyPair( new KeyPair( publicKey, privateKey ), certChain, alias, password );
	}

	public void setKeyPair( KeyPair keyPair, Certificate[] certChain, String alias, char[] password ) throws KeyStoreException, CertificateException {

		KeyStore.PrivateKeyEntry privateKeyEntry = new KeyStore.PrivateKeyEntry( keyPair.getPrivate(), certChain );

		KeyStore.ProtectionParameter entryPassword = new KeyStore.PasswordProtection( password );

		keyStore.setEntry( alias, privateKeyEntry, entryPassword );
	}
	
	public void storeKeyStoreFile( char[] password, String filPathName ) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		FileOutputStream keyStoreOutputStream = null;

		try {
			keyStoreOutputStream = new FileOutputStream( filPathName );

			keyStore.store(keyStoreOutputStream, password);
		} finally {
			keyStoreOutputStream.close();
		}
	}	
}

