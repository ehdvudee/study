package jce.cert.sample;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import jce.cert.CaCertGenerator;
import jce.cert.CertGeneratorFactory;
import jce.cert.RootCertGenerator;
import jce.cert.UserCertGenerator;
import jce.util.DataTypeUtil;
import org.junit.Test;

public class TestDrive {

	@Test
	public void main() throws NoSuchAlgorithmException, InvalidKeyException, CertificateException, NoSuchProviderException, SignatureException, IOException {
		
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance( "RSA" );
		keyPairGenerator.initialize( 2048 );

		KeyPair rootKeyPair = keyPairGenerator.generateKeyPair();
		KeyPair caKeyPair = keyPairGenerator.generateKeyPair();
		KeyPair eeCertKeyPair = keyPairGenerator.generateKeyPair();
		
		X509Certificate rootCert =  new CertGeneratorFactory().getBuilder()
				.subject( rootKeyPair )
				.validity( 365 )
				.algorithm( "SHA256withRSA" )
				.issuer( rootKeyPair.getPrivate() )
				.dn( "CN = DigiCert High Assurance EV Root CA OU = www.digicert.com O = DigiCert Inc C = US" )
				.serialNumber( BigInteger.ONE )
				.type( RootCertGenerator.class )
				.build()
				.generateCertificate();
		
		X509Certificate caCert = new CertGeneratorFactory().getBuilder()
				.subject( caKeyPair )
				.validity( 365 )
				.algorithm( "SHA256withRSA" )
				.issuer( rootKeyPair.getPrivate() )
				.issuerCert( rootCert )
				.dn( "CN = DigiCert SHA2 High Assurance Server CA OU = www.digicert.com O = DigiCert Inc C = US" )
				.serialNumber( BigInteger.valueOf( 1L ) )
				.type( CaCertGenerator.class )
				.build()
				.generateCertificate();
		
		List<String> dnsName = new ArrayList<>();

		dnsName.add( "*.wikipedia.org" );
		dnsName.add( "wikipedia.org" );
		dnsName.add( "*.m.wikipedia.org" );
		dnsName.add( "*.zero.wikipedia.org" );
		
		X509Certificate eeCert = new CertGeneratorFactory().getBuilder()
				.subject( eeCertKeyPair )
				.validity( 365 )
				.algorithm( "SHA256withRSA" )
				.issuer( caKeyPair.getPrivate() )
				.issuerCert( caCert )
				.dn( "CN = *.wikipedia.org O = Wikimedia Foundation\\, Inc. L = San Francisco S = California C = US" )
				.serialNumber( BigInteger.ONE )
				.subjectAltName( dnsName )
				.type( UserCertGenerator.class )
				.build()
				.generateCertificate();
		
		System.out.println( "root pub, pri, cert" );
		System.out.println( DataTypeUtil.convertByteArrayToHexString( rootKeyPair.getPublic().getEncoded() ) );
		System.out.println( DataTypeUtil.convertByteArrayToHexString( rootKeyPair.getPrivate().getEncoded() ) );
		System.out.println( DataTypeUtil.convertByteArrayToHexString( rootCert.getEncoded() ) );
		System.out.println( "inter pub, pri, cert" );
		System.out.println( DataTypeUtil.convertByteArrayToHexString( caKeyPair.getPublic().getEncoded() ) );
		System.out.println( DataTypeUtil.convertByteArrayToHexString( caKeyPair.getPrivate().getEncoded() ) );
		System.out.println( DataTypeUtil.convertByteArrayToHexString( caCert.getEncoded() ) );
		
		FileOutputStream localFileOutputStream = null;
		try {
			localFileOutputStream = new FileOutputStream( "./doc/rootCert.der" );
			localFileOutputStream.write( rootCert.getEncoded() );
			localFileOutputStream.close();
			
			localFileOutputStream = new FileOutputStream( "./doc/caCert.der" );
			localFileOutputStream.write( caCert.getEncoded() );
			localFileOutputStream.close();
			
			localFileOutputStream = new FileOutputStream( "./doc/eeCert.der" );
			localFileOutputStream.write( eeCert.getEncoded() );
		} finally {
			if (localFileOutputStream != null) localFileOutputStream.close();		
		}
	}
}
