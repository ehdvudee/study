package jce.cert;

import java.io.IOException;
import java.security.PublicKey;

import sun.security.x509.BasicConstraintsExtension;
import sun.security.x509.CertificateExtensions;
import sun.security.x509.GeneralName;
import sun.security.x509.GeneralNames;
import sun.security.x509.IssuerAlternativeNameExtension;
import sun.security.x509.KeyIdentifier;
import sun.security.x509.KeyUsageExtension;
import sun.security.x509.SubjectAlternativeNameExtension;
import sun.security.x509.SubjectKeyIdentifierExtension;
import sun.security.x509.X500Name;

public class RootCertGenerator extends X509V3CertGenerator {

	public RootCertGenerator( CertGeneratorFactory.Builder builder ) {
		super(builder);
	}

	@Override
	public CertificateExtensions generateCertificateExtensions( PublicKey subjectPublickey ) throws IOException {

		KeyUsageExtension keyUsage = new KeyUsageExtension(); 
		keyUsage.set( KeyUsageExtension.KEY_CERTSIGN, true ); 

		CertificateExtensions exts = new CertificateExtensions();

		GeneralNames issuerAltName = new GeneralNames();
		issuerAltName.add( new GeneralName( new X500Name( "CN = " + TEST_CN ) ) );
		exts.set( IssuerAlternativeNameExtension.NAME,
				new IssuerAlternativeNameExtension( false, issuerAltName ) );

		GeneralNames subjectAltName = new GeneralNames();
		subjectAltName.add( new GeneralName( new X500Name( "CN = " + TEST_CN ) ) );
		exts.set( SubjectAlternativeNameExtension.NAME,
				new SubjectAlternativeNameExtension( false, subjectAltName ) );

		exts.set( SubjectKeyIdentifierExtension.NAME,
				new SubjectKeyIdentifierExtension(
						new KeyIdentifier( subjectPublickey ).getIdentifier() ) );

		exts.set( BasicConstraintsExtension.NAME,
				new BasicConstraintsExtension( true, -1 ) );

		exts.set( KeyUsageExtension.NAME, keyUsage ); 

		return exts;

	}
}
