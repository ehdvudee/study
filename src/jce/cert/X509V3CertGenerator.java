package jce.cert;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

import sun.security.x509.AlgorithmId;
import sun.security.x509.CertificateAlgorithmId;
import sun.security.x509.CertificateExtensions;
import sun.security.x509.CertificateSerialNumber;
import sun.security.x509.CertificateValidity;
import sun.security.x509.CertificateVersion;
import sun.security.x509.CertificateX509Key;
import sun.security.x509.GeneralNames;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;

public abstract class X509V3CertGenerator {
	
	private KeyPair subject;
	private GeneralNames subjectAltName;
	private PrivateKey issuer;
	private long validity;
	private String algorithm;
	private X509Certificate issuerCert;
	private String dn;
	private BigInteger serialNumber;
	
	// 설정값으로 할당 필요
	public static final String TEST_CN = "testCN";
	
	public X509V3CertGenerator( CertGeneratorFactory.Builder builder ) {
		subject = builder.getSubject();
		subjectAltName = builder.getSubjectAltName();
		issuer = builder.getIssuer();
		validity = builder.getValidity();
		algorithm = builder.getAlgorithm();
		issuerCert = builder.getIssuerCert();
		dn = builder.getDn();
		serialNumber = builder.getSerialNumber();
	}
	
	public X509Certificate getIssuerCert() { return issuerCert; } 
	public GeneralNames getSubjectAltName() { return subjectAltName; }
	
	public X509Certificate generateCertificate() throws IOException, CertificateException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException {
		X509CertInfo info = new X509CertInfo();
		
		Date from = new Date();
		Date to = new Date( from.getTime() + validity * 24L * 60L * 60L * 1000L );
		CertificateValidity interval = new CertificateValidity( from, to );

//		BigInteger serialNumber = new BigInteger( 64, new SecureRandom() );
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
		certificate.sign( issuer, algorithm );

		sigAlgId = (AlgorithmId) certificate.get( X509CertImpl.SIG_ALG );
		info.set( CertificateAlgorithmId.NAME + "." + CertificateAlgorithmId.ALGORITHM, sigAlgId );
		
		CertificateExtensions exts = generateCertificateExtensions( subject.getPublic() );
		
		if( exts != null ) {
			info.set( X509CertInfo.EXTENSIONS, exts );
		}
		
		certificate = new X509CertImpl( info );
		certificate.sign( issuer, algorithm );

		if ( issuerCert != null ) {
			certificate.verify( issuerCert.getPublicKey() );
		} 
		
		return certificate;
	}
	
	public abstract CertificateExtensions generateCertificateExtensions( PublicKey publickey ) throws IOException;
	
}
