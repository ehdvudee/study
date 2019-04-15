package jce;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import jce.util.KeyStoreUtil;

public class Sample009KeyStore {
	public static void main( String[] args ) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, InvalidKeySpecException, UnrecoverableEntryException {
		
		File key = new File( "./jks/kms_cert_pri.p8key");
		File cert = new File( "./jks/kms_cert.cer" );
		File caCert = new File( "./jks/ca_cert.cer" );
		File rootCert = new File( "./jks/root_cert.cer" );
		
		InputStream isCert = new BufferedInputStream( new FileInputStream( cert ) );
		InputStream isCaCert = new BufferedInputStream( new FileInputStream( caCert ) );
		InputStream isRootrt = new BufferedInputStream( new FileInputStream( rootCert ) );
		InputStream isKey = new BufferedInputStream( new FileInputStream( key ) );
		
		byte[] bKey = new byte[(int)key.length()];
		try {
			isKey.read( bKey );
		} finally {
			if ( isKey != null ) isKey.close();
		}
		
		PrivateKey objKey = KeyFactory.getInstance( "RSA" ).generatePrivate( new PKCS8EncodedKeySpec( bKey ) );
		X509Certificate objCert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate( isCert );
		X509Certificate objCaCert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate( isCaCert );
		X509Certificate objRootCert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate( isRootrt );
		
		List <X509Certificate> chain = new ArrayList<X509Certificate>();
		
		// cert[0] 부터 위로 root 방향
		chain.add( objCert );
		chain.add( objCaCert );
		chain.add( objRootCert );
		
		KeyStoreUtil keyStoreUtil = KeyStoreUtil.getInstance( "change".toCharArray(), "./jks/keystore.jks" );
		keyStoreUtil.setKeyPair( new KeyPair( objCert.getPublicKey(), objKey ), chain.toArray(new Certificate[]{}), "kms", "kms".toCharArray() );
		keyStoreUtil.storeKeyStoreFile( "change".toCharArray(), "./jks/keystore.jks" );
		
		KeyPair sKey = keyStoreUtil.getKeyPair( "kms".toCharArray(), "kms" );
		
		System.out.println( DatatypeConverter.printBase64Binary( objKey.getEncoded() ) );
		System.out.println( DatatypeConverter.printBase64Binary( sKey.getPrivate().getEncoded() ) );
	}
}
