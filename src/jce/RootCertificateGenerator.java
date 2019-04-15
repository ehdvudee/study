package jce;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import jce.util.KeyStoreUtil;
import sun.security.x509.AlgorithmId;
import sun.security.x509.CertificateAlgorithmId;
import sun.security.x509.CertificateSerialNumber;
import sun.security.x509.CertificateValidity;
import sun.security.x509.CertificateVersion;
import sun.security.x509.CertificateX509Key;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;

public class RootCertificateGenerator extends X509VerCertificateGenerator {

	public static void main( String[] args ) throws InvalidKeyException, CertificateException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException, IOException, KeyStoreException {
		
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance( "DSA" );
		keyPairGenerator.initialize( 2048 );
		KeyPair root = keyPairGenerator.generateKeyPair();
		KeyPair ca = keyPairGenerator.generateKeyPair();
		KeyPair client = keyPairGenerator.generateKeyPair();
		
		System.out.println( "root key hex string");
		System.out.println( convertByteArrayToHexString( root.getPublic().getEncoded() ) );
		System.out.println( convertByteArrayToHexString( root.getPrivate().getEncoded() ) );
		System.out.println( "root key hex string");
		RootCertificateGenerator genCert = new RootCertificateGenerator( "CN=SHIN_CA, OU=MagicKMS, O=Dreamsecurity, C=KR" );
		X509Certificate cert = genCert.generateCertificate( root, 60, "SHA256withDSA" );
		System.out.println(" root cert hex string");
		System.out.println( convertByteArrayToHexString( cert.getEncoded() ) );
		System.out.println(" root cert hex string");
		
		RootCertificateGenerator genCert2 = new RootCertificateGenerator( "CN=test,OU=test,O=Dreamsecurityinc,C=KR" );
		X509Certificate cert2 = genCert2.generateCertificate( new KeyPair( ca.getPublic(), root.getPrivate() ), 60, "SHA256withDSA", cert );
	
		
		RootCertificateGenerator genCert3 = new RootCertificateGenerator( "CN=test123,OU=test123,O=Dreamsecurityinc123,C=KR" );
		X509Certificate cert3 = genCert3.generateCertificate( new KeyPair( client.getPublic(), ca.getPrivate() ), 60, "SHA256withDSA", cert2 );
		
		
		List <X509Certificate> chain = new ArrayList<X509Certificate>();
		
		chain.add( cert3 );
		chain.add( cert2 );
		chain.add( cert );

		KeyStoreUtil.createNewKeyStore( "dosjwlvk1".toCharArray(), "./ks/keystore.jks");
		
		KeyStoreUtil keyStoreUtil = KeyStoreUtil.getInstance( "dosjwlvk1".toCharArray(), "./ks/keystore.jks" );
		keyStoreUtil.setKeyPair( client, chain.toArray(new Certificate[]{}), "chaintest", "chaintest".toCharArray() );
		keyStoreUtil.storeKeyStoreFile( "dosjwlvk1".toCharArray(), "./ks/newkeystore.jks");
		
		CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
		CertPath certPath = certificateFactory.generateCertPath( chain );
		
		System.out.println(certPath);
		System.out.println(Arrays.toString(chain.toArray()));
		
		
		FileOutputStream localFileOutputStream = null;
		try {
			localFileOutputStream = new FileOutputStream( "./ks/rootCert.der" );
			localFileOutputStream.write( cert.getEncoded() );
			localFileOutputStream.close();		
			localFileOutputStream = new FileOutputStream( "./ks/caCert.der" );
			localFileOutputStream.write( cert2.getEncoded() );
			localFileOutputStream.close();		
			localFileOutputStream = new FileOutputStream( "./ks/clientCert.der" );
			localFileOutputStream.write( cert3.getEncoded() );
		} finally {
			if (localFileOutputStream != null) localFileOutputStream.close();		
		}
	}

	public RootCertificateGenerator( String dn ) {
		super( dn );
	}
	
	public X509Certificate generateCertificate( KeyPair subject, int validity, String algorithm ) throws IOException, CertificateException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException {
		return this.generateCertificate( subject, validity, algorithm, null );
	}
	
	public X509Certificate generateCertificate( KeyPair subject, int validity, String algorithm, X509Certificate issuerCert ) throws IOException, CertificateException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException {
		PrivateKey privateKey = subject.getPrivate();

		X509CertInfo info = new X509CertInfo();
		
		Date from = new Date();
		Date to = new Date( from.getTime() + validity * 24L * 60L * 60L * 1000L );
		CertificateValidity interval = new CertificateValidity( from, to );

		BigInteger serialNumber = new BigInteger( 64, new SecureRandom() );
		AlgorithmId sigAlgId = new AlgorithmId( AlgorithmId.sha256WithRSAEncryption_oid );
		
		if ( issuerCert != null ) {
			info.set( X509CertInfo.ISSUER, new X500Name( issuerCert.getSubjectDN().getName() ) );
			info.set( X509CertInfo.SUBJECT, new X500Name( dn ) );
		} else {
			info.set( X509CertInfo.ISSUER, new X500Name( dn ) );
			info.set( X509CertInfo.SUBJECT, new X500Name( dn ) );
		}
		
		info.set( X509CertInfo.VALIDITY, interval );
		info.set( X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber( serialNumber ) );
		info.set( X509CertInfo.KEY, new CertificateX509Key( subject.getPublic() ) );
		info.set( X509CertInfo.VERSION, new CertificateVersion( CertificateVersion.V3 ) );
		info.set( X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId( sigAlgId ) );
		
		X509CertImpl certificate = new X509CertImpl( info );
		certificate.sign( privateKey, algorithm );

		sigAlgId = (AlgorithmId) certificate.get( X509CertImpl.SIG_ALG );
		info.set( CertificateAlgorithmId.NAME + "." + CertificateAlgorithmId.ALGORITHM, sigAlgId );
		certificate = new X509CertImpl( info );
		certificate.sign( privateKey, algorithm );

		if ( issuerCert != null ) {
			certificate.verify( issuerCert.getPublicKey() );
		} 
		
		return certificate;
	}
	
	private static String convertByteArrayToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for(byte b : bytes) {
			sb.append(String.format("%02x", b&0xff));
		}
		return sb.toString();
	}
}
